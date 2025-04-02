package Controller;

import BLL.LanceBLL;
import BLL.LeilaoBLL;
import Model.ClienteSessao;
import Model.Lance;
import Model.Leilao;
import Model.ResultadoOperacao;

import java.util.ArrayList;
import java.util.List;

public class LanceController {

    public static ResultadoOperacao adicionarLanceEletronico(int idLeilao, int numLance, double multiploLance) {
        int idCliente = ClienteSessao.getIdCliente();
        List<Leilao> leiloesAtivos = LeilaoBLL.listarLeiloes(true);

        double valorLance = 0;
        int valorLanceAtual = 0;

        ResultadoOperacao resultado = LanceBLL.adicionarLanceEletronico(0, idLeilao, valorLance, numLance, multiploLance, idCliente, valorLanceAtual);
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance) {
        int idCliente = ClienteSessao.getIdCliente();
        List<Leilao> leiloesAtivos = LeilaoBLL.listarLeiloes(true);

        ResultadoOperacao resultado = LanceBLL.adicionarLanceDireto(0, idLeilao, valorLance, idCliente);
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance) {
        int idCliente = ClienteSessao.getIdCliente();
        List<Leilao> leiloesAtivos = LeilaoBLL.listarLeiloes(true);

        ResultadoOperacao resultado = LanceBLL.adicionarLanceCartaFechada(0, idLeilao, valorLance, idCliente);
        return resultado;
    }

    public static List<Lance> listarLancesDoCliente() {
        int idCliente = ClienteSessao.getIdCliente();
        return LanceBLL.listarMeuLance(idCliente);
    }

    public static List<Lance> obterLancesPorLeilao(int idLeilao) {return LanceBLL.obterLancesPorLeilao(idLeilao);
    }

    public static List<Leilao> listarLeiloesByTipo(List<Leilao> leiloes, int idTipoLeilao) {
        List<Leilao> leiloesByTipo = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            if (leilao.getTipoLeilao() == idTipoLeilao) {
                leiloesByTipo.add(leilao);
            }
        }
        return leiloesByTipo;
    }

    public static boolean verificarDisponibilidadeLeilao(List<Leilao> leiloes, int idLeilao) {

        for (Leilao leilao : leiloes) {
            if (leilao.getId() == idLeilao) {
                return true;
            }
        }
        return false;
    }
}
