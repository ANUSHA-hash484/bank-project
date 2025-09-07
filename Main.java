import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();
        int choice = 0;

        // Optional: Add sample customers
        bank.addCustomer(1, "Anusha", "anusha@example.com", 5000);
        bank.addCustomer(2, "Medha", "medha@example.com", 3000);

        do {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Display Customer Details");
            System.out.println("4. Display All Customers");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    int did = sc.nextInt();
                    System.out.print("Enter amount to deposit: ");
                    double damount = sc.nextDouble();
                    bank.deposit(did, damount);
                    break;
                case 2:
                    System.out.print("Enter Customer ID: ");
                    int wid = sc.nextInt();
                    System.out.print("Enter amount to withdraw: ");
                    double wamount = sc.nextDouble();
                    bank.withdraw(wid, wamount);
                    break;
                case 3:
                    System.out.print("Enter Customer ID: ");
                    int cid = sc.nextInt();
                    bank.displayCustomer(cid);
                    break;
                case 4:
                    bank.displayAllCustomers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        } while (choice != 5);

        sc.close();
    }
}
