package View;

import Controller.FormularioAprovarClienteController;
import Utils.Tools;

public class FormularioAprovarClienteView {
    public static void exibirMenu() {
        while (true)
        {
            System.out.println("Email do cliente:");
            String email = Tools.scanner.nextLine();
            boolean resFormularioAprovarClienteController = FormularioAprovarClienteController.aprovarCliente(email);
            if (resFormularioAprovarClienteController) System.out.println("Utilizador alterado com sucesso!");
            else System.out.println("Email inválido");
        }
    }
}
