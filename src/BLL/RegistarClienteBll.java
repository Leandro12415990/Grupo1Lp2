package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistarClienteBll {

    public static boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Utilizador utilizador = null;
        LocalDate hora = LocalDate.now();

        Tools.utilizadores = ImportDal.carregarUtilizador();
        int id = 0, max = -1;
        boolean idEncontrado = false;

        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getId() > max) max = u.getId();
            if (email == u.getEmail())
            {
                id = u.getId();
                idEncontrado = true;
                break;
            }
        }

        try {
            if (idEncontrado) utilizador = new Utilizador( id, nome, email, nascimento, morada, password, hora, hora, 1);
            else utilizador = new Utilizador( max + 1, nome, email, nascimento, morada, password, hora, hora, 1);
        } catch (Exception e) {
            return false;
        }

        Tools.utilizadores.add(utilizador);

        ImportDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
