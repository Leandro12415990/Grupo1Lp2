package View;

import BLL.TransacaoBLL;
import BLL.UtilizadorBLL;
import DAL.UtilizadorDAL;
import Model.Transacao;
import Model.ResultadoOperacao;
import Model.ClienteSessao;
import Model.Utilizador;
import Controller.TransacaoController;
import Utils.Constantes;
import Utils.Tools;

import java.sql.SQLOutput;
import java.util.List;

public class TransacaoView {
    public void exibirMenuTransacao() {
        ClienteSessao clienteSessao = new ClienteSessao();
        int opc;
        do {
            System.out.println("\n" + "=".repeat(8) + " CARTEIRA " + "=".repeat(8));
            System.out.println("1. Adicionar Créditos");
            System.out.println("2. Ver Saldo");
            System.out.println("3. Ver Depósitos");
            System.out.println("4. Ver Movimentos");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção " + Tools.alertaCancelar());
            opc = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(opc))) return;

            switch (opc) {
                case 1:
                    adicionarCreditos(clienteSessao.getIdCliente());
                    break;
                case 2:
                    verCarteira(clienteSessao.getIdCliente());
                    break;
                case 3:
                    verDepositos(clienteSessao.getIdCliente());
                    break;
                case 4:
                    verMovimentos(clienteSessao.getIdCliente());
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private void adicionarCreditos(int idCliente) {
        TransacaoController transacaoController = new TransacaoController();
        System.out.println("\nADICIONAR CRÉDITOS");

        verCarteira(idCliente);
        double saldoAtual = buscarValorTotalAtual(idCliente);

        System.out.print("\nInsira a quantidade de créditos que pretende depositar " + Tools.alertaCancelar());
        Double creditos = Tools.scanner.nextDouble();
        if (Tools.verificarSaida(String.valueOf(creditos))) return;

        ResultadoOperacao resultado = transacaoController.criarTransacao(idCliente, saldoAtual, creditos);
        if (resultado.Sucesso)
            System.out.println("\n Os créditos serão adicionados à sua conta aquando da aprovação do gestor!");
        else System.out.println(resultado.msgErro);
    }

    private Double buscarValorTotalAtual(int idCliente) {
        TransacaoController transacaoController = new TransacaoController();
        return transacaoController.buscarValorTotalAtual(idCliente);
    }

    private Double valorPendente(int idCliente) {
        TransacaoController transacaoController = new TransacaoController();
        return transacaoController.valorPendente(idCliente);
    }

    private void listarTransacoes(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        TransacaoController transacaoController = new TransacaoController();
        List<Transacao> transacoesList = transacaoController.listarDepositos(apenasPendentes, idTipoTransacao, idCliente);
        if (idCliente != 0) {
            exibirTransacoes(transacoesList,true);
        } else {
            exibirTransacoes(transacoesList,false);
        }

    }

    public void aprovarDepositos() {
        TransacaoController transacaoController = new TransacaoController();
        List<Transacao> pendentes = transacaoController.listarDepositos(true, Constantes.tiposTransacao.DEPOSITO, 0);

        if (pendentes.isEmpty()) {
            System.out.println("\n Não há depósitos pendentes de aprovação.");
            return;
        }
        while (true) {
            System.out.println("\nDEPÓSITOS PENDENTES DE APROVAÇÃO!");
            listarTransacoes(true, Constantes.tiposTransacao.DEPOSITO, 0);

            System.out.print("Insira o ID do Depósito que pretende aprovar/negar " + Tools.alertaCancelar());
            if (!Tools.scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                Tools.scanner.next();
                continue;
            }

            int idDeposito = Tools.scanner.nextInt();
            Tools.scanner.nextLine();

            if (Tools.verificarSaida(String.valueOf(idDeposito))) break;

            ResultadoOperacao resultado = transacaoController.verificarTransacao(idDeposito);
            if (resultado.Sucesso) {
                Transacao deposito = transacaoController.buscarTransacao(idDeposito);
                while (true) {
                    System.out.printf("Pretende aprovar(A) ou negar(N)? " + Tools.alertaCancelar());
                    String input = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(input)) return;

                    char opc = Character.toUpperCase(input.charAt(0));

                    switch (opc) {
                        case 'A':
                            System.out.println("O depósito " + deposito.getIdTransacao() + " foi aprovado com sucesso!");
                            transacaoController.atualizarSaldo(deposito.getIdCliente(), deposito.getValorTransacao());
                            transacaoController.atualizarEstadosTransacao(deposito.getIdTransacao(), Constantes.estadosTransacao.ACEITE);
                            break;
                        case 'N':
                            System.out.println("O depósito " + deposito.getIdTransacao() + " foi negado!");
                            transacaoController.atualizarEstadosTransacao(deposito.getIdTransacao(), Constantes.estadosTransacao.NEGADO);
                            break;
                        default:
                            System.out.println("Opção inválida. Tente novamente...");
                            continue;
                    }
                    return;
                }
            } else {
                System.out.println(resultado.msgErro);
            }
        }
    }

