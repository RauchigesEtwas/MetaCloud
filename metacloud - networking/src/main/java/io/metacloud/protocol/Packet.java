package io.metacloud.protocol;

public abstract class Packet {

    public abstract void write(IBuffer buffer);

    public abstract void read(IBuffer buffer);
}
