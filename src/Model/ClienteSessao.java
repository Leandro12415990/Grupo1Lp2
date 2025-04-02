package Model;

public class ClienteSessao {
    public static int idCliente = -1;

    public static int getIdCliente() {
        return idCliente;
    }

    public static void setIdCliente(int id) {
        idCliente = id;
    }

    public static boolean isClienteLogado() {
        return idCliente != -1;
    }

    public static void logout() {
        idCliente = -1;
    }
}
