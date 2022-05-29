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


    public ApiSendCloudPlayerDataPacket(String cloudPlayerName, String chosen, String message, String server) {
        this.cloudPlayerName = cloudPlayerName;
        this.chosen = chosen;
        this.message = message;
        this.server = server;
    }

    public ApiSendCloudPlayerDataPacket(String cloudPlayerName, String chosen, String message) {
        this.cloudPlayerName = cloudPlayerName;
        this.chosen = chosen;
        this.message = message;
    }

    public ApiSendCloudPlayerDataPacket(String cloudPlayerName, String chosen, CloudTextComponent component) {
        this.cloudPlayerName = cloudPlayerName;
        this.chosen = chosen;
        this.component = component;
    }

    public ApiSendCloudPlayerDataPacket(String cloudPlayerName, String chosen, String title, String subTitle, Integer fadeIn, Integer stay, Integer fadeOut) {
        this.cloudPlayerName = cloudPlayerName;
        this.chosen = chosen;
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getCloudPlayerName() {
        return cloudPlayerName;
    }

    public String getChosen() {
        return chosen;
    }

    public CloudTextComponent getComponent() {
        return component;
    }

    public String getMessage() {
        return message;
    }

    public String getServer() {
        return server;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Integer getFadeIn() {
        return fadeIn;
    }

    public Integer getStay() {
        return stay;
    }

    public Integer getFadeOut() {
        return fadeOut;
    }
}
