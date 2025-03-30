package Controller;

import BLL.FormularioAprovarClienteBLL;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteController {
    public static void aprovarCliente(String email)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEmail().equals(email))
            {
                FormularioAprovarClienteBLL.aprovarCliente(u);
            }
        }
    }
}
