package me.sankalpchauhan.popmovies.utils;

import java.io.IOException;

/**
 * A utility class for common methods
 */
public class Utility {

    public static boolean isOnline() {
        /**
         * ATTRIBUTION: isOnline() CODE
         * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
         **/
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
