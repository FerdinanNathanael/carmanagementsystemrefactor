
package BackendCode;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer extends Person implements Serializable {

    private int Bill;
    private static final FileManager<Customer> fileManager = new FileManager<>("Customer.ser");

    public Customer() {
        super();
    }

    public Customer(int Bill, int ID, String CNIC, String Name, String Contact_No) {
        super(ID, CNIC, Name, Contact_No);
        this.Bill = Bill;
    }

    public int getBill() {
        return Bill;
    }

    public void setBill(int Bill) {
        this.Bill = Bill;
    }

    @Override
    public String toString() {
        return super.toString() + " Customer{" + "Bill=" + Bill + '}' + "\n";
    }

    @Override
    public void Add() {
        ArrayList<Customer> customers = View();
        this.ID = customers.isEmpty() ? 1 : customers.get(customers.size() - 1).getID() + 1;
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

    public static ArrayList<Customer> SearchByName(String name) {
        return fileManager.search(c -> c.getName().equalsIgnoreCase(name));
    }

    public static Customer SearchByCNIC(String CustomerCNIC) {
        ArrayList<Customer> list = fileManager.search(c -> c.getCNIC().equalsIgnoreCase(CustomerCNIC));
        return list.isEmpty() ? null : list.get(0);
    }

    public static Customer SearchByID(int id) {
        ArrayList<Customer> list = fileManager.search(c -> c.getID() == id);
        return list.isEmpty() ? null : list.get(0);
    }

    public static ArrayList<Customer> View() {
        return fileManager.loadAll();
    }
}
