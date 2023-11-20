package fpoly.vunvph33438.vehiclevista.Model;

import java.util.Date;

public class HoaDon {
        private int id_Receipt;
        private int id_Car;
        private int id_User;
        private Date rentalStartDate;
        private Date rentalEndDate;
        private int price;
        private int paymentMethod;
        private boolean status;

    public HoaDon() {
    }

    public HoaDon(int id_Receipt, int id_Car, int id_User, Date rentalStartDate, Date rentalEndDate, int price, int paymentMethod, boolean status) {
        this.id_Receipt = id_Receipt;
        this.id_Car = id_Car;
        this.id_User = id_User;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.status = status;
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

    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public Date getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(Date rentalEndDate) {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
