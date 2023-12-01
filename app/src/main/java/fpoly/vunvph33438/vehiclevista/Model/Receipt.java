package fpoly.vunvph33438.vehiclevista.Model;

public class Receipt {
    private int id_Receipt;
    private int id_Car;
    private int id_User;
    private String rentalStartDate;
    private String rentalEndDate;
    private int price;
    private int paymentMethod;
    private String date;

    public Receipt() {
    }

    public Receipt(int id_Receipt, int id_Car, int id_User, String rentalStartDate, String rentalEndDate, int paymentMethod, int price,String date) {
        this.id_Receipt = id_Receipt;
        this.id_Car = id_Car;
        this.id_User = id_User;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.date = date;
    }

    public int getId_Receipt() {
        return id_Receipt;
    }

    public void setId_Receipt(int id_Receipt) {
        this.id_Receipt = id_Receipt;
    }

    public int getId_Car() {
        return id_Car;
    }

    public void setId_Car(int id_Car) {
        this.id_Car = id_Car;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public String getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(String rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public String getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(String rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
