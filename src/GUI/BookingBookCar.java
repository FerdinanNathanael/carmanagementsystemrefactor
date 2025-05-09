package GUI;

import BackendCode.Booking;
import BackendCode.Car;
import BackendCode.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
smell code: long method
reason: dev wanted all logic in same method
solution: extract, split(onbookclicked,validatecarId,validatecustomerid)

smell code: feature envy
reason: dev langsung pakai car.searchbyId dan customer.searchbyid
solution: business logic abstracted

smell code: duplicate code
reason: validation and customerId banyak
solution: make it into a method

other smell code: poor class naming, magic string
*/

public class BookingBookCar extends JFrame {

    private JButton bookButton, cancelButton;
    private JLabel carIdLabel, carIdErrorLabel, customerIdLabel, customerIdErrorLabel;
    private JTextField carIdField, customerIdField;

    private Car selectedCar;
    private Customer selectedCustomer;

    public BookingBookCar() {
        super("Book Car");

        setupUI();
        setupEvents();
    }

    private void setupUI() {
        setLayout(new FlowLayout());
        setSize(320, 240);
        setResizable(false);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Parent_JFrame.getMainFrame().setEnabled(true);
                dispose();
            }
        });

        // Initialize components
        carIdLabel = new JLabel("Enter Car ID to be Booked");
        carIdField = new JTextField();
        carIdField.setPreferredSize(new Dimension(240, 22));
        carIdErrorLabel = new JLabel();
        carIdErrorLabel.setPreferredSize(new Dimension(280, 14));
        carIdErrorLabel.setForeground(Color.RED);

        customerIdLabel = new JLabel("Enter Customer ID");
        customerIdField = new JTextField();
        customerIdField.setPreferredSize(new Dimension(240, 22));
        customerIdErrorLabel = new JLabel();
        customerIdErrorLabel.setPreferredSize(new Dimension(280, 14));
        customerIdErrorLabel.setForeground(Color.RED);

        bookButton = new JButton("Book");
        bookButton.setPreferredSize(new Dimension(100, 22));

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 22));

        // Add to layout
        add(carIdLabel);
        add(carIdField);
        add(carIdErrorLabel);

        add(customerIdLabel);
        add(customerIdField);
        add(customerIdErrorLabel);

        add(bookButton);
        add(cancelButton);
    }

    private void setupEvents() {
        bookButton.addActionListener(e -> onBookClicked());
        cancelButton.addActionListener(e -> {
            Parent_JFrame.getMainFrame().setEnabled(true);
            dispose();
        });
    }

    private void onBookClicked() {
        boolean validCar = validateCarId();
        boolean validCustomer = validateCustomerId();

        if (validCar && validCustomer) {
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "You are about to book the car:\n" + selectedCar.toString() +
                "\nFor customer:\n" + selectedCustomer.toString() +
                "\n\nAre you sure you want to continue?",
                "Booking Confirmation",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (confirm == JOptionPane.OK_OPTION) {
                Booking booking = new Booking(0, selectedCustomer, selectedCar, System.currentTimeMillis(), 0);
                booking.Add();

                // Update UI
                Parent_JFrame.getMainFrame().getContentPane().removeAll();
                Booking_Details detailsPanel = new Booking_Details();
                Parent_JFrame.getMainFrame().add(detailsPanel.getMainPanel());
                Parent_JFrame.getMainFrame().getContentPane().revalidate();

                JOptionPane.showMessageDialog(null, "Car successfully booked!");
                Parent_JFrame.getMainFrame().setEnabled(true);
                dispose();
            }
        }
    }

    private boolean validateCarId() {
        String input = carIdField.getText().trim();

        if (input.isEmpty()) {
            carIdErrorLabel.setText("Please enter a Car ID.");
            return false;
        }

        try {
            int id = Integer.parseInt(input);
            if (id <= 0) {
                carIdErrorLabel.setText("Car ID must be a positive number.");
                return false;
            }

            Car car = Car.SearchByID(id);
            if (car == null) {
                carIdErrorLabel.setText("Car ID does not exist.");
                return false;
            }

            if (car.isRented()) {
                carIdErrorLabel.setText("This car is already booked.");
                return false;
            }

            selectedCar = car;
            carIdErrorLabel.setText("");
            return true;

        } catch (NumberFormatException e) {
            carIdErrorLabel.setText("Invalid Car ID format.");
            return false;
        }
    }

    private boolean validateCustomerId() {
        String input = customerIdField.getText().trim();

        if (input.isEmpty()) {
            customerIdErrorLabel.setText("Please enter a Customer ID.");
            return false;
        }

        try {
            int id = Integer.parseInt(input);
            if (id <= 0) {
                customerIdErrorLabel.setText("Customer ID must be a positive number.");
                return false;
            }

            Customer customer = Customer.SearchByID(id);
            if (customer == null) {
                customerIdErrorLabel.setText("Customer ID does not exist.");
                return false;
            }

            selectedCustomer = customer;
            customerIdErrorLabel.setText("");
            return true;

        } catch (NumberFormatException e) {
            customerIdErrorLabel.setText("Invalid Customer ID format.");
            return false;
        }
    }
}
