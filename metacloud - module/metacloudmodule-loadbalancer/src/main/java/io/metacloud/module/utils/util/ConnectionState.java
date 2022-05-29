package io.metacloud.module.utils.util;

/**
 * The enum Connection state.
 */
public enum ConnectionState {
    /**
     * Default connection state.
     */
    DEFAULT,
    /**
     * Handshake received connection state.
     */
    HANDSHAKE_RECEIVED,
    /**
     * Connecting connection state.
     */
    CONNECTING,
    /**
     * Connected connection state.
     */
    CONNECTED,
    /**
     * Disconnected connection state.
     */
    DISCONNECTED;

}