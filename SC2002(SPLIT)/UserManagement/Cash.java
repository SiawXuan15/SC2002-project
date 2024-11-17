package UserManagement;

public class Cash implements PaymentProcessor {
    @Override
    public void makePayment() {
        System.out.println("Payment type: Cash");
    }
}