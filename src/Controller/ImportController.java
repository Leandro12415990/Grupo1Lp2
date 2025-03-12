package Controller;

import BLL.ImportBll;
import Model.Leilao;
import Model.Utilizador;

import java.util.List;

public class ImportController {

    // Método para mostrar todos os leilões
    public static void mostrarLeilao() {
        ImportBll.listarLeilao();
    }

    // Método para mostrar todos os utilizadores
    public static void mostrarUtilizador() {
        ImportBll.listarUtilizador();
    }

    // Método para gravar os leilões
    public static void gravarLeiloes(List<Leilao> leiloes) {
        ImportBll.gravarLeiloes(leiloes);
    }

    // Método para gravar os utilizadores
    public static void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportBll.gravarUtilizadores(utilizadores);
    }
}
