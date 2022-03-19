package io.metacloud.configuration.configs.group;

import io.metacloud.configuration.configs.group.GroupVersions;

public class GroupProperties{
    private Integer percentForNewServerAutomatically;
    private GroupVersions version;
    private String template;
    private String node;

    public Integer getPercentForNewServerAutomatically() {
        return percentForNewServerAutomatically;
    }

    public void setPercentForNewServerAutomatically(Integer percentForNewServerAutomatically) {
        this.percentForNewServerAutomatically = percentForNewServerAutomatically;
    }

    public GroupVersions getVersion() {
        return version;
    }

    public void setVersion(GroupVersions version) {
        this.version = version;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
