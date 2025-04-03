package Model;

import java.time.LocalDateTime;

public class Carteira {
    private int idDeposito;
    private int idCliente;
    private Double valorTotal;
    private Double valorDeposito;
    private LocalDateTime dataDeposito;
    private int idEstadoDeposito;

    public Carteira(int idDeposito, int idCliente, Double valorTotal, Double valorDeposito, LocalDateTime dataDeposito, int idEstadoDeposito) {
        this.idDeposito = idDeposito;
        this.idCliente = idCliente;
        this.valorTotal = valorTotal;
        this.valorDeposito = valorDeposito;
        this.dataDeposito = dataDeposito;
        this.idEstadoDeposito = idEstadoDeposito;
    }

    public int getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorDeposito() {
        return valorDeposito;
    }

    public void setValorDeposito(Double valorDeposito) {
        this.valorDeposito = valorDeposito;
    }

    public LocalDateTime getDataDeposito() {
        return dataDeposito;
    }

    public void setDataDeposito(LocalDateTime dataDeposito) {
        this.dataDeposito = dataDeposito;
    }

    public int getIdEstadoDeposito() {
        return idEstadoDeposito;
    }

    public void setIdEstadoDeposito(int idEstadoDeposito) {
        this.idEstadoDeposito = idEstadoDeposito;
    }
}
