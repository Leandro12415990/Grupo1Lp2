package BLL;

import DAL.ClassificacaoDAL;
import Model.Classificacao;

public class ClassificacaoBLL {

    private final ClassificacaoDAL dal;

    public ClassificacaoBLL(ClassificacaoDAL dal) {
        this.dal = dal;
    }

    public void registarClassificacao(Classificacao c) {
        dal.adicionarClassificacao(c);
    }
}
