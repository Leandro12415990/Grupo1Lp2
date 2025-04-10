package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.EmailSender;
import Utils.Tools;

public class FormularioAprovarClienteBLL {
    public static boolean aprovarCliente(Utilizador u, int estado)
    {
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()){
            u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
            EmailSender.enviarEmailSimples(u.getEmail(), "Aprovado", "Aprovado");
        }
        else u.setEstado(Tools.estadoUtilizador.INATIVO.getCodigo());

        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
        {
            for (Utilizador uti : Tools.utilizadores)
            {
                if (u.getEmail().equalsIgnoreCase(uti.getEmail()))
                {
                    if (uti.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo())
                    {
                        ImportDal.gravarUtilizador(Tools.utilizadores);
                        return true;
                    }
                }
            }
        }
        else
        {
            for (Utilizador uti : Tools.utilizadores)
            {
                if (u.getEmail().equalsIgnoreCase(uti.getEmail()))
                {
                    if (uti.getEstado() == Tools.estadoUtilizador.INATIVO.getCodigo())
                    {
                        ImportDal.gravarUtilizador(Tools.utilizadores);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
