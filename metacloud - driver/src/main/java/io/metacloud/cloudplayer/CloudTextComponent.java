package io.metacloud.apidriver.cloudplayer.bin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class CloudTextComponent {

    private String component;
    private ArrayList<HashMap<ClickEventAction, Object>> clickEventAction;
    private ArrayList<HashMap<HoverEventAction, Object>> hoverEventAction;
    private ArrayList<String> extras;


    public CloudTextComponent(String component) {
        this.component = component;
        extras = new ArrayList<>();
        clickEventAction = new ArrayList<>();
        hoverEventAction = new ArrayList<>();
    }

    public CloudTextComponent setClickEvent(ClickEventAction clickEvent, Object object){
        HashMap<ClickEventAction, Object> paseEvent = new HashMap<>();
        paseEvent.put(clickEvent, object);
        this.clickEventAction.add(paseEvent);
        return this;
    }

    public CloudTextComponent setHoverEvent(HoverEventAction clickEvent, Object object){
        HashMap<HoverEventAction, Object> paseEvent = new HashMap<>();
        paseEvent.put(clickEvent, object);
        this.hoverEventAction.add(paseEvent);
        return this;
    }

    public CloudTextComponent addExtra(String message){
        extras.add(message);
        return this;
    }


    public String getComponent() {
        return component;
    }

    public ArrayList<HashMap<ClickEventAction, Object>> getClickEventAction() {
        return clickEventAction;
    }

    public ArrayList<HashMap<HoverEventAction, Object>> getHoverEventAction() {
        return hoverEventAction;
    }

    public ArrayList<String> getExtras() {
        return extras;
    }
}
