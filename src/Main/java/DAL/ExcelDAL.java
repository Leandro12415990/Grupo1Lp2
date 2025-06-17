package DAL;

import BLL.ProdutoBLL;
import BLL.RelatorioFinalBLL;
import Model.Leilao;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelDAL {

    public String guardarRelatorio() throws IOException, MessagingException {
        Workbook workbook = new XSSFWorkbook();
        RelatorioFinalBLL relatorioBLL = new RelatorioFinalBLL();
        ProdutoBLL produtoBLL = new ProdutoBLL();

        List<Leilao> leiloesTerminados = relatorioBLL.obterLeiloesTerminadosOntem();
        List<Leilao> leiloesHoje = relatorioBLL.obterLeiloesIniciadosHoje();
        List<Utilizador> utilizadoresLogadosOntem = relatorioBLL.clienteLogadoOntem();
        List<Utilizador> utilizadoresPendentes = relatorioBLL.pendenteRegisto();

        DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dataSimplesFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // ABA 1 - Leilões Terminados Ontem
        Sheet leiloesSheet = workbook.createSheet("Leilões Terminados Ontem");
        Row header = leiloesSheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Nome Produto");
        header.createCell(2).setCellValue("Tipo de Leilão");
        header.createCell(3).setCellValue("Data Início");
        header.createCell(4).setCellValue("Data Fim");

        int rowNum = 1;
        for (Leilao leilao : leiloesTerminados) {
            Row row = leiloesSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(leilao.getId());
            String nomeProduto = produtoBLL.getNomeProdutoById(leilao.getIdProduto());
            row.createCell(1).setCellValue(nomeProduto != null ? nomeProduto : "N/A");
            row.createCell(2).setCellValue(Tools.tipoLeilao.fromCodigo(leilao.getTipoLeilao()).name());
            row.createCell(3).setCellValue(leilao.getDataInicio() != null ? leilao.getDataInicio().format(dataHoraFormatter) : "N/A");
            row.createCell(4).setCellValue(leilao.getDataFim() != null ? leilao.getDataFim().format(dataHoraFormatter) : "N/A");
        }

        // ABA 2 - Leilões Iniciados Hoje
        Sheet leiloesHojeSheet = workbook.createSheet("Leilões Iniciados Hoje");
        Row header2 = leiloesHojeSheet.createRow(0);
        header2.createCell(0).setCellValue("ID");
        header2.createCell(1).setCellValue("Nome Produto");
        header2.createCell(2).setCellValue("Tipo de Leilão");
        header2.createCell(3).setCellValue("Data Início");
        header2.createCell(4).setCellValue("Data Fim");

        int rowHoje = 1;
        for (Leilao leilao : leiloesHoje) {
            Row row = leiloesHojeSheet.createRow(rowHoje++);
            row.createCell(0).setCellValue(leilao.getId());
            String nomeProduto = produtoBLL.getNomeProdutoById(leilao.getIdProduto());
            row.createCell(1).setCellValue(nomeProduto != null ? nomeProduto : "N/A");
            row.createCell(2).setCellValue(Tools.tipoLeilao.fromCodigo(leilao.getTipoLeilao()).name());
            row.createCell(3).setCellValue(leilao.getDataInicio() != null ? leilao.getDataInicio().format(dataHoraFormatter) : "N/A");
            row.createCell(4).setCellValue(leilao.getDataFim() != null ? leilao.getDataFim().format(dataHoraFormatter) : "N/A");
        }

        // ABA 3 - Clientes Pendentes
        Sheet clientePendente = workbook.createSheet("Cliente Pendente Registo");
        Row clientePendenteHeader = clientePendente.createRow(0);
        clientePendenteHeader.createCell(0).setCellValue("ID Cliente");
        clientePendenteHeader.createCell(1).setCellValue("Nome");
        clientePendenteHeader.createCell(2).setCellValue("Data Registo");

        int rowPendente = 1;
        for (Utilizador utilizador : utilizadoresPendentes) {
            Row row = clientePendente.createRow(rowPendente++);
            row.createCell(0).setCellValue(utilizador.getId());
            row.createCell(1).setCellValue(utilizador.getNomeUtilizador());
            row.createCell(2).setCellValue(utilizador.getDataRegisto() != null ? utilizador.getDataRegisto().format(dataSimplesFormatter) : "N/A");
        }

        // ABA 4 - Clientes Logados Ontem
        Sheet loginOntem = workbook.createSheet("Cliente Logado Ontem");
        Row loginOntemHeader = loginOntem.createRow(0);
        loginOntemHeader.createCell(0).setCellValue("ID Cliente");
        loginOntemHeader.createCell(1).setCellValue("Nome Cliente");

        int rowLogin = 1;
        for (Utilizador utilizador : utilizadoresLogadosOntem) {
            Row row = loginOntem.createRow(rowLogin++);
            row.createCell(0).setCellValue(utilizador.getId());
            row.createCell(1).setCellValue(utilizador.getNomeUtilizador());
        }

        // Gerar nome de ficheiro com Data/hora
        DateTimeFormatter nomeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String timestamp = LocalDateTime.now().format(nomeFormatter);
        String nomeFicheiro = "Relatorio Diario - " + timestamp + ".xlsx";
        String caminhoArquivo = Constantes.caminhosFicheiros.CSV_FILE_FICHEIRO_EMAIL + nomeFicheiro;

        FileOutputStream fileOut = new FileOutputStream(new File(caminhoArquivo));
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        return caminhoArquivo;
    }
}
