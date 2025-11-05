import org.example.Material;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final List<Material> materials = new ArrayList<>();

    public void addMaterial(Material m) {
        materials.add(m);
    }

    public List<Material> getAllMaterials() {
        return materials;
    }

    public Material findMaterialById(int id) {
        for (Material m : materials) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public boolean removeMaterial(int id) {
        return materials.removeIf(m -> m.getId() == id);
    }
}
