package Controller;

import BLL.LanceBLL;
import BLL.LeilaoBLL;
import Model.ClienteSessao;
import Model.Lance;
import Model.Leilao;
import Model.ResultadoOperacao;
import Utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class LanceController {

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, double novoValorLance) {
        LanceBLL lanceBLL = new LanceBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        ClienteSessao clienteSessao = new ClienteSessao();

        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceEletronico(idLeilao, novoValorLance, idCliente, tipoLeilao);

        return resultado;
    }


    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance) {
        LanceBLL lanceBLL = new LanceBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        ClienteSessao clienteSessao = new ClienteSessao();

        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        valorLance = leilao.getValorMinimo();
        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceDireto(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance) {
        LanceBLL lanceBLL = new LanceBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        ClienteSessao clienteSessao = new ClienteSessao();

        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceCartaFechada(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }


    public List<Lance> listarLancesDoCliente() {
        LanceBLL lanceBLL = new LanceBLL();

        lanceBLL.carregarLances();

        int idCliente = Tools.clienteSessao.getIdCliente();
        return lanceBLL.listarMeuLance(idCliente);
    }


    public List<Lance> obterLancesPorLeilao(int idLeilao) {
        LanceBLL lanceBLL = new LanceBLL();
        return lanceBLL.obterLancesPorLeilao(idLeilao);
    }

    public List<Leilao> listarLeiloesByTipo(List<Leilao> leiloes, int idTipoLeilao) {
        List<Leilao> leiloesByTipo = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            if (leilao.getTipoLeilao() == idTipoLeilao) {
                leiloesByTipo.add(leilao);
            }
        }
        return leiloesByTipo;
    }

    public boolean verificarDisponibilidadeLeilao(List<Leilao> leiloes, int idLeilao) {
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == idLeilao) {
                return true;
            }
        }
        return false;
    }

    public String obterNomeVencedor(int idLance) {
        LanceBLL lanceBLL = new LanceBLL();
        return lanceBLL.obterVencedor(idLance).getNomeUtilizador();
    }

    public int selecionarLanceVencedor(int idLeilao) {
        LanceBLL lanceBLL = new LanceBLL();
        return lanceBLL.selecionarLanceVencedor(idLeilao);
    }

}

