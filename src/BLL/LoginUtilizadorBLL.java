package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class LoginUtilizadorBLL {
    public static Utilizador login(String email, String password)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword()))
            {
                u.setUltimoLogin(LocalDate.now());
                ImportDal.gravarUtilizador(Tools.utilizadores);
                if (u.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo() || u.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo()) return u;
            }
        }
        return null;
    }

    public static boolean lerDados()
    {
        Tools.utilizadores = ImportDal.carregarUtilizador();
        if (Tools.utilizadores != null) return true;
        return false;
    }
}
