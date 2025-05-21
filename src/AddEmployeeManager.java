import javax.swing.*;
import Restaurant.*;

public class AddEmployeeManager extends EmployeeManager {
    private String name;
    private String role;
    private double salary;
    private Employee employee;
    private EmployeeDAO employeeDAO = new EmployeeDAO(); // Assuming this is instantiated

    @Override
    protected void collectEmployeeDetails() {
        name = JOptionPane.showInputDialog("Enter employee name:");
        role = JOptionPane.showInputDialog("Enter employee role:");
        String salaryStr = JOptionPane.showInputDialog("Enter employee salary:");
        salary = Double.parseDouble(salaryStr);
        employee = new Employee(0, name, role, salary); // ID is auto-generated
    }

    @Override
    protected void processEmployee() {
        employeeDAO.addEmployee(employee);
    }

    @Override
    protected void confirmAction() {
        JOptionPane.showMessageDialog(null, "Employee added successfully.");
    }
}
