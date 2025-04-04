package View;

import BLL.TransacaoBLL;
import Controller.TransacaoController;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.util.List;

public class TransacaoView {
    private static int idCliente = ClienteSessao.getIdCliente();

    public static void exibirMenuTransacao() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(8) + " CARTEIRA " + "=".repeat(8));
            System.out.println("1. Adicionar Créditos");
            System.out.println("2. Ver Depósitos");
            System.out.println("3. Ver Carteira");
            System.out.println("4. Ver Transações");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    //System.out.println("Funcionalidade em desenvolvimento");
                    adicionarCreditos();
                    break;
                case 2:
                    //System.out.println("Funcionalidade em desenvolvimento");
                    verDepositos(idCliente);
                    break;
                case 3:
                    //System.out.println("Funcionalidade em desenvolvimento");
                    verCarteira(idCliente);
                    break;
                case 4:
                    //System.out.println("Funcionalidade em desenvolvimento");
                    verTransacoes(idCliente);
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private static void adicionarCreditos() {
        System.out.println("\nADICIONAR CRÉDITOS");

        Double saldoAtual = buscarValorTotalAtual(idCliente);
        Double saldoPendente = valorPendente(idCliente);
        System.out.println("Saldo atual: " + saldoAtual + "€");
        System.out.println("Saldo pendente de aprovação: " + saldoPendente + "€");

        System.out.print("Insira a quantidade de créditos que pretende depositar " + Tools.alertaCancelar());
        Double creditos = Tools.scanner.nextDouble();
        if (Tools.verificarSaida(String.valueOf(creditos))) return;

        ResultadoOperacao resultado = TransacaoController.criarTransacao(idCliente, saldoAtual, creditos);
        if (resultado.Sucesso)
            System.out.println("Os créditos serão adicionados à sua conta quando o gestor aprovar o depósito!");
        else System.out.println(resultado.msgErro);
    }

    private static Double buscarValorTotalAtual(int idCliente) {
        return TransacaoController.buscarValorTotalAtual(idCliente);
    }

    private static Double valorPendente(int idCliente) {
        return TransacaoController.valorPendente(idCliente);
    }

    private static void listarTransacoes(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        TransacaoController.listarDepositos(apenasPendentes, idTipoTransacao, idCliente);
    }

    public static void aprovarDepositos() {
        System.out.println("\nDEPÓSITOS PENDENTES DE APROVAÇÃO!");
        listarTransacoes(true, 0, 0);

        System.out.print("Insira o ID do Depósito que pretende aprovar/negar " + Tools.alertaCancelar());
        if (!Tools.scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, insira um número.");
            Tools.scanner.next();
            return;
        }

        int idDeposito = Tools.scanner.nextInt();
        if (Tools.verificarSaida(String.valueOf(idDeposito))) return;

        ResultadoOperacao resultado = TransacaoController.verificarTransacao(idDeposito);
        if (resultado.Sucesso) {
            Transacao deposito = TransacaoController.buscarTransacao(idDeposito);
            while (true) {
                System.out.println("Pretende aprovar ou negar? ");
                System.out.println("1. Aprovar");
                System.out.println("2. Negar");
                System.out.print("Escolha a opção " + Tools.alertaCancelar());
                if (!Tools.scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    Tools.scanner.next();
                    continue;
                }

                int opc = Tools.scanner.nextInt();
                if (Tools.verificarSaida(String.valueOf(opc))) return;
                switch (opc) {
                    case 1:
                        System.out.println("Aprovado!");
                        TransacaoController.atualizarSaldo(deposito.getIdCliente(), deposito.getValorTransacao());
                        TransacaoController.atualizarEstadosTransacao(deposito.getIdTransacao(), Constantes.estadosTransacao.ACEITE);
                        return;
                    case 2:
                        System.out.println("Negado!");
                        TransacaoController.atualizarEstadosTransacao(deposito.getIdTransacao(), Constantes.estadosTransacao.NEGADO);
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente...");
                        return;
                }
            }
        } else System.out.println(resultado.msgErro);
    }

    public static void exibirTransacoes(List<Transacao> transacaoList, boolean vistaCliente) {
        if (!vistaCliente) {
            System.out.printf("%-12s %-30s %-30s %-25s %-30s %-30s %-30s\n",
                    "IdTransação", "Cliente", "Valor Total Conta", "Valor Depósito", "Data Depósito", "TipoTransação", "Estado");
            System.out.println("-".repeat(145));
            for (Transacao transacao : transacaoList) {
                String estadoStr = Tools.estadoDeposito.fromCodigo(transacao.getIdEstadoTransacao()).name();
                String tipoStr = Tools.tipoTransacao.fromCodigo(transacao.getIdTipoTransacao()).name();
                String cliente = (getUtilizador(transacao.getIdCliente()).getNomeUtilizador()) + " (" + getUtilizador(transacao.getIdCliente()).getEmail() + ")";
                System.out.printf("%-12s %-30s %-30s %-25s %-30s %-30s %-30s\n",
                        transacao.getIdTransacao(),
                        cliente,
                        transacao.getValorTotal(),
                        transacao.getValorTransacao(),
                        transacao.getDataTransacao() != null ? Tools.DATA_HORA.format(transacao.getDataTransacao()) : "N/A",
                        tipoStr,
                        estadoStr);
            }
        } else {
            System.out.printf("%-18s %-30s %-25s %-30s %-30s %-30s\n",
                    "IdTransação", "Valor Total Conta", "Valor Depósito", "Data Depósito", "TipoTransação", "Estado");
            System.out.println("-".repeat(145));
            for (Transacao transacao : transacaoList) {
                String estadoStr = Tools.estadoDeposito.fromCodigo(transacao.getIdEstadoTransacao()).name();
                String tipoStr = Tools.tipoTransacao.fromCodigo(transacao.getIdTipoTransacao()).name();
                System.out.printf("%-18s %-30s %-25s %-30s %-30s %-30s\n",
                        transacao.getIdTransacao(),
                        transacao.getValorTotal(),
                        transacao.getValorTransacao(),
                        transacao.getDataTransacao() != null ? Tools.DATA_HORA.format(transacao.getDataTransacao()) : "N/A",
                        tipoStr,
                        estadoStr);
            }
        }
    }

    public static Utilizador getUtilizador(int idCliente) {
        return TransacaoController.getUtilizador(idCliente);
    }

    public static void verDepositos(int idCliente) {
        listarTransacoes(false, Constantes.tiposTransacao.DEPOSITO, idCliente);
    }

    public static void verTransacoes(int idCliente) {
        listarTransacoes(false, 0, idCliente);
    }

    public static void verCarteira(int idCliente) {
        Double saldoAtual = buscarValorTotalAtual(idCliente);
        Double saldoPendente = valorPendente(idCliente);
        System.out.println("Saldo atual: " + saldoAtual + "€");
        System.out.println("Saldo pendente de aprovação: " + saldoPendente + "€");
    }

}
