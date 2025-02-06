package org.example;

import org.example.session.Connection;

public class Main {
    public static void main(String[] args) {
        if (Connection.getInstance() != null ){
            System.out.println("Connection successful");
        }else {
            System.out.println("Connection not successful");
        }
    }
}
