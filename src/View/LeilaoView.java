package View;

import Controller.ProdutoController;
import Model.Leilao;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;
import Controller.LeilaoController;

import java.time.LocalDate;
import java.util.List;

public class LeilaoView {
    public static void exibirMenuLeiloes() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LEILÕES " + "=".repeat(5));
            System.out.println("1. Adicionar Leilão");
            System.out.println("2. Editar Leilão");
            System.out.println("3. Consultar Leilão");
            System.out.println("4. Eliminar Leilão");
            System.out.println("5. Listar Leilão");
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
                    listaLeiloes(false);
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private static void criarLeilao() {
        System.out.println("\nCRIAÇÃO DE UM LEILÃO\n");
        ResultadoOperacao resultadoProdutos = ProdutoController.listarProduto(true);
        if (resultadoProdutos.Sucesso) {
            System.out.print("\nIntroduza o ID do produto que pretende leiloar " + Tools.alertaCancelar());

            int idProduto = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idProduto))) return;

            ResultadoOperacao isAvailable = LeilaoController.verificarDisponibilidadeProduto(idProduto);
            if (isAvailable.Sucesso) {
                System.out.print("Insira a descrição do leilão " + Tools.alertaCancelar());
                String descricao = Tools.scanner.next();
                if (Tools.verificarSaida(descricao)) return;

                int idTipoLeilao;
                do {
                    System.out.println("\n1. Leilão Eletrónico");
                    System.out.println("2. Leilão Carta Fechada");
                    System.out.println("3. Leilão Venda Direta");
                    System.out.print("Escolha o tipo de leilão " + Tools.alertaCancelar());
                    idTipoLeilao = Tools.scanner.nextInt();
                    if (Tools.verificarSaida(String.valueOf(idTipoLeilao))) return;
                    if (idTipoLeilao != Constantes.tiposLeilao.ELETRONICO && idTipoLeilao != Constantes.tiposLeilao.CARTA_FECHADA && idTipoLeilao != Constantes.tiposLeilao.VENDA_DIRETA) {
                        System.out.println("Opção inválida. Tente novamente...");
                    }
                } while (idTipoLeilao < 1 || idTipoLeilao > 3);

                LocalDate dataInicio;
                while (true) {
                    System.out.print("\nInsira a data de início (dd/MM/yyyy) " + Tools.alertaCancelar());
                    Tools.scanner.nextLine();
                    String dataInicioStr = Tools.scanner.nextLine().trim();

                    if (Tools.verificarSaida(dataInicioStr)) return;
                    dataInicio = Tools.parseDate(dataInicioStr);

                    if (dataInicio != null) {
                        break;
                    } else {
                        System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
                    }
                }

                LocalDate dataFim = null;
                while (true) {
                    System.out.print("Insira a data de fim do leilão (dd/MM/yyyy) ou pressione ENTER para não definir " + Tools.alertaCancelar());
                    String dataFimStr = Tools.scanner.nextLine().trim();

                    if (Tools.verificarSaida(dataFimStr)) return;
                    if (!dataFimStr.isEmpty()) {
                        dataFim = Tools.parseDate(dataFimStr);

                        if (dataFim != null) {
                            ResultadoOperacao isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                            if (isCorrect.Sucesso) {
                                break;
                            } else {
                                System.out.println(isCorrect.msgErro);
                            }
                        } else {
                            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.\n");
                        }
                    } else {
                        break;
                    }

                }

                System.out.print("Insira o valor mínimo " + Tools.alertaCancelar());
                double valorMin = Tools.scanner.nextDouble();
                Tools.scanner.nextLine();
                if (Tools.verificarSaida(String.valueOf(valorMin))) return;

                double valorMax = 0.0;
                while (true) {
                    System.out.print("Insira o valor máximo ou pressione ENTER para não definir (-1 para cancelar): ");
                    String entrada = Tools.scanner.nextLine();
                    if (Tools.verificarSaida(entrada)) return;

                    if (!entrada.isEmpty()) {
                        try {
                            valorMax = Double.parseDouble(entrada);
                            if (valorMax != 0.0) {
                                ResultadoOperacao resultadoValores = LeilaoController.verificarValorMax(valorMin, valorMax);
                                if (resultadoValores.Sucesso) {
                                    break;
                                } else {
                                    System.out.println(resultadoValores.msgErro);
                                    valorMax = 0.0;
                                }
                            } else {
                                System.out.println("Entrada inválida. Por favor, insira um valor válido.\n");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Por favor, insira um valor numérico.\n");
                        }
                    } else {
                        break;
                    }
                }


                double multiploLance = 0;
                if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                    System.out.print("Insira o valor de cada lance " + Tools.alertaCancelar());
                    multiploLance = Tools.scanner.nextDouble();
                    if (Tools.verificarSaida(String.valueOf(multiploLance))) return;
                }
                int idEstado = LeilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, Constantes.estadosLeilao.ATIVO);
                ResultadoOperacao resultado = LeilaoController.criarLeiloes(0, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);

                if (resultado.Sucesso) {
                    ProdutoController.atualizarEstadoProduto(idProduto, 2);
                    System.out.println("Leilão criado com sucesso!");
                } else {
                    System.out.println(resultado.msgErro);
                }
            } else {
                System.out.println("\n" + isAvailable.msgErro);
            }
        } else {
            System.out.println(resultadoProdutos.msgErro);
        }
    }

    static void listaLeiloes(boolean apenasDisponiveis) {
        LeilaoController.listarLeiloes(apenasDisponiveis);
    }

    public static void exibirLeiloes(List<Leilao> leiloes) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE LEILÕES " + "=".repeat(5));
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                "Id", "Produto", "Descrição", "TipoLeilão", "Data Início", "Data Fim", "Valor Minimo", "Valor Maximo", "Multiplo de Lance", "Estado");
        System.out.println("-".repeat(260));
        for (Leilao leilao : leiloes) {
            String estadoStr = Tools.estadoLeilao.fromCodigo(leilao.getEstado()).name();
            String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(leilao.getTipoLeilao()).name();
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                    leilao.getId(),
                    nomeProduto(leilao.getIdProduto()),
                    leilao.getDescricao(),
                    tipoLeilaoStr,
                    leilao.getDataInicio() != null ? Tools.FORMATTER.format(leilao.getDataInicio()) : "N/A",
                    leilao.getDataFim() != null ? Tools.FORMATTER.format(leilao.getDataFim()) : "N/A",
                    leilao.getValorMinimo(),
                    leilao.getValorMaximo() != null && leilao.getValorMaximo() != 0.0 ? leilao.getValorMaximo() : "N/A",
                    leilao.getMultiploLance() != null && leilao.getMultiploLance() != 0 ? leilao.getMultiploLance() : "N/A",
                    estadoStr);
        }
    }

    private static void procurarLeilao() {
        System.out.println("\nPROCURAR UM LEILÃO");
        listaLeiloes(false);
        System.out.print("\nIntroduza o ID do Leilão que pretende consultar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);

        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
        } else {
            System.out.println("O Leilão com o Id " + id + " não foi encontrado.");
        }
    }

    private static void exibirLeilaoDetalhado(Leilao leilao) {
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

    private static void eliminarLeilao() {
        System.out.println("\nELIMINAÇÃO DE UM LEILÃO");
        listaLeiloes(false);
        System.out.print("\nIntroduza o ID do Leilão que pretende eliminar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);
        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
            System.out.println("\nTem a certeza que pretende eliminar o leilão com o Id " + id + "? (S/N)");
            char opc = Character.toUpperCase(Tools.scanner.next().charAt(0));
            switch (opc) {
                case 'S':
                    boolean sucesso = LeilaoController.eliminarLeilao(id);
                    if (sucesso) {
                        System.out.println("Leilão eliminado com sucesso.");
                    } else {
                        System.out.println("Erro ao eliminar leilão.");
                    }
                    break;
                case 'N':
                    System.out.println("Eliminação cancelada.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("[ERRO] Leilão não encontrado.");
        }
    }

    private static void editarLeilao() {
        System.out.println("\nEDIÇÃO DE UM LEILÃO");
        listaLeiloes(false);
        System.out.print("\nIntroduza o ID do Leilão que pretende editar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);

        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
            System.out.println("\nIntroduza os novos dados");
            ResultadoOperacao resultado = ProdutoController.listarProduto(true);
            int idProduto = leilao.getIdProduto();
            if (resultado.Sucesso) {
                System.out.print("\nNovo ID do produto que pretende leiloar ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String input;


                while (true) {
                    input = Tools.scanner.nextLine();
                    if (Tools.verificarSaida(input)) return;
                    if (input.isEmpty()) break;

                    try {
                        idProduto = Integer.parseInt(input);

                        ResultadoOperacao isAvailable = LeilaoController.verificarDisponibilidadeProduto(idProduto);
                        if (!isAvailable.Sucesso) {
                            System.out.println(isAvailable.msgErro);
                            System.out.print("Tente novamente ou pressione ENTER para não alterar: ");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido ou pressione Enter para manter o atual.");
                        System.out.print("Tente novamente: ");
                    }
                }
            } else {
                System.out.println(resultado.msgErro);
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

            LocalDate dataInicio = leilao.getDataInicio();
            while (true) {
                System.out.print("\nNova data de início (dd/MM/yyyy) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String dataInicioStr = Tools.scanner.nextLine().trim();
                if (Tools.verificarSaida(dataInicioStr)) return;
                if (dataInicioStr.isEmpty()) break;

                dataInicio = Tools.parseDate(dataInicioStr);
                if (dataInicio != null) break;
                else System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
            }

            LocalDate dataFim = leilao.getDataFim();
            while (true) {
                ResultadoOperacao isCorrect;
                System.out.print("Nova data de fim (dd/MM/yyyy) ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String dataFimStr = Tools.scanner.nextLine().trim();
                if (Tools.verificarSaida(dataFimStr)) return;
                if (!dataFimStr.isEmpty()) {
                    dataFim = Tools.parseDate(dataFimStr);

                    if (dataFim != null) {
                        isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                        if (isCorrect.Sucesso) break;
                        else System.out.println(isCorrect.msgErro);
                    } else {
                        System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.\n");
                    }
                } else {
                    if (dataFim != null) {
                        isCorrect = Tools.verificarDatasAnteriores(dataInicio, dataFim);
                        if (isCorrect.Sucesso) break;
                        else System.out.println(isCorrect.msgErro);
                    }
                }
            }

            double valorMin = leilao.getValorMinimo();
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

            double valorMax = leilao.getValorMaximo();
            while (true) {
                ResultadoOperacao resultadoValores;
                System.out.print("Novo valor máximo ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                String valorMaxStr = Tools.scanner.nextLine().trim();

                if (Tools.verificarSaida(valorMaxStr)) return;
                if (!valorMaxStr.isEmpty()) {
                    try {
                        valorMax = Double.parseDouble(valorMaxStr);
                        if (valorMax != 0.0) {
                            resultadoValores = LeilaoController.verificarValorMax(valorMin, valorMax);
                            if (resultadoValores.Sucesso) {
                                break;
                            } else {
                                System.out.println(resultadoValores.msgErro);  // Exibe erro de validação
                                valorMax = 0.0;
                            }
                        } else {
                            System.out.println("Entrada inválida. Por favor, insira um valor válido.\n");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Por favor, insira um valor numérico.\n");
                    }
                } else {
                    if (valorMax != 0.0) {
                        resultadoValores = LeilaoController.verificarValorMax(valorMin, valorMax);
                        if (resultadoValores.Sucesso) {
                            break;
                        } else {
                            System.out.println(resultadoValores.msgErro);  // Exibe erro de validação
                            valorMax = 0.0;
                        }
                    }
                }
            }

            double multiploLance = leilao.getMultiploLance();
            if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO) {
                while (true) {
                    System.out.print("Novo múltiplo de lance ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String multiploLanceStr = Tools.scanner.nextLine().trim();

                    if (Tools.verificarSaida(multiploLanceStr)) return;
                    if (multiploLanceStr.isEmpty()) break;

                    try {
                        multiploLance = Double.parseDouble(multiploLanceStr);
                        if (multiploLance <= 0) {
                            System.out.println("O múltiplo de lance deve ser maior que zero. Tente novamente.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Insira um número válido.");
                    }
                }
            }

            int idEstado = leilao.getEstado();
            idEstado = LeilaoController.determinarEstadoLeilaoByDatas(dataInicio, dataFim, idEstado);
            boolean sucesso = LeilaoController.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);

            if (sucesso) {
                if (leilao.getIdProduto() != idProduto) {
                    ProdutoController.atualizarEstadoProduto(leilao.getIdProduto(), 1);
                    ProdutoController.atualizarEstadoProduto(idProduto, 2);
                }
                System.out.println("Leilão editado com sucesso!");
            } else {
                System.out.println("Erro ao editar o leilão!");
            }
        } else {
            System.out.println("Leilão não encontrado.");
        }
    }

    private static String nomeProduto(int idProduto) {
        return ProdutoController.getNomeProdutoById(idProduto);
    }

}
