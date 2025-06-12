package DAL;

import Model.ResultadoOperacao;
import Model.Template;
import Model.Transacao;
import Utils.Constantes.caminhosFicheiros;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateDAL {
    private Map<String, Template> templatesCache = null;

    private ResultadoOperacao carregarTodosTemplatesCSV() {
        templatesCache = new HashMap<>();
        ResultadoOperacao resultado = new ResultadoOperacao();

        Path caminho = Paths.get(caminhosFicheiros.CSV_FILE_TEMPLATE);
        if (!Files.exists(caminho)) {
            resultado.msgErro = "Ficheiro de templates não encontrado: " + caminhosFicheiros.CSV_FILE_TEMPLATE;
            return resultado;
        }

        try (BufferedReader reader = Files.newBufferedReader(caminho)) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    if (!linha.trim().equalsIgnoreCase("\"ID\";\"Assunto\";\"Corpo\"")) {
                        resultado.msgErro = "Cabeçalho do ficheiro inválido.";
                        return resultado;
                    }
                    continue;
                }

                if (linha.isBlank()) continue;

                String[] partes = linha.split(";", 3); // Supondo que seja ";" mesmo
                if (partes.length < 3) continue;

                String id = partes[0].trim().replaceAll("^\"|\"$", "");
                String assunto = partes[1].trim().replaceAll("^\"|\"$", "").replace("\"\"", "\"");
                String corpo = partes[2].trim()
                        .replaceAll("^\"|\"$", "")
                        .replace("\"\"", "\"")
                        .replace("\\n", "\n")
                        .replace("\\t", "\t");

                templatesCache.put(id, new Template(id, assunto, corpo));
            }

            resultado.Sucesso = true;
            resultado.Objeto = resultado;

        } catch (IOException e) {
            resultado.msgErro = "Erro ao ler o ficheiro de templates: " + e.getMessage();
        }

        return resultado;
    }

    public List<Template> carregarTodosTemplates() {
        List<Template> listaTemplate = new ArrayList<>();

        String sql = "SELECT * FROM Template";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                String id_template = rs.getString("id_Template");
                String assunto = rs.getString("Assunto");
                String corpo = rs.getString("Corpo");

                Template template = new Template(id_template, assunto, corpo);
                listaTemplate.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTemplate;
    }

    public Template carregarTemplatePorId(String idProcurado) throws IOException {
        Template templateRetornado = null;

        String sql = "SELECT * FROM Template WHERE id_Template = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, idProcurado);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id_template = rs.getString("id_Template");
                    String assunto = rs.getString("Assunto");
                    String corpo = rs.getString("Corpo");

                    templateRetornado = new Template(id_template, assunto, corpo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return templateRetornado;
    }

    public void guardarTemplateCSV(String id, Template template) throws IOException {
        if (templatesCache == null) carregarTodosTemplates();
        templatesCache.put(id, template);

        List<String> linhasParaGravar = new ArrayList<>();

        for (Map.Entry<String, Template> entry : templatesCache.entrySet()) {
            String idAtual = entry.getKey();
            Template t = entry.getValue();

            String assunto = "\"" + t.getAssunto().replace("\"", "\"\"") + "\"";
            String corpo = "\"" + t.getCorpo()
                    .replace("\n", "\\n")
                    .replace("\t", "\\t")
                    .replace("\"", "\"\"") + "\"";
            String linha = "\"" + idAtual + "\"" + Tools.separador()  + assunto + Tools.separador() + corpo;

            linhasParaGravar.add(linha);
        }
        Files.write(Paths.get(caminhosFicheiros.CSV_FILE_TEMPLATE), linhasParaGravar);
    }

    public void guardarTemplate(String id, Template template) throws IOException {
        String sqlInsert = "INSERT INTO Template (Assunto, Corpo) " +
                "VALUES (?, ?)";

        String sqlUpdate = "UPDATE Template SET Assunto = ?, Corpo = ? " +
                "WHERE Id_Template = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ) {
            if (template.getId() == "0") {
                // INSERT
                stmtInsert.setString(1, template.getId());
                stmtInsert.setString(2, template.getAssunto());
                stmtInsert.setString(3, template.getCorpo());

                stmtInsert.addBatch();
            } else {
                // UPDATE
                stmtUpdate.setString(1, template.getId());
                stmtUpdate.setString(2, template.getAssunto());
                stmtUpdate.setString(3, template.getCorpo());
                stmtUpdate.setString(4, template.getId());

                stmtUpdate.addBatch();
            }

            stmtInsert.executeBatch();
            stmtUpdate.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
