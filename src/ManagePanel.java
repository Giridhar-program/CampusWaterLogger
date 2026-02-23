import javax.swing.*;
import java.awt.*;

public class ManagePanel extends JPanel {

    private JTextField txtId;
    private JTextField txtLoc;

    public ManagePanel() {
        // Layout: 4 Rows, 2 Columns
        setLayout(new GridLayout(4, 2, 10, 20));
        Theme.stylePanel(this);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 300, 50)); 
        
        txtId = new JTextField();
        Theme.styleTextField(txtId);
        txtLoc = new JTextField();
        Theme.styleTextField(txtLoc);

        JButton btnAdd = new JButton("Add Dispenser");
        Theme.styleButton(btnAdd);
        JButton btnUpdate = new JButton("Update Location");
        Theme.styleButton(btnUpdate);
        JButton btnDelete = new JButton("Delete Dispenser");
        btnDelete.setBackground(new Color(180, 50, 50)); // Red for delete
        btnDelete.setForeground(Color.WHITE);

        // Row 1
        add(new JLabel("Dispenser ID (Required for all actions):"));
        add(txtId);
        
        // Row 2
        add(new JLabel("Location Name (For Add/Update):"));
        add(txtLoc);
        
        // Row 3
        add(btnAdd);
        add(btnUpdate);

        // Row 4
        add(new JLabel("")); // Spacer
        add(btnDelete);

        // --- Logic: CREATE ---
        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String loc = txtLoc.getText().trim();
            
            if(!id.isEmpty() && !loc.isEmpty()) {
                if(WaterLoggerApp.database.containsKey(id)) {
                    JOptionPane.showMessageDialog(this, "ID already exists!");
                    return;
                }
                Dispenser newDispenser = new Dispenser(id, loc, 0, "08:00 AM");
                WaterLoggerApp.database.put(id, newDispenser);
                
                if(DatabaseHandler.insertDispenser(newDispenser)) {
                    StaffPanel.refreshList();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Added!");
                } else {
                    JOptionPane.showMessageDialog(this, "Database Error!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill both fields.");
            }
        });

        // --- Logic: UPDATE ---
        btnUpdate.addActionListener(e -> {
            String id = txtId.getText().trim();
            String loc = txtLoc.getText().trim();

            if(!id.isEmpty() && !loc.isEmpty()) {
                if(WaterLoggerApp.database.containsKey(id)) {
                    Dispenser d = WaterLoggerApp.database.get(id);
                    d.location = loc; // Update the memory object
                    
                    if(DatabaseHandler.updateDispenser(d)) {
                        clearFields();
                        JOptionPane.showMessageDialog(this, "Updated!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Database Error!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Dispenser ID not found!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill both fields.");
            }
        });

        // --- Logic: DELETE ---
        btnDelete.addActionListener(e -> {
            String id = txtId.getText().trim();
            
            if(!id.isEmpty()) {
                if(WaterLoggerApp.database.containsKey(id)) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if(confirm == JOptionPane.YES_OPTION) {
                        WaterLoggerApp.database.remove(id); // Remove from memory
                        
                        if(DatabaseHandler.deleteDispenser(id)) {
                            StaffPanel.refreshList();
                            clearFields();
                            JOptionPane.showMessageDialog(this, "Deleted!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Database Error!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Dispenser ID not found!");
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Please enter an ID to delete.");
            }
        });
    }

    private void clearFields() {
        txtId.setText("");
        txtLoc.setText("");
    }
}