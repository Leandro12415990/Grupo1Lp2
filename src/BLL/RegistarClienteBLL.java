package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistarClienteBLL {

    public static boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Utilizador utilizador = null;
        LocalDate hora = LocalDate.now();

        Tools.utilizadores = ImportDal.carregarUtilizador();
        int max = -1;

        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getId() > max) max = u.getId();
            if (email == u.getEmail())
            {
                return false;
            }
        }

        try {
            utilizador = new Utilizador( max + 1, nome, email, nascimento, morada, password, hora, hora, 1, Tools.estadoUtilizador.PENDENTE.getCodigo(), 0.0);
        } catch (Exception e) {
            return false;
        }

        Tools.utilizadores.add(utilizador);

        ImportDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
