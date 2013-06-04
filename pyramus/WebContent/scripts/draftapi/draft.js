var INTERVAL = 60 * 1000;

var DRAFTAPI;
var DRAFTUI;
var __STOPDRAFTING = false;

/** Saves the checksum of the latest draft data.
 * 
 * @param draftData The draft data to save.
 * @returns {Number} The checksum of the data.
 */
function storeLatestDraftDataHash(draftData) {
  var hash = checksum(draftData);
  $(document.body).getStorage().set("latestDraftDataHash", hash);
  return hash;
}

/** Returns the checksum of the latest draft data.
 * 
 * @returns {Number} The checksum of the latest draft.
 */
function getLatestDraftDataHash() {
  var hash = $(document.body).getStorage().get("latestDraftDataHash");  
  if (!hash)
    hash = storeLatestDraftDataHash(DRAFTAPI.createFormDraft());
  return hash;
}

/** Returns <code>true</code> if the given draft is
 * equal to the latest draft.
 * 
 * @param draftData The draft data to compare.
 * @returns {Boolean} <code>true</code> if the given draft
 * is equal to the latest draft.
 */
function isDraftEqualToLatestDraft(draftData) {
  var hash = getLatestDraftDataHash();
  return checksum(draftData) == hash;
}

/** Initializes drafting. Call this before any other drafting functions.
 * 
 * @see IxDraftUI
 * @param options The options passed to <code>IxDraftUI</code>
 */
function initDrafting(options) {
  DRAFTAPI = new IxDraftAPI();
  DRAFTUI = new IxDraftUI(options);
};


/** Starts saving drafts at <code>INTERVAL</code> intervals.
 * 
 */
function startDraftSaving() {
  setTimeout("saveFormDraft();", INTERVAL);
}

/** Save the draft of the current form.
 * 
 */
function saveFormDraft() {
  if (__STOPDRAFTING != true) {
    var draftData = DRAFTAPI.createFormDraft();
    if (!isDraftEqualToLatestDraft(draftData)) {
      DRAFTUI.updateDraftStart();
      
      storeLatestDraftDataHash(draftData);
      
      JSONRequest.request("drafts/saveformdraft.json", {
        parameters: {
          draftData: draftData
        },
        onSuccess: function (jsonResponse) {
          try {
            if (jsonResponse.draftModified)
              DRAFTUI.updateDraftEnd(jsonResponse.draftModified.time);
          } finally {
            setTimeout("saveFormDraft();", INTERVAL);
          }
        },
        onFailure: function () {
          setTimeout("saveFormDraft();", INTERVAL);
        }
      });
    } else {
      setTimeout("saveFormDraft();", INTERVAL);
    }
  }
};

/** Delete the draft of the current form.
 * 
 * @param onSuccess This function is called once the draft is deleted.
 */
function deleteFormDraft(onSuccess) {
  DRAFTUI.deleteDraftStart();
  JSONRequest.request("drafts/deleteformdraft.json", {
    onSuccess: function (jsonResponse) {
      DRAFTUI.deleteDraftEnd();
      if (onSuccess) {
        onSuccess();
      }
    }
  });
}

/** Calculates the checksum for <code>s</code>.
 * 
 * @param {String} s The string whose checksum is calculated.
 * @returns {Number} The checksum of <code>s</code>.
 */
function checksum(s) {
  var i;
  var chk = s.length;
  for (i = 0, l = s.length; i < l; i++)
    chk += (s.charCodeAt(i) * i);
  return chk;
}
