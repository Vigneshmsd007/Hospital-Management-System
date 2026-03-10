package Monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class HospitalManagement {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Vignesh866744";

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        try {
            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");

            Patient patient = new Patient(connection, scanner);
            Doctors doctor = new Doctors(connection, scanner);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Add Doctor");
                System.out.println("4. View Doctors");
                System.out.println("5. Book Appointment");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {

                case 1:
                    patient.addPatient();
                    break;

                case 2:
                    patient.viewPatients();
                    break;

                case 3:
                    doctor.addDoctor();
                    break;

                case 4:
                    doctor.viewDoctors();
                    break;

                case 5:
                    bookAppointment(patient, doctor, connection, scanner);
                    break;

                case 6:
                    connection.close();
                    System.out.println("Connection closed!");
                    return;
                
                    default:
                        System.out.println("Invalid choice!");
                }
            }

            // Close the connection
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        } finally {
            // Ensure the connection is closed in case of an exception
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close connection: " + e.getMessage());
                }
            }
            scanner.close();
        }
    }

    public static void bookAppointment(Patient patient, Doctors doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter doctor ID: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        System.out.print("Enter appointment time (HH:MM:SS): ");
        String appointmentTime = scanner.next();
        
        
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, appointmentTime, connection)) {
            	String appointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    preparedStatement.setString(4, appointmentTime);

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book appointment!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Invalid patient ID or doctor ID!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, String appointmentTime, Connection connection) {

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            preparedStatement.setString(3, appointmentTime);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int count = resultSet.getInt(1);

                if(count == 0){
                    return true;
                }else{
                    System.out.println("This time slot is already booked. Please choose another time.");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }}