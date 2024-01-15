import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Model {

    private ArrayList<Point> vertices;
    private ArrayList<Face> faces;
    private Point centerPoint;
    private BoundingBox bounds;

    public Model(String fileName) {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        loadModel(fileName);
    }

    private void loadModel(String fileName) {
        File myObj = new File(fileName);
        Scanner stdin = null;
        double minx,miny,minz,maxx,maxy,maxz;
        minx=miny=minz=Integer.MAX_VALUE;
        maxx=maxy=maxz=Integer.MIN_VALUE;
        try {
            stdin = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (stdin.hasNextLine()) {
            String[] split = stdin.nextLine().split(" ");
            if (split[0].equals("v")) {
                Point point = new Point(
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3])
                );
                vertices.add(point);

                minx = Math.min(minx, point.x);
                maxx = Math.max(maxx, point.x);
                miny = Math.min(miny, point.y);
                maxy = Math.max(maxy, point.y);
                minz = Math.min(minz, point.z);
                maxz = Math.max(maxz, point.z);
            }
            else if (split[0].equals("f")) {
                Face current = new Face();
                for (int i=1;i<split.length;i++) {
                    String[] currentPoints = split[i].split("//?");
                    int a = Integer.parseInt(currentPoints[0])-1;
                    int b = Integer.parseInt(currentPoints[1])-1;
                    if (a<vertices.size() && b<vertices.size()) {
                        current.addPoint(vertices.get(a),vertices.get(b));
                    }
                }
                faces.add(current);
            }
        }
    }

    public ArrayList<Point> getVertices() {
        return vertices;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

}
