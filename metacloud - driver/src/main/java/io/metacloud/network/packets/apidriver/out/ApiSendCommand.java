package io.metacloud.network.packets.apidriver.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ApiSendCommand extends Packet {

    private String command;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("command", command);
    }

    @Override
    public void read(IBuffer buffer) {
        command = buffer.read("command", String.class);
    }

    public ApiSendCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}

