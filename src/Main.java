import BLL.*;
import DAL.*;
import Controller.*;
import Model.Lance;
import View.*;

public class Main {

    public static void main(String[] args) {
        // DAL
        ImportDal importDal = new ImportDal();
        ProdutoDal produtoDal = new ProdutoDal();

        // BLL
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL(importDal);
        LeilaoBLL leilaoBLL = new LeilaoBLL(importDal);
        ProdutoBLL produtoBLL = new ProdutoBLL(produtoDal, leilaoBLL);
        LoginUtilizadorBLL loginBLL = new LoginUtilizadorBLL(importDal);

        LanceBLL lanceBLL = new LanceBLL(importDal, leilaoBLL,utilizadorBLL); // sem transacaoBLL e utilizadorBLL por agora
        TransacaoBLL transacaoBLL = new TransacaoBLL(importDal,lanceBLL); // sem lanceBLL por agora

        lanceBLL.setTransacaoBLL(transacaoBLL);

        EstatisticaBLL estatisticaBLL = new EstatisticaBLL(leilaoBLL, lanceBLL);

        //CONTROLLERS
        UtilizadorController utilizadorController = new UtilizadorController(utilizadorBLL,importDal);
        LeilaoController leilaoController = new LeilaoController(leilaoBLL,lanceBLL,produtoBLL,transacaoBLL);
        ProdutoController produtoController = new ProdutoController(produtoBLL);
        EstatisticaController estatisticaController = new EstatisticaController(estatisticaBLL,leilaoBLL);
        TransacaoController transacaoController = new TransacaoController(transacaoBLL);
        LanceController lanceController = new LanceController(lanceBLL,leilaoBLL);
        LoginController loginController = new LoginController(loginBLL);

        //VIEWS
        MenuInicialView menuInicialView = new MenuInicialView();
        MenuGestorView menuGestorView = new MenuGestorView();
        MenuClienteView menuClienteView = new MenuClienteView();
        UtilizadorView utilizadorView = new UtilizadorView(utilizadorController);
        LeilaoView leilaoView = new LeilaoView(leilaoController,produtoController,lanceController);
        ProdutoView produtoView = new ProdutoView(produtoController);
        EstatisticaView estatisticaView = new EstatisticaView(estatisticaController,leilaoView);
        TransacaoView transacaoView = new TransacaoView(transacaoController);
        LoginView loginView = new LoginView(loginController);
        LanceView lanceView = new LanceView(lanceController,leilaoController,produtoController);

        menuInicialView.menuInicial(loginView, menuGestorView, menuClienteView, utilizadorView, leilaoView, produtoView, estatisticaView, transacaoView, lanceView);
    }
}
