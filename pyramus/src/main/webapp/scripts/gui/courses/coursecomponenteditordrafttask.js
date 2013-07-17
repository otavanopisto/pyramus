IxCourseComponentEditorDraftTask = Class.create(IxAbstractDraftTask, {
  initialize : function($super) {
    $super();
  },
  createDraftData: function (element) {
    var componentsEditor = getCourseComponentsEditor();
    if (componentsEditor) {
      var draftData = new Hash();
      
      var componentEditors = componentsEditor.getComponentEditors();
      var componentCount = componentEditors.length;
      
      draftData.set('componentCount', this._compress(componentCount));
      
      for (var componentIndex = 0; componentIndex < componentCount; componentIndex++) {
        var componentPrefix = 'component.' + componentIndex;

        var componentEditor = componentEditors[componentIndex];
        draftData.set(componentPrefix + '.id', this._compress(componentEditor.getComponentId()));
        draftData.set(componentPrefix + '.name', this._compress(componentEditor.getName()));
        draftData.set(componentPrefix + '.length', this._compress(componentEditor.getLength()));
        draftData.set(componentPrefix + '.description', this._compress(componentEditor.getDescription()));
        
        var resourceCategoryIds = componentEditor.getComponentResourceCategoryIds();
        var resourceCategoryCount = resourceCategoryIds.length;
        
        draftData.set(componentPrefix + '.resourceCategoryCount', this._compress(resourceCategoryCount));
        
        for (var resourceCategoryIndex = 0; resourceCategoryIndex < resourceCategoryCount; resourceCategoryIndex++) {
          var categoryPrefix = componentPrefix + '.resourceCategory.' + resourceCategoryIndex;
          var categoryId = resourceCategoryIds[resourceCategoryIndex];
          var resourceCount = componentEditor.getComponentResourceCategoryResourceCount(categoryId);
        
          draftData.set(categoryPrefix + '.id', this._compress(categoryId));
          draftData.set(categoryPrefix + '.name', this._compress(componentEditor.getComponentResourceCategoryName(categoryId)));
          draftData.set(categoryPrefix + '.resourceCount', this._compress(resourceCount));
          
          for (var resourceIndex = 0; resourceIndex < resourceCount; resourceIndex++) {
            var resourcePrefix = categoryPrefix + '.resource.' + resourceIndex;
            
            draftData.set(resourcePrefix + '.id', this._compress(componentEditor.getComponentResourceId(categoryId, resourceIndex)));
            draftData.set(resourcePrefix + '.resourceId', this._compress(componentEditor.getComponentResourceResourceId(categoryId, resourceIndex)));
            draftData.set(resourcePrefix + '.resourceType', this._compress(componentEditor.getComponentResourceResourceType(categoryId, resourceIndex)));
            draftData.set(resourcePrefix + '.resourceName', this._compress(componentEditor.getComponentResourceResourceName(categoryId, resourceIndex)));
            draftData.set(resourcePrefix + '.usage', this._compress(componentEditor.getComponentResourceUsage(categoryId, resourceIndex)));
            draftData.set(resourcePrefix + '.quantity', this._compress(componentEditor.getComponentResourceQuantity(categoryId, resourceIndex)));
          }
        }
      }

      return new IxElementDraft('courseComponentEditor', 'courseComponentEditor', draftData.toJSON());
    }
  },
  restoreDraftData: function (elementDraft) {
    var componentsEditor = getCourseComponentsEditor();
    if (componentsEditor) {
      componentsEditor.removeAllCourseComponents();
      
      var draftData = Object.isString(elementDraft.getData()) ? elementDraft.getData().evalJSON() : elementDraft.getData();
      var componentCount = draftData['componentCount'];
      for (var componentIndex = 0; componentIndex < componentCount; componentIndex++) {
        var componentPrefix = 'component.' + componentIndex;

        var componentId = draftData[componentPrefix + '.id'];
        var componentName = draftData[componentPrefix + '.name'];
        var componentLength = draftData[componentPrefix + '.length'];
        var componentDescription = draftData[componentPrefix + '.description'];
        
        var componentEditor = componentsEditor.addCourseComponent(componentId, componentName, componentLength, componentDescription);
        
        var resourceCategoryCount = draftData[componentPrefix + '.resourceCategoryCount'];
        
        for (var resourceCategoryIndex = 0; resourceCategoryIndex < resourceCategoryCount; resourceCategoryIndex++) {
          var categoryPrefix = componentPrefix + '.resourceCategory.' + resourceCategoryIndex;
          
          var categoryId = draftData[categoryPrefix + '.id'];
          var categoryName = draftData[categoryPrefix + '.name'];
          var resourceCount = draftData[categoryPrefix + '.resourceCount'];
        
          componentEditor.addResourceCategory(categoryId, categoryName);
          
          for (var resourceIndex = 0; resourceIndex < resourceCount; resourceIndex++) {
            var resourcePrefix = categoryPrefix + '.resource.' + resourceIndex;
            
            var componentResourceId = draftData[resourcePrefix + '.id'];
            var resourceId = draftData[resourcePrefix + '.resourceId'];
            var resourceType = draftData[resourcePrefix + '.resourceType'];
            var resourceName = draftData[resourcePrefix + '.resourceName'];
            var resourceUsage = draftData[resourcePrefix + '.usage'];
            var resourceQuantity = draftData[resourcePrefix + '.quantity'];
            
            componentEditor.addResource(categoryId, componentResourceId, resourceId, resourceType, resourceName, resourceUsage, resourceQuantity);
          }
        }
      }      
    }
  }
});

Object.extend(IxCourseComponentEditorDraftTask, {
  supports: ['div.courseComponents']
});

IxDraftTaskVault._registerTaskType(IxCourseComponentEditorDraftTask, 'courseComponentEditor');