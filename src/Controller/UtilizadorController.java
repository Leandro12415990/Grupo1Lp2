package Controller;

import BLL.*;
import DAL.ImportDal;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Tools;
import View.UtilizadorView;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public class UtilizadorController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static void mostrarUtilizador(int estado, int tipo) {
        List<Utilizador> utilizadores = UtilizadorBLL.listarUtilizador(estado, tipo);

        UtilizadorView.exibirUtilizadores(utilizadores);
    }

    public static ResultadoOperacao verificarDados(Utilizador utilizador, String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecond) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        boolean respValidaDataNascimento = validaDataNascimento(nascimento);

        if (!respValidaDataNascimento) {
            resultado.msgErro = "Deve ter mais de 18 anos para se registar.";
        } else if (utilizador == null) {
            // Caso de registo
            if (!isValidEmail(email)) {
                resultado.msgErro = "O email inserido não é valido.";
            } else if (!passwordFirst.equals(passwordSecond)) {
                resultado.msgErro = "As palavras-passe não coincidem.";
            } else {
                UtilizadorBLL.criarCliente(nome, email, nascimento, morada, passwordFirst);
                resultado.Objeto = resultado;
                resultado.Sucesso = true;
            }
        } else {
            // Caso de edição
            UtilizadorBLL.editarCliente(utilizador, nome, nascimento, morada, passwordFirst);
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }

        return resultado;
    }

    public static boolean aprovarTodosClientes(int estado) {
        boolean aprovouAlguem = false;

        for (Utilizador u : Tools.utilizadores) {
            boolean deveAprovar = (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
                    ? u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo()
                    : u.getEstado() != Tools.estadoUtilizador.INATIVO.getCodigo();

            if (deveAprovar) {
                UtilizadorBLL.aprovarTodosClientes(u, estado);
                aprovouAlguem = true;
            }
        }

        // Verifica se todos os utilizadores estão no estado esperado após a aprovação
        for (Utilizador u : Tools.utilizadores) {
            boolean estadoIncorreto = (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
                    ? u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo()
                    : u.getEstado() != Tools.estadoUtilizador.INATIVO.getCodigo();
            if (estadoIncorreto) return false;
        }
        if (aprovouAlguem) ImportDal.gravarUtilizador(Tools.utilizadores);
        return true;
    }


    private static boolean validaDataNascimento(LocalDate nascimento) {
        if (nascimento.isAfter(LocalDate.now()) || calcularIdade(nascimento) < 18) return false;
        else return true;
    }

    public static int calcularIdade(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false; // Evita erro de NullPointerException
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean aprovarCliente(String email, int estado) {
        for (Utilizador u : Tools.utilizadores) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                boolean respFormularioAprovarClienteBLL = UtilizadorBLL.aprovarCliente(u, estado);
                if (respFormularioAprovarClienteBLL) return true;
            }
        }
        return false;
    }

    public static boolean verificarPassword(String passwordFirst, String passwordSecound) {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
    }
}
