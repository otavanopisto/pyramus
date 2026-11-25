const Koski = (() => {

  return {
    loadLogEntries,
    queuePerson,
    toggleKoskiLogDetailsVisibility
  };
  
  function loadLogEntries(personId) {
    Pyramus.get("/koski/persons/{0}/queuestatus".format(personId))
      .then(data => {
        refreshUI(data);
      })
      .catch(function (error) {
        console.error(error.message);
      });  
  }

  function queuePerson(personId) {
    Pyramus.put("/koski/persons/{0}/queuestatus".format(personId))
      .then(data => {
        refreshUI(data);
      })
      .catch(function (error) {
        console.error(error);
      });  
  }

  function toggleKoskiLogDetailsVisibility(event) {
    Event.stop(event);
    var element = $('koski-status-details');
    if (element.visible()) {
      element.hide();
    } else {
      element.show();
    }
  }
    
  function refreshUI(data) {
    if (data) {
      const logElement = $('koski-status-details-log');
      logElement.replaceChildren();

      var logEntries = data.logEntries;
      var status = data.koskiStatus;
      if (logEntries) {
        for (var i = 0, l = logEntries.length; i < l; i++) {
          var m = moment(logEntries[i].date);
          var dateStr = getLocale().getDate(logEntries[i].date, false);
          var timeStr = m.format("H:mm");
          var studyProgrammeName = logEntries[i].studyProgrammeName ? " (" + logEntries[i].studyProgrammeName + ")" : "";
          var message = logEntries[i].message ? " (" + logEntries[i].message + ")" : "";
          var stateText = getLocale().getText("koski.states." + logEntries[i].state);
          var text = dateStr + " " + timeStr + " " + stateText + message + studyProgrammeName;
          var detailclass = "koski-details-"
              + logEntries[i].stateType.toLowerCase();
          var e = new Element("div", {
            className : detailclass
          }).update(text);
          logElement.insert(e);
        }
      }
      
      const statusElement = document.getElementById('koski-status');
      statusElement.className = 'koski-status koski-' + status.toLowerCase();
    }
  }
  
})();
