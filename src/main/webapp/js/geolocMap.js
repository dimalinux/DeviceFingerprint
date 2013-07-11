/*
 *  Copyright 2013, Dmitry Holodov <sourceCode{AT}noc.to>
 */
$(function() {

    var map = null;
    var redMarker;
    var yellowMarker;
    var infoWindow;
    var reverseGeocoder;

    window.showMap = function(latitude, longitude, accuracy) {

        if (map === null) {
            init();
        }

        var mapTypeId = (accuracy < 500) ?
            google.maps.MapTypeId.HYBRID : google.maps.MapTypeId.ROADMAP;
         map.setMapTypeId(mapTypeId);

        var location = new google.maps.LatLng(latitude, longitude);
        redMarker.setPosition(location);
        redMarker.setTitle(fmtLocation(latitude, longitude));

        var circleOptions = {
            'strokeColor': "#2100FA",
            'strokeOpacity': 0.35,
            'strokeWeight': 2,
            'fillColor': "#2100FA",
            'fillOpacity': 0.25,
            'map': map,
            'center': location,
            'radius': accuracy
        };

        var circle = new google.maps.Circle(circleOptions);
        map.fitBounds(circle.getBounds());

        google.maps.event.addListener(circle, "click", function() {
            infoWindow.content =
                "<table class='infoWindowTable'>" +
                    "<tr><td>Accuracy radius</td></tr>" +
                    "<tr><td>" + accuracy + " meters</td></tr>" +
                    "</table>";
            infoWindow.open(map, redMarker);
        });

        reverseGeocodeAndSetMarker(location);
    };

    function reverseGeocodeAndSetMarker(decodeLocation) {
        yellowMarker.setMap(null);

        reverseGeocoder.geocode({'latLng':decodeLocation}, function (addressResults, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                var closestAddress = addressResults[0];
                var geometry = closestAddress.geometry;
                var reversedLocation = geometry.location;
                yellowMarker.setMap(map);
                yellowMarker.setPosition(reversedLocation);
                yellowMarker.setTitle(fmtLocation(reversedLocation.lat(), reversedLocation.lng()));
                google.maps.event.addListener(yellowMarker, 'click', function() {
                    infoWindow.content =
                        "<table class='infoWindowTable'>" +
                            "<tr><td>Closest Reverse Geocoded Address:</td></tr>" +
                            "<tr><td>" + closestAddress.formatted_address + "</td></tr>" +
                            //"<tr><td>" + yellowMarker.title + "</td></tr>" +
                            "</table>";
                    map.fitBounds(geometry.viewport);
                    infoWindow.open(map, yellowMarker);
                });
            } else {
                log("Reverse geocodeing failed: " + status);
            }
        });
    }

    function init() {
        var mapOptions = {};
        map = new google.maps.Map(document.getElementById("mapCanvas"), mapOptions);

        redMarker = new google.maps.Marker({
            map: map,
            animation: google.maps.Animation.DROP,
            zIndex: google.maps.Marker.MAX_ZINDEX + 1 // ensure red marker is always on top
        });

        infoWindow = new google.maps.InfoWindow({content: ""});

        google.maps.event.addListener(redMarker, "click", function() {
            infoWindow.content =
                "<table class='redInfoWindowTable'>" +
                    "<tr><td>Geolocation Coordinates</td></tr>" +
                    "<tr><td>" + redMarker.title + "</td></tr>" +
                    "</table>";
            infoWindow.open(map, redMarker);
        });

        yellowMarker = createReverseGeocodeMarker();
        reverseGeocoder = new google.maps.Geocoder();

        $("#extendableDiv").mouseup(function() {
            google.maps.event.trigger(map,"resize");
        });
    }

    function createReverseGeocodeMarker() {
        var pinColor = "EEEE00"; // yellow
        var textOnPin = "R";
        var pinImage = new google.maps.MarkerImage(
            "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=" + textOnPin + "|" + pinColor,
            new google.maps.Size(21, 34),
            new google.maps.Point(0, 0),
            new google.maps.Point(10, 34)
        );
        var pinShadow = new google.maps.MarkerImage(
            "http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
            new google.maps.Size(40, 37),
            new google.maps.Point(0, 0),
            new google.maps.Point(12, 35)
        );

        return new google.maps.Marker({
            map: map,
            animation: google.maps.Animation.DROP,
            icon: pinImage,
            shadow: pinShadow
        });
    }

    function log(msg) {
        if (window.console && console.log) {
            console.log(msg);
        }
    }

    function fmtLocation(latitude, longitude, seperator) {
        seperator = typeof seperator  !== "undefined" ? seperator : ", ";
        return latitude.toFixed(7) + seperator + longitude.toFixed(7);
    }
});
