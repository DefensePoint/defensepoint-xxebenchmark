package com.defensepoint.xxebenchmark.util;

public class OSUtil {

    public enum OS {
        win, lin, mac, sol
    }

    private static OS os = null;

    public static OS getOS() {
        if (os == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.win;
            } else if (operSys.contains("nix") || operSys.contains("nux")
                    || operSys.contains("aix")) {
                os = OS.lin;
            } else if (operSys.contains("mac")) {
                os = OS.mac;
            } else if (operSys.contains("sunos")) {
                os = OS.sol;
            }
        }
        return os;
    }
}
