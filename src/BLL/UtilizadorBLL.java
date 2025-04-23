package BLL;

import DAL.UtilizadorDAL;
import Model.ResultadoOperacao;
import Model.Template;
import Model.Utilizador;
import Utils.EmailSender;
import Utils.Tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UtilizadorBLL {
    public List<Utilizador> listarUtilizador(int estado, int tipo) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> utilizadoresList = new ArrayList<>();
        if (estado != 0) {
            for (Utilizador utilizador : utilizadorDAL.carregarUtilizadores()) {
                if (utilizador.getEstado() == estado && utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        } else {
            for (Utilizador utilizador : utilizadorDAL.carregarUtilizadores()) {
                if (utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        }
        return utilizadoresList;
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

    public ResultadoOperacao alterarEstadoUtilizador(Utilizador u, int estado) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (u == null || estado == 0) {
            resultado.msgErro = "Erro a alterar estado do utilizador";
        } else {
            u.setEstado(estado);
            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) {
                String assunto = "Bem-vindo à ValorAlta Leilões!";
                String mensagem = String.format("""
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2>Olá %s,</h2>
                        <p>É com enorme satisfação que informamos que a sua conta foi <strong>aprovada</strong> na <strong>Valor em Alta Leilões</strong>!</p>
                        <p>Agora você pode:</p>
                        <ul>
                            <li>Aceder aos nossos leilões exclusivos</li>
                            <li>Fazer lances em tempo real</li>
                            <li>Gerir o seu perfil e histórico</li>
                        </ul>
                        <p>Estamos felizes por tê-lo connosco! Se precisar de ajuda, não hesite em contactar-nos.</p>
                        <p style="margin-top: 20px;">Cumprimentos,<br><strong>Equipa Valor em Alta Leilões</strong></p>
                    </body>
                </html>
                """, u.getNomeUtilizador());

                    EmailSender.enviarEmailHtml(
                            u.getNomeUtilizador(),
                            u.getEmail(),
                            assunto,
                            mensagem
                    );

            }
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
            gravarUtilizadores(Tools.utilizadores);
        }
        return resultado;
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
