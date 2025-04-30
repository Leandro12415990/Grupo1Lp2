import BLL.ClassificacaoBLL;
import Controller.ClassificacaoController;
import DAL.ClassificacaoDAL;
import View.ClassificacaoView;

public class TesteClassificacao {

    public static void main(String[] args) {
        int idLeilaoTeste = 102;
        int idUtilizadorTeste = 5;

        ClassificacaoView view = new ClassificacaoView();
        ClassificacaoDAL dal = new ClassificacaoDAL();
        ClassificacaoBLL bll = new ClassificacaoBLL(dal);
        ClassificacaoController controller = new ClassificacaoController(view, bll);

        controller.processarClassificacao(idLeilaoTeste, idUtilizadorTeste);
    }
}
