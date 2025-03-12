package Controller;

import BLL.RegistarClienteBll;

import java.time.LocalDate;

public class RegistarClienteController {

    public static boolean verificarDados(String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecound)
    {
        boolean respRegistarCliente = false;
        boolean respVerificarPassword = verifivarPassword(passwordFirst, passwordSecound);
        if (respVerificarPassword) respRegistarCliente = RegistarClienteBll.criarCliente(nome, email, nascimento, morada, passwordFirst);

        if (!respVerificarPassword || !respRegistarCliente) return false;

        return true;
    }

    private static boolean verifivarPassword(String passwordFirst, String passwordSecound)
    {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
        // teste
    }
}
