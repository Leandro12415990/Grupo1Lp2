package View;

import Utils.Tools;

import java.time.LocalDate;

import static Utils.Tools.scanner;

public class EditarClienteView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("Nome:");
            String nome = scanner.nextLine();
            System.out.println("E-mail:");
            String email = scanner.nextLine();
            System.out.println("Data de Nascimento:");
            LocalDate dataNascimento = LocalDate.parse(scanner.nextLine());
            System.out.println("Morada:");
            String morada = scanner.nextLine();
        }
    }
}