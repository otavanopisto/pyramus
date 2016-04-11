package fi.otavanopisto.pyramus.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.validation.ConstraintViolation;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.util.dataimport.DataImportUtils;
import fi.otavanopisto.pyramus.util.dataimport.ValueInterpreter;

public class DataImporter {
  
  public void importDataFromStream(InputStream stream, Collection<String> entities) {
    try {
      DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
      Document initialDataDocument = db.parse(stream);
      importDataFromDocument(initialDataDocument, entities);
    } catch (ParserConfigurationException e) {
      throw new SmvcRuntimeException(e);
    } catch (SAXException e) {
      throw new SmvcRuntimeException(e);
    } catch (IOException e) {
      throw new SmvcRuntimeException(e);
    }
  }
  
  @SuppressWarnings({ "rawtypes" })
  public void importDataFromDocument(Document initialDataDocument, Collection<String> entities) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    try {
      NodeIterator storeIterator = XPathAPI.selectNodeIterator(initialDataDocument.getDocumentElement(), "store");      
      Node storeNode;
      while ((storeNode = storeIterator.nextNode()) != null) {
        if (storeNode instanceof Element) {
          Element storeElement = (Element) storeNode;
          String variableName = storeElement.getAttribute("storeVariable");
          String hql = storeElement.getAttribute("hql");
          Object result;
          
          System.out.println("Processing " + variableName);
          
          if (!StringUtils.isBlank(hql)) {
            result = systemDAO.createJPQLQuery(hql).getSingleResult();
            
            if (result == null)
              throw new SmvcRuntimeException(new Exception("storeVariable hql=\"" + hql + "\" returned null"));
          } else {
            Class variableClass;
            try {
              variableClass = Class.forName(storeElement.getAttribute("class"));
              StringBuilder jpqlBuilder = new StringBuilder();
              jpqlBuilder.append("from ");
              jpqlBuilder.append(variableClass);
              NodeList criteriaList = storeElement.getChildNodes();

              if (criteriaList.getLength() > 0) {
                jpqlBuilder.append(" where ");
                for (int i = 0; i < criteriaList.getLength(); i++) {
                  if (criteriaList.item(i) instanceof Element) {
                    Element criteriaElement = (Element) criteriaList.item(i);
                    StoreVariableCriteria criteria = StoreVariableCriteria.getCriteria(criteriaElement.getNodeName());
                    switch (criteria) {
                      case Equals:
                        String property = criteriaElement.getAttribute("name");
                        String value = ((Text) criteriaElement.getFirstChild()).getData();
                        jpqlBuilder.append(property + " = " + value);
                      break;
                    }
                  }
                  
                  if (i < (criteriaList.getLength() - 1)) {
                    jpqlBuilder.append(" AND ");
                  }
                }
              }
              
              result = systemDAO.createJPQLQuery(jpqlBuilder.toString()).getSingleResult();
              
              if (result == null)
                throw new SmvcRuntimeException(new Exception("storeVariable class=\"" + variableClass.getName() + "\" returned null"));
            } catch (ClassNotFoundException e) {
              throw new SmvcRuntimeException(e);
            }  
          }
          
          System.out.println("Storing " + variableName);
          storeValue(variableName, getPojoId(result));
        }
      }
      
      NodeIterator entityIterator = XPathAPI.selectNodeIterator(initialDataDocument.getDocumentElement(), "entity");
      Node node;
      
      while ((node = entityIterator.nextNode()) != null) {
        if (node instanceof Element) {

          Element element = (Element) node;
          String entityPackageName = element.getAttribute("package");
          String entityClassName = element.getAttribute("class");
          String className = entityPackageName + '.' + entityClassName;
          
          if (entities == null ||entities.contains(className)) {
            Class<?> entityClass = Class.forName(entityPackageName + "." + entityClassName);
         
            NodeIterator entryIterator = XPathAPI.selectNodeIterator(element, "e");
            Element entry;
            
            while ((entry = (Element) entryIterator.nextNode()) != null) {
              processEntryTag(null, entityClass, entry, null);
              System.out.println("  >> added entity " + className);
            }
          }
        }
      }
    } catch (TransformerException e) {
      throw new SmvcRuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new SmvcRuntimeException(e);
    } 
  }

  private Object processEntryTag(Object parent, Class<?> entityClass, Element entry, Element parentListElement) { 
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    System.out.println("Processing entity: " + entityClass.getName());
    try {
      
      Constructor<?> defaultConstructor = entityClass.getDeclaredConstructor(new Class[] {});
      defaultConstructor.setAccessible(true);
      Object pojo = defaultConstructor.newInstance(new Object[] {});
      
      NamedNodeMap attributes = entry.getAttributes();
      for (int i = 0, len = attributes.getLength(); i < len; i++) {
        Node attribute = attributes.item(i);
        String propertyName = attribute.getNodeName();
        
        if (!"storeVariable".equals(propertyName)) {
          String propertyValue = attribute.getNodeValue();
          if (propertyValue.startsWith("{") && propertyValue.endsWith("}")) {
            propertyValue = String.valueOf(getStoredValue(propertyValue.substring(1, propertyValue.length() - 1)));
          }
          
          System.out.println("    >> property: " + propertyName + " to " + propertyValue);
          DataImportUtils.setValue(pojo, propertyName, propertyValue);
        }
      }
  
      NodeList entryChildren = entry.getChildNodes();
      for (int j = 0, len = entryChildren.getLength(); j < len; j++) {
        Node n = entryChildren.item(j);
        if (n instanceof Element) {
          Element element = (Element) n;
          String nodeName = element.getTagName();
          EntityDirective entityDirective = EntityDirective.getDirective(nodeName);
          if (entityDirective == null)
            throw new SmvcRuntimeException(new Exception("Unknown entity directive '" + nodeName + "'"));
          
          switch (entityDirective) {
            case Map:
              String mapName = element.getAttribute("name");
              String methodName = element.getAttribute("method");
              NodeIterator itemIterator = XPathAPI.selectNodeIterator(element, "item");
              Element itemElement;
              while ((itemElement = (Element) itemIterator.nextNode()) != null) {
                Element keyElement = (Element) XPathAPI.selectSingleNode(itemElement, "key");
                Element valueElement = (Element) XPathAPI.selectSingleNode(itemElement, "value");
                if (keyElement == null||valueElement == null)
                  throw new SmvcRuntimeException(new Exception("Malformed map item"));
                
                String keyValue = ((Text) keyElement.getFirstChild()).getData();
                String valueValue = ((Text) valueElement.getFirstChild()).getData();
                
                Field mapField = DataImportUtils.getField(pojo, mapName);
                ParameterizedType genericType = (ParameterizedType) mapField.getGenericType();
                Class<?> mapKeyTypeClass = (Class<?>) genericType.getActualTypeArguments()[0];
                Class<?> mapValueTypeClass = (Class<?>) genericType.getActualTypeArguments()[1];
                
                Object key;
                Object value;
                
                if (!isHibernateClass(mapKeyTypeClass)) {
                  ValueInterpreter<?> valueInterpreter = DataImportUtils.getValueInterpreter(mapKeyTypeClass);
                  if (valueInterpreter != null)
                    key = valueInterpreter.interpret(keyValue);
                  else
                    throw new SmvcRuntimeException(new Exception("Value interpreter for " + mapKeyTypeClass + " is not implemented yet"));
                } else {
                  key = getPojo(mapKeyTypeClass, keyValue);
                }
                
                if (!isHibernateClass(mapValueTypeClass)) {
                  ValueInterpreter<?> valueInterpreter = DataImportUtils.getValueInterpreter(mapValueTypeClass);
                  if (valueInterpreter != null)
                    value = valueInterpreter.interpret(valueValue);
                  else
                    throw new SmvcRuntimeException(new Exception("Value interpreter for " + mapValueTypeClass + " is not implemented yet"));
                } else {
                  value = getPojo(mapValueTypeClass, valueValue);
                }
                
                Class<?>[] params = {key.getClass(), value.getClass()};
                Object[] paramValues = {key, value}; 
                Method method = DataImportUtils.getMethod(pojo, methodName, params);
                method.invoke(pojo, paramValues);
              }
            break;
            case List:
              String listName = element.getAttribute("name");
              String listClass = element.getAttribute("class");
              Class<?> listTypeClass;
              
              if (StringUtils.isBlank(listClass)) {
                Field listField = DataImportUtils.getField(pojo, listName);
                ParameterizedType genericType = (ParameterizedType) listField.getGenericType();
                listTypeClass = (Class<?>) genericType.getActualTypeArguments()[0];
              } else {
                listTypeClass = Class.forName(listClass);
              }
              
              NodeIterator listEntryIterator = XPathAPI.selectNodeIterator(element, "e");
              
              Element listEntry;
              while ((listEntry = (Element) listEntryIterator.nextNode()) != null) {
                Object listEntity = processEntryTag(pojo, listTypeClass, listEntry, element);
                System.out.println("  >> added list entity " + listEntity.getClass().toString());
              }
            break;
            case Join:
              String className = element.getAttribute("class");
              
              String idField = ((Text) element.getFirstChild()).getData();
              
              Field joinField = DataImportUtils.getField(pojo, element.getAttribute("name"));
              
              if ("PARENT".equals(idField)) {
                String parentListMethod = parentListElement.getAttribute("method");
                AccessType parentListAccessType = AccessType.Field;
                
                if (!StringUtils.isEmpty(parentListMethod))
                  parentListAccessType = AccessType.Method;
                
                if (parentListAccessType == AccessType.Method) {
                  Class<?>[] params = {pojo.getClass()};
                  Method method = DataImportUtils.getMethod(parent, parentListMethod, params);
                  method.invoke(parent, pojo);
                } else {
                  DataImportUtils.setFieldValue(pojo, joinField, parent);
                }
              } else {
                Object joinedPojo = getPojo(className, idField);
                if (joinedPojo != null) {
                  DataImportUtils.setFieldValue(pojo, joinField, joinedPojo);
                } else {
                  throw new SmvcRuntimeException(new Exception(className + " #" + idField + " could not be found"));
                }
              }
            break;
          }
        }
      }
      
     
      Set<ConstraintViolation<Object>> constraintViolations = systemDAO.validateEntity(pojo);
      
	  if (constraintViolations.isEmpty()) {
	    systemDAO.persistEntity(pojo);
	  } else {
	    String message = "";
	    for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
	      message += constraintViolation.getMessage() + '\n';
	    }
	    
	    throw new SmvcRuntimeException(new Exception("Validation failure: " + message));
	  }
  
      Long id = getPojoId(pojo);
      
      if (id != null) {
        String storeVariable = entry.getAttribute("storeVariable");
        if (!StringUtils.isBlank(storeVariable)) {
          storeValue(storeVariable, id);
          System.out.println("  >> # " + id + " stored as " + storeVariable);
        }
      }
      
      return id;
    } catch (Exception e) {
      throw new SmvcRuntimeException(new Exception("Error while processing entity: " + entityClass.getName() + " " + e.getMessage(), e));
    }
  } 
  
  private Long getPojoId(Object pojo) {
    if (pojo != null) {
      try {
        Method getIdMethod = DataImportUtils.getMethod(pojo, "getId", new Class<?>[] {});
        if (getIdMethod != null) {
          return (Long) getIdMethod.invoke(pojo, new Object[] {});
        }
      } catch (Exception e) { 
        throw new SmvcRuntimeException(new Exception("getId failed for " + pojo.getClass().getName()));
      }
    }

    return null;
  }
  
  private Object getPojo(Class<?> clazz, String identifier) throws ClassNotFoundException {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    
    Long id;
    if (identifier.startsWith("{") && identifier.endsWith("}")) {
      id = getStoredValue(identifier.substring(1, identifier.length() - 1));
      if (id == null)
        throw new SmvcRuntimeException(new Exception("Could not resolve: " + identifier));
    } else  {
      id = NumberUtils.createLong(identifier);
    }
    
    return systemDAO.findEntityById(clazz, id);
  }
  
  private Object getPojo(String className, String identifier) throws ClassNotFoundException {
    Class<?> pojoClass = Class.forName(className);
    return getPojo(pojoClass, identifier);
  }
  
  private boolean isHibernateClass(Class<?> clazz) {
    return clazz.isAnnotationPresent(Entity.class);
  }
  
  private Long getStoredValue(String name) {
    return storedValues.get(name);
  }
  
  private void storeValue(String name, Long id) {
    storedValues.put(name, id);
  } 
  
  private enum AccessType {
    Field,
    Method
  }
  
  private Map<String, Long> storedValues = new HashMap<>();

  private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
  
  public enum EntityDirective {
    Join ("j"),
    List ("list"),
    Map  ("map");
    
    private EntityDirective(String name) {
      this.name = name;
    }
    
    private String name;
    
    public static EntityDirective getDirective(String name) {
      for (EntityDirective entityDirectiveNode : values()) {
        if (entityDirectiveNode.name.equals(name))
          return entityDirectiveNode;
      }
      
      return null;
    } 
  }
  
  public enum StoreVariableCriteria {
    Equals ("eq");
    
    private StoreVariableCriteria(String name) {
      this.name = name;
    }
    
    private String name;
    
    public static StoreVariableCriteria getCriteria(String name) {
      for (StoreVariableCriteria criteria : values()) {
        if (criteria.name.equals(name))
          return criteria;
      }
      
      return null;
    } 
  }
}
