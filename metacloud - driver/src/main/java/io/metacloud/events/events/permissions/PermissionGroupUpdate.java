package io.metacloud.events.events.permissions;

import io.metacloud.events.bin.IEventStack;

import java.util.List;
import java.util.Map;

public class PermissionGroupUpdate extends IEventStack {

    private String name;
    private Map<String, Boolean> permissions;
    private Boolean isDefault;
    private List<String> inherit;

    public PermissionGroupUpdate(String name, Map<String, Boolean> permissions, Boolean isDefault, List<String> inherit) {
        this.name = name;
        this.permissions = permissions;
        this.isDefault = isDefault;
        this.inherit = inherit;
    }

    public String getName() {
        return name;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }


    public Boolean getDefault() {
        return isDefault;
    }


    public List<String> getInherit() {
        return inherit;
    }


}
