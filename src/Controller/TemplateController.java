package Controller;

import BLL.EmailBLL;
import DAL.TemplateDAL;
import Model.Template;

import java.io.IOException;

public class TemplateController {

    private final TemplateDAL templateDAL;
    private final EmailBLL emailBLL;

    public TemplateController() {
        this.templateDAL = new TemplateDAL();
        this.emailBLL = new EmailBLL();
    }

    public Template obterTemplatePorId(String idTemplate) throws IOException {
        return templateDAL.carregarTemplatePorId(idTemplate);
    }

    public void guardarTemplate(String idTemplate, Template template) throws IOException {
        templateDAL.guardarTemplate(idTemplate, template);
    }

}
