package BLL;

import DAL.ExcelDAL;
import DAL.LeilaoDAL;
import DAL.TemplateDAL;
import DAL.UtilizadorDAL;
import Model.Leilao;
import Model.Template;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    public String gerarFicheiro() {
        try {
            ExcelDAL excelDAL = new ExcelDAL();
            return excelDAL.guardarRelatorio();
        } catch (Exception _) {
        }
        return null;
    }

    public void agendarGeracaoRelatorio(LocalTime horaAgendada) {
        EmailBLL emailBLL = new EmailBLL();
        TemplateDAL templateDAL = new TemplateDAL();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador u = utilizadorBLL.procurarUtilizadorPorId(1);
        LocalDateTime proximaExecucao = LocalDateTime.now().with(horaAgendada);

        if (LocalDateTime.now().toLocalTime().isAfter(horaAgendada)) {
            proximaExecucao = proximaExecucao.plusDays(1);
        }

        long delayInicial = Duration.between(LocalDateTime.now(), proximaExecucao).toMillis();
        long intervalo24h = 24 * 60 * 60 * 1000;

        final String[] caminhoFicheiroCriado = new String[1];

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                caminhoFicheiroCriado[0] = gerarFicheiro();
                Template template = null;
                try {
                    template = templateDAL.carregarTemplatePorId(Constantes.templateIds.EMAIL_RELATORIO_DIARIO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    emailBLL.enviarEmailComAnexo(template, u.getEmail(), Tools.substituirTags(u, null, null), u.getId(), caminhoFicheiroCriado[0]);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }, delayInicial, intervalo24h);
    }

}

