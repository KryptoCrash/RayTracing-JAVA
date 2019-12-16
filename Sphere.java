public class Sphere {
  Vector pos;
  double radius;
  Vector color;
  double albedo;
  public Sphere(Vector pos, double radius, Vector color, double albedo) {
      this.pos = pos;
      this.radius = radius;
      this.color = color;
      this.albedo = albedo;
  }
  public Intersection intersect(Ray ray) {
      Vector origin = ray.origin;
      Vector dir = ray.dir;
      Vector distToRay = Vector.subtract(origin, pos);
      double a = Vector.dot(dir, dir);
      double b = 2 * Vector.dot(dir, distToRay);
      double c = Vector.dot(distToRay, distToRay) - Math.pow(radius, 2);
      double discriminator = Math.pow(b, 2) - 4 * a * c;
      if(discriminator < 0) {
          return new Intersection(this, ray, Float.POSITIVE_INFINITY);
      }
      double dist = (-b - Math.sqrt(discriminator)) / 2 * a;
      Vector intersectPoint = Vector.add(origin, Vector.multiply(dist, dir));
      Vector hitNormal = Vector.norm(Vector.subtract(intersectPoint, pos));
      return new Intersection(this, ray, dist, intersectPoint, hitNormal);
  }
}