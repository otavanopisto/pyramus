var entities = JSDATA["entities"].evalJSON();

function onLoad(event) {
  tabControl = new IxProtoTabs($('tabs'));

  var settingsTable = new IxTable($('settingsTableContainer'), {
    id : "settingsTable",
    rowHoverEffect : true,
    columns : [ {
      header : '',
      width : 22,
      left : 8,
      dataType : 'checkbox',
      editable : true,
      paramName : 'track'
    }, {
      header : getLocale().getText("settings.manageChangeLog.settingsTableName"),
      left : 8 + 22 + 8,
      rigth : 8,
      dataType : 'text',
      editable : false,
      paramName : 'name'
    }, {
      dataType : 'hidden',
      paramName : 'entity'
    }, {
      dataType : 'hidden',
      paramName : 'property'
    } ]
  });

  var rowIndex;
  var trackColumnIndex = settingsTable.getNamedColumnIndex('track');
  settingsTable.detachFromDom();

  for ( var i = 0, l = entities.length; i < l; i++) {
    var entity = entities[i];
    rowIndex = settingsTable.addRow([ false, jsonEscapeHTML(entity.displayName), '', '' ]);
    settingsTable.hideCell(rowIndex, trackColumnIndex);
    for ( var j = 0, ll = entity.properties.length; j < ll; j++) {
      var property = entity.properties[j];
      settingsTable.addRow([ property.track, "&nbsp;&nbsp;&nbsp;&nbsp" + jsonEscapeHTML(property.displayName), jsonEscapeHTML(entity.name),
                             jsonEscapeHTML(property.name) ]);
    }
  }

  settingsTable.reattachToDom();
}
