/*
 *  Copyright 2013, Dmitry Holodov <sourceCode{AT}noc.to>
 */
$(function() {

    var locationHashUpdatedInternally = false;
    var positionOptions = null;
    var geolocationStartTime = null;
    var geolocationEndTime = null;

    if (!('geolocation' in navigator)) {
        $("#noGeolocation").toggle(true);
        $("#inputSection, #outputSection").toggle(false);
        return;
    }

    $("#helpButton").click(function() {
        $("#helpPopup").toggle();
    });
    $("#helpPopupClose").click(function() {
        $("#helpPopup").hide();
    });

    $(".positionInput").change(positionInputChanged);

    $("#cancelGeolocationRequest").click(function() {
        window.location.reload();
    });

    $(window).bind("hashchange", function(event) {
        if (locationHashUpdatedInternally) {
            locationHashUpdatedInternally = false;
        } else {
            locationHashToForm();
        }
    });
    locationHashToForm();

    function updateLocationHash(locationHash) {
        var oldHash = location.hash.substring(1);
        if (locationHash !== oldHash) {
            locationHashUpdatedInternally = true;
            location.hash = locationHash;
        }
    }

    function formToLocationHash() {
        var uri = new URI("#");

        var timeoutMultiplier = $("#timeoutMultiplier").val();
        if (timeoutMultiplier !== "omit") {
            uri.addFragment("timeout", $("#timeout").val());
            uri.addFragment("timeoutMultiplier", timeoutMultiplier);
        }
        var maxAgeMultiplier = $("#maxAgeMultiplier").val();
        if (maxAgeMultiplier !== "omit") {
            uri.addFragment("maxAge", $("#maxAge").val());
            uri.addFragment("maxAgeMultiplier", maxAgeMultiplier);
        }
        var highAccuracy = $("#highAccuracy").val();
        if (highAccuracy !== "omit") {
            uri.addFragment("highAccuracy", highAccuracy);
        }

        updateLocationHash(uri.fragment());
    }

    function locationHashToForm() {
        var params = new URI.parseQuery(location.hash.substring(1));

        $("#timeoutMultiplier").val(params["timeoutMultiplier"] || "omit");
        $("#timeout").val(params["timeout"] || "");
        $("#maxAgeMultiplier").val(params["maxAgeMultiplier"] || "omit");
        $("#maxAge").val(params["maxAge"] || "");
        $("#highAccuracy").val(params["highAccuracy"] || "omit");

        // positionInputChanged() will verify/reset any bad values
        positionInputChanged();
    }

    $("#submitGeoLocParams").click(function() {
        geolocationStartTime = new Date();
        navigator.geolocation.getCurrentPosition(
            posSuccessCallback,
            posErrorCallback,
            positionOptions
        );
        startWaitingState();
    });


    var timeTickerIntervalId = null;

    function startWaitingState() {
        var $elapsedTime = $("#elapsedTime");
        var startTimeMs = geolocationStartTime.getTime();

        timeTickerIntervalId = setInterval( function() {
            var secs = Math.floor((Date.now() - startTimeMs)/1000);
            var hrs = Math.floor( secs / 3600 );
            secs %= 3600;
            var mns = Math.floor( secs / 60 );
            secs %= 60;
            $elapsedTime.text(
                ( hrs < 10 ? "0" : "" ) + hrs
                + ":" + ( mns < 10 ? "0" : "" ) + mns
                + ":" + ( secs < 10 ? "0" : "" ) + secs
            );
        }, 1000);

        $("#submitGeoLocParams").toggle(false);
        $("#outputSection, #waitingForResults").toggle(true);
        $("#geolocationResults").toggle(false);
    }

    function endWaitingState() {
        geolocationEndTime = new Date();
        $("#geolocationStartTime").text(geolocationStartTime.toLocaleString());
        $("#geolocationEndTime").text(geolocationEndTime.toLocaleString());
        $("#submitGeoLocParams").toggle(true);
        $("#waitingForResults").toggle(false);
        $("#geolocationResults").toggle(true);
        clearInterval(timeTickerIntervalId);
        timeTickerIntervalId = null;
        $("#elapsedTime").empty();
    }


    function positionInputChanged() {
        $("#outputSection").toggle(false);

        positionOptions = {};

        var timeoutMultiplier = $("#timeoutMultiplier").val();
        var timeoutUsed = timeoutMultiplier !== "omit";
        var $timeout = $("#timeout");
        $timeout.toggle(timeoutUsed);
        if (timeoutUsed) {
            var timeoutBase = parseInt($timeout.val());
            if (isNaN(timeoutBase)) {
                if (timeoutMultiplier <= 1000) {
                    timeoutBase = 15000 / timeoutMultiplier;
                } else {
                    timeoutBase = 1;
                }
            }
            $timeout.val(timeoutBase);
            positionOptions["timeout"] = timeoutBase * timeoutMultiplier;
        }

        var maxAgeMultiplier = $("#maxAgeMultiplier").val();
        var maxAgeUsed = maxAgeMultiplier !== "omit";
        var $maxAge = $("#maxAge");
        $maxAge.toggle(maxAgeUsed);
        if (maxAgeUsed) {
            var maxAgeBase = parseInt($maxAge.val());
            if (isNaN(maxAgeBase)) {
                if (maxAgeMultiplier <= 1000) {
                    maxAgeBase = 15000 / maxAgeMultiplier;
                } else {
                    maxAgeBase = 1;
                }
            }
            $maxAge.val(maxAgeBase);
            positionOptions["maximumAge"] = maxAgeBase * maxAgeMultiplier;
        }

        var highAccuracy = $("#highAccuracy").val();
        if (highAccuracy !== "omit") {
            positionOptions["enableHighAccuracy"] = JSON.parse(highAccuracy);
        }

        $("#rawPositionOptions").text("positionOptions = " + obj2str(positionOptions));

        formToLocationHash();
    }

    function posErrorCallback(error) {
        endWaitingState();
        $(".positionErrorOnly").toggle(true);
        $(".positionSuccessOnly").toggle(false);

        var code = error.code;
        switch (code) {
            case error.PERMISSION_DENIED:
                code = "PERMISSION_DENIED (" + code + ")";
                break;
            case error.POSITION_UNAVAILABLE:
                code = "POSITION_UNAVAILABLE (" + code + ")";
                break;
            case error.TIMEOUT:
                code = "TIMEOUT (" + code + ")";
                break;
        }
        $("#errorCode").text(code);
        var errorMessageVisible = "message" in error && error.message.length > 0;
        $("#errorMessageRow").toggle(errorMessageVisible);
        if (errorMessageVisible) {
            $("#errorMessage").text(error.message);
        }
        $("#rawResults").text("positionError = " + obj2str(error));
    }

    function posSuccessCallback(position) {
        endWaitingState();
        $(".positionErrorOnly").toggle(false);
        $(".positionSuccessOnly").toggle(true);

        var coords = position.coords;

        $("#latitude").text(coords.latitude.toFixed(7));
        $("#longitude").text(coords.longitude.toFixed(7));
        $("#accuracy").text(formatAccuracy(coords.accuracy));

        window.showMap(coords.latitude, coords.longitude, coords.accuracy);

        function setOptionalNumericRow(valName, units) {
            var isVisible = (valName in coords && coords[valName] !== null && !isNaN(coords[valName]));
            $("#" + valName + "Row").toggle(isVisible);
            if (isVisible) {
                var val = coords[valName] + units;
                $("#" + valName).text(val);
            }
        }

        setOptionalNumericRow("altitude", " meters");
        setOptionalNumericRow("altitudeAccuracy", " meters");
        setOptionalNumericRow("heading", "°");
        setOptionalNumericRow("speed", " meters/sec");

        var timeMs = position.timestamp;
        // In IOS 6.0 (fixed in 6.1), the timestamp is in microseconds, so we divide by 1000 to compensate.
        var isTimeInMicroseconds = (timeMs > 14000000000000);
        var time = (!isTimeInMicroseconds) ?
            new Date(timeMs).toLocaleString() :
            new Date(timeMs/1000).toLocaleString() + " (source time was in microseconds)";
        $("#timestamp").text(time);

        $("#rawResults").text("position = " + obj2str(position));
    }

    function formatAccuracy(accuracy) {
        var units = " meters";
        if (accuracy > 1000 && (accuracy % 1000 === 0)) {
            accuracy /= 1000;
            units = " kilometers";
        }
        return accuracy + units;
    }

    // Recursively ensures that all properties on the object are direct (i.e. not inherited).
    // JSON.stringify won't handle the inherited values. None of the object we deal with have
    // circular references.
    function flatten(obj) {
        var flatCopy = {};
        for(var key in obj) {
            var val = obj[key];
            if (typeof val === "object" && val !== null) {
                flatCopy[key] = flatten(val);
            } else if (typeof val === "number" && isNaN(val)) {
                // We strip out the quotes later.  JSON.stringify converts NaN to null.
                flatCopy[key] = "NaN";
            } else if (typeof val !== "function") {
                flatCopy[key] = val;
            }
        }
        return flatCopy;
    }

    function obj2str(obj) {
        return JSON.stringify(flatten(obj), null,3).replace(/"NaN"/g, "NaN");
    }

});