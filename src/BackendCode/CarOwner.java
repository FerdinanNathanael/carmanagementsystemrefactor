
package BackendCode;

import java.io.Serializable;
import java.util.ArrayList;

public class CarOwner extends Person implements Serializable {

    private int Balance;
    private static final FileManager<CarOwner> fileManager = new FileManager<>("CarOwner.ser");

    public CarOwner() {
        super();
    }

    public CarOwner(int Balance, int ID, String CNIC, String Name, String Contact_No) {
        super(ID, CNIC, Name, Contact_No);
        this.Balance = Balance;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int Balance) {
        this.Balance = Balance;
    }

    @Override
    public String toString() {
        return super.toString() + " CarOwner{" + "Balance=" + Balance + '}' + "\n";
    }

    @Override
    public void Add() {
        ArrayList<CarOwner> carOwners = View();
        this.ID = carOwners.isEmpty() ? 1 : carOwners.get(carOwners.size() - 1).getID() + 1;
        fileManager.saveOrUpdate(this, c -> c.getID() == this.ID);
    }

    @Override
    public void Update() {
        fileManager.saveOrUpdate(this, c -> c.getID() == this.ID);
    }

    @Override
    public void Remove() {
        fileManager.remove(c -> c.getID() == this.ID);
    }

    public static ArrayList<CarOwner> SearchByName(String name) {
        return fileManager.search(c -> c.getName().equalsIgnoreCase(name));
    }

    public static CarOwner SearchByCNIC(String CNIC) {
        ArrayList<CarOwner> list = fileManager.search(c -> c.getCNIC().equalsIgnoreCase(CNIC));
        return list.isEmpty() ? null : list.get(0);
    }

    public static CarOwner SearchByID(int id) {
        ArrayList<CarOwner> list = fileManager.search(c -> c.getID() == id);
        return list.isEmpty() ? null : list.get(0);
    }

    public ArrayList<Car> getAllCars() {
        ArrayList<Car> allCars = Car.View();
        ArrayList<Car> ownedCars = new ArrayList<>();
        for (Car car : allCars) {
            if (car.getCarOwner().getID() == this.ID) {
                ownedCars.add(car);
            }
        }
        return ownedCars;
    }

    public static ArrayList<CarOwner> View() {
        return fileManager.loadAll();
    }
}
