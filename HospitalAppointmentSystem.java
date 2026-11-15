import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class Patient {
    int tokenNumber;
    String name;
    int age;
    String department;
    boolean served;

    Patient(int tokenNumber, String name, int age,
            String department) {
        this.tokenNumber = tokenNumber;
        this.name = name;  
        this.age = age;
        this.department = department;
        this.served = false;
    }
}


   
            

public class HospitalAppointmentSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Patient> patients = new ArrayList<>();
    static int nextToken = 1;
    static void savePatients() {
    try {
        FileWriter writer = new FileWriter("patients.txt");

        for (Patient p : patients) {
            writer.write(
                p.tokenNumber + "|" +
                p.name + "|" +
                p.age + "|" +
                p.department + "|" +
                p.served + "\n"
            );
        }

        writer.close();
        System.out.println("Patient details saved!");

    } catch (IOException e) {
        System.out.println("Error saving patient details.");
    }
}
    static void loadPatients() {
    try {
        BufferedReader reader =
            new BufferedReader(new FileReader("patients.txt"));

        String line;

        while ((line = reader.readLine()) != null) {
            String[] data = line.split("\\|");

            Patient p = new Patient(
                Integer.parseInt(data[0]),
                data[1],
                Integer.parseInt(data[2]),
                data[3]
            );

            p.served = Boolean.parseBoolean(data[4]);

            patients.add(p);

            if (p.tokenNumber >= nextToken) {
                nextToken = p.tokenNumber + 1;
            }
        }

        reader.close();

    } catch (FileNotFoundException e) {
        System.out.println("No previous patient records found.");
    } catch (IOException e) {
        System.out.println("Error loading patient details.");
    }
}

    public static void main(String[] args) {
        loadPatients();
        int choice=0;

        do {
            System.out.println("\n================================");
            System.out.println(" HOSPITAL APPOINTMENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Register Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient by Token");
            System.out.println("4. Call Next Patient");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerPatient();
                    break;

                case 2:
                    viewPatients();
                    break;

                case 3:
                    searchPatient();
                    break;

                case 4:
                    callNextPatient();
                    break;

                case 5:
                    System.out.println("Thank you for using the system.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);

        sc.close();
    }

    static void registerPatient() {

        System.out.print("Enter patient name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter patient age: ");

        if (!sc.hasNextInt()) {
            System.out.println("Age must be a number.");
            sc.nextLine();
            return;
        }

        int age = sc.nextInt();
        sc.nextLine();

        if (age < 0 || age > 120) {
            System.out.println("Please enter a valid age.");
            return;
        }

        System.out.println("\nSelect Department:");
        System.out.println("1. General Medicine");
        System.out.println("2. Pediatrics");
        System.out.println("3. Orthopedics");
        System.out.print("Enter department choice: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid department choice.");
            sc.nextLine();
            return;
        }

        int departmentChoice = sc.nextInt();
        sc.nextLine();

        String department;

        switch (departmentChoice) {
            case 1:
                department = "General Medicine";
                break;
            case 2:
                department = "Pediatrics";
                break;
            case 3:
                department = "Orthopedics";
                break;
            default:
                System.out.println("Invalid department.");
                return;
        }

        Patient p = new Patient(
            nextToken, name, age, department
        );

        patients.add(p);
        savePatients();

        System.out.println("\nAppointment Registered!");
        System.out.println("Patient Name: " + p.name);
        System.out.println("Token Number: H"
                + String.format("%03d", p.tokenNumber));
        System.out.println("Department: " + p.department);

        nextToken++;
    }

    static void viewPatients() {

        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }

        System.out.println("\n--- Patient List ---");

        for (Patient p : patients) {
            System.out.println(
                "Token: H" + String.format("%03d", p.tokenNumber)
                + " | Name: " + p.name
                + " | Age: " + p.age
                + " | Department: " + p.department
                + " | Status: "
                + (p.served ? "Completed" : "Waiting")
            );
        }
    }

    static void searchPatient() {

        System.out.print("Enter token number (example: 1): ");

        if (!sc.hasNextInt()) {
            System.out.println("Token must be a number.");
            sc.nextLine();
            return;
        }

        int token = sc.nextInt();
        sc.nextLine();

        for (Patient p : patients) {
            if (p.tokenNumber == token) {
                System.out.println("\nPatient Found!");
                System.out.println("Name: " + p.name);
                System.out.println("Age: " + p.age);
                System.out.println("Department: " + p.department);
                System.out.println("Token: H"
                        + String.format("%03d", p.tokenNumber));
                System.out.println("Status: "
                        + (p.served ? "Completed" : "Waiting"));
                return;
            }
        }

        System.out.println("Patient not found.");
    }

    static void callNextPatient() {

        for (Patient p : patients) {
            if (!p.served) {
                System.out.println("\nCalling Next Patient");
                System.out.println("Token: H"
                        + String.format("%03d", p.tokenNumber));
                System.out.println("Patient Name: " + p.name);
                System.out.println("Department: " + p.department);

                p.served = true;
                savePatients();
                return;
            }
        }

        System.out.println("No patients are waiting.");
    }
}
