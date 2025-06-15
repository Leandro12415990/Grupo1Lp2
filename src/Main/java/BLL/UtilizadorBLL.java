package BLL;

import DAL.ImportDAL;
import DAL.TemplateDAL;
import Controller.UtilizadorController;
import DAL.LeilaoDAL;
import DAL.UtilizadorDAL;
import Model.ResultadoOperacao;
import Model.Template;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Constantes.templateIds;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class UtilizadorBLL {
    public List<Utilizador> carregarUtilizadores() {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        return utilizadorDAL.carregarUtilizadores();
    }

    public List<Utilizador> listarUtilizador(int estado, int tipo) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> todosUtilizadores = utilizadorDAL.carregarUtilizadores();

        List<Utilizador> filtrados = new ArrayList<>();
        for (Utilizador u : todosUtilizadores) {
            boolean estadoOK = (estado == 0 || u.getEstado() == estado);
            boolean tipoOK = (tipo == 0 || u.getTipoUtilizador() == tipo);
            if (estadoOK && tipoOK) filtrados.add(u);
        }
        return filtrados;
    }


    public Utilizador criarCliente(String nome, String email, LocalDate nascimento, String morada, String password) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        Utilizador utilizador;
        LocalDate data = LocalDate.now();

        Tools.utilizadores = utilizadorDAL.carregarUtilizadores();
        int max = -1;

        for (Utilizador u : Tools.utilizadores) {
            if (u.getId() > max) max = u.getId();
            if (email.equals(u.getEmail())) return null;
        }

        try {
            utilizador = new Utilizador(max + 1, nome, email, nascimento, morada, password, data, data, Tools.tipoUtilizador.CLIENTE.getCodigo(), Tools.estadoUtilizador.PENDENTE.getCodigo(), 0.0);
        } catch (Exception e) {
            return null;
        }

        Tools.utilizadores.add(utilizador);
        utilizadorDAL.gravarUtilizadores(Tools.utilizadores);
        return utilizador;
    }

    public ResultadoOperacao alterarEstadoUtilizador(Utilizador u, int estado) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();
        EmailBLL emailBLL = new EmailBLL();
        TemplateDAL templateDAL = new TemplateDAL();

        if (u == null || estado == 0) {
            resultado.msgErro = "Erro a alterar estado do utilizador";
        } else {
            u.setEstado(estado);
            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) {
                Template template = templateDAL.carregarTemplatePorId(templateIds.EMAIL_APROVADO);
                if (template != null) {
                    emailBLL.enviarEmail(template, u.getEmail(), Tools.substituirTags(u, null, null), u.getId());
                } else {
                    resultado.msgErro = "O Template não foi encontrado";
                }
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
            if (u.getId() == idCliente) return u;
        }
        return null;
    }

    public Utilizador procurarUtilizadorPorEmail(String email) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador u : utilizadores) {
            if (Objects.equals(u.getEmail(), email)) return u;
        }
        return null;
    }

    public Utilizador procurarUtilizadorByNome(String nome) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador u : utilizadores) {
            if (Objects.equals(u.getNomeUtilizador(), nome)) return u;
        }
        return null;
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        utilizadorDAL.gravarUtilizadores(utilizadores);
    }

    public void verificarLoginsUtilizadores(List<Utilizador> utilizadores) throws IOException, MessagingException {
        EmailBLL emailBLL = new EmailBLL();
        TemplateDAL templateDAL = new TemplateDAL();

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime tresMesesAtras = agora.minusMonths(3);

        for (Utilizador u : utilizadores) {
            if (u.getUltimoLogin() != null) {
                LocalDateTime ultimoLogin = u.getUltimoLogin().atStartOfDay();

                if (ultimoLogin.isBefore(tresMesesAtras)) {
                    if (!emailBLL.foiEmailAvisoEnviado(u.getId(), templateIds.EMAIL_CLIENTE_OFFLINE)) {
                        Template template = templateDAL.carregarTemplatePorId(templateIds.EMAIL_CLIENTE_OFFLINE);
                        if (template != null) {
                            emailBLL.enviarEmail(template, u.getEmail(), Tools.substituirTags(u, null, null), u.getId());
                        }
                    }
                }
            }
        }
    }
    public boolean validaDataNascimento(LocalDate nascimento) {
        if (nascimento.isAfter(LocalDate.now()) || calcularIdade(nascimento) < 18) return false;
        else return true;
    }

    public int calcularIdade(LocalDate nascimento) {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }

    //IMPORTAR UTILIZADORES BY FICHEIRO - PP
    public ResultadoImportacao importarUtilizadores() {
        ImportDAL importDAL = new ImportDAL();
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        int totalImportados = 0;
        int totalExistentes = 0;
        List<String> erros = new ArrayList<>();
        List<Utilizador> utilizadoresImportados = new ArrayList<>();

        List<String[]> linhas = importDAL.lerLinhasCSV(Constantes.caminhosFicheiros.CSV_FILE_IMPORT_CLIENTES, 4);

        for (String[] dados : linhas) {
            try {
                String nome = dados[0];
                String morada = dados[1];
                LocalDate dataNascimento = parseDate(dados[2]);
                String email = dados[3];
                LocalDateTime dataRegisto = LocalDateTime.now();

                if (dataNascimento == null) {
                    erros.add("Data de nascimento inválida: " + Arrays.toString(dados));
                    continue;
                }

                if (!validaDataNascimento(dataNascimento)) {
                    erros.add("Utilizador com menos de 18 anos: " + Arrays.toString(dados));
                    continue;
                }
                Utilizador utilizadorExiste = procurarUtilizadorPorEmail(email);
                if (utilizadorExiste != null) {
                    totalExistentes++;
                    continue;
                }

                String password = gerarPasswordTemporaria();
                Utilizador utilizador = criarCliente(nome, email, dataNascimento, morada, password);
                enviarEmailNovaPassword(utilizador.getId());

                utilizadoresImportados.add(utilizador);
                totalImportados++;
            } catch (Exception e) {
                erros.add("Erro ao importar linha: " + Arrays.toString(dados) + " - " + e.getMessage());
            }
        }

        return new ResultadoImportacao(utilizadoresImportados, totalImportados, totalExistentes, erros);
    }

    private String gerarPasswordTemporaria() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void enviarEmailNovaPassword(int id) throws MessagingException, IOException {
        EmailBLL emailBLL = new EmailBLL();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(id);
        emailBLL.enviarEmail(Constantes.templateIds.EMAIL_CLIENTES_CRIADO_IMPORT, utilizador.getEmail(),
                Tools.substituirTags(utilizador, null, null), utilizador.getId());
    }

    private LocalDate parseDate(String dataTexto) {
        try {
            return LocalDate.parse(dataTexto, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            return null;
        }
    }

    // Classe auxiliar para devolver os dados à View
    public static class ResultadoImportacao {
        public final List<Utilizador> utilizadoresImportados;
        public final int totalImportados;
        public final int totalExistentes;
        public final List<String> erros;

        public ResultadoImportacao(List<Utilizador> utilizadoresImportados, int totalImportados, int totalExistentes, List<String> erros) {
            this.utilizadoresImportados = utilizadoresImportados;
            this.totalImportados = totalImportados;
            this.totalExistentes = totalExistentes;
            this.erros = erros;
        }
    }
}
