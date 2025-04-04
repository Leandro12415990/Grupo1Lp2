package View;

import BLL.TransacaoBLL;
import Controller.TransacaoController;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.util.List;

public class TransacaoView {
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
                    System.out.println("Funcionalidade em desenvolvimento");
                    adicionarCreditos();
                    break;
                case 2:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verDepositos();
                    break;
                case 3:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verCarteira();
                    break;
                case 4:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verTransacoes();
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
        int idCliente = ClienteSessao.getIdCliente();
        System.out.println("\nADICIONAR CRÉDITOS");

        Double saldoAtual = buscarValorTotalAtual(idCliente);
        Double saldoPendente = valorPendente(idCliente);
        System.out.println("Saldo atual: " + saldoAtual + "€");
        System.out.println("Saldo pendente de aprovação: " + saldoPendente + "€");

        System.out.print("Insira a quantidade de créditos que pretende depositar " + Tools.alertaCancelar());
        Double creditos = Tools.scanner.nextDouble();
        if(Tools.verificarSaida(String.valueOf(creditos))) return;

        ResultadoOperacao resultado = TransacaoController.criarTransacao(idCliente, saldoAtual, creditos);
        if (resultado.Sucesso) System.out.println("Os créditos serão adicionados à sua conta quando o gestor aprovar o depósito!");
            else System.out.println(resultado.msgErro);
    }

    private static Double buscarValorTotalAtual(int idCliente) {
        return TransacaoController.buscarValorTotalAtual(idCliente);
    }

    private static Double valorPendente(int idCliente) {
        return TransacaoController.valorPendente(idCliente);
    }

    private static void listarTransacoes(boolean apenasPendentes) {
        TransacaoController.listarDepositos(apenasPendentes);
    }

    public static void aprovarDepositos() {
        System.out.println("\nDEPÓSITOS PENDENTES DE APROVAÇÃO!");
        listarTransacoes(true);

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
            boolean continuar = true;
            while (continuar) {
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
                if (Tools.verificarSaida(String.valueOf(opc))) {
                    continuar = false;
                    break;
                }

                switch (opc) {
                    case 1:
                        System.out.println("Aprovado!");
                        TransacaoController.atualizarSaldo(deposito.getIdCliente(), deposito.getValorTransacao());
                        TransacaoController.atualizarEstadosTransacao(deposito.getIdTransacao(), Constantes.estadosTransacao.ACEITE);
                        continuar = false;
                        break;
                    case 2:
                        System.out.println("Negado!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente...");
                        break;
                }
            }
        } else System.out.println(resultado.msgErro);
    }

    public static void exibirTransacoes(List<Transacao> transacaoList) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE DEPÓSITOS " + "=".repeat(5) + "\n");
        System.out.printf("%-12s %-30s %-30s %-25s %-30s %-30s\n",
                "IdDepósito", "Cliente", "Valor Total Conta", "Valor Depósito", "Data Depósito", "Estado");
        System.out.println("-".repeat(145));
        for (Transacao transacao : transacaoList) {
            String estadoStr = Tools.estadoDeposito.fromCodigo(transacao.getIdEstadoTransacao()).name();
            String cliente = (getUtilizador(transacao.getIdCliente()).getNomeUtilizador()) + " (" + getUtilizador(transacao.getIdCliente()).getEmail() + ")";
            System.out.printf("%-12s %-30s %-30s %-25s %-30s %-30s\n",
                    transacao.getIdTransacao(),
                    cliente,
                    transacao.getValorTotal(),
                    transacao.getValorTransacao(),
                    transacao.getDataTransacao() != null ? Tools.DATA_HORA.format(transacao.getDataTransacao()) : "N/A",
                    estadoStr);
        }
    }

    public static Utilizador getUtilizador(int idCliente) {
        return TransacaoController.getUtilizador(idCliente);
    }

}
