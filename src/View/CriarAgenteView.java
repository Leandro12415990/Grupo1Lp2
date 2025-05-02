package View;

import Controller.CriarAgenteController;
import Utils.Tools;

public class CriarAgenteView {
    public boolean menu() {
        CriarAgenteController criarAgenteController = new CriarAgenteController();
        while (true) {
            System.out.print("Leilão: " + Tools.alertaCancelar());
            String leilaoID = Tools.scanner.nextLine();
            if (Tools.verificarSaida(leilaoID)) return false;

            criarAgenteController.criarAgente(leilaoID);
        }
    }
}
