package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;
import Model.ClienteSessao;

import java.time.LocalDate;

public class LoginUtilizadorBLL {

    public Utilizador login(String email, String password)
    {
        ImportDal importDal = new ImportDal();
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword()))
            {
                u.setUltimoLogin(LocalDate.now());
                importDal.gravarUtilizador(Tools.utilizadores);
                ClienteSessao.setIdCliente(u.getId());

                if (u.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo() || u.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo()) return u;

            }
        }
        return null;
    }

    public boolean lerDados() {
        ImportDal importDal = new ImportDal();
        Tools.utilizadores = importDal.carregarUtilizador();
        return Tools.utilizadores != null;
    }
}