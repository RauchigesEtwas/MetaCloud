package io.metacloud.modules;

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
        ArrayList<String> modules = getModulesFiles();
        if (modules.isEmpty()){
            Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES, "no §bModule§7 was §bfound");
        }

        Thread execuet = new Thread(() -> {
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
        });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();
    }


    public void reloadAllModules(){
        enableAllModules();
        Thread execuet = new Thread(() -> {
        loadedModules.forEach(s -> {
            s.reload();
        });
        });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();

    }

    public void disableAllModules(){
        Thread execuet = new Thread(() -> {
        loadedModules.forEach(s -> {
            s.unload();
        });
    });
        execuet.setPriority(Thread.MIN_PRIORITY);
        execuet.run();

}

    public ArrayList<LoadedModule> getLoadedModules() {
        return loadedModules;
    }

    private ArrayList<String> getModulesFiles() {
        File file = new File("./modules/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String FirstFilter = files[i].getName();
            String group = FirstFilter.split(".jar")[0];
            modules.add(group);
        }
        return modules;
    }
}
