import java.util.ArrayList;

public class World {

    Camera mainCamera;
    ArrayList<Model> objects;

    public World() {
        objects = new ArrayList<Model>();
    }

    public void assignCamera(Camera camera) {
        mainCamera = camera;
        camera.setRenderingWorld(this);
    }

    public void addModel(Model model) {
        objects.add(model);
    }

    public void removeModel(Model model) {
        objects.remove(model);
    }

    public ArrayList<Model> getModels() {
        return objects;
    }

}
