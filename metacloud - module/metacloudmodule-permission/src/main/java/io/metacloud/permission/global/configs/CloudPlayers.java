package io.metacloud.permission.global.configs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloudPlayers {
    private  String  uuid;
    private  String  name;
    private Map<String, Long> groups;
    private Map<String, Boolean> permissions;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Map<String, Long> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Long> groups) {
        this.groups = groups;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}
