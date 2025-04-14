package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorBLL {
    public static List<Utilizador> listarUtilizador(int estado, int tipo) {
        ImportDal.carregarUtilizador();
        List<Utilizador> utilizadoresList = new ArrayList<>();
        if (estado != 0) {
            for (Utilizador utilizador : Tools.utilizadores) {
                if (utilizador.getEstado() == estado && utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        } else {
            for (Utilizador utilizador : Tools.utilizadores) {
                if (utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        }
        return utilizadoresList;
    }

    public static boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password) {
        Utilizador utilizador;
        LocalDate data = LocalDate.now();

        Tools.utilizadores = ImportDal.carregarUtilizador();
        int max = -1;

        for (Utilizador u : Tools.utilizadores) {
            if (u.getId() > max) max = u.getId();
            if (email == u.getEmail()) return false;
        }

        try {
            utilizador = new Utilizador(max + 1, nome, email, nascimento, morada, password, data, data, Tools.tipoUtilizador.CLIENTE.getCodigo(), Tools.estadoUtilizador.PENDENTE.getCodigo(), 0.0);
        } catch (Exception e) {
            return false;
        }

        Tools.utilizadores.add(utilizador);
        ImportDal.gravarUtilizador(Tools.utilizadores);
        return true;
    }

    public static boolean aprovarCliente(Utilizador u, int estado) {
        // Define o novo estado do utilizador
        int novoEstado = (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
                ? Tools.estadoUtilizador.ATIVO.getCodigo()
                : Tools.estadoUtilizador.INATIVO.getCodigo();

        u.setEstado(novoEstado);

        // Procura o utilizador correspondente e verifica se o estado foi aplicado
        for (Utilizador uti : Tools.utilizadores) {
            if (u.getEmail().equalsIgnoreCase(uti.getEmail()) && uti.getEstado() == novoEstado) {
                ImportDal.gravarUtilizador(Tools.utilizadores);
                return true;
            }
        }

        return false;
    }

    public static boolean editarCliente(Utilizador utilizador, String nome, LocalDate nascimento, String morada, String password) {
        int soma = 0, index = 0;

        Tools.utilizadores = ImportDal.carregarUtilizador();

        for (Utilizador u : ImportDal.carregarUtilizador()) {
            if (u.getEmail().equals(utilizador.getEmail())) {
                index = soma;
                if (!nome.isEmpty()) utilizador.setNomeUtilizador(nome);
                if (nascimento != null) utilizador.setDataNascimento(nascimento);
                if (!morada.isBlank()) utilizador.setMorada(morada);
                if (!password.isBlank()) utilizador.setPassword(password);
            }
            soma++;
        }

        Tools.utilizadores.set(index, utilizador);

        ImportDal.gravarUtilizador(Tools.utilizadores);

        return true;
    }

    public static void aprovarTodosClientes(Utilizador u, int estado) {
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
        else u.setEstado(Tools.estadoUtilizador.INATIVO.getCodigo());
    }
}
