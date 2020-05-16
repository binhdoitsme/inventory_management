package com.hanu.ims;

import com.hanu.ims.view.BatchListView;
import com.hanu.ims.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    // startup instance
    private Startup startupInstance;

    public App() {
    }

    public App configureSelf() {
        try {
            startupInstance.getConfigurations()
                    .configureDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    private <T extends Startup> App useStartup(Class<T> startupClass)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        startupInstance = startupClass.getDeclaredConstructor().newInstance();
        return this;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        new BatchListView().show();
//        new LoginView().show();
    }


    public static void main(String[] args) throws InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        new App().useStartup(Startup.class).configureSelf().launch(args);
    }
}
