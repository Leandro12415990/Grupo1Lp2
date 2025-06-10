package View;

import Controller.LanceController;
import Controller.LeilaoController;
import Controller.NegociacaoController;
import Controller.ProdutoController;
import Model.Leilao;
import Model.Produto;
import Model.ResultadoOperacao;
import Utils.Constantes.estadosLeilao;
import Utils.Constantes.tiposLeilao;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class LeilaoView {
    public void exibirMenuLeiloes() throws MessagingException, IOException {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LEILÕES " + "=".repeat(5));
            System.out.println("1. Adicionar Leilão");
            System.out.println("2. Editar Leilão");
            System.out.println("3. Consultar Leilão");
            System.out.println("4. Eliminar Leilão");
            System.out.println("5. Listar Leilão");
            System.out.println("6. Fechar Leilão");
            System.out.println("0. Voltar ao menu principal...");
            opc = Tools.pedirInt("Escolha uma opção: ");
            switch (opc) {
                case 1 -> criarLeilaoOuNegociacao();
                case 2 -> editarLeilao();
                case 3 -> procurarLeilao();
                case 4 -> eliminarLeilao();
                case 5 -> listaLeiloes(Tools.estadoLeilao.DEFAULT);
                case 6 -> fecharLeilaoManual();
                case 0 -> System.out.println("\nSair...");
                default -> System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public void criarLeilaoOuNegociacao() throws MessagingException, IOException {
        ProdutoView produtoView = new ProdutoView();
        ProdutoController produtoController = new ProdutoController();
        LeilaoController leilaoController = new LeilaoController();
        NegociacaoController negociacaoController = new NegociacaoController();
        boolean isGestor = Tools.clienteSessao.getIdTipoCliente() == Tools.tipoUtilizador.GESTOR.getCodigo();
        boolean isCliente = Tools.clienteSessao.getIdTipoCliente() == Tools.tipoUtilizador.CLIENTE.getCodigo();

        if (!isCliente && !isGestor) {
            System.out.println("Erro: Sessão inválida. Nenhum utilizador autenticado.");
            return;
        }
        System.out.println("\nCRIAÇÃO DE UM " + (isCliente ? "LEILÃO POR NEGOCIAÇÃO" : "LEILÃO") + "\n");
        int idProduto = 0;
        if (isGestor) {
            List<Produto> produtosDisponiveis = produtoController.listarProduto(true);
            if (produtosDisponiveis.isEmpty()) {
                System.out.println("Não existem produtos disponíveis.");
                return;
            }

            produtoView.exibirProduto(produtosDisponiveis);
            idProduto = Tools.pedirOpcaoMenu("\nIntroduza o ID do produto que pretende leiloar" + Tools.alertaCancelar());
            if (Tools.verificarSaida(String.valueOf(idProduto))) return;
            Tools.scanner.nextLine();

            ResultadoOperacao isAvailable = leilaoController.verificarDisponibilidadeProduto(idProduto);
            if (!isAvailable.Sucesso) {
                System.out.println("\n" + isAvailable.msgErro);
                return;
            }
        }
        String nome = null;
        if (isCliente) {
            System.out.print("Insira o nome do produto " + Tools.alertaCancelar());
            nome = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(nome)) return;
        }

        System.out.print("Insira a descrição do leilão " + Tools.alertaCancelar());
        String descricao = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(descricao)) return;

        int idTipoLeilao = -1;
        if (isGestor) {
            do {
                System.out.println("\n1. Leilão Eletrónico");
                System.out.println("2. Leilão Carta Fechada");
                System.out.println("3. Leilão Venda Direta");
                idTipoLeilao = Tools.pedirOpcaoMenu("Escolha o tipo de leilão " + Tools.alertaCancelar());
                if (Tools.verificarSaida(String.valueOf(idTipoLeilao))) return;
                if (idTipoLeilao < 1 || idTipoLeilao > 3)
                    System.out.println("Opção inválida. Tente novamente...");
            } while (idTipoLeilao < 1 || idTipoLeilao > 3);
        } else if (isCliente) {
            idTipoLeilao = tiposLeilao.NEGOCIACAO; // = 4
        }

        Tools.scanner.nextLine();

        // CLIENTE: fluxo de negociação
        if (idTipoLeilao == tiposLeilao.NEGOCIACAO) {
            double valor = 0.0;
            while (true) {
                System.out.print("Valor pretendido: ");
                String valorStr = Tools.scanner.nextLine().trim();
                if (Tools.verificarSaida(valorStr)) return;
                try {
                    valor = Double.parseDouble(valorStr);
                    if (valor <= 0) {
                        System.out.println("O valor deve ser maior que zero.");
                    } else break;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Insira um número válido.");
                }
            }

            int idCliente = Tools.clienteSessao.getIdCliente();
            ResultadoOperacao resultado = negociacaoController.criarNegociacao(idCliente, nome, descricao, valor);
            if (resultado.Sucesso) {
                System.out.println("Negociação criada com sucesso!");
            } else {
                System.out.println("Erro ao criar negociação: " + resultado.msgErro);
            }
            return;
        }

        // GESTOR: fluxo de leilão comum
        LocalDateTime dataInicio;
        LocalDateTime dataFim = null;

        while (true) {
            System.out.print("\nInsira a data de início (dd/MM/yyyy hh:mm:ss) " + Tools.alertaCancelar());
            String dataInicioStr = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(dataInicioStr)) return;
            dataInicio = Tools.parseDateTime(dataInicioStr);
            if (dataInicio != null) break;
            System.out.println("Formato de data inválido.");
        }

        while (true) {
            System.out.print("Insira a data de fim do leilão (dd/MM/yyyy hh:mm:ss) ou ENTER para não definir " + Tools.alertaCancelar());
            String dataFimStr = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(dataFimStr)) return;
            if (dataFimStr.isEmpty()) break;

            dataFim = Tools.parseDateTime(dataFimStr);
            if (dataFim != null) {
                ResultadoOperacao isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                if (isCorrect.Sucesso) break;
                else System.out.println(isCorrect.msgErro);
            } else System.out.println("Formato inválido.");
        }

        double valorMin = 0.0, valorMax = 0.0;

        if (idTipoLeilao == tiposLeilao.VENDA_DIRETA) {
            System.out.print("Insira o valor pretendido: ");
            valorMin = Tools.scanner.nextDouble();
        } else {
            System.out.print("Insira o valor mínimo: ");
            valorMin = Tools.scanner.nextDouble();

            while (true) {
                System.out.print("Insira o valor máximo ou ENTER para não definir " + Tools.alertaCancelar());
                String valorMaxStr = Tools.scanner.nextLine().trim();
                if (Tools.verificarSaida(valorMaxStr)) return;
                if (valorMaxStr.isEmpty()) break;

                try {
                    valorMax = Double.parseDouble(valorMaxStr);
                    if (valorMax < valorMin) {
                        System.out.printf("O valor máximo não pode ser inferior ao mínimo (%.2f).\n", valorMin);
                    } else break;
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido.");
                }
            }
        }

        double multiploLance = 0.0;
        if (idTipoLeilao == tiposLeilao.ELETRONICO) {
            System.out.print("Insira o valor de cada lance: ");
            multiploLance = Tools.scanner.nextDouble();
        }

        int idEstado = leilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, estadosLeilao.ATIVO);

        ResultadoOperacao resultado = leilaoController.criarLeiloes(0, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
        if (resultado.Sucesso) {
            produtoController.atualizarEstadoProduto(idProduto, 2);
            System.out.println("\nLeilão criado com sucesso!");
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    public void listaLeiloes(Tools.estadoLeilao estado) throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        List<Leilao> leiloesList = leilaoController.listarLeiloes(estado);
        exibirLeiloes(leiloesList);
    }

    public void exibirLeiloes(List<Leilao> leiloes) throws MessagingException, IOException {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE LEILÕES " + "=".repeat(5));
        System.out.println("No caso dos leilões de Venda Direta apenas existe um valor!");
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                "Id", "Produto", "Descrição", "TipoLeilão", "Data Início", "Data Fim", "Valor Minimo/Valor", "Valor Maximo", "Multiplo de Lance", "Estado");
        System.out.println("-".repeat(260));
        for (Leilao leilao : leiloes) {
            String estadoStr = Tools.estadoLeilao.fromCodigo(leilao.getEstado()).name();
            String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(leilao.getIdTipoLeilao()).name();
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                    leilao.getId(),
                    nomeProduto(leilao.getIdProduto()),
                    leilao.getDescricao(),
                    tipoLeilaoStr,
                    leilao.getDataInicio() != null ? (leilao.getIdTipoLeilao() == tiposLeilao.ELETRONICO ?
                            Tools.DATA_HORA.format(leilao.getDataInicio()) :
                            Tools.FORMATTER.format(leilao.getDataInicio())) : "N/A",
                    leilao.getDataFim() != null ? (leilao.getIdTipoLeilao() == tiposLeilao.ELETRONICO ?
                            Tools.DATA_HORA.format(leilao.getDataFim()) :
                            Tools.FORMATTER.format(leilao.getDataFim())) : "N/A",
                    leilao.getValorMinimo(),
                    leilao.getValorMaximo() != null && leilao.getValorMaximo() != 0.0 ? leilao.getValorMaximo() : "N/A",
                    leilao.getMultiploLance() != null && leilao.getMultiploLance() != 0 ? leilao.getMultiploLance() : "N/A",
                    estadoStr);
        }
    }

    private void procurarLeilao() throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        System.out.println("\nPROCURAR UM LEILÃO");
        listaLeiloes(Tools.estadoLeilao.DEFAULT);
        int id = Tools.pedirOpcaoMenu("\nIntroduza o ID do Leilão que pretende consultar " + Tools.alertaCancelar());
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = leilaoController.procurarLeilaoPorId(id);

        if (leilao != null) exibirLeilaoDetalhado(leilao);
        else System.out.println("O Leilão com o Id " + id + " não foi encontrado.");
    }

    private void exibirLeilaoDetalhado(Leilao leilao) throws MessagingException, IOException {
        String estadoStr = Tools.estadoLeilao.fromCodigo(leilao.getEstado()).name();
        String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(leilao.getIdTipoLeilao()).name();
        System.out.println("\n- DETALHES DO LEILÃO COM O ID " + leilao.getId() + " -");
        System.out.println("Produto: " + nomeProduto(leilao.getIdProduto()));
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tipo Leilão: " + tipoLeilaoStr);
        System.out.println("Data Início: " + Tools.FORMATTER.format(leilao.getDataInicio()));
        System.out.println("Data Fim: " + (leilao.getDataFim() != null ? Tools.FORMATTER.format(leilao.getDataFim()) : "N/A"));
        System.out.println("Valor Minimo: " + leilao.getValorMinimo());
        System.out.println("Valor Máximo: " + (leilao.getValorMaximo() != null && leilao.getValorMaximo() != -1.0 ? leilao.getValorMaximo() : "N/A"));
        System.out.println("Múltiplo de Lance: " + (leilao.getMultiploLance() != null && leilao.getMultiploLance() != 0 ? leilao.getMultiploLance() : "N/A"));
        System.out.println("Estado: " + estadoStr);
    }

    private void eliminarLeilao() throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        System.out.println("\nELIMINAÇÃO DE UM LEILÃO");
        listaLeiloes(Tools.estadoLeilao.DEFAULT);
        int id = Tools.pedirOpcaoMenu("\nIntroduza o ID do Leilão que pretende eliminar " + Tools.alertaCancelar());
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = leilaoController.procurarLeilaoPorId(id);
        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
            System.out.println("\nTem a certeza que pretende eliminar o leilão com o Id " + id + "? (S/N)");
            char opc = Character.toUpperCase(Tools.scanner.next().charAt(0));
            switch (opc) {
                case 'S':
                    boolean sucesso = leilaoController.eliminarLeilao(id);
                    if (sucesso) System.out.println("Leilão eliminado com sucesso.");
                    else System.out.println("Erro ao eliminar leilão.");
                    break;
                case 'N':
                    System.out.println("Eliminação cancelada.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } else System.out.println("Leilão não encontrado.");
    }

    private void editarLeilao() throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        ProdutoController produtoController = new ProdutoController();

        System.out.println("\nEDIÇÃO DE UM LEILÃO");
        listaLeiloes(Tools.estadoLeilao.DEFAULT);
        int id = Tools.pedirOpcaoMenu("\nIntroduza o ID do Leilão que pretende editar " + Tools.alertaCancelar());
        Tools.scanner.nextLine(); // limpar buffer
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = leilaoController.procurarLeilaoPorId(id);
        if (leilao == null) {
            System.out.println("Leilão não encontrado.");
            return;
        }

        exibirLeilaoDetalhado(leilao);
        System.out.println("\n--- Introduza os novos dados (pressione ENTER para manter o atual) ---");

        // Produto
        int idProduto = leilao.getIdProduto();
        List<Produto> produtosDisponiveis = produtoController.listarProduto(true);
        if (!produtosDisponiveis.isEmpty()) {
            System.out.print("Novo ID do produto ou ENTER para manter: " + Tools.alertaCancelar());
            String input = Tools.scanner.nextLine();
            if (Tools.verificarSaida(input)) return;
            if (!input.isEmpty()) {
                try {
                    int novoId = Integer.parseInt(input);
                    ResultadoOperacao disponivel = leilaoController.verificarDisponibilidadeProduto(novoId);
                    if (disponivel.Sucesso) idProduto = novoId;
                    else System.out.println(disponivel.msgErro + " Mantendo produto atual.");
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido. Mantendo produto atual.");
                }
            }
        }

        // Descrição
        System.out.print("Nova descrição " + Tools.alertaCancelar());
        String descricao = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(descricao)) return;
        if (descricao.isEmpty()) descricao = leilao.getDescricao();

        // Tipo Leilão
        int idTipoLeilao = leilao.getIdTipoLeilao();
        System.out.println("\n1. Leilão Eletrónico\n2. Carta Fechada\n3. Venda Direta");
        System.out.print("Novo tipo de leilão " + Tools.alertaCancelar());
        String tipoInput = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(tipoInput)) return;
        if (!tipoInput.isEmpty()) {
            try {
                int novoTipo = Integer.parseInt(tipoInput);
                if (novoTipo == tiposLeilao.ELETRONICO || novoTipo == tiposLeilao.CARTA_FECHADA || novoTipo == tiposLeilao.VENDA_DIRETA)
                    idTipoLeilao = novoTipo;
                else System.out.println("Tipo inválido. Mantendo o tipo atual.");
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Mantendo o tipo atual.");
            }
        }

        boolean isEletronico = idTipoLeilao == tiposLeilao.ELETRONICO;
        LocalDateTime dataInicio = leilao.getDataInicio();
        LocalDateTime dataFim = leilao.getDataFim();

        // Data Início
        while (true) {
            System.out.print("Nova data de início (dd/MM/yyyy hh:mm:ss) " + Tools.alertaCancelar());
            String dataInicioStr = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(dataInicioStr)) return;
            if (dataInicioStr.isEmpty()) break;

            dataInicio = Tools.parseDateTime(dataInicioStr);

            if (dataInicio == null) {
                System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy hh:mm:ss");
            } else {
                break;
            }
        }

        // Data Fim
        while (true) {
            System.out.print("Nova data de fim (dd/MM/yyyy hh:mm:ss) " + Tools.alertaCancelar());
            String dataFimStr = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(dataFimStr)) return;
            if (dataFimStr.isEmpty()) break;

            dataFim = isEletronico ? Tools.parseDateTime(dataFimStr) : Tools.parseDateTimeByDate(dataFimStr);
            if (dataFim != null) {
                ResultadoOperacao valid = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                if (valid.Sucesso) break;
                else System.out.println(valid.msgErro);
            } else System.out.println("Formato inválido.");
        }

        // Valores
        double valorMin = leilao.getValorMinimo();
        double valorMax = leilao.getValorMaximo();
        double multiploLance = leilao.getMultiploLance();

        if (idTipoLeilao == tiposLeilao.VENDA_DIRETA) {
            System.out.print("Novo valor de venda " + Tools.alertaCancelar());
        } else {
            System.out.print("Novo valor mínimo " + Tools.alertaCancelar());
        }

        String valorMinStr = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(valorMinStr)) return;
        if (!valorMinStr.isEmpty()) {
            try {
                double novoValorMin = Double.parseDouble(valorMinStr);
                if (novoValorMin >= 0) valorMin = novoValorMin;
                else System.out.println("Valor não pode ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Mantendo atual.");
            }
        }

        // Valor Máximo (opcional)
        System.out.print("Novo valor máximo " + Tools.alertaCancelar());
        String valorMaxStr = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(valorMaxStr)) return;
        if (!valorMaxStr.isEmpty()) {
            try {
                double novoValorMax = Double.parseDouble(valorMaxStr);
                if (novoValorMax >= valorMin) valorMax = novoValorMax;
                else System.out.println("Máximo deve ser maior ou igual ao mínimo.");
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Mantendo atual.");
            }
        }

        // Lance (apenas se eletrônico)
        if (idTipoLeilao == tiposLeilao.ELETRONICO) {
            System.out.print("Novo valor do lance " + Tools.alertaCancelar());
            String lanceStr = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(lanceStr)) return;
            if (!lanceStr.isEmpty()) {
                try {
                    double novoLance = Double.parseDouble(lanceStr);
                    if (novoLance > 0) multiploLance = novoLance;
                    else System.out.println("Lance deve ser maior que zero.");
                } catch (NumberFormatException e) {
                    System.out.println("Valor inválido. Mantendo atual.");
                }
            }
        }

        int idEstado = leilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, leilao.getEstado());
        boolean sucesso = leilaoController.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);

        if (sucesso) {
            if (leilao.getIdProduto() != idProduto) {
                produtoController.atualizarEstadoProduto(leilao.getIdProduto(), 1);
                produtoController.atualizarEstadoProduto(idProduto, 2);
            }
            System.out.println("Leilão editado com sucesso!");
        } else {
            System.out.println("Erro ao editar o leilão.");
        }
    }

    private String nomeProduto(int idProduto) throws MessagingException, IOException {
        ProdutoController produtoController = new ProdutoController();
        return produtoController.getNomeProdutoById(idProduto);
    }

    public void fecharLeilaoManual() throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        LanceController lanceController = new LanceController();

        System.out.println("\n===== LEILÕES ATIVOS =====");
        listaLeiloes(Tools.estadoLeilao.ATIVO);

        int inputId = Tools.pedirOpcaoMenu("\nInsira o ID do leilão que deseja fechar " + Tools.alertaCancelar());
        if (Tools.verificarSaida(String.valueOf(inputId))) return;
        Tools.scanner.nextLine();

        int idLeilao;
        try {
            idLeilao = inputId;
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        // Confirmação de encerramento
        System.out.print("Tem certeza que quer fechar o leilão? (S/N) " + Tools.alertaCancelar());
        String confirmInput = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(confirmInput) || confirmInput.isEmpty() || Character.toUpperCase(confirmInput.charAt(0)) != 'S')
            return;

        LocalDateTime dataFim = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.print("Quer usar a data de agora (" + dataFim.format(formatter) + ")? (S/N) " + Tools.alertaCancelar());
        String dataChoice = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(dataChoice)) return;

        if (!dataChoice.isEmpty() && Character.toUpperCase(dataChoice.charAt(0)) != 'S') {
            System.out.print("Insira a data que quer fechar o leilão (formato dd/MM/yyyy): " + Tools.alertaCancelar());
            String inputData = Tools.scanner.nextLine().trim();

            if (Tools.verificarSaida(inputData)) return;

            if (inputData.isEmpty()) {
                System.out.println("Data não pode ser vazia.");
                return;
            }

            LocalDateTime dataManual = Tools.parseDateTimeByDate(inputData);
            if (dataManual == null) {
                System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                return;
            }
            dataFim = dataManual.withHour(0).withMinute(0).withSecond(0).withNano(0);
        }

        ResultadoOperacao resultadoOperacao = leilaoController.fecharLeilao(idLeilao, dataFim);

        if (!resultadoOperacao.Sucesso) {
            System.out.println(resultadoOperacao.msgErro);
            return;
        }
        System.out.println("Leilão fechado com sucesso!");
        int lanceVencedor = lanceController.selecionarLanceVencedor(idLeilao);
        String nomeVencedor = lanceController.obterNomeVencedor(lanceVencedor);

        if (nomeVencedor != null) {
            System.out.println("O vencedor do Leilão é: " + nomeVencedor);
        } else {
            System.out.println("Não existem vencedores.");
        }
    }


}