    public void exibirTransacoes(List<Transacao> transacaoList, boolean vistaCliente) {
        if (!vistaCliente) {
            System.out.println("\n" + "=".repeat(5) + " DEPÓSITOS PENDENTES " + "=".repeat(5));
            System.out.printf("%-18s %-45s %-25s %-25s %-25s %-18s %-30s\n",
                    "IdTransação", "Cliente", "Valor Total Conta", "Valor Depósito", "Data Depósito", "TipoTransação", "Estado");
            System.out.println("-".repeat(172));
            for (Transacao transacao : transacaoList) {
                String estadoStr = Tools.estadoDeposito.fromCodigo(transacao.getIdEstadoTransacao()).name();
                String tipoStr = Tools.tipoTransacao.fromCodigo(transacao.getIdTipoTransacao()).name();
                String cliente = (getUtilizador(transacao.getIdCliente()).getNomeUtilizador()) + " (" + getUtilizador(transacao.getIdCliente()).getEmail() + ")";

                System.out.printf("%-18s %-45s %-25s %-25s %-25s %-18s %-30s\n",
                        transacao.getIdTransacao(),
                        cliente,
                        transacao.getValorTotal() + "€",
                        transacao.getValorTransacao() + "€",
                        transacao.getDataTransacao() != null ? Tools.DATA_HORA.format(transacao.getDataTransacao()) : "N/A",
                        tipoStr,
                        estadoStr);
            }
        } else {
            System.out.printf("%-18s %-25s %-20s %-30s %-20s %-30s\n",
                    "IdTransação", "Valor Total Conta", "Valor Transação", "Data Transação", "Tipo Transação", "Estado");
            System.out.println("-".repeat(130));
            for (Transacao transacao : transacaoList) {
                String estadoStr = Tools.estadoDeposito.fromCodigo(transacao.getIdEstadoTransacao()).name();
                String tipoStr = Tools.tipoTransacao.fromCodigo(transacao.getIdTipoTransacao()).name();
                String valorTransacao = (transacao.getIdTipoTransacao() == Constantes.tiposTransacao.LANCE_DEBITO ? "-" : "+") + transacao.getValorTransacao() + "€";
                System.out.printf("%-18s %-25s %-20s %-30s %-20s %-30s\n",
                        transacao.getIdTransacao(),
                        transacao.getValorTotal() + "€",
                        valorTransacao,
                        transacao.getDataTransacao() != null ? Tools.DATA_HORA.format(transacao.getDataTransacao()) : "N/A",
                        tipoStr,
                        estadoStr);
            }
        }
    }

    public Utilizador getUtilizador(int idCliente) {
        TransacaoController transacaoController = new TransacaoController();
        return transacaoController.getUtilizador(idCliente);
    }

    public void verDepositos(int idCliente) {
        System.out.println("\n" + "=".repeat(5) + " DEPÓSITOS " + "=".repeat(5));
        listarTransacoes(false, Constantes.tiposTransacao.DEPOSITO, idCliente);
    }

    public void verMovimentos(int idCliente) {
        System.out.println("\n" + "=".repeat(5) + " MOVIMENTOS " + "=".repeat(5));
        listarTransacoes(false, 0, idCliente);
    }

    public void verCarteira(int idCliente) {
        Double saldoAtual = buscarValorTotalAtual(idCliente);
        Double saldoPendente = valorPendente(idCliente);
        System.out.println("\n" + "=".repeat(5) + " SALDO " + "=".repeat(5));
        System.out.printf("Saldo atual: %.2f€\n", saldoAtual);
        System.out.printf("Saldo pendente de aprovação: %.2f€\n", saldoPendente);

    }
}
