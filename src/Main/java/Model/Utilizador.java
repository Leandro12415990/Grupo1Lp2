package Model;

import java.time.LocalDate;

public class Utilizador {
    private int id;
    private String nomeUtilizador;
    private String email;
    private LocalDate dataNascimento;
    private String morada;
    private String password;
    private LocalDate dataRegisto;
    private LocalDate ultimoLogin;
    private int tipoUtilizador;
    private int estado;
    private Double saldo;

    public Utilizador(int id, String nomeUtilizador, String email, LocalDate dataNascimento, String morada, String password,
                      LocalDate dataRegisto, LocalDate ultimoLogin, int tipoUtilizador, int estado, Double saldo) {
        this.id = id;
        this.nomeUtilizador = nomeUtilizador;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.morada = morada;
        this.password = password;
        this.dataRegisto = dataRegisto;
        this.ultimoLogin = ultimoLogin;
        this.tipoUtilizador = tipoUtilizador;
        this.estado = estado;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUtilizador() {
        return nomeUtilizador;
    }

    public String getEmail() {return email; }

    public void setNomeUtilizador(String nomeUtilizador) {
        this.nomeUtilizador = nomeUtilizador;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDataRegisto() {
        return dataRegisto;
    }

    public void setDataRegisto(LocalDate dataRegisto) {
        this.dataRegisto = dataRegisto;
    }

    public LocalDate getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDate ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public int getTipoUtilizador() {
        return tipoUtilizador;
    }

    public void setTipoUtilizador(int tipoUtilizador) {
        this.tipoUtilizador = tipoUtilizador;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
