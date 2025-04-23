import Utils.EmailSender;
import View.MenuInicialView;

public class Main {

    public static void main(String[] args) {
        EmailSender.enviarEmailSimples("Pedro","pedromgp06@gmail.com", "Teste", "Este é um e-mail de teste FINAL para o andré");
        MenuInicialView menuInicialView = new MenuInicialView();
        menuInicialView.menuInicial();
        //System.out.println("Email enviado com sucesso!");
    }
}
