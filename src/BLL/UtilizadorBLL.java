package BLL;

import DAL.TemplateDAL;
import DAL.UtilizadorDAL;
import Model.ResultadoOperacao;
import Model.TemplateModel;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilizadorBLL {
    public List<Utilizador> listarUtilizador(int estado, int tipo) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> todosUtilizadores = utilizadorDAL.carregarUtilizadores();

        List<Utilizador> filtrados = new ArrayList<>();
        for (Utilizador u : todosUtilizadores) {
            boolean estadoOK = (estado == 0 || u.getEstado() == estado);
            boolean tipoOK = (tipo == 0 || u.getTipoUtilizador() == tipo);
            if (estadoOK && tipoOK) {
                filtrados.add(u);
            }
        }

        return filtrados;
    }


    public boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        Utilizador utilizador;
        LocalDate data = LocalDate.now();

        Tools.utilizadores = utilizadorDAL.carregarUtilizadores();
        int max = -1;

        for (Utilizador u : Tools.utilizadores) {
            if (u.getId() > max) max = u.getId();
            if (email.equals(u.getEmail())) return false;
        }

        try {
            utilizador = new Utilizador(max + 1, nome, email, nascimento, morada, password, data, data, Tools.tipoUtilizador.CLIENTE.getCodigo(), Tools.estadoUtilizador.PENDENTE.getCodigo(), 0.0);
        } catch (Exception e) {
            return false;
        }

        Tools.utilizadores.add(utilizador);
        utilizadorDAL.gravarUtilizadores(Tools.utilizadores);
        return true;
    }

    public ResultadoOperacao alterarEstadoUtilizador(Utilizador u, int estado) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();
        EmailBLL emailBLL = new EmailBLL();
        TemplateDAL dal = new TemplateDAL();


        if (u == null || estado == 0) {
            resultado.msgErro = "Erro a alterar estado do utilizador";
        } else {
            u.setEstado(estado);
            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) {
                TemplateModel template = dal.carregarTemplatePorId("1");
                Map<String, String> variaveis = new HashMap<>();
                variaveis.put("NOME", u.getNomeUtilizador());
                emailBLL.enviarEmail(template, u.getEmail(), variaveis);

            }
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
            gravarUtilizadores(Tools.utilizadores);
        }
        return resultado;
    }


    public boolean editarCliente(Utilizador utilizador, String nome, LocalDate nascimento, String morada, String password) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        int soma = 0, index = 0;

        for (Utilizador u : Tools.utilizadores) {
            if (u.getEmail().equals(utilizador.getEmail())) {
                index = soma;
                if (!nome.isEmpty()) utilizador.setNomeUtilizador(nome);
                if (!morada.isBlank()) utilizador.setMorada(morada);
                if (!password.isBlank()) utilizador.setPassword(password);
            }
            soma++;
        }

        Tools.utilizadores.set(index, utilizador);
        utilizadorDAL.gravarUtilizadores(Tools.utilizadores);
        return true;
    }

    public Utilizador procurarUtilizadorPorId(int idCliente) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador u : utilizadores) {
            if (u.getId() == idCliente) {
                return u;
            }
        }
        return null;
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        utilizadorDAL.gravarUtilizadores(utilizadores);
    }
}
