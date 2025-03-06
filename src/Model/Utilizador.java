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
    private String tipoUtilizador;

    public Utilizador(int id, String nomeUtilizador, String email, LocalDate dataNascimento, String morada, String password,
                      LocalDate dataRegisto, LocalDate ultimoLogin, String tipoUtilizador) {
        this.id = id;
        this.nomeUtilizador = nomeUtilizador;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.morada = morada;
        this.password = password;
        this.dataRegisto = dataRegisto;
        this.ultimoLogin = ultimoLogin;
        this.tipoUtilizador = tipoUtilizador;
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

    public String getTipoUtilizador() {
        return tipoUtilizador;
    }

    public void setTipoUtilizador(String tipoUtilizador) {
        this.tipoUtilizador = tipoUtilizador;
    }
}
