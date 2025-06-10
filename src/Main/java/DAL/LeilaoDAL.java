package DAL;

import Model.Leilao;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;


public class LeilaoDAL {
    public List<Leilao> carregaLeiloes() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LEILAO, 10, dados -> {
            int id = Integer.parseInt(dados[0]);
            int idProduto = Integer.parseInt(dados[1]);
            String descricao = dados[2];
            int idTipoLeilao = Integer.parseInt(dados[3]);
            LocalDateTime dataInicio = Tools.parseDateTimeByDate(dados[4]);
            LocalDateTime dataFim = dados[5].isEmpty() ? null : Tools.parseDateTimeByDate(dados[5]);
            Double valorMinimo = Double.parseDouble(dados[6]);
            Double valorMaximo = dados[7].isEmpty() ? null : Double.parseDouble(dados[7]);
            Double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ? Double.parseDouble(dados[8]) : null;
            int idEstado = Integer.parseInt(dados[9]);

            return new Leilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim,
                    valorMinimo, valorMaximo, multiploLance, idEstado);
        });
    }

    public void gravarLeiloes(List<Leilao> leiloes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;ID_PRODUTO;DESCRICAO;ID_TIPO_LEILAO;DATA_INICIO;DATA_FIM;VALOR_MINIMO;VALOR_MAXIMO;MULTIPLO_LANCE;ID_ESTADO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LEILAO, cabecalho, leiloes, leilao ->
                leilao.getId() + Tools.separador() +
                        leilao.getIdProduto() + Tools.separador() +
                        leilao.getDescricao() + Tools.separador() +
                        leilao.getIdTipoLeilao() + Tools.separador() +
                        Tools.formatDateTime(leilao.getDataInicio()) + Tools.separador() +
                        (leilao.getDataFim() != null ? Tools.formatDateTime(leilao.getDataFim()) : "") + Tools.separador() +
                        leilao.getValorMinimo() + Tools.separador() +
                        (leilao.getValorMaximo() != null ? leilao.getValorMaximo() : "") + Tools.separador() +
                        (leilao.getMultiploLance() != null ? leilao.getMultiploLance() : "") + Tools.separador() +
                        leilao.getEstado()
        );
    }

}
