package task1;

import java.util.Scanner;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.ArrayList;

public class Controller {

    List<LiteraturePrize> literaturePrizes = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public Controller(List<LiteraturePrize> literaturePrizes) {
        this.literaturePrizes = literaturePrizes;
    }

    public void displayMenu() {

        while (true) {
            System.out.println("----------------------");
            System.out.println("Literature prize menu");
            System.out.println("----------------------");
            System.out.println("List ................1");
            System.out.println("Select ..............2");
            System.out.println("Search ..............3");
            System.out.println("Exit.................0");
            System.out.println("----------------------");
            System.out.print("Enter choice > ");

            // Check inf an integer
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid number. Please enter a number between 0 and 3.");
                System.out.print("Enter choice > ");
                scanner.next(); // get invalid inputs
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            // Chech if withing the range
            if (choice < 0 || choice > 3) {
                System.out.println("Invalid choice. Please enter a number between 0 and 3.");
                continue; // Skip loop and ask again
            }

            switch (choice) {
                case 1:
                    System.out.println("List option selected.");
                    int startYear = getValidStart();
                    int endYear = getValidEnd();
                    String winnersList = listPrizeWinners(startYear, endYear, literaturePrizes);
                    System.out.println(winnersList);
                    break;
                case 2:
                    System.out.println("Select option selected.");
                    displayWinnerForYear();
                    break;
                case 3:
                    System.out.println("Search option selected.");
                    searchByGenre();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    // Will never be reached but just in case
                    System.out.println("Unexpected error. Please try again.");
            }
        }
    }
    // This method prints all the winners between two dates given by user

    public String listPrizeWinners(int startYear, int endYear, List<LiteraturePrize> literaturePrizes) {

        List<LiteraturePrize> filteredPrizes = literaturePrizes.stream()
                .filter(prize -> prize.getYear() >= startYear && prize.getYear() <= endYear)
                .collect(Collectors.toList());

        int maxLength = 0;
        for (LiteraturePrize prize : filteredPrizes) {
            if (prize.toString().length() > maxLength) {
                maxLength = prize.toString().length();
            }
        }

        int rowLength = maxLength + 3;

        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append(repeatChar('-', rowLength - 5)).append("\n");
        String header = "| Year | Prize winners (and associated nations)%s|\n";
        header = String.format(header, repeatChar(' ', rowLength - header.length() - 2));
        output.append(header);
        output.append(repeatChar('-', rowLength - 5)).append("\n");
        for (LiteraturePrize prize : filteredPrizes) {
            String prizeString = prize.toString();
            prizeString = String.format(prizeString, repeatChar(' ', rowLength - prizeString.length() - 2));
            output.append(prizeString);
        }

        output.append(repeatChar('-', rowLength - 5)).append("\n");
        return output.toString();
    }

    public void displayWinnerForYear() {
        int year = getValidYear(); // usage of a helper method to get valid year
        // Find the LiteraturePrize object for the given year

        LiteraturePrize prizeForYear = literaturePrizes.stream()
                .filter(prize -> prize.getYear() == year)
                .findFirst()
                .orElse(null);

        // does the literaturePrize exist?
        if (prizeForYear == null) {
            System.out.println("No Literature Prize was awarded in the year " + year + ".");
        } else {
            System.out.println("Winner(s) for the year " + year + ":");
            for (Laureate laureate : prizeForYear.winners) {
                // Usage of toString from Laureate
                System.out.println(laureate.toString());
            }
        }
    }

