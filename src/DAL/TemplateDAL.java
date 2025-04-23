package DAL;

import Model.TemplateModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;

public class TemplateDAL {
    private final String CAMINHO_CSV = "data\\EmailRegisto.csv";

    public TemplateModel carregarTemplate() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(CAMINHO_CSV));
        String linha = reader.readLine();
        reader.close();

        if (linha == null || linha.isBlank()) {
            throw new IOException("Ficheiro de template vazio.");
        }

        // Remover aspas e converter \n e \t para reais
        String[] partes = linha.split(",", 2);
        if (partes.length < 2) {
            throw new IOException("Formato inválido no ficheiro.");
        }

        String assunto = partes[0].trim().replaceAll("^\"|\"$", "").replace("\"\"", "\"");
        String corpo = partes[1].trim()
                .replaceAll("^\"|\"$", "")  // remove aspas exteriores
                .replace("\"\"", "\"")      // repõe aspas duplas reais
                .replace("\\n", "\n")       // converte \n para quebra de linha real
                .replace("\\t", "\t");      // converte \t para tab real

        return new TemplateModel(assunto, corpo);
    }

    public void guardarTemplate(TemplateModel template) throws IOException {
        String assunto = "\"" + template.getAssunto().replace("\"", "\"\"") + "\"";
        String corpo = "\"" + template.getCorpo()
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\"", "\"\"") + "\"";

        String linha = assunto + "," + corpo;

        Files.writeString(Paths.get(CAMINHO_CSV), linha);
    }
}
