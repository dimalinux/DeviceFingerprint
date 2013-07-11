APPLET_NAME=DeviceInfoApplet
#PLUGIN_JAR="C:\Program Files\Java\jre6\lib\plugin.jar"
PLUGIN_JAR="/usr/lib/jvm/java-6-sun/jre/lib/plugin.jar"
INSTALL_DIR=../webapp

$(INSTALL_DIR)/$(APPLET_NAME).class.gz: $(INSTALL_DIR)/$(APPLET_NAME).class
	gzip --best -c $< > $@

$(INSTALL_DIR)/$(APPLET_NAME).class: $(APPLET_NAME).java
	javac -cp $(PLUGIN_JAR) -d $(INSTALL_DIR) -source 1.4 -target 1.4 $(APPLET_NAME).java

clean:
	/bin/rm -f $(INSTALL_DIR)/$(APPLET_NAME).class $(INSTALL_DIR)/$(APPLET_NAME).class.gz
