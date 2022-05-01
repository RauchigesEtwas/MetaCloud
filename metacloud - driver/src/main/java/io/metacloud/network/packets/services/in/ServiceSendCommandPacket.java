package io.metacloud.network.packets.services;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ServiceSendCommandPacket extends Packet {


    private String command;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("command", command);
    }

    @Override
    public void read(IBuffer buffer) {
        command = buffer.read("command", String.class);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
