package io.metacloud.module.utils.networking.handler;
/*
 * Created AT: 23.07.2021
 * Created by Robin B. (UniqueByte)
 */

import io.metacloud.module.utils.util.PacketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.DecoderException;

import java.util.List;

public class LengthDeserializer extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        byte[] buf = new byte[3];
        for (int i = 0; i < buf.length; ++i) {
            int length;
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }
            buf[i] = in.readByte();
            if (buf[i] < 0) continue;
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.wrappedBuffer(buf);
                length = PacketUtils.readVarInt(buffer);
            } finally {
                if (buffer != null) {
                    buffer.release();
                }
            }
            if (length == 0) {
                throw new CorruptedFrameException("Empty packet!");
            }
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }
            if (length > 0x200000) {
                throw new DecoderException("Packet size of " + length + " is larger than protocol maximum of 2097152");
            }
            if (in.hasMemoryAddress()) {
                out.add(in.slice(in.readerIndex(), length).retain());
                in.skipBytes(length);
            } else {
                ByteBuf dst = ctx.alloc().directBuffer(length);
                in.readBytes(dst);
                out.add(dst);
            }
            return;
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
