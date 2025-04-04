package Controller;

import BLL.ProdutoBLL;
import Model.Leilao;
import Model.Produto;
import Model.ResultadoOperacao;
import BLL.LeilaoBLL;
import Utils.Constantes;
import View.LeilaoView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LeilaoController {

    public static ResultadoOperacao criarLeiloes(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, Double valorMax, Double multiploLance, int idEstado) {
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
            LeilaoBLL.adicionarLeilao(leilao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static void listarLeiloes(boolean apenasDisponiveis) {
        List<Leilao> leiloes = LeilaoBLL.listarLeiloes(apenasDisponiveis);

        LeilaoView.exibirLeiloes(leiloes);
    }

    public static Leilao procurarLeilaoPorId(int Id) {
        if (Id > 0) {
            return LeilaoBLL.procurarLeilaoPorId(Id);
        }
        return null;
    }

    public static boolean eliminarLeilao(int Id) {
        if (Id > 0) {
            Leilao leilao = procurarLeilaoPorId(Id);
            if (leilao != null) {
                LeilaoBLL.eliminarLeilao(leilao);
                return true;
            }
        }
        return false;
    }

    public static boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        return LeilaoBLL.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
    }

    public static ResultadoOperacao verificarDisponibilidadeProduto(int idProduto) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        Produto produto = ProdutoBLL.procurarProduto(idProduto);
        if (produto == null) {
            resultado.msgErro = "O Produto que introduziu não existe no sistema.";
        } else {
            boolean isAvailable = ProdutoBLL.verificarDisponibilidadeProduto(idProduto);
            if (isAvailable) {
                resultado.Objeto = resultado;
                resultado.Sucesso = true;
            } else {
                resultado.msgErro = "O Produto selecionado não se encontra disponível.";
            }
        }
        return resultado;
    }

    public static ResultadoOperacao verificarValorMax(double valorMin, double valorMax) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (valorMax < valorMin) {
            resultado.msgErro = "O valor máximo não pode ser inferior ao valor minimo.\n";
        } else {
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static int determinarEstadoLeilaoByDatas(LocalDateTime dataInicio, LocalDateTime dataFim, int idEstado) {
        return LeilaoBLL.determinarEstadoLeilaoByDatas(dataInicio, dataFim, idEstado);
    }

    public static boolean fecharLeilao(int idLeilao, int idEstadoLeilao, LocalDate dataFim) {
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);
        if (leilao == null) return false;

        LeilaoBLL.alterarEstadoLeilao(idLeilao, idEstadoLeilao);
        LeilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
        return true;
    }


}

