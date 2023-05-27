package me.bjtmastermind.iconextracter;

public class Utils {

    public static String getMCPath() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return System.getProperty("user.home")+"\\AppData\\Roaming\\.minecraft\\";
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            return "~/Library/Application Support/minecraft/";
        } else {
            return System.getProperty("user.home")+"/.minecraft/";
        }
    }
}
