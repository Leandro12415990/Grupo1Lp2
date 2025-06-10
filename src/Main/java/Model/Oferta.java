package Model;

import java.time.LocalDateTime;

public abstract class Oferta {
    protected int id;
    protected String descricao;
    protected LocalDateTime dataInicio;
    protected int estado;

    public Oferta(int id, String descricao, LocalDateTime dataInicio, int estado) {
        this.id = id;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
