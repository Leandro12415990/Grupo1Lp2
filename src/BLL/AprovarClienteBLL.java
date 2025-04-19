package BLL;

import Model.Utilizador;
import Utils.Tools;

public class AprovarClienteBLL {
    public void AprovarTodos(Utilizador u)
    {
        u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
    }
}
