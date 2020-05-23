package com.hanu.ims;

import java.lang.reflect.InvocationTargetException;

/**
 * Created as a separate class to be able to run after Maven JAR Build
 */
public class AppLauncher {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new App().useStartup(Startup.class).configureSelf().launch(args);
    }
}
