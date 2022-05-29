package io.metacloud.modules;


import io.metacloud.Driver;
import io.metacloud.console.logger.enums.MSGType;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LoadedModule {

    private String moduleName;
    private File file;
    private Properties properties;

    public LoadedModule(String moduleName) {
        this.moduleName = moduleName;
        this.file = new File("./modules/" + this.moduleName + ".jar");
    }


    public Properties readProperties(){
        if (this.file == null) {
            return null;
        }

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.properties");
                if (jarEntry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        properties = new Properties();
                        properties.load(reader);

                        return properties;
                    } catch (Exception ignored) {
                    }

                } else {

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,  "No §bmodule.properties §7found");

                }

            } catch (Exception ignored) {
            }
        } catch (Exception e) {

        }
        return null;
    }


    public Properties getProperties() {
        return properties;
    }

    public Properties load() {

        if (this.file == null) {
            return null;
        }

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.properties");
                if (jarEntry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        properties = new Properties();
                        properties.load(reader);

                        Class classtoLoad = Class.forName(properties.getProperty("main"), true, classLoader);
                        Method method = classtoLoad.getDeclaredMethod("onEnable");
                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,  "module §b" + properties.getProperty("name") + "§7 was loaded with §aSuccess §7(Author(s): §b" + properties.getProperty("author") + "§7, Version: §b" + properties.getProperty("version") + "§7)");
                        return properties;
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }

                } else {

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,  "No §bmodule.properties §7found");

                }

            } catch (Exception ignored) {
            }
        } catch (Exception e) {

        }
        return null;

    }

    public void unload() {
        if (this.file == null){
            return;
        }

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.properties");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        Properties properties = new Properties();
                        properties.load(reader);



                        Class classtoLoad = Class.forName(properties.getProperty("main"), true, classLoader);
                        Method method = classtoLoad.getDeclaredMethod("onDisable");
                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);

                    }catch (Exception ignored){}

                }else {

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,  "No §bmodule.properties §7found");

                }

            }catch (Exception ignored){}
        }catch (Exception e){

        }



    }

    public void reload(){
        if (this.file == null){
            return;
        }

        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.properties");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        Properties properties = new Properties();
                        properties.load(reader);



                        Class classtoLoad = Class.forName(properties.getProperty("main"), true, classLoader);
                        Method method = classtoLoad.getDeclaredMethod("onReload");
                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);
                        Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,   "module §b"+properties.getProperty("name")+" §7was reloaded");

                    }catch (Exception ignored){}

                }else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_MODULES,   "No §bmodule.properties §7found (Jar-Name: §b"+this.moduleName+"§7)");
                }

            }catch (Exception ignored){}
        }catch (Exception e){

        }
    }

    public String getModuleName() {
        return moduleName;
    }
}
