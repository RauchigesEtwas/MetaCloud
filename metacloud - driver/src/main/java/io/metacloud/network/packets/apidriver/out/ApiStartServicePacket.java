package io.metacloud.network.packets.apidriver.out;

import io.metacloud.protocol.Buffer;
import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

public class ApiStartServicePacket extends Packet {

    private String group;
    private Integer count;

    @Override
    public void write(IBuffer buffer) {
        buffer.write("group", group);
        buffer.write("count", count);
    }

    @Override
    public void read(IBuffer buffer) {
        group =buffer.read("group",String.class);
        count = buffer.read("count", Integer.class);
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
