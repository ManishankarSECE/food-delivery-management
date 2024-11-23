package fdms;

import java.sql.*;
import java.util.Scanner;

public class Operations {

	public void register(Connection conn, Customer a,String query ) {

	    try (PreparedStatement cusStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			cusStmt.setString(1, a.getFirstName());
			cusStmt.setString(2, a.getLastName());
			cusStmt.setString(3,a.getPhoneNumber());
			cusStmt.setString(4, a.getAddress());
			cusStmt.setString(5, a.getEmail());
			cusStmt.executeUpdate();
			ResultSet generatedKeys = cusStmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            int cusId = generatedKeys.getInt(1);
	        

	           System.out.println("registered successfully with  ID: " + cusId);
	           
	        }
	    }
	    catch (SQLException e) {
	    	e.printStackTrace();
	    	}
	   }
	
static void placeOrder(Connection conn, Scanner scanner) {
        
    	try {
            System.out.print("Enter customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();

            String query = "SELECT * FROM FoodItems WHERE availability = TRUE";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("\nAvailable Food Items:");
                while (rs.next()) {
                    int id = rs.getInt("food_item_id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    System.out.printf("%d - %s ($%.2f)\n", id, name, price);
                }
            }

            System.out.print("Enter food item ID to order: ");
            int foodItemId = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();

             
            String priceQuery = "SELECT price FROM FoodItems WHERE food_item_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(priceQuery)) {
                pstmt.setInt(1, foodItemId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    double price = rs.getDouble("price");
                    double totalPrice = price * quantity;

                    String insertOrder = "INSERT INTO Orders (customer_id, order_date, total_price, status, delivery_address) " +
                                         "VALUES (?, NOW(), ?, 'Pending', (SELECT address FROM Customers WHERE customer_id = ?))";
                    try (PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                        orderStmt.setInt(1, customerId);
                        orderStmt.setDouble(2, totalPrice);
                        orderStmt.setInt(3, customerId);
                        orderStmt.executeUpdate();

                        ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int orderId = generatedKeys.getInt(1);
                            

                  
                            String insertOrderItem = "INSERT INTO OrderItems (order_id, food_item_id, quantity, item_price) " +
                                                     "VALUES (?, ?, ?, ?)";
                            try (PreparedStatement itemStmt = conn.prepareStatement(insertOrderItem)) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setInt(2, foodItemId);
                                itemStmt.setInt(3, quantity);
                                itemStmt.setDouble(4, price);
                                itemStmt.executeUpdate();
                            }
                            String insertDelivery = "INSERT INTO Deliveries (order_id, delivery_person, status, delivery_date,delivery_time, tracking_number) " +
                                    "VALUES (?, 'hotel boy' ,'IN progress', now()+ interval 30 minute,now()+ interval 1 HOUR, concat('TRK', lpad(?, 5, '0')))";
				            try (PreparedStatement deliveryStmt = conn.prepareStatement(insertDelivery)) {
				                deliveryStmt.setInt(1, orderId);
				                deliveryStmt.setInt(2, orderId); 
				                deliveryStmt.executeUpdate();
				            }



                            System.out.println("Order placed successfully with Order ID: " + orderId);
                        }
                    }
                } else {
                    System.out.println("Food item not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

static void viewDeliveryStatus(Connection conn, Scanner scanner) {
    try {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        String query = "SELECT status, delivery_date FROM Deliveries WHERE order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                Timestamp deliveryDate = rs.getTimestamp("delivery_date");
                System.out.printf("Order Status: %s\nDelivery Date: %s\n", status, deliveryDate != null ? deliveryDate.toString() : "Pending");
            } else {
                System.out.println("Order not found.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 static void provideFeedback(Connection conn, Scanner scanner) {
    try {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();  
        System.out.print("Enter comments: ");
        String comments = scanner.nextLine();

        String insertFeedback = "INSERT INTO Feedback (order_id, rating, comments, feedback_date) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(insertFeedback)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, rating);
            pstmt.setString(3, comments);
            pstmt.executeUpdate();
            System.out.println("Feedback submitted successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
