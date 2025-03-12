package BLL;

import DAL.ImportDal;
import Model.Leilao;
import Model.Utilizador;

import java.util.List;

public class ImportBll {

    public static List<Leilao> obterTodosLeiloes() {
        return ImportDal.carregarLeilao();
    }

    public static void listarLeilao() {
        List<Leilao> leiloes = obterTodosLeiloes();
        for (Leilao leilao : leiloes) {
            System.out.println("ID: " + leilao.getId() + " - Produto: " + leilao.getNomeProduto());
        }
    }

    public static List<Utilizador> obterTodosUtilizadores() {
        return ImportDal.carregarUtilizador();
    }

    public static void listarUtilizador() {
        List<Utilizador> utilizadors = obterTodosUtilizadores();
        for (Utilizador utilizador : utilizadors) {
            System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador());
        }
    }

    // Método para gravar os leilões no CSV
    public static void gravarLeiloes(List<Leilao> leiloes) {
        ImportDal.gravarLeilao(leiloes);
    }

    // Método para gravar os utilizadores no CSV
    public static void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportDal.gravarUtilizador(utilizadores);
    }
}
