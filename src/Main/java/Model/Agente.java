package Model;

import java.time.LocalDateTime;

public class Agente {
    private int id;
    private int clienteId;
    private int leilaoId;
    private LocalDateTime dataConfiguracao;

    public Agente(int id, int clienteId, int leilaoId, LocalDateTime dataConfiguracao) {
        this.id = id;
        this.clienteId = clienteId;
        this.leilaoId = leilaoId;
        this.dataConfiguracao = dataConfiguracao;
    }

    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public int getLeilaoId() { return leilaoId; }
    public LocalDateTime getDataConfiguracao() { return dataConfiguracao; }
}
