package View;

import Utils.Tools;
import Controller.RegistarClienteController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistarClienteView {
    public static void MenuRegistarCliente()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String passwordFirst, passwordSecound;

        System.out.println("=== Registar ===");
        System.out.println("Nome:");
        String nome = Tools.scanner.nextLine();
        System.out.println("E-mail:");
        String email = Tools.scanner.nextLine();
        LocalDate nascimento = null;
        boolean dataValida = false;
        do {
            System.out.println("Data de Nascimento (dd/MM/yyyy):");
            try {
                nascimento = LocalDate.parse(Tools.scanner.nextLine(), formatter);
                dataValida = true;
            }
            catch (Exception e) {
                System.out.println("Tipo de data inválida, tente novamente");
            }
        } while (!dataValida);
        System.out.println("Morada:");
        String morada = Tools.scanner.nextLine();
        boolean respVerificarPassword = false;
        do
        {
            System.out.println("Insira a Password:");
            passwordFirst = Tools.scanner.nextLine();
            System.out.println("Repita a Password:");
            passwordSecound = Tools.scanner.nextLine();
            respVerificarPassword = verifivarPassword(passwordFirst, passwordSecound);
            if (!respVerificarPassword) System.out.println("Erro, passwords nao coincidem, tente novamente");
        } while (!respVerificarPassword);

        boolean resp = RegistarClienteController.verificarDados(nome, email, nascimento, morada, passwordFirst, passwordSecound);
        if (resp) System.out.println("Cliente registado com sucesso");
        else System.out.println("Erro ao registar cliente");
    }

    private static boolean verifivarPassword(String passwordFirst, String passwordSecound)
    {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
    }
}
