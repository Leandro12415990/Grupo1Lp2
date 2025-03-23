package Controller;

import BLL.RegistarClienteBll;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class RegistarClienteController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean verificarDados(String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecound)
    {
        boolean respRegistarCliente = false;
        boolean respValidaDataNascimento = validaDataNascimento(nascimento);

        if (!respValidaDataNascimento || !isValidEmail(email)) return false;

        respRegistarCliente = RegistarClienteBll.criarCliente(nome, email, nascimento, morada, passwordFirst);

        if (!respRegistarCliente) return false;

        return true;
    }

     private static boolean validaDataNascimento(LocalDate nascimento)
     {
         if (nascimento.isAfter(LocalDate.now()) || calcularIdade(nascimento) < 18) return false;
         else return true;
     }

    public static int calcularIdade(LocalDate nascimento)
    {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }

    public static boolean isValidEmail(String email)
    {
        if (email == null) return false; // Evita erro de NullPointerException
        return Pattern.matches(EMAIL_REGEX, email);
    }
}

