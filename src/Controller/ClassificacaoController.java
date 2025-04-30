package Controller;

import BLL.ClassificacaoBLL;
import Model.Classificacao;
import View.ClassificacaoView;

public class ClassificacaoController {

    private final ClassificacaoView view;
    private final ClassificacaoBLL bll;

    public ClassificacaoController(ClassificacaoView view, ClassificacaoBLL bll) {
        this.view = view;
        this.bll = bll;
    }

    public void processarClassificacao(int idLeilao, int idUtilizador) {
        if (bll.classificacaoJaExiste(idLeilao, idUtilizador)) {
            System.out.println("Já classificou este leilão.");
            return;
        }

        int nota = view.pedirClassificacao();
        String comentario = view.pedirComentario();

        Classificacao c = new Classificacao(idLeilao, idUtilizador, nota, comentario);
        bll.registarClassificacao(c);

        System.out.println("Classificação registada com sucesso!");
    }
}
