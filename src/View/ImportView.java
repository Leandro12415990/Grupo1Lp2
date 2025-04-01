package View;

import Controller.ImportController;

public class ImportView {

    public static void mostrarUtilizador(int estado){
        System.out.println("=== LISTA DE Utilizadores ===");
        ImportController.mostrarUtilizador(estado);
    }
}