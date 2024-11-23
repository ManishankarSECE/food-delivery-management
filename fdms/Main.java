package fdms;

import java.sql.*;
import java.util.*;

public class Main {
    private static List<FoodItem> foodItems = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        try (Connection conn = Sqlconnection.getConnection()) {
        	Operations operations = new Operations();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n*** Food Delivery Management System ***");
                System.out.println("1. New customer");
                System.out.println("2. Place an Order");
                System.out.println("3. View Delivery Status");
                System.out.println("4. Provide Feedback");  
                System.out.println("5. delivery and food management(not for customers)");  
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                	case 1:
                		register(conn,scanner,operations);
                		break;
                    case 2:
                        Operations.placeOrder(conn, scanner);
                        break;
                    case 3:
                    	Operations.viewDeliveryStatus(conn, scanner);
                        break;
                    case 4:
                    	Operations.provideFeedback(conn, scanner);
                        break;
                    case 5:
                    	FoodManagement.main(null);
                        break;
                    case 6:
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   
    private static void register(Connection conn, Scanner scanner, Operations op) {
    	
    		scanner.nextLine();
    		System.out.println("Enter first name: ");
    		String fname = scanner.nextLine();
    		System.out.println("Enter second name: ");
    		String sname = scanner.nextLine();
    		System.out.println("Enter email: ");
    		String email = scanner.nextLine();
    		System.out.println("Enter phone number: ");
    		String phno = scanner.nextLine();
    		System.out.println("Enter address: ");
    		String add = scanner.nextLine();
    		
    		String query = "INSERT INTO customers ( first_name, last_name,email,phone_number,address,registration_date) " +
                    "VALUES (?,?,?,?,?,NOW())";
    		Customer a =new Customer(fname,sname,email,phno,add);
    		
			op.register(conn, a, query);
    		
    		
    }
}
