package io.metacloud.network.packets.apidriver.out;

import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ApiSendMessage extends Packet {

    private String type;
    private String message;


    @Override
    public void write(IBuffer buffer) {
        buffer.write("type", type);
        buffer.write("message", message);
    }

    @Override
    public void read(IBuffer buffer) {
        message = buffer.read("message", String.class);
        type = buffer.read("type", String.class);

    }

    public ApiSendMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
