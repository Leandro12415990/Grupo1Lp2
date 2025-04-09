package BLL;

import DAL.ImportDal;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LanceBLL {
    private static List<Lance> lances = new ArrayList<>();
    private static List<Utilizador> utilizadors = new ArrayList<>();

    public static List<Lance> carregarLance() {
        lances = ImportDal.carregarLance();
        return lances;
    }

    public static ResultadoOperacao adicionarLanceDireto(int idLance, int idLeilao, double valorLance, int idCliente, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();


        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idTipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        ImportDal.gravarLance(lances);

        if (valorLance == leilao.getValorMinimo()) {
            fimLeilao(idLeilao, dataLance);
        }

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceCartaFechada(int idLance, int idLeilao, double valorLance, int idCliente, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idTipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        ImportDal.gravarLance(lances);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public static ResultadoOperacao adicionarLanceEletronico(int idLance, int idLeilao, double valorLance, int numLance, double multiploLance, int idCliente, int valorLanceAtual, int idtipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        valorLance += (multiploLance * numLance);

        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idtipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        List<Lance> lances = carregarLance();
        idLance = verUltimoId(lances) + 1;
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idtipoLeilao);

        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        ImportDal.gravarLance(lances);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }


    public static int verUltimoId(List<Lance> lances) {
        int ultimoId = 0;
        for (Lance lance : lances) {
            if (lance.getIdLance() > ultimoId) {
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
        List<Lance> lances = carregarLance();

        if (lances == null) {
            return new ArrayList<>();
        }

        return lances.stream()
                .filter(lance -> lance.getIdLeilao() == idLeilao)
                .collect(Collectors.toList());
    }

    public static void fimLeilao(int idLeilao, LocalDateTime dataFim) {

        LeilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
    }

    public static ResultadoOperacao verificarSaldoEAtualizar(int idCliente, double valor, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        Utilizador utilizador = UtilizadorBLL.procurarUtilizadorPorId(idCliente);

        if (utilizador.getSaldo() < valor) {
            resultado.Sucesso = false;
            resultado.msgErro = "Saldo insuficiente.";
            return resultado;
        }

        double novoSaldo = utilizador.getSaldo() - valor;
        utilizador.setSaldo(novoSaldo);

        List<Utilizador> utilizadores = Tools.utilizadores;
        for (int i = 0; i < utilizadores.size(); i++) {
            if (utilizadores.get(i).getId() == idCliente) {
                utilizadores.set(i, utilizador);
                break;
            }
        }
        Transacao transacao = obterTipoEEstadoPorLeilao(idCliente,novoSaldo,valor,idTipoLeilao);

        TransacaoBLL.criarTransacao(transacao);

        ImportDal.gravarUtilizador(utilizadores);

        resultado.Sucesso = true;
        resultado.msgErro = null;
        return resultado;
    }

    public static Transacao obterTipoEEstadoPorLeilao (int idCliente, double saldo, double valorTransacao,int tipoLeilao){
        int idEstadoTransacao = 0;
        int idTipoTransacao = 0;
        switch (tipoLeilao) {
            case Constantes.tiposLeilao.VENDA_DIRETA, Constantes.tiposLeilao.ELETRONICO:
                idTipoTransacao = Constantes.tiposTransacao.LANCE_DEBITO;
                idEstadoTransacao= Constantes.estadosTransacao.ACEITE;
                break;
            case Constantes.tiposLeilao.CARTA_FECHADA:
                idTipoTransacao = Constantes.tiposTransacao.LANCE_DEPOSITO;
                idEstadoTransacao = Constantes.estadosTransacao.PENDENTE;
                break;
            default:
                break;
        }
        return new Transacao(0, idCliente,saldo,valorTransacao,LocalDateTime.now(),idTipoTransacao,idEstadoTransacao);
    }


}


