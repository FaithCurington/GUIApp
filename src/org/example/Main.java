import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Your main application window
public class Main extends JFrame {
    // GUI components
    private JButton bnLoadData;
    private JButton bnDisplay;
    private JButton bnAdd;
    private JButton bnUpdate;
    private JButton bnCalculate;
    private JButton bnExit;
    private JButton bnRemove;
    private JLabel lbMenu;
    private JPanel mainPanel;

    // Data manager
    private final DataManager dataManager = new DataManager();

    // ✅ Constructor (where we build the GUI and add action listeners)
    public Main() {
        // Set up window basics
        setContentPane(mainPanel);
        setTitle("Minecraft Materials DMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // ✅ Add Action Listeners for each button

        // LOAD DATA
        bnLoadData.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Loading materials from file... (feature coming soon)"));

        // DISPLAY MATERIALS
        bnDisplay.addActionListener(e -> openDisplayMaterialsWindow());

        // ADD MATERIAL
        bnAdd.addActionListener(e -> openAddMaterialDialog());

        // UPDATE MATERIAL
        bnUpdate.addActionListener(e -> openUpdateMaterialDialog());

        // CALCULATE USEFULNESS
        bnCalculate.addActionListener(e -> openCalculateDialog());

        // REMOVE MATERIAL
        bnRemove.addActionListener(e -> openRemoveDialog());

        // EXIT PROGRAM
        bnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // ✅ Helper Methods (These go OUTSIDE the constructor but INSIDE the class)

    // ---------------- Add Material ----------------
    private void openAddMaterialDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField durabilityField = new JTextField();
        JTextField hardnessField = new JTextField();
        JTextField specialField = new JTextField();

        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Category:", categoryField,
                "Durability:", durabilityField,
                "Hardness:", hardnessField,
                "Special Property:", specialField
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Add New Material", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Material m = new Material(
                        Integer.parseInt(idField.getText()),
                        nameField.getText(),
                        categoryField.getText(),
                        Integer.parseInt(durabilityField.getText()),
                        Integer.parseInt(hardnessField.getText()),
                        specialField.getText()
                );
                dataManager.addMaterial(m);
                JOptionPane.showMessageDialog(this, "Material added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers for ID, Durability, and Hardness.");
            }
        }
    }

    // ---------------- Display Materials ----------------
    private void openDisplayMaterialsWindow() {
        List<Material> list = dataManager.getAllMaterials();

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No materials found!");
            return;
        }

        String[] columns = {"ID", "Name", "Category", "Durability", "Hardness", "Special Property"};
        Object[][] data = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            Material m = list.get(i);
            data[i][0] = m.getId();
            data[i][1] = m.getName();
            data[i][2] = m.getCategory();
            data[i][3] = m.getDurability();
            data[i][4] = m.getHardness();
            data[i][5] = m.getSpecialProperty();
        }

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame tableFrame = new JFrame("All Materials");
        tableFrame.add(scrollPane);
        tableFrame.setSize(700, 300);
        tableFrame.setLocationRelativeTo(this);
        tableFrame.setVisible(true);
    }

    // ---------------- Update Material ----------------
    private void openUpdateMaterialDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Material ID to update:");
        if (input != null) {
            try {
                int id = Integer.parseInt(input);
                Material m = dataManager.findMaterialById(id);

                if (m == null) {
                    JOptionPane.showMessageDialog(this, "Material not found.");
                    return;
                }

                JTextField categoryField = new JTextField(m.getCategory());
                JTextField durabilityField = new JTextField(String.valueOf(m.getDurability()));
                JTextField hardnessField = new JTextField(String.valueOf(m.getHardness()));
                JTextField specialField = new JTextField(m.getSpecialProperty());

                Object[] message = {
                        "Category:", categoryField,
                        "Durability:", durabilityField,
                        "Hardness:", hardnessField,
                        "Special Property:", specialField
                };

                int option = JOptionPane.showConfirmDialog(this, message,
                        "Update Material", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        m.setCategory(categoryField.getText());
                        m.setDurability(Integer.parseInt(durabilityField.getText()));
                        m.setHardness(Integer.parseInt(hardnessField.getText()));
                        m.setSpecialProperty(specialField.getText());
                        JOptionPane.showMessageDialog(this, "Material updated!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number input.");
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        }
    }

    // ---------------- Remove Material ----------------
    private void openRemoveDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Material ID to remove:");
        if (input != null) {
            try {
                int id = Integer.parseInt(input);
                boolean removed = dataManager.removeMaterial(id);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Material removed!");
                } else {
                    JOptionPane.showMessageDialog(this, "Material not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID input.");
            }
        }
    }

    // ---------------- Calculate Usefulness ----------------
    private void openCalculateDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Material ID:");
        if (input != null) {
            try {
                int id = Integer.parseInt(input);
                Material m = dataManager.findMaterialById(id);
                if (m != null) {
                    double score = (m.getDurability() * 0.6) + (m.getHardness() * 0.4);
                    JOptionPane.showMessageDialog(this, "Usefulness Score for " +
                            m.getName() + ": " + score);
                } else {
                    JOptionPane.showMessageDialog(this, "Material not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID input.");
            }
        }
    }

    // ---------------- Main Entry ----------------
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        new Main();
    }
}

