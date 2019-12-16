import java.util.*;

public class Scene {
  Sphere[] objects;
  List<Light> lights;
  Camera camera;
  public Scene(List<Sphere> objects, List<Light> lights, Camera camera) {
      this.objects = objects;
      this.lights = lights;
      this.camera = camera;
  }
}