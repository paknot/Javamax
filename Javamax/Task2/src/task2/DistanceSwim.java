package task2;

public class DistanceSwim extends Qualification {
    private double distance;
    private Instructor awardedBy; 

    // Getters and Setters
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Instructor getAwardedBy() {
        return awardedBy;
    }

    public void setAwardedBy(Instructor awardedBy) {
        this.awardedBy = awardedBy;
    }

    // Override to String for Qualification display
    @Override
    public String toString() {
        return "DistanceSwim: " + distance + " meters, awarded by: " + awardedBy.getName();
    }
}