package View;

import BLL.FormularioAprovarClienteBLL;
import Controller.AprovarClienteController;
import Utils.Tools;

import java.text.MessageFormat;

import static Utils.Tools.scanner;

public class AprovarClienteView {
    public static void exibirMenu(int estado) {
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
            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) ImportView.mostrarUtilizador(Tools.estadoUtilizador.PENDENTE.getCodigo(), 2);
            if (estado == Tools.estadoUtilizador.INATIVO.getCodigo()) ImportView.mostrarUtilizador(Tools.estadoUtilizador.getDefault().getCodigo(), 2);
            System.out.println("-----------------------------------------------------------------");
            System.out.println(MessageFormat.format("1. {0} todos", menuMSG));
            System.out.println(MessageFormat.format("2. {0} Cliente especifico", menuMSG));
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    boolean respAprovarTodos = AprovarClienteController.AprovarTodos(estado);
                    if (respAprovarTodos) System.out.println(MessageFormat.format("Utilizadores {0} com sucesso!", responseMSG));
                    else System.out.println(MessageFormat.format("Utilizadores não foram todos {0} com sucesso," +
                            "liste os utilizadores no menu para verificar quais não foram {0}!", responseMSG));
                    break;
                case 2:
                    FormularioAprovarClienteView.exibirMenu(estado);
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
