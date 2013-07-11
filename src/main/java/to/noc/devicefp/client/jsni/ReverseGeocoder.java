/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

public final class ReverseGeocoder {
           
    public interface Callback {
        void setReverseGeocodeAddress(ReverseGeocodeJso geocodeResults);
    }
    
    public native static void computeAddress(Callback callback, double latitude, double longitude) /*-{
            
        var coordinates = new $wnd.google.maps.LatLng(latitude, longitude);

        var geocoder = new $wnd.google.maps.Geocoder();
        geocoder.geocode({'latLng': coordinates}, function(results, status) {
            var reverseGeocode = { status: status };
            if (results.length > 0) {
              var address = results[0];
               
              reverseGeocode.latitude = address.geometry.location.lat();
              reverseGeocode.longitude = address.geometry.location.lng();
              reverseGeocode.locationType = address.geometry.location_type;
              reverseGeocode.address = address.formatted_address;
              reverseGeocode.addressType = address.types[0];
              
              var components = address.address_components;
              var numComponents = components.length;

              for (var c = 0; c < numComponents; c++) {
                var comp = components[c];
                var compTypes = comp.types;
                var name = comp.long_name;
                if (comp.short_name != name) {
                    name += " (" + comp.short_name + ")";
                }
                // only the first component type is retained and we convert it
                // to camel case
                var key = compTypes[0].replace(
                            /_(.)/g, 
                            function(g0, g1) { return g1.toUpperCase(); }
                         );
                reverseGeocode[key] = name;
              }              
            }
           
            callback.@to.noc.devicefp.client.jsni.ReverseGeocoder.Callback::setReverseGeocodeAddress(Lto/noc/devicefp/client/jsni/ReverseGeocodeJso;)(reverseGeocode); 
        });
    }-*/;
    
}
