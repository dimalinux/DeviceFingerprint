/*
 *  Copyright 2013, Dmitry Holodov <sourceCode(AT)noc.to>
 */
$(function() {
    var reverseGeocoder = new google.maps.Geocoder();
    var infoWindow = new google.maps.InfoWindow({content: ''});
    var resultMarkers = [];

    var mapOptions = { mapTypeId:google.maps.MapTypeId.HYBRID };
    var map = new google.maps.Map(document.getElementById('mapCanvas'), mapOptions);

    var redMarker; // initialized in first call to setLocation

    $(window).bind('hashchange', function() {
        var location = parseLocationStr(window.location.hash);
        setLocation(location);
        reverseGeocode(location);
    });
    $(window).trigger('hashchange'); // trigger once on initial page load

    $("#coordinateInputGo").click(function() {
        // real work is done by the hashchange handler
        setLocationHash(parseLocationStr($("#coordinateInput").val()));
    });

    $("#helpButton").click(function() {
        $("#helpPopup").toggle();
    });
    $("#helpPopupClose").click(function() {
        $("#helpPopup").hide();
    });

    google.maps.event.addListener(redMarker, 'click', function() {
        infoWindow.content =
            "<table class='redInfoWindowTable'>" +
                "<tr><td>Reverse Geocode Coordinates</td></tr>" +
                "<tr><td>" + redMarker.title + "</td></tr>" +
            "</table>";
        infoWindow.open(map, redMarker);
    });

    google.maps.event.addListener(map, 'click', function (event) {
        // heavy lifting done by hashchange handler
        setLocationHash(event.latLng);
    });

    $('#extendableDiv').mouseup(function() {
        google.maps.event.trigger(map,'resize');
    });


    function log(msg) {
        if (window.console && console.log) {
            console.log(msg);
        }
    }

    function fmtLocation(latitude, longitude, seperator) {
        seperator = typeof seperator  !== 'undefined' ? seperator : ", ";
        return latitude.toFixed(7) + seperator + longitude.toFixed(7);
    }

    // Skips non-digits at start and end of string. While a comma is the
    // expected latitude/longitude separator, any non-digit characters can be used.
    // Example valid inputs:
    //  #40.5957263, -75.2395336
    //  40.5957263 -75.2395336
    //  (45.5234°, -122.6762°)
    function parseLocationStr(coordinateStr) {
        var coordRegex = /[^\d-]*(-?[\d]+\.?[\d]*)[^\d-]+(-?[\d]+\.?[\d]*)[^\d]*/;
        var match = coordRegex.exec(coordinateStr);
        return (match !== null) ?
            new google.maps.LatLng(match[1], match[2]) :
            new google.maps.LatLng(40.7498777, -73.8470165); // US Open center court
    }

    function setLocationHash(location) {
        var hashFragment = fmtLocation(location.lat(), location.lng(), ",");
        var oldHashFragment = window.location.hash.substr(1);
        if (oldHashFragment !== hashFragment) {
            window.location.hash = hashFragment;
        } else {
            // make sure the formatting in the input box is clean, as the hashChange
            // handler won't get invoked
            $("#coordinateInput").val(fmtLocation(location.lat(), location.lng()));
        }
    }

    function setLocation(location) {
        var coordStr = fmtLocation(location.lat(), location.lng());
        $("#coordinateInput").val(coordStr);
        if (typeof redMarker === 'undefined') {
           redMarker = new google.maps.Marker({
                map: map,
                animation: google.maps.Animation.DROP,
                zIndex: google.maps.Marker.MAX_ZINDEX + 1 // ensure red marker is always on top
            });
        }
        redMarker.setPosition(location);
        redMarker.setTitle(coordStr);
    }

    function clearResultMarkers() {
        $.each(resultMarkers, function(index, marker) {
            marker.setMap(null);
        });
        resultMarkers.length = 0;
    }

    function addResultMarkers(addressResults) {

        var bounds = new google.maps.LatLngBounds();
        bounds.extend(redMarker.getPosition());

        $.each(addressResults, function(resultNumber, address) {
            var geometry = address.geometry;
            var latitude = geometry.location.lat();
            var longitude = geometry.location.lng();
            var marker = createResultMarker(resultNumber, latitude, longitude);
            resultMarkers.push(marker);
            bounds.extend(new google.maps.LatLng(latitude, longitude));
            google.maps.event.addListener(marker, 'click', function() {
                infoWindow.content =
                    "<table class='infoWindowTable'>" +
                        "<tr><td>" + address.formatted_address + "</td></tr>" +
                        "<tr><td>" + marker.title + "</td></tr>" +
                    "</table>";
                map.fitBounds(geometry.viewport);
                infoWindow.open(map, marker);
            });
        });

        map.fitBounds(bounds);
    }

    // declared outside next method for reuse (pin shadow never changes)
    var pinShadow = new google.maps.MarkerImage(
        "http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
        new google.maps.Size(40, 37),
        new google.maps.Point(0, 0),
        new google.maps.Point(12, 35)
    );

    function createResultMarker(resultNumber, latitude, longitude) {
        var pinColor = "EEEE00"; // yellow
        var pinImage = new google.maps.MarkerImage(
            "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=" + resultNumber + "|" + pinColor,
            new google.maps.Size(21, 34),
            new google.maps.Point(0, 0),
            new google.maps.Point(10, 34)
        );

        return new google.maps.Marker({
            position: new google.maps.LatLng(latitude, longitude),
            map: map,
            animation: google.maps.Animation.DROP,
            icon: pinImage,
            shadow: pinShadow,
            title: fmtLocation(latitude, longitude)
        });
    }

    function reverseGeocode(latlng) {
        $reverseGeocode =  $("#reverseGeocode").empty();
        clearResultMarkers();

        reverseGeocoder.geocode({'latLng':latlng}, function (addressResults, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                addResultMarkers(addressResults);
                $reverseGeocode.html(geocodeData2Table(addressResults));
                $(".resultCoordinateButton").click(function() {
                    showResultOnMap($(this).val());
                });
            } else {
                $reverseGeocode.html("<span class='decodeError'>" + status.replace('_', ' ') + "</span>");
            }
        });
    }

    function showResultOnMap(resultNumber) {
        google.maps.event.trigger(resultMarkers[resultNumber], 'click');
        window.scroll(0, 0);
    }

    function geocodeData2Table(results) {
        var $table = $('<table/>');

        for (var i = 0, nResults = results.length; i < nResults; i++) {
            var address = results[i];
            var types = address.types;
            var numTypes = types.length;
            var components = address.address_components;
            var numComponents = components.length;
            var lat = address.geometry.location.lat();
            var lng = address.geometry.location.lng();
            var locationStr = fmtLocation(lat, lng);
            var locationType = address.geometry.location_type.toLowerCase();
            var locationButton =
                "<button class='resultCoordinateButton' value='" + i + "'>" +
                    locationStr +
                "</button>";

            if (i > 0) {
                $table.append("<tr><td class='interResultSpace' colspan='3'</td></tr>");
            }
            $table.append("<tr><td colspan='3' class='resultHeader'>Result " + i + "</td></tr>");
            $table.append(
                "<tr>" +
                    "<td class='label'>Location:</td>" +
                    "<td>" + locationType +  "</td>" +
                    "<td>" + locationButton + "</td>"+
                "</tr>"
            );

            for (var j = 0; j < numTypes; j++) {
                var $row = $('<tr/>');
                if (j === 0) {
                    $row.append("<td class='label' rowspan='" + numTypes + "'>Address:</td>");
                }
                $row.append("<td class='type'>" + types[j] + "</td>");
                if (j === 0) {
                    $row.append("<td rowspan='" + numTypes + "'>" + address.formatted_address + "</td>");
                }
                $table.append($row);
            }

            // compute span for components label
            var componentsLabelRowSpan = 0;
            for (var k = 0; k < numComponents; k++) {
                componentsLabelRowSpan += components[k].types.length;
            }

            for (var l = 0; l < numComponents; l++) {
                var comp = components[l];
                var compTypes = comp.types;
                var name = comp.long_name;
                if (comp.short_name !== name) {
                    name += " (" + comp.short_name + ")";
                }

                for (var t = 0; t < compTypes.length; t++) {
                    var $cRow = $('<tr/>');
                    if (l === 0 && t === 0) {
                        $cRow.append("<td class='label' rowspan='" + componentsLabelRowSpan + "'>Components:</td>");
                    }
                    $cRow.append("<td class='type'>" + compTypes[t] + "</td>");
                    if (t === 0) {
                        $cRow.append("<td class='component' rowspan='" + compTypes.length + "'>" + name + "</td>");
                    }

                    $table.append($cRow);
                }
            }
        }

        return $table;
    }

});