package com.hanu.ims;

import com.hanu.ims.view.LoginView;
import com.hanu.ims.view.SupplierListView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class App extends Application {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    // startup instance
    private Startup startupInstance;

    App() {
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

    <T extends Startup> App useStartup(Class<T> startupClass)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        startupInstance = startupClass.getDeclaredConstructor().newInstance();
        return this;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
//        new BatchListView().show();
        new LoginView().show();
    }
}
