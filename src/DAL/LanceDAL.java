package DAL;

import Model.Lance;
import Model.Leilao;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;



public class LanceDAL {
    public List<Lance> carregarLances() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LANCE, 7, dados -> {
            int idLance = Integer.parseInt(dados[0]);
            int idLeilao = Integer.parseInt(dados[1]);
            int idCliente = Integer.parseInt(dados[2]);
            double valorLance = Double.parseDouble(dados[3]);
            int numLance = dados[4].isEmpty() ? 0 : Integer.parseInt(dados[4]);
            int pontosUtilizados = dados[5].isEmpty() ? 0 : Integer.parseInt(dados[5]);
            LocalDateTime dataLance = Tools.parseDateTimeByDate(dados[6]);
            return new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);});
    }

    public void gravarLances(List<Lance> lances) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LANCE, cabecalho, lances, lance ->
                lance.getIdLance() + Tools.separador() +
                        lance.getIdLeilao() + Tools.separador() +
                        lance.getIdCliente() + Tools.separador() +
                        lance.getValorLance() + Tools.separador() +
                        lance.getNumLance() + Tools.separador() +
                        (lance.getPontosUtilizados() + Tools.separador() +
                                Tools.formatDateTime(lance.getDataLance()))
        );
    }
}
