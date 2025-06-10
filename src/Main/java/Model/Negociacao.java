package Model;

import java.time.LocalDateTime;

public class Negociacao extends Oferta {
    private int idCliente;
    private String nome;
    private double valor;
    private double valorContraproposta;

    public Negociacao(int id, int idCliente, String nome, String descricao,
                      double valor, LocalDateTime dataInicio,
                      double valorContraproposta, int estado) {
        super(id, descricao, dataInicio, estado);
        this.idCliente = idCliente;
        this.nome = nome;
        this.valor = valor;
        this.valorContraproposta = valorContraproposta;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorContraproposta() {
        return valorContraproposta;
    }

    public void setValorContraproposta(double valorContraproposta) {
        this.valorContraproposta = valorContraproposta;
    }
}
