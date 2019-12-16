public class Camera {
  Vector pos;
  Vector facing;
  double fov;
  double aspectRatio;
  double angle;
  Vector forward;
  Vector right;
  Vector up;
  Vector upg;
  public Camera(Vector pos, Vector facing, double fov, double aspectRatio) {
      this.pos = pos;
      this.facing = facing;
      this.angle = Math.tan(Math.PI * 0.5 * fov / 180);
      this.aspectRatio = aspectRatio;
      this.upg = new Vector(0, -1, 0);
      this.forward = Vector.norm(Vector.subtract(facing, pos));
      this.right = Vector.norm(Vector.cross(this.forward, this.upg));
      this.up = Vector.cross(this.forward, this.right);
  }
}