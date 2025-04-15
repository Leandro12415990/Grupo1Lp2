package Model;

import java.time.LocalDateTime;

public class Transacao {
    private int idTransacao;
    private int idCliente;
    private Double valorTotal;
    private Double valorTransacao;
    private LocalDateTime dataTransacao;
    private int idTipoTransacao;
    private int idEstadoTransacao;

    public Transacao(int idTransacao, int idCliente, Double valorTotal, Double valorTransacao, LocalDateTime dataTransacao, int idTipoTransacao, int idEstadoTransacao) {
        this.idTransacao = idTransacao;
        this.idCliente = idCliente;
        this.valorTotal = valorTotal;
        this.valorTransacao = valorTransacao;
        this.dataTransacao = dataTransacao;
        this.idTipoTransacao = idTipoTransacao;
        this.idEstadoTransacao = idEstadoTransacao;
    }

    public int getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
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

    public Double getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(Double valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public int getIdTipoTransacao() {
        return idTipoTransacao;
    }

    public void setIdTipoTransacao(int idTipoTransacao) {
        this.idTipoTransacao = idTipoTransacao;
    }

    public int getIdEstadoTransacao() {
        return idEstadoTransacao;
    }

    public void setIdEstadoTransacao(int idEstadoTransacao) {
        this.idEstadoTransacao = idEstadoTransacao;
    }
}
