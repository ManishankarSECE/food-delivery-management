package fdms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class FoodManagement {

    public static void main(String[] args) {
        try (Connection conn = Sqlconnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n=== Food Management System ===");
                System.out.println("1. Add Food Item");
                System.out.println("2. Update Food Item Price");
                System.out.println("3. Update Delivery Status");
                System.out.println("4. View Menu");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        addFood(conn, scanner);
                        break;
                    case 2:
                        updatePrice(conn, scanner);
                        break;
                    case 3:
                        updateDeliveryStatus(conn, scanner);
                        break;
                    case 4:
                        viewMenu(conn);
                        break;
                    case 5:
                        System.out.println("Exiting Food Management System.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addFood(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter food item name: ");
        String name = scanner.nextLine();
        System.out.println("Enter food category: ");
        String category = scanner.nextLine();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        System.out.println("Is the item available? (true/false): ");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine();  

        String query = "INSERT INTO fooditems (name, category, price, description, availability) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setDouble(3, price);
            pstmt.setString(4, description);
            pstmt.setBoolean(5, availability);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Food item added successfully.");
            } else {
                System.out.println("Failed to add food item.");
            }
        }
    }

    private static void updatePrice(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter food item ID to update: ");
        int foodItemId = scanner.nextInt();   
        System.out.println("Enter new price: ");
        double price = scanner.nextDouble();

        
        String query = "UPDATE fooditems SET price = ? WHERE food_item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, price);
            pstmt.setInt(2, foodItemId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Price updated successfully.");
            } else {
                System.out.println("Food item with ID " + foodItemId + " not found.");
            }
        }
    }


    private static void updateDeliveryStatus(Connection conn, Scanner scanner) throws SQLException {
        System.out.println("Enter order ID to update status: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();  
        System.out.println("Enter new delivery status: ");
        String status = scanner.nextLine();

        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Delivery status updated successfully.");
            } else {
                System.out.println("Order with ID " + orderId + " not found.");
            }
        }
    }

    private static void viewMenu(Connection conn) throws SQLException {
        String query = "SELECT food_item_id, name, category, price, description, availability FROM fooditems";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("--- Menu ---");
            while (rs.next()) {
                int foodItemId = rs.getInt("food_item_id");   
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                boolean availability = rs.getBoolean("availability");
                
                System.out.printf("ID: %d, Name: %s, Category: %s, Price: %.2f, Description: %s, Availability: %s%n",
                        foodItemId, name, category, price, description, availability ? "Available" : "Out of stock");
            }
        }
    }

}
