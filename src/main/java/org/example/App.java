package org.example;

import org.example.session.Connection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.view.AppController;
import org.example.view.Scenes;
import org.example.view.View;

import java.io.IOException;

/**

 JavaFX App*/
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static AppController currentController;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(Scenes.ROOT.getURL());
        View view = AppController.loadFXML(Scenes.ROOT);
        scene = new Scene(view.scene, 700, 700);
        currentController = (AppController) view.controller;
        currentController.onOpen(null);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/org/example/view/styles.css").toExternalForm()); }
    public static void main(String[] args) {
        launch();
    }

}