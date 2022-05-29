package io.metacloud.module;

import io.metacloud.Driver;
import io.metacloud.NetworkingBootStrap;
import io.metacloud.configs.Configuration;
import io.metacloud.configs.LocationConfiguration;
import io.metacloud.configs.SignLayout;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.configuration.configs.ServiceConfiguration;
import io.metacloud.modules.interfaces.IModule;

import java.io.File;
import java.util.ArrayList;

public class SignsModule implements IModule {

    private static SignsModule instance;
    private NetworkingBootStrap networkingBootStrap;

    @Override
    public void onEnable() {
        instance = this;




        if (!new File("./modules/signs/config.json").exists()){
            new File("./modules/signs/").mkdirs();
            Configuration configuration = new Configuration();
            LocationConfiguration locationConfiguration = new LocationConfiguration();
            locationConfiguration.setSigns(new ArrayList<>());
            ArrayList<SignLayout> emptyLayouts = new ArrayList<>();
            SignLayout emptylayout01 = new SignLayout();
            emptylayout01.setLine0("§8◣ §r%service_name% §8◥");
            emptylayout01.setLine1("§bjoin");
            emptylayout01.setLine2("%motd%");
            emptylayout01.setLine3("%online_players%/%max_players%");

            SignLayout emptylayout02 = new SignLayout();
            emptylayout02.setLine0("§8◣ §r%service_name% §8◥");
            emptylayout02.setLine1("§alobby");
            emptylayout02.setLine2("%motd%");
            emptylayout02.setLine3("%online_players%/%max_players%");

            emptyLayouts.add(emptylayout01);
            emptyLayouts.add(emptylayout02);


            ArrayList<SignLayout> onlineLayouts = new ArrayList<>();
            SignLayout onlinelayout01 = new SignLayout();
            onlinelayout01.setLine0("§8◣ §r%service_name% §8◥");
            onlinelayout01.setLine1("§bjoin");
            onlinelayout01.setLine2("%motd%");
            onlinelayout01.setLine3("%online_players%/%max_players%");
            SignLayout onlinelayout02 = new SignLayout();
            onlinelayout02.setLine0("§8◣ §r%service_name% §8◥");
            onlinelayout02.setLine1("§alobby");
            onlinelayout02.setLine2("%motd%");
            onlinelayout02.setLine3("%online_players%/%max_players%");

            onlineLayouts.add(onlinelayout01);
            onlineLayouts.add(onlinelayout02);


            ArrayList<SignLayout> fullLayouts = new ArrayList<>();
            SignLayout fulllayout01 = new SignLayout();
            fulllayout01.setLine0("§8◣ §r%service_name% §8◥");
            fulllayout01.setLine1("§ejoin");
            fulllayout01.setLine2("%motd%");
            fulllayout01.setLine3("%full_players%/%max_players%");

            SignLayout fulllayout02 = new SignLayout();
            fulllayout02.setLine0("§8◣ §r%service_name% §8◥");
            fulllayout02.setLine1("§efull");
            fulllayout02.setLine2("%motd%");
            fulllayout02.setLine3("%full_players%/%max_players%");

            fullLayouts.add(fulllayout01);
            fullLayouts.add(fulllayout02);


            ArrayList<SignLayout> maintenanceLayouts = new ArrayList<>();
            SignLayout maintenancelayout01 = new SignLayout();
            maintenancelayout01.setLine0("");
            maintenancelayout01.setLine1("This group is currently");
            maintenancelayout01.setLine2("in §cmaintenance");
            maintenancelayout01.setLine3("");

            SignLayout maintenancelayout02 = new SignLayout();
            maintenancelayout02.setLine0("");
            maintenancelayout02.setLine1("This group is currently");
            maintenancelayout02.setLine2("in §c§lmaintenance");
            maintenancelayout02.setLine3("");

            maintenanceLayouts.add(maintenancelayout01);
            maintenanceLayouts.add(maintenancelayout02);


            ArrayList<SignLayout> loadingLayouts = new ArrayList<>();
            SignLayout loadinglayout01 = new SignLayout();
            loadinglayout01.setLine0("");
            loadinglayout01.setLine1("searching Server...");
            loadinglayout01.setLine2("o O o");
            loadinglayout01.setLine3("");

            SignLayout loadinglayout02 = new SignLayout();
            loadinglayout02.setLine0("");
            loadinglayout02.setLine1("searching Server...");
            loadinglayout02.setLine2("o o O");
            loadinglayout02.setLine3("");
            SignLayout loadinglayout03 = new SignLayout();
            loadinglayout03.setLine0("");
            loadinglayout03.setLine1("searching Server...");
            loadinglayout03.setLine2("o O o");
            loadinglayout03.setLine3("");
            SignLayout loadinglayout04 = new SignLayout();
            loadinglayout04.setLine0("");
            loadinglayout04.setLine1("searching Server...");
            loadinglayout04.setLine2("O o o");
            loadinglayout04.setLine3("");

            loadingLayouts.add(loadinglayout01);
            loadingLayouts.add(loadinglayout02);
            loadingLayouts.add(loadinglayout03);
            loadingLayouts.add(loadinglayout04);

            configuration.setEmpty(emptyLayouts);
            configuration.setOnline(onlineLayouts);
            configuration.setFull(fullLayouts);
            configuration.setMaintenance(maintenanceLayouts);
            configuration.setLoading(loadingLayouts);

            new ConfigDriver("./modules/signs/locations.json").save(locationConfiguration);
            new ConfigDriver("./modules/signs/config.json").save(configuration);
        }

        Configuration config = (Configuration) new ConfigDriver("./modules/signs/config.json").read(Configuration.class);
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);


