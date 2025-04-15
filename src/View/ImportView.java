package View;

import Controller.ImportController;

public class ImportView {

    public static void mostrarUtilizador(int estado, int tipo){
        System.out.println("=== LISTA DE Utilizadores ===");
        ImportController.mostrarUtilizador(estado, tipo);
    }
}