package Controller;

import BLL.AprovarClienteBLL;
import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

public class AprovarClienteController {
    public static boolean AprovarTodos()
    {
        AprovarClienteBLL aprovarClienteBLL = new AprovarClienteBLL();
        ImportDal importDal = new ImportDal();
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
            {
                aprovarClienteBLL.AprovarTodos(u);
            }
        }

        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
            {
                return false;
            }
        }

        importDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }
}
