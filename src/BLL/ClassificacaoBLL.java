package BLL;

import DAL.ClassificacaoDAL;
import Model.Classificacao;

public class ClassificacaoBLL {

    private final ClassificacaoDAL dal;

    public ClassificacaoBLL(ClassificacaoDAL dal) {
        this.dal = dal;
    }

    public void registarClassificacao(Classificacao classificacao) {
        dal.adicionarClassificacao(classificacao);
    }

    public boolean classificacaoJaExiste(int idLeilao, int idUtilizador) {
        return dal.carregarClassificacoes().stream()
                .anyMatch(c -> c.getIdLeilao() == idLeilao && c.getIdCLiente() == idUtilizador);
    }
}
