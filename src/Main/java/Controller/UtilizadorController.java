package Controller;

import BLL.*;
import DAL.UtilizadorDAL;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UtilizadorController {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public List<Utilizador> mostrarUtilizador(int estado, int tipo) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        return utilizadorBLL.listarUtilizador(estado, tipo);

    }

    public ResultadoOperacao verificarDados(Utilizador utilizador, String nome, String email, LocalDate nascimento, String morada, String passwordFirst, String passwordSecond) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
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

    public ResultadoOperacao alterarEstadoUtilizadores(String email, int novoEstado, boolean alterarTodos, int idTipoUtilizador) throws MessagingException, IOException {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        for (Utilizador u : Tools.utilizadores) {
            if (u.getTipoUtilizador() != idTipoUtilizador) {
                continue;
            }

            boolean deveAlterar = alterarTodos
                    ? u.getEstado() != novoEstado
                    : u.getEmail().equalsIgnoreCase(email);

            if (deveAlterar) {
                resultado = utilizadorBLL.alterarEstadoUtilizador(u, novoEstado);
                if (!alterarTodos) break;
            }
        }
        return resultado;
    }

    public boolean validarDominioEmail(String email) {
        try {
            String dominio = email.substring(email.indexOf("@") + 1);

            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

            DirContext dirContext = new InitialDirContext(env);
            Attributes attrs = dirContext.getAttributes(dominio, new String[]{"MX"});

            Attribute attr = attrs.get("MX");

            return attr != null && attr.size() > 0;

        } catch (Exception e) {
            return false;
        }
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

    public boolean verificarPassword(String passwordFirst, String passwordSecound) {
        if (!passwordFirst.equals(passwordSecound)) return false;
        else return true;
    }

    public void verificarLoginsUtilizadores() throws MessagingException, IOException {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        utilizadorBLL.verificarLoginsUtilizadores(utilizadorBLL.carregarUtilizadores());
    }

    public Utilizador procurarUtilizadorPorEmail(String email) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        return utilizadorBLL.procurarUtilizadorPorEmail(email);
    }

    public double obterSaldoCliente(int idCliente) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(idCliente);
        if (utilizador != null) {
            return utilizador.getSaldo();
        }
        return -1.0;
    }

}
