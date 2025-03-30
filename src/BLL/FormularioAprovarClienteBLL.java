package BLL;

import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteBLL {
    public static void aprovarCliente(Utilizador u)
    {
        u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
    }
}
