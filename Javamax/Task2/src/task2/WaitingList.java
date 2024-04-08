package task2;

import java.util.LinkedList;
import java.util.List;

public class WaitingList {

    private List<SwimStudent> students;

    //Constructor
    public WaitingList() {
        this.students = new LinkedList<>();
    }
    //Does it have a student
    public boolean contains(SwimStudent student) {
        return students.contains(student);
    }
    //Add student to waiting list
    public boolean addStudent(SwimStudent student) {
        return students.add(student);
    }
    //Remove student
    public boolean removeStudent(SwimStudent student) {
        return students.remove(student);
    }
    //return students in the waiting list
    public List<SwimStudent> getStudents() {
        return students;
    }
}
