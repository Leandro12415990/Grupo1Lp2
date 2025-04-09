package BLL;

import DAL.ImportDal;
import Model.Utilizador;

import java.util.List;

public class UtilizadorBLL {
    public static Utilizador procurarUtilizadorPorId(int idCliente) {
        List<Utilizador> utilizadores = ImportDal.carregarUtilizador(); // assumindo que lês de um ficheiro
        for (Utilizador u : utilizadores) {
            if (u.getId() == idCliente) {
                return u;
            }
        }
        return null;
    }
}
