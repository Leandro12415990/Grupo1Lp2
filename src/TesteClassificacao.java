import BLL.ClassificacaoBLL;
import Controller.ClassificacaoController;
import DAL.ClassificacaoDAL;
import View.ClassificacaoView;

public class TesteClassificacao {
    public static void main(String[] args) {
        // Instanciar as camadas
        ClassificacaoView view = new ClassificacaoView();
        ClassificacaoDAL dal = new ClassificacaoDAL();
        ClassificacaoBLL bll = new ClassificacaoBLL(dal);
        ClassificacaoController controller = new ClassificacaoController(view, bll);

        // Testar com um ID de leilão e de utilizador fictícios
        int idLeilaoTeste = 1;
        int idUtilizadorTeste = 10;

        // Chamar o processo completo de classificação
        controller.processarClassificacao(idLeilaoTeste, idUtilizadorTeste);

        System.out.println("Classificação registada com sucesso!");
    }
}
