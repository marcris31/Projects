package org.example;

import org.example.BusinessLogic.Controller;
import org.example.View.View;

public class Main {
    public static void main( String[] args ) throws InterruptedException {
        View view = new View();
        Controller controller = new Controller(view);
    }
}