package DAL;

import Model.ResultadoOperacao;
import Model.Template;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateDAL {
    private Map<String, Template> templatesCache = null;
    private final String CABECALHO = "\"ID\"" + Tools.separador() +
            "\"Assunto\"" + Tools.separador() + "\"Corpo\"";

    private ResultadoOperacao carregarTodosTemplates() {
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

    public Template carregarTemplatePorId(String idProcurado) throws IOException {
        if (templatesCache == null)
            carregarTodosTemplates();
        return templatesCache.get(idProcurado);
    }

    public void guardarTemplate(String id, Template template) throws IOException {
        if (templatesCache == null) carregarTodosTemplates();
        templatesCache.put(id, template);

        List<String> linhasParaGravar = new ArrayList<>();
        linhasParaGravar.add(CABECALHO);

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
}