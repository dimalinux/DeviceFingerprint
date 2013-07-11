/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
import java.applet.Applet;
import java.awt.Color;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import netscape.javascript.JSObject;
import netscape.javascript.JSException;

/*
 *  Loads system information and then calls the following
 *  (predefined) javascript method in the window containing
 *  the applet:
 *
 *  deviceInfoAppletLoaded(
 *      javaVendor,
 *      javaVendorUrl,
 *      javaVersion,
 *      osArch,
 *      osName,
 *      osVersion,
 *      lanIpAddress
 *  );
 */
public class DeviceInfoApplet extends Applet {

    private static final long serialVersionUID = 151304970055336603L;

    public void init() {
	    setBackground(Color.WHITE);
    }

    public void start() {
        String javaVendor = System.getProperty("java.vendor");
        String javaVendorUrl = System.getProperty("java.vendor.url");
        String javaVersion = System.getProperty("java.version");
        String osArch = System.getProperty("os.arch");
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String lanIpAddress = getLanIpAddress();

        try {
            JSObject window = JSObject.getWindow(this);

            Object[] params = {
                    javaVendor,
                    javaVendorUrl,
                    javaVersion,
                    osArch,
                    osName,
                    osVersion,
                    lanIpAddress
            };
            window.call("deviceInfoAppletLoaded", params);

        } catch (JSException jse) {
            jse.printStackTrace();
        }
    }

    /*
     * Starting with Java 6 update 22, the code below cannot be called in a method scripted
     * from JavaScript without escalating privliges. We now invoke JavaScript (callback)
     * instead of JavaScript invoking our methods to avoid the issue:
     *      http://docs.oracle.com/javase/6/docs/technotes/guides/security/doprivileged.html
     *      http://stackoverflow.com/questions/4135138/access-denied-java-net-socketpermission-127-0-0-18080-connect-resolve
     *      http://stackoverflow.com/questions/4989120/problem-with-java-applet-to-connect-our-server-to-call-a-php-file
     */
    private String getLanIpAddress() {
        String ip = "";

        /* TBD: Get code to work when user is behind a proxy */
        String serverHost = getCodeBase().getHost();
        int serverPort = getCodeBase().getPort();
        if (serverPort == -1) {
            serverPort = 80;
        }

        try {
            boolean wasTimeout = false;
            Socket socket = new Socket();
            socket.setSoLinger(true, 30);
            final InetSocketAddress sa = new InetSocketAddress(serverHost, serverPort);
            try {
                socket.connect(sa, 3000); // 3 second timeout
            } catch (SocketTimeoutException ex) {
                wasTimeout = true;
            }
            
            ip = socket.getLocalAddress().toString();
            socket.close();
            if (ip.startsWith("/")) {
                ip = ip.substring(1);
            }
            if (wasTimeout) {
                ip += " (timeout)";
            }
        } catch (Exception ex) {
            ip += ex.toString();
            ex.printStackTrace(System.out);
        }
        return ip;
    }
}
