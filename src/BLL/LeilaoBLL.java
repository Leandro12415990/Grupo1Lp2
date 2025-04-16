package BLL;

import DAL.ImportDal;
import Model.Leilao;
import Utils.Constantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private final ImportDal importDal;
    private final List<Leilao> leiloes;

    public LeilaoBLL(ImportDal importDal) {
        this.importDal = importDal;
        this.leiloes = importDal.carregarLeilao();
        atualizarEstados();
    }

    private void atualizarEstados() {
        for (Leilao leilao : leiloes) {
            int novoEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), leilao.getDataFim(), leilao.getEstado());
            if (leilao.getEstado() != novoEstado) {
                leilao.setEstado(novoEstado);
            }
        }
        importDal.gravarLeilao(leiloes);
    }

    public void adicionarLeilao(Leilao leilao) {
        leilao.setId(verificarUltimoId() + 1);
        leiloes.add(leilao);
        importDal.gravarLeilao(leiloes);
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
        if (!apenasDisponiveis) return new ArrayList<>(leiloes);

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
        leiloes.remove(leilao);
        importDal.gravarLeilao(leiloes);
    }

    public boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
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
            importDal.gravarLeilao(leiloes);
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
        Leilao leilao = procurarLeilaoPorId(idLeilao);
        if (leilao != null) {
            leilao.setDataFim(dataFim);
            leilao.setEstado(determinarEstadoLeilaoByDatas(leilao.getDataInicio(), dataFim, leilao.getEstado()));
            importDal.gravarLeilao(leiloes);
        }
    }

}
