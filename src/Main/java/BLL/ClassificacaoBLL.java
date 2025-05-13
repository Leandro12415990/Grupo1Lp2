package BLL;

import Controller.LeilaoController;
import DAL.ClassificacaoDAL;
import Model.Classificacao;
import Model.Leilao;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassificacaoBLL {
    private final ClassificacaoDAL dal = new ClassificacaoDAL();

    public boolean jaFoiAvaliado(int idUtilizador, int idLeilao) {
        List<Classificacao> lista = dal.carregarClassificacoes();

        for (Classificacao c : lista) {
            if (c.getIdUtilizador() == idUtilizador && c.getIdLeilao() == idLeilao) {
                return true;
            }
        }

        return false;
    }

    public void adicionarClassificacao(int idLeilao, int idUtilizador, int classificacao, String comentario) {
        Classificacao nova = new Classificacao(idLeilao, idUtilizador, classificacao, comentario);
        dal.adicionarClassificacao(nova);
    }

    public List<Leilao> listarLeiloesAvaliadosPeloCliente(int idCliente) {
        List<Classificacao> todas = getClassificacoesPorCliente(idCliente);
        List<Leilao> leiloesAvaliados = new ArrayList<>();

        LeilaoBLL leilaoBLL = new LeilaoBLL();

        for (Classificacao c : todas) {
            Leilao leilao = leilaoBLL.procurarLeilaoPorId(c.getIdLeilao());
            if (leilao != null) {
                leiloesAvaliados.add(leilao);
            }
        }

        return leiloesAvaliados;
    }

    public List<Classificacao> getClassificacoesPorCliente(int idCliente) {
        List<Classificacao> todas = dal.carregarClassificacoes();
        List<Classificacao> resultado = new ArrayList<>();

        for (Classificacao c : todas) {
            if (c.getIdUtilizador() == idCliente) {
                resultado.add(c);
            }
        }

        return resultado;
    }

    public List<Leilao> listarLeiloesPorAvaliar(int idCliente) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        List<Leilao> terminados = leilaoBLL.listarLeiloesTerminadosComLancesDoCliente(idCliente);

        List<Classificacao> avaliadas = getClassificacoesPorCliente(idCliente);
        List<Integer> idsAvaliados = new ArrayList<>();
        for (Classificacao c : avaliadas) {
            idsAvaliados.add(c.getIdLeilao());
        }

        List<Leilao> porAvaliar = new ArrayList<>();
        for (Leilao l : terminados) {
            if (!idsAvaliados.contains(l.getId())) {
                porAvaliar.add(l);
            }
        }

        return porAvaliar;
    }

    public Classificacao obterClassificacaoDoCliente(int idCliente, int idLeilao) {
        List<Classificacao> todas = dal.carregarClassificacoes();

        for (Classificacao c : todas) {
            if (c.getIdUtilizador() == idCliente && c.getIdLeilao() == idLeilao) {
                return c;
            }
        }

        return null;
    }









}
