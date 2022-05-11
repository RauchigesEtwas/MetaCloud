package io.metacloud.network.packets.apidriver.out;

import io.metacloud.cloudplayer.CloudTextComponent;
import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ApiSendCloudPlayerDataPacket extends Packet {

    private String cloudPlayerName;
    private String chosen;
    private CloudTextComponent component;
    private String message;
    private String server;
    private String title;
    private String subTitle;
    private Integer fadeIn;
    private Integer stay;
    private Integer fadeOut;


    @Override
    public void write(IBuffer buffer) {
    buffer.write("cloudPlayerName", cloudPlayerName);
    buffer.write("chosen", chosen);
    buffer.write("component", component);
    buffer.write("server", server);
    buffer.write("message", message);
    buffer.write("title", title);
    buffer.write("subTitle", subTitle);
    buffer.write("fadeIn", fadeIn);
    buffer.write("stay", stay);
    buffer.write("fadeOut", fadeOut);
    }

    @Override
    public void read(IBuffer buffer) {
        cloudPlayerName = buffer.read("cloudPlayerName", String.class);
        chosen = buffer.read("chosen", String.class);
        server = buffer.read("server", String.class);
        message = buffer.read("message", String.class);
        title = buffer.read("title", String.class);
        subTitle = buffer.read("subTitle", String.class);
        fadeIn = buffer.read("fadeIn", Integer.class);
        stay = buffer.read("stay", Integer.class);
        fadeOut = buffer.read("fadeOut", Integer.class);
        component = buffer.read("component", CloudTextComponent.class);
    }

    public String getCloudPlayerName() {
        return cloudPlayerName;
    }

    public void setCloudPlayerName(String cloudPlayerName) {
        this.cloudPlayerName = cloudPlayerName;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public CloudTextComponent getComponent() {
        return component;
    }

    public void setComponent(CloudTextComponent component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(Integer fadeIn) {
        this.fadeIn = fadeIn;
    }

    public Integer getStay() {
        return stay;
    }

    public void setStay(Integer stay) {
        this.stay = stay;
    }

    public Integer getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(Integer fadeOut) {
        this.fadeOut = fadeOut;
    }
}
