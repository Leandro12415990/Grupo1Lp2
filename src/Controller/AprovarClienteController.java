package Controller;

import BLL.AprovarClienteBLL;
import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

public class AprovarClienteController {
    public static boolean AprovarTodos()
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
            {
                AprovarClienteBLL.AprovarTodos(u);
            }
        }

        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
            {
                return false;
            }
        }

        ImportDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
