package DAL;

import Model.Agente;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDate;
import java.util.List;

public class AgenteDAL {
    public List<Agente> carregarAgentes() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, 11, dados -> {
            int idAgente = Integer.parseInt(dados[0]);
            int idCliente = Integer.parseInt(dados[1]);
            int idLeilao = Integer.parseInt(dados[2]);
            LocalDate dataRegisto = Tools.parseDate(dados[6]);
            return new Agente(idAgente, idCliente, idLeilao, dataRegisto);
        });
    }

    public void gravarAgente(List<Agente> agentes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;Cliente;Leilao;";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, cabecalho, agentes, agente ->
                agente.getIdAgente() + Tools.separador() +
                        agente.getIdCliente() + Tools.separador() +
                        agente.getIdLeilao() + Tools.separador() +
                        agente.getDataRegisto()
        );
    }
}
