package io.metacloud;

public class CloudStorageDriver {

    private boolean cloudSetup;

    public CloudStorageDriver() {}


    public boolean isCloudSetup() {
        return cloudSetup;
    }

    public void setCloudSetup(boolean cloudSetup) {
        this.cloudSetup = cloudSetup;
    }

    public String getCloudLogo(){
        return "   __  ________________  _______   ____  __  _____ \n" +
                "  /  |/  / __/_  __/ _ |/ ___/ /  / __ \\/ / / / _ \\\n" +
                " / /|_/ / _/  / / / __ / /__/ /__/ /_/ / /_/ / // /\n" +
                "/_/  /_/___/ /_/ /_/ |_\\___/____/\\____/\\____/____/ \n" +
                "   ________________________________________________                                                 \n" +
                "   <!> The Minecraft CloudSystem | V: 1.0.0-ALPHA";
    }

    public String[] dropFirstString(String[] input){
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

}
