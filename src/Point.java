public class Point {
    double x, y, z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceFrom(Point other) {
        return Math.sqrt(Math.pow(x - other.x,2) + Math.pow(y -other.y,2) + Math.pow(z -other.z,2));
    }
}
