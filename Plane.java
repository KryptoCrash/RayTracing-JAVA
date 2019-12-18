public class Plane extends Object {
    Vector normal;
    Vector color;
    double albedo;
    public Plane(Vector normal, Vector color, double albedo) {
        this.normal = normal;
        this.color = color;
        this.albedo = albedo;
    }
    public Intersection intersect(Ray ray) {
        double dist = -(Vector.dot(ray.origin, normal)) / Vector.dot(ray.dir, normal);
        Vector intersectPoint = Vector.add(ray.origin, Vector.multiply(dist, ray.dir));
        return new Intersection((Plane) this, ray, dist, intersectPoint, normal);
    }
}
