/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import to.noc.devicefp.client.entity.DeviceCs;
import to.noc.devicefp.client.entity.JsDataCs;
import to.noc.devicefp.client.entity.PluginCs;
import to.noc.devicefp.client.jsni.CurrentDeviceDisplayEventHandling;

public class DeviceDetailsView extends Composite implements IsWidget {
    //private static final Logger log = Logger.getLogger(DeviceDetailsView.class.getName());
    private static final Binder BINDER = GWT.create(Binder.class);

    private boolean useLiveDisplayUpdates;

    interface Binder extends UiBinder<HTMLPanel, DeviceDetailsView> {}
    private static DeviceDetailsView instance;

    public interface DeleteHandler {
        void deleteButtonClicked();
    }
    private DeleteHandler deleteHandler;

    private DeviceCs device;

    @UiField DisclosurePanel summarySection;
    @UiField SummaryPanel summaryPanel;

    @UiField DisclosurePanel zombieCookieSection;
    @UiField ZombieCookiePanel zombieCookiePanel;

    @UiField DisclosurePanel browserLocationSection;
    @UiField BrowserLocationPanel browserLocationPanel;

    @UiField DisclosurePanel maxMindSection;
    @UiField MaxMindPanel maxMindPanel;

    @UiField DisclosurePanel jsSection;
    @UiField JsPanel jsPanel;

    @UiField DisclosurePanel displaySection;
    @UiField DisplayPanel displayPanel;

    @UiField DisclosurePanel requestHeadersSection;
    @UiField RequestHeadersPanel requestHeadersPanel;

    @UiField DisclosurePanel silverlightSection;
    @UiField SilverlightPanel silverlightPanel;

    @UiField DisclosurePanel javaSection;
    @UiField JavaPanel javaPanel;

    @UiField DisclosurePanel flashSection;
    @UiField FlashPanel flashPanel;

    @UiField DisclosurePanel tcpSynSection;
    @UiField TcpSynPanel tcpSynPanel;

    @UiField DisclosurePanel dnsSection;
    @UiField DnsPanel dnsPanel;

    @UiField DisclosurePanel pluginSection;
    @UiField PluginsPanel pluginsPanel;

    private boolean openAllState = true;
    @UiField Button openOrCloseAllSectionsButton;
    @UiField Button deleteButton;

    private DisclosurePanel[] allDisclosurePanels;

    @SuppressWarnings("LeakingThisInConstructor")
    private DeviceDetailsView(boolean useLiveDisplayUpdates) {
        this.useLiveDisplayUpdates = useLiveDisplayUpdates;
        initWidget(BINDER.createAndBindUi(this));
        allDisclosurePanels = new DisclosurePanel[]{
            summarySection,
            zombieCookieSection,
            browserLocationSection,
            maxMindSection,
            jsSection,
            displaySection,
            requestHeadersSection,
            silverlightSection,
            javaSection,
            flashSection,
            tcpSynSection,
            dnsSection,
            pluginSection
        };
    }

