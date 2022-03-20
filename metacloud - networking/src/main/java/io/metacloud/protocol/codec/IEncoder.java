package io.metacloud.protocol.codec;

import io.metacloud.Utils;
import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public interface IEncoder {

    void encode(DataOutputStream output, Packet packet, IBuffer buffer) throws IOException;

    class NetPacketEncoder implements IEncoder {
        @Override
        public void encode(DataOutputStream output, Packet packet, IBuffer buffer) throws IOException {
            buffer.write("clazz", packet.getClass().getName());
            packet.write(buffer);
            output.write(Utils.nullChar(buffer.array()));
            output.flush();
        }
    }
}
