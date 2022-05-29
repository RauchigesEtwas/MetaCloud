package io.metacloud.modules;

import java.util.HashMap;
import java.util.Properties;
import io.metacloud.Driver;
import io.metacloud.console.logger.enums.MSGType;
import java.io.File;
import java.util.ArrayList;

public class ModuleDriver {

    private ArrayList<LoadedModule> loadedModules;
    private boolean exists;

    public ModuleDriver() {
        this.loadedModules = new ArrayList<>();
    }

    public void enableAllModules(){
        ArrayList<String> modules = getModules();
        if (modules.isEmpty()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES, "no §bModule§7 was §bfound");
        }
        modules.forEach(moduleName -> {
            exists = false;
            this.loadedModules.forEach(loadedModule -> {
                if (loadedModule.getModuleName().equalsIgnoreCase(moduleName)){
                    exists = true;
                }
            });

            if (!exists){
                LoadedModule loadedModule = new LoadedModule(moduleName);
                this.loadedModules.add(loadedModule);
                Properties properties = loadedModule.load();
            }

        });
    }


    public void reloadAllModules(){
        enableAllModules();
        loadedModules.forEach(s -> {
            s.reload();
        });
    }

    public void disableAllModules(){
        loadedModules.forEach(s -> {
            s.unload();
        });
}

    public ArrayList<LoadedModule> getLoadedModules() {
        return loadedModules;
    }

    public HashMap<String, Properties> getAllProperties(){
        HashMap<String, Properties> loadedProperties = new HashMap<>();
        this.getModules().forEach(s -> {
            LoadedModule loadedModule = new LoadedModule(s);
            Properties properties = loadedModule.readProperties();
            loadedProperties.put(s, properties );

        });

        return loadedProperties;
    }

    private ArrayList<String> getModules() {
        File file = new File("./modules/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i != files.length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".jar")){
                String group = FirstFilter.split(".jar")[0];
                modules.add(group);
            }

        }
        return modules;
    }
}
