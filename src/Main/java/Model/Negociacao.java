package Model;

import java.time.LocalDateTime;

public class Negociacao {

    private int idNegociacao;
    private int idCliente;
    private String nome;
    private String descricao;
    private double valor;
    private LocalDateTime dataInicio;
    private double valorContraproposta;
    private int estado;

    public Negociacao(int idNegociacao, int idCliente, String nome, String descricao, double valor, LocalDateTime dataInicio, double valorContraproposta, int estado) {
        this.idNegociacao = idNegociacao;
        this.idCliente = idCliente;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.valorContraproposta = valorContraproposta;
        this.estado = estado;
    }

    public int getIdNegociacao() {
        return idNegociacao;
    }

    public void setIdNegociacao(int idNegociacao) {
        this.idNegociacao = idNegociacao;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public double getValorContraproposta() {
        return valorContraproposta;
    }

    public void setValorContraproposta(double valorContraproposta) {
        this.valorContraproposta = valorContraproposta;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
