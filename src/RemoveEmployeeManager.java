import Restaurant.*;
import javax.swing.*;

public class RemoveEmployeeManager extends EmployeeManager {
    private int id;
    private EmployeeDAO employeeDAO = new EmployeeDAO(); // Assuming this is instantiated

    @Override
    protected void collectEmployeeDetails() {
        String idStr = JOptionPane.showInputDialog("Enter employee ID to remove:");
        id = Integer.parseInt(idStr);
    }

    @Override
    protected void processEmployee() {
        employeeDAO.removeEmployee(id);
    }

    @Override
    protected void confirmAction() {
        JOptionPane.showMessageDialog(null, "Employee removed successfully.");
    }
}
