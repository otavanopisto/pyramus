package fi.otavanopisto.pyramus.domainmodel.worklist;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

@Converter
public class WorklistItemEditableFieldsConverter implements AttributeConverter<Set<WorklistItemEditableFields>, String> {
  
  public String convertToDatabaseColumn(Set<WorklistItemEditableFields> editableFields) {
    if (editableFields == null || editableFields.size() == 0) {
      return null;
    }
    return String.join(",", editableFields.stream().map(Object::toString).collect(Collectors.toList()));
  }

  public Set<WorklistItemEditableFields> convertToEntityAttribute(String dbData) {
    Set<WorklistItemEditableFields> result = new HashSet<>();
    if (!StringUtils.isEmpty(dbData)) {
      String[] editableFields = dbData.split(",");
      for (String editableField : editableFields) {
        result.add(WorklistItemEditableFields.valueOf(editableField));
      }
    }
    return result;
  }

}
