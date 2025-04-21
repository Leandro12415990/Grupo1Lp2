package Controller;

import BLL.RegistarClienteBLL;
import Model.ResultadoOperacao;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class RegistarClienteController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public ResultadoOperacao verificarDados(String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecound) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        boolean respValidaDataNascimento = validaDataNascimento(nascimento);

        if (!respValidaDataNascimento) {
            resultado.msgErro = "Deve ter mais de 18 anos para se registar.";
        } else if (!isValidEmail(email)) {
            resultado.msgErro = "O email inserido não é valido.";
        } else {
            RegistarClienteBLL.criarCliente(nome, email, nascimento, morada, passwordFirst);
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
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

