/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.jsni;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Logger;


public final class PluginHandler {
    private static final Logger log = Logger.getLogger(PluginHandler.class.getName());
    private static HTML applet = null;
    private static HTML flash = null;
    private static HTML silverlight = null;
    private static boolean silverlightLoadError = false;
    
    public interface AppletCallback {
        void setJavaData(
                String javaVendor,
                String javaVendorUrl,
                String javaVersion,
                String osArch,
                String osName,
                String osVersion,
                String lanIpAddress
        );
    }

    public interface FlashCallback {
        void setFlashData(FlashDataJso flashData);
    }
    
    public interface SilverlightCallback {
        void setSilverlightData(SilverlightDataJso silverlightData);
    }
    
    private static AppletCallback appletCallback;
    private static FlashCallback flashCallback;
    private static SilverlightCallback silverlightCallback;
    
    
    private static native void exportAppletLoadedMethod() /*-{
       $wnd.deviceInfoAppletLoaded =
          $entry(@to.noc.devicefp.client.jsni.PluginHandler::appletLoaded(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    }-*/;

    private static native void exportFlashPluginLoadedMethod() /*-{
        $wnd.deviceInfoFlashLoaded =
           $entry(@to.noc.devicefp.client.jsni.PluginHandler::flashLoaded(Lto/noc/devicefp/client/jsni/FlashDataJso;));
    }-*/;
    
    private static native void exportSilverlightPluginLoadedMethod() /*-{
        $wnd.onSilverlightError = function(sender, args) {
           var code = args.ErrorCode;
           var type = args.ErrorType;
           var mesg  = args.ErrorMessage;
           @to.noc.devicefp.client.jsni.PluginHandler::silverlightError(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(code, type, mesg);
        };
        $wnd.deviceInfoSilverlightLoaded = function(sender, args) {
           @to.noc.devicefp.client.jsni.PluginHandler::silverlightLoaded(Lto/noc/devicefp/client/jsni/SilverlightDataJso;)(sender.getHost());
        };
    }-*/;
    
    // called from javascript
    private static void appletLoaded(
            String javaVendor,
            String javaVendorUrl,
            String javaVersion,
            String osArch,
            String osName,
            String osVersion,
            String lanIpAddress
    ) {
        appletCallback.setJavaData(
                javaVendor,
                javaVendorUrl,
                javaVersion,
                osArch,
                osName,
                osVersion,
                lanIpAddress
        );
    }

    // called from javascript    
    private static void flashLoaded(FlashDataJso flashData) {
        flashCallback.setFlashData(flashData);
    }
    
    // called from javascript   
    private static void silverlightLoaded(SilverlightDataJso silverlightData) {
        if (!silverlightLoadError) {
            silverlightCallback.setSilverlightData(silverlightData);
        } else {
            log.severe("Do to the plugin load error, we will not read silverlight values");
        }
    }
    
    // called from javascript
    private static void silverlightError(String errorCode, String errorType, String message) {
        silverlightLoadError = true;
        StringBuilder sb = new StringBuilder("Silverlight Error: code=");
        sb.append(errorCode);
        sb.append(" type=");
        sb.append(errorType);
        sb.append(" message=");
        sb.append(errorType);
        String fullMessage = sb.toString();
        log.severe(fullMessage);
    }
    
    /* returns the previous cookie value or an error message */
    public static native boolean setFlashCookie(String newCookieId) /*-{
        try {
            var flashPlugin = $doc.getElementById('DeviceInfoFlash');
            return flashPlugin.setLsoCookie(newCookieId);
        } catch(e) {
           // flash plugin catches exceptions, so this shouldn't happen
           $wnd.alert("ERROR in PluginHandler.setFlashCookie: " + e.message);
        }
        return false;
    }-*/;
    

