package View;

import Controller.LanceController;
import Model.ClienteSessao;
import Model.Lance;
import Utils.Tools;

import java.util.List;

import static BLL.LanceBLL.*;
import static View.LeilaoView.listaLeiloes;

public class LanceView {
    public static void exibirMenuLance() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LANCES " + "=".repeat(5));
            System.out.println("1. Ver Leilões ativos");
            System.out.println("2. Ver os meus Lances");
            System.out.println("3. Ver Lances por Leilão");
            System.out.println("4. Ver Leilões Terminados");
            System.out.println("5. Dar Lance direto");
            System.out.println("6. Dar Lance Carta Fechada");
            System.out.println("7. Dar Lance Eletronico");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    //listaLeiloes();
                    break;
                case 2:
                    int IdCliente = ClienteSessao.getIdCliente();
                    List<Lance> meuLance = listarMeuLance(IdCliente);
                    if(meuLance.isEmpty()){
                        System.out.println("Não fez lances ainda.");
                    } else
                        System.out.println("\nOs Seus Lances: ");
                    for (Lance lance : meuLance){
                        System.out.println(lance);
                    }
                    break;
                case 3:
                    System.out.print("Digite o ID do leilão que deseja ver os lances: ");
                    int idLeilao = Tools.scanner.nextInt();
                    LanceController.mostrarLancesPorLeilao(idLeilao);
                    break;
                case 4:
                    System.out.println("Em desenvolvimento");
                    break;
                case 5:
                    int idCliente = ClienteSessao.getIdCliente();
                    System.out.print("Digite o ID do leilão em que deseja participar: ");
                    int idLeilao1 = Tools.scanner.nextInt();

                    System.out.print("Digite o valor do lance: ");
                    double valorLance = Tools.scanner.nextDouble();
                    adicionarLance(idLeilao1, idCliente, valorLance);
                    break;
                case 6:
                    double valorLance1 = Tools.scanner.nextDouble();
                    adicionarLanceCartaFechada(valorLance1);
                    break;
                case 7:
                    int idCliente2 = ClienteSessao.getIdCliente();
                    System.out.print("Digite o ID do leilão em que deseja participar: ");
                    int idLeilao3 = Tools.scanner.nextInt();
                    System.out.print("Digite o valor do lance: ");
                    Integer NumLance = Tools.scanner.nextInt();
                    adicionarLanceEletronico(idLeilao3, idCliente2, NumLance);
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }
}
