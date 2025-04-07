package BLL;

import DAL.ImportDal;
import Model.Lance;
import Model.Leilao;
import Model.ResultadoOperacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanceBLL {
    private static List<Lance> lances = new ArrayList<>();

    public static List<Lance> carregarLance() {
        lances = ImportDal.carregarLance();
        return lances;
    }

    public static ResultadoOperacao adicionarLanceDireto(int idLance, int idLeilao, double valorLance, int idCliente) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;

        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        ImportDal.gravarLance(lances);

        if (valorLance == leilao.getValorMinimo()) {
            fimLeilao(idLeilao, dataLance);
        }

        resultado.Sucesso = true;
        resultado.Objeto = resultado;
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceCartaFechada(int idLance, int idLeilao, double valorLance, int idCliente) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;


        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        ImportDal.gravarLance(lances);

        resultado.Sucesso = true;
        resultado.Objeto = resultado;
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceEletronico(int idLance, int idLeilao, double valorLance, int numLance, double multiploLance, int idCliente, int valorLanceAtual) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;

        valorLance = (valorLance + (multiploLance * numLance));

        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);

        ImportDal.gravarLance(lances);

        resultado.Sucesso = true;
        resultado.Objeto = resultado;
        return resultado;

    }

    public static int verUltimoId(List<Lance> lances) {
        int ultimoId = 0;
        for (Lance lance : lances) {
            if (lance.getIdLance() > ultimoId) {
                ultimoId = lance.getIdLance();
            }
        }
        return ultimoId;
    }

    public static List<Lance> listarMeuLance(int IdCliente) {
        List<Lance> lances = carregarLance();

        if (lances == null) {
            return new ArrayList<>();
        }

        return lances.stream()
                .filter(lance -> lance.getIdCliente() == IdCliente)
                .collect(Collectors.toList());
    }

    public static List<Lance> obterLancesPorLeilao(int idLeilao) {
        List<Lance> lances = carregarLance();

        if (lances == null) {
            return new ArrayList<>();
        }

        return lances.stream()
                .filter(lance -> lance.getIdLeilao() == idLeilao)
                .collect(Collectors.toList());
    }

    public static void fimLeilao(int idLeilao, LocalDateTime dataFim){

        LeilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
    }

}
