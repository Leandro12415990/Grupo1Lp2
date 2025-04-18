package Controller;

import BLL.LanceBLL;
import BLL.LeilaoBLL;
import Model.*;

import java.util.ArrayList;
import java.util.List;

public class LanceController {
    private final LanceBLL lanceBLL;
    private final LeilaoBLL leilaoBLL;
    private ClienteSessao clienteSessao;

    public LanceController(LanceBLL lanceBLL, LeilaoBLL leilaoBLL, ClienteSessao clienteSessao) {
        this.lanceBLL = lanceBLL;
        this.leilaoBLL = leilaoBLL;
        this.clienteSessao = clienteSessao;
    }

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, int numLance, double multiploLance) {
        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int tipoLeilao = leilao.getTipoLeilao();

        double valorLance = 0;
        int valorLanceAtual = 0;

        ResultadoOperacao resultado = lanceBLL.adicionarLanceEletronico(idLeilao, valorLance, numLance, multiploLance, idCliente, valorLanceAtual, tipoLeilao);
        return resultado;
    }


    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance) {
        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        valorLance = leilao.getValorMinimo();
        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceDireto(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance) {
        int idCliente = clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceCartaFechada(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }


    public List<Lance> listarLancesDoCliente() {
        int idCliente = clienteSessao.getIdCliente();
        return lanceBLL.listarMeuLance(idCliente);
    }

    public List<Lance> obterLancesPorLeilao(int idLeilao) {
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
        return lanceBLL.obterNomeVencedor(idLance);
    }

    public int selecionarLanceVencedor(int idLeilao) {
        return lanceBLL.selecionarLanceVencedor(idLeilao);
    }

}

