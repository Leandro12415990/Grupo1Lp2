package Controller;

import BLL.ClassificacaoBLL;

public class ClassificacaoController {
    private final ClassificacaoBLL bll = new ClassificacaoBLL();

    public boolean jaFoiAvaliado(int idUtilizador, int idLeilao) {
        return bll.jaFoiAvaliado(idUtilizador, idLeilao);
    }

    public void registarAvaliacao(int idUtilizador, int idLeilao, int classificacao, String comentario) {
        bll.adicionarClassificacao(idLeilao, idUtilizador, classificacao, comentario);
    }
}
