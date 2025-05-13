package BLL;

import DAL.LeilaoDAL;
import DAL.UtilizadorDAL;
import Model.Leilao;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioFinalBLL {

    private final LeilaoDAL leilaoDAL;
    private final UtilizadorDAL utilizadorDAL;

    public RelatorioFinalBLL() {
        this.leilaoDAL = new LeilaoDAL();
        this.utilizadorDAL = new UtilizadorDAL();
    }

    public List<Leilao> obterLeiloesTerminadosOntem() {
        List<Leilao> terminadosOntem = new ArrayList<>();
        LocalDate ontem = LocalDate.now().minusDays(1);

        for (Leilao leilao : leilaoDAL.carregaLeiloes()) {
            if (leilao.getDataFim() != null &&
                    leilao.getEstado() == Constantes.estadosLeilao.FECHADO &&
                    leilao.getDataFim().toLocalDate().isEqual(ontem)) {
                terminadosOntem.add(leilao);
            }
        }
        return terminadosOntem;
    }

    public List<Leilao> obterLeiloesIniciadosHoje() {
        List<Leilao> iniciadosHoje = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        for (Leilao leilao : leilaoDAL.carregaLeiloes()) {
            if (leilao.getDataInicio() != null &&
                    leilao.getDataInicio().toLocalDate().isEqual(hoje)) {
                iniciadosHoje.add(leilao);
            }
        }
        return iniciadosHoje;
    }

    public List<Utilizador> clienteLogadoOntem() {
        List<Utilizador> logadosOntem = new ArrayList<>();
        LocalDate ontem = LocalDate.now().minusDays(1);

        for (Utilizador utilizador : utilizadorDAL.carregarUtilizadores()) {
            if (utilizador.getUltimoLogin() != null &&
                    utilizador.getUltimoLogin().isEqual(ontem)) {
                logadosOntem.add(utilizador);
            }
        }
        return logadosOntem;
    }

    public List<Utilizador> pendenteRegisto() {
        List<Utilizador> pendentes = new ArrayList<>();

        for (Utilizador utilizador : utilizadorDAL.carregarUtilizadores()) {
            if (utilizador.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo()) {
                pendentes.add(utilizador);
            }
        }
        return pendentes;
    }
}
