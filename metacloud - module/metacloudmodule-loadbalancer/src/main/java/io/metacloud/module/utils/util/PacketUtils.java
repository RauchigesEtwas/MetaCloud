package io.metacloud.module.utils.util;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The type Packet utils.
 */
@UtilityClass
public class PacketUtils {
    /**
     * Read var int int.
     *
     * @param buffer the buffer
     * @return the int
     */
    public int readVarInt(ByteBuf buffer) {
        byte currentByte;
        int number = 0;
        int round = 0;
        do {
            currentByte = buffer.readByte();
            number |= (currentByte & 0x7F) << round++ * 7;
            if (round <= 5) continue;
            throw new RuntimeException("VarInt is too big");
        } while ((currentByte & 0x80) == 128);
        return number;
    }

    /**
     * Write var int.
     *
     * @param buffer the buffer
     * @param number the number
     */
    public void writeVarInt(ByteBuf buffer, int number) {
        while ((number & 0xFFFFFF80) != 0) {
            buffer.writeByte(number & 0x7F | 0x80);
            number >>>= 7;
        }
        buffer.writeByte(number);
    }

    /**
     * Gets var int length.
     *
     * @param number the number
     * @return the var int length
     */
    public int getVarIntLength(int number) {
        if ((number & 0xFFFFFF80) == 0) {
            return 1;
        }
        if ((number & 0xFFFFC000) == 0) {
            return 2;
        }
        if ((number & 0xFFE00000) == 0) {
            return 3;
        }
        if ((number & 0xF0000000) == 0) {
            return 4;
        }
        return 5;
    }

    /**
     * Read string string.
     *
     * @param buffer the buffer
     * @return the string
     * @throws IOException the io exception
     */
    public String readString(ByteBuf buffer) throws IOException {
        int length = PacketUtils.readVarInt(buffer);
        if (length > 32767) {
            throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + length + " > 32767)");
        }
        if (length < 0) {
            throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
        }
        byte[] stringBytes = new byte[length];
        buffer.readBytes(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

}