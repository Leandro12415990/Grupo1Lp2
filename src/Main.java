import BLL.*;
import DAL.*;
import Controller.*;
import Model.ClienteSessao;
import Utils.Tools;
import View.*;

public class Main {

    public static void main(String[] args) {
        // 1. Criação das instâncias DAL
        ImportDAL importDal = new ImportDAL();
        ProdutoDAL produtoDal = new ProdutoDAL(importDal);
        LeilaoDAL leilaoDal = new LeilaoDAL(importDal);
        LanceDAL lanceDal = new LanceDAL(importDal);
        TransacaoDAL transacaoDAL = new TransacaoDAL(importDal);
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL(importDal);

        ClienteSessao clienteSessao = new ClienteSessao();

        Tools.utilizadores = utilizadorDAL.carregarUtilizadores();

        // 4. Criação das instâncias BLL
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL(utilizadorDAL);
        LeilaoBLL leilaoBLL = new LeilaoBLL(leilaoDal);
        ProdutoBLL produtoBLL = new ProdutoBLL(produtoDal, leilaoBLL);
        LoginUtilizadorBLL loginBLL = new LoginUtilizadorBLL(utilizadorDAL);
        LanceBLL lanceBLL = new LanceBLL(lanceDal, utilizadorDAL, leilaoBLL, utilizadorBLL);
        TransacaoBLL transacaoBLL = new TransacaoBLL(transacaoDAL, utilizadorDAL, lanceBLL);
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL(utilizadorDAL, leilaoBLL, lanceBLL);

        // 5. Definir a transaçãoBLL no LanceBLL
        lanceBLL.setTransacaoBLL(transacaoBLL);

        // 6. Criação dos controllers
        UtilizadorController utilizadorController = new UtilizadorController(utilizadorBLL);
        LeilaoController leilaoController = new LeilaoController(leilaoBLL, lanceBLL, produtoBLL, transacaoBLL);
        ProdutoController produtoController = new ProdutoController(produtoBLL);
        EstatisticaController estatisticaController = new EstatisticaController(estatisticaBLL, leilaoBLL);
        TransacaoController transacaoController = new TransacaoController(transacaoBLL);
        LanceController lanceController = new LanceController(lanceBLL, leilaoBLL, clienteSessao);
        LoginController loginController = new LoginController(loginBLL, clienteSessao);

        // 7. Criação das views
        MenuInicialView menuInicialView = new MenuInicialView();
        MenuGestorView menuGestorView = new MenuGestorView();
        MenuClienteView menuClienteView = new MenuClienteView();
        UtilizadorView utilizadorView = new UtilizadorView(utilizadorController);
        LeilaoView leilaoView = new LeilaoView(leilaoController, produtoController, lanceController);
        ProdutoView produtoView = new ProdutoView(produtoController);
        EstatisticaView estatisticaView = new EstatisticaView(estatisticaController, leilaoView, utilizadorView);
        TransacaoView transacaoView = new TransacaoView(transacaoController, clienteSessao);
        LoginView loginView = new LoginView(loginController);
        LanceView lanceView = new LanceView(lanceController, leilaoController, produtoController);

        // 8. Chamada para o menu inicial (com a passagem das views)
        menuInicialView.menuInicial(loginView, menuGestorView, menuClienteView, utilizadorView, leilaoView, produtoView, estatisticaView, transacaoView, lanceView, clienteSessao);
    }
}
