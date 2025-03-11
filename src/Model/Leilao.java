package Model;

import java.time.LocalDate;

public class Leilao {
    private int id;
    private String nomeProduto;
    private String descricao;
    private String tipoLeilao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorMinimo;
    private Double valorMaximo;
    private Double multiploLance;
    private String estado;

    public Leilao(int id, String nomeProduto, String descricao, String tipoLeilao, LocalDate dataInicio,
                  LocalDate dataFim, Double valorMinimo, Double valorMaximo, Double multiploLance, String estado) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.tipoLeilao = tipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.multiploLance = multiploLance;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoLeilao() {
        return tipoLeilao;
    }

    public void setTipoLeilao(String tipoLeilao) {
        this.tipoLeilao = tipoLeilao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(Double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public Double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(Double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public Double getMultiploLance() {
        return multiploLance;
    }

    public void setMultiploLance(Double multiploLance) {
        this.multiploLance = multiploLance;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
