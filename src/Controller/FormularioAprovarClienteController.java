package Controller;

import BLL.FormularioAprovarClienteBLL;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteController {
    public static boolean aprovarCliente(String email)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEmail().equalsIgnoreCase(email))
            {
                boolean respFormularioAprovarClienteBLL = FormularioAprovarClienteBLL.aprovarCliente(u);
                if (respFormularioAprovarClienteBLL) return true;
            }
        }
        return false;
    }
}
