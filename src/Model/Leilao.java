package Model;

import java.time.LocalDate;

public class Leilao {
    private int id;
    private int idProduto;
    private String descricao;
    private String tipoLeilao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Double valorMinimo;
    private Double valorMaximo;
    private Double multiploLance;
    private int idEstado;

    public Leilao(int id, int idProduto, String descricao, String tipoLeilao, LocalDate dataInicio,
                  LocalDate dataFim, Double valorMinimo, Double valorMaximo, Double multiploLance, int idEstado) {
        this.id = id;
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.tipoLeilao = tipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.multiploLance = multiploLance;
        this.idEstado = idEstado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
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

    public int getEstado() {
        return idEstado;
    }

    public void setEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
