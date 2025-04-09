package Controller;

import BLL.FormularioAprovarClienteBLL;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteController {
    public static boolean aprovarCliente(String email, int estado)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEmail().equalsIgnoreCase(email))
            {
                boolean respFormularioAprovarClienteBLL = FormularioAprovarClienteBLL.aprovarCliente(u, estado);
                if (respFormularioAprovarClienteBLL) return true;
            }
        }
        return false;
    }
}
