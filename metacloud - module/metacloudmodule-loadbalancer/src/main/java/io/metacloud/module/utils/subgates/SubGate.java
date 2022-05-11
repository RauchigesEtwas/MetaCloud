package de.rauchigesetwas.loadbalancer.utils.subgates;
/*
 * Created AT: 01.08.2021
 * Created by Robin B. (RauchigesEtwas)
 */

public class SubGate {

    public String proxyedtaskname;
    public String ip;
    public Integer port;


    public SubGate(String proxyedtaskname, String ip, Integer port) {
        this.proxyedtaskname = proxyedtaskname;
        this.ip = ip;
        this.port = port;
    }
}
