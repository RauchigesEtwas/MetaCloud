package io.metacloud;

import lombok.Getter;

public class Driver {

    @Getter
    private static Driver instance;
    @Getter
    private static CloudStorage cloudStorage = new CloudStorage();

    public Driver() {
        instance = this;
    }


    public static void main(String[] args) {
        System.out.println(cloudStorage.getCloudLogo());
    }
}
