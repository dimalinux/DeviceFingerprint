/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */

package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import to.noc.devicefp.client.entity.BrowserLocationCs;
import to.noc.devicefp.client.entity.MaxMindLocationCs;
import to.noc.devicefp.client.entity.ReverseGeocodeCs;

public class LocationMap extends Composite implements IsWidget {
    //private static final Logger log = Logger.getLogger(LocationMap.class.getName());
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, LocationMap> {}
    
    private static int mapCount = 0;
    private String mapName;
    
    @UiField DivElement mapCanvas;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public LocationMap() {
        mapName = "map" + mapCount++;
        initWidget(BINDER.createAndBindUi(this));
    }

    private void setLocation(Double latitude, Double longitude, Integer accuracyRadius) {
        mapCanvas.setInnerText("");
        //  In theory, the coordinates should never be null if this method is called,
        //  but the accuracy radius can be for Maxmind data.  To be safe, we check anyway.
        if (latitude != null && longitude != null) {
            if (accuracyRadius == null) {
                accuracyRadius = 0;
            }
            renderMap(mapName, mapCanvas, latitude, longitude, accuracyRadius);
        }
    }
    
    
    // Note: The two location classes here cannot share a common parent interface
    // due to restrictions on GWT's JavascriptObject.
    public void setLocation(MaxMindLocationCs location) {
        setLocation(location.getLatitude(), location.getLongitude(), location.getAccuracyRadius()); 
    }
    
    public void setLocation(BrowserLocationCs location, ReverseGeocodeCs reverseGeocode) {
        setLocation(location.getLatitude(), location.getLongitude(), location.getAccuracyRadius());
        if (reverseGeocode != null) {
            String address = reverseGeocode.getAddress();
            if (address == null) {
                address = "-";
            }
            Double latitude = reverseGeocode.getLatitude();
            Double longitude = reverseGeocode.getLongitude();
            if (latitude != null && longitude != null) {
                renderReverseGeocodeMarker(mapName, latitude, longitude, address);
            }
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }
    
    /*
     * The javascript for the Maps API is included from DeviceInfo.gwt.xml.
     * 
     * We're doing this in JavaScript, but the Google Maps V3 API can be
     * accessed using Java with this project:
     *      http://code.google.com/p/gwt-google-maps-v3/wiki/GettingStarted
     * 
     * ... but the project is deprecated (code is being integrated into some
     * project for Java Wrappers to all Google API's). In the meantime there's
     * no API solution in maven and it seemed easier to just write a few lines
     * of JavaScript.
     * 
     * 
     * http://code.google.com/p/gmaps-api-issues/issues/detail?id=1448
     * 
     * Things we can disable and control in map (but don't currently modify):
     *      mapTypeControl: false,
     *      panControl: false,
     *      zoomControl: false,
     *      scaleControl: false,
     *      streetViewControl: false
     */
    private native static void renderMap(String mapName, DivElement div, double latitude, double longitude, int accuracy) /*-{
            
        var coordinates = new $wnd.google.maps.LatLng(latitude, longitude);

        var mapTypeId = (accuracy && accuracy < 500) ?
            $wnd.google.maps.MapTypeId.HYBRID : $wnd.google.maps.MapTypeId.ROADMAP;

        var mapOptions = {
            center: coordinates,
            mapTypeId: mapTypeId,
            scrollwheel: false
        };

        var map = $wnd[mapName] = new $wnd.google.maps.Map(div, mapOptions);
        var infoWindow = $wnd[mapName + "_infoWindow"] = 
                            new $wnd.google.maps.InfoWindow({content: ''});
 
        var circleOptions; 
        if (accuracy) {
            circleOptions = {
                'strokeColor': "#2100FA",
                'strokeOpacity': 0.35,
                'strokeWeight': 2,
                'fillColor': "#2100FA",
                'fillOpacity': 0.25,
                'map': map,
                'center': coordinates,
                'radius': accuracy
            };
        } else {
            // create a large circle for bounds fitting, but don't give it a map
            circleOptions = {
                'center': coordinates,
                'radius': 1000000
            };        
        }

        var coordinatesStr = "(" + latitude.toFixed(7) + "°, " + longitude.toFixed(7) + "°)";
        var accuracyStr = (accuracy) ? accuracy + " meter accuracy" : "unknown accuracy";
            
        var marker = new $wnd.google.maps.Marker({
            'map': map,
            'position': coordinates,
            'title': coordinatesStr
        });

        var circle = new $wnd.google.maps.Circle(circleOptions);
        var bounds = circle.getBounds();
        
        var idleListener = $wnd.google.maps.event.addListener(map, "idle", function(){
            $wnd.google.maps.event.removeListener(idleListener);
            $wnd.google.maps.event.trigger(map, 'resize');         
            map.fitBounds(bounds);
        });

        $wnd.google.maps.event.addListener(marker, 'click', function() {
            infoWindow.content =
                "<table class='infoWindowTable'>" +
                    "<tr><td>Geolocation Center</td></tr>" +
                    "<tr><td>" + coordinatesStr + "</td></tr>" +
                    "<tr><td>" + accuracyStr + "</td></tr>" +
                "</table>";
            infoWindow.open(map, marker);
        });

        map.fitBounds(bounds);
             
    }-*/;
    
    private native static void renderReverseGeocodeMarker(String mapName, double latitude, double longitude, String address) /*-{
        var map = $wnd[mapName];
    
        var pinColor = "EEEE00"; // yellow
        var pinImage = new $wnd.google.maps.MarkerImage(
            "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=R|" + pinColor,
            new $wnd.google.maps.Size(21, 34),
            new $wnd.google.maps.Point(0, 0),
            new $wnd.google.maps.Point(10, 34)
        );
        var pinShadow = new $wnd.google.maps.MarkerImage(
            "http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
            new $wnd.google.maps.Size(40, 37),
            new $wnd.google.maps.Point(0, 0),
            new $wnd.google.maps.Point(12, 35)
        );
     
        var coordinatesStr = "(" + latitude.toFixed(7) + "°, " + longitude.toFixed(7) + "°)";

        var marker = new $wnd.google.maps.Marker({
            position: new $wnd.google.maps.LatLng(latitude, longitude),
            map: map,
            animation: $wnd.google.maps.Animation.DROP,
            icon: pinImage,
            shadow: pinShadow,
            title: coordinatesStr
        });
      
        $wnd.google.maps.event.addListener(marker, 'click', function() {
            var infoWindow = $wnd[mapName + "_infoWindow"];
            infoWindow.content =
                "<table class='infoWindowTable'>" +
                    "<tr><td>Closest Reverse Geocode Coordinates</td></tr>" +
                    "<tr><td>" + coordinatesStr + "</td></tr>" +
                    "<tr><td>" + address + "</td></tr>" +
                "</table>";
            infoWindow.open(map, marker);
        });
             
    }-*/;
}
