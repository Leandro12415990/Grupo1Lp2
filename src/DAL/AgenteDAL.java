package DAL;

import Model.Agente;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDate;
import java.util.List;

public class AgenteDAL {
    public List<Utilizador> carregarAgente() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, 11, dados -> {
            int id = Integer.parseInt(dados[0]);
            String nomeUtilizador = dados[1];
            String email = dados[2];
            LocalDate dataNascimento = Tools.parseDate(dados[3]);
            String morada = dados[4];
            String password = dados[5];
            LocalDate dataRegisto = Tools.parseDate(dados[6]);
            LocalDate ultimoLogin = dados[7].isEmpty() ? null : Tools.parseDate(dados[7]);
            int tipoUtilizador = Integer.parseInt(dados[8]);
            int estado = Integer.parseInt(dados[9]);
            Double saldo = Double.parseDouble(dados[10]);
            return new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
        });
    }

    public void gravarAgente(List<Agente> agentes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;Cliente;Leilao;";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, cabecalho, agentes, agente ->
                agente.getIdAgente() + Tools.separador() +
                        agente.getIdCliente() + Tools.separador() +
                        agente.getIdLeilao()
        );
    }
}
