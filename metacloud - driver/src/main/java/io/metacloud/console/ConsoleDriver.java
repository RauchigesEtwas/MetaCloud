package io.metacloud.console;

import io.metacloud.command.CommandDriver;

public class ConsoleDriver extends  Thread{


    private CommandDriver commandDriver;

    public ConsoleDriver(){
        this.commandDriver = new CommandDriver();

    }

    @Override
    public void run() {
        while (!isInterrupted() && isAlive()){

        }
    }
}
