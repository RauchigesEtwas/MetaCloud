package io.metacloud.configuration.configs;

import io.metacloud.configuration.IConfig;

import java.util.ArrayList;

public class ServiceConfiguration implements IConfig {

    private General general;
    private Communication communication;
    private Messages messages;

    public void setGeneral(General general) {
        this.general = general;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public General getGeneral() {
        return general;
    }

    public Communication getCommunication() {
        return communication;
    }

    public Messages getMessages() {
        return messages;
    }

    public static class Messages {
        private String prefix;
        private String maintenanceKickMessage;
        private String maintenanceGroupMessage;
        private String noFallbackKickMessage;
        private String fullNetworkKickMessage;
        private String fullServiceKickMessage;
        private String onlyProxyJoinKickMessage;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getMaintenanceKickMessage() {
            return maintenanceKickMessage;
        }

        public void setMaintenanceKickMessage(String maintenanceKickMessage) {
            this.maintenanceKickMessage = maintenanceKickMessage;
        }

        public String getMaintenanceGroupMessage() {
            return maintenanceGroupMessage;
        }

        public void setMaintenanceGroupMessage(String maintenanceGroupMessage) {
            this.maintenanceGroupMessage = maintenanceGroupMessage;
        }

        public String getNoFallbackKickMessage() {
            return noFallbackKickMessage;
        }

        public void setNoFallbackKickMessage(String noFallbackKickMessage) {
            this.noFallbackKickMessage = noFallbackKickMessage;
        }

        public String getFullNetworkKickMessage() {
            return fullNetworkKickMessage;
        }

        public void setFullNetworkKickMessage(String fullNetworkKickMessage) {
            this.fullNetworkKickMessage = fullNetworkKickMessage;
        }

        public String getFullServiceKickMessage() {
            return fullServiceKickMessage;
        }

        public void setFullServiceKickMessage(String fullServiceKickMessage) {
            this.fullServiceKickMessage = fullServiceKickMessage;
        }

        public String getOnlyProxyJoinKickMessage() {
            return onlyProxyJoinKickMessage;
        }

        public void setOnlyProxyJoinKickMessage(String onlyProxyJoinKickMessage) {
            this.onlyProxyJoinKickMessage = onlyProxyJoinKickMessage;
        }
    }

    public static class Communication {
        private String nodeAuthKey;
        private String managerHostAddress;
        private int networkingPort;
        private int restApiPort;
        private String restApiAuthKey;
        private ArrayList<String> whitelistAddresses;

        public String getNodeAuthKey() {
            return nodeAuthKey;
        }

        public void setNodeAuthKey(String nodeAuthKey) {
            this.nodeAuthKey = nodeAuthKey;
        }

        public String getManagerHostAddress() {
            return managerHostAddress;
        }

        public void setManagerHostAddress(String managerHostAddress) {
            this.managerHostAddress = managerHostAddress;
        }

        public int getNetworkingPort() {
            return networkingPort;
        }

        public void setNetworkingPort(int networkingPort) {
            this.networkingPort = networkingPort;
        }

        public int getRestApiPort() {
            return restApiPort;
        }

        public void setRestApiPort(int restApiPort) {
            this.restApiPort = restApiPort;
        }

        public String getRestApiAuthKey() {
            return restApiAuthKey;
        }

        public void setRestApiAuthKey(String restApiAuthKey) {
            this.restApiAuthKey = restApiAuthKey;
        }

        public ArrayList<String> getWhitelistAddresses() {
            return whitelistAddresses;
        }

        public void setWhitelistAddresses(ArrayList<String> whitelistAddresses) {
            this.whitelistAddresses = whitelistAddresses;
        }
    }

    public static class General{
        private boolean autoUpdate;
        private boolean proxyOnlineMode;
        private boolean showPlayerConnections;
        private int defaultProxyStartupPort;
        private int defaultServerStartupPort;
        private String serverSplitter;


        public boolean isShowPlayerConnections() {
            return showPlayerConnections;
        }

        public void setShowPlayerConnections(boolean showPlayerConnections) {
            this.showPlayerConnections = showPlayerConnections;
        }

        public boolean isAutoUpdate() {
            return autoUpdate;
        }

        public void setAutoUpdate(boolean autoUpdate) {
            this.autoUpdate = autoUpdate;
        }

        public boolean isProxyOnlineMode() {
            return proxyOnlineMode;
        }

        public void setProxyOnlineMode(boolean proxyOnlineMode) {
            this.proxyOnlineMode = proxyOnlineMode;
        }

        public int getDefaultProxyStartupPort() {
            return defaultProxyStartupPort;
        }

        public void setDefaultProxyStartupPort(int defaultProxyStartupPort) {
            this.defaultProxyStartupPort = defaultProxyStartupPort;
        }

        public int getDefaultServerStartupPort() {
            return defaultServerStartupPort;
        }

        public void setDefaultServerStartupPort(int defaultServerStartupPort) {
            this.defaultServerStartupPort = defaultServerStartupPort;
        }

        public String getServerSplitter() {
            return serverSplitter;
        }

        public void setServerSplitter(String serverSplitter) {
            this.serverSplitter = serverSplitter;
        }
    }
}
