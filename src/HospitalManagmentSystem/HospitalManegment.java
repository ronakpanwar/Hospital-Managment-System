package HospitalManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManegment {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";

    private static final String username = "root";

    private static final String password = "root";

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
           e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Docter doctor = new Docter(connection);
            while(true){
                System.out.println("HOSPITAL MANAGMENT SYSTEM");
                System.out.println("1. add Patient");
                System.out.println("2. view Patients");
                System.out.println("3. view Doctor ");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choise ");
                int choise = scanner.nextInt();
                switch (choise){
                    case 1:
                       // add
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //
                        bookAppointment(patient, doctor , connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        return ;
                    default:
                        System.out.println("Enter Valid Option ");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient , Docter docter , Connection connection , Scanner scanner){
        System.out.print("Enter patient id : ");
        int patientId = scanner.nextInt();
        System.out.print("Enter docter id : ");
        int docterId = scanner.nextInt();
        System.out.print("Enter appointment Date (YYYY-MM-DD) : ");
        String date = scanner.next();
        if(patient.getPatientById(patientId) && docter.getDoctorById(docterId)){
            if(cheakDocterAvailable(docterId, date ,connection)){
                     String query = "INSERT INTO appointment(patient_id , doctor_id , appointment_date) VALUES (?,?,?)";
                     try{
                         PreparedStatement preparedStatement = connection.prepareStatement(query);
                         preparedStatement.setInt(1,patientId);
                         preparedStatement.setInt(2,docterId);
                         preparedStatement.setString(3,date);
                         int rowAffected = preparedStatement.executeUpdate();
                         if(rowAffected>0){
                             System.out.println("Apppointment booked");
                         }else{
                             System.out.println("Failed to book");
                         }
                     }catch (SQLException e){
                         e.printStackTrace();
                     }

            }else{
                System.out.println("Docter is not available in this date..");
            }

        }else{
            System.out.println("Either docter or patient dosnt exist ..");
        }
    }

    public static boolean cheakDocterAvailable(int docterId , String date , Connection connection){
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,docterId);
            preparedStatement.setString(2 , date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next() ){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return false ;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
