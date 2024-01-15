import java.util.ArrayList;
import java.util.HashMap;

public class Face {
    private ArrayList<Point> points;
    private ArrayList<Point> normals;

    public Face() {
        points = new ArrayList<>();
        normals = new ArrayList<>();
    }

    public void addPoint(Point p, Point normal) {
        points.add(p);
        normals.add(normal);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }
}
