import Utils.EmailSender;
import View.MenuInicialView;


public class Main {

    public static void main(String[] args) {
        EmailSender.enviarEmailSimples("pedromgp06@gmail.com", "Teste", "Este é um e-mail de teste FINAL para o andré");
        MenuInicialView.menuInicial();
        System.out.println("Email enviado com sucesso!");
    }
}
