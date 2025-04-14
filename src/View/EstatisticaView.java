package View;

import BLL.LeilaoBLL;
import Controller.EstatisticaController;
import DAL.ImportDal;
import Model.Leilao;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import java.time.Period;
import java.util.List;

public class EstatisticaView {

    /** Exibição dos menus */
    public static void exibirMenuListagem() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LISTAGEM " + "=".repeat(5));
            System.out.println("1. Estatisticas Por Leilão");
            System.out.println("2. Estatisticas Globais");
            System.out.println("3. Estatisticas Por Tipo Leilão");
            System.out.println("4. Estatisticas De Clientes");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            switch (opc) {
                case 1:
                    estatisticasPorLeilao();
                    break;
                case 2:
                    exibirMenuGlobal();
                    break;
                case 3:
                    exibirMenuPorTipo();
                    break;
                case 4:
                    exibirMenuEstatisticaCliente();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public static void exibirMenuGlobal() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LISTAGEM " + "=".repeat(5));
            System.out.println("1. Quantidade de leilôes terminados.");
            System.out.println("2. Leilão que mais tempo esteve ativo.");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            switch (opc) {
                case 1:
                    exibirLeiloesFinalizados();
                    break;
                case 2:
                    mostrarLeilaoMaisTempoAtivo();
                    break;
                case 3:
                    mostrarLeilaoComMaisLances();
                    break;
                case 4:
                    mostrarMediaTempoEntreLances();
                    break;
                case 5:
                    mostrarLeiloesSemLances();
                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public static void exibirMenuPorTipo() {
        int opc;
        do {
            System.out.println("\n=== ESCOLHA O TIPO DE LEILÃO ===");
            System.out.println("1. Eletrónico");
            System.out.println("2. Carta Fechada");
            System.out.println("3. Venda Direta");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");

            switch (opc) {
                case 1:
                    menuAcoesEletronico();
                    break;
                case 2:
                    menuAcoesCartaFechada();
                    break;
                case 3:
                    menuAcoesVendaDireta();
                    break;
                case 0:
                    System.out.println("A sair...");
                    break;
                default: System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void exibirMenuEstatisticaCliente() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LISTAGEM " + "=".repeat(5));
            System.out.println("1. Clientes Registados");
            System.out.println("2. Média De Idades Dos Clientes");
            System.out.println("3. Percentagem de clientes que usam o maior domínio de e-mail ");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            switch (opc) {
                case 1:
                    mostrarTodosClientes();
                    break;
                case 2:
                    mostrarMediaIdadeUtilizadores();
                    break;
                case 3:
                    mostrarDominioMaisUsadoEPercentagem();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public static void menuAcoesEletronico() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES ELETRÓNICOS ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 3:
                    mostrarLeilaoComMaisLancesPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 4:
                    mostrarMediaTempoEntreLancesPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 5:
                    mostrarLeiloesSemLancesPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;

                case 0:
                    System.out.println("A voltar...");
                    break;
                default: System.out.println(" Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void menuAcoesCartaFechada() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES CARTA FECHADA ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 3:
                    mostrarLeilaoComMaisLancesPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 4:
                    mostrarMediaTempoEntreLancesPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 5:
                    mostrarLeiloesSemLancesPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void menuAcoesVendaDireta() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES VENDA DIRETA ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 3:
                    mostrarLeilaoComMaisLancesPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 4:
                    mostrarMediaTempoEntreLancesPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 5:
                    mostrarLeiloesSemLancesPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    /** metodos da view */

    public static void exibirContagemPorTipo(int idTipo) {
        System.out.println("\n" + "=".repeat(5) + " LEILÕES FECHADOS " + "=".repeat(5));

        int total = EstatisticaController.contarLeiloesFechadosPorTipo(idTipo);
        String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(idTipo).name();
        if (total == 0) {
            System.out.println("Não existem leilões fechados do tipo \"" + tipoLeilaoStr + "\".");
        } else {
            System.out.println("Total de leilões fechados do tipo \"" + tipoLeilaoStr + "\": " + total);
            System.out.println("\n Lista de leilões fechados:\n");
        }
        List<String> linhas = EstatisticaController.listarLeiloesFechadosFormatadosPorTipo(idTipo);
        for (String linha : linhas) {
            System.out.println(linha);
        }
    }

    public static void exibirLeiloesFinalizados() {
            System.out.println("\n" + "=".repeat(5) + " LEILÕES FECHADOS " + "=".repeat(5));

            int total = EstatisticaController.contarLeilaoGlobal();

            if (total == 0) {
                System.out.println("Não existem leilões com estado 'Fechado'.");
            } else {
                System.out.println("A quantidade de leilões terminados é: " + total);
                System.out.println("\n Lista de leilões fechados:\n");

                List<String> linhas = EstatisticaController.listarLeiloesFechadosFormatados();
                for (String linha : linhas) {
                    System.out.println(linha);
                }
            }
        }

    public static void mostrarLeilaoMaisTempoAtivo() {
        Leilao leilao = EstatisticaController.getLeilaoMaisTempoAtivo();

        if (leilao == null) {
            System.out.println("Não existem leilões válidos.");
            return;
        }

        Period tempo = Period.between(leilao.getDataInicio().toLocalDate(), leilao.getDataFim().toLocalDate());

        System.out.println("\n=== Leilão com mais tempo ativo ===");
        System.out.println("ID: " + leilao.getId());
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tempo ativo: " + tempo.getYears() + " anos, " +
                tempo.getMonths() + " meses, " +
                tempo.getDays() + " dias");
    }

    public static void mostrarLeilaoMaisTempoPorTipo(int idTipo) {
        Leilao leilao = EstatisticaController.getLeilaoTipoMaisTempoAtivo(idTipo);

        if (leilao == null) {
            System.out.println("Nenhum leilão válido encontrado para este tipo.");
            return;
        }

        Period tempo = Period.between(leilao.getDataInicio().toLocalDate(), leilao.getDataFim().toLocalDate());

        System.out.println("\n=== Leilão com mais tempo ativo (por tipo) ===");
        System.out.println("ID: " + leilao.getId());
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tempo ativo: " + tempo.getYears() + " anos, " +
                tempo.getMonths() + " meses, " + tempo.getDays() + " dias");
    }

    public static void mostrarLeilaoComMaisLances() {
        String[] dados = EstatisticaController.getDadosLeilaoComMaisLances();

        if (dados == null) {
            System.out.println("Não existem lances registados.");
            return;
        }

        System.out.println("\n=== Leilão com mais lances ===");
        System.out.println("ID do Leilão: " + dados[0]);
        System.out.println("Descrição: " + dados[1]);
        System.out.println("Total de lances: " + dados[2]);
    }

    public static void mostrarLeilaoComMaisLancesPorTipo(int idTipo) {
        String[] dados = EstatisticaController.getDadosLeilaoComMaisLancesPorTipo(idTipo);

        if (dados == null) {
            System.out.println("Nenhum leilão com lances encontrado para esse tipo.");
            return;
        }

        String tipoStr = Tools.tipoLeilao.fromCodigo(idTipo).name();

        System.out.println("\n=== Leilão com mais lances do tipo " + tipoStr + " ===");
        System.out.println("ID do Leilão: " + dados[0]);
        System.out.println("Descrição: " + dados[1]);
        System.out.println("Total de lances: " + dados[2]);
    }

    public static void mostrarMediaTempoEntreLances() {
        double media = EstatisticaController.calcularMediaTempoEntreLances();

        if (media == -1) {
            System.out.println("Não foi possível calcular a média (faltam lances suficientes).");
            return;
        }

        System.out.println("\n=== Média de tempo entre lances ===");
        System.out.println("Tempo médio: " + Tools.formatarMinutosParaHorasEMinutos(media));
    }

    public static void mostrarMediaTempoEntreLancesPorTipo(int idTipoLeilao) {
        double media = EstatisticaController.calcularMediaTempoEntreLancesPorTipo(idTipoLeilao);

        if (media == -1) {
            System.out.println("Não há lances suficientes para calcular a média.");
            return;
        }

        System.out.println("\n=== Média de tempo entre lances para tipo " +
                Tools.tipoLeilao.fromCodigo(idTipoLeilao).name() + " ===");

        System.out.println("Tempo médio: " + Tools.formatarMinutosParaHorasEMinutos(media));
    }

    public static void mostrarLeiloesSemLances() {
        List<Leilao> semLances = EstatisticaController.getLeiloesSemLances();

        if (semLances.isEmpty()) {
            System.out.println("Todos os leilões têm pelo menos um lance.");
            return;
        }

        System.out.println("\n=== Leilões sem lances ===");
        System.out.println("Quantidade: " + semLances.size());
        System.out.println("Lista de leilões sem lances:\n");

        for (Leilao leilao : semLances) {
            System.out.println("ID: " + leilao.getId() +
                    " | Descrição: " + leilao.getDescricao() +
                    " | Tipo: " + leilao.getTipoLeilao());
        }
    }

    public static void mostrarLeiloesSemLancesPorTipo(int idTipoLeilao) {
        List<Leilao> semLances = EstatisticaController.getLeiloesSemLancesPorTipo(idTipoLeilao);

        String tipoStr = Tools.tipoLeilao.fromCodigo(idTipoLeilao).name();

        if (semLances.isEmpty()) {
            System.out.println("Não há leilões do tipo " + tipoStr + " sem lances.");
            return;
        }

        System.out.println("\n=== Leilões do tipo " + tipoStr + " sem lances ===");
        System.out.println("Quantidade: " + semLances.size());
        System.out.println("Lista de leilões:\n");

        for (Leilao leilao : semLances) {
            System.out.println("ID: " + leilao.getId() +
                    " | Descrição: " + leilao.getDescricao());
        }
    }

    public static void mostrarTodosClientes() {
        List<Utilizador> clientes = ImportDal.carregarUtilizador();

        if (clientes == null || clientes.isEmpty()) {
            System.out.println("Não existem clientes registados.");
            return;
        }

        System.out.println("\n=== Lista de Clientes ===");

        for (Utilizador u : clientes) {
            System.out.println("ID: " + u.getId() +
                    " | Nome: " + u.getNomeUtilizador() +
                    " | Email: " + u.getEmail() + "\n");
        }

        System.out.println("Total de clientes: " + clientes.size() + "\n");
    }

    public static void mostrarMediaIdadeUtilizadores() {
        double media = EstatisticaController.getMediaIdadeUtilizadores();

        if (media == -1) {
            System.out.println("Não foi possível calcular a média de idades.");
            return;
        }

        System.out.printf("\n=== Média de idade dos utilizadores ===\n");
        System.out.printf("Média: %.2f anos\n", media);
    }

    public static void mostrarDominioMaisUsadoEPercentagem() {
        String[] resultado = EstatisticaController.getDominioMaisUsadoEPercentagem();

        if (resultado == null) {
            System.out.println("Não foi possível calcular (sem clientes ou emails válidos).");
            return;
        }

        System.out.println("\n=== Domínio de Email Mais Usado ===");
        System.out.println("Domínio mais comum: " + resultado[0]);
        System.out.println("Percentagem de clientes: " + resultado[1] + "%");
    }

    public static void estatisticasPorLeilao() {
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();

        if (leiloes == null || leiloes.isEmpty()) {
            System.out.println("Não há leilões registados.");
            return;
        }

        LeilaoView.exibirLeiloes(leiloes);

        int id = Tools.pedirOpcaoMenu("Insira o ID do leilão que deseja analisar "+ Tools.alertaCancelar());

        if (Tools.verificarSaida(String.valueOf(id))) return;

        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(id);
        if (leilao == null) {
            System.out.println("Leilão não encontrado.");
            return;
        }

        Period tempo = EstatisticaController.getTempoAtivoLeilao(id);
        System.out.println("\n=== Tempo total ativo do leilão ===");
        System.out.println("Duração: " + tempo.getYears() + " anos, "
                + tempo.getMonths() + " meses, "
                + tempo.getDays() + " dias");

        System.out.println("\n=== Clientes ordenados pelo maior lance no leilão \"" + leilao.getDescricao() + "\" ===");
        List<String> lista = EstatisticaController.getClientesOrdenadosPorValorMaisAlto(id);
        if (lista.isEmpty()) {
            System.out.println("Nenhum cliente participou neste leilão.");
        } else {
            for (String linha : lista) {
                System.out.println(linha);
            }
        }
    }

}
