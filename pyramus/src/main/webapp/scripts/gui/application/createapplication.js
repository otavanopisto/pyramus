(function() {

  function checkAge() {
    var showUnderage = false;
    var applicationForm = $("#application-form").alpaca();
    var line = applicationForm.childrenByPropertyId["line"].getValue();
    if (line == 'nettilukio' || line == 'nettipk' || line == 'lahilukio' || line == 'bandilinja') {
      var birthday = applicationForm.childrenByPropertyId["birthday"].getValue();
      var years = moment().diff(moment(birthday, "D.M.YYYY"), 'years');
      showUnderage = years < 18;
    }
    applicationForm.childrenByPropertyId["underage"].setValue(showUnderage);
    if (showUnderage) {
      $("#section-underage").show();
    }
    else {
      $("#section-underage").hide();
    }
  }
  
  $(document).ready(function() {
    Alpaca.setDefaultLocale("fi_FI");
    $("#application-form").alpaca({
      "optionsSource": "/scripts/gui/application/application-options.cnf",
      "schemaSource": "/scripts/gui/application/application-schema.cnf",
      "options": {
        "form": {
          "buttons":{
            "submit":{
              "click": function() {
                var value = this.getValue();
                $.ajax({
                  url: "/1/application/createapplication",
                  type: "POST",
                  data: JSON.stringify(value, null, " "), 
                  dataType: "json",
                  contentType: "application/json; charset=utf-8",
                  success: function(result) {
                    console.log('success');
                  },
                  error: function() {
                    console.log('error');
                  },
                  complete: function() {
                    console.log('complete');
                  }
                });
              }
            }
          }
        },
        "fields": {
          "line": {
            "onFieldChange": function(e) {
              checkAge();
              $("#line-intro").remove();
              var line = this.getValue();
              $.ajax({
                url: "/scripts/gui/application/intro-" + line + ".html",
                type: "GET",
                contentType: "application/html; charset=utf-8",
                success: function(result) {
                  $("#line-selector").after(result);
                }
              });
            }
          },
          "birthday": {
            "onFieldChange": function(e) {
              checkAge();
            },
            "validator": function(callback) {
              var value = this.getValue();
              if (value && moment(value, 'D.M.YYYY').isValid() && value.lastIndexOf('.') == value.length - 5) {
                callback({"status": true});
              }
              else {
                callback({
                  "status": false,
                  "message": "Päivämäärän muoto on virheellinen"
                });
              }
            }
          }
        }
      },
      "postRender": function(control) {
        $("#buttons-section").append($("div.alpaca-form-buttons-container"));
        $("div[alpaca-layout-binding-field-name='underage']").hide();
        checkAge();
      },
      "view": {
        "parent": "bootstrap-edit",
        "layout": {
          "template": '/scripts/gui/application/application-template.html',
          "bindings": {
            "line": "#section-line",
            "last-name": "#section-basic",
            "first-names": "#section-basic",
            "birthday": "#section-basic",
            "ssn-end": "#section-basic",
            "sex": "#section-basic",
            "sex-other": "#section-basic",
            "address": "#section-basic",
            "zip-code": "#section-basic",
            "city": "#section-basic",
            "country": "#section-basic",
            "municipality": "#section-basic",
            "nationality": "#section-basic",
            "language": "#section-basic",
            "email": "#section-basic",
            "underage": "#section-underage",
            "underage-last-name": "#section-underage",
            "underage-first-name": "#section-underage",
            "underage-phone": "#section-underage",
            "underage-email": "#section-underage",
            "underage-address": "#section-underage",
            "underage-zip-code": "#section-underage",
            "underage-city": "#section-underage",
            "underage-country": "#section-underage",
            "underage-basis": "#section-underage",
            "other-school": "#section-additional",
            "other-school-name": "#section-additional",
            "study-goals": "#section-additional",
            "job": "#section-additional",
            "job-other": "#section-additional",
            "additional-info": "#section-additional",
            "previous-studies-lukio": "#section-additional",
            "previous-studies-lukio-other": "#section-additional",
            "previous-studies-pk": "#section-additional",
            "previous-studies-pk-other": "#section-additional",
            "additional-info-music": "#section-additional",
            "music-link": "#section-additional",
            "lodging": "#section-additional",
            "source": "#section-source",
            "source-other": "#section-source"
          }
        }
      }      
    });
  });
  
}).call(this);