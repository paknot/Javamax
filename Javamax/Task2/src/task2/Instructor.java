package task2;

import java.util.HashSet;
import java.util.Set;

public class Instructor {

    private String name;
    private Set<SwimLesson> lessons;
    private Set<Qualification> qualifications;

    // Constructor
    public Instructor(String name) {
    this.name = name;
    this.lessons = new HashSet<>();
    this.qualifications = new HashSet<>();
}

    // Get name
    public String getName() {
        return name;
    }
    // Add and remove lesson
    public void addLesson(SwimLesson lesson) {
        lessons.add(lesson);
    }

    public void removeLesson(SwimLesson lesson) {
        lessons.remove(lesson);
    }
    //Add qualification
    public void addQualification(Qualification q) {
        qualifications.add(q);
    }
    //Remove qualification
    public void removeQualification(Qualification q) {
        qualifications.remove(q);
    }
    //Get lessons for instructor
    public Set<SwimLesson> getLessons() {
        return lessons;
    }
}
