package Controller;

import BLL.EditarClienteBLL;
import Model.ResultadoOperacao;
import Model.Utilizador;

import java.time.LocalDate;
import java.time.Period;

public class EditarClienteController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static ResultadoOperacao verificarDados(Utilizador utilizador, String nome, LocalDate nascimento, String morada, String password) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        EditarClienteBLL editarClienteBLL = new EditarClienteBLL();
        boolean respValidaDataNascimento = validaDataNascimento(nascimento);

        if (!respValidaDataNascimento) {
            resultado.msgErro = "Deve ter mais de 18 anos para se registar.";
        } else {
            editarClienteBLL.EditarCliente(utilizador, nome, nascimento, morada, password);
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    private static boolean validaDataNascimento(LocalDate nascimento)
    {
        if (nascimento != null && (nascimento.isAfter(LocalDate.now()) || calcularIdade(nascimento) < 18)) return false;
        else return true;
    }

    public static int calcularIdade(LocalDate nascimento)
    {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }
}
