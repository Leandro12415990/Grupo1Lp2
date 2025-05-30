package BLL;

import Controller.LeilaoController;
import DAL.LanceDAL;
import DAL.LeilaoDAL;
import Model.Lance;
import Model.Leilao;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private static List<Leilao> leiloes = new ArrayList<>();

    public List<Leilao> carregarLeiloes() throws MessagingException, IOException {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        LeilaoController leilaoController = new LeilaoController();
        leiloes = leilaoDAL.carregaLeiloes();
        int idEstado;
        for (Leilao leilao : leiloes) {
            idEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), leilao.getDataFim(), leilao.getEstado());
            if (leilao.getEstado() != idEstado && idEstado == Constantes.estadosLeilao.FECHADO && leilao.getTipoLeilao() == Constantes.tiposLeilao.CARTA_FECHADA)
                leilaoController.fecharLeilao(leilao.getId(), leilao.getDataFim());
            leilao.setEstado(idEstado);
            leilaoDAL.gravarLeiloes(leiloes);
        }
        return leiloes;
    }

    public void adicionarLeilao(Leilao leilao) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        leiloes = leilaoDAL.carregaLeiloes();
        int novoId = gerarProximoId();
        leilao.setId(novoId);
        leiloes.add(leilao);
        leilaoDAL.gravarLeiloes(leiloes);
    }

    private int gerarProximoId() {
        int ultimoId = 0;
        for (Leilao leilao : leiloes) {
            if (leilao.getId() > ultimoId) ultimoId = leilao.getId();
        }
        return ultimoId + 1;
    }

    public List<Leilao> listarLeiloes(Tools.estadoLeilao estado) throws MessagingException, IOException {
        carregarLeiloes();
        List<Leilao> leilaosEmpty = new ArrayList<>();
        if (estado != Tools.estadoLeilao.DEFAULT) {
            if (estado == Tools.estadoLeilao.ATIVO) {
                List<Leilao> ativos = new ArrayList<>();
                for (Leilao leilao : leiloes) {
                    if (leilao.getEstado() == Constantes.estadosLeilao.ATIVO) ativos.add(leilao);
                }
                return ativos;
            } else if (estado == Tools.estadoLeilao.FECHADO) {
                List<Leilao> fechados = new ArrayList<>();
                for (Leilao leilao : leiloes) {
                    if (leilao.getEstado() == Constantes.estadosLeilao.FECHADO) fechados.add(leilao);
                }
                return fechados;
            }
        } else {
            List<Leilao> todos = new ArrayList<>();
            for (Leilao leilao : leiloes) todos.add(leilao);
            return todos;
        }
        return leilaosEmpty;
    }

    public Leilao procurarLeilaoPorId(int id) {
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == id) return leilao;
        }
        return null;
    }

    public void eliminarLeilao(Leilao leilao) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        leiloes.remove(leilao);
        leilaoDAL.gravarLeiloes(leiloes);
    }

    public boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        Leilao leilao = procurarLeilaoPorId(id);
        if (leilao != null) {
            leilao.setIdProduto(idProduto);
            leilao.setDescricao(descricao);
            leilao.setTipoLeilao(idTipoLeilao);
            leilao.setDataInicio(dataInicio);
            leilao.setDataFim(dataFim);
            leilao.setValorMinimo(valorMin);
            leilao.setValorMaximo(valorMax);
            leilao.setMultiploLance(multiploLance);
            leilao.setEstado(idEstado);
            leilaoDAL.gravarLeiloes(leiloes);
            return true;
        }
        return false;
    }

    public int determinarEstadoLeilaoByDatas(LocalDateTime dataInicio, LocalDateTime dataFim, int idEstado) {
        if (idEstado == Constantes.estadosLeilao.INATIVO || idEstado == Constantes.estadosLeilao.CANCELADO)
            return idEstado;

        if (dataFim != null && dataFim.isBefore(LocalDateTime.now())) {
            return Constantes.estadosLeilao.FECHADO;
        }
        if (dataInicio.isBefore(LocalDateTime.now()) || dataInicio.equals(LocalDateTime.now())) {
            return Constantes.estadosLeilao.ATIVO;
        }
        return Constantes.estadosLeilao.PENDENTE;
    }

    public boolean atualizarLeilao(Leilao leilaoAtualizado) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        List<Leilao> leiloes = leilaoDAL.carregaLeiloes();

        boolean atualizado = false;
        for (int i = 0; i < leiloes.size(); i++) {
            if (leiloes.get(i).getId() == leilaoAtualizado.getId()) {
                leiloes.set(i, leilaoAtualizado);
                atualizado = true;
                break;
            }
        }
        if (atualizado) leilaoDAL.gravarLeiloes(leiloes);
        return atualizado;
    }

    public List<Leilao> listarLeiloesTerminadosComLancesDoCliente(int idCliente) throws MessagingException, IOException {
        carregarLeiloes();
        LanceDAL lanceDAL = new LanceDAL();

        List<Leilao> leiloesFechados = listarLeiloes(Tools.estadoLeilao.FECHADO);
        List<Lance> lances = lanceDAL.carregarLances();
        Tools.inicializarUltimoIdLance(lances);
        List<Leilao> resultado = new ArrayList<>();

        for (Leilao leilao : leiloesFechados) {
            boolean clienteParticipou = false;
            for (Lance lance : lances) {
                if (lance.getIdCliente() == idCliente && lance.getIdLeilao() == leilao.getId()) {
                    clienteParticipou = true;
                    break;
                }
            }
            if (clienteParticipou) resultado.add(leilao);
        }
        return resultado;
    }

    public boolean estenderFimLeilaoSeNecessario(Leilao leilao, LocalDateTime dataLance) {
        LocalDateTime fimAtual = leilao.getDataFim();

        if (dataLance.isAfter(fimAtual.minusSeconds(15)) && dataLance.isBefore(fimAtual)) {
            LocalDateTime novoFim = fimAtual.plusSeconds(15);
            leilao.setDataFim(novoFim);

            // Corrigido: carrega a lista atualizada antes de gravar
            LeilaoDAL leilaoDAL = new LeilaoDAL();
            List<Leilao> leiloesAtualizados = leilaoDAL.carregaLeiloes();

            for (int i = 0; i < leiloesAtualizados.size(); i++) {
                if (leiloesAtualizados.get(i).getId() == leilao.getId()) {
                    leiloesAtualizados.set(i, leilao); // atualiza o leilão correto
                    break;
                }
            }

            leilaoDAL.gravarLeiloes(leiloesAtualizados); // grava a lista completa atualizada
            System.out.println("✅ Estendendo fim do leilão de " + fimAtual + " para " + novoFim);
            return true;
        } else {
            //System.out.println("❌ Lance fora da janela final de 15 segundos. Não estendendo tempo.");
            return false;
        }
    }



    public boolean fecharLeilao(int idLeilao, LocalDateTime dataFim) throws MessagingException, IOException {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        List<Leilao> todosLeiloes = leilaoDAL.carregaLeiloes();

        for (Leilao leilao : todosLeiloes) {
            if (leilao.getId() == idLeilao) {
                leilao.setDataFim(dataFim);
                leilao.setEstado(Constantes.estadosLeilao.FECHADO); // Atualiza o estado, se necessário
                leilaoDAL.gravarLeiloes(todosLeiloes); // Grava corretamente a lista
                return true;
            }
        }
        return false;
    }

}


