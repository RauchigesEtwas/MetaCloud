package io.metacloud;

import lombok.Getter;

public class Driver {

    @Getter
    private static Driver instance;
    @Getter
    private static CloudStorageDriver cloudStorage = new CloudStorageDriver();

}
