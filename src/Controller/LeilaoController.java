package Controller;

import BLL.LanceBLL;
import BLL.LeilaoBLL;
import BLL.ProdutoBLL;
import BLL.TransacaoBLL;
import Model.Leilao;
import Model.Produto;
import Model.ResultadoOperacao;
import Utils.Constantes;
import View.LeilaoView;

import java.time.LocalDateTime;
import java.util.List;

public class LeilaoController {

    private final LeilaoBLL leilaoBLL;
    private final LanceBLL lanceBLL;
    private final ProdutoBLL produtoBLL;
    private final TransacaoBLL transacaoBLL;

    public LeilaoController(LeilaoBLL leilaoBLL, LanceBLL lanceBLL, ProdutoBLL produtoBLL, TransacaoBLL transacaoBLL) {
        this.leilaoBLL = leilaoBLL;
        this.lanceBLL = lanceBLL;
        this.produtoBLL = produtoBLL;
        this.transacaoBLL = transacaoBLL;
    }

    public ResultadoOperacao criarLeiloes(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, Double valorMax, Double multiploLance, int idEstado) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição não pode ser nula.";
        } else if (dataInicio == null) {
            resultado.msgErro = "A data de inicio não pode ser nula.";
        } else if (valorMin < 0) {
            resultado.msgErro = "O valor minimo não pode ser negativo.";
        } else if (valorMax != 0.0 && valorMax >= 0 && valorMax < valorMin) {
            resultado.msgErro = "O valor máximo deve ser maior do que o valor minimo.";
        } else if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO && multiploLance < 0) {
            resultado.msgErro = "O múltiplo de lance deve ser positivo.";
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            resultado.msgErro = "A data de fim deve ser superior à data de inicio.";
        } else {
            Leilao leilao = new Leilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
            leilaoBLL.adicionarLeilao(leilao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public List<Leilao> listarLeiloes(boolean apenasDisponiveis) {
        return leilaoBLL.listarLeiloes(apenasDisponiveis);
    }

    public Leilao procurarLeilaoPorId(int Id) {
        if (Id > 0) {
            return leilaoBLL.procurarLeilaoPorId(Id);
        }
        return null;
    }

    public boolean eliminarLeilao(int Id) {
        if (Id > 0) {
            Leilao leilao = procurarLeilaoPorId(Id);
            if (leilao != null) {
                leilaoBLL.eliminarLeilao(leilao);
                return true;
            }
        }
        return false;
    }

    public boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        return leilaoBLL.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
    }

    public ResultadoOperacao verificarDisponibilidadeProduto(int idProduto) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        Produto produto = produtoBLL.procurarProduto(idProduto);
        if (produto == null) {
            resultado.msgErro = "O Produto que introduziu não existe no sistema.";
        } else {
            boolean isAvailable = produtoBLL.verificarDisponibilidadeProduto(idProduto);
            if (isAvailable) {
                resultado.Objeto = resultado;
                resultado.Sucesso = true;
            } else {
                resultado.msgErro = "O Produto selecionado não se encontra disponível.";
            }
        }
        return resultado;
    }

    public ResultadoOperacao verificarValorMax(double valorMin, double valorMax) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (valorMax < valorMin) {
            resultado.msgErro = "O valor máximo não pode ser inferior ao valor minimo.\n";
        } else {
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public int determinarEstadoLeilaoByDatas(LocalDateTime dataInicio, LocalDateTime dataFim, int idEstado) {
        return leilaoBLL.determinarEstadoLeilaoByDatas(dataInicio, dataFim, idEstado);
    }

    public boolean fecharLeilao(int idLeilao, LocalDateTime dataFim) {
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        if (leilao == null) return false;

        leilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
        int idLanceVencedor = lanceBLL.selecionarLanceVencedor(idLeilao);
        transacaoBLL.devolverSaldo(idLeilao, idLanceVencedor);

        return true;
    }


}

