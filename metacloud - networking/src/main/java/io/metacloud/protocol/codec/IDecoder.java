package io.metacloud.protocol.codec;


import io.metacloud.protocol.Buffer;
import io.metacloud.protocol.IBuffer;
import io.metacloud.protocol.Packet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface IDecoder {


    Packet decode(Packet packet, Buffer buffer) throws IOException;

    class NetPacketDecoder implements IDecoder {

        @Override
        public Packet decode(Packet packet, Buffer buffer) {
            try {
                String clazz = buffer.read("clazz", String.class);
                if (clazz == null) return new Packet() {
                    @Override
                    public void write(IBuffer buffer) {}
                    @Override
                    public void read(IBuffer buffer) {}
                };

                Packet instance = (Packet) Class.forName(clazz).getConstructor().newInstance();
                instance.read(buffer);
                return instance;
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return new Packet() {
                @Override
                public void write(IBuffer buffer) {}
                @Override
                public void read(IBuffer buffer) {}
            };
        }
    }
}
