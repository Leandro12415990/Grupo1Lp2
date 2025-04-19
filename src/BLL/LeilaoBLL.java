package BLL;

import Controller.LeilaoController;
import Model.Lance;
import Model.Leilao;
import DAL.ImportDal;
import Utils.Constantes;
import View.LanceView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private static List<Leilao> leiloes = new ArrayList<>();

    public static List<Leilao> carregarLeiloes() {
        ImportDal importDal = new ImportDal();
        leiloes = importDal.carregarLeilao();
        int idEstado;
        for (Leilao leilao : leiloes) {
            idEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), leilao.getDataFim(), leilao.getEstado());
            if (leilao.getEstado() != idEstado && idEstado == Constantes.estadosLeilao.FECHADO && leilao.getTipoLeilao() == Constantes.tiposLeilao.CARTA_FECHADA)
                LeilaoController.fecharLeilao(leilao.getId(), leilao.getDataFim());
            leilao.setEstado(idEstado);
            importDal.gravarLeilao(leiloes);
        }
        return leiloes;
    }

    public static void adicionarLeilao(Leilao leilao) {
        ImportDal importDal = new ImportDal();
        carregarLeiloes();
        leilao.setId(verificarUltimoId(leiloes) + 1);
        leiloes.add(leilao);
        importDal.gravarLeilao(leiloes);
    }

    private static int verificarUltimoId(List<Leilao> leiloes) {
        int ultimoId = 0;
        for (Leilao leilao : leiloes) {
            if (leilao.getId() > ultimoId) {
                ultimoId = leilao.getId();
            }
        }
        return ultimoId;
    }

    public static List<Leilao> listarLeiloes(boolean apenasDisponiveis) {
        carregarLeiloes();
        if (!apenasDisponiveis) {
            return leiloes;
        }
        List<Leilao> leiloesAtivos = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            if (leilao.getEstado() == Constantes.estadosLeilao.ATIVO) {
                leiloesAtivos.add(leilao);
            }
        }
        return leiloesAtivos;
    }

    public static Leilao procurarLeilaoPorId(int Id) {
        carregarLeiloes();
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == Id) {
                return leilao;
            }
        }
        return null;
    }

    public static void eliminarLeilao(Leilao leilao) {
        ImportDal importDal = new ImportDal();
        leiloes.remove(leilao);
        importDal.gravarLeilao(leiloes);
    }

    public static boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        ImportDal importDal = new ImportDal();
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

    public static int determinarEstadoLeilaoByDatas(LocalDateTime dataInicio, LocalDateTime dataFim, int idEstado) {
        if (idEstado != Constantes.estadosLeilao.INATIVO || idEstado != Constantes.estadosLeilao.CANCELADO) {
            if (dataFim != null) {
                if (dataFim.isBefore(LocalDateTime.now())) {
                    return Constantes.estadosLeilao.FECHADO;
                }
            }
            if (dataInicio.isBefore(LocalDateTime.now()) || dataInicio.equals(LocalDateTime.now())) {
                return Constantes.estadosLeilao.ATIVO;
            }
            if (dataInicio.isAfter(LocalDateTime.now())) {
                return Constantes.estadosLeilao.PENDENTE;
            }
            return Constantes.estadosLeilao.PENDENTE;
        } else return idEstado;
    }

    public static void colocarDataFimLeilao(int idLeilao, LocalDateTime dataFim) {
        ImportDal importDal = new ImportDal();
        List<Leilao> leiloes = importDal.carregarLeilao();
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == idLeilao) {
                leilao.setDataFim(dataFim);
                int novoEstado = determinarEstadoLeilaoByDatas(leilao.getDataInicio(), dataFim, leilao.getEstado());
                leilao.setEstado(novoEstado);
                break;
            }
        }
        importDal.gravarLeilao(leiloes);
    }

}
