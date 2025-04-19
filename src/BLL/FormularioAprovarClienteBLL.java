package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteBLL {
    public boolean aprovarCliente(Utilizador u)
    {
        ImportDal importDal = new ImportDal();
        u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());

        for (Utilizador uti : Tools.utilizadores)
        {
            if (u.getEmail().equalsIgnoreCase(uti.getEmail()))
            {
                if (uti.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo())
                {
                    importDal.gravarUtilizador(Tools.utilizadores);
                    return true;
                }
            }
        }
        return false;
    }
}
