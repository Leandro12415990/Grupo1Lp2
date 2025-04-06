package View;

import Controller.FormularioAprovarClienteController;
import Utils.Tools;

public class FormularioAprovarClienteView {
    public static void exibirMenu(int estado) {
        while (true)
        {
            System.out.println("Email do cliente " + Tools.alertaCancelar());
            String email = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return;
            boolean resFormularioAprovarClienteController = FormularioAprovarClienteController.aprovarCliente(email, estado);
            if (resFormularioAprovarClienteController) System.out.println("Utilizador alterado com sucesso!");
            else System.out.println("Email inválido");
        }
    }
}
