package View;

import Controller.LanceController;
import Controller.LeilaoController;
import Controller.ProdutoController;
import Model.Leilao;
import Model.Produto;
import Model.ResultadoOperacao;
import Utils.Constantes;
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
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    criarLeilao();
                    break;
                case 2:
                    editarLeilao();
                    break;
                case 3:
                    procurarLeilao();
                    break;
                case 4:
                    eliminarLeilao();
                    break;
                case 5:
                    listaLeiloes(Tools.estadoLeilao.DEFAULT);
                    break;
                case 6:
                    fecharLeilaoManual();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private void criarLeilao() throws MessagingException, IOException {
        ProdutoView produtoView = new ProdutoView();
        ProdutoController produtoController = new ProdutoController();
        LeilaoController leilaoController = new LeilaoController();

        System.out.println("\nCRIAÇÃO DE UM LEILÃO\n");
        List<Produto> produtosDisponiveis = produtoController.listarProduto(true);
        if (produtosDisponiveis.isEmpty()) {
            System.out.println("Não existem produtos disponiveis.");
        } else {
            produtoView.exibirProduto(produtosDisponiveis);

            System.out.print("\nIntroduza o ID do produto que pretende leiloar " + Tools.alertaCancelar());

            int idProduto = Tools.scanner.nextInt();
            Tools.scanner.nextLine();
            if (Tools.verificarSaida(String.valueOf(idProduto))) return;

            ResultadoOperacao isAvailable = leilaoController.verificarDisponibilidadeProduto(idProduto);
            if (isAvailable.Sucesso) {
                System.out.print("\nInsira a descrição do leilão " + Tools.alertaCancelar());
                String descricao = Tools.scanner.nextLine().trim();
                if (Tools.verificarSaida(descricao)) return;

                int idTipoLeilao;
                do {
                    System.out.println("\n1. Leilão Eletrónico");
                    System.out.println("2. Leilão Carta Fechada");
                    System.out.println("3. Leilão Venda Direta");
                    System.out.print("Escolha o tipo de leilão " + Tools.alertaCancelar());
                    idTipoLeilao = Tools.scanner.nextInt();
                    Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(String.valueOf(idTipoLeilao))) return;
                    if (idTipoLeilao != Constantes.tiposLeilao.ELETRONICO && idTipoLeilao != Constantes.tiposLeilao.CARTA_FECHADA && idTipoLeilao != Constantes.tiposLeilao.VENDA_DIRETA) {
                        System.out.println("Opção inválida. Tente novamente...");
                    }
                } while (idTipoLeilao < 1 || idTipoLeilao > 3);

                LocalDateTime dataInicio = null;
                LocalDateTime dataFim = null;
                if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                    while (true) {
                        System.out.print("\nInsira a data de início (dd/MM/yyyy HH:mm:SS) (-1 para cancelar): ");
                        String dataInicioStr = Tools.scanner.nextLine().trim();
                        if (Tools.verificarSaida(dataInicioStr)) return;
                        dataInicio = Tools.parseDateTime(dataInicioStr);

                        if (dataInicio != null) break;
                        else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm:SS.");
                    }
                    while (true) {
                        System.out.print("Insira a data de fim do leilão (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não definir " + Tools.alertaCancelar());
                        String dataFimStr = Tools.scanner.nextLine().trim();
                        if (Tools.verificarSaida(dataFimStr)) return;
                        if (!dataFimStr.isEmpty()) {
                            dataFim = Tools.parseDateTime(dataFimStr);

                            if (dataFim != null) {
                                ResultadoOperacao isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                                if (isCorrect.Sucesso) break;
                                else System.out.println(isCorrect.msgErro);
                            } else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm:SS.\n");
                        } else break;
                    }
                } else {
                    while (true) {
                        System.out.print("\nInsira a data de início (dd/MM/yyyy HH:mm:SS.) (-1 para cancelar): ");
                        String dataInicioStr = Tools.scanner.nextLine().trim();
                        if (Tools.verificarSaida(dataInicioStr)) return;
                        dataInicio = Tools.parseDateTimeByDate(dataInicioStr);

                        if (dataInicio != null) {
                            dataInicio = dataInicio.withHour(0).withMinute(0).withSecond(0).withNano(0);
                            break;
                        } else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm:SS.\n");
                    }

                    while (true) {
                        System.out.print("Insira a data de fim do leilão (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não definir " + Tools.alertaCancelar());
                        String dataFimStr = Tools.scanner.nextLine().trim();
                        if (Tools.verificarSaida(dataFimStr)) return;
                        if (!dataFimStr.isEmpty()) {
                            dataFim = Tools.parseDateTimeByDate(dataFimStr);

                            if (dataFim != null) {
                                dataFim = dataFim.withHour(0).withMinute(0).withSecond(0).withNano(0);
                                ResultadoOperacao isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                                if (isCorrect.Sucesso) break;
                                else System.out.println(isCorrect.msgErro);
                            } else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm:SS.\n");
                        } else break;
                    }
                }

                double valorMin = 0.0;
                double multiploLance = 0.0;
                // No caso de ser um Leilão Venda Direta, apenas pede um valor (estou a armazenar na variável valorMin)
                if (idTipoLeilao == Constantes.tiposLeilao.VENDA_DIRETA) {
                    while (true) {
                        System.out.print("Insira o valor pretendido:" + Tools.alertaCancelar());
                        String valorMinStr = Tools.scanner.nextLine().trim();
                        if (Tools.verificarSaida(valorMinStr)) return;
                        if (valorMinStr.isEmpty()) break;

                        try {
                            valorMin = Double.parseDouble(valorMinStr);
                            if (valorMin < 0) {
                                System.out.println("O valor não pode ser negativo. Tente novamente.");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Insira um número válido.");
                        }
                    }
                } else {
                    System.out.print("Insira o valor mínimo. " + Tools.alertaCancelar());
                    valorMin = Tools.scanner.nextDouble();
                    Tools.scanner.nextLine();
                    if (Tools.verificarSaida(String.valueOf(valorMin))) return;

                }

                double valorMax = 0;
                //Apenas aplica-se a leilões do tipo Eletrónico ([André] - o valorMax é o valor do lance)
                if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                    System.out.print("Insira o valor de cada lance " + Tools.alertaCancelar());
                    valorMax = Tools.scanner.nextDouble();
                    if (Tools.verificarSaida(String.valueOf(valorMax))) return;
                }
                int idEstado = leilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, Constantes.estadosLeilao.ATIVO);
                ResultadoOperacao resultado = leilaoController.criarLeiloes(0, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);

                if (resultado.Sucesso) {
                    produtoController.atualizarEstadoProduto(idProduto, 2);
                    System.out.println("Leilão criado com sucesso!");
                } else System.out.println(resultado.msgErro);
            } else System.out.println("\n" + isAvailable.msgErro);
        }
    }

    public List<Leilao> listaLeiloes(Tools.estadoLeilao estado) throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        List<Leilao> leiloesList = leilaoController.listarLeiloes(estado);
        exibirLeiloes(leiloesList);
        return leiloesList;
    }

    public void exibirLeiloes(List<Leilao> leiloes) throws MessagingException, IOException {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE LEILÕES " + "=".repeat(5));
        System.out.println("No caso dos leilões de Venda Direta apenas existe um valor!");
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                "Id", "Produto", "Descrição", "TipoLeilão", "Data Início", "Data Fim", "Valor Minimo/Valor", "Valor Maximo", "Multiplo de Lance", "Estado");
        System.out.println("-".repeat(260));
        for (Leilao leilao : leiloes) {
            String estadoStr = Tools.estadoLeilao.fromCodigo(leilao.getEstado()).name();
            String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(leilao.getTipoLeilao()).name();
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                    leilao.getId(),
                    nomeProduto(leilao.getIdProduto()),
                    leilao.getDescricao(),
                    tipoLeilaoStr,
                    leilao.getDataInicio() != null ? (leilao.getTipoLeilao() == Constantes.tiposLeilao.ELETRONICO ?
                            Tools.DATA_HORA.format(leilao.getDataInicio()) :
                            Tools.FORMATTER.format(leilao.getDataInicio())) : "N/A",
                    leilao.getDataFim() != null ? (leilao.getTipoLeilao() == Constantes.tiposLeilao.ELETRONICO ?
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
        System.out.print("\nIntroduza o ID do Leilão que pretende consultar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = leilaoController.procurarLeilaoPorId(id);

        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
        } else {
            System.out.println("O Leilão com o Id " + id + " não foi encontrado.");
        }
    }

    private void exibirLeilaoDetalhado(Leilao leilao) throws MessagingException, IOException {
        String estadoStr = Tools.estadoLeilao.fromCodigo(leilao.getEstado()).name();
        String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(leilao.getTipoLeilao()).name();
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
        System.out.print("\nIntroduza o ID do Leilão que pretende eliminar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
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
        System.out.print("\nIntroduza o ID do Leilão que pretende editar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = leilaoController.procurarLeilaoPorId(id);

        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
            System.out.println("\nIntroduza os novos dados");
            List<Produto> produtosDisponiveis = produtoController.listarProduto(true);
            int idProduto = leilao.getIdProduto();
            if (produtosDisponiveis.isEmpty()) {
                System.out.println("Não existem produtos disponíveis para leiloar.");
            } else {
                System.out.print("\nNovo ID do produto que pretende leiloar ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String input;

                while (true) {
                    input = Tools.scanner.nextLine();
                    if (Tools.verificarSaida(input)) return;
                    if (input.isEmpty()) break;

                    try {
                        idProduto = Integer.parseInt(input);

                        ResultadoOperacao isAvailable = leilaoController.verificarDisponibilidadeProduto(idProduto);
                        if (!isAvailable.Sucesso) {
                            System.out.println(isAvailable.msgErro);
                            System.out.print("Tente novamente ou pressione ENTER para não alterar: ");
                        } else break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido ou pressione Enter para manter o atual.");
                        System.out.print("Tente novamente: ");
                    }
                }
            }

            System.out.print("Nova descrição do leilão ou pressione ENTER para não alterar " + Tools.alertaCancelar());
            String descricao = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(descricao)) return;
            if (descricao.isEmpty()) descricao = leilao.getDescricao();

            int idTipoLeilao = leilao.getTipoLeilao();
            while (true) {
                System.out.println("\n1. Leilão Eletrónico");
                System.out.println("2. Leilão Carta Fechada");
                System.out.println("3. Leilão Venda Direta");
                System.out.print("Escolha o novo tipo de leilão ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String tipoInput = Tools.scanner.nextLine().trim();

                if (Tools.verificarSaida(tipoInput)) return;
                if (tipoInput.isEmpty()) break;

                try {
                    idTipoLeilao = Integer.parseInt(tipoInput);
                    if (idTipoLeilao != Constantes.tiposLeilao.ELETRONICO && idTipoLeilao != Constantes.tiposLeilao.CARTA_FECHADA && idTipoLeilao != Constantes.tiposLeilao.VENDA_DIRETA) {
                        System.out.println("Opção inválida. Tente novamente...");
                        idTipoLeilao = leilao.getTipoLeilao();
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Insira um número entre 0 e 3.");
                }
            }

            LocalDateTime dataInicio = leilao.getDataInicio();
            LocalDateTime dataFim = leilao.getDataFim();
            // No caso de ser um Leilão Eletronico, pede DateTime
            if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                while (true) {
                    System.out.print("\nNova data de início (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String dataInicioStr = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(dataInicioStr)) return;
                    if (dataInicioStr.isEmpty()) break;
                    dataInicio = Tools.parseDateTime(dataInicioStr);

                    if (dataInicio != null) break;
                    else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm.SS.");
                }
                while (true) {
                    ResultadoOperacao isCorrect;
                    System.out.print("Nova data de fim (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String dataFimStr = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(dataFimStr)) return;
                    if (!dataFimStr.isEmpty()) {
                        dataFim = Tools.parseDateTime(dataFimStr);

                        if (dataFim != null) {
                            isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                            if (isCorrect.Sucesso) break;
                            else System.out.println(isCorrect.msgErro);
                        } else {
                            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm.SS.\n");
                        }
                    } else {
                        if (dataFim != null) {
                            isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                            if (isCorrect.Sucesso) break;
                            else System.out.println(isCorrect.msgErro);
                        }
                        break;
                    }
                }
            } else {
                while (true) {
                    System.out.print("\nNova data de início (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String dataInicioStr = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(dataInicioStr)) return;
                    if (dataInicioStr.isEmpty()) break;
                    dataInicio = Tools.parseDateTimeByDate(dataInicioStr);

                    if (dataInicio != null) {
                        dataInicio = dataInicio.withHour(0).withMinute(0).withSecond(0).withNano(0);
                        break;
                    } else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm.SS");
                }

                while (true) {
                    ResultadoOperacao isCorrect;
                    System.out.print("Nova data de fim (dd/MM/yyyy HH:mm:SS) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String dataFimStr = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(dataFimStr)) return;
                    if (!dataFimStr.isEmpty()) {
                        dataFim = Tools.parseDateTimeByDate(dataFimStr);

                        if (dataFim != null) {
                            dataFim = dataFim.withHour(0).withMinute(0).withSecond(0).withNano(0);
                            isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                            if (isCorrect.Sucesso) break;
                            else System.out.println(isCorrect.msgErro);
                        } else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy HH:mm:SS.\n");
                    } else {
                        if (dataFim != null) {
                            isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                            if (isCorrect.Sucesso) break;
                            else System.out.println(isCorrect.msgErro);
                        }
                        break;
                    }
                }
            }
            double valorMin = leilao.getValorMinimo();
            Double valorMaxObj = leilao.getValorMaximo();
            double valorMax = valorMaxObj != null ? valorMaxObj : 0.0;

            // No caso de ser um Leilão Venda Direta, apenas pede um valor (estou a armazenar na variável valorMin)
            if (idTipoLeilao == Constantes.tiposLeilao.VENDA_DIRETA) {
                while (true) {
                    System.out.print("Novo valor ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String valorMinStr = Tools.scanner.nextLine().trim();

                    if (Tools.verificarSaida(valorMinStr)) return;
                    if (valorMinStr.isEmpty()) break;

                    try {
                        valorMin = Double.parseDouble(valorMinStr);
                        if (valorMin < 0) {
                            System.out.println("O valor não pode ser negativo. Tente novamente.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido.");
                    }
                }
            } else {
                valorMin = leilao.getValorMinimo();
                while (true) {
                    System.out.print("Novo valor mínimo ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String valorMinStr = Tools.scanner.nextLine().trim();

                    if (Tools.verificarSaida(valorMinStr)) return;
                    if (valorMinStr.isEmpty()) break;

                    try {
                        valorMin = Double.parseDouble(valorMinStr);
                        if (valorMin < 0) {
                            System.out.println("O valor mínimo não pode ser negativo. Tente novamente.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido.");
                    }
                }

            }

            double multiploLance = 0.0;
            if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                while (true) {
                    System.out.print("Novo valor do lance ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String novoValorLance = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(novoValorLance)) return;
                    if (novoValorLance.isEmpty()) break;

                    try {
                        valorMax = Double.parseDouble(novoValorLance);
                        if (valorMax <= 0) {
                            System.out.println("O valor do lance deve ser maior que zero. Tente novamente.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido.");
                    }
                }
            }

            int idEstado = leilao.getEstado();
            idEstado = leilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, idEstado);
            boolean sucesso = leilaoController.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);

            if (sucesso) {
                if (leilao.getIdProduto() != idProduto) {
                    produtoController.atualizarEstadoProduto(leilao.getIdProduto(), 1);
                    produtoController.atualizarEstadoProduto(idProduto, 2);
                }
                System.out.println("Leilão editado com sucesso!");
            } else {
                System.out.println("Erro ao editar o leilão!");
            }
        } else {
            System.out.println("Leilão não encontrado.");
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
        System.out.print("\nInsira o ID do leilão que deseja fechar: " + Tools.alertaCancelar());
        int idLeilao = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(idLeilao))) return;

        LocalDateTime dataFim = LocalDateTime.now();

        System.out.print("Tem certeza que quer fechar o leilão? (S/N) " + Tools.alertaCancelar());
        String imput1 = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(imput1)) return;
        char confirmacao = Character.toUpperCase(imput1.charAt(0));

        if (confirmacao != 'S') return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.print("Quer usar a data de agora (" + dataFim.format(formatter) + ")? (S/N) " + Tools.alertaCancelar());
        String imput2 = Tools.scanner.nextLine().trim();
        if (Tools.verificarSaida(imput2)) return;
        char confirmacaoData = Character.toUpperCase(imput2.charAt(0));

        if (confirmacaoData != 'S') {
            System.out.println("Insira a data que quer fechar o leilão (formato dd/MM/yyyy): " + Tools.alertaCancelar());
            String dataFimManual = Tools.scanner.nextLine().trim();

            if (Tools.verificarSaida(dataFimManual)) return;

            if (!dataFimManual.isEmpty()) {
                dataFim = Tools.parseDateTimeByDate(dataFimManual);
                if (dataFim != null) {
                    dataFim = dataFim.withHour(0).withMinute(0).withSecond(0).withNano(0);
                } else {
                    System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                    return;
                }
            } else {
                System.out.println("Data não pode ser vazia.");
                return;
            }
        }

        boolean sucesso = leilaoController.fecharLeilao(idLeilao, dataFim);

        if (sucesso) {
            System.out.println("Leilão fechado com sucesso!");
            String vencedor = lanceController.obterNomeVencedor(lanceController.selecionarLanceVencedor(idLeilao));
            if (vencedor != null) {
                System.out.println("O vencedor do Leilão é: " + lanceController.obterNomeVencedor(lanceController.selecionarLanceVencedor(idLeilao)));
            } else System.out.println("Não existem vencedores");
        } else {
            System.out.println("Leilão não encontrado!");
        }
    }


}
