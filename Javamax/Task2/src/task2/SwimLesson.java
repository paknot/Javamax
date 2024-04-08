package task2;

import java.util.HashSet;
import java.util.Set;

public class SwimLesson {

    private WeekDay day;
    private String start_time;
    private SwimLevel level;
    private Set<SwimStudent> attendees;
    private Instructor instructor;

    public SwimLesson() {
        attendees = new HashSet<>();
    }

    // Getters and Setters
    public WeekDay getDay() {
        return day;
    }
    //Set day 
    public void setDay(WeekDay day) {
        this.day = day;
    }
    // Get and Set time
    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }
    
    //Get and set level
    
    public SwimLevel getLevel() {
        return level;
    }

    public void setLevel(SwimLevel level) {
        this.level = level;
    }
    //Add student to the lesson
    public void addStudent(SwimStudent student) {
        if (attendees.size() >= 4) {
            throw new IllegalArgumentException("Not allowed to have more than 4 students per lesson");
        }
        attendees.add(student);
    }
    
    //Remove swimStudent
    public boolean remove(SwimStudent student) {
        return attendees.remove(student);
    }
    // Getter and setter for instructor

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
    //Get attendees
    public Set<SwimStudent> getAttendees() {
        return attendees;
    }
}
