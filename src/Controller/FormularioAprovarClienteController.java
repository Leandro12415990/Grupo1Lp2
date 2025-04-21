package Controller;

import BLL.FormularioAprovarClienteBLL;
import Model.Utilizador;
import Utils.Tools;

public class FormularioAprovarClienteController {
    public boolean aprovarCliente(String email)
    {
        FormularioAprovarClienteBLL formularioAprovarClienteBLL = new FormularioAprovarClienteBLL();
        for (Utilizador u : Tools.utilizadores)
        {
            if (u.getEmail().equalsIgnoreCase(email))
            {
                boolean respFormularioAprovarClienteBLL = formularioAprovarClienteBLL.aprovarCliente(u);
                if (respFormularioAprovarClienteBLL) return true;
            }
        }
        return false;
    }
}
