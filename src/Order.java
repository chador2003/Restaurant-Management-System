

public class Order {
    private int id;
    private String customerName;
    private int tableNo;
    private String status;
    private String menuItems;
    private int quantity; // Add quantity field

    public Order(int id, String customerName, int tableNo, String status, String menuItems, int quantity) {
        this.id = id;
        this.customerName = customerName;
        this.tableNo = tableNo;
        this.status = status;
        this.menuItems = menuItems;
        this.quantity = quantity; // Initialize quantity
    }

    public Order() {
        
    }

    // Getter method for id
    
    public int getId() {
        return id;
    }

    // Setter method for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter method for customerName
    public String getCustomerName() {
        return customerName;
    }

    // Setter method for customerName
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter method for tableNo
    public int getTableNo() {
        return tableNo;
    }

    // Setter method for tableNo
    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    // Getter method for status
    public String getStatus() {
        return status;
    }

    // Setter method for status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter method for menuItems
    public String getMenuItems() {
        return menuItems;
    }

    // Setter method for menuItems
    public void setMenuItems(String menuItems) {
        this.menuItems = menuItems;
    }

    // Getter method for quantity
    public int getQuantity() {
        return quantity;
    }
    // Setter method for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
