package View;

import BLL.AgenteBLL;
import Controller.AgenteController;
import Controller.LanceController;
import Model.Agente;
import Model.Leilao;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AgenteView {
    AgenteController agenteController = new AgenteController();
    private final Scanner scanner = new Scanner(System.in);

    public void menuAgente() throws MessagingException, IOException {
        while (true) {
            System.out.println("\n--- Menu Agente ---");
            System.out.println("1. Criar Agente");
            System.out.println("2. Eliminar Agente");
            System.out.println("3. Listar Agentes");
            System.out.println("0. Sair");

            System.out.print("Escolha uma opção: ");
            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
                continue;
            }

            switch (opcao) {
                case 1:
                    criarAgente();
                    break;
                case 2:
                    eliminarAgente();
                    break;
                case 3:
                    listarAgentes();
                    break;
                case 0:
                    System.out.println("Saindo do menu de Agentes.");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void criarAgente() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        AgenteBLL agenteBLL = new AgenteBLL();

        System.out.println("\n===== CONFIGURAR AGENTE EM LEILÕES ELETRÔNICOS =====");

        List<Leilao> leiloesEletronicos = agenteBLL.obterLeiloesDisponiveisParaCliente(Tools.clienteSessao.getIdCliente());

        if (leiloesEletronicos.isEmpty()) {
            System.out.println("Não existem leilões disponíveis do tipo Eletrônico.\n");
            return;
        }

        for (Leilao leilao : leiloesEletronicos) {
            System.out.println("ID: " + leilao.getId() +
                    " | Produto: " + leilao.getDescricao() +
                    " | Valor do Múltiplo de Lance: " + leilao.getMultiploLance());
        }

        try {
            System.out.print("\nInsira o ID do leilão para configurar o agente " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();

            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;

            boolean leilaoDisponivel = lanceController.verificarDisponibilidadeLeilao(leiloesEletronicos, idLeilao);

            if (!leilaoDisponivel) {
                System.out.println("Leilão indisponível ou inválido!");
                return;
            }

            int idCliente = Tools.clienteSessao.getIdCliente();

            boolean jaExiste = agenteController.listarAgentes().stream()
                    .anyMatch(a -> a.getClienteId() == idCliente && a.getLeilaoId() == idLeilao);

            if (jaExiste) {
                System.out.println("Já existe um agente configurado para este leilão.");
                return;
            }

            int idAgente = agenteController.obterProximoId();
            Agente novoAgente = new Agente(idAgente, idCliente, idLeilao, LocalDateTime.now());

            boolean sucesso = agenteController.criarAgente(novoAgente);
            if (sucesso) {
                System.out.println("Agente criado com sucesso!");
            } else {
                System.out.println("Falha ao criar agente.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Insira um número válido.");
            Tools.scanner.nextLine();
        }
    }

    private void eliminarAgente() {
        try {
            int idCliente = Tools.clienteSessao.getIdCliente();
            List<Agente> todosAgentes = agenteController.listarAgentes();
            List<Agente> agentesDoCliente = new ArrayList<>();

            for (Agente agente : todosAgentes) {
                if (agente.getClienteId() == idCliente) {
                    agentesDoCliente.add(agente);
                }
            }

            if (agentesDoCliente.isEmpty()) {
                System.out.println("Não tem agentes configurados.");
                return;
            }

            System.out.println("Os seus Agentes:");
            for (Agente agente : agentesDoCliente) {
                String dataFormatada = Tools.formatDateTime(agente.getDataConfiguracao());
                System.out.printf("Leilão ID: %d | Agente ID: %d | Data Configuração: %s%n",
                        agente.getLeilaoId(), agente.getId(), dataFormatada);
            }

            System.out.print("Escolha o ID do leilão do agente que deseja eliminar: " + Tools.alertaCancelar());
            String entrada = scanner.nextLine();

            if (Tools.verificarSaida(entrada)) return;

            int idLeilaoEscolhido;
            try {
                idLeilaoEscolhido = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido.");
                return;
            }

            Agente agenteParaEliminar = null;
            for (Agente agente : agentesDoCliente) {
                if (agente.getLeilaoId() == idLeilaoEscolhido) {
                    agenteParaEliminar = agente;
                    break;
                }
            }

            if (agenteParaEliminar == null) {
                System.out.println("ID do leilão inválido.");
                return;
            }

            System.out.print("Tem a certeza que quer eliminar este agente? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                System.out.println("Eliminação cancelada.");
                return;
            }

            boolean sucesso = agenteController.eliminarAgente(idLeilaoEscolhido, idCliente);
            if (sucesso) {
                System.out.println("Agente eliminado com sucesso!");
            } else {
                System.out.println("Falha ao eliminar agente.");
            }

        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado. Tente novamente.");
        }
    }

    private void listarAgentes() {
        System.out.println("\n--- Os Seus Agentes ---");

        int idCliente = Tools.clienteSessao.getIdCliente();
        List<Agente> agentes = agenteController.listarAgentes();

        List<Agente> agentesDoCliente = new ArrayList<>();
        for (Agente agente : agentes) {
            if (agente.getClienteId() == idCliente) {
                agentesDoCliente.add(agente);
            }
        }

        if (agentesDoCliente.isEmpty()) {
            System.out.println("Não tem nenhum agente configurado.");
            return;
        }

        for (Agente agente : agentesDoCliente) {
            String dataFormatada = Tools.formatDateTime(agente.getDataConfiguracao());
            System.out.printf("LEILAO ID: %d | AGENTE ID: %d | Data Configuração: %s%n",
                    agente.getLeilaoId(),
                    agente.getId(),
                    dataFormatada);
        }
    }


}
