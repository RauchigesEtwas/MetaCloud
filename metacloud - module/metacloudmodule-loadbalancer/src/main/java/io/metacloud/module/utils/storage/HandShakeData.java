package io.metacloud.module.utils.storage;

import io.metacloud.module.utils.util.PacketUtils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class HandShakeData {
    private int protocolVersion;
    private String hostname;
    private int port;
    private int nextState;

    /**
     * Read.
     *
     * @param buf the buf
     * @throws IOException the io exception
     */
    public void read(ByteBuf buf) throws IOException {
        this.protocolVersion = PacketUtils.readVarInt(buf);
        this.hostname = PacketUtils.readString(buf);
        this.port = buf.readUnsignedShort();
        this.nextState = PacketUtils.readVarInt(buf);
        if (this.hostname.endsWith("FML\u0000")) {
            this.hostname = this.hostname.substring(0, this.hostname.length() - 4);
        }
    }

    /**
     * Gets protocol version.
     *
     * @return the protocol version
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Sets protocol version.
     *
     * @param protocolVersion the protocol version
     */
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Gets hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * Sets hostname.
     *
     * @param hostname the hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets next state.
     *
     * @return the next state
     */
    public int getNextState() {
        return this.nextState;
    }

    /**
     * Sets next state.
     *
     * @param nextState the next state
     */
    public void setNextState(int nextState) {
        this.nextState = nextState;
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(Object other) {
        return other instanceof HandShakeData;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getProtocolVersion();
        result = result * 59 + this.getPort();
        result = result * 59 + this.getNextState();
        String $hostname = this.getHostname();
        result = result * 59 + ($hostname == null ? 43 : $hostname.hashCode());
        return result;
    }

    public String toString() {
        return "HandshakeData(protocolVersion=" + this.getProtocolVersion() + ", hostname=" + this.getHostname() + ", port=" + this.getPort() + ", nextState=" + this.getNextState() + ")";
    }
}
