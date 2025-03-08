package BLL;

import Model.Utilizador;

import java.time.LocalDate;

public class RegistarClienteBll {

    public static boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password)
    {
        try {
            //Utilizador utilizador = new Utilizador(nome, email, nascimento, morada, password);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
