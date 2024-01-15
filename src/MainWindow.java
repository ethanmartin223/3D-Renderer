import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MainWindow extends JFrame {

    public MainWindow() {

        //win settings
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setTitle("Renderer");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        //create Main Camera
        Camera camera = new Camera(this, 0,.15,0);
        camera.lookAt(0,0,0);

        //create world for camera to render
        World world = new World();
        world.assignCamera(camera);

        world.addModel(new Model("Rook.obj"));

        double i = 0;
        camera.setPosition(0,.1,0);

        while (true) {
            camera.renderFrame(true);
        }
    }


    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        MainWindow window = new MainWindow();
    }
}