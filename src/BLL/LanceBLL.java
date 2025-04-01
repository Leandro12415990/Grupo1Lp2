package BLL;

import DAL.ImportDal;
import Model.ClienteSessao;
import Model.Lance;
import Model.Leilao;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanceBLL {
    private static List<Lance> lances = new ArrayList<>();

    public static List<Lance> carregarLance(){
        lances = ImportDal.carregarLance();
        return lances;
    }

    public static void adicionarLance(int idLeilao, int idCliente, double valorLance) {

        int idCLiente = ClienteSessao.getIdCliente();
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);

        if (leilao == null) {
            System.out.println("Leilão não encontrado!");
            return;
        }

        if (valorLance != leilao.getValorMinimo()) {
            System.out.println("Erro: O valor do lance deve ser igual ao valor solicitado no leilão.");
            return;
        }

        List<Lance> lances = carregarLance();

        int novoIdLance = verUltimoId(lances) + 1;

        int numLance = 0;
        int pontosUtilizados = 0;

        // Verificar o tipo de leilão e ajustar os valores de numLance e pontosUtilizados
        /*if ("com pontos".equals(leilao.getTipoLeilao())) {
            // Lance com pontos, então usamos os dois campos
            numLance = 1;  // Exemplo, pode ser outro valor dependendo da lógica
            pontosUtilizados = 50;  // Exemplo, define o valor de pontos utilizados
        } else if ("normal".equals(leilao.getTipoLeilao())) {
            // Lance normal, pode ter um valor para numLance, mas sem pontos
            numLance = 1;  // Exemplo de incremento de lance
            pontosUtilizados = 0;  // Não utiliza pontos
        }*/

        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(novoIdLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);

        lances.add(lance);

        ImportDal.gravarLance(lances);

        System.out.println("Lance de " + valorLance + " adicionado com sucesso ao Leilão " + idLeilao);
    }

    public static void adicionarLanceCartaFechada(double valorLance) {
        int idCliente = ClienteSessao.getIdCliente();

        // Buscar os leilões ativos
        List<Leilao> leiloesAtivos = LeilaoBLL.listarLeiloes();
        List<Leilao> leiloesCartaFechada = new ArrayList<>();

        // Filtrar apenas os leilões do tipo "CARTA FECHADA"
        for (Leilao leilao : leiloesAtivos) {
            if (leilao.getTipoLeilao().equalsIgnoreCase("CARTA FECHADA")) {
                leiloesCartaFechada.add(leilao);
            }
        }

        // Se não houver leilões de Carta Fechada, sair do método
        if (leiloesCartaFechada.isEmpty()) {
            System.out.println("❌ Não há leilões de CARTA FECHADA ativos no momento.");
            return;
        }

        // Listar os leilões disponíveis para o usuário escolher
        System.out.println("\n===== LEILÕES ATIVOS - CARTA FECHADA =====");
        for (Leilao leilao : leiloesCartaFechada) {
            System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getIdProduto());
        }

        // Solicitar o ID do leilão ao usuário
        System.out.print("\nDigite o ID do leilão em que deseja participar: ");
        int idLeilao = Tools.scanner.nextInt();

        // Verificar se o ID inserido corresponde a um leilão do tipo "CARTA FECHADA"
        Leilao leilaoSelecionado = null;
        for (Leilao leilao : leiloesCartaFechada) {
            if (leilao.getId() == idLeilao) {
                leilaoSelecionado = leilao;
                break;
            }
        }

        if (leilaoSelecionado == null) {
            System.out.println("❌ O ID digitado não pertence a um leilão do tipo 'CARTA FECHADA'.");
            return;
        }

        // Criar novo lance
        List<Lance> lances = carregarLance();
        int novoIdLance = verUltimoId(lances) + 1;

        // Definir valores nulos/zero para campos não utilizados
        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(novoIdLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);

        lances.add(lance);
        ImportDal.gravarLance(lances);

        System.out.println("✅ Lance de " + valorLance + " adicionado ao Leilão " + idLeilao + " com sucesso.");
    }



    public static void adicionarLanceEletronico(int idLeilao, int idCliente, Integer numLance) {

        int idCliente2 = ClienteSessao.getIdCliente();

        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);

        if (leilao == null) {
            System.out.println("Leilão não encontrado!");
            return;
        }

        double multiploLance = leilao.getMultiploLance();

        if (numLance <= 0) {
            System.out.println("Número de lances inválido! Deve ser maior que zero.");
            return;
        }

        double valorLance = numLance * multiploLance;

        List<Lance> lances = carregarLance();

        int novoIdLance = verUltimoId(lances) + 1;

        int pontosUtilizados = 0;

        LocalDateTime dataLance = LocalDateTime.now();
        Lance lance = new Lance(novoIdLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);

        lances.add(lance);

        ImportDal.gravarLance(lances);

        System.out.println("Lance eletrônico de " + numLance + " lance registado! Valor total: " + valorLance);
    }

    public static int verUltimoId(List<Lance> lances){
        int ultimoId = 0;
        for (Lance lance : lances){
            if (lance.getIdLance() > ultimoId){
                ultimoId = lance.getIdLance();
            }
        }
        return ultimoId;
    }

    public static List<Lance> listarMeuLance(int IdCliente) {
        List<Lance> lances = carregarLance();

        if (lances == null) {
            return new ArrayList<>();
        }

        return lances.stream()
                .filter(lance -> lance.getIdCliente() == IdCliente)
                .collect(Collectors.toList());
    }

    public static List<Lance> obterLancesPorLeilao(int idLeilao) {
        carregarLance();
        List<Lance> lancesLeilao = new ArrayList<>();

        for (Lance lance : carregarLance()) {
            if (lance.getIdLeilao() == idLeilao) {
                lancesLeilao.add(lance);
            }
        }

        return lancesLeilao;
    }


    public static Lance procurarLanceId(int id){
        carregarLance();
        for (Lance lance : lances){
            if (lance.getIdLance() == id){
                return lance;
            }
        }
        return null;
    }
}
