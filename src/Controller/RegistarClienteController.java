package Controller;

import java.time.LocalDate;

public class RegistarClienteController {
    private boolean verificarDados(String nome, String morada, LocalDate nascimento, String email, String passwordFirst, String passwordSecound)
    {
        boolean respVerificarPassword = verifivarPassword(passwordFirst, passwordSecound);
        if (!respVerificarPassword) return false;



        return true;
    }

    private boolean verifivarPassword(String passwordFirst, String passwordSecound)
    {
        if (passwordFirst != passwordSecound) return false;
        else return true;
    }
}
