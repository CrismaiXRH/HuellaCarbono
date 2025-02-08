package org.example.view;

import javax.security.auth.login.LoginContext;

public enum Scenes {
    ROOT("/org/example/view/layout.fxml"),
    PRIMARY("/org/example/view/primary.fxml"),
    LOGIN("/org/example/view/login.fxml"),
    REGISTER("/org/example/view/register.fxml"),
    INFORMATION("/org/example/view/information.fxml"),
    HABIT("/org/example/view/anadirhabito.fxml");

    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}