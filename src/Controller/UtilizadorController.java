package Controller;

import BLL.*;
import DAL.ImportDAL;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public class UtilizadorController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private final UtilizadorBLL utilizadorBLL;

    public UtilizadorController(UtilizadorBLL utilizadorBLL) {
        this.utilizadorBLL = utilizadorBLL;
    }

    public List<Utilizador> mostrarUtilizador(int estado, int tipo) {
        return utilizadorBLL.listarUtilizador(estado, tipo);
    }

    public ResultadoOperacao verificarDados(Utilizador utilizador, String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecond) {
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
                utilizadorBLL.criarCliente(nome, email, nascimento, morada, passwordFirst);
                resultado.Objeto = resultado;
                resultado.Sucesso = true;
            }
        } else {
            // Caso de edição
            utilizadorBLL.editarCliente(utilizador, nome, nascimento, morada, passwordFirst);
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }

        return resultado;
    }

    public boolean aprovarTodosClientes(int estado) {
        boolean aprovouAlguem = false;

        for (Utilizador u : Tools.utilizadores) {
            boolean deveAprovar = (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
                    ? u.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo()
                    : u.getEstado() != Tools.estadoUtilizador.INATIVO.getCodigo();

            if (deveAprovar) {
                utilizadorBLL.aprovarTodosClientes(u, estado);
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
        if (aprovouAlguem) utilizadorBLL.gravarUtilizadores(Tools.utilizadores);
        return true;
    }

    private boolean validaDataNascimento(LocalDate nascimento) {
        if (nascimento.isAfter(LocalDate.now()) || calcularIdade(nascimento) < 18) return false;
        else return true;
    }

    public int calcularIdade(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false; // Evita erro de NullPointerException
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public boolean aprovarCliente(String email, int estado) {
        for (Utilizador u : Tools.utilizadores) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                boolean respFormularioAprovarClienteBLL = utilizadorBLL.aprovarCliente(u, estado);
                if (respFormularioAprovarClienteBLL) return true;
            }
        }
        return false;
    }

    public boolean verificarPassword(String passwordFirst, String passwordSecond) {
        return passwordFirst.equals(passwordSecond);
    }
}
