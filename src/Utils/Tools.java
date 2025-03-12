package Utils;

import Model.Utilizador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tools {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Utilizador> utilizadores = new ArrayList<>();

    public static String separador() {
        return ";";
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDateTime(LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(DATA_HORA) : "";
    }


    public static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

    public static List<String[]> lerCSV(String caminho) {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha.split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public static void escreverCSV(String caminho, List<String[]> linhas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminho))) {
            for (String[] linha : linhas) {
                bw.write(String.join(";", linha));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
