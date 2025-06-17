package Model;

import java.time.LocalDateTime;

public class Lance {
    private int idLance;
    private int idLeilao;
    private int idCliente;
    private double valorLance;
    private int idNegociacao;
    private double valorContraProposta;
    private LocalDateTime dataLance;
    private int estado;

    public Lance(int idLance, int idLeilao, int idCliente,
                 double valorLance, int idNegociacao, double valorContraProposta, LocalDateTime dataLance, int estado) {

        this.idLance = idLance;
        this.idLeilao = idLeilao;
        this.idCliente = idCliente;
        this.valorLance = valorLance;
        this.idNegociacao = idNegociacao;
        this.valorContraProposta = valorContraProposta;
        this.dataLance = dataLance;
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdLance() {
        return idLance;
    }

    public void setIdLance(int idLance) {
        this.idLance = idLance;
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public void setIdLeilao(int idLeilao) {
        this.idLeilao = idLeilao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getValorLance() {
        return valorLance;
    }

    public void setValorLance(double valorLance) {
        this.valorLance = valorLance;
    }

    public int getIdNegociacao() {
        return idNegociacao;
    }

    public void setIdNegociacao(int idNegociacao) {
        this.idNegociacao = idNegociacao;
    }

    public double getValorContraProposta() {
        return valorContraProposta;
    }

    public void setValorContraProposta(double valorContraProposta) {
        this.valorContraProposta = valorContraProposta;
    }

    public LocalDateTime getDataLance() {
        return dataLance;
    }

    public void setDataLance(LocalDateTime dataLance) {
        this.dataLance = dataLance;
    }

    @Override
    public String toString() {
        return "Lance{" +
                "idLance=" + idLance +
                ", idLeilao=" + idLeilao +
                ", idCliente=" + idCliente +
                ", valorLance=" + valorLance +
                ", numLance=" + idNegociacao +
                ", dataLance=" + dataLance +
                ", estado=" + estado +
                '}';
    }
}
