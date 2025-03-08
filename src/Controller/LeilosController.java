package Controller;

import DAL.ImportDal;
import Model.Leilao;
import BLL.LeiloesBLL;
import View.LeiloesView;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LeilosController {
    public static boolean criarLeiloes(int id, String produto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMin, double valorMax, double multiploLance, String estado) {
        if (produto == null || produto.isEmpty()
                || descricao == null || descricao.isEmpty()
                || dataInicio == null || valorMin < 0) {
            return false;
        } else if (tipoLeilao == "ELETRONICO" && multiploLance < 0) {
            return false;
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            return false;
        } else if (valorMax >= 0 && valorMax < valorMin) { // Apenas valida se valorMax foi definido
            return false;
        } else if (valorMin < 0) {
            return false;
        }
        Leilao leilao = new Leilao(id, produto, descricao, tipoLeilao, dataInicio, dataFim, valorMin,valorMax, multiploLance, estado);
        LeiloesBLL.adicionarLeilao(leilao);

        return true;
    }
    public static void listarLeiloes() {
        // A Controller chama a BLL para pegar os leilões
        List<Leilao> leiloes = LeiloesBLL.listarLeiloes();

        // A Controller agora passa os leilões para a View para exibir
        LeiloesView.exibirLeiloes(leiloes);
    }
}
