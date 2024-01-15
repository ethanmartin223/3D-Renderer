import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Math.*;

public class Camera extends JPanel {

    private int viewWidth, viewHeight;
    private double x,y,z;
    private double lookX, lookY, lookZ;
    private double fov;

    private JFrame parent;
    private World world;

    private int fps;
    private long lastCallToFPS;

    double scale;
    int xDrag, yDrag, xPress, yPress;
    double xPosition, yPosition, lastReleasedPositionY, lastReleasedPositionX;

    public Camera(MainWindow mw, double x, double y, double z) {
        setPosition(x,y,z);
        fov = 1;

        parent = mw;
        parent.add(this, BorderLayout.CENTER);

        viewWidth = parent.getWidth();
        viewHeight = parent.getHeight();
        lastCallToFPS = System.currentTimeMillis();

        xPosition = 0;
        yPosition = 0;
        lastReleasedPositionX = 0;
        lastReleasedPositionY = 0;
        scale = 20;

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                lastReleasedPositionX = xPosition;
                lastReleasedPositionY = yPosition;
                setPosition( xPosition*.001,.1,yPosition*.001);

            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xDrag = (int) (xPosition+e.getX());
                yDrag = (int) (yPosition+e.getY());
                xPosition = lastReleasedPositionX-(xDrag-xPress)/scale;
                yPosition = lastReleasedPositionY-(yDrag-yPress)/scale;
                //System.out.println(xPosition+" "+yPosition);
                setPosition( xPosition*.001,.1,yPosition*.001);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                xPress = e.getX();
                yPress = e.getY();
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale+= (1+scale*.1);
                } else {
                    scale-= (1+scale*.1);
                }

            }
        });
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
        fps++;

        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0));

        for (Model m : world.getModels()) {

            //render vert normalized to camera
            double rx, ry;


            for (Point a : m.getVertices()) {
                rx = Math.atan((a.x - x)) / ((a.y - y));
                ry = Math.atan(((a.z - z)) / ((a.y - y)));
                double screenX = (double) viewWidth / 2 + (rx * (viewWidth / fov));
                double screenY = (double) viewHeight / 2 + (ry * (viewHeight / fov));
//                double dx = cos(lookY)*(sin(lookZ)*(a.y - y)+cos(lookZ)*(a.x - x))-sin(lookY)*(a.z - z);
//                double dy = sin(lookX)*(cos(lookY)*(a.z-z)+sin(lookY)*(sin(lookZ)*
//                        (a.y - y)+cos(lookZ) * (a.x - x))) + cos(lookX)*(cos(lookZ)*(a.y-y)-sin(lookZ)*(a.x-x));
//                double dz = cos(lookX)*(cos(lookY)*(a.z-z)+sin(lookY)*(sin(lookZ)*
//                        (a.y - y)+cos(lookZ) * (a.x - x))) - sin(lookX)*(cos(lookZ)*(a.y-y)-sin(lookZ)*(a.x-x));

                double sx = screenX;
                double sy = screenY;
//                double ex = 0.5;
//                double ey = 0.5;
//                double ez = 1 / tan(fov / 2);
//
//                System.out.println( (dx)+" "+(dy)+ " "+dz);
//                double screenX = (dx-.5)*(ez/dz);
//                double screenY = (dy-.5)*(ez/dz);
                //System.out.println("screenX: " + sx + " screenY: " + sy);
                g.fillRect((int) sx, (int) sy, 1, 1);
            }

            //render faces
            for (Face f : m.getFaces()) {
                ArrayList<Integer> xPoints = new ArrayList<>();
                ArrayList<Integer> yPoints = new ArrayList<>();
                for (Point a : f.getPoints()) {
                    rx = Math.atan((a.x - x)) / ((a.y - y));
                    ry = Math.atan(((a.z - z)) / ((a.y - y)));
                    double screenX = (double) viewWidth / 2 + (rx * (viewWidth / fov));
                    double screenY = (double) viewHeight / 2 + (ry * (viewHeight / fov));

                    xPoints.add((int) screenX);
                    yPoints.add((int) screenY);

                }

                if (!f.getPoints().isEmpty()) {
                    Point r = f.getPoints().get(0);
                    int c = (int) ((lerp(0,255,r.distanceFrom(new Point(x,y,z)))));
                    System.out.println(c);
                    //System.out.println(c);
                    g.setColor(new Color(c, c, c));
                    g.fillPolygon(xPoints.stream().mapToInt(i -> i).toArray(),
                            yPoints.stream().mapToInt(i -> i).toArray(), xPoints.size());
                }
            }
        }
    }


    public void renderFrame() {
        this.repaint();
    }

    public void renderFrame(boolean debug) {
        if (debug) {
            if (lastCallToFPS + 1000 < System.currentTimeMillis()) {
                lastCallToFPS = System.currentTimeMillis();
                System.out.println("FPS: " + fps+" \nCAMERA POS: "+x+" "+y+" "+z+"\n");
                fps = 0;
            }
        }
        renderFrame();
    }

    public void setRenderingWorld(World world) {
        this.world = world;
    }

    public void lookAt(double lx, double ly, double lz) {
        lookX = lx;
        lookY = ly;
        lookZ = lz;
    }

    public int getFps() {
        return fps;
    }

    private double lerp(double v0, double v1, double t) {
        return (1 - t) * v0 + t * v1;
    }
}
