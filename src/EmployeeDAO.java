import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = ""; // Replace with your database password

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employee (name, role, salary) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getRole());
            statement.setBigDecimal(3, BigDecimal.valueOf(employee.getSalary()));

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEmployee(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                double salary = resultSet.getBigDecimal("salary").doubleValue();
                employees.add(new Employee(id, name, role, salary));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        Employee employee = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String role = resultSet.getString("role");
                    double salary = resultSet.getBigDecimal("salary").doubleValue();
                    employee = new Employee(id, name, role, salary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }
}
