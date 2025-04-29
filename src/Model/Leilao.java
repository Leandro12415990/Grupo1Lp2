package Model;

import java.time.LocalDateTime;

public class Leilao {
    private int id;
    private int idProduto;
    private String descricao;
    private int idTipoLeilao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double valorMinimo;
    private Double valorMaximo;
    private Double valorAtualLanceEletronico;
    private int idEstado;

    public Leilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio,
                  LocalDateTime dataFim, Double valorMinimo, Double valorMaximo, Double valorAtualLanceEletronico, int idEstado) {
        this.id = id;
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.idTipoLeilao = idTipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.valorAtualLanceEletronico = valorAtualLanceEletronico;
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

    public int getTipoLeilao() {
        return idTipoLeilao;
    }

    public void setTipoLeilao(int idTipoLeilao) {
        this.idTipoLeilao = idTipoLeilao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
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

    public Double getValorAtualLanceEletronico() {
        return valorAtualLanceEletronico;
    }

    public void setValorAtualLanceEletronico(Double valorAtualLanceEletronico) {
        this.valorAtualLanceEletronico = valorAtualLanceEletronico;
    }

    public int getEstado() {
        return idEstado;
    }

    public void setEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
