package Controller;

import java.time.LocalDate;

public class LeilosController {
    private boolean criarLeiloes(String produto, String descricao, int tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMax, double valorMin, double multiploLance) {
        if (produto == null || produto.isEmpty()
                || descricao == null || descricao.isEmpty()
                || dataInicio == null || valorMin < 0) {
            return false;
        } else if (tipoLeilao == 1 && multiploLance < 0) {
            return false;
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            return false;
        } else if (valorMax >= 0 && valorMax < valorMin) { // Apenas valida se valorMax foi definido
            return false;
        }
        return true;
    }

}
