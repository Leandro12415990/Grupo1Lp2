package Model;

import java.util.List;

public class ResultadoImportacao {
    public final List<Utilizador> utilizadoresImportados;
    public final int totalImportados;
    public final int totalExistentes;
    public final List<String> erros;

    public ResultadoImportacao(List<Utilizador> utilizadoresImportados, int totalImportados, int totalExistentes, List<String> erros) {
        this.utilizadoresImportados = utilizadoresImportados;
        this.totalImportados = totalImportados;
        this.totalExistentes = totalExistentes;
        this.erros = erros;
    }
}
