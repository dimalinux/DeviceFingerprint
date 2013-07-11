<%@page pageEncoding="UTF-8" %>
<%@page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><!doctype html>
<html lang="en">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-height, user-scalable=yes">
        <meta name="author" content="Dmitry Holodov">
        <meta name="description" content="View the information fingerprint that your browser leaks to websites">
        <meta name="keywords" content="Device Fingerprint, Zombie Cookie, Geolocate, IP Geolocation, DNS, TCP Fingerprint, Flash, Silverlight, Java">
        <meta name="fragment" content="!">
        <title>Device Fingerprint</title>
        <c:if test="${not empty dnsUrl}"><script src="${dnsUrl}"></script></c:if>
        <script type="text/javascript">
${clientJs}
        </script>
        <script src="/deviceFingerprint/deviceFingerprint.nocache.js"></script>
        <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <link rel="stylesheet" href="/css/index.css" type="text/css" />
    </head>
    <body>
    <div id="squareInch" style='height: 1in; width: 1in; position: absolute; z-index:-1;'></div>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
            style="position:absolute;width:0;height:0;border:0">
    </iframe>

    <div id="outerDiv">
        <div id="outlineDiv">
            <div id="banner">
                <h1 id="bannerTitle">Device Fingerprint</h1>
                <div id="loginWidget">
                    <c:if test="${!user.anonymous}"><span id="userName"><c:out value="${user.fullName} <${user.email}>"/></span></c:if>
                    <button id="loginLogoutButton" type="button" disabled>${user.anonymous ? "Login" : "Log off"}</button>
                </div>
            </div>
            <div id="mainMenu">
                <a id="currentMenuItem" href="#">Current<span class="wideScreenOnly"> Imprint</span></a>
                <a id="linkedMenuItem" href="#!Linked">Linked<span class="wideScreenOnly"> Imprints</span></a>
                <a id="sameIpMenuItem" href="#!SameIp">Same IP<span class="wideScreenOnly"> Imprints</span></a>
                <c:if test="${user.admin}">
                    <a id="allMenuItem" href="#!All">All<span class="wideScreenOnly"> Imprints</span></a>
                </c:if>
                <a id="feedbackMenuItem" href="#!Feedback">Feedback</a>
                <a id="helpMenuItem" href="#!Help">Help</a>
            </div>
            <img id="loadingImg" src="/images/loading.gif" alt="Loading"/>

            <div id='gwtStart'></div>
            <noscript>
                <div id="jsNotEnabled">
                    Your web browser must have JavaScript enabled for this application to display correctly.
                </div>
            </noscript>
        </div>
    </div>
    <footer>
        <ul class="breadcrumbs">
            <li><a href="/geoloc" target="_blank">Geolocation Tester</a></li>
            <li><a href="/geodecode"  target="_blank">Reverse Geocode Tool</a></li>
        </ul>
    </footer>
    </body>
</html>
