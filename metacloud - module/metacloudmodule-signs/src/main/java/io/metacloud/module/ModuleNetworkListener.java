package io.metacloud.module;


import io.metacloud.configs.Location;
import io.metacloud.configs.LocationConfiguration;
import io.metacloud.configuration.ConfigDriver;
import io.metacloud.events.bin.EventHandler;
import io.metacloud.events.bin.ICloudEvent;
import io.metacloud.events.events.signs.CloudSignAddEvent;
import io.metacloud.events.events.signs.CloudSignRemoveEvent;

import java.util.ArrayList;

public class ModuleNetworkListener implements ICloudEvent {



    @EventHandler
    public void handelSignAddEvent(CloudSignAddEvent event){
        LocationConfiguration locs = (LocationConfiguration)(new ConfigDriver("./modules/signs/locations.json")).read(LocationConfiguration.class);
        Location location = new Location();
        location.setSignUUID(event.getSignUUID());
        location.setGroupName(event.getGroupName());
        location.setLocationPosX(Double.valueOf(event.getLocationPosX()));
        location.setLocationPosY(Double.valueOf(event.getLocationPosY()));
        location.setLocationPosZ(Double.valueOf(event.getLocationPosZ()));
        location.setLocationWorld(event.getLocationWorld());
        locs.getSigns().add(location);
        new ConfigDriver("./modules/signs/locations.json").save(locs);
        SignsModule.getInstance().pushToRest();
    }

    @EventHandler
    public void HandelSignRemove(CloudSignRemoveEvent event){
        LocationConfiguration locs = (LocationConfiguration)(new ConfigDriver("./modules/signs/locations.json")).read(LocationConfiguration.class);


        System.out.println("test");
        ArrayList<Location> locations = new ArrayList<>();
        locs.getSigns().forEach(location -> {
            if (!location.getSignUUID().equals(event.getSignuuid())){
                locations.add(location);
            }
        });

        locs.setSigns(locations);
        new ConfigDriver("./modules/signs/locations.json").save(locs);
        SignsModule.getInstance().pushToRest();

    }

}
