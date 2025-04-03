package BLL;

import DAL.ImportDal;
import Model.Carteira;
import Model.Utilizador;
import Utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class CarteiraBLL {
    private static List<Carteira> carteiraList = new ArrayList<>();
    private static List<Utilizador> utilizadores = ImportDal.carregarUtilizador();

    public static List<Carteira> carregarCarteira() {
        carteiraList = ImportDal.carregarCarteira();
        return carteiraList;
    }

    public static void criarDeposito(Carteira carteira) {
        carregarCarteira();
        carteira.setIdDeposito(verificarUltimoIdCarteira(carteiraList) + 1);
        carteiraList.add(carteira);
        ImportDal.gravarCarteira(carteiraList);
    }

    private static int verificarUltimoIdCarteira(List<Carteira> carteiraList) {
        int ultimoId = 0;
        for (Carteira carteira : carteiraList) {
            if (carteira.getIdDeposito() > ultimoId) {
                ultimoId = carteira.getIdDeposito();
            }
        }
        return ultimoId;
    }

    public static Double buscarValorTotalAtual(int IdCliente) {
        List<Utilizador> utilizadores = ImportDal.carregarUtilizador();

        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) {
                return utilizador.getSaldo();
            }
        }
        return 0.0;
    }

    public static void atualizarSaldo(int idCliente, double creditos) {
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) {
                utilizador.setSaldo(creditos);
            }
        }
    }

    public static Double valorPendente(int idCliente) {
        carteiraList = ImportDal.carregarCarteira();
        double valorTotalPendente = 0;
        for (Carteira carteira : carteiraList) {
            if (carteira.getIdCliente() == idCliente) {
                if(carteira.getIdEstadoDeposito() == Constantes.estadosDeposito.PENDENTE)
                    valorTotalPendente += carteira.getValorDeposito();
            }
        }
        return valorTotalPendente;
    }


}
