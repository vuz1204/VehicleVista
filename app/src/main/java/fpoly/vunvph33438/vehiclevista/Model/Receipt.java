package fpoly.vunvph33438.vehiclevista.Model;

import java.text.NumberFormat;
import java.util.Locale;

public class Receipt {
    private int id_Receipt;
    private int id_Car;
    private int id_User;
    private String rentalStartDate;
    private String rentalEndDate;
    private int price;
    private int paymentMethod;
    private int status;
    private String date;
    private byte[] imagePayment;

    public Receipt() {
    }

    public Receipt(int id_Receipt, int id_Car, int id_User, String rentalStartDate, String rentalEndDate, int price,int paymentMethod,int status, String date, byte[] imagePayment) {
        this.id_Receipt = id_Receipt;
        this.id_Car = id_Car;
        this.id_User = id_User;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.date = date;
        this.imagePayment = imagePayment;
    }

    public String getPriceFormatted() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        return numberFormat.format(price);
    }

    public void setPriceFormatted(String formattedPrice) {
        try {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            Number parsed = numberFormat.parse(formattedPrice);
            this.price = parsed.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public byte[] getImagePayment() {
        return imagePayment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setImagePayment(byte[] imagePayment) {
        this.imagePayment = imagePayment;

    }
}
