package Controller;

import BLL.ClassificacaoBLL;
import Model.Classificacao;
import Model.Leilao;

import java.util.List;

public class ClassificacaoController {
    private final ClassificacaoBLL bll = new ClassificacaoBLL();

    public boolean jaFoiAvaliado(int idUtilizador, int idLeilao) {
        return bll.jaFoiAvaliado(idUtilizador, idLeilao);
    }

    public void registarAvaliacao(int idUtilizador, int idLeilao, int classificacao, String comentario) {
        bll.adicionarClassificacao(idLeilao, idUtilizador, classificacao, comentario);
    }

    public List<Leilao> listarLeiloesAvaliadosPeloCliente(int idCliente) {
        return bll.listarLeiloesAvaliadosPeloCliente(idCliente);
    }

    public List<Leilao> listarLeiloesPorAvaliar(int idCliente) {
        ClassificacaoBLL bll = new ClassificacaoBLL();
        return bll.listarLeiloesPorAvaliar(idCliente);
    }

    public Classificacao obterClassificacaoDoCliente(int idCliente, int idLeilao) {
        ClassificacaoBLL bll = new ClassificacaoBLL();
        return bll.obterClassificacaoDoCliente(idCliente, idLeilao);
    }

}


