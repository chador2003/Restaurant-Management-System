
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Restaurant.*;
import Restaurant.Menu;

public class CustomerPageUI extends JFrame {
    private OrderDAO orderDAO = new OrderDAO();
    private String username;

    private AdminPageUI adminPageUI;
    private DiscountStrategyDAO DiscountStrategyDAO;

    public CustomerPageUI(String username) {
        this.username = username;
        this.DiscountStrategyDAO = new DiscountStrategyDAO();

        setTitle("Customer Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 5, 5)); // Adjusted to fit the new button

        JButton viewMenuButton = new JButton("View Menu");
        styleButton(viewMenuButton);
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMenu();
            }
        });
        buttonPanel.add(viewMenuButton);

        JButton placeOrderButton = new JButton("Place Order");
        styleButton(placeOrderButton);
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
        buttonPanel.add(placeOrderButton);

        JButton viewOrderListButton = new JButton("View Ordered List");
        styleButton(viewOrderListButton);
        viewOrderListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOrderedList();
            }
        });
        buttonPanel.add(viewOrderListButton);

        JButton cancelOrderButton = new JButton("Cancel Order");
        styleButton(cancelOrderButton);
        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelOrder();
            }
        });
        buttonPanel.add(cancelOrderButton);

        JButton payButton = new JButton("Pay for Order");
        styleButton(payButton);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payForOrder();
            }
        });
        buttonPanel.add(payButton);

        JButton viewProfileButton = new JButton("View Profile");
        styleButton(viewProfileButton);
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewProfile();
            }
        });
        buttonPanel.add(viewProfileButton);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
    }
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
    }

    private void viewMenu() {
        List<Menu> menuItems = MenuImp.getInstance().getAllItems();
        String[] columnNames = {"Item ID", "Item Name", "Item Type", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    
        for (Menu item : menuItems) {
            Object[] row = {
                item.getItemId(),
                item.getItemName(),
                item.getItemType(),
                String.format("$%.2f", item.getPrice()) // Format price to display currency
            };
            tableModel.addRow(row);
        }
    
        JTable table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Increase header font size and change font
        table.setFont(new Font("Arial", Font.PLAIN, 12)); // Set table font
        table.setRowHeight(30); // Increase row height for better readability and spacing
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Allow resizing of columns
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single row selection
    
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400)); // Adjust scroll pane size
    
        // Customize JOptionPane dialog box
        JOptionPane optionPane = new JOptionPane();
        optionPane.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for JOptionPane
        optionPane.setMessage(scrollPane);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, "Menu"); // Set title of dialog box
        dialog.setVisible(true);
    }
    
    private void placeOrder() {
        List<Table> vacantTables = getVacantTables();
    
        if (!vacantTables.isEmpty()) {
            // Create JComboBox for table selection
            JComboBox<Integer> tableComboBox = new JComboBox<>();
            for (Table table : vacantTables) {
                tableComboBox.addItem(table.getTableNo());
            }
    
            // Create JComboBox for menu item selection
            JComboBox<String> itemComboBox = new JComboBox<>();
            for (Menu menuItem : MenuImp.getInstance().getAllItems()) {
                itemComboBox.addItem(menuItem.getItemName());
            }
    
            // Create JTextField for quantity input
            JTextField quantityField = new JTextField();
            
            // Create JPanel for components layout
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Added padding
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
    
            // Add components to panel with GridBagLayout for better control
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Select Table Number:"), gbc);
            gbc.gridx = 1;
            panel.add(tableComboBox, gbc);
    
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Select Menu Item:"), gbc);
            gbc.gridx = 1;
            panel.add(itemComboBox, gbc);
    
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Enter Quantity:"), gbc);
            gbc.gridx = 1;
            panel.add(quantityField, gbc);
    
            // Display dialog with panel
            int result = JOptionPane.showConfirmDialog(this, panel, "Place Order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                int selectedTableNo = (int) tableComboBox.getSelectedItem();
                String selectedItem = (String) itemComboBox.getSelectedItem();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                Menu selectedMenuItem = null;
                for (Menu menuItem : MenuImp.getInstance().getAllItems()) {
                    if (menuItem.getItemName().equals(selectedItem)) {
                        selectedMenuItem = menuItem;
                        break;
                    }
                }
    
                if (selectedMenuItem != null) {
                    String menuItemsStr = selectedMenuItem.getItemName();
                    Order order = new Order(0, username, selectedTableNo, "Pending", menuItemsStr, quantity);
    
                    if (orderDAO.placeOrder(order)) {
                        JOptionPane.showMessageDialog(this, "Order placed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to place order.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No vacant tables available.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
   private void viewOrderedList() {
    List<Order> orders = orderDAO.getOrdersByUsername(username);
    String[] columnNames = {"Order ID", "Customer Name", "Table No", "Status", "Menu Items", "Quantity"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    for (Order order : orders) {
        Object[] row = {
            order.getId(),
            order.getCustomerName(),
            order.getTableNo(),
            order.getStatus(),
            order.getMenuItems(),
            order.getQuantity()
        };
        tableModel.addRow(row);
    }

    JTable table = new JTable(tableModel);
    table.setFillsViewportHeight(true); // Ensure table fills the viewport
    table.setAutoCreateRowSorter(true); // Enable row sorting

    // Set column widths for better presentation
    table.getColumnModel().getColumn(0).setPreferredWidth(50);  // Order ID
    table.getColumnModel().getColumn(1).setPreferredWidth(100); // Customer Name
    table.getColumnModel().getColumn(2).setPreferredWidth(50);  // Table No
    table.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
    table.getColumnModel().getColumn(4).setPreferredWidth(150); // Menu Items
    table.getColumnModel().getColumn(5).setPreferredWidth(50);  // Quantity

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(600, 400)); // Adjusted dimensions for better fit

    // Set custom table header for better appearance
    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("SansSerif", Font.BOLD, 14));
    header.setBackground(Color.LIGHT_GRAY);
    header.setForeground(Color.BLACK);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Added padding

    JOptionPane.showMessageDialog(this, panel, "Ordered List", JOptionPane.INFORMATION_MESSAGE);
}


    private void cancelOrder() {
        String orderIdStr = JOptionPane.showInputDialog(this, "Enter Order ID to cancel:");
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderDAO.getOrderById(orderId);
                if (order != null && order.getCustomerName().equals(username) && "Pending".equals(order.getStatus())) {
                    if (orderDAO.updateOrderStatus(orderId, "Cancelled")) {
                        JOptionPane.showMessageDialog(this, "Order cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel order.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid order ID or order not pending.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void payForOrder() {
        String orderIdStr = JOptionPane.showInputDialog(this, "Enter Order ID to pay for:");
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            try {
                int orderId = Integer.parseInt(orderIdStr);
                Order order = orderDAO.getOrderById(orderId);
                if (order != null && order.getCustomerName().equals(username) && "Completed".equals(order.getStatus())) {
                    Menu menuItem = MenuImp.getInstance().getItemByName(order.getMenuItems());
                    if (menuItem != null) {
                        float totalCost = menuItem.getPrice() * order.getQuantity();
    
                        // Retrieve the current discount percentage from the database
                        float discountPercentage = DiscountStrategyDAO.getDiscountState();
    
                        // Apply the discount
                        DiscountStrategy discountStrategy;
                        if (discountPercentage == 0.10f) {
                            discountStrategy = new TenPercentDiscount();
                        } else if (discountPercentage == 0.20f) {
                            discountStrategy = new TwentyPercentDiscount();
                        } else {
                            discountStrategy = new NoDiscount();
                        }
    
                        float discountedCost = discountStrategy.applyDiscount(totalCost);
    
                        int result = JOptionPane.showConfirmDialog(this, "Total cost after discount (" + (discountPercentage * 100) + "%) is $" + discountedCost + ". Proceed with payment?", "Confirm Payment", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            if (orderDAO.updateOrderStatus(orderId, "Paid")) {
                                JOptionPane.showMessageDialog(this, "Payment successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to update order status.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Menu item not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Order not completed or not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Method to fetch tables with vacant state
    private List<Table> getVacantTables() {
        TableDAO tableDAO = new TableDAO();
        return tableDAO.getVacantTables();
    }

    private void viewProfile() {
        UserProfile userProfile = new UserProfileProxy(username);
    
        JDialog profileDialog = new JDialog(this, "User Profile", true);
        profileDialog.setSize(400, 300);
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setLayout(new BorderLayout(10, 10));
        profileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
    
        JLabel fullNameLabel = new JLabel("Full Name:");
        JLabel fullNameValue = new JLabel(userProfile.getFullName());
        gbc.gridx = 0;
        gbc.gridy = 0;
        profilePanel.add(fullNameLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(fullNameValue, gbc);
    
        JLabel usernameLabel = new JLabel("Username:");
        JLabel usernameValue = new JLabel(userProfile.getUsername());
        gbc.gridx = 0;
        gbc.gridy = 1;
        profilePanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(usernameValue, gbc);
    
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JLabel phoneNumberValue = new JLabel(userProfile.getPhoneNumber());
        gbc.gridx = 0;
        gbc.gridy = 2;
        profilePanel.add(phoneNumberLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(phoneNumberValue, gbc);
    
        JLabel emailLabel = new JLabel("Email:");
        JLabel emailValue = new JLabel(userProfile.getEmail());
        gbc.gridx = 0;
        gbc.gridy = 3;
        profilePanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        profilePanel.add(emailValue, gbc);
    
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileDialog.dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        profilePanel.add(closeButton, gbc);
    
        profileDialog.add(profilePanel, BorderLayout.CENTER);
        profileDialog.setVisible(true);
    }

    private void logout() {
        dispose(); // Close the current frame
        new MainUI().setVisible(true); // Open the MainUI frame
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CustomerPageUI("testuser").setVisible(true);
            }
        });
    }
}
