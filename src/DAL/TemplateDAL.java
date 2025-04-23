package DAL;

import Model.TemplateModel;

import java.io.IOException;
import java.nio.file.*;

public class TemplateDAL {

    public TemplateModel carregarTemplate(String caminho) throws IOException {
        String conteudo = Files.readString(Paths.get(caminho));
        String[] partes = conteudo.split("\n", 2);
        String assunto = partes[0].replace("Assunto:", "").trim();
        String corpo = partes.length > 1 ? partes[1].trim() : "";
        return new TemplateModel(assunto, corpo);
    }

    public void guardarTemplate(String caminho, TemplateModel template) throws IOException {
        String conteudo = "Assunto: " + template.getAssunto() + "\n" + template.getCorpo();
        Files.writeString(Paths.get(caminho), conteudo);
    }
}
