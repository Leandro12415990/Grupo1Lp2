package Model;

import java.time.LocalDateTime;

public class Lance {
    private int idLance;
    private int idLeilao;
    private int idCliente;
    private String nomeCliente;
    private String emailCliente;
    private double valorLance;
    private int numLance;
    private double valorLanceEletronio;
    private LocalDateTime dataLance;
    private int ordemLance;

    public Lance() {}

    public Lance(int idLance, int idLeilao, int idCliente, String nomeCliente, String emailCliente,
                 double valorLance, int numLance, double valorLanceEletronio, LocalDateTime dataLance, int ordemLance){

        this.idLance = idLance;
        this.idLeilao = idLeilao;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.valorLance = valorLance;
        this.numLance = numLance;
        this.valorLanceEletronio = valorLanceEletronio;
        this.dataLance = dataLance;
        this.ordemLance = ordemLance;
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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
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

    public double getValorLanceEletronio() {
        return valorLanceEletronio;
    }

    public void setValorLanceEletronio(double valorLanceEletronio) {
        this.valorLanceEletronio = valorLanceEletronio;
    }

    public LocalDateTime getDataLance() {
        return dataLance;
    }

    public void setDataLance(LocalDateTime dataLance) {
        this.dataLance = dataLance;
    }

    public int getOrdemLance() {
        return ordemLance;
    }

    public void setOrdemLance(int ordemLance) {
        this.ordemLance = ordemLance;
    }

    @Override
    public String toString(){
        return idLance + idLeilao + idCliente + nomeCliente + emailCliente + valorLance +
                numLance + valorLanceEletronio + dataLance + ordemLance;

    }
}
