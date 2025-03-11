package View;

import Controller.ImportController;

public class ImportView {
    public static void mostrarLeilao() {
        System.out.println("=== LISTA DE LEILÕES ===");
        ImportController.mostrarLeilao();
    }

    public static void mostrarUtilizador() {
        System.out.println("=== LISTA DE Utilizadores ===");
        ImportController.mostrarUtilizador();
    }

    public static void mostrarLance() {
        System.out.println("=== LISTA DE LANCES ===");
        ImportController.mostrarLance();
    }
}
