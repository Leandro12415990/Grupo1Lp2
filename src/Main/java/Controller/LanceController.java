package Controller;

import BLL.LanceBLL;
import BLL.LeilaoBLL;
import DAL.LanceDAL;
import DAL.NegociacaoDAL;
import Model.Lance;
import Model.Leilao;
import Model.Negociacao;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Utils.Tools.scanner;

public class LanceController {
    private LanceBLL lanceBLL = new LanceBLL();

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, double novoValorLance) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();


        int idCliente = Tools.clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceEletronico(idLeilao, novoValorLance, idCliente, tipoLeilao);
        return resultado;
    }


    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();

        int idCliente = Tools.clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceDireto(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();

        int idCliente = Tools.clienteSessao.getIdCliente();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int tipoLeilao = leilao.getTipoLeilao();

        ResultadoOperacao resultado = lanceBLL.adicionarLanceCartaFechada(idLeilao, valorLance, idCliente, tipoLeilao);
        return resultado;
    }


    public List<Lance> listarLancesDoCliente() {
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
            if (leilao.getTipoLeilao() == idTipoLeilao) leiloesByTipo.add(leilao);
        }
        return leiloesByTipo;
    }

    public boolean verificarDisponibilidadeLeilao(List<Leilao> leiloes, int idLeilao) {
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == idLeilao) return true;
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

    public double obterUltimoLanceDoLeilao(int idLeilao, List<Leilao> leilaos) {
        Leilao leilaoEncontrado = null;

        for (Leilao leilao : leilaos) {
            if (leilao.getId() == idLeilao) {
                leilaoEncontrado = leilao;
                break;
            }
        }
        List<Lance> lances = lanceBLL.obterLancesPorLeilao(idLeilao);

        if (lances.isEmpty()) return leilaoEncontrado.getValorMinimo();
        return lances.get(lances.size() - 1).getValorLance();
    }

    /*public ResultadoOperacao fazerProposta(int idNegociacao, int idClienteProponente, double valorProposta) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        NegociacaoDAL negociacaoDAL = new NegociacaoDAL();
        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();

        for (Negociacao n : negociacoes) {
            if (n.getIdNegociacao() == idNegociacao && n.getEstado() == 1) {
                if (n.getIdCliente() == idClienteProponente) {
                    resultado.msgErro = "Não podes dar lance no teu próprio leilão.";
                    return resultado;
                }

                if (valorProposta >= n.getValor()) {
                    n.setEstado(4);
                    resultado.Sucesso = true;
                    resultado.msgErro = "Lance aceito. Leilão fechado.";
                } else {
                    n.setValorContraproposta(valorProposta);
                    resultado.Sucesso = true;
                    resultado.msgErro = "Proposta enviada ao dono do leilão.";
                }

                negociacaoDAL.gravarNegociacoes(negociacoes);
                return resultado;
            }
        }

        resultado.msgErro = "Leilão não encontrado ou já encerrado.";
        return resultado;
    }*/

    public Lance buscarLancePorId(int idLance) {
        for (Lance l : lanceBLL.carregarLances()) {
            if (l.getIdLance() == idLance) return l;
        }
        return null;
    }

    public ResultadoOperacao recusarLance(int idLance) {
        LanceDAL lanceDAL = new LanceDAL();
        List<Lance> lances = lanceBLL.carregarLances();
        for (Lance l : lances) {
            if (l.getIdLance() == idLance) {
                l.setEstado(3);
                lanceDAL.gravarLances(lances);
                ResultadoOperacao resultado = new ResultadoOperacao();
                resultado.Sucesso = true;
                return resultado;
            }
        }
        ResultadoOperacao erro = new ResultadoOperacao();
        erro.msgErro = "Lance não encontrado.";
        return erro;
    }

    public ResultadoOperacao atualizarValorLance(int idLance, double novoValor) {
        List<Lance> lances = lanceBLL.carregarLances();
        LanceDAL lanceDAL = new LanceDAL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        for (Lance l : lances) {
            if (l.getIdLance() == idLance) {
                l.setValorLance(novoValor);
                l.setValorContraProposta(0);
                l.setEstado(Constantes.estadosLance.PROPOSTA);
                l.setDataLance(LocalDateTime.now());
                lanceDAL.gravarLances(lances);
                resultado.Sucesso = true;
                return resultado;
            }
        }

        resultado.Sucesso = false;
        resultado.msgErro = "Lance não encontrado.";
        return resultado;
    }


    public ResultadoOperacao definirContraproposta(int idLance, double valor) {
        List<Lance> lances = lanceBLL.carregarLances();
        LanceDAL lanceDAL = new LanceDAL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        for (Lance l : lances) {
            if (l.getIdLance() == idLance) {
                l.setValorContraProposta(valor);
                l.setEstado(Constantes.estadosLance.CONTRAPROPOSTA);
                l.setDataLance(LocalDateTime.now());
                lanceDAL.gravarLances(lances);
                resultado.Sucesso = true;
                return resultado;
            }
        }

        resultado.Sucesso = false;
        resultado.msgErro = "Lance não encontrado.";
        return resultado;
    }






}

