/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import java.util.Date;
import java.util.List;
import to.noc.devicefp.client.entity.*;
import static to.noc.devicefp.client.ui.TimeFormatSelector.TimeFormat.*;
import static to.noc.devicefp.shared.ValDisplayUtil.getIntervalStr;
import static to.noc.devicefp.shared.ValDisplayUtil.widthHeightValue;

public class SummaryPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<HTMLPanel, SummaryPanel> {}

    @UiField TableRowElement publicHostnameRow;
    @UiField SpanElement publicHostnameSpan;

    @UiField TableRowElement publicIpRow;
    @UiField SpanElement publicIpSpan;

    @UiField TableRowElement proxiedHostRow;
    @UiField SpanElement proxiedHostSpan;

    @UiField TableRowElement proxiedIpRow;
    @UiField SpanElement proxiedIpSpan;

    @UiField TableRowElement lanIpRow;
    @UiField SpanElement lanIpSpan;

    @UiField TableRowElement publicDnsHostRow;
    @UiField SpanElement publicDnsHostSpan;

    @UiField TableRowElement publicDnsIpRow;
    @UiField SpanElement publicDnsIpSpan;

    @UiField TableRowElement operatingSystemRow;
    @UiField SpanElement operatingSystemSpan;

    @UiField TableRowElement uaBrowserRow;
    @UiField SpanElement uaBrowserSpan;
    @UiField ImageElement uaBrowserIcon;

    @UiField TableRowElement uaOperatingSystemRow;
    @UiField SpanElement uaOperatingSystemSpan;
    @UiField ImageElement uaOperatingSystemIcon;

    @UiField TableRowElement processorCoresRow;
    @UiField SpanElement processorCoresSpan;

    @UiField TableRowElement screenResolutionRow;
    @UiField SpanElement screenResolutionSpan;

    @UiField TableRowElement clientUptimeRow;
    @UiField SpanElement clientUptimeSpan;

    @UiField TableRowElement refererRow;
    @UiField SpanElement refererSpan;

    @UiField TableRowElement serverStartTimestampRow;
    @UiField(provided=true) TimeWithSelectableFormat serverStartTimestamp = new TimeWithSelectableFormat(DEVICE_TZ);

    @UiField TableRowElement serverEndTimestampRow;
    @UiField(provided=true) TimeWithSelectableFormat serverEndTimestamp = new TimeWithSelectableFormat(DELTA);

    @UiField TableRowElement clientStartTimestampRow;
    @UiField(provided=true) TimeWithSelectableFormat clientStartTimestamp = new TimeWithSelectableFormat(DEVICE_TZ);

    @UiField TableRowElement clientWindowCloseTimestampRow;
    @UiField(provided=true) TimeWithSelectableFormat clientWindowCloseTimestamp = new TimeWithSelectableFormat(DELTA);

    @SuppressWarnings("LeakingThisInConstructor")
    public SummaryPanel() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }


    public void setValue(DeviceCs device) {
        FlashDataCs flashData = device.getFlashData();
        JavaDataCs javaData = device.getJavaData();
        SilverlightDataCs silverlightData = device.getSilverlightData();

        setRemoteIpAndHostSpans(device.getIpAddress(), device.getRemotePort(),
                device.getRemoteHost());

        setProxySpans(device.getProxiedIp(), device.getProxiedHost());

        setLanIpSpan(javaData);

        setDnsSpan(device.getDnsData());

        setOsSpan(device);

        setUserAgentSpans(device.getUserAgentData());

        setProcessorCountSpan(silverlightData);

        setScreenResolution(flashData, device.getDisplayData());

        setUptimeSpan(silverlightData);

        setRefererSpan(device.getRequestHeaders());

        setTimestampSpans(device);
    }


    private static void setSpan(TableRowElement row, SpanElement valueSpan, Object value) {
        boolean visible = value != null;
        if (visible) {
            valueSpan.setInnerText(value.toString());
        }
        UIObject.setVisible(row, visible);
    }

    private static void setTimestamp(
            TableRowElement row,
            TimeWithSelectableFormat timeWidget,
            JsDataCs jsData,
            Date timestamp
            ) {
        setTimestamp(row, timeWidget, jsData, timestamp, null);
    }

    private static void setTimestamp(
            TableRowElement row,
            TimeWithSelectableFormat timeWidget,
            JsDataCs jsData,
            Date timestamp,
            Date startTimeStamp
            ) {
        boolean visible = timestamp != null;
        if (visible) {
            if (startTimeStamp == null) {
                timeWidget.setValue(jsData, timestamp);
            } else {
                timeWidget.setValue(jsData, timestamp, startTimeStamp);
            }
        }
        UIObject.setVisible(row, visible);
    }


    private void setScreenResolution(FlashDataCs flashData, DisplayDataCs displayData) {
        String resolution = null;
        if (flashData != null || displayData != null) {
            resolution = (flashData != null)
                    ? widthHeightValue(flashData.getScreenResolutionX(), flashData.getScreenResolutionY())
                    : widthHeightValue(displayData.getWidth(), displayData.getHeight());
        }
        setSpan(screenResolutionRow, screenResolutionSpan, resolution);
    }


    private void setRefererSpan(List<? extends RequestHeaderCs> requestHeaders) {
        String referer = null;
        if (requestHeaders != null) {
            for (RequestHeaderCs rh : requestHeaders) {
                if ("Referer".equals(rh.getName())) {
                    referer = rh.getValue();
                    break;
                }
            }
        }
        setSpan(refererRow, refererSpan, referer);
    }

    private void setUserAgentSpans(UserAgentDataCs userAgentData) {
        String uaName = (userAgentData != null) ? userAgentData.getUaName() : null;
        boolean isUaNameVisible = uaName != null;
        if (isUaNameVisible) {
            String uaVersion = userAgentData.getUaVersion();
            if (uaVersion != null) {
                uaName += " " + uaVersion;
            }
            uaBrowserSpan.setInnerText(uaName);
            uaBrowserIcon.setSrc("images/ua/browser/" + userAgentData.getUaIcon());
        }
        UIObject.setVisible(uaBrowserRow, isUaNameVisible);

        String uaOsName = (userAgentData != null) ? userAgentData.getOsName() : null;
        boolean isUaOsNameVisible = uaOsName != null;
        if (isUaOsNameVisible) {
            uaOperatingSystemSpan.setInnerText(uaOsName);
            uaOperatingSystemIcon.setSrc("images/ua/os/" + userAgentData.getOsIcon());
        }
        UIObject.setVisible(uaOperatingSystemRow, isUaOsNameVisible);
    }

    private void setDnsSpan(DnsDataCs dnsData) {

        String ip = (dnsData != null) ? dnsData.getSourceIp() : null;
        String hostname = (dnsData != null) ? dnsData.getHostName() : null;

        if (hostname != null && hostname.equals(ip)) {
            // if the hostname is equal to the ip, only display the ip field
            hostname = null;
        } else {
            // if we have a good hostname, skip the dns server ip in the summary
            ip = null;
        }

        // only one of these two is set, the other is hidden
        setSpan(publicDnsIpRow, publicDnsIpSpan, ip);
        setSpan(publicDnsHostRow, publicDnsHostSpan, hostname);
    }

    private static boolean has64BitUserAgent(JsDataCs jsData, UserAgentDataCs uaData) {
        boolean value = false;
        if (jsData != null) {
            String userAgent = jsData.getUserAgent();
            if (userAgent != null) {
                value = userAgent.matches(".*\\b(WOW64|x86_64|Win64)\\b.*");
            }
        }

        if (!value && uaData != null) {
            String uaOs = uaData.getOsName();
            if (uaOs != null && uaOs.endsWith("Mountain Lion")) {
                value = true;
            }
        }

        return value;
    }


    /*
     * Use the operating system info from Flash and fall back to Java if Flash
     * is unavailable. Span is omitted if neither is available, but the UA OS
     * span will always be shown.
     */
    private void setOsSpan(DeviceCs device) {
        FlashDataCs flashData = device.getFlashData();
        JavaDataCs javaData = device.getJavaData();
        JsDataCs jsData = device.getJsData();
        UserAgentDataCs uaData = device.getUserAgentData();

        String operatingSystem = null;
        if (flashData != null || javaData != null) {
            StringBuilder sb = new StringBuilder();
            if (flashData != null) {
                sb.append(flashData.getOs());
                sb.append(" (");
                sb.append(flashData.getCpuArchitecture());
                sb.append(")");
                // I had to start checking the user agent, as Google Chrome started bundling
                // a flash version that reports getSupports64BitProcesses as FALSE on 64-bit
                // platforms.  :(
                if (has64BitUserAgent(jsData, uaData) || flashData.getSupports64BitProcesses() == Boolean.TRUE) {
                    sb.append(" 64-bit");
                } else if ( flashData.getSupports32BitProcesses() == Boolean.TRUE
                        &&  !"Google Pepper".equals(flashData.getManufacturer())
                        ) {
                    sb.append(" 32-bit");
                }
            } else {
                sb.append(javaData.getOsName());
                sb.append(" (");
                sb.append(javaData.getOsArch());
                sb.append(")");
            }
            operatingSystem = sb.toString();
        }
        setSpan(operatingSystemRow, operatingSystemSpan, operatingSystem);
    }

    private void setRemoteIpAndHostSpans(String remoteIp, Integer remotePort, String remoteHost) {
        if (remoteIp.equals(remoteHost)) {
            remoteHost = null;
        } else if (remoteHost != null) {
            // Inserting zero-width spaces after the periods.
            // "word-wrap: break-word;" does not work inside
            // tables with "table-layout: auto;"
            remoteHost = remoteHost.replaceAll("\\.", ".\u200B");
        }
        setSpan(publicHostnameRow, publicHostnameSpan, remoteHost);
        String ipTxt = remoteIp + " (" + remotePort + ")";
        publicIpSpan.setInnerText(ipTxt);
    }

    private void setProxySpans(String proxiedIp, String proxiedHost) {
        if (proxiedHost != null && proxiedHost.equals(proxiedIp)) {
            // don't display proxied host if it's equal to the ip
            proxiedHost = null;
        }
        setSpan(proxiedHostRow, proxiedHostSpan, proxiedHost);
        setSpan(proxiedIpRow, proxiedIpSpan, proxiedIp);
    }

    private void setLanIpSpan(JavaDataCs javaData) {
        String lanIp = (javaData != null) ? javaData.getLanIpAddress() : null;
        setSpan(lanIpRow, lanIpSpan, lanIp);
    }

    private void setProcessorCountSpan(SilverlightDataCs silverlightData) {
        Integer processorCount = (silverlightData != null)
                ? silverlightData.getProcessorCount() : null;
        setSpan(processorCoresRow, processorCoresSpan, processorCount);
    }

    private void setUptimeSpan(SilverlightDataCs silverlightData) {
        String clientUptime = null;
        if (silverlightData != null) {
            String slOsVer = silverlightData.getOsVersion();
            // Tickcount appears to be broken on Mac OS X, so we skip the interval
            if (slOsVer != null && !slOsVer.startsWith("10.")) {
                Integer tickcount = silverlightData.getSysUptimeMs();
                if (tickcount != null) {
                    clientUptime = getIntervalStr(tickcount);
                }
            }
        }
        setSpan(clientUptimeRow, clientUptimeSpan, clientUptime);
    }

    private void setTimestampSpans(DeviceCs device) {
        JsDataCs jsData = device.getJsData();

        // server timestamp, in theory, should always be present
        serverStartTimestamp.setValue(jsData, device.getServerStamp());

        setTimestamp(serverEndTimestampRow, serverEndTimestamp, jsData,
                device.getServerEndStamp(), device.getServerStamp());

        Date clientStartTs = (jsData != null) ? jsData.getClientStamp() : null;
        setTimestamp(clientStartTimestampRow, clientStartTimestamp, jsData, clientStartTs);

        Date clientWindowCloseTs = (jsData != null) ? jsData.getWindowCloseStamp() : null;
        setTimestamp(clientWindowCloseTimestampRow, clientWindowCloseTimestamp, jsData,
                clientWindowCloseTs, clientStartTs);
    }

    public void refreshFlash(DeviceCs device) {
        setScreenResolution(device.getFlashData(), device.getDisplayData());
        setOsSpan(device);
    }

    public void refreshJava(DeviceCs device) {
        setLanIpSpan(device.getJavaData());
        setOsSpan(device);
    }

    public void refreshSilverlight(DeviceCs device) {
        SilverlightDataCs silverlightData = device.getSilverlightData();
        setProcessorCountSpan(silverlightData);
        setUptimeSpan(silverlightData);
    }

}
