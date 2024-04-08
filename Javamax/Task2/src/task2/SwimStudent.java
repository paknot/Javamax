package task2;

import java.util.LinkedList;
import java.util.List;

public class SwimStudent {

    private String name;
    private String level;
    private List<Qualification> qualifications;
    
    //Constructor
    public SwimStudent(String name, String level) {
        this.name = name;
        this.level = level;
        this.qualifications = new LinkedList<>();
    }

    // Get name
    public String getName() {
        return name;
    }

    // Get level
    public String getLevel() {
        return level;
    }

    // Set level
    public void setLevel(String level) {
        this.level = level;
    }

    // Update level (oversimplified for now)
    public void updateLevel(String newLevel) {

        this.level = newLevel;
    }
    //Add qualifications to Student
    public void addQualification(Qualification q) {
        qualifications.add(q);
    }
    //Return list of qualifications
    public List<Qualification> getQualifications() {
        return new LinkedList<>(qualifications); 
    }
}
