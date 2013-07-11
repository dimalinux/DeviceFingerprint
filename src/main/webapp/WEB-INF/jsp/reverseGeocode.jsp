<%@page pageEncoding="UTF-8" %><!DOCTYPE html>
<html lang="en">
<!-- Copyright 2012, Dmitry Holodov -->
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="description" content="Interactively view results from Google's reverse Geocoding API">
    <meta name="keywords" content="Reverse Geocode, Geodecode, Google Geocoding API, JavaScript">
    <meta name="author" content="Dmitry Holodov">
    <meta name="viewport" content="width=device-height, user-scalable=yes">
    <title>Google Reverse Geocode Tool</title>
    <link rel="stylesheet" href="/css/geoCommon.css">
    <link rel="stylesheet" href="/css/reverseGeocode.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script src="/js/reverseGeocode.js"></script>
</head>
<body>
<div id="banner">
    <h1 id="bannerTitle">Google Reverse Geocode Tool</h1>
    <button id="helpButton">Help</button>
</div>
<div id="helpPopup">
    <p>
        Interactively view all results from Google's reverse Geocoding API both
        visually via the yellow map pins and textually below the map. The
        red pin marks the coordinate location sent to Google.  There are
        3 ways to enter new coordinates:
    </p>
    <ol>
        <li>
            Click on the map.
        </li>
        <li>
            Copy the latitude and longitude values in degrees into the
            input box and hit "Go".
        </li>
        <li>
            Construct a URL with the latitude and longitude values after
            hash symbol. For examples, just click on the map and view
            your browser's address bar.
        </li>
    </ol>
    <p>
        Feedback always appreciated!
        <a href="http://www.google.com/recaptcha/mailhide/d?k=01Q1BGgC8sBWOiNjWHgCT8xg==&amp;c=1d3HOTDsshXPJSdauwlMCjp8i7NhS1vWhCikx8egf5U="
        onclick="window.open('http://www.google.com/recaptcha/mailhide/d?k\07501Q1BGgC8sBWOiNjWHgCT8xg\75\75\46c\0751d3HOTDsshXPJSdauwlMCjp8i7NhS1vWhCikx8egf5U\075'); return false;"
        title="Show address">
        email
        </a>
    </p>
    <button id="helpPopupClose">x</button>
</div>
<div id="extendableDiv">
    <div id="mapCanvas"></div>
</div>
<div id="inputs">
    Target:
    <input id="coordinateInput" type="text" size="30">
    <input id="coordinateInputGo" value="Go" type="button">
</div>
<div id="reverseGeocode"></div>
<noscript>
    <div class="errorMessage">
        Your web browser must have JavaScript enabled for this page to display correctly.
    </div>
</noscript>
<footer>
    <ul class="breadcrumbs">
        <li><a href="/">Home</a></li>
        <li><a href="/geoloc">Geolocation Tester</a></li>
    </ul>
</footer>
</body>
</html>
