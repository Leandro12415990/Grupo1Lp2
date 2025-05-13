import BLL.RelatorioFinalBLL;
import View.MenuInicialView;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) throws IOException, MessagingException {
        MenuInicialView menuInicialView = new MenuInicialView();
        menuInicialView.menuInicial();
    }
}
