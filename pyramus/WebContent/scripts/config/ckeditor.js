CKEDITOR.plugins.addExternal('ixajax', GLOBAL_contextPath + '/scripts/ckplugins/ixajax/');
CKEDITOR.plugins.addExternal('ixtidyclipboard', GLOBAL_contextPath + '/scripts/ckplugins/ixtidyclipboard/');
CKEDITOR.plugins.addExternal('ixdisableeditor', GLOBAL_contextPath + '/scripts/ckplugins/ixdisableeditor/');

CKEDITOR.config.IxTidyClipboard = {
  serverURL: GLOBAL_contextPath + '/settings/htmlcleanup.json',
  serverDataParam: 'htmlData', 
  serverResponseParam: 'htmlData',
  untidyContent: [/<script/i, /\<embed/i, /<object/i, /<applet/i, /<font/i, /<meta/i, /<w:/i, /style="/, /class="/, /id="/]
};

CKEDITOR.config.IxDisableEditor = {
  messageStyle: "padding: 8px; font-weight: bold; text-align: center; vertical-align: middle"    
};

CKEDITOR.config.scayt_autoStartup  = false;
CKEDITOR.config.skin = "pyramus,../../ckskins/pyramus/";
CKEDITOR.config.entities = false;
CKEDITOR.config.extraPlugins = "ixtidyclipboard";

var defaultDescriptionToolbar = [
  ['Cut','Copy','Paste','PasteText','-', 'Scayt'],
  ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
  ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
  ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
  ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
  ['Link','Unlink'],
  ['Image','Table','HorizontalRule','SpecialChar'],
  '/',
  ['Format','Font','FontSize'],
  ['TextColor','BGColor'],
  ['Maximize', 'ShowBlocks','-','About']
];

var simpleToolbar = [
  ['Bold','Italic','Underline']
];


CKEDITOR.config.toolbar_courseDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_moduleDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_projectDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_studentProjectDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_gradingScaleDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_studentAdditionalInformation = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_studentGroupDescription = defaultDescriptionToolbar;
CKEDITOR.config.toolbar_studentContactEntryText = simpleToolbar;
CKEDITOR.config.toolbar_courseGradeText = simpleToolbar;
