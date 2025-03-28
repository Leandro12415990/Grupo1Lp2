package Controller;

import Model.Leilao;
import Model.ResultadoOperacao;
import BLL.LeilaoBLL;
import View.LeilaoView;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class LeilaoController {

    public static ResultadoOperacao criarLeiloes(int id, int idProduto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMin, double valorMax, double multiploLance, String estado) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição não pode ser nula.";
        } else if (dataInicio == null) {
            resultado.msgErro = "A data de inicio não pode ser nula.";
        } else if (valorMin <0) {
            resultado.msgErro = "O valor minimo não pode ser negativo.";
        } else if (valorMax >= 0 && valorMax < valorMin) {
            resultado.msgErro = "O valor máximo deve ser maior do que o valor minimo.";
        } else if (Objects.equals(tipoLeilao, "ELETRONICO") && multiploLance < 0) {
            resultado.msgErro = "O múltiplo de lance deve ser positivo.";
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            resultado.msgErro = "A data de fim deve ser superior à data de inicio.";
        }  else {
            Leilao leilao = new Leilao(id, idProduto, descricao, tipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, estado);
            LeilaoBLL.adicionarLeilao(leilao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
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

    public static boolean editarLeilao(int id, int idProduto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMin, double valorMax, double multiploLance, String estado) {

        Leilao leilao = procurarLeilaoPorId(id);
        if (leilao != null) {
            if (idProduto == -1) {
                idProduto = leilao.getIdProduto();
            }
            if (descricao.isEmpty()) {
                descricao = leilao.getDescricao();
            }
            if (tipoLeilao.isEmpty()) {
                tipoLeilao = leilao.getTipoLeilao();
            }
            if (valorMin == -1) {
                valorMin = leilao.getValorMinimo();
            }
            if (valorMax == -1) {
                valorMax = leilao.getValorMaximo();
            }
            if (multiploLance == -1) {
                multiploLance = leilao.getMultiploLance();
            }
            return LeilaoBLL.editarLeilao(id, idProduto, descricao, tipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, estado);
        }
        return false;
    }
}

