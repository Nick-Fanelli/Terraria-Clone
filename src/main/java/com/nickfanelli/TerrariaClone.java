package com.nickfanelli;

import com.nickfanelli.graphics.StaticGraphics;

public class TerrariaClone {

    public static void main(String[] args) {

        StaticGraphics.initializeGraphics();

        Application application = new Application();
        application.startApplication();

        StaticGraphics.destroyGraphics();

    }

}
