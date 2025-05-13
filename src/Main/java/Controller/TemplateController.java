package Controller;

import BLL.EmailBLL;
import DAL.TemplateDAL;
import Model.TemplateModel;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public class TemplateController {

    private final TemplateDAL templateDAL;
    private final EmailBLL emailBLL;

    public TemplateController() {
        this.templateDAL = new TemplateDAL();
        this.emailBLL = new EmailBLL();
    }

    public TemplateModel obterTemplatePorId(String idTemplate) throws IOException {
        return templateDAL.carregarTemplatePorId(idTemplate);
    }

    public void guardarTemplate(String idTemplate, TemplateModel template) throws IOException {
        templateDAL.guardarTemplate(idTemplate, template);
    }

    public void enviarEmail(String templateId, String toEmail, Map<String, String> variaveis) throws IOException, MessagingException {
        emailBLL.enviarEmail(templateId, toEmail, variaveis);
    }

    public void enviarEmail(TemplateModel template, String toEmail, Map<String, String> variaveis) throws MessagingException {
        emailBLL.enviarEmail(template, toEmail, variaveis);
    }
}
