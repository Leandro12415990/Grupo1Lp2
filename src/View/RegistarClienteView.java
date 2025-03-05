package View;
import Utils.Tools;

public class RegistarClienteView {
    public void MenuRegistarCliente()
    {
        System.out.println("----------Registar----------");
        System.out.println("Nome:");
        String nome = Tools.scanner.nextLine();
        System.out.println("Morada");
        String morada = Tools.scanner.nextLine();
        System.out.println("Data de Nascimento");
        String nascimento = Tools.scanner.nextLine();
        System.out.println("E-mail");
        String email = Tools.scanner.nextLine();
        System.out.println("Password");
        String password = Tools.scanner.nextLine();
    }
}
