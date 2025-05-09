
package BackendCode;

import java.io.Serializable;
import java.util.ArrayList;

public class Car implements Serializable {

    private int ID;
    private String Maker, Name, Colour, Type;
    int SeatingCapacity;
    String Model, Condition, RegNo;
    private int RentPerHour;
    private CarOwner carOwner;

    private static final FileManager<Car> fileManager = new FileManager<>("Car.ser");

    public Car() {}

    public Car(int ID, String Maker, String Name, String Colour, String Type, int SeatingCapacity,
               String Model, String Condition, String RegNo, int RentPerHour, CarOwner carOwner) {
        this.ID = ID;
        this.Maker = Maker;
        this.Name = Name;
        this.Colour = Colour;
        this.Type = Type;
        this.SeatingCapacity = SeatingCapacity;
        this.Model = Model;
        this.Condition = Condition;
        this.RegNo = RegNo;
        this.RentPerHour = RentPerHour;
        this.carOwner = carOwner;
    }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getMaker() { return Maker; }
    public void setMaker(String Maker) { this.Maker = Maker; }

    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }

    public String getColour() { return Colour; }
    public void setColour(String Colour) { this.Colour = Colour; }

    public String getType() { return Type; }
    public void setType(String Type) { this.Type = Type; }

    public int getSeatingCapacity() { return SeatingCapacity; }
    public void setSeatingCapacity(int SeatingCapacity) { this.SeatingCapacity = SeatingCapacity; }

    public String getModel() { return Model; }
    public void setModel(String Model) { this.Model = Model; }

    public String getCondition() { return Condition; }
    public void setCondition(String Condition) { this.Condition = Condition; }

    public String getRegNo() { return RegNo; }
    public void setRegNo(String RegNo) { this.RegNo = RegNo; }

    public int getRentPerHour() { return RentPerHour; }
    public void setRentPerHour(int RentPerHour) { this.RentPerHour = RentPerHour; }

    public CarOwner getCarOwner() { return carOwner; }
    public void setCarOwner(CarOwner carOwner) { this.carOwner = carOwner; }

    @Override
    public String toString() {
        return "Car{" + "ID=" + ID + ", Maker=" + Maker + ", Name=" + Name + ", Colour=" + Colour +
               ", Type=" + Type + ", SeatingCapacity=" + SeatingCapacity + ", Model=" + Model +
               ", Condition=" + Condition + ", RegNo=" + RegNo + ", RentPerHour=" + RentPerHour +
               ", carOwner=" + carOwner + '}' + "\n";
    }

    public void Add() {
        ArrayList<Car> cars = View();
        this.ID = cars.isEmpty() ? 1 : cars.get(cars.size() - 1).getID() + 1;
        fileManager.saveOrUpdate(this, c -> c.getID() == this.ID);
    }

    public void Update() {
        fileManager.saveOrUpdate(this, c -> c.getID() == this.ID);
    }

    public void Remove() {
        fileManager.remove(c -> c.getID() == this.ID);
    }

    public static ArrayList<Car> SearchByName(String name) {
        return fileManager.search(c -> c.getName().equalsIgnoreCase(name));
    }

    public static Car SearchByID(int id) {
        ArrayList<Car> found = fileManager.search(c -> c.getID() == id);
        return found.isEmpty() ? null : found.get(0);
    }

    public static Car SearchByRegNo(String regNo) {
        ArrayList<Car> found = fileManager.search(c -> c.getRegNo().equalsIgnoreCase(regNo));
        return found.isEmpty() ? null : found.get(0);
    }

    public static ArrayList<Car> View() {
        return fileManager.loadAll();
    }

    public boolean isRented() {
        ArrayList<Car> BookedCars = Booking.getBookedCars();
        for (Car c : BookedCars) {
            if (c.getID() == this.ID) return true;
        }
        return false;
    }
}
