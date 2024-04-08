package task1;

import java.util.ArrayList;

public class LiteraturePrize {

     int year;
    ArrayList<Laureate> winners;

    public LiteraturePrize(int year) {
        this.year = year;
        this.winners = new ArrayList<>();
    }

    public void addWinner(Laureate winner) {
        winners.add(winner);
    }
    public int getYear() {
        return this.year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (winners.isEmpty()) {
            sb.append("| ").append(year).append(" | NOT AWARDED%s|\n");
        } else {
            sb.append("| ").append(year).append(" | ");
            for (int i = 0; i < winners.size(); i++) {
                Laureate winner = winners.get(i);
                sb.append(winner.name).append(" [").append(winner.country).append("]");
                if (i < winners.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("%s|\n");
        }
        return sb.toString();
    }

}
