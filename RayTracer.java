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
      for(int col = 0; col < canvas[0].length; col++) {
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
    double xx = (2 * ((x + 0.5) * (1.0 / canvas[0].length)) - 1) * camera.angle * camera.aspectRatio;
    double yy = (1 - 2 * ((y + 0.5) * (1.0 / canvas.length))) * camera.angle;

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
    Vector objColor = new Vector(0, 0, 0);
    double albedo = 0;
    //Calculate diffuse shading. Vector.dot(hitNormal, lightDir)
    if (inter.obj instanceof Plane) {
      inter.obj = (Plane) inter.obj;
      objColor = ((Plane) inter.obj).color;
      albedo = ((Plane) inter.obj).albedo;
    }
    if (inter.obj instanceof Sphere) {
      inter.obj = (Sphere) inter.obj;
      objColor = ((Sphere) inter.obj).color;
      albedo = ((Sphere) inter.obj).albedo;
    }

    for(Light light : scene.lights) {
      Vector lightDir = Vector.norm(Vector.subtract(light.pos, intersectPoint));
      double facingRatio = Math.max(0.02, Vector.dot(hitNormal, lightDir));
      Vector reflectDir = Vector.norm(Vector.subtract(inter.ray.dir, Vector.multiply(2 * Vector.dot(hitNormal, inter.ray.dir), hitNormal)));
      Ray reflectRay = new Ray(intersectPoint, reflectDir);
      double hitScalar = Vector.dot(reflectDir, lightDir);
      finalColor = Vector.add(finalColor, hitScalar > 0.0 ? Vector.multiply(Math.pow(hitScalar, 20.0) * inShadow(intersectPoint, light, inter.obj), light.color) : new Vector(0, 0, 0));
      finalColor = Vector.add(finalColor, Vector.multiply(((albedo / Math.PI) * facingRatio * light.intensity) * inShadow(intersectPoint, light, inter.obj), Vector.multiplyVectors(objColor, light.color.fromRGB())));
    }
    return finalColor;
  }
  public double inShadow(Vector intersectPoint, Light light, Object interObj) {
    double shadowScalar = 0;
    for(int samples = 0; samples < 100; samples++) {
      double lightMag = Vector.subtract(light.pos, intersectPoint).mag();
      Vector lightDir = Vector.norm(Vector.subtract(Vector.add(light.pos, new Vector(Math.random(), Math.random(), Math.random())), intersectPoint));
      Ray shadowRay = new Ray(intersectPoint, lightDir);
      boolean intersectFound = false;
      for(Object obj : scene.objects) {
        Intersection shadowInter = obj.intersect(shadowRay);
        if(shadowInter.dist > 0 && shadowInter.dist != Float.POSITIVE_INFINITY && shadowInter.dist <= lightMag && obj != interObj) intersectFound = true;
      }
      if (!intersectFound) {
        shadowScalar++;
      }
    }
    return (shadowScalar + Math.random())/100;
  }
  public Intersection checkForIntersection(Ray ray) {
    //Not very complicated. This basically loops through all objects and gets the closest one so we can render it.
    //We use a king of the hill method to discard of anything but the closest intersection.
    Intersection closestInter = null;
    double closestDist = Float.POSITIVE_INFINITY;
    for(Object obj : scene.objects) {
      Intersection inter = obj.intersect(ray);
      if(inter.dist != Float.POSITIVE_INFINITY && inter.dist > 0 && inter.dist < closestDist) {
        closestInter = inter;
        closestDist = inter.dist;
      }
    }
    return closestInter;
  }
}