    // This method searches for all genres of given type and highlights them
    public void searchByGenre() {
        System.out.println("Enter search term for writing genre > ");
        String searchTerm = scanner.nextLine().toLowerCase();
        // Find the Laureate object for the genre
        List<Laureate> matchingLaureates = literaturePrizes.stream()
                .flatMap(prize -> prize.winners.stream())
                .filter(laureate -> laureate.genres.stream().anyMatch(genre -> genre.toLowerCase().contains(searchTerm)))
                .sorted(Comparator.comparing(laureate -> laureate.name))
                .collect(Collectors.toList());

        if (matchingLaureates.isEmpty()) {
            System.out.println("No laureates found for the genre '" + searchTerm + "'.");
            return;
        }

        // Calculate column widths
        int nameWidth = matchingLaureates.stream().mapToInt(laureate -> laureate.name.length()).max().orElse(10);
        int genreWidth = matchingLaureates.stream()
                .map(laureate -> String.join(", ", laureate.genres))
                .mapToInt(String::length)
                .max()
                .orElse(10);

        // Adjust for the uppercased search term and padding
        nameWidth += 2;
        genreWidth = Math.max(genreWidth, searchTerm.length() + 4) + 2;

        // Create header and row format strings using dynamic widths
        String headerFormat = "| %-" + nameWidth + "s | %-" + genreWidth + "s | Year |%n";
        String lineFormat = "| %-" + nameWidth + "s | %-" + genreWidth + "s | %4d |%n";
        // String dashLine = createDashLine(nameWidth + genreWidth + 9); // Adjusted total width for the dash line
        String dashLine = repeatChar('-', nameWidth + genreWidth + 14); // Adjusted total width for the dash line

        //Print top line       
        System.out.println(dashLine);
        System.out.printf(headerFormat, "Name", "Genres");
        System.out.println(dashLine);

        //Print the contents with genre in CAPS
        for (Laureate laureate : matchingLaureates) {
            String highlightedGenres = laureate.genres.stream()
                    .map(genre -> {
                        int index = genre.toLowerCase().indexOf(searchTerm);
                        if (index >= 0) {
                            String match = genre.substring(index, index + searchTerm.length());
                            return genre.substring(0, index) + match.toUpperCase() + genre.substring(index + searchTerm.length());
                        } else {
                            return genre;
                        }
                    })
                    .collect(Collectors.joining(", "));

            int correspondingYear = literaturePrizes.stream()
                    .filter(prize -> prize.winners.contains(laureate))
                    .findFirst()
                    .map(prize -> prize.year)
                    .orElse(0);

            System.out.printf(lineFormat, laureate.name, highlightedGenres, correspondingYear);
            System.out.println(dashLine);
        }
    }

    static String repeatChar(char c, int length) {
        if (length <= 0) {
            return "";
        }
        return String.join("", Collections.nCopies(length, String.valueOf(c)));
    }

    static String centerText(String text, int length) {
        String pad = Controller.repeatChar(' ', (length - text.length() + 1) / 2);
        StringBuilder sb = new StringBuilder(pad).append(text).append(pad);
        if (text.length() % 2 != 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    // A helper method for getting a valid year
    private int getValidYear() {
        int year = 0; // Initialize year

        while (year < 1901 || year > 2022) {
            System.out.println("Enter year of prize (1901 - 2022) > ");
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid year! Please enter a number.");
                scanner.next(); // Consume the non-integer input
                System.out.println("Enter year of prize (1901 - 2022) > ");
            }
            year = scanner.nextInt();

            if (year < 1901 || year > 2022) {
                System.out.println("Invalid year. Please enter a year between 1901 and 2022.");
            }
        }
        return year;
    }

    private int getValidStart() {
        int startYear = 0;
        while (startYear < 1901 || startYear > 2022) {
            System.out.println("Enter start year > ");
            while (!scanner.hasNextInt()) {
                System.out.println("Year not valid, please enter valid year 1901-2022");
                scanner.next(); // Consume the non-integer input
                System.out.println("Enter start year > ");
            }
            startYear = scanner.nextInt();
            if (startYear < 1901 || startYear > 2022) {
                System.out.println("Invalid year. Please enter a year between 1901 and 2022.");
            }
        }
        return startYear;
    }

    private int getValidEnd() {
        int endYear = 0;
        while (endYear < 1901 || endYear > 2022) {
            System.out.println("Enter start year > ");
            while (!scanner.hasNextInt()) {
                System.out.println("Year not valid, please enter valid year 1901-2022");
                scanner.next(); // Consume the non-integer input
                System.out.println("Enter start year > ");
            }
            endYear = scanner.nextInt();
            if (endYear < 1901 || endYear > 2022) {
                System.out.println("Invalid year. Please enter a year between 1901 and 2022.");
            }
        }
        return endYear;
    }

}
