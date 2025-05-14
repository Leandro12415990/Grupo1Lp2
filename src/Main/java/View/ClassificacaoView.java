package View;

import Controller.ClassificacaoController;
import Model.Classificacao;
import Model.Leilao;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class ClassificacaoView {

    public void exibirMenuClassificacao() throws MessagingException, IOException {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU CLASSIFICACOES " + "=".repeat(5));
            System.out.println("1. Ver Leilões Ja Classificados");
            System.out.println("2. Ver Leilões Por Classificar");
            System.out.println("0. Voltar ao menu principal...");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            switch (opc) {
                case 1:
                    verMinhaClassificacao();
                    break;
                case 2:
                    avaliarLeiloesPorAvaliar();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);

    }

    public void pedirClassificacao(Leilao leilao) {
        int nota = 0;
        System.out.println("\nGostou do leilão: " + leilao.getDescricao() + "? Deixe a sua avaliação.");

        do {
            nota = Tools.pedirOpcaoMenu("Insira uma classificação (1 a 5)" + Tools.alertaCancelar());
            if (nota == -1) {
                System.out.println("Classificação cancelada.");
                return;
            }
            if (nota < 1 || nota > 5)
                nota = Tools.pedirOpcaoMenu("Por favor, insira um número válido entre 1 e 5." + Tools.alertaCancelar());
        } while (nota < 1 || nota > 5);

        ClassificacaoController controller = new ClassificacaoController();
        int idCliente = Tools.clienteSessao.getIdCliente();

        System.out.print("Insira um comentário: ");
        Tools.scanner.nextLine();
        String comentario = Tools.scanner.nextLine().trim();

        controller.registarAvaliacao(idCliente, leilao.getId(), nota, comentario);
        System.out.println("Obrigado pela sua avaliação!");
    }

    public void listarLeiloesAvaliados() {
        int idCliente = Tools.clienteSessao.getIdCliente();

        ClassificacaoController controller = new ClassificacaoController();
        List<Leilao> avaliados = controller.listarLeiloesAvaliadosPeloCliente(idCliente);

        if (avaliados.isEmpty()) {
            System.out.println("Ainda não avaliou nenhum leilão.");
        } else {
            System.out.println("\nLeilões que já avaliou:");
            for (Leilao l : avaliados) {
                System.out.println("ID: " + l.getId() + " | Descrição: " + l.getDescricao());
            }
        }
    }

    public void listarLeiloesPorAvaliar() throws MessagingException, IOException {
        int idCliente = Tools.clienteSessao.getIdCliente();
        ClassificacaoController controller = new ClassificacaoController();

        List<Leilao> porAvaliar = controller.listarLeiloesPorAvaliar(idCliente);

        if (porAvaliar.isEmpty()) {
            System.out.println("Não tens leilões por avaliar.");
        } else {
            System.out.println("\nLeilões terminados por avaliar:");
            for (Leilao l : porAvaliar) {
                System.out.println("ID: " + l.getId() + " | Descrição: " + l.getDescricao());
            }
        }
    }

    public void avaliarLeiloesPorAvaliar() throws MessagingException, IOException {
        int idCliente = Tools.clienteSessao.getIdCliente();
        ClassificacaoController controller = new ClassificacaoController();
        List<Leilao> leiloes = controller.listarLeiloesPorAvaliar(idCliente);

        if (leiloes.isEmpty()) {
            System.out.println("Não tens leilões por avaliar.");
            return;
        }
        listarLeiloesPorAvaliar();

        while (true) {
            int idSelecionado = Tools.pedirOpcaoMenu("\nEscolhe o ID de um leilão para avaliar " + Tools.alertaCancelar());

            if (idSelecionado == -1) {
                System.out.println("Operação cancelada.");
                return;
            }

            Leilao leilaoSelecionado = null;
            for (Leilao l : leiloes) {
                if (l.getId() == idSelecionado) {
                    leilaoSelecionado = l;
                    break;
                }
            }

            if (leilaoSelecionado == null) {
                System.out.println("ID inválido. Certifica-te que escolheste um leilão da lista.");
            } else {
                ClassificacaoView classificacaoView = new ClassificacaoView();
                classificacaoView.pedirClassificacao(leilaoSelecionado);
                return;
            }
        }
    }

    public void verMinhaClassificacao() {
        int idCliente = Tools.clienteSessao.getIdCliente();

        ClassificacaoController controller = new ClassificacaoController();
        List<Leilao> avaliados = controller.listarLeiloesAvaliadosPeloCliente(idCliente);

        if (avaliados.isEmpty()) {
            System.out.println("Ainda não avaliou nenhum leilão.");
            return;
        }

        listarLeiloesAvaliados();

        int idSelecionado = Tools.pedirOpcaoMenu("\nInsere o ID do leilão para ver a tua avaliação " + Tools.alertaCancelar());
        if (idSelecionado == -1) {
            System.out.println("Operação cancelada.");
            return;
        }

        Classificacao classificacao = controller.obterClassificacaoDoCliente(idCliente, idSelecionado);
        if (classificacao == null) {
            System.out.println("Não encontrámos nenhuma avaliação tua para esse leilão.");
        } else {
            System.out.println("\nA tua avaliação:");
            System.out.println("Nota: " + classificacao.getClassificacao());
            System.out.println("Comentário: " + classificacao.getComentario());
        }
    }


}

