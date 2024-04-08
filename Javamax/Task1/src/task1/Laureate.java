package task1;

import java.util.Arrays;
import java.util.List;

public class Laureate {

    String name;
    String birthYear;
    String deathYear; // null on default unless they are dead
    String country;
    String language;
    String citation;
    List<String> genres;

    public Laureate(String name, String birthYear, String deathYear, String country, String language, String citation, List<String> genres) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.country = country;
        this.language = language;
        this.citation = citation;
        this.genres = genres;
    }
    
    public String getName() {
        return this.name;
    }

    // Getter for the country
    public String getCountry() {
        return this.country;
    }

    @Override
    public String toString() {
        
        String firstHead = "| Winner(s)                 ";
        String secondHead = "| Born ";
        String thirdHead = "| Died ";
        String fourthHead = "| Language(s)           ";
        String fifthHead = "| Genre(s)             ";
        
        String line = Controller.repeatChar('-', 90);

        StringBuilder sb = new StringBuilder();
        sb.append(line).append("\n");
        sb.append(firstHead).append(secondHead).append(thirdHead).append(fourthHead).append(fifthHead).append("|").append("\n");
        sb.append(line).append("\n");

        List<List<String>> rows = Arrays.asList(
            Arrays.asList(firstHead, name),
            Arrays.asList(secondHead, birthYear),
            Arrays.asList(thirdHead, deathYear != null ? deathYear : ""),
            Arrays.asList(fourthHead, language),
            Arrays.asList(fifthHead, genres.get(0))
        );

        for (List<String> item : rows) {
            String cell = "| " + item.get(1) + "%s";
            sb.append(String.format(cell, Controller.repeatChar(' ', item.get(0).length() - cell.length() + 2)));
        }
        sb.append("|\n");
        
        StringBuilder emptyRowBuilder = new StringBuilder("");
        for (String head : Arrays.asList(firstHead, secondHead, thirdHead, fourthHead)) {
            emptyRowBuilder.append("|").append(Controller.repeatChar(' ', head.length() - 1));
        }
        
        String emptyRow = emptyRowBuilder.append("| %s |").toString();
        for (int i = 1; i < genres.size(); i++) {
            String genre = genres.get(i);
            sb.append(String.format(emptyRow, genre + Controller.repeatChar(' ', fifthHead.length() - genre.length() - 3))).append("\n");
        }
        
        sb.append(line).append('\n');
        sb.append("|").append(Controller.centerText("Citation:", line.length() - 2)).append("|").append("\n");

        int CITE_LIMIT = 80;
        while (citation.length() > CITE_LIMIT) {
            String chunk = citation.substring(0, citation.substring(0, CITE_LIMIT).lastIndexOf(" "));
            citation = citation.substring(chunk.length() + 1).trim();
            sb.append("|").append(Controller.centerText(chunk, line.length() - 2)).append("|").append("\n");
        }

        sb.append("|").append(Controller.centerText(citation, line.length() - 2)).append("|").append("\n");
        sb.append(line).append('\n');
        return sb.toString();
    }
    
}
