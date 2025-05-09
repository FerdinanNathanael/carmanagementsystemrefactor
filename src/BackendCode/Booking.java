
package BackendCode;

import java.io.Serializable;
import java.util.ArrayList;

/*
UNTUK BACKEND SEMUANYA (ntar dibagi)

smell code: duplicate code
reason: add,update,remove,view method di booking,car,customer,carowner
solution: bikin FileManager

smell code: shotgun surgery
reason: ganti behavior dimanapun harus ganti di smua class
solution: read/write operation dalam filemanager

smell code: Large class
reason: booking.java customer.java car.java doing too much
solution: moved logic to Filemanager(sptnya file manager god class must fix)

other smell: manual stream handling, poor encapsulation
*/

public class Booking implements Serializable {

    private int ID;
    private Customer customer;
    private Car car;
    private long RentTime, ReturnTime;

    private static final FileManager<Booking> fileManager = new FileManager<>("Booking.ser");

    public Booking() {}

    public Booking(int ID, Customer customer, Car car, long RentTime, long ReturnTime) {
        this.ID = ID;
        this.customer = customer;
        this.car = car;
        this.RentTime = RentTime;
        this.ReturnTime = ReturnTime;
    }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public long getRentTime() { return RentTime; }
    public void setRentTime(long RentTime) { this.RentTime = RentTime; }

    public long getReturnTime() { return ReturnTime; }
    public void setReturnTime(long ReturnTime) { this.ReturnTime = ReturnTime; }

    @Override
    public String toString() {
        return "Booking{" + "ID=" + ID + ", customer=" + customer + ", car=" + car +
               ", RentTime=" + RentTime + ", ReturnTime=" + ReturnTime + '}' + "\n";
    }

    public void Add() {
        ArrayList<Booking> bookings = View();
        this.ID = bookings.isEmpty() ? 1 : bookings.get(bookings.size() - 1).getID() + 1;
        this.ReturnTime = 0;
        fileManager.saveOrUpdate(this, b -> b.getID() == this.ID);
    }

    public void Update() {
        fileManager.saveOrUpdate(this, b -> b.getID() == this.ID);
    }

    public void Remove() {
        fileManager.remove(b -> b.getID() == this.ID);
    }

    public int calculateBill() {
        long totalTime = (ReturnTime - RentTime) / (1000 * 60 * 60);
        int rate = car.getRentPerHour();
        return totalTime > 0 ? (int) (rate * totalTime) : rate;
    }

    public static ArrayList<Booking> SearchByCustomerID(int customerId) {
        return fileManager.search(b -> b.customer.getID() == customerId);
    }

    public static ArrayList<Booking> SearchByCarRegNo(String carRegNo) {
        return fileManager.search(b -> b.car.getRegNo().equalsIgnoreCase(carRegNo));
    }

    public static ArrayList<Booking> SearchByCarID(int carId) {
        return fileManager.search(b -> b.car.getID() == carId);
    }

    public static ArrayList<Booking> View() {
        return fileManager.loadAll();
    }

    public static ArrayList<Car> getBookedCars() {
        ArrayList<Car> bookedCars = new ArrayList<>();
        for (Booking b : View()) {
            if (b.ReturnTime == 0) {
                bookedCars.add(b.car);
            }
        }
        return bookedCars;
    }

    public static ArrayList<Car> getUnbookedCars() {
        ArrayList<Car> all = Car.View();
        ArrayList<Car> booked = getBookedCars();
        all.removeIf(booked::contains);
        return all;
    }
}
