package Model;

public class ClienteSessao {
    private int idCliente = -1;
    private int idTipoCliente = -1;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int id) {
        this.idCliente = id;
    }

    public int getIdTipoCliente() { return idTipoCliente; }

    public void setIdTipoCliente(int id) {this.idTipoCliente = id;}

    public boolean isClienteLogado() {
        return idCliente != -1;
    }

    public void logout() { idCliente = -1;
    }
}
