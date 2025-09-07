import java.sql.*;

public class Bank {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_project?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // Your root password is empty

    // Constructor to load JDBC driver
    public Bank() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            System.out.println("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found");
            e.printStackTrace();
        }
    }

    public void addCustomer(int id, String name, String email, double balance) {
        String sql = "INSERT INTO customers (id, name, email, balance) VALUES (?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setDouble(4, balance);
            ps.executeUpdate();
            System.out.println("Customer added successfully.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deposit(int id, double amount) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String update = "UPDATE customers SET balance = balance + ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(update)) {
                ps.setDouble(1, amount);
                ps.setInt(2, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    recordTransaction(con, id, "Deposit", amount);
                    System.out.println("Deposit successful.");
                } else System.out.println("Customer not found.");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void withdraw(int id, double amount) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String check = "SELECT balance FROM customers WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(check)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    if (balance >= amount) {
                        String update = "UPDATE customers SET balance = balance - ? WHERE id = ?";
                        try (PreparedStatement ps2 = con.prepareStatement(update)) {
                            ps2.setDouble(1, amount);
                            ps2.setInt(2, id);
                            ps2.executeUpdate();
                            recordTransaction(con, id, "Withdraw", amount);
                            System.out.println("Withdraw successful.");
                        }
                    } else System.out.println("Insufficient balance.");
                } else System.out.println("Customer not found.");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void displayCustomer(int id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Email: " + rs.getString("email") +
                                   ", Balance: " + rs.getDouble("balance"));
            } else System.out.println("Customer not found.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void displayAllCustomers() {
        String sql = "SELECT * FROM customers";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- All Customers ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Email: " + rs.getString("email") +
                                   ", Balance: " + rs.getDouble("balance"));
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void recordTransaction(Connection con, int id, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (customer_id, type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        }
    }
}
