package io.metacloud.global;

import java.util.ArrayList;

public class Motd {

    private String protocol;
    private String firstline;
    private String secondline;
    private ArrayList<String> playerinfos;


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getFirstline() {
        return firstline;
    }

    public void setFirstline(String firstline) {
        this.firstline = firstline;
    }

    public String getSecondline() {
        return secondline;
    }

    public void setSecondline(String secondline) {
        this.secondline = secondline;
    }

    public ArrayList<String> getPlayerinfos() {
        return playerinfos;
    }

    public void setPlayerinfos(ArrayList<String> playerinfos) {
        this.playerinfos = playerinfos;
    }
}
