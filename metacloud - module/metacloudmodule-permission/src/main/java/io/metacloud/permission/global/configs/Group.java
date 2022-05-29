package io.metacloud.permission.global.configs;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {


    private String name;
    private String prefix;
    private String suffix;
    private Boolean isDefault;
    private ArrayList<String> inherit;
    private HashMap<String , Boolean> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public ArrayList<String> getInherit() {
        return inherit;
    }

    public void setInherit(ArrayList<String> inherit) {
        this.inherit = inherit;
    }

    public HashMap<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashMap<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}