    public static DeviceDetailsView instance() {
        if (instance == null) {
            instance = new DeviceDetailsView(false);
        }
        return instance;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void setDeleteHandler(DeleteHandler delegate) {
        this.deleteHandler = delegate;
    }

    public DisplayPanel getDisplayPanel() {
        return displayPanel;
    }


    /*
     * Summary
     */
    private void initSummarySection() {
        /* Summary section is always visible unless explicitly closed */
        if (summarySection.isOpen()) {
            summaryPanel.setValue(device);
        }
    }

    @UiHandler("summarySection")
    void onOpenSummarySection(OpenEvent<DisclosurePanel> event) {
        initSummarySection();
    }


    /*
     *  Zombie Cookies
     */
    private void initCookieSection() {
        boolean isVisible = device.getCookieStates() != null;
        if (isVisible && zombieCookieSection.isOpen()) {
            zombieCookiePanel.setValue(
                    device.getJsData(),
                    device.getCookieStates(),
                    device.getRequestHeaders()
                    );
        }
        zombieCookieSection.setVisible(isVisible);
    }

    @UiHandler("zombieCookieSection")
    void onOpenCookieSection(OpenEvent<DisclosurePanel> event) {
        initCookieSection();
    }


    /*
     * JavaScript
     */
    private void initJsSection() {
        boolean isVisible = device.getJsData() != null;
        if (isVisible && jsSection.isOpen()) {
            jsPanel.setValue(device.getJsData());
        }
        jsSection.setVisible(isVisible);
    }

    @UiHandler("jsSection")
    void onOpenJsSection(OpenEvent<DisclosurePanel> event) {
        initJsSection();
    }


    /*
     * JavaScript Display
     */
    private void initDisplaySection() {
        boolean isVisible = device.getDisplayData() != null;
        if (isVisible && displaySection.isOpen()) {
           displayPanel.setValue(device.getDisplayData(), useLiveDisplayUpdates);
           if (useLiveDisplayUpdates) {
               CurrentDeviceDisplayEventHandling.instance().startEventHandling();
           }
        }
        displaySection.setVisible(isVisible);
    }

    @UiHandler("displaySection")
    void onOpenDisplaySection(OpenEvent<DisclosurePanel> event) {
        initDisplaySection();
    }

    @UiHandler("displaySection")
    void onCloseDisplaySection(CloseEvent<DisclosurePanel> event) {
        if (useLiveDisplayUpdates) {
            CurrentDeviceDisplayEventHandling.instance().stopEventHandling();
        }
    }


    /*
     * Request Headers
     */
    private void initRequestHeadersSection() {
        // Checking against null to avoid exceptions, but I don't
        // think we need to check for zero size.
        boolean isVisible = device.getRequestHeaders() != null;
        if (isVisible && requestHeadersSection.isOpen()) {
            requestHeadersPanel.setValue(device.getRequestHeaders());
        }
        requestHeadersSection.setVisible(isVisible);
    }

    @UiHandler("requestHeadersSection")
    void onOpenRequestHeaders(OpenEvent<DisclosurePanel> event) {
        initRequestHeadersSection();
    }


    /*
     * Browser Geolocation
     */
    private void initBrowserLocationSection() {

        boolean isVisible = device.getBrowserLocation() != null;
        if (!isVisible) {
            // Second chance to make the section visible even if no location data
            // has appeared
            JsDataCs jsData = device.getJsData();
            Boolean geolocCapabable = (jsData != null) ?
                    jsData.getGeolocationCapable() : null;
            isVisible = Boolean.TRUE.equals(geolocCapabable);
        }

        if (isVisible && browserLocationSection.isOpen()) {
            browserLocationPanel.setValue(
                    device.getJsData(),
                    device.getBrowserLocation(),
                    device.getReverseGeocode(),
                    useLiveDisplayUpdates
                    );
        }
        browserLocationSection.setVisible(isVisible);
    }

    @UiHandler("browserLocationSection")
    void onOpenBrowserLocationSection(OpenEvent<DisclosurePanel> event) {
        initBrowserLocationSection();
    }


    /*
     * IP based location (Maxmind)
     */
    private void initMaxMindSection() {
        boolean isVisible = device.getMaxMindLocation() != null;
        if (isVisible && maxMindSection.isOpen()) {
            maxMindPanel.setValue(device.getJsData(), device.getMaxMindLocation());
        }
        maxMindSection.setVisible(isVisible);
    }

    @UiHandler("maxMindSection")
    void onOpenMaxMindLocationInfo(OpenEvent<DisclosurePanel> event) {
        initMaxMindSection();
    }


    /*
     * Flash
     */
    private void initFlashSection() {
        boolean isVisible = device.getFlashData() != null;
        if (isVisible && flashSection.isOpen()) {
            flashPanel.setValue(device.getFlashData());
        }
        flashSection.setVisible(isVisible);
    }

    @UiHandler("flashSection")
    public void onOpenFlashSection(OpenEvent<DisclosurePanel> event) {
        initFlashSection();
    }


    /*
     * Java
     */
    private void initJavaSection() {
        boolean isVisible = device.getJavaData() != null;
        /*
         *  Java applet is no longer loaded.  We only display Java information
         *  for devices saved before we stopped loading the applet.
         *
         *  if (!isVisible) {
         *     // one more chance to make the java section visible even if the
         *     // applet did not load
         *     JsDataCs jsData = device.getJsData();
         *     isVisible = jsData != null
         *             && jsData.getJavaEnabled() == Boolean.TRUE
         *             && "Gecko".equals(jsData.getProduct());
         *  }
         */

        if (isVisible && javaSection.isOpen()) {
            javaPanel.setValue(device.getJavaData(), useLiveDisplayUpdates);
        }
        javaSection.setVisible(isVisible);
    }

    @UiHandler("javaSection")
    void onOpenJavaSection(OpenEvent<DisclosurePanel> event) {
        initJavaSection();
    }


    /*
     * Silverlight
     */
    private void initSilverlightSection() {
        boolean isVisible = device.getSilverlightData() != null;
        if (isVisible && silverlightSection.isOpen()) {
            silverlightPanel.setValue(device.getSilverlightData());
        }
        silverlightSection.setVisible(isVisible);
    }

    @UiHandler("silverlightSection")
    void onOpenSilverlightInfo(OpenEvent<DisclosurePanel> event) {
        initSilverlightSection();
    }


    /*
     * Tcp SYN
     */
    private void initTcpSynSection() {
        boolean isVisible = device.getTcpSynData() != null;
        if (isVisible && tcpSynSection.isOpen()) {
            tcpSynPanel.setValue(device.getTcpSynData());
        }
        tcpSynSection.setVisible(isVisible);
    }

    @UiHandler("tcpSynSection")
    void onOpenTcpSynSection(OpenEvent<DisclosurePanel> event) {
        initTcpSynSection();
    }


    /*
     * DNS
     */
    private void initDnsSection() {
        boolean isVisible = device.getDnsData() != null;
        if (isVisible && dnsSection.isOpen()) {
            dnsPanel.setValue(device.getDnsData());
        }
        dnsSection.setVisible(isVisible);
    }

    @UiHandler("dnsSection")
    void onOpenDnsDataSection(OpenEvent<DisclosurePanel> event) {
        initDnsSection();
    }


    /*
     * Plugins
     */
    private void initPluginSection() {
        List<? extends PluginCs> plugins = device.getPlugins();
        boolean isVisible = plugins != null && plugins.size() > 0;
        if (isVisible && pluginSection.isOpen()) {
            pluginsPanel.setValue(device.getPlugins());
        }
        pluginSection.setVisible(isVisible);
    }

    @UiHandler("pluginSection")
    void onOpenPluginSection(OpenEvent<DisclosurePanel> event) {
        initPluginSection();
    }

    private void initDeleteButton() {
        if (deleteHandler != null) {
            deleteButton.setEnabled(true);
            deleteButton.setVisible(true);
            deleteButton.setText("Delete");
        } else {
            deleteButton.setEnabled(false);
            deleteButton.setVisible(false);
        }
    }

    @UiHandler("deleteButton")
    void onDeleteButtonClicked(ClickEvent event) {
        deleteButton.setEnabled(false);
        deleteButton.setText("Deleting...");
        deleteHandler.deleteButtonClicked();
    }

    @UiHandler("openOrCloseAllSectionsButton")
    void onOpenAllPanelsButtonClicked(ClickEvent event) {
        openOrCloseAllSectionsButton.setEnabled(false);
        openOrCloseAllSectionsButton.setText("working...");
        // make sure the disabled button is rendered before starting an expensive
        // open all operation.
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                for (DisclosurePanel sectionPanel : allDisclosurePanels) {
                    sectionPanel.setOpen(openAllState);
                }
                openAllState = !openAllState;
               initOpenAllPanelsButton();
            }
        });
    }

    private void initOpenAllPanelsButton() {
         openOrCloseAllSectionsButton.setText(openAllState ? "↘↘" : "↖↖");
         openOrCloseAllSectionsButton.setTitle(openAllState ? "show all" : "hide all");
         openOrCloseAllSectionsButton.setEnabled(true);
    }


    public void setDevice(DeviceCs device, boolean useLiveDisplayUpdates) {

        // Can't do this optimization, as flash, java and other member fields
        // can be initilized while the user is viewing help or saved devices.
        //if (this.device == device) {
        //    return;
        //}

        openOrCloseAllSectionsButton.setEnabled(false);
        this.device = device;
        this.useLiveDisplayUpdates = useLiveDisplayUpdates;

        initSummarySection();
        initCookieSection();
        initBrowserLocationSection();
        initMaxMindSection();
        initJsSection();
        initDisplaySection();
        initRequestHeadersSection();
        initSilverlightSection();
        initJavaSection();
        initFlashSection();
        initTcpSynSection();
        initDnsSection();
        initPluginSection();

        initDeleteButton();
        initOpenAllPanelsButton();
    }

    /*
     * The remaining methods are to handle data that comes in or changes after
     * the above setDevice method was already called.  (when displaying the
     * current device
     */

    public void refreshBrowserLocation() {
        initBrowserLocationSection();
    }

    public void refreshSilverlightData() {
        initSilverlightSection();
        if (summarySection.isOpen()) {
            summaryPanel.refreshSilverlight(device);
        }
    }

    public void refreshDisplayData() {
        /*
         * TBD: Do we want to change the screen resolution in the summary if
         * effected?
         */
        if (useLiveDisplayUpdates && displaySection.isOpen()) {
            displayPanel.setValue(device.getDisplayData(), true);
        }

    }

    public void refreshJavaData() {
        if (summarySection.isOpen()) {
            summaryPanel.refreshJava(device);
        }
        initJavaSection();
    }

    public void refreshFlashData() {
        if (summarySection.isOpen()) {
            summaryPanel.refreshFlash(device);
        }
        initFlashSection();
    }

    public void refreshCookieValues() {
        initCookieSection();
    }

}
