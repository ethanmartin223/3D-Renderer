import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

import static java.lang.Math.*;

public class Camera extends JPanel {

    private int viewWidth, viewHeight;
    private double x,y,z;
    private double lookX, lookY, lookZ;
    private double fov;

    private JFrame parent;
    private World world;

    public Camera(MainWindow mw, double x, double y, double z) {
        setPosition(x,y,z);
        fov = 1;

        parent = mw;
        parent.add(this, BorderLayout.CENTER);

        viewWidth = parent.getWidth();
        viewHeight = parent.getHeight();
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setFov(double deg) {
        this.fov = deg;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0));

        for (Model m : world.getModels()) {
            double rx, ry;
            for (Point a : m.getVertices()) {
                rx = Math.atan((a.x - x)) / ((a.y - y));
                ry = Math.atan(((a.z - z)) / ((a.y - y)));
                double screenX = (double) viewWidth / 2 + (rx * (viewWidth / fov));
                double screenY = (double) viewHeight / 2 + (ry * (viewHeight / fov));
                double dx = cos(lookY)*(sin(lookZ)*(a.y - y)+cos(lookZ)*(a.x - x))-sin(lookY)*(a.z - z);
                double dy = sin(lookX)*(cos(lookY)*(a.z-z)+sin(lookY)*(sin(lookZ)*
                        (a.y - y)+cos(lookZ) * (a.x - x))) + cos(lookX)*(cos(lookZ)*(a.y-y)-sin(lookZ)*(a.x-x));
                double dz = cos(lookX)*(cos(lookY)*(a.z-z)+sin(lookY)*(sin(lookZ)*
                        (a.y - y)+cos(lookZ) * (a.x - x))) - sin(lookX)*(cos(lookZ)*(a.y-y)-sin(lookZ)*(a.x-x));

                double sx = screenX;
                double sy = screenY;
//                double ex = 0.5;
//                double ey = 0.5;
//                double ez = 1 / tan(fov / 2);
//
//                System.out.println( (dx)+" "+(dy)+ " "+dz);
//                double screenX = (dx-.5)*(ez/dz);
//                double screenY = (dy-.5)*(ez/dz);
                System.out.println("screenX: " + sx + " screenY: " + sy);
                g.fillRect((int) sx, (int) sy, 1, 1);
            }
        }
    }


    public void renderFrame() {
        this.repaint();
    }

    public void setRenderingWorld(World world) {
        this.world = world;
    }

    public void lookAt(double lx, double ly, double lz) {
        lookX = lx;
        lookY = ly;
        lookZ = lz;
    }
}
