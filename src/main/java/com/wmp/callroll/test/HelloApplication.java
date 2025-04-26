package com.wmp.callroll.test;



import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class HelloApplication extends GameApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("HelloApplication");
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
    }
}