package fpoly.vunvph33438.vehiclevista.Model;

public class Car {
    private int idCar;
    private int idBrand;
    private String model;
    private int price;
    private String description;
    private int available;
    private byte[] image;


    public Car(int idCar, int idBrand, String model, int price,String description, int available, byte[] image) {
        this.idCar = idCar;
        this.idBrand = idBrand;
        this.model = model;
        this.price = price;
        this.description = description;
        this.available = available;
        this.image = image;
    }

    public Car() {

    }

    public int getIdCar() {
        return idCar;
    }

    public void setIdCar(int idCar) {
        this.idCar = idCar;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int isAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
