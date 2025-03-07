package Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Tools {

    public static String separador(){
        return ";";
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            System.err.println("Data inválida: " + dateStr);
            return null;
        }
    }

    public static String formatDate(LocalDate date) {
        return (date != null) ? date.format(FORMATTER) : "";
    }
}
