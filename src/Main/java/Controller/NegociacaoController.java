package Controller;

import BLL.UtilizadorBLL;
import DAL.NegociacaoDAL;
import Model.Leilao;
import Model.Negociacao;
import Model.ResultadoOperacao;
import Model.Utilizador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NegociacaoController {

    private NegociacaoDAL negociacaoDAL = new NegociacaoDAL();

    public ResultadoOperacao criarNegociacao(int idCliente, String nome, String descricao, double valor) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        if (nome == null || nome.trim().isEmpty()) {
            resultado.msgErro = "O nome da negociação é obrigatório.";
            return resultado;
        }

        if (valor <= 0) {
            resultado.msgErro = "O valor da negociação deve ser maior que zero.";
            return resultado;
        }

        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();
        int novoId = negociacoes.stream().mapToInt(Negociacao::getIdNegociacao).max().orElse(0) + 1;

        Negociacao novaNegociacao = new Negociacao(novoId, idCliente, nome, descricao, valor, LocalDateTime.now(), 0.0, 1);
        negociacoes.add(novaNegociacao);
        negociacaoDAL.gravarNegociacoes(negociacoes);

        resultado.Sucesso = true;
        resultado.Objeto = novaNegociacao;
        return resultado;
    }

    public List<Negociacao> listarNegociacoesPorCliente(int idCliente) {
        List<Negociacao> todasNegociacoes = negociacaoDAL.carregarNegociacoes();
        List<Negociacao> negociacoesCliente = new ArrayList<>();

        for (Negociacao negociacao : todasNegociacoes) {
            if (negociacao.getIdCliente() == idCliente) {
                negociacoesCliente.add(negociacao);
            }
        }

        return negociacoesCliente;
    }

    public ResultadoOperacao editarNegociacao(int idNegociacao, int idCliente, String novoNome, String novaDescricao, double novoValor) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();
        boolean encontrado = false;

        for (Negociacao negociacao : negociacoes) {
            if (negociacao.getIdNegociacao() == idNegociacao && negociacao.getIdCliente() == idCliente) {
                if (negociacao.getEstado() == 0) {
                    resultado.msgErro = "Não é possível editar um leilão fechado.";
                    return resultado;
                }

                if (novoNome != null && !novoNome.trim().isEmpty()) {
                    negociacao.setNome(novoNome.trim());
                }

                if (novaDescricao != null) {
                    negociacao.setDescricao(novaDescricao.trim());
                }

                if (novoValor > 0) {
                    negociacao.setValor(novoValor);
                }

                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            negociacaoDAL.gravarNegociacoes(negociacoes);
            resultado.Sucesso = true;
        } else {
            resultado.msgErro = "Leilão não encontrado.";
        }

        return resultado;
    }

    public ResultadoOperacao fecharNegociacao(int idNegociacao, int idCliente) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();
        boolean encontrado = false;

        for (Negociacao negociacao : negociacoes) {
            if (negociacao.getIdNegociacao() == idNegociacao && negociacao.getIdCliente() == idCliente) {
                if (negociacao.getEstado() == 0) {
                    resultado.msgErro = "O leilão já está fechado.";
                    return resultado;
                }

                negociacao.setEstado(4);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            negociacaoDAL.gravarNegociacoes(negociacoes);
            resultado.Sucesso = true;
        } else {
            resultado.msgErro = "Leilão não encontrado.";
        }

        return resultado;
    }

    public List<Negociacao> listarLeiloesAtivosDeOutrosClientes(int idClienteAtual) {
        List<Negociacao> todas = negociacaoDAL.carregarNegociacoes();
        List<Negociacao> resultado = new ArrayList<>();

        for (Negociacao n : todas) {
            if (n.getEstado() == 1 && n.getIdCliente() != idClienteAtual) {
                resultado.add(n);
            }
        }

        return resultado;
    }

    public Negociacao buscarNegociacaoPorId(int id) {

        for (Negociacao n : negociacaoDAL.carregarNegociacoes()) {
            if (n.getIdNegociacao() == id) return n;
        }
        return null;
    }

    public ResultadoOperacao fecharNegociacaoComLanceAceito(int idNegociacao, double valorAceito, int idComprador) {
        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();
        for (Negociacao n : negociacoes) {
            if (n.getIdNegociacao() == idNegociacao) {
                n.setValor(valorAceito);
                n.setIdCliente(idComprador);
                n.setEstado(4);
                negociacaoDAL.gravarNegociacoes(negociacoes);
                ResultadoOperacao r = new ResultadoOperacao();
                r.Sucesso = true;
                return r;
            }
        }
        ResultadoOperacao r = new ResultadoOperacao();
        r.msgErro = "Negociação não encontrada.";
        return r;
    }

    public String obterNomeClientePorId(int idCliente) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        List<Utilizador> cliente = utilizadorBLL.carregarUtilizadores();
        for (Utilizador c : cliente) {
            if (c.getId() == idCliente) {
                return c.getNomeUtilizador();
            }
        }
        return "Desconhecido";
    }
}
