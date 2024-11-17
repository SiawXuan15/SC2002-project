package UserManagement;

public class BankTransfer implements PaymentProcessor {
    @Override
    public void makePayment() {
        System.out.println("Payment type: Bank Transfer");
    }
}