        ArrayList<SignLayout> empty = new ArrayList<>();
        ArrayList<SignLayout> online = new ArrayList<>();
        ArrayList<SignLayout> full = new ArrayList<>();
        ArrayList<SignLayout> maintenance = new ArrayList<>();
        ArrayList<SignLayout> loading = new ArrayList<>();


        for (int i = 0; i !=config.getLoading().size() ; i++) {
            SignLayout layout = config.getLoading().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            loading.add(newLayout);
        }

        for (int i = 0; i !=config.getMaintenance().size() ; i++) {
            SignLayout layout = config.getMaintenance().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            maintenance.add(newLayout);
        }


        for (int i = 0; i !=config.getFull().size() ; i++) {
            SignLayout layout = config.getFull().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            full.add(newLayout);
        }

        for (int i = 0; i !=config.getEmpty().size() ; i++) {
            SignLayout layout = config.getEmpty().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            empty.add(newLayout);
        }

        for (int i = 0; i !=config.getOnline().size() ; i++) {
            SignLayout layout = config.getOnline().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            online.add(newLayout);
        }

        Configuration update = new Configuration();
        update.setEmpty(empty);
        update.setLoading(loading);
        update.setMaintenance(maintenance);
        update.setFull(full);
        update.setOnline(online);
        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("module-signs-layout", "./modules/signs/config.json", update);

        LocationConfiguration locationConfiguration = (LocationConfiguration)new ConfigDriver("./modules/signs/locations.json").read(LocationConfiguration.class);

        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).addContent("module-signs-locations", "./modules/signs/locations.json",  locationConfiguration);

        Driver.getInstance().getEventDriver().registerListener(new ModuleNetworkListener());
        pushToRest();

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        this.pushToRest();
    }


    public static SignsModule getInstance() {
        return instance;
    }


    public void pushToRest(){

        Configuration config = (Configuration) new ConfigDriver("./modules/signs/config.json").read(Configuration.class);
        ServiceConfiguration service = (ServiceConfiguration) new ConfigDriver("./service.json").read(ServiceConfiguration.class);


        ArrayList<SignLayout> empty = new ArrayList<>();
        ArrayList<SignLayout> online = new ArrayList<>();
        ArrayList<SignLayout> full = new ArrayList<>();
        ArrayList<SignLayout> maintenance = new ArrayList<>();
        ArrayList<SignLayout> loading = new ArrayList<>();


        for (int i = 0; i !=config.getLoading().size() ; i++) {
            SignLayout layout = config.getLoading().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            loading.add(newLayout);
        }

        for (int i = 0; i !=config.getMaintenance().size() ; i++) {
            SignLayout layout = config.getMaintenance().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            maintenance.add(newLayout);
        }


        for (int i = 0; i !=config.getFull().size() ; i++) {
            SignLayout layout = config.getFull().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            full.add(newLayout);
        }

        for (int i = 0; i !=config.getEmpty().size() ; i++) {
            SignLayout layout = config.getEmpty().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            empty.add(newLayout);
        }

        for (int i = 0; i !=config.getOnline().size() ; i++) {
            SignLayout layout = config.getOnline().get(i);
            SignLayout newLayout = new SignLayout();
            newLayout.setLine0(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine0()));
            newLayout.setLine1(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine1()));
            newLayout.setLine2(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine2()));
            newLayout.setLine3(Driver.getInstance().getStorageDriver().utf8ToUBase64(layout.getLine3()));

            online.add(newLayout);
        }

        Configuration update = new Configuration();
        update.setEmpty(empty);
        update.setLoading(loading);
        update.setMaintenance(maintenance);
        update.setFull(full);
        update.setOnline(online);
        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("module-signs-layout", update);


        LocationConfiguration locationConfiguration = (LocationConfiguration)new ConfigDriver("./modules/signs/locations.json").read(LocationConfiguration.class);

        Driver.getInstance().getRestDriver().getRestServer(service.getCommunication().getRestApiPort()).updateContent("module-signs-locations",  locationConfiguration);

    }
}
