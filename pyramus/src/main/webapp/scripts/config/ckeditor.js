//CKEDITOR.config.skin = "pyramus,../../ckskins/pyramus/";
CKEDITOR.config.scayt_autoStartup  = false;
CKEDITOR.config.entities = false;

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
