package View;

import BLL.EmailBLL;
import BLL.UtilizadorBLL;
import Controller.UtilizadorController;
import DAL.TemplateDAL;
import Model.ResultadoOperacao;
import Model.Template;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorView {

    public void registarCliente() throws IOException, MessagingException {
        UtilizadorController utilizadorController = new UtilizadorController();
        TemplateDAL templateDAL = new TemplateDAL();
        EmailBLL emailBLL = new EmailBLL();
        String passwordFirst, passwordSecound;

        System.out.println("\n" + "-".repeat(7) + " REGISTO " + "-".repeat(7));

        System.out.print("Nome " + Tools.alertaCancelar());
        String nome = Tools.scanner.nextLine();
        if (Tools.verificarSaida(nome)) return;

        System.out.print("E-mail " + Tools.alertaCancelar());
        String email = Tools.scanner.nextLine();
        if (Tools.verificarSaida(email)) return;

        LocalDate nascimento = null;
        boolean dataValida = false;
        do {
            System.out.print("Data de Nascimento (dd/MM/yyyy) " + Tools.alertaCancelar());
            try {
                String data = Tools.scanner.nextLine();
                if (Tools.verificarSaida(data)) return;
                else {
                    nascimento = LocalDate.parse(data, Tools.FORMATTER);
                    dataValida = true;
                    if (dataValida) {
                        dataValida = utilizadorController.validaDataNascimento(nascimento);
                    }
                    if (!dataValida) {
                        System.out.println("Data de nascimento inválida");
                    }
                }
            } catch (Exception e) {
                System.out.println("Tipo de data inválida, tente novamente...\n");
            }
        } while (!dataValida);

        System.out.print("Morada " + Tools.alertaCancelar());
        String morada = Tools.scanner.nextLine();
        if (Tools.verificarSaida(morada)) return;

        boolean respVerificarPassword = false;
        do {
            System.out.print("Insira a Password " + Tools.alertaCancelar());
            passwordFirst = Tools.scanner.nextLine();
            if (Tools.verificarSaida(passwordFirst)) return;

            System.out.print("Repita a Password: ");
            passwordSecound = Tools.scanner.nextLine();
            respVerificarPassword = utilizadorController.verificarPassword(passwordFirst, passwordSecound);
            if (!respVerificarPassword) System.out.println("Erro, passwords não coincidem, tente novamente...\n");
        } while (!respVerificarPassword);

        ResultadoOperacao resultado = utilizadorController.verificarDados(null, nome, email, nascimento, morada, passwordFirst, passwordSecound);

        if (resultado.Sucesso) {
            System.out.println("Cliente registado com sucesso");
            Utilizador u = procurarUtilizadorPorEmail(email);
            Template template = templateDAL.carregarTemplatePorId(Constantes.templateIds.EMAIL_REGISTO);
            if (template != null)
                emailBLL.enviarEmail(template, u.getEmail(), Tools.substituirTags(u, null, null), u.getId());
        } else System.out.println(resultado.msgErro);
    }

    public void alterarEstadoClientes(int estado, String acao, int idTipoUtilizador) throws MessagingException, IOException {
        UtilizadorController utilizadorController = new UtilizadorController();
        while (true) {
            List<Utilizador> utilizadoresAlterarEstado = new ArrayList<>();

            if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) {
                utilizadoresAlterarEstado = utilizadorController.mostrarUtilizador(Tools.estadoUtilizador.PENDENTE.getCodigo(), 2);
            } else if (estado == Tools.estadoUtilizador.INATIVO.getCodigo()) {
                utilizadoresAlterarEstado = utilizadorController.mostrarUtilizador(Tools.estadoUtilizador.ATIVO.getCodigo(), 2);
            }

            if (utilizadoresAlterarEstado == null || utilizadoresAlterarEstado.isEmpty()) {
                System.out.println("Não existem utilizadores disponíveis para " + acao.toLowerCase() + "...");
                return;
            }

            System.out.println("=".repeat(7) + "MENU " + acao.toUpperCase() + " CLIENTES" + "=".repeat(7) + "\n");
            exibirUtilizadores(utilizadoresAlterarEstado);
            System.out.println("\n1. " + acao + " Todos os Clientes");
            System.out.println("2. " + acao + " Cliente Específico");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = Tools.scanner.nextInt();
            Tools.scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    ResultadoOperacao resultadoTodos = utilizadorController.alterarEstadoUtilizadores(null, estado, true, idTipoUtilizador);
                    if (resultadoTodos.Sucesso) {
                        String mensagemFinal = switch (acao.toLowerCase()) {
                            case "aprovar" -> "Clientes aprovados com sucesso!";
                            case "inativar" -> "Clientes inativados com sucesso!";
                            default -> "Estado dos clientes alterado com sucesso!";
                        };
                        System.out.println(mensagemFinal);
                    } else {
                        System.out.println(resultadoTodos.msgErro);
                    }
                    return;
                case 2:
                    formularioAlterarEstadoCliente(estado, acao, idTipoUtilizador);
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    public void formularioAlterarEstadoCliente(int estado, String acao, int idTipoUtilizador) throws MessagingException, IOException {
        UtilizadorController utilizadorController = new UtilizadorController();

        while (true) {
            System.out.print("Email do cliente " + Tools.alertaCancelar());
            String email = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return;

            ResultadoOperacao resultado = utilizadorController.alterarEstadoUtilizadores(email, estado, false, idTipoUtilizador);
            if (resultado.Sucesso) {
                String mensagemFinal = switch (acao.toLowerCase()) {
                    case "aprovar" -> "Cliente aprovados com sucesso!";
                    case "inativar" -> "Cliente inativados com sucesso!";
                    default -> "Estado dos cliente alterado com sucesso!";
                };
                System.out.println(mensagemFinal);
            } else {
                System.out.println(resultado.msgErro);
            }
        }
    }

    public void editarCliente(Utilizador utilizador) {
        UtilizadorController utilizadorController = new UtilizadorController();
        while (true) {
            String passwordFirst;
            String passwordSecound = "";

            System.out.println("\n" + "=".repeat(7) + " EDITAR FICHA CLIENTE " + "=".repeat(7));

            System.out.print("Nome " + Tools.alertaCancelar());
            String nome = Tools.scanner.nextLine();
            if (Tools.verificarSaida(nome)) return;

            LocalDate nascimento = utilizador.getDataNascimento();

            System.out.print("Morada " + Tools.alertaCancelar());
            String morada = Tools.scanner.nextLine();
            if (Tools.verificarSaida(morada)) return;

            boolean respVerificarPassword;
            do {
                System.out.print("Insira a Password " + Tools.alertaCancelar());
                passwordFirst = Tools.scanner.nextLine();
                if (Tools.verificarSaida(passwordFirst)) return;
                if (!passwordFirst.isEmpty()) {
                    System.out.print("Repita a Password: ");
                    passwordSecound = Tools.scanner.nextLine();
                    respVerificarPassword = utilizadorController.verificarPassword(passwordFirst, passwordSecound);
                    if (!respVerificarPassword)
                        System.out.println("Erro, passwords não coincidem, tente novamente...\n");
                } else break;
            } while (!respVerificarPassword);

            ResultadoOperacao resultado = utilizadorController.verificarDados(utilizador, nome, utilizador.getEmail(), nascimento, morada, passwordFirst, passwordSecound);
            if (resultado.Sucesso) System.out.println("Cliente alterado com sucesso");
            else System.out.println(resultado.msgErro);
            break;
        }
    }

    public void mostrarUtilizador(int estado, int tipo) {
        UtilizadorController utilizadorController = new UtilizadorController();
        utilizadorController.mostrarUtilizador(estado, tipo);
    }

    public void exibirUtilizadores(List<Utilizador> utilizadores) {
        System.out.println("\n" + "=".repeat(20) + " LISTAGEM DE UTILIZADORES " + "=".repeat(20));
        System.out.printf(
                "%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-25s %-10s %-10s\n",
                "Id", "Nome Utilizador", "Email", "Data Nascimento", "Morada",
                "Data Registo", "Último Login", "Tipo Utilizador", "Estado", "Saldo (€)"
        );
        System.out.println("-".repeat(250));

        for (Utilizador utilizador : utilizadores) {
            String estadoStr = Tools.estadoUtilizador.fromCodigo(utilizador.getEstado()).name();
            String tipoUtilizadorStr = Tools.tipoUtilizador.fromCodigo(utilizador.getTipoUtilizador()).name();

            String dataNascimento = utilizador.getDataNascimento() != null ? utilizador.getDataNascimento().toString() : "N/A";
            String morada = utilizador.getMorada() != null ? utilizador.getMorada() : "N/A";
            String dataRegisto = utilizador.getDataRegisto() != null ? utilizador.getDataRegisto().toString() : "N/A";
            String ultimoLogin = utilizador.getUltimoLogin() != null ? utilizador.getUltimoLogin().toString() : "N/A";

            System.out.printf(
                    "%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-25s %-10s %8.2f€\n",
                    utilizador.getId(),
                    utilizador.getNomeUtilizador(),
                    utilizador.getEmail(),
                    dataNascimento,
                    morada,
                    dataRegisto,
                    ultimoLogin,
                    tipoUtilizadorStr,
                    estadoStr,
                    utilizador.getSaldo()
            );

        }
    }

    public void verDadosCliente(Utilizador utilizador) {
        System.out.println("\n" + "=".repeat(10) + " FICHA CLIENTE " + "=".repeat(10));

        String tipoUtilizadorStr = Tools.tipoUtilizador.fromCodigo(utilizador.getTipoUtilizador()).name();

        System.out.println("Nome: " + utilizador.getNomeUtilizador());
        System.out.println("Email: " + utilizador.getEmail());
        System.out.println("Data de Nascimento: " + Tools.formatDate(utilizador.getDataNascimento()));
        System.out.println("Morada: " + utilizador.getMorada());
        System.out.println("Data de Registo: " + Tools.formatDate(utilizador.getDataRegisto()));
        System.out.println("Último Login: " + Tools.formatDate(utilizador.getUltimoLogin()));
        System.out.println("Tipo de Utilizador: " + tipoUtilizadorStr);
        System.out.printf("Saldo atual: %.2f€\n", utilizador.getSaldo());
        System.out.println("=".repeat(35));
    }

    public void verificarLoginsUtilizadores() throws MessagingException, IOException {
        UtilizadorController utilizadorController = new UtilizadorController();
        utilizadorController.verificarLoginsUtilizadores();
    }

    public Utilizador procurarUtilizadorPorEmail(String email) {
        UtilizadorController utilizadorController = new UtilizadorController();
        return utilizadorController.procurarUtilizadorPorEmail(email);
    }

    //IMPORTAR UTILIZADORES BY FICHEIRO - PP
    public UtilizadorBLL.ResultadoImportacao importar() {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        return utilizadorBLL.importarUtilizadores();
    }

}
