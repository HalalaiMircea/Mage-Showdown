package com.mageshowdown.utils;

import java.util.Locale;

public class OSDetector {
    public enum OSType {
        WINDOWS, LINUX, MACOS, OTHER
    }

    public static OSType getOSType() {
        String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        OSType detectedOS;
        if ((OS.contains("mac")) || (OS.contains("darwin")))
            detectedOS = OSType.MACOS;
        else if (OS.contains("win"))
            detectedOS = OSType.WINDOWS;
        else if (OS.contains("nux"))
            detectedOS = OSType.LINUX;
        else
            detectedOS = OSType.OTHER;
        return detectedOS;
    }
}
