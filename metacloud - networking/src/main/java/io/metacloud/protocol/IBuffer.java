package io.metacloud.protocol;

import java.util.List;
import java.util.Map;

public interface  IBuffer {
    byte[] array();

    void write(String key, Object value);

    <T> T read(String key, Class<T> clazz);

    <T> List<T> readList(String key, Class<T> clazz);

    <T1, T2> Map<T1, T2> readMap(String key, Class<T1> clazz1, Class<T2> clazz2);

}
