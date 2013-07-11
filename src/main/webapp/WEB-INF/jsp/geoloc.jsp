<%@page pageEncoding="UTF-8" %><!DOCTYPE html>
<html lang="en">
<!-- Copyright 2013, Dmitry Holodov -->
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="description" content="Understand and test the JavaScript Geolocation API">
    <meta name="keywords" content="Geolocation API, JavaScript, test">
    <meta name="author" content="Dmitry Holodov">
    <meta name="viewport" content="width=device-height, user-scalable=yes">
    <title>Browser Geolocation Tester</title>
    <link rel="stylesheet" href="/css/geoCommon.css">
    <link rel="stylesheet" href="/css/geoloc.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="http://maps.googleapis.com/maps/api/js?sensor=true"></script>
    <script src="/js/URI.js"></script>
    <script src="/js/geolocMap.js"></script>
    <script src="/js/geoloc.js"></script>
</head>
<body>
<div id="outerDiv">
    <div id="outlineDiv">
        <div id="banner">
            <h1 id="bannerTitle">Browser Geolocation Tester</h1>
            <button id="helpButton">Help</button>
        </div>
        <div id="helpPopup">
            <p>
                There are significant variations in the behavior of the
                <a href="http://www.w3.org/TR/geolocation-API/">Geolocation API</a> under different browsers,
                browser versions, and different responses to browser popup questions. This web page allows you
                to interactively test your browser&apos;s behavior.
            </p>
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

        <div id="innerBody">
            <div id="inputSection">
                <table id="inputTable">
                    <caption><h2 class="sectionHeader">Configure Geolocation Query:</h2></caption>
                    <tr>
                        <th>
                            <label for="timeout">Timeout:</label>
                        </th>
                        <td>
                            <input id="timeout" class="positionInput" type="number" min="0" size="11">
                        </td>
                        <td>
                            <select id="timeoutMultiplier" name="timeoutMultiplier" class="positionInput">
                                <option value="omit">omit</option>
                                <option value="1">milliseconds</option>
                                <option value="1000">seconds</option>
                                <option value="3600000">hours</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="maxAge">Maximum Age:</label>
                        </th>
                        <td>
                            <input id="maxAge" class="positionInput" type="number" min="0" size="11">
                        </td>
                        <td>
                            <select id="maxAgeMultiplier" class="positionInput">
                                <option value="omit">omit</option>
                                <option value="1">milliseconds</option>
                                <option value="1000">seconds</option>
                                <option value="3600000">hours</option>
                                <option value="86400000">days</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <label for="highAccuracy">High Accuracy:</label>
                        </th>
                        <td colspan="2">
                            <select id="highAccuracy" class="positionInput">
                                <option value="omit">omit</option>
                                <option value="true">true</option>
                                <option value="false">false</option>
                            </select>
                        </td>
                    </tr>
                </table>

                <div class="jsCodeSection">
                    <h2 class="sectionHeader">JavaScript Input:</h2>
                    <code id="rawPositionOptions" class="jsCode"></code>
                </div>
                <button id="submitGeoLocParams">Call Geolocation API</button>
            </div>
            <!-- inputSection -->

            <div id="outputSection">
                <div id="waitingForResults">
                    Waiting for callback. If user permission is required, the
                    <a href="http://www.w3.org/TR/geolocation-API/">Geolocation API</a> timeout period should
                    not start until the user answers.
                    <div id="elapsedTime"></div>
                    <button id="cancelGeolocationRequest">Cancel Request</button>
                </div>
                <div id="geolocationResults">
                    <table id="resultsTable">
                        <caption><h2 class="sectionHeader">Results:</h2></caption>
                        <tr class="positionSuccessOnly">
                            <th>Latitude:</th>
                            <td id="latitude"></td>
                        </tr>
                        <tr class="positionSuccessOnly">
                            <th>Longitude:</th>
                            <td id="longitude"></td>
                        </tr>
                        <tr class="positionSuccessOnly">
                            <th>Accuracy:</th>
                            <td id="accuracy"></td>
                        </tr>
                        <tr id="altitudeRow" class="positionSuccessOnly">
                            <th>Altitude:</th>
                            <td id="altitude"></td>
                        </tr>
                        <tr id="altitudeAccuracyRow" class="positionSuccessOnly">
                            <th>Altitude Accuracy:</th>
                            <td id="altitudeAccuracy"></td>
                        </tr>
                        <tr id="headingRow" class="positionSuccessOnly">
                            <th>Heading:</th>
                            <td id="heading"></td>
                        </tr>
                        <tr id="speedRow" class="positionSuccessOnly">
                            <th>Speed:</th>
                            <td id="speed"></td>
                        </tr>
                        <tr class="positionErrorOnly">
                            <th>Error code:</th>
                            <td id="errorCode"></td>
                        </tr>
                        <tr id="errorMessageRow" class="positionErrorOnly">
                            <th>Error Message:</th>
                            <td id="errorMessage"></td>
                        </tr>
                        <tr class="positionSuccessOnly">
                            <th>Timestamp:</th>
                            <td id="timestamp"></td>
                        </tr>
                        <tr>
                            <td class="spacerRow" colspan="2"></td>
                        </tr>
                        <tr>
                            <th>Query Started:</th>
                            <td id="geolocationStartTime"></td>
                        </tr>
                        <tr>
                            <th>Query Completed:</th>
                            <td id="geolocationEndTime"></td>
                        </tr>
                    </table>

                    <div class="jsCodeSection">
                        <h2 class="sectionHeader">JavaScript Output:</h2>
                        <code id="rawResults" class="jsCode"></code>
                    </div>

                    <div id="extendableDiv" class="positionSuccessOnly">
                        <div id="mapCanvas"></div>
                    </div>

                </div>
                <!-- end geolocationResults -->

            </div>
            <!-- end outputSection -->

            <div id="noGeolocation" class="errorMessage">
                Your web browser does not support Geolocation.
            </div>
            <noscript>
                <div class="errorMessage">
                    Your web browser must have JavaScript enabled for this page to display correctly.
                </div>
            </noscript>
        </div>
        <!-- end innerBody div -->
    </div>
    <!-- end outlineDiv -->
</div>
<!-- end outerDiv -->


<footer>
    <ul class="breadcrumbs">
        <li><a href="/">Home</a></li>
        <li><a href="/geodecode">Reverse Geocode Tool</a></li>
    </ul>
</footer>

</body>
</html>
