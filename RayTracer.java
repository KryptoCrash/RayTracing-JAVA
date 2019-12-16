import java.util.*;

public class RayTracer {
  Scene scene;
  Camera camera;
  Vector[][] canvas;
  public RayTracer(Scene scene, Vector[][] canvas) {
    this.scene = scene;
    this.camera = scene.camera;
    this.canvas = canvas;
  }
  public Vector[][] render() {
    for(int row = 0; row < canvas.length; row++) {
      for(int col = 0; col < canvas.length; col++) {
        Vector pixelColor = shootRay(new Ray(camera.pos, getDir(col, row)));
        canvas[row][col] = pixelColor;
      }
    }
    return canvas;
  }
  public Vector getDir(int x, int y) {
    //Really complicated math that I don't understand but works. This is essentially gets the direction from the camera to the canvas in 3d space.
    //This allows us to shoot a ray in the correct direction into the scene.

    //Get the center of each pixel instead of the top left coordinate.
    double xx = (2 * ((x + 0.5) * (1 / canvas[0].length)) - 1) * camera.angle * camera.aspectRatio;
    double yy = (1 - 2 * ((y + 0.5) * (1 / canvas.length))) * camera.angle;

    //We use the pre-made camera unit vectors to map our pixel centers in relation to the camera in 3d space.
    //Multiply the pixel centers by their corresponding vector to get a 1d representation of the pixel in 3d space.
    //Add these pixel centers to get the 2d representation of the pixel on the 3d space. (the point where the ray we want to shoot intersects the 3d canvas)
    //Add this to the camera's forward vector to get the vector from the camera to this point.
    //Normalize because that is probably a good idea.
    return Vector.norm(
            Vector.add(
                    camera.forward,
                    Vector.add(
                            Vector.multiply(xx, camera.right),
                            Vector.multiply(yy, camera.up)
                    )
            )
    );
  }
  public Vector shootRay(Ray ray) {
    //Helper method. If no intersection is found return black.
    Intersection inter = checkForIntersection(ray);
    if(inter != null) {
      return shade(ray, inter);
    }
    return new Vector(0, 0, 0);
  }
  public Vector shade(Ray ray, Intersection inter) {
    Vector intersectPoint = inter.intersectPoint;
    Vector hitNormal = inter.hitNormal;
    Vector finalColor = new Vector(0, 0,0);
    //Calculate diffuse shading. Vector.dot(hitNormal, lightDir)
    double albedo = inter.obj.albedo;
    Vector objColor = inter.obj.color;
    for(Light light : scene.lights) {
      Vector lightDir = Vector.norm(Vector.subtract(light.pos, intersectPoint));
      double facingRatio = Math.max(0.02, Vector.dot(hitNormal, lightDir));
      finalColor = Vector.add(finalColor, Vector.multiply(((albedo / Math.PI) * facingRatio * light.intensity), Vector.multiplyVectors(objColor, light.color.fromRGB())));
    }
    return finalColor;
  }
  public Intersection checkForIntersection(Ray ray) {
    //Not very complicated. This basically loops through all objects and gets the closest one so we can render it.
    //We use a king of the hill method to discard of anything but the closest intersection.
    Intersection closestInter = null;
    double closestDist = Float.POSITIVE_INFINITY;
    for(Sphere obj : scene.objects) {
      Intersection inter = obj.intersect(ray);
      if(inter.dist != Float.POSITIVE_INFINITY && inter.dist > 0 && inter.dist < closestDist) {
        closestInter = inter;
        closestDist = inter.dist;
      }
    }
    return closestInter;
  }
}