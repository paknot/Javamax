package task2;

public class PersonalSurvival extends Qualification {
    private SurvivalAward level;
    private Instructor awardedBy; // New attribute for the instructor

    // Getters and Setters
    public SurvivalAward getLevel() {
        return level;
    }

    public void setLevel(SurvivalAward level) {
        this.level = level;
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
        return "PersonalSurvival level: " + level + ", Awarded by: " + awardedBy.getName();
    }
}
