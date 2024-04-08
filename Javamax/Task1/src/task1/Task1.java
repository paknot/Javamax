package task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1 {

  public void parseLiteraturePrizesFromFile(String fileName, List<LiteraturePrize> literaturePrizes) {
    try {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        LiteraturePrize currentPrize = null;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            // Start a new LiteraturePrize if the line is a year (including "Not awarded" years)
            if (line.matches("\\d{4}")) {
                // If there's an existing currentPrize, add it to the list
                if (currentPrize != null) {
                    literaturePrizes.add(currentPrize);
                }
                // Start a new prize for the year
                currentPrize = new LiteraturePrize(Integer.parseInt(line));
            } else if (line.startsWith("Not awarded")) {
                // Explicitly handle "Not awarded" years if necessary
                if (currentPrize != null) {
                    currentPrize.winners = new ArrayList<>();
                }
            } else if (!line.trim().isEmpty() && !line.equals("-----")) {
                // Process laureate details, assuming the line after the year is a laureate
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    // Parse the laureate details as before
                    String[] nameAndYears = parts[0].trim().split("\\s+");
                    String name = String.join(" ", Arrays.copyOf(nameAndYears, nameAndYears.length - 1)).replace("(b.", "").trim();
                    String birthYear = nameAndYears[nameAndYears.length - 1];
                    birthYear = birthYear.replace("(", "").replace(")", "");
                    String deathYear = "n/a"; // Default deathYear to "n/a"

                   if (birthYear.startsWith("b.")) {
                        birthYear = birthYear.substring(2); // Remove 'b.' prefix
                    } else {
                        String[] years = birthYear.split("-");
                        birthYear = years[0];
                        if (years.length > 1 && !years[1].isEmpty() && !years[1].equals("b.")) {
                            deathYear = years[1];
                            deathYear = deathYear.replaceAll("\\D+", ""); // Remove any non-numeric characters
                        }
                        birthYear = birthYear.replaceAll("\\D+", ""); // Clean birthYear of any non-numeric characters
                    }

                    String country = parts[1].trim();
                    String language = parts[2].trim();
                    // Assuming next line is citation and the line after next is genres
                    String citation = lines.get(++i).trim();
                    List<String> genres = Arrays.asList(lines.get(++i).trim().split(", "));

                    Laureate laureate = new Laureate(name, birthYear, deathYear, country, language, citation, genres);
                    if (currentPrize != null) {
                        currentPrize.addWinner(laureate);
                    }
                }
            }
        }

        // Add the last prize if it hasn't been added yet
        if (currentPrize != null) {
            literaturePrizes.add(currentPrize);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        String fileName = "literature-prizes.txt";
        List<LiteraturePrize> prizes = new ArrayList<>();
        new Task1().parseLiteraturePrizesFromFile(fileName, prizes);

        Controller display = new Controller(prizes);
        display.displayMenu();
    }
}
