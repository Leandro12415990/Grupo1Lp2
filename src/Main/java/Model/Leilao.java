package Model;

import java.time.LocalDateTime;

public class Leilao extends Oferta {
    private Integer idProduto;
    private int idTipoLeilao;
    private LocalDateTime dataFim;
    private Double valorMinimo;
    private Double valorMaximo;
    private Double multiploLance;

    public Leilao(int id, Integer idProduto, String descricao, int idTipoLeilao,
                  LocalDateTime dataInicio, LocalDateTime dataFim,
                  Double valorMinimo, Double valorMaximo, Double multiploLance, int estado) {
        super(id, descricao, dataInicio, estado);
        this.idProduto = idProduto;
        this.idTipoLeilao = idTipoLeilao;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.multiploLance = multiploLance;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdTipoLeilao() {
        return idTipoLeilao;
    }

    public void setIdTipoLeilao(int idTipoLeilao) {
        this.idTipoLeilao = idTipoLeilao;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(Double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public Double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(Double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public Double getMultiploLance() {
        return multiploLance;
    }

    public void setMultiploLance(Double multiploLance) {
        this.multiploLance = multiploLance;
    }
}
