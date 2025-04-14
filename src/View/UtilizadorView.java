package View;

import Controller.*;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Tools;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import static Utils.Tools.scanner;

public class UtilizadorView {

    public static void registarCliente() {
        String passwordFirst, passwordSecound;

        System.out.println("\n" + "-".repeat(7) + " REGISTO " + "-".repeat(7));

        System.out.print("Nome " + Tools.alertaCancelar());
        String nome = Tools.scanner.nextLine();
        if (Tools.verificarSaida(nome)) return;

        System.out.print("E-mail " + Tools.alertaCancelar());
        String email = Tools.scanner.nextLine();
        if (Tools.verificarSaida(email)) return;

        LocalDate nascimento = null;
        boolean dataValida = false;
        do {
            System.out.print("Data de Nascimento (dd/MM/yyyy) " + Tools.alertaCancelar());
            try {
                String data = Tools.scanner.nextLine();
                if (Tools.verificarSaida(email)) return;
                else {
                    nascimento = LocalDate.parse(data, Tools.FORMATTER);
                    dataValida = true;
                }
            } catch (Exception e) {
                System.out.println("Tipo de data inválida, tente novamente...\n");
            }
        } while (!dataValida);
        System.out.print("Morada " + Tools.alertaCancelar());
        String morada = Tools.scanner.nextLine();
        if (Tools.verificarSaida(morada)) return;

        boolean respVerificarPassword = false;
        do {
            System.out.print("Insira a Password " + Tools.alertaCancelar());
            passwordFirst = Tools.scanner.nextLine();
            if (Tools.verificarSaida(passwordFirst)) return;

            System.out.print("Repita a Password: ");
            passwordSecound = Tools.scanner.nextLine();
            respVerificarPassword = UtilizadorController.verificarPassword(passwordFirst, passwordSecound);
            if (!respVerificarPassword) System.out.println("Erro, passwords não coincidem, tente novamente...\n");
        } while (!respVerificarPassword);

        ResultadoOperacao resultado = UtilizadorController.verificarDados(null,nome, email, nascimento, morada, passwordFirst, passwordSecound);
        if (resultado.Sucesso) System.out.println("Cliente registado com sucesso");
        else System.out.println(resultado.msgErro);
    }

    public static void formularioAprovarCliente(int estado) {
        while (true) {
            System.out.println("Email do cliente " + Tools.alertaCancelar());
            String email = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return;

            boolean resFormularioAprovarClienteController = UtilizadorController.aprovarCliente(email, estado);
            if (resFormularioAprovarClienteController) System.out.println("Utilizador alterado com sucesso!");
            else System.out.println("Email inválido");
        }
    }

    public static void editarCliente(Utilizador utilizador) {
        while (true) {
            String passwordFirst;
            String passwordSecound = "";

            System.out.println("\n" + "-".repeat(7) + " REGISTO " + "-".repeat(7));

            System.out.print("Nome " + Tools.alertaCancelar());
            String nome = Tools.scanner.nextLine();
            if (Tools.verificarSaida(nome)) return;

            LocalDate nascimento = null;
            boolean dataValida = false;
            do {
                System.out.print("Data de Nascimento (dd/MM/yyyy) " + Tools.alertaCancelar());
                try {
                    String data = Tools.scanner.nextLine();
                    if (Tools.verificarSaida(data)) return;
                    else if (data.isEmpty()) {
                        dataValida = true;
                        break;
                    } else {
                        nascimento = LocalDate.parse(data, Tools.FORMATTER);
                        dataValida = true;
                    }
                } catch (Exception e) {
                    System.out.println("Tipo de data inválida, tente novamente...\n");
                }
            } while (!dataValida);
            System.out.print("Morada " + Tools.alertaCancelar());
            String morada = Tools.scanner.nextLine();
            if (Tools.verificarSaida(morada)) return;

            boolean respVerificarPassword;
            do {
                System.out.print("Insira a Password " + Tools.alertaCancelar());
                passwordFirst = Tools.scanner.nextLine();
                if (Tools.verificarSaida(passwordFirst)) return;
                if (!passwordFirst.isEmpty()) {
                    System.out.print("Repita a Password: ");
                    passwordSecound = Tools.scanner.nextLine();
                    respVerificarPassword = UtilizadorController.verificarPassword(passwordFirst, passwordSecound);
                    if (!respVerificarPassword)
                        System.out.println("Erro, passwords não coincidem, tente novamente...\n");
                } else break;
            } while (!respVerificarPassword);

            ResultadoOperacao resultado = UtilizadorController.verificarDados(utilizador, nome, utilizador.getEmail(), nascimento, morada, passwordFirst, passwordSecound);
            if (resultado.Sucesso) System.out.println("Cliente alterado com sucesso");
            else System.out.println(resultado.msgErro);
        }
    }

    public static void aprovarCliente(int estado) {
        String menuMSG = "", responseMSG = "";
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
        {
            menuMSG = "Aprovar";
            responseMSG = "aprovados";
        }
        else
        {
            menuMSG = "Inativar";
            responseMSG = "inativados";
        }
        while (true) {
            System.out.println("Menu Aprovar Clientes");
            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) UtilizadorView.mostrarUtilizador(Tools.estadoUtilizador.PENDENTE.getCodigo(), 2);
            if (estado == Tools.estadoUtilizador.INATIVO.getCodigo()) UtilizadorView.mostrarUtilizador(Tools.estadoUtilizador.getDefault().getCodigo(), 2);
            System.out.println("-----------------------------------------------------------------");
            System.out.println(MessageFormat.format("1. {0} todos", menuMSG));
            System.out.println(MessageFormat.format("2. {0} Cliente especifico", menuMSG));
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    boolean respAprovarTodos = UtilizadorController.aprovarTodosClientes(estado);
                    if (respAprovarTodos) System.out.println(MessageFormat.format("Utilizadores {0} com sucesso!", responseMSG));
                    else System.out.println(MessageFormat.format("Utilizadores não foram todos {0} com sucesso," +
                            "liste os utilizadores no menu para verificar quais não foram {0}!", responseMSG));
                    break;
                case 2:
                    UtilizadorView.formularioAprovarCliente(estado);
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    public static void mostrarUtilizador(int estado, int tipo) {
        UtilizadorController.mostrarUtilizador(estado, tipo);
    }

    public static void exibirUtilizadores(List<Utilizador> utilizadores) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE UTILIZADORES " + "=".repeat(5));
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-25s %-10s %-10s\n",
                "Id", "Nome Utilizador", "Email", "Data Nascimento", "Morada", "Data Registo", "Ultimo Login", "Tipo Utilizador", "Estado", "Saldo");
        System.out.println("-".repeat(245));
        for (Utilizador utilizador : utilizadores) {
            String estadoStr = Tools.estadoUtilizador.fromCodigo(utilizador.getEstado()).name();
            String tipoUtilizadorStr = Tools.tipoLeilao.fromCodigo(utilizador.getTipoUtilizador()).name();
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-25s %-10s %-10s\n",
                    utilizador.getId(),
                    utilizador.getNomeUtilizador(),
                    utilizador.getEmail(),
                    utilizador.getDataNascimento(),
                    utilizador.getMorada(),
                    utilizador.getDataRegisto() != null ? utilizador.getDataRegisto().toString() : "N/A",
                    utilizador.getUltimoLogin() != null ? utilizador.getUltimoLogin().toString() : "N/A",
                    tipoUtilizadorStr,
                    estadoStr,
                    utilizador.getSaldo());
        }
    }
}
