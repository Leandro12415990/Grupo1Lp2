package BLL;

import Model.Utilizador;
import Utils.Tools;

public class AprovarClienteBLL {
    public static void AprovarTodos(Utilizador u, int estado)
    {
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
        else u.setEstado(Tools.estadoUtilizador.INATIVO.getCodigo());
    }
}
