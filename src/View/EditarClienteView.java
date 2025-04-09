package View;

import Controller.EditarClienteController;
import Controller.RegistarClienteController;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static Utils.Tools.scanner;

public class EditarClienteView {
    public static void exibirMenu(Utilizador utilizador) {
        while (true) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String passwordFirst, passwordSecound;

            System.out.println("\n" + "-".repeat(7) + " REGISTO " + "-".repeat(7));

            System.out.print("Nome (-1 para cancelar): ");
            String nome = Tools.scanner.nextLine();
            if(nome.equals("-1")) {
                MenuClienteView.exibirMenu(utilizador);
            }
            LocalDate nascimento = null;
            boolean dataValida = false;
            do {
                System.out.print("Data de Nascimento (dd/MM/yyyy) (-1 para cancelar): ");
                try {
                    String data = Tools.scanner.nextLine();
                    if(data.equals("-1")) {
                        MenuClienteView.exibirMenu(utilizador);
                    } else if (data.isEmpty())
                    {
                        dataValida = true;
                        break;
                    }
                    else {
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
                MenuClienteView.exibirMenu(utilizador);
            }

            boolean respVerificarPassword = false;
            do
            {
                System.out.print("Insira a Password (-1 para cancelar): ");
                passwordFirst = Tools.scanner.nextLine();
                if(passwordFirst.equals("-1")) {
                    MenuInicialView.menuInicial();
                }
                if (!passwordFirst.isEmpty())
                {
                    System.out.print("Repita a Password: ");
                    passwordSecound = Tools.scanner.nextLine();
                    respVerificarPassword = verificarPassword(passwordFirst, passwordSecound);
                    if (!respVerificarPassword) System.out.println("Erro, passwords não coincidem, tente novamente...\n");
                } else break;
            } while (!respVerificarPassword);

            ResultadoOperacao resultado = EditarClienteController.verificarDados(utilizador, nome, nascimento, morada, passwordFirst);
            if (resultado.Sucesso) {
                System.out.println("Cliente registado com sucesso");
            } else {
                System.out.println(resultado.msgErro);
            }
        }
    }

    private static boolean verificarPassword(String passwordFirst, String passwordSecound)
    {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
    }
}