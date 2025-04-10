package Controller;

import BLL.AprovarClienteBLL;
import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

public class AprovarClienteController {
    public static boolean AprovarTodos(int estado)
    {
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
        {
            for (Utilizador u : Tools.utilizadores)
            {
                if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
                {
                    AprovarClienteBLL.AprovarTodos(u, estado);
                }
            }
        }
        else
        {
            for (Utilizador u : Tools.utilizadores)
            {
                if (u.getEstado() != Tools.estadoUtilizador.INATIVO.getCodigo())
                {
                    AprovarClienteBLL.AprovarTodos(u, estado);
                }
            }
        }

        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
        {
            for (Utilizador u : Tools.utilizadores)
            {
                if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
                {
                    return false;
                }
            }
        }
        else
        {
            for (Utilizador u : Tools.utilizadores)
            {
                if (u.getEstado() != Tools.estadoUtilizador.INATIVO.getCodigo())
                {
                    return false;
                }
            }
        }

        ImportDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
