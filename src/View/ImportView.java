package View;

import Controller.ImportController;

public class ImportView {

    public static void mostrarUtilizador(int estado, int tipo){
        ImportController importController = new ImportController();
        System.out.println("=== LISTA DE Utilizadores ===");
        importController.mostrarUtilizador(estado, tipo);
    }
}