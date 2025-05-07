package BLL;

import DAL.ClassificacaoDAL;
import Model.Classificacao;

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
}
