public class Intersection {
    Object obj;
    Ray ray;
    double dist;
    Vector intersectPoint;
    Vector hitNormal;
    public Intersection(Sphere obj, Ray ray, double dist, Vector intersectPoint, Vector hitNormal) {
        this.obj = obj;
        this.ray = ray;
        this.dist = dist;
        this.intersectPoint = intersectPoint;
        this.hitNormal = hitNormal;
    }
    public Intersection(Plane obj, Ray ray, double dist, Vector intersectPoint, Vector hitNormal) {
        this.obj = obj;
        this.ray = ray;
        this.dist = dist;
        this.intersectPoint = intersectPoint;
        this.hitNormal = hitNormal;
    }
    public Intersection(Sphere obj, Ray ray, double dist) {
        this.obj = obj;
        this.ray = ray;
        this.dist = dist;
    }
}
