package View;

import Controller.FormularioAprovarClienteController;
import Utils.Tools;

public class FormularioAprovarClienteView {
    public static void exibirMenu() {
        while (true)
        {
            System.out.println("Email do cliente:");
            String email = Tools.scanner.nextLine();
            FormularioAprovarClienteController.aprovarCliente(email);
        }
    }
}
