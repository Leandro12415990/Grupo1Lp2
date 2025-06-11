package BLL;

import Controller.LanceController;
import Controller.LeilaoController;
import DAL.AgenteDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AgenteBLL {
    private final AgenteDAL agenteDAL = new AgenteDAL();
    private final LeilaoBLL leilaoBLL = new LeilaoBLL();
    private final LanceBLL lanceBLL = new LanceBLL();
    private final Set<Integer> leiloesMonitorizados = ConcurrentHashMap.newKeySet();

    private static final Map<Integer, Set<String>> mensagensAgentePorCliente = new ConcurrentHashMap<>();

    public boolean adicionarAgente(Agente agente) {
        List<Agente> agentes = agenteDAL.carregarAgentes();
        agentes.add(agente);
        return agenteDAL.guardarAgentes(agentes);
    }

    public boolean removerAgente(int idLeilao, int idCliente) {
        List<Agente> agentes = agenteDAL.carregarAgentes();
        boolean removido = agentes.removeIf(a -> a.getLeilaoId() == idLeilao && a.getClienteId() == idCliente);
        return removido && agenteDAL.guardarAgentes(agentes);
    }

    public void ativarAgentesParaLeilao(int idLeilao, int idTipoLeilao) {
        LanceController lanceController = new LanceController();
        LeilaoController leilaoController = new LeilaoController();
        List<Agente> agentes = agenteDAL.carregarAgentesPorLeilao(idLeilao);

        if (agentes == null || agentes.isEmpty()) return;

        Collections.reverse(agentes);

        for (Agente agente : agentes) {
            try {
                Leilao leilao = leilaoController.procurarLeilaoPorId(idLeilao);
                if (leilao == null || leilao.getEstado() != Constantes.estadosLeilao.ATIVO) break;

                List<Lance> lancesOriginais = lanceController.obterLancesPorLeilao(idLeilao);
                if (lancesOriginais == null) lancesOriginais = Collections.emptyList();

                List<Lance> lancesDoLeilao = new ArrayList<>();
                for (Lance lance : lancesOriginais) {
                    if (lance != null) {
                        lancesDoLeilao.add(lance);
                    }
                }

                Lance ultimoLanceObj = lancesDoLeilao.isEmpty() ? null : lancesDoLeilao.get(lancesDoLeilao.size() - 1);

                if (ultimoLanceObj != null && ultimoLanceObj.getIdCliente() == agente.getClienteId()) {
                    continue;
                }

                double ultimoLance = (ultimoLanceObj == null)
                        ? leilao.getValorMinimo()
                        : ultimoLanceObj.getValorLance();

                double proximoLanceEsperado = ultimoLance + leilao.getMultiploLance();

                ResultadoOperacao resultado = lanceBLL.adicionarLanceEletronico(
                        idLeilao, proximoLanceEsperado, agente.getClienteId(), idTipoLeilao
                );

                if (resultado != null && resultado.Sucesso) {
                    leilaoBLL.estenderFimLeilaoSeNecessario(leilao, LocalDateTime.now());
                }

                if (resultado != null && !resultado.Sucesso &&
                        resultado.msgErro.toLowerCase().contains("saldo")) {

                    mensagensAgentePorCliente
                            .computeIfAbsent(agente.getClienteId(), k -> ConcurrentHashMap.newKeySet())
                            .add("Agente " + agente.getId() + " parou por falta de saldo no leilão " + idLeilao);
                }

                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }
    }

    public void iniciarMonitorizacaoDinamica() {
        new Thread(() -> {
            while (true) {
                try {
                    monitorizarNovosLeiloesComAgentes();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void monitorizarNovosLeiloesComAgentes() throws MessagingException, IOException {
        leilaoBLL.carregarLeiloes();
        List<Agente> todosAgentes = agenteDAL.carregarAgentes();
        if (todosAgentes.isEmpty()) return;

        List<Integer> leiloesComAgentes = todosAgentes.stream()
                .map(Agente::getLeilaoId)
                .distinct()
                .toList();

        for (int idLeilao : leiloesComAgentes) {
            Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
            if (leilao == null || leilao.getEstado() != Constantes.estadosLeilao.ATIVO) continue;

            if (!leiloesMonitorizados.add(idLeilao)) {
                continue;
            }

            new Thread(() -> {
                while (true) {
                    try {
                        Leilao leilaoAtivo = leilaoBLL.procurarLeilaoPorId(idLeilao);
                        if (leilaoAtivo == null || leilaoAtivo.getEstado() != Constantes.estadosLeilao.ATIVO) break;

                        ativarAgentesParaLeilao(idLeilao, leilaoAtivo.getTipoLeilao());
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        break;
                    }
                }
                leiloesMonitorizados.remove(idLeilao);
            }).start();
        }
    }

    public int obterProximoId() {
        List<Agente> agentes = agenteDAL.carregarAgentes();
        return agentes.stream().mapToInt(Agente::getId).max().orElse(0) + 1;
    }

    public List<Agente> listarAgentes() {
        return agenteDAL.carregarAgentes();
    }

    public List<Leilao> obterLeiloesDisponiveisParaCliente(int idcliente) throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        LanceController lanceController = new LanceController();

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(Tools.estadoLeilao.fromCodigo(Constantes.estadosLeilao.ATIVO));
        List<Leilao> leiloesEletronicos = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);

        List<Agente> todosAgentes = agenteDAL.carregarAgentes();
        List<Integer> idsLeiloesComAgenteDoCliente = new ArrayList<>();

        for (Agente agente : todosAgentes) {
            if (agente.getClienteId() == Tools.clienteSessao.getIdCliente()) {
                idsLeiloesComAgenteDoCliente.add(agente.getLeilaoId());
            }
        }

        List<Leilao> leiloesDisponiveis = new ArrayList<>();
        for (Leilao leilao : leiloesEletronicos) {
            if (!idsLeiloesComAgenteDoCliente.contains(leilao.getId())) {
                leiloesDisponiveis.add(leilao);
            }
        }

        return leiloesDisponiveis;
    }

    public static void verificarMensagensAgente(int idCliente) {
        Set<String> mensagens = mensagensAgentePorCliente.remove(idCliente);
        if (mensagens != null && !mensagens.isEmpty()) {
            for (String msg : mensagens) {
                System.out.println(msg);
            }
        }
    }
}
