public class Vector {
    double x, y, z;
  public Vector(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
  }
  public Vector neg() {
      return new Vector(-x, -y, -z);
  }
  public Vector abs() {
        return new Vector(Math.abs(z), Math.abs(y), Math.abs(z));
  }
  public double mag() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
  }
  public Vector fromRGB() {
      return new Vector(x/255, y/255,z/255);
  }
  public static Vector add(Vector v1, Vector v2) {
      return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
  }
  public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
  }
  public static Vector multiply(double scalar, Vector v1) {
        return new Vector(v1.x * scalar, v1.y * scalar, v1.z * scalar);
  }
  public static Vector norm(Vector v1) {
      double mag = v1.mag();
      double div = (mag == 0) ? Float.POSITIVE_INFINITY : 1.0 / mag;
      return Vector.multiply(div, v1);
  }
  public static Vector multiplyVectors(Vector v1, Vector v2) {
      return new Vector(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
  }
  public static double dot(Vector v1, Vector v2) {
      return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
  }
  public static Vector cross(Vector v1, Vector v2) {
      return new Vector(
              v1.y * v2.z - v1.z * v2.y,
              v1.z * v2.x - v1.x * v2.z,
              v1.x * v2.y - v1.y * v2.x
      );
  }
}