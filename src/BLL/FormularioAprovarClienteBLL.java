package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteBLL {
    public static boolean aprovarCliente(Utilizador u)
    {
        u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());

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
        return false;
    }
}
