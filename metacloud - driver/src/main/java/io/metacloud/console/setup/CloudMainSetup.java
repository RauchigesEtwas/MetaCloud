package io.metacloud.console.setup;

import io.metacloud.Driver;
import jline.console.ConsoleReader;

public class CloudMainSetup {

    public CloudMainSetup(ConsoleReader reader, String line) {
        switch (Driver.getInstance().getCloudStorage().getSetupStep()){
            case 0:

                break;
        }
    }
}
