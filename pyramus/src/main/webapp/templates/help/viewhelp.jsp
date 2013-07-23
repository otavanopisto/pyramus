<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
  <head> 
    <title><fmt:message key="help.viewHelp.pageTitle"/></title>
    <jsp:include page="/templates/generic/head_generic.jsp"></jsp:include>
    <jsp:include page="/templates/generic/jsonrequest_support.jsp"></jsp:include>
    
    <script type="text/javascript"">
      var selectedPageId = -1;
    
      function viewHelpPage(pageId) {
        var imageURL = '${pageContext.request.contextPath}/gfx/progress.gif';
        $('viewHelpContentsContainer').appendChild(new Element("div", {className: "helpPageLoading", style: "background-image: url('" + imageURL + "');"}));

        new Ajax.Request(GLOBAL_contextPath + '/help/viewhelppage.page?page=' + pageId, {  
          onSuccess: function(transport){
            $('viewHelpContentsContainer').update(transport.responseText);  
          },
          onFailure: function(transport) {
            throw new Error(transport.responseText);
          }
        });
      }
      
      function applySelectedPageStyle(pageContainer) {
        $$('.viewHelpPageNavigationSelectedItem').each(function (element) {
          element.removeClassName('viewHelpPageNavigationSelectedItem');
        });
        pageContainer.addClassName("viewHelpPageNavigationSelectedItem");
      } 
      
      function addSearchResults(parentElement, itemInfos) {
        for (var i = 0, l = itemInfos.length; i < l; i++) {
          var itemInfo = itemInfos[i];

          var id = itemInfo.id;
          var title = itemInfo.title;
          
          switch (itemInfo.type) {
            case 'page':
              var pageContainer = new Element("div", {className: "viewHelpPageNavigationItem viewHelpPageNavigationPageContainer"});
              var itemLink = new Element("a", {href: "javascript:void(null);", pageId: id}).update(title);
              var labelElement = new Element("div", {className: "viewHelpPageNavigationItemLabel"});
              
              labelElement.appendChild(itemLink);
              pageContainer.appendChild(labelElement);
              
              if (selectedPageId == id) {
                applySelectedPageStyle(pageContainer);
              }
              
              Event.observe(itemLink, "click", function (event) {
                var linkElement = Event.element(event);
                var pageContainer = $(linkElement.parentNode.parentNode);
                selectedPageId = linkElement.getAttribute('pageId');
                applySelectedPageStyle(pageContainer);
                viewHelpPage(selectedPageId);
              });
              
              parentElement.appendChild(pageContainer);
            break;
            case 'folder':
              var folderContainer = new Element("div", {className: "viewHelpPageNavigationItem viewHelpPageNavigationFolderContainer"});
              var itemSpan = new Element("span").update(title);
              var labelElement = new Element("div", {className: "viewHelpPageNavigationItemLabel"});
              var childrenElement = new Element("div", {className: "viewHelpPageNavigationFolderChildren"});
              
              labelElement.appendChild(itemSpan);
              folderContainer.appendChild(labelElement);
              folderContainer.appendChild(childrenElement);
              
              addSearchResults(childrenElement, itemInfo.pages);
              
              parentElement.appendChild(folderContainer);
            break;
          }
        }
      }
      
      function doSearch() {
        var imageURL = '${pageContext.request.contextPath}/gfx/progress.gif';
        $('viewHelpNavigationItemsContainer').appendChild(new Element("div", {className: "helpNavigationLoading", style: "background-image: url('" + imageURL + "');"}));

        JSONRequest.request("help/searchhelp.json", {
          parameters: {
            text: $('viewHelpNavigationSearch').value
          },
          onSuccess: function (jsonResponse) {
            $('viewHelpNavigationItemsContainer').update('');
            addSearchResults($('viewHelpNavigationItemsContainer'), jsonResponse.results);
          } 
        });
      }
      
      function onSearchKeyUp(event) {
        var inputElement = Event.element(event);
        
        inputElement._lastKeyPress = new Date().getTime(); 
        
        setTimeout(function () {
          var now = new Date().getTime();
          if ((now - inputElement._lastKeyPress) > 250) {
            doSearch();
          }
        }, 300);
      }

      function onLoad(event) {
        doSearch();
        
        Event.observe($('viewHelpNavigationSearch'), "keyup", onSearchKeyUp);
      }
    </script>    
  </head>

  <body onload="onLoad(event);" class="fixedSizedContentContainer">
    <jsp:include page="/templates/generic/header.jsp"></jsp:include>
  
    <div id="viewHelpNavigationContainer">
      <div id="viewHelpNavigationSearchContainer">
        <div class="genericFormSection">
	        <jsp:include page="/templates/generic/fragments/formtitle.jsp">
	          <jsp:param name="titleLocale" value="help.viewHelp.searchTitle"/>
	          <jsp:param name="helpLocale" value="help.viewHelp.searchHelp"/>
	        </jsp:include>          
	        <div>
	          <input type="text" id="viewHelpNavigationSearch"/> 
	        </div>
	      </div>
      </div>
      <div id="viewHelpNavigationItemsContainer">
        
      </div>
    </div>    
    
    <div id="viewHelpContentsContainer">
       
    </div>

    <jsp:include page="/templates/generic/footer.jsp"></jsp:include>
  </body>
</html>