    private static HTML createAppletObjectTag() {
        return new HTML(
            "<object " +
                " classid='clsid:8AD9C840-044E-11D1-B3E9-00805F499D93'" +
                " type='application/x-java-applet;version=1.4'" +
                " width='1' height='1' style='float:left'" +
                " id='DeviceInfoApplet'" +
                ">" +
                    "<param name='code' value='DeviceInfoApplet.class'>" +
                    "<param name='scriptable' value='true'>" +
                    "<param name='mayscript' value='true'>" +
            "</object>"
        );
    }

    private static HTML createAppletEmbedTag() {
        return new HTML(
            "<embed" +
                " id='DeviceInfoApplet'" +
                " code='DeviceInfoApplet.class'" +
                " width='1' height='1' style='float:left'" +
                " type='application/x-java-applet;version=1.4'" +
                " mayscript='true'" +
             "/>"
        );
    }

    private static HTML createFlashObjectTag(String cookieId) {
        return new HTML(
            "<object " +
                " classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000'" +
                " codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0'" +
                " width='1' height='1' style='float:left'" +
                " id='DeviceInfoFlash'" +
                ">" +
                    "<param name='movie' VALUE='DeviceInfoFlash.swf'/>" +
                    "<param name='quality' VALUE='high'/>" +
                    "<param name='bgcolor' VALUE='white'/>" +
                    "<param name='FlashVars' VALUE='cookieId=" + cookieId + "'/>" +
                    "<param name='allowScriptAccess' value='true'/>" +
            "</object>"
        );
    }

    private static HTML createFlashEmbedTag(String cookieId) {
        return new HTML(
            "<embed" +
                " id='DeviceInfoFlash'" +
                " name='DeviceInfoFlash'" +
                " src='DeviceInfoFlash.swf'" +
                " quality='high'" +
                " bgcolor='white'" +
                " width='1' height='1' style='float:left'" +
                " type='application/x-shockwave-flash'" +
                " allowScriptAccess='true'" +
                " FlashVars='cookieId=" + cookieId + "'" +
                " pluginspage='http://www.macromedia.com/go/getflashplayer'" +
             "/>"
        );
    }
    
    /*
     * TBD: See if we can lower the min runtime version
     */
    private static HTML createSilverlightObjectTag() {
        return new HTML(
            "<object " +
                " data='data:application/x-silverlight-2,'" +
                " type='application/x-silverlight-2'" +                                  
                " width='1' height='1' style='float:left'" +
                " id='DeviceInfoSilverlight'" +
                ">" +
                    "<param name='source' value='DeviceInfo.xap'/>" +
                    "<param name='onError' value='onSilverlightError'/>" +
                    "<param name='onLoad' value='deviceInfoSilverlightLoaded'/>" +
                    "<param name='background' value='white'/>" +
                    "<param name='minRuntimeVersion' value='4.0.0.0'/>" +               
                    "<param name='autoUpgrade' value='false'/>" +
                    "<param name='enablehtmlaccess' value='true'/>" +
            "</object>"
        );
    }


    public static void loadApplet(AppletCallback callbackRecipient) {

        PluginHandler.appletCallback = callbackRecipient;

        exportAppletLoadedMethod();

        applet = (Window.Navigator.getAppName().equals("Microsoft Internet Explorer")) ?
                createAppletObjectTag() : createAppletEmbedTag();

        //log.info("applet string: " + applet.getHTML());
        RootPanel.get().add(applet);
    }


    public static void loadFlash(FlashCallback callbackRecipient, String cookieId) {

        PluginHandler.flashCallback = callbackRecipient;

        exportFlashPluginLoadedMethod();

        flash = (Window.Navigator.getAppName().equals("Microsoft Internet Explorer")) ?
                createFlashObjectTag(cookieId) : createFlashEmbedTag(cookieId);

        //log.info("flash string: " + flash.getHTML());
        RootPanel.get().add(flash);
    }
    
    public static void loadSilverlight(SilverlightCallback callbackRecipient) {

        PluginHandler.silverlightCallback = callbackRecipient;

        exportSilverlightPluginLoadedMethod();

        silverlight = createSilverlightObjectTag();

        //log.info("silverlight string: " + silverlight.getHTML());
        RootPanel.get().add(silverlight);
    }    

}
