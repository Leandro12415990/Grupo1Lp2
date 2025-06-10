package View;

import BLL.LanceBLL;
import Controller.LanceController;
import Controller.NegociacaoController;
import DAL.NegociacaoDAL;
import Model.Lance;
import Model.Negociacao;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NegociacaoView {

    private final NegociacaoController negociacaoController = new NegociacaoController();
    private final Scanner scanner = new Scanner(System.in);

    public void exibirMenu() throws IOException, MessagingException {
        LeilaoView leilaoView = new LeilaoView();
        while (true) {
            System.out.println("\n===== MENU CLIENTE LEILÃO/NEGOCIAÇÃO =====");
            System.out.println("1. Criar Leilão");
            System.out.println("2. Ver os Meus Leilões");
            System.out.println("3. Ver as minhas Propostas");
            System.out.println("4. Editar os Meus Leilões");
            System.out.println("5. Remover Leilão");
            System.out.println("6. Ver Propostas Recebidas e Renegociar");
            System.out.println("0. Sair");

            System.out.print("Escolha a opção: ");
            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    leilaoView.criarLeilaoOuNegociacao();
                    break;
                case "2":
                    verNegociacoes();
                    break;
                case "3":
                    listarMeuLanceNegociacao();
                    break;
                case "4":
                    editarNegociacao();
                    break;
                case "5":
                    fecharNegociacao();
                    break;
                case "6":
                    gerirPropostas();
                    break;
                case "0":
                    System.out.println("A sair...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void criarNegociacao() {
        System.out.println("\n--- Criar Novo Leilão ---");

        System.out.print("Nome do Leilão: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        double valor = 0.0;
        while (true) {
            System.out.print("Valor: ");
            String valorStr = scanner.nextLine().trim();
            try {
                valor = Double.parseDouble(valorStr);
                if (valor <= 0) {
                    System.out.println("O valor deve ser maior que zero.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Insira um número válido.");
            }
        }

        int idCliente = Tools.clienteSessao.getIdCliente();

        ResultadoOperacao resultado = negociacaoController.criarNegociacao(idCliente, nome, descricao, valor);
        if (resultado.Sucesso) {
            System.out.println("Leilão criado com sucesso!");
        } else {
            System.out.println("Erro ao criar Leilão: " + resultado.msgErro);
        }
    }

    private void verNegociacoes() {
        System.out.println("\n--- Meus Leilões ---");

        int idCliente = Tools.clienteSessao.getIdCliente();
        List<Negociacao> negociacoes = negociacaoController.listarNegociacoesPorCliente(idCliente);

        if (negociacoes.isEmpty()) {
            System.out.println("Nenhum Leilão encontrado.");
        } else {
            for (Negociacao negociacao : negociacoes) {
                System.out.println("Nome: " + negociacao.getNome());
                System.out.println("Descrição: " + negociacao.getDescricao());
                System.out.println("Valor: " + negociacao.getValor());
                System.out.println("Data Início: " + Tools.formatDateTime(negociacao.getDataInicio()));
                System.out.println("Estado: " + negociacao.getEstado());
                System.out.println("------------------------------");
            }
        }
    }

    private void editarNegociacao() {
        verNegociacoes();
        System.out.println("\n--- Editar Leilão ---");
        System.out.print("ID do Leilão a editar: ");
        int idNegociacao = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Novo Nome (deixe vazio para manter): ");
        String nome = scanner.nextLine().trim();

        System.out.print("Nova Descrição (deixe vazio para manter): ");
        String descricao = scanner.nextLine().trim();

        double valor = -1.0;
        System.out.print("Novo Valor (deixe vazio para manter): ");
        String valorInput = scanner.nextLine().trim();
        if (!valorInput.isEmpty()) {
            try {
                valor = Double.parseDouble(valorInput);
                if (valor <= 0) {
                    System.out.println("Valor inválido. Deve ser maior que zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Operação cancelada.");
                return;
            }
        }

        int idCliente = Tools.clienteSessao.getIdCliente();
        ResultadoOperacao resultado = negociacaoController.editarNegociacao(
                idNegociacao,
                idCliente,
                nome.isEmpty() ? null : nome,
                descricao.isEmpty() ? null : descricao,
                valor
        );

        if (resultado.Sucesso) {
            System.out.println("Leilão atualizado com sucesso.");
        } else {
            System.out.println("Erro ao editar Leilão: " + resultado.msgErro);
        }
    }

    private void fecharNegociacao() {
        System.out.println("\n--- Fechar Leilão ---");

        int idCliente = Tools.clienteSessao.getIdCliente();
        List<Negociacao> todasNegociacoes = negociacaoController.listarNegociacoesPorCliente(idCliente);
        List<Negociacao> leiloesAtivos = new ArrayList<>();

        for (Negociacao negociacao : todasNegociacoes) {
            if (negociacao.getEstado() == 1) {
                leiloesAtivos.add(negociacao);
            }
        }

        if (leiloesAtivos.isEmpty()) {
            System.out.println("Não tens leilões ativos para fechar.");
            return;
        }

        System.out.println("Leilões Ativos:");
        for (Negociacao negociacao : leiloesAtivos) {
            System.out.println("ID: " + negociacao.getId());
            System.out.println("Nome: " + negociacao.getNome());
            System.out.println("Valor: " + negociacao.getValor());
            System.out.println("Data Início: " + Tools.formatDateTime(negociacao.getDataInicio()));
            System.out.println("------------------------------");
        }

        System.out.print("Digite o ID do leilão que queres fechar: ");
        int idNegociacao = Integer.parseInt(scanner.nextLine().trim());

        ResultadoOperacao resultado = negociacaoController.fecharNegociacao(idNegociacao, idCliente);

        if (resultado.Sucesso) {
            System.out.println("Leilão fechado com sucesso.");
        } else {
            System.out.println("Erro ao fechar Leilão: " + resultado.msgErro);
        }
    }

    public void listarMeuLanceNegociacao() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        List<Lance> meusLances = lanceController.listarLancesDoCliente();

        List<Lance> lancesProposta = new ArrayList<>();
        for (Lance lance : meusLances) {
            if (lance.getEstado() == Constantes.estadosLance.PROPOSTA || lance.getEstado() == Constantes.estadosLance.CONTRAPROPOSTA) {
                lancesProposta.add(lance);
            }
        }

        if (lancesProposta.isEmpty()) {
            System.out.println("Nenhum lance encontrado para o cliente.");
        } else {
            System.out.println("\nOs Seus Lances:");
            System.out.println("-".repeat(130));
            System.out.printf("%-20s %-15s %-25s %-25s%n", "ID do Lance", "Nome do Leilão", "Valor Proposta (€)", "Data");
            System.out.println("-".repeat(130));
            for (Lance lance : lancesProposta) {
                String dataFormatada = Tools.formatDateTime(lance.getDataLance());
                System.out.printf("%-20s %-15s %-25s %-25s%n", lance.getIdLance(), lance.getIdNegociacao(), lance.getValorLance(), dataFormatada);
            }
        }
    }

    public void gerirPropostas() throws MessagingException, IOException {
        int idClienteSessao = Tools.clienteSessao.getIdCliente();

        LanceBLL lanceBLL = new LanceBLL();
        List<Lance> todosLances = lanceBLL.carregarLances();
        NegociacaoDAL negociacaoDAL = new NegociacaoDAL();
        List<Negociacao> leilao = negociacaoDAL.carregarNegociacoes();

        NegociacaoController negociacaoController = new NegociacaoController();
        List<Negociacao> meusLeiloes = negociacaoController.listarNegociacoesPorCliente(idClienteSessao);

        List<Lance> lancesFiltrados = new ArrayList<>();
        boolean estouComoVendedor = false;

        for (Lance l : todosLances) {
            for (Negociacao n : meusLeiloes) {
                // Vendedor
                if (l.getIdNegociacao() == n.getId() &&
                        l.getIdCliente() != idClienteSessao &&
                        l.getEstado() == Constantes.estadosLance.PROPOSTA) {

                    lancesFiltrados.add(l);
                    estouComoVendedor = true;
                }
            }

            // Comprador
            if (l.getIdCliente() == idClienteSessao &&
                    l.getEstado() == Constantes.estadosLance.CONTRAPROPOSTA) {

                Negociacao n = negociacaoController.buscarNegociacaoPorId(l.getIdNegociacao());
                if (n != null && n.getEstado() == Constantes.estadosLeilao.ATIVO) {
                    lancesFiltrados.add(l);
                }
            }

        }


        System.out.println(estouComoVendedor ? "\n--- Propostas Recebidas ---" : "\n--- Contrapropostas Recebidas ---");
        System.out.println("-".repeat(130));
        System.out.printf("%-15s %-15s %-20s %-20s %-20s %-20s %-25s%n",
                "ID Lance", "Leilão", "ID Cliente", "Valor Inicial", "Proposta", "ContraProposta", "Data");
        System.out.println("-".repeat(130));

        for (Lance l : lancesFiltrados) {
            Negociacao negociacao = negociacaoController.buscarNegociacaoPorId(l.getIdNegociacao());
            if (negociacao == null) continue;
            String nomeCliente = negociacaoController.obterNomeClientePorId(l.getIdCliente());
            System.out.printf("%-15d %-15s %-20s %-20s %-20.2f %-20.2f %-25s%n",
                    l.getIdLance(), negociacao.getNome(), nomeCliente, negociacao.getValor(),
                    l.getValorLance(), l.getValorContraProposta(), Tools.formatDateTime(l.getDataLance()));
        }

        System.out.print("\nDigite o ID do lance que deseja gerir" + Tools.alertaCancelar());
        int idLanceSelecionado;
        try {
            idLanceSelecionado = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        if (idLanceSelecionado == 0) return;

        LanceController lanceController = new LanceController();
        Lance lanceSelecionado = lanceController.buscarLancePorId(idLanceSelecionado);

        if (lanceSelecionado == null) {
            System.out.println("Lance não encontrado.");
            return;
        }

        Negociacao negociacao = negociacaoController.buscarNegociacaoPorId(lanceSelecionado.getIdNegociacao());

        if (negociacao == null) {
            System.out.println("Leilão associado não encontrado.");
            return;
        }

        if (negociacao.getIdCliente() == idClienteSessao) {
            System.out.println("1. Aceitar proposta");
            System.out.println("2. Recusar proposta");
            System.out.println("3. Fazer contraproposta");
            System.out.println("0. Voltar");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    ResultadoOperacao resAceitar = negociacaoController.fecharNegociacaoComLanceAceito(
                            negociacao.getId(),
                            lanceSelecionado.getValorLance(),
                            lanceSelecionado.getIdCliente()
                    );
                    System.out.println(resAceitar.Sucesso ? "Proposta aceite e leilão fechado." : "Erro: " + resAceitar.msgErro);
                    break;

                case "2":
                    ResultadoOperacao resRecusar = lanceController.recusarLance(idLanceSelecionado);
                    System.out.println(resRecusar.Sucesso ? "Proposta recusada." : "Erro: " + resRecusar.msgErro);
                    break;

                case "3":
                    System.out.print("Digite o valor da contraproposta: ");
                    double novoValor;
                    try {
                        novoValor = Double.parseDouble(scanner.nextLine().trim());
                        if (novoValor <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido.");
                        return;
                    }

                    ResultadoOperacao resultadoContra = lanceController.definirContraproposta(
                            idLanceSelecionado, novoValor
                    );

                    System.out.println(resultadoContra.Sucesso ? "Contraproposta enviada." : "Erro: " + resultadoContra.msgErro);
                    break;
                case "0":
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } else if (lanceSelecionado.getIdCliente() == idClienteSessao) {
            // Comprador
            if (lanceSelecionado.getValorContraProposta() <= 0) {
                System.out.println("Ainda não há contraproposta do vendedor.");
                return;
            }

            System.out.println("1. Aceitar contraproposta");
            System.out.println("2. Recusar contraproposta");
            System.out.println("3. Fazer nova proposta");
            System.out.println("0. Voltar");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    ResultadoOperacao aceitar = negociacaoController.fecharNegociacaoComLanceAceito(
                            negociacao.getId(),
                            lanceSelecionado.getValorContraProposta(),
                            idClienteSessao
                    );
                    System.out.println(aceitar.Sucesso ? "Contraproposta aceite. Leilão fechado." : "Erro: " + aceitar.msgErro);
                    break;

                case "2":
                    ResultadoOperacao recusar = lanceController.recusarLance(idLanceSelecionado);
                    System.out.println(recusar.Sucesso ? "Contraproposta recusada." : "Erro: " + recusar.msgErro);
                    break;

                case "3":
                    System.out.print("Digite o novo valor da proposta: ");
                    double novaProposta;
                    try {
                        novaProposta = Double.parseDouble(scanner.nextLine().trim());
                        if (novaProposta <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        System.out.println("Valor inválido.");
                        return;
                    }

                    ResultadoOperacao resultadoNovaProposta = lanceController.atualizarValorLance(
                            idLanceSelecionado, novaProposta
                    );

                    System.out.println(resultadoNovaProposta.Sucesso ?
                            "Nova proposta enviada." :
                            "Erro ao enviar nova proposta: " + resultadoNovaProposta.msgErro);
                    break;
                case "0":
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } else {
            System.out.println("Não tens permissões para gerir este lance.");
        }
    }


}
