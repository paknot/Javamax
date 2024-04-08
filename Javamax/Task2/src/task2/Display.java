package task2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Display {
    
    Scanner scanner = new Scanner(System.in);
    
        //Method to greet the USer
    public static void greetUser() {
        System.out.println("Welcome to the Swimmatron 3000!");
        System.out.println("The swimming pool runs on Mondays, Wednesdays and Fridays");
        System.out.println("From 17:00 to 19:30");
        System.out.println("Each session last 30 minutes");
        System.out.println("");
    }
    //Menu
    public static void showMenu(Controller controller) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("|             Menu                 |");
            System.out.println("------------------------------------");
            System.out.println("|         Available Actions:       |");
            System.out.println("|1. View Swim Student Information  |");
            System.out.println("|2. View Swim Lesson Details       |");
            System.out.println("|3. View Instructor Schedule       |");
            System.out.println("|4. Add New Swim Student           |");
            System.out.println("|5. Award Swim Qualification       |");
            System.out.println("|6. Move Student From Waiting List |");
            System.out.println("|0. Exit                           |");
            System.out.println("-----------------------------------");
            System.out.print("Please enter your choice:");
            

            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (input) {
                    case 1:
                        controller.viewSwimStudentInformation();
                        break;
                    case 2:
                        controller.viewSwimLessonDetails();
                        break;
                    case 3:
                        controller.viewInstructorSchedule();
                        break;
                    case 4:
                        controller.addNewSwimStudent();
                        break;
                    case 5:
                        controller.awardSwimQualification();
                        break;
                    case 6:
                        controller.moveStudentFromWaitingList();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); 
            }
        }
    }
    
}
