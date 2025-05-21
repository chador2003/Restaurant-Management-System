import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import Restaurant.*;
import Restaurant.Menu;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AdminPageUI extends JFrame{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   
    private JComboBox<String> statusComboBox; // Declare JComboBox for order status

    private EmployeeDAO employeeDAO = new EmployeeDAO(); // Assuming this is instantiated
    private DiscountStrategyDAO DiscountStrategyDAO;

    private float discountPercentage = 0.0f; // Discount state

    public AdminPageUI() {
        DiscountStrategyDAO = new DiscountStrategyDAO();
        setTitle("Admin Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Title label
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10)); // Adjusted to 7 rows
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the buttons

        // Create and add buttons
        JButton manageInventoryButton = createButton("Manage Inventory", e -> manageInventory());
        JButton manageMenuButton = createButton("Manage Menu", e -> manageMenu());
        JButton tableStateButton = createButton("Manage Table State", e -> manageTableState());
        JButton viewOrderButton = createButton("Manage Orders", e -> viewOrder());
        JButton manageEmployeeButton = createButton("Manage Employee", e -> showEmployeeManagementDialog());
        JButton setDiscountButton = createButton("Set Discount", e -> setDiscount());
        JButton logoutButton = createButton("Logout", e -> logout());

        buttonPanel.add(manageInventoryButton);
        buttonPanel.add(manageMenuButton);
        buttonPanel.add(tableStateButton);
        buttonPanel.add(viewOrderButton);
        buttonPanel.add(manageEmployeeButton); // Add the manage employee button
        buttonPanel.add(setDiscountButton); // Add the set discount button
        buttonPanel.add(logoutButton); // Add the logout button

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private void setDiscount() {
        String[] options = {"0%", "10%", "20%"};
        String selectedOption = (String) JOptionPane.showInputDialog(this, "Select discount percentage:",
                "Set Discount", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selectedOption != null) {
            float discountPercentage = Float.parseFloat(selectedOption.replace("%", "")) / 100;
            DiscountStrategyDAO.setDiscountState(discountPercentage);
            JOptionPane.showMessageDialog(this, "Discount set to " + selectedOption, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }
    // Helper method to create buttons
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        return button;
    }

    private void manageInventory() {
        JFrame inventoryManagementFrame = new JFrame("Inventory Management");
        inventoryManagementFrame.setSize(400, 300);
        inventoryManagementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inventoryManagementFrame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // Increased spacing between rows
    
        // Add Inventory Item Button
        JButton addInventoryButton = createButton("Add Inventory Item", e -> addInventory());
        panel.add(addInventoryButton);
    
        // Remove Inventory Item Button
        JButton removeInventoryButton = createButton("Remove Inventory Item", e -> removeInventory());
        panel.add(removeInventoryButton);
    
        // Update Inventory Item Button
        JButton updateInventoryButton = createButton("Update Inventory Item", e -> updateInventory());
        panel.add(updateInventoryButton);
    
        // View Inventory Button
        JButton viewInventoryButton = createButton("View Inventory", e -> viewInventory());
        panel.add(viewInventoryButton);
    
        inventoryManagementFrame.add(panel);
        inventoryManagementFrame.setVisible(true);
    }
    

    private void addInventory() {
        InventoryImp inventoryImp = InventoryImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter item name:");
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter price:");
        String expireDateStr = JOptionPane.showInputDialog(this, "Enter expire date (yyyy-MM-dd):");

        int quantity = Integer.parseInt(quantityStr);
        float price = Float.parseFloat(priceStr);
        try {
            java.util.Date expireDate = DATE_FORMAT.parse(expireDateStr);
            Inventory newItem = new Inventory(0, itemName, quantity, price, expireDate);
            inventoryImp.addItem(newItem);
            JOptionPane.showMessageDialog(this, "Item added successfully!");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private void removeInventory() {
        InventoryImp inventoryImp = InventoryImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter item name to remove:");
        String quantityToRemoveStr = JOptionPane.showInputDialog(this, "Enter quantity to remove:");

        Inventory itemToRemove = null;
        List<Inventory> items = inventoryImp.getAllItems();
        for (Inventory item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            int currentQuantity = itemToRemove.getQuantity();
            int quantityToRemove = Integer.parseInt(quantityToRemoveStr);

            if (quantityToRemove <= currentQuantity) {
                itemToRemove.setQuantity(currentQuantity - quantityToRemove);
                inventoryImp.updateItem(itemToRemove);
                JOptionPane.showMessageDialog(this, "Item removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a quantity less than or equal to " + currentQuantity + ".");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item not found.");
        }
    }

    private void updateInventory() {
        InventoryImp inventoryImp = InventoryImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter item name to update:");

        Inventory itemToUpdate = null;
        List<Inventory> items = inventoryImp.getAllItems();
        for (Inventory item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate != null) {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:");
            String priceStr = JOptionPane.showInputDialog(this, "Enter new price:");
            String expireDateStr = JOptionPane.showInputDialog(this, "Enter new expire date (yyyy-MM-dd):");

            int quantity = Integer.parseInt(quantityStr);
            float price = Float.parseFloat(priceStr);
            try {
                java.util.Date expireDate = DATE_FORMAT.parse(expireDateStr);
                itemToUpdate.setQuantity(quantity);
                itemToUpdate.setPrice(price);
                itemToUpdate.setExpireDate(expireDate);
                inventoryImp.updateItem(itemToUpdate);
                JOptionPane.showMessageDialog(this, "Item updated successfully!");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Item not found.");
        }
    }

    private void viewInventory() {
        InventoryImp inventoryImp = InventoryImp.getInstance();
        List<Inventory> items = inventoryImp.getAllItems();

        String[] columnNames = {"Item ID", "Name", "Quantity", "Price", "Expire Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Inventory item : items) {
            Object[] row = {
                    item.getItemId(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getPrice(),
                    DATE_FORMAT.format(item.getExpireDate())
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Inventory List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void manageMenu() {
        JFrame menuManagementFrame = new JFrame("Menu Management");
        menuManagementFrame.setSize(400, 300);
        menuManagementFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuManagementFrame.setLocationRelativeTo(null);
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // Increased spacing between rows
    
        // Add Menu Item Button
        JButton addMenuItemButton = createButton("Add Menu Item", e -> addMenuItem());
        panel.add(addMenuItemButton);
    
        // Remove Menu Item Button
        JButton removeMenuItemButton = createButton("Remove Menu Item", e -> removeMenuItem());
        panel.add(removeMenuItemButton);
    
        // Update Menu Item Button
        JButton updateMenuItemButton = createButton("Update Menu Item", e -> updateMenuItem());
        panel.add(updateMenuItemButton);
    
        // View Menu Button
        JButton viewMenuButton = createButton("View Menu", e -> viewMenu());
        panel.add(viewMenuButton);
    
        menuManagementFrame.add(panel);
        menuManagementFrame.setVisible(true);
    }

    private void addMenuItem() {
        MenuImp menuImp = MenuImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter menu item name:");
        String itemType = JOptionPane.showInputDialog(this, "Enter menu item type:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter price:");
        float price = Float.parseFloat(priceStr);

        // Use MenuFactory to create a new menu item
        Menu newItem = MenuFactory.createMenuItem(itemName, itemType, price);
        menuImp.addItem(newItem);
        JOptionPane.showMessageDialog(this, "Menu item added successfully!");
    }

    private void removeMenuItem() {
        MenuImp menuImp = MenuImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter menu item name to remove:");

        // Find the item by name
        Menu itemToRemove = null;
        List<Menu> items = menuImp.getAllItems();
        for (Menu item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            menuImp.removeItem(itemToRemove);
            JOptionPane.showMessageDialog(this, "Menu item removed successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Menu item not found.");
        }
    }

    private void updateMenuItem() {
        MenuImp menuImp = MenuImp.getInstance();
        String itemName = JOptionPane.showInputDialog(this, "Enter menu item name to update:");

        // Find the item by name
        Menu itemToUpdate = null;
        List<Menu> items = menuImp.getAllItems();
        for (Menu item : items) {
            if (item.getItemName().equalsIgnoreCase(itemName)) {
                itemToUpdate = item;
                break;
            }
        }

        if (itemToUpdate != null) {
            String newItemName = JOptionPane.showInputDialog(this, "Enter new menu item name:", itemToUpdate.getItemName());
            String newItemType = JOptionPane.showInputDialog(this, "Enter new menu item type:", itemToUpdate.getItemType());
            String priceStr = JOptionPane.showInputDialog(this, "Enter new price:", itemToUpdate.getPrice());
            float price = Float.parseFloat(priceStr);
            itemToUpdate.setItemName(newItemName);
            itemToUpdate.setItemType(newItemType);
            itemToUpdate.setPrice(price);
            menuImp.updateItem(itemToUpdate);
            JOptionPane.showMessageDialog(this, "Menu item updated successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Menu item not found.");
        }
    }

    private void viewMenu() {
        MenuImp menuImp = MenuImp.getInstance();
        List<Menu> items = menuImp.getAllItems();

        String[] columnNames = {"Item ID", "Name", "Type", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Menu item : items) {
            Object[] row = {
                    item.getItemId(),
                    item.getItemName(),
                    item.getItemType(),
                    item.getPrice()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Menu List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void manageTableState() {
    // Create the table state management panel
    JPanel tableStatePanel = new JPanel(new BorderLayout());
    tableStatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

    // Create table model and populate data
    DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1; // Only allow editing of the state column
        }
    };
    tableModel.addColumn("Table No");
    tableModel.addColumn("State");

    TableDAO tableDAO = new TableDAO();
    List<Table> tables = tableDAO.getAllTables();
    for (Table table : tables) {
        tableModel.addRow(new Object[]{table.getTableNo(), table.getState()});
    }

    // Create the table with the table model
    JTable table = new JTable(tableModel);
    table.setFillsViewportHeight(true); // Ensure the table fills the available space
    table.getTableHeader().setReorderingAllowed(false); // Disable column reordering

    // Add a combobox editor for the state column
    JComboBox<String> stateComboBox = new JComboBox<>(new String[]{"Vacant", "Reserved", "Occupied"});
    DefaultCellEditor stateEditor = new DefaultCellEditor(stateComboBox);
    table.getColumnModel().getColumn(1).setCellEditor(stateEditor);

    // Add a table model listener to update table state in the database
    table.getModel().addTableModelListener(e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 1) {
                int tableNo = (int) table.getValueAt(row, 0);
                String newState = (String) table.getValueAt(row, column);
                tableDAO.updateTableState(tableNo, newState);
            }
        }
    });

    // Create a scroll pane for the table
    JScrollPane scrollPane = new JScrollPane(table);

    // Create and configure the OK button
    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> {
        // Close the table state management panel
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(tableStatePanel);
        frame.dispose();
    });

    // Add components to the panel
    tableStatePanel.add(scrollPane, BorderLayout.CENTER);
    tableStatePanel.add(okButton, BorderLayout.SOUTH);

    // Create and configure the frame
    JFrame tableStateFrame = new JFrame("Table State Management");
    tableStateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    tableStateFrame.getContentPane().add(tableStatePanel);
    tableStateFrame.setSize(600, 400);
    tableStateFrame.setLocationRelativeTo(null);
    tableStateFrame.setVisible(true);
}

private void viewOrder() {
    // Initialize OrderDAO
    OrderDAO orderDAO = new OrderDAO();

    // Fetch all orders from the order DAO
    List<Order> orders = orderDAO.getAllOrders();

    // Create a table model
    DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Allow editing of the "Status" column
            return column == 5;
        }
    };
    tableModel.addColumn("Order ID");
    tableModel.addColumn("Customer Name");
    tableModel.addColumn("Table No.");
    tableModel.addColumn("Menu Items");
    tableModel.addColumn("Quantity");
    tableModel.addColumn("Status");

    // Populate the table model with orders
    for (Order order : orders) {
        Object[] rowData = {
                order.getId(),
                order.getCustomerName(),
                order.getTableNo(),
                order.getMenuItems(),
                order.getQuantity(),
                order.getStatus()
        };
        tableModel.addRow(rowData);
    }

    // Create a JTable with the table model
    JTable table = new JTable(tableModel);

    // Set custom cell renderer for the "Status" column
    table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

    // Create a JComboBox for order status
    JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Pending", "Processing", "Completed"});
    DefaultCellEditor statusEditor = new DefaultCellEditor(statusComboBox);
    table.getColumnModel().getColumn(5).setCellEditor(statusEditor);

    // Add a TableModelListener to update order status
    table.getModel().addTableModelListener(e -> {
        int row = e.getFirstRow();
        int column = e.getColumn();
        if (column == 5) { // Check if the change is in the "Status" column
            int orderId = (int) table.getValueAt(row, 0); // Get the order ID
            String newStatus = (String) table.getValueAt(row, column); // Get the new status
            // Update the order status in the database
            boolean updated = orderDAO.updateOrderStatus(orderId, newStatus);
            if (!updated) {
                // Handle update failure
                JOptionPane.showMessageDialog(null, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Create a JScrollPane for the table
    JScrollPane scrollPane = new JScrollPane(table);

    // Create the OK button
    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> {
        // Close the frame when the OK button is clicked
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(okButton);
        frame.dispose();
    });

    // Create a JPanel for the button
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(okButton);

    // Create a JPanel to hold both the scroll pane and the button panel
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    mainPanel.add(scrollPane, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Create a JFrame for displaying the table
    JFrame frame = new JFrame("Orders");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.pack();
    frame.setLocationRelativeTo(null); // Center the frame on the screen
    frame.setVisible(true);
}

    public class StatusCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
            // Set the background color based on the status value
            String status = (String) value;
            if ("Pending".equals(status)) {
                cellComponent.setBackground(Color.YELLOW);
            } else if ("Processing".equals(status)) {
                cellComponent.setBackground(Color.ORANGE);
            } else if ("Completed".equals(status)) {
                cellComponent.setBackground(Color.GREEN);
            } else {
                // For any other status, keep the default background color
                cellComponent.setBackground(table.getBackground());
            }
    
            return cellComponent;
        }
    }
    private void showEmployeeManagementDialog() {
        JDialog dialog = new JDialog(this, "Manage Employees", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the buttons

        JButton viewEmployeeButton = createButton("View Employees", e -> viewEmployees());
        JButton addEmployeeButton = createButton("Add Employee", e -> addEmployee());
        JButton removeEmployeeButton = createButton("Remove Employee", e -> removeEmployee());

        panel.add(viewEmployeeButton);
        panel.add(addEmployeeButton);
        panel.add(removeEmployeeButton);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addEmployee() {
        EmployeeManager manager = new AddEmployeeManager();
        manager.manageEmployee();
    }

    private void removeEmployee() {
        EmployeeManager manager = new RemoveEmployeeManager();
        manager.manageEmployee();
    }

    private void viewEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();

        // Column names for the table
        String[] columnNames = {"ID", "Name", "Role", "Salary"};

        // Data for the table
        Object[][] data = new Object[employees.size()][4];
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            data[i][0] = employee.getId();
            data[i][1] = employee.getName();
            data[i][2] = employee.getRole();
            data[i][3] = employee.getSalary();
        }

        // Create table with data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(550, 350)); // Adjusted size for better fit

        JOptionPane.showMessageDialog(this, scrollPane, "Employee List", JOptionPane.INFORMATION_MESSAGE);
    }
    private void logout() {
        dispose(); // Close the current frame
        new MainUI().setVisible(true); // Open the MainUI frame
    }


  
       public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminPageUI().setVisible(true);
            }
        });
    }
}