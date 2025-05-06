package Model;

import java.time.LocalDate;

public class Agente {
    private int idAgente;
    private int idCliente;
    private int idLeilao;
    private LocalDate dataRegisto;

    public Agente(int idAgente, int idCliente, int idLeilao, LocalDate dataRegisto) {
        this.idAgente = idAgente;
        this.idCliente = idCliente;
        this.idLeilao = idLeilao;
        this.dataRegisto = dataRegisto;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public void setIdLeilao(int idLeilao) {
        this.idLeilao = idLeilao;
    }

    public LocalDate getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(LocalDate dataRegisto) {
        this.dataRegisto = dataRegisto;
    }
}
