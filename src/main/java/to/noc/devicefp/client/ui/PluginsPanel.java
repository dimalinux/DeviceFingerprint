/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.*;
import java.util.List;
import to.noc.devicefp.client.entity.PluginCs;

public class PluginsPanel extends Composite implements IsWidget {
    private static final Binder BINDER = GWT.create(Binder.class);

    interface Binder extends UiBinder<SimplePanel, PluginsPanel> {}

    interface Style extends CssResource {
        String oddPlugin();
    }

    @UiField Style style;
    @UiField FlexTable table;
    private RowFormatter rowFormatter;

    private List<? extends PluginCs> plugins;

    @SuppressWarnings("LeakingThisInConstructor")
    public PluginsPanel() {
        initWidget(BINDER.createAndBindUi(this));
        rowFormatter = table.getRowFormatter();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void addRow(int row, String label, String value, boolean isOddPlugin) {
        addRow(row, label, new Label(value), isOddPlugin);
    }

    private void addRow(int row, String label, Widget value, boolean isOddPlugin) {
        table.setWidget(row, 0, new Label(label));
        table.setWidget(row, 1, value);
        if (isOddPlugin) {
            rowFormatter.addStyleName(row, style.oddPlugin());
        }
    }

    public void setValue(List<? extends PluginCs> plugins) {
        // equality check safe, plugins list don't change after initialized
        if (this.plugins == plugins) {
            return;
        }
        this.plugins = plugins;

        table.removeAllRows();

        int row = 0;
        boolean isOddPlugin = false;

        for (PluginCs plugin : plugins) {
            String name = plugin.getName();
            if (name != null) {
                addRow(row++, "Name", name, isOddPlugin);
            }
            String description = plugin.getDescription();
            if (description != null && description.length() > 0) {
                HTML value = new HTML(SimpleHtmlSanitizer.sanitizeHtml(description));
                addRow(row++, "Description", value, isOddPlugin);
            }
            String version = plugin.getVersion();
            if (version != null && version.length() > 0) {
                addRow(row++, "Version", version, isOddPlugin);
            }
            String filename = plugin.getFilename();
            if (filename != null) {
                addRow(row++, "Filename", filename, isOddPlugin);
            }

            isOddPlugin = !isOddPlugin;
        }
    }

}
