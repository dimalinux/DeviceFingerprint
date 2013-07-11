/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package {
import flash.display.LoaderInfo;
import flash.display.Sprite;
import flash.system.Capabilities;
import flash.external.ExternalInterface;
import flash.net.SharedObject;


public class DeviceInfoFlash extends Sprite {

    private static const COOKIE_NAME:String = "device";
    private static const javaScriptCallback:String = "deviceInfoFlashLoaded";
    private static const DESIRED_FIELDS:Array = [
        "avHardwareDisable",
        "cpuArchitecture",
        "hasAccessibility",
        "hasAudio",
        "hasAudioEncoder",
        "hasEmbeddedVideo",
        "hasIME",
        "hasMP3",
        "hasPrinting",
        "hasScreenBroadcast",
        "hasScreenPlayback",
        "hasStreamingAudio",
        "hasStreamingVideo",
        "hasTLS",
        "hasVideoEncoder",
        "isDebugger",
        //"isEmbeddedInAcrobat",
        "language",
        "localFileReadDisable",
        "manufacturer",
        "maxLevelIDC",
        "os",
        "pixelAspectRatio",
        "playerType",
        "screenColor",
        "screenDPI",
        "screenResolutionX",
        "screenResolutionY",
        "supports32BitProcesses",
        "supports64BitProcesses",
        "touchscreenType",
        "version"
    ];

    public function DeviceInfoFlash():void {
        var flashValues:Object = new Object();

        try {
            var cookieId:String = LoaderInfo(this.root.loaderInfo).parameters["cookieId"];


            for each(var prop:String in DESIRED_FIELDS) {
                flashValues[prop] = Capabilities[prop];
            }

            var lso:SharedObject = SharedObject.getLocal(COOKIE_NAME);
            var existingCookieId:String = getLsoCookie();
            var lsoStorageReadWriteTest:Boolean = lsoStorageTest();

            flashValues["existingCookieId"] = existingCookieId;
            flashValues["lsoStorageTest"] = lsoStorageReadWriteTest;
            if (existingCookieId == null && lsoStorageReadWriteTest) {
                setLsoCookie(cookieId);
            }

            ExternalInterface.addCallback("getLsoCookie", getLsoCookie);
            ExternalInterface.addCallback("setLsoCookie", setLsoCookie);

        } catch (e:Error) {
            var message:String = 'Flash error in DeviceInfoFlash constructor: ' + e.toString();
            trace(message);
        }

        ExternalInterface.call(javaScriptCallback, flashValues);
    }

    private function lsoStorageTest():Boolean {
        var testKey:String = "lso_rw_test";
        var testValue:String = "abcd1234";
        var didPass:Boolean = false;

        try {
            var lso:SharedObject = SharedObject.getLocal(testKey);
            lso.data.value = testValue;
            lso.flush();
            didPass = (testValue == lso.data.value);
        }
        catch (e:Error) {
            var message:String = 'LSO Storage test failed: ' + e.toString();
            trace(message);
        }

        return didPass;
    }

    public function getLsoCookie():String {
        var value:String = null;

        try {
            var lso:SharedObject = SharedObject.getLocal(COOKIE_NAME);
            value = lso.data.value;
        }
        catch (e:Error) {
            var message:String = 'Flash error in getLsoCookie: ' + e.toString();
            trace(message);
        }
        return value;
    }

    /*
     * Sets the LSO with the passed in name.  Any previous value is returned.
     */
    public function setLsoCookie(value:String):Boolean {

        var succeeded:Boolean = false;

        try {
            var lso:SharedObject = SharedObject.getLocal(COOKIE_NAME);
            lso.data.value = value;
            lso.flush();
            succeeded = true;
        }
        catch (e:Error) {
            var message:String = 'Flash error in setLsoCookie: ' + e.toString();
            trace(message);
        }

        return succeeded;
    }
}
/* end class */
}
/* end package */
