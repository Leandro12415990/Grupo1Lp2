package View;

import Model.ResultadoOperacao;
import Utils.Tools;
import Controller.RegistarClienteController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistarClienteView {
    private static int validate = 0;
    public static void MenuRegistarCliente()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String passwordFirst, passwordSecound;

        System.out.println("\n" + "-".repeat(7) + " REGISTO " + "-".repeat(7));

        System.out.print("Nome (-1 para cancelar): ");
        String nome = Tools.scanner.nextLine();
        if(nome.equals("-1")) {
            MenuInicialView.menuInicial();
        }
        System.out.print("E-mail (-1 para cancelar): ");
        String email = Tools.scanner.nextLine();
        if(email.equals("-1")) {
            MenuInicialView.menuInicial();
        }
        LocalDate nascimento = null;
        boolean dataValida = false;
        do {
            System.out.print("Data de Nascimento (dd/MM/yyyy) (-1 para cancelar): ");
            try {
                String data = Tools.scanner.nextLine();
                if(data.equals("-1")) {
                    MenuInicialView.menuInicial();
                } else {
                    nascimento = LocalDate.parse(data, formatter);
                    dataValida = true;
                }


            }
            catch (Exception e) {
                System.out.println("Tipo de data inválida, tente novamente...\n");
            }
        } while (!dataValida);
        System.out.print("Morada (-1 para cancelar): ");
        String morada = Tools.scanner.nextLine();
        if(morada.equals("-1")) {
            MenuInicialView.menuInicial();
        }
        boolean respVerificarPassword = false;
        do
        {
            System.out.print("Insira a Password (-1 para cancelar): ");
            passwordFirst = Tools.scanner.nextLine();
            if(passwordFirst.equals("-1")) {
                MenuInicialView.menuInicial();
            }
            System.out.print("Repita a Password: ");
            passwordSecound = Tools.scanner.nextLine();
            respVerificarPassword = verificarPassword(passwordFirst, passwordSecound);
            if (!respVerificarPassword) System.out.println("Erro, passwords não coincidem, tente novamente...\n");
        } while (!respVerificarPassword);

        ResultadoOperacao resultado = RegistarClienteController.verificarDados(nome, email, nascimento, morada, passwordFirst, passwordSecound);
        if (resultado.Sucesso) {
            System.out.println("Cliente registado com sucesso");
        } else {
            System.out.println(resultado.msgErro);
        }

    }

    private static boolean verificarPassword(String passwordFirst, String passwordSecound)
    {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
    }
}
