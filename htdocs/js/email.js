// email.js
// Nov 21, 2013
// Michael Mancuso
//


//function for skimming the data from text fields/boxes, then sending it to android interface.
var getAndSetData = function()
{
    //declare vars to hold all textfield data; using individual var statements to minimize
    //chance of errors at cost of some potential speed loss (only if I were to minify the code)
    var subject = $("#subject").val();
    
    var sendToUser = $("#sendto-user").val();
    var sendToDomain = $("#sendto-domain").val();
    
    var carbon1User = $("#cc-user-1").val();
    var carbon1Domain = $("#cc-domain-1").val();
    
    var carbon2User = $("#cc-user-2").val();
    var carbon2Domain = $("#cc-domain-2").val();
    
    var carbon3User = $("#cc-user-3").val();
    var carbon3Domain = $("#cc-domain-3").val();
    
    var emailBody = $("#nody-text").val();
    
    //validation. Emails require at least a send to address and a subject. Everything else is optional.
    if ($.trim(subject).length == 0 || $.trim(sendToUser).length == 0 || $.trim(sendToDomain).length == 0 )
    {
        //TEMP CODE, REPLACE WITH ANDROID CALLBACK
        alert("Empty entry");
        return;
    }
    
    //create object
    var objectToSend =
    {
        "subject":subject,
        "sendTo": sendToUser + "@" + sendToDomain,
        "carbon1": carbon1User + "@" +  carbon1Domain,
        "carbon2": carbon2User + "@" + carbon2Domain,
        "carbon3": carbon3User + "@" + carbon3Domain,
        "body": emailBody
    };
    
    //stringify has been in webkit since 2009, so will work for the webview.
    var jsonString = JSON.stringify(objectToSend);
    
    
}

var clearData = function()
{
    //each loop not needed for type selector
    $("input:text").val("");
    $("#body-text").val("");
}


//connect button handlers
$("#send-email").click(function(){getAndSetData();});
$("#new-email").click(function(){clearData();});