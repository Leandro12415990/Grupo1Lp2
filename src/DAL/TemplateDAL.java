package DAL;

import Model.TemplateModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TemplateDAL {
    private final String CAMINHO_CSV = "data\\EmailRegisto.csv";
    private Map<String, TemplateModel> templatesCache = null;
    private final String CABECALHO = "\"ID\",\"Assunto\",\"Corpo\"";

    private void carregarTodosTemplates() throws IOException {
        templatesCache = new HashMap<>();

        Path caminho = Paths.get(CAMINHO_CSV);
        if (!Files.exists(caminho)) {
            throw new IOException("Ficheiro de templates não encontrado: " + CAMINHO_CSV);
        }

        BufferedReader reader = Files.newBufferedReader(caminho);
        String linha;
        boolean primeiraLinha = true;

        while ((linha = reader.readLine()) != null) {
            if (primeiraLinha) {
                primeiraLinha = false;
                if (!linha.equals(CABECALHO)) {
                    throw new IOException("Cabeçalho do ficheiro inválido.");
                }
                continue;
            }

            if (linha.isBlank()) continue;

            String[] partes = linha.split(",", 3);
            if (partes.length < 3) continue;

            String id = partes[0].trim().replaceAll("^\"|\"$", "");
            String assunto = partes[1].trim().replaceAll("^\"|\"$", "").replace("\"\"", "\"");
            String corpo = partes[2].trim()
                    .replaceAll("^\"|\"$", "")
                    .replace("\"\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\t", "\t");

            templatesCache.put(id, new TemplateModel(id, assunto, corpo));
        }

        reader.close();
    }

    public TemplateModel carregarTemplatePorId(String idProcurado) throws IOException {
        if (templatesCache == null) {
            carregarTodosTemplates();
        }

        TemplateModel template = templatesCache.get(idProcurado);
        if (template == null) {
            throw new IOException("Template com ID " + idProcurado + " não encontrado.");
        }

        return template;
    }

    public void guardarTemplate(String id, TemplateModel template) throws IOException {
        if (templatesCache == null) {
            carregarTodosTemplates();
        }

        templatesCache.put(id, template);

        List<String> linhasParaGravar = new ArrayList<>();
        linhasParaGravar.add(CABECALHO);

        for (Map.Entry<String, TemplateModel> entry : templatesCache.entrySet()) {
            String idAtual = entry.getKey();
            TemplateModel t = entry.getValue();

            String assunto = "\"" + t.getAssunto().replace("\"", "\"\"") + "\"";
            String corpo = "\"" + t.getCorpo()
                    .replace("\n", "\\n")
                    .replace("\t", "\\t")
                    .replace("\"", "\"\"") + "\"";
            String linha = "\"" + idAtual + "\"," + assunto + "," + corpo;

            linhasParaGravar.add(linha);
        }

        Files.write(Paths.get(CAMINHO_CSV), linhasParaGravar);
    }
}
