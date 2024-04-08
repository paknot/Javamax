package task2;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Controller {

    private final WaitingList waitingList = new WaitingList();
    private final List<SwimLesson> allLessons = new LinkedList<>();
    private final List<SwimStudent> students = new LinkedList<>();
    private final List<Instructor> instructors = new LinkedList<>();
    private final Scanner scanner = new Scanner(System.in);

    //Max number of students per lesson
    private static final int MAX_STUDENTS_PER_LESSON = 4;

    //Start all needed functions
    public void start() {
        initializeDummies();
        initializeSwimLessons();
        assignStudentsToClasses();
        Display.greetUser();
        Display.showMenu(this);
    }
    //Predefined instructors

    private void initializeDummies() {
        instructors.add(new Instructor("Jim Big"));
        instructors.add(new Instructor("Andre Porta"));
        instructors.add(new Instructor("Juan"));
        instructors.add(new Instructor("Anrew Smith"));
        instructors.add(new Instructor("Andrew Tate"));
        instructors.add(new Instructor("Big Hoss"));

        SwimStudent alice = new SwimStudent("alice", "NOVICE");
        SwimStudent bob = new SwimStudent("bobek", "IMPROVER");
        SwimStudent charlie = new SwimStudent("charlie", "ADVANCED");
        students.add(alice);
        students.add(bob);
        students.add(charlie);

    }
    //Assing hardcoded students to random classes
    
    public void assignStudentsToClasses() {
    Random random = new Random(); // For selecting a random lesson

    for (SwimStudent student : students) {
        // Filter lessons based on student level and pick a random one
        List<SwimLesson> lessonsForLevel = allLessons.stream()
                .filter(lesson -> lesson.getLevel().toString().equalsIgnoreCase(student.getLevel()) && lesson.getAttendees().size() < MAX_STUDENTS_PER_LESSON)
                .collect(Collectors.toList());

        if (!lessonsForLevel.isEmpty()) {
            // Select a random lesson from the filtered list
            SwimLesson assignedLesson = lessonsForLevel.get(random.nextInt(lessonsForLevel.size()));
            assignedLesson.addStudent(student);
            System.out.println("Assigned " + student.getName() + " to a " + student.getLevel() + " class on " + assignedLesson.getDay() + " at " + assignedLesson.getStartTime() + ", Instructor: " + assignedLesson.getInstructor().getName() + ".");
        } else {
            System.out.println("No available class for " + student.getName() + ". Adding to the waiting list.");
            waitingList.addStudent(student);
        }
    }
}

    //Start swim Lessons and add intrtuctors to them
    public void initializeSwimLessons() {

        int instructorIndex = 0; // To cycle through instructors

        //A loop that will assign Instructors to lessons in a rounnd robin style
        for (WeekDay day : WeekDay.values()) {
            for (String time : new String[]{"17:00", "17:30", "18:00", "18:30", "19:00", "19:30"}) {
                for (SwimLevel level : SwimLevel.values()) {
                    SwimLesson lesson = new SwimLesson();
                    lesson.setDay(day);
                    lesson.setStartTime(time);
                    lesson.setLevel(level);

                    // Assign an instructor in a round-robin fashion
                    Instructor assignedInstructor = instructors.get(instructorIndex % instructors.size());
                    lesson.setInstructor(assignedInstructor);
                    // Ensure the Instructor class can link back to lessons if necessary

                    allLessons.add(lesson);

                    instructorIndex++; // Move to the next instructor
                }
            }
        }
    }

//  View student info (number 1)
    public void viewSwimStudentInformation() {
        //Check if there are any students in the system
        if (students.isEmpty()) {
            System.out.println("There are no swim students to display.");
            return;
        }

        // Sort students alphabetically by name
        List<SwimStudent> sortedStudents = students.stream()
                .sorted(Comparator.comparing(SwimStudent::getName))
                .collect(Collectors.toList());

        //Print all list of available students
        System.out.println("Swim Student List:");
        for (int i = 0; i < sortedStudents.size(); i++) {
            System.out.println((i + 1) + ". " + sortedStudents.get(i).getName());
        }

        System.out.print("Select a swim student by entering the number next to their name: ");
        int selection;

        //Check if a number of a student is valid
        while (true) {
            try {
                System.out.print("\nEnter a number: ");
                selection = Integer.parseInt(scanner.nextLine());
                break; // Exit the loop if a number is successfully parsed
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        int index = selection - 1;
        System.out.println("The selected index is: " + index);

        if (index < 0 || index >= sortedStudents.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        SwimStudent selectedStudent = sortedStudents.get(index);

        // Finding the student's lesson, if any
        Optional<SwimLesson> lesson = allLessons.stream()
                .filter(l -> l.getAttendees().contains(selectedStudent))
                .findFirst();

        // Display the selected student's information
        System.out.println("\nSelected Student Information:");
        System.out.println("Name: " + selectedStudent.getName());
        System.out.println("Level: " + selectedStudent.getLevel());

        if (lesson.isPresent()) {
            SwimLesson l = lesson.get();
            System.out.println("Lesson Day: " + l.getDay());
            System.out.println("Lesson Start Time: " + l.getStartTime());
            System.out.println("Instructor: " + l.getInstructor().getName());
        } else {
            System.out.println("No lesson found for this student.");
        }

        // Display qualifications
        System.out.println("Qualifications:");
        if (!selectedStudent.getQualifications().isEmpty()) {
            selectedStudent.getQualifications().forEach(q -> System.out.println(q.toString()));
        } else {
            System.out.println("No qualifications found.");
        }
    }

//all swim lessons from all instructors
    private List<SwimLesson> getAllSwimLessons() {
        List<SwimLesson> allLessons = new LinkedList<>();
        for (Instructor instructor : instructors) {
            allLessons.addAll(instructor.getLessons());
        }
        return allLessons;
    }

    //View swim lesson (number 2)
    public void viewSwimLessonDetails() {
        // Display available days
        System.out.println("Available Days:");
        Arrays.stream(WeekDay.values()).forEach(day -> System.out.println(day.name()));
        WeekDay day = promptForDay();

        // Since the original approach didn't work as expected, ensure lessons are pre-filtered for the day
        List<SwimLesson> lessonsForDay = allLessons.stream()
                .filter(lesson -> lesson.getDay().equals(day))
                .collect(Collectors.toList());

        if (lessonsForDay.isEmpty()) {
            System.out.println("No lessons found for " + day + ".");
            return;
        }

        // Display available times
        System.out.println("Available Times for " + day + ":");
        lessonsForDay.stream()
                .map(SwimLesson::getStartTime)
                .distinct()
                .forEach(System.out::println);

        String startTime = promptForTime();

        // Filter lessons by the selected time
        List<SwimLesson> lessonsForTime = lessonsForDay.stream()
                .filter(lesson -> lesson.getStartTime().equals(startTime))
                .collect(Collectors.toList());

        if (lessonsForTime.isEmpty()) {
            System.out.println("No lessons found for " + day + " at " + startTime + ".");
            return;
        }

        // Display available levels
        System.out.println("Available Levels for " + day + " at " + startTime + ":");
        lessonsForTime.stream()
                .map(lesson -> lesson.getLevel().name())
                .distinct()
                .forEach(System.out::println);

        //Get the level form the user        
        String levelInput = promptForLevel();
        SwimLevel level;
        try {
            level = SwimLevel.valueOf(levelInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid level entered. Please enter a valid swim level.");
            return;
        }

        // Find the matching lesson
        Optional<SwimLesson> matchingLesson = lessonsForTime.stream()
                .filter(lesson -> lesson.getLevel().equals(level))
                .findFirst();

        //No matching lessons with given details        
        if (!matchingLesson.isPresent()) {
            System.out.println("No swim lesson found for the given details.");
            return;
        }

        SwimLesson selectedLesson = matchingLesson.get();
        Instructor instructor = selectedLesson.getInstructor();
        Set<SwimStudent> attendees = selectedLesson.getAttendees();
        int availableSpots = MAX_STUDENTS_PER_LESSON - attendees.size();

        //Print lessons details
        System.out.println("Instructor's Name: " + instructor.getName());
        System.out.println("Students enrolled in the class:");
        attendees.forEach(student -> System.out.println(student.getName()));
        System.out.println(availableSpots > 0 ? "Spaces available: " + availableSpots : "The class is currently full.");
    }

    //View Instructors schedule (number 3)
    public void viewInstructorSchedule() {
        // Sort instructors alphabetically by name
        instructors.sort(Comparator.comparing(Instructor::getName));

        // Display the list of instructors
        System.out.println("Our instructors:");
        instructors.forEach(instructor -> System.out.println(instructor.getName()));

        // Get the user's choice
        System.out.println("Select an instructor by typing their name:");
        String instructorName = scanner.nextLine();
        Optional<Instructor> selectedInstructorOpt = instructors.stream()
                .filter(instructor -> instructor.getName().equalsIgnoreCase(instructorName))
                .findFirst();

        //Check if instructor of a given name exist
        if (!selectedInstructorOpt.isPresent()) {
            System.out.println("Instructor not found.");
            return;
        }

        Instructor selectedInstructor = selectedInstructorOpt.get();
        // Filter the allLessons list for lessons taught by the selected instructor
        List<SwimLesson> lessonsForInstructor = allLessons.stream()
                .filter(lesson -> lesson.getInstructor().equals(selectedInstructor))
                .collect(Collectors.toList());

        //Check if Instructor  lessons
        if (lessonsForInstructor.isEmpty()) {
            System.out.println("No lessons found for Instructor " + selectedInstructor.getName() + ".");
            return;
        }

        // Print the schedule for the instructor
        System.out.println("Schedule for Instructor " + selectedInstructor.getName() + ":");
        for (WeekDay day : WeekDay.values()) {
            final WeekDay currentDay = day; // To use in lambda expression
            List<SwimLesson> lessonsForDay = lessonsForInstructor.stream()
                    .filter(lesson -> lesson.getDay() == currentDay)
                    .sorted(Comparator.comparing(SwimLesson::getStartTime)) // Ensure lessons are in order of start time
                    .collect(Collectors.toList());

            //Display lessons details
            if (!lessonsForDay.isEmpty()) {
                System.out.println(currentDay + ":");
                lessonsForDay.forEach(lesson -> {
                    System.out.println("  Time: " + lesson.getStartTime()
                            + ", Level: " + lesson.getLevel()
                            + ", Students: " + lesson.getAttendees().stream()
                                    .map(SwimStudent::getName)
                                    .collect(Collectors.joining(", ")));
                });
            }
        }
    }

    //Add swim Student (number 4)
    public void addNewSwimStudent() {
        System.out.println("Enter the name of the new swim student:");
        String studentName = scanner.nextLine();
        SwimStudent newStudent = new SwimStudent(studentName, SwimLevel.NOVICE.toString()); //making a new student a NOVICE
        students.add(newStudent);

        System.out.println("Input 'yes' to book a class or 'no' to add student to waiting list: ");
        String choice = askYesOrNo();

        //if they answer yes to swim lesson
        if ("yes".equals(choice)) {

            //display novice level classes on Monday, Wednesday, and Friday
            allLessons.stream()
                    .filter(lesson -> lesson.getLevel() == SwimLevel.NOVICE)
                    .filter(lesson -> lesson.getDay() == WeekDay.MONDAY || lesson.getDay() == WeekDay.WEDNESDAY || lesson.getDay() == WeekDay.FRIDAY)
                    .forEach(lesson -> {
                        String availability = lesson.getAttendees().size() < MAX_STUDENTS_PER_LESSON ? "Available Spots: " + (MAX_STUDENTS_PER_LESSON - lesson.getAttendees().size()) : "Class is full";
                        System.out.println("Day: " + lesson.getDay() + ", Time: " + lesson.getStartTime() + ", Instructor: " + lesson.getInstructor().getName() + " - " + availability);
                    });
            //Ask for day and time
            WeekDay day = promptForDay();
            String startTime = promptForTime();

            Optional<SwimLesson> selectedLesson = allLessons.stream()
                    .filter(lesson -> lesson.getLevel() == SwimLevel.NOVICE && lesson.getDay() == day && lesson.getStartTime().equals(startTime) && lesson.getAttendees().size() < MAX_STUDENTS_PER_LESSON)
                    .findFirst();

            if (selectedLesson.isPresent()) {
                selectedLesson.get().addStudent(newStudent);
                System.out.println("Student " + studentName + " has been added to the novice class on " + day + " at " + startTime + ", Instructor: " + selectedLesson.get().getInstructor().getName() + ".");
            } else {
                System.out.println("No available spots or for this day/time.");
                waitingList.addStudent(newStudent);
                System.out.println("Student has been added to the waiting list.");
            }

            //If they answer no
        } else if ("no".equals(choice)) {
            waitingList.addStudent(newStudent);
            System.out.println("Student has been added to the waiting list.");
        } else {
            System.out.println("Invalid choice. Operation cancelled.");
        }
    }

    //Award a prize (number 5)
    public void awardSwimQualification() {
        // Select Instructor
        instructors.sort(Comparator.comparing(Instructor::getName));
        System.out.println("Instructors:");
        instructors.forEach(instructor -> System.out.println(instructor.getName()));
        System.out.print("Pick and instructor:");
        String instructorName = scanner.nextLine().trim();
        Instructor instructor = instructors.stream()
                .filter(i -> i.getName().equalsIgnoreCase(instructorName))
                .findFirst()
                .orElse(null);

        //Check if Instructor exist
        if (instructor == null) {
            System.out.println("Instructor not found.");
            return;
        }

        // Select Swim Student
        students.sort(Comparator.comparing(SwimStudent::getName));
        System.out.println("Select a swim student by name (Level indicated next to name):");
        students.forEach(student -> System.out.println(student.getName() + " - Level: " + student.getLevel()));
        System.out.print("Enter student's name: ");
        String studentName = scanner.nextLine().trim();
        SwimStudent student = students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(studentName))
                .findFirst()
                .orElse(null);

        //Chekc if a student exist        
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Ask for the type of award
        System.out.println("Is this award for Distance Swim (D) or Personal Survival (P)?");
        String awardType = scanner.nextLine().trim();
        if (awardType.equalsIgnoreCase("D")) {
            handleDistanceAward(student, instructor);
        } else if (awardType.equalsIgnoreCase("P")) {
            //Not gonna work unless ADVANCED           
            handlePersonalSurvivalAward(student, instructor);
        } else {
            System.out.println("Invalid input.");
        }
    }
    //Distance award price 

    private void handleDistanceAward(SwimStudent student, Instructor instructor) {
        List<Double> availableDistances;
        if (student.getLevel().equals(SwimLevel.NOVICE.toString())) {
            availableDistances = Arrays.asList(5.0, 10.0, 20.0);
        } else if (student.getLevel().equals(SwimLevel.IMPROVER.toString())) {
            availableDistances = Arrays.asList(100.0, 200.0, 400.0);
        } else {
            availableDistances = Arrays.asList(800.0, 1500.0, 3000.0);
        }

        System.out.println("Select a distance for the swim award:");
        availableDistances.forEach(distance -> System.out.println(distance + " metres"));
        System.out.print("Enter your choice as a number:");

        double distance;

        while (true) {
            try {
                distance = Double.parseDouble(scanner.nextLine().trim()); // Attempt to parse the input as double
                if (!availableDistances.contains(distance)) {
                    throw new IllegalArgumentException("Invalid selection."); // Throw to catch block if not valid
                }
                break; // Break the loop if input is valid
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid selection. Please select an available distance.");
            }
        }

        if (hasDistanceAward(student, distance)) {
            System.out.println("Distance Swim qualification for " + distance + " metres already awarded.");
            return;
        }

        // Record the distance swim award
        DistanceSwim distanceSwim = new DistanceSwim();
        distanceSwim.setDistance(distance);
        distanceSwim.setAwardedBy(instructor); // Set the instructor
        student.addQualification(distanceSwim);
        System.out.println(distance + " metres Distance Swim qualification awarded.");

        // Check for level upgrade
        if ((student.getLevel().equals(SwimLevel.NOVICE.toString()) && distance == 20.0)
                || (student.getLevel().equals(SwimLevel.IMPROVER.toString()) && distance == 400.0)) {
            upgradeStudentLevel(student);
        }
    }
    //Upgrading student skill level 

    private void upgradeStudentLevel(SwimStudent student) {
        if (student.getLevel().equals(SwimLevel.NOVICE.toString())) {
            student.setLevel(SwimLevel.IMPROVER.toString());
            System.out.println("Student upgraded to IMPROVER level.");
        } else if (student.getLevel().equals(SwimLevel.IMPROVER.toString())) {
            student.setLevel(SwimLevel.ADVANCED.toString());
            System.out.println("Student upgraded to ADVANCED level.");
        }
        waitingList.addStudent(student); //add to waiting list
        System.out.println("Student added to the waiting list for the next level's group.");
    }
    //Personal survival award just for ADVANCED

    private void handlePersonalSurvivalAward(SwimStudent student, Instructor instructor) {
        if (!student.getLevel().equals(SwimLevel.ADVANCED.toString())) {
            System.out.println("Only ADVANCED level students can be assessed for Personal Survival qualifications.");
            return;
        }

        System.out.println("Select a category for the Personal Survival award (Bronze, Silver, Gold):");
        String category = scanner.nextLine().trim().toUpperCase();
        // Validate selection
        if (!Arrays.asList("BRONZE", "SILVER", "GOLD").contains(category)) {
            System.out.println("Invalid selection. Please select Bronze, Silver, or Gold.");
            return; // Optionally, you could loop back to allow another attempt
        }

        SurvivalAward awardLevel = SurvivalAward.valueOf(category); // Assuming category is valid

        if (hasPersonalSurvivalAward(student, awardLevel)) {
            System.out.println("Personal Survival " + category + " qualification already awarded.");
            return;
        }

        PersonalSurvival personalSurvival = new PersonalSurvival();
        personalSurvival.setLevel(SurvivalAward.valueOf(category));
        personalSurvival.setAwardedBy(instructor); // Set the instructor
        student.addQualification(personalSurvival);
        System.out.println("Personal Survival " + category + " qualification awarded.");
    }

    //Check if they have the Distance award already
    private boolean hasDistanceAward(SwimStudent student, double distance) {
        return student.getQualifications().stream()
                .filter(q -> q instanceof DistanceSwim)
                .anyMatch(q -> ((DistanceSwim) q).getDistance() == distance);
    }

    //Check if they habe the Personal Award already
    private boolean hasPersonalSurvivalAward(SwimStudent student, SurvivalAward level) {
        return student.getQualifications().stream()
                .filter(q -> q instanceof PersonalSurvival)
                .anyMatch(q -> ((PersonalSurvival) q).getLevel() == level);
    }

    //Remove from Waiting List
    public void moveStudentFromWaitingList() {
        // Display the ordered waiting list
        displayOrderedWaitingList();

        // Prompt user to select a swim student by name
        System.out.println("Please select a swim student from the waiting list:");
        String studentName = scanner.nextLine();

        // Find the student in the waiting list
        SwimStudent selectedStudent = waitingList.getStudents().stream()
                .filter(student -> student.getName().equalsIgnoreCase(studentName))
                .findFirst()
                .orElse(null);

        if (selectedStudent == null) {
            System.out.println("Student not found in the waiting list.");
            return;
        }

        // Display weekly schedule of classes for the required level
        displayAvailableClassesForLevel(selectedStudent.getLevel());

        // Prompt user to select a session( day and time)
        WeekDay day = promptForDay();
        String startTime = promptForTime();

        Optional<SwimLesson> selectedLessonOpt = allLessons.stream()
                .filter(lesson -> lesson.getLevel().toString().equalsIgnoreCase(selectedStudent.getLevel())
                && lesson.getDay() == day
                && lesson.getStartTime().equals(startTime))
                .findFirst();

        if (selectedLessonOpt.isPresent()) {
            SwimLesson selectedLesson = selectedLessonOpt.get();
            if (selectedLesson.getAttendees().size() < MAX_STUDENTS_PER_LESSON) {
                // Remove the student from their current lesson, if any
                removeFromPreviousClass(selectedStudent);
                // Add the student to the new lesson
                selectedLesson.addStudent(selectedStudent);
                // Remove the student from the waiting list
                waitingList.removeStudent(selectedStudent);
                System.out.println("Student " + studentName + " has been moved to the selected class and removed from the waiting list.");
            } else {
                System.out.println("Selected class is full. Please select a different class.");
            }
        } else {
            System.out.println("No class found for the specified day and time.");
        }
    }
    //Display waiting swimmers in order

    private void displayOrderedWaitingList() {

        waitingList.getStudents().stream()
                .sorted(Comparator.comparing(student -> SwimLevel.valueOf(student.getLevel().toUpperCase())))
                .forEach(student -> System.out.println(student.getName() + " - " + student.getLevel()));
    }

    //Display available classes
    private void displayAvailableClassesForLevel(String level) {
        // Filter the lessons by the given level and display only those with available spots
        getAllSwimLessons().stream()
                .filter(lesson -> lesson.getLevel().toString().equalsIgnoreCase(level) && lesson.getAttendees().size() < MAX_STUDENTS_PER_LESSON)
                .forEach(lesson -> System.out.println("Day: " + lesson.getDay()
                + ", Time: " + lesson.getStartTime()
                + ", Available Spots: " + (MAX_STUDENTS_PER_LESSON - lesson.getAttendees().size())));
    }
    //Remove previous swim lesson

    private void removeFromPreviousClass(SwimStudent student) {
        allLessons.forEach(lesson -> lesson.getAttendees().remove(student));
    }

    //Ask for Day and verify
    private static WeekDay promptForDay() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a day of the week (Monday, Wednesday, Friday): ");
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "MONDAY":
                    return WeekDay.MONDAY;
                case "WEDNESDAY":
                    return WeekDay.WEDNESDAY;
                case "FRIDAY":
                    return WeekDay.FRIDAY;
                default:
                    System.out.println("Invalid day. Please enter either Monday, Wednesday, or Friday.");
            }
        }
    }

    //Ask for time and verify
    private static String promptForTime() {
        Scanner scanner = new Scanner(System.in);
        Set<String> allowedTimes = new HashSet<>(Arrays.asList("17:00", "17:30", "18:00", "18:30", "19:00", "19:30"));

        while (true) {
            System.out.println("Enter a time (HH:MM) like 17:00: ");
            String input = scanner.nextLine().trim();

            if (allowedTimes.contains(input)) {
                return input;
            } else {
                System.out.println("Invalid time. Please enter one of the specified times.");
            }
        }
    }

    //Ask for level and verify
    private static String promptForLevel() {
        Scanner scanner = new Scanner(System.in);
        Set<String> allowedLVL = new HashSet<>(Arrays.asList("NOVICE", "IMPROVER", "ADVANCED"));

        while (true) {
            System.out.println("Enter a level (NOVICE, IMPROVER, ADVANCED");
            String input = scanner.nextLine().trim();

            if (allowedLVL.contains(input)) {
                return input;
            } else {
                System.out.println("Invalid time. Please enter one of the specified times.");
            }
        }
    }

    //Ask yes or no and verify
    public static String askYesOrNo() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Yes/no: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(input) || "no".equals(input)) {
                return input;
            } else {
                System.out.println("Invalid input. Only 'yes' or 'no' are accepted.");
            }
        }
    }

}
