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

    public Leilao(int id, String nomeProduto, String descricao, String tipoLeilao, LocalDate dataInicio,
                  LocalDate dataFim, Double valorMinimo, Double valorMaximo, Double multiploLance) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.tipoLeilao = tipoLeilao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.multiploLance = multiploLance;
    }

    public int getId() {
        return id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipoLeilao() {
        return tipoLeilao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public Double getValorMaximo() {
        return valorMaximo;
    }

    public Double getMultiploLance() {
        return multiploLance;
    }
}
