package DAL;

import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDate;
import java.util.List;

public class UtilizadorDAL {
    private final ImportDAL importDal;

    public UtilizadorDAL(ImportDAL importDal) {
        this.importDal = importDal;
    }

    public List<Utilizador> carregarUtilizadores() {
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, 11, dados -> {
            int id = Integer.parseInt(dados[0]);
            String nomeUtilizador = dados[1];
            String email = dados[2];
            LocalDate dataNascimento = Tools.parseDate(dados[3]);
            String morada = dados[4];
            String password = dados[5];
            LocalDate dataRegisto = Tools.parseDate(dados[6]);
            LocalDate ultimoLogin = dados[7].isEmpty() ? null : Tools.parseDate(dados[7]);
            int tipoUtilizador = Integer.parseInt(dados[8]);
            int estado = Integer.parseInt(dados[9]);
            Double saldo = Double.parseDouble(dados[10]);
            return new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
        });
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        String cabecalho = "ID;NOME;EMAIL;DATA NASCIMENTO;MORADA;PASSWORD;DATA REGISTO;ULTIMO LOGIN;TIPO UTILIZADOR;ESTADO;SALDO";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, cabecalho, utilizadores, utilizador ->
                utilizador.getId() + Tools.separador() +
                        utilizador.getNomeUtilizador() + Tools.separador() +
                        utilizador.getEmail() + Tools.separador() +
                        Tools.formatDate(utilizador.getDataNascimento()) + Tools.separador() +
                        utilizador.getMorada() + Tools.separador() +
                        utilizador.getPassword() + Tools.separador() +
                        Tools.formatDate(utilizador.getDataRegisto()) + Tools.separador() +
                        Tools.formatDate(utilizador.getUltimoLogin()) + Tools.separador() +
                        utilizador.getTipoUtilizador() + Tools.separador() +
                        utilizador.getEstado() + Tools.separador() +
                        utilizador.getSaldo()
        );
    }
}
