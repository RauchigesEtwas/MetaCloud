package io.metacloud;

public class Utils {

    public static byte[] nullChar(byte[] bytes) {
        byte[] result = new byte[bytes.length + 1];
        byte[] nullByte = new byte[1];
        nullByte[0] = -1;
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        System.arraycopy(nullByte, 0, result, bytes.length, 1);
        return result;
    }
}
