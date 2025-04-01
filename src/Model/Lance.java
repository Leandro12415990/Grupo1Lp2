package Model;

import java.time.LocalDateTime;

public class Lance {
    private int idLance;
    private int idLeilao;
    private int idCliente;
    private double valorLance;
    private int numLance;
    private int pontosUtilizados;
    private LocalDateTime dataLance;

    public Lance(int idLance, int idLeilao, int idCliente,
                 double valorLance, int numLance, int pontosUtilizados, LocalDateTime dataLance) {

        this.idLance = idLance;
        this.idLeilao = idLeilao;
        this.idCliente = idCliente;
        this.valorLance = valorLance;
        this.numLance = numLance;
        this.pontosUtilizados = pontosUtilizados;
        this.dataLance = dataLance;
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

    public int getNumLance() {
        return numLance;
    }

    public void setNumLance(int numLance) {
        this.numLance = numLance;
    }

    public int getPontosUtilizados() {
        return pontosUtilizados;
    }

    public void setPontosUtilizados(int pontosUtilizados) {
        this.pontosUtilizados = pontosUtilizados;
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
                ", numLance=" + numLance +
                ", dataLance=" + dataLance +
                '}';
    }
}
