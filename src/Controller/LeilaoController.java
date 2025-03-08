package Controller;

import Model.Leilao;
import BLL.LeilaoBLL;
import View.LeilaoView;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class LeilaoController {

    public static boolean criarLeiloes(int id, String produto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMin, double valorMax, double multiploLance, String estado) {
        if (produto == null || produto.isEmpty()
                || descricao == null || descricao.isEmpty()
                || dataInicio == null || valorMin < 0) {
            return false;
        } else if (Objects.equals(tipoLeilao, "ELETRONICO") && multiploLance < 0) {
            return false;
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            return false;
        } else if (valorMax >= 0 && valorMax < valorMin) { // Apenas valida se valorMax foi definido
            return false;
        }
        Leilao leilao = new Leilao(id, produto, descricao, tipoLeilao, dataInicio, dataFim, valorMin,valorMax, multiploLance, estado);
        LeilaoBLL.adicionarLeilao(leilao);

        return true;
    }

    public static void listarLeiloes() {
        // A Controller chama a BLL para pegar os leilões
        List<Leilao> leiloes = LeilaoBLL.listarLeiloes();

        // A Controller agora passa os leilões para a View para exibir
        LeilaoView.exibirLeiloes(leiloes);
    }

    public static Leilao procurarLeilaoPorId(int Id) {
        if (Id > 0) {
            return LeilaoBLL.procurarLeilaoPorId(Id);
        }
        return null;
    }
}
