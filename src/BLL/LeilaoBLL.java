package BLL;

import Controller.LeilaoController;
import DAL.LeilaoDAL;
import Model.Leilao;
import Utils.Constantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private static List<Leilao> leiloes = new ArrayList<>();

    public List<Leilao> carregarLeiloes() {
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

    private void atualizarEstados() {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        for (Leilao leilao : leiloes) {
            int novoEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), leilao.getDataFim(), leilao.getEstado());
            if (leilao.getEstado() != novoEstado) {
                leilao.setEstado(novoEstado);
                leilaoDAL.gravarLeiloes(leiloes);
            }
        }
        leilaoDAL.gravarLeiloes(leiloes);
    }

    public void adicionarLeilao(Leilao leilao) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        leiloes.add(leilao);
        leilaoDAL.gravarLeiloes(leiloes);
    }

    private int verificarUltimoId() {
        int ultimoId = 0;
        for (Leilao leilao : leiloes) {
            if (leilao.getId() > ultimoId) {
                ultimoId = leilao.getId();
            }
        }
        return ultimoId;
    }

    public List<Leilao> listarLeiloes(boolean apenasDisponiveis) {
        if (!apenasDisponiveis) return carregarLeiloes();

        List<Leilao> ativos = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            if (leilao.getEstado() == Constantes.estadosLeilao.ATIVO) {
                ativos.add(leilao);
            }
        }
        return ativos;
    }

    public Leilao procurarLeilaoPorId(int id) {
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == id) {
                return leilao;
            }
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
            leilao.setValorAtualLanceEletronico(multiploLance);
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

    public void colocarDataFimLeilao(int idLeilao, LocalDateTime dataFim) {
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        List<Leilao> leiloes = leilaoDAL.carregaLeiloes();
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == idLeilao) {
                leilao.setDataFim(dataFim);
                int novoEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), dataFim, leilao.getEstado());
                leilao.setEstado(novoEstado);
                break;
            }
        }
        leilaoDAL.gravarLeiloes(leiloes);
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

        if (atualizado) {
            leilaoDAL.gravarLeiloes(leiloes);
        }

        return atualizado;
    }

}
