import java.util.*;

public class Scene {
  Object[] objects;
  Light[] lights;
  Camera camera;
  public Scene(Object[] objects, Light[] lights, Camera camera) {
      this.objects = objects;
      this.lights = lights;
      this.camera = camera;
  }
}