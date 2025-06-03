package BLL;

import DAL.ImportDAL;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static Utils.DataBaseConnection.getConnection;

public class TESTEpp {

    public List<Utilizador> importarUtilizadores() {
        int totalImportados = 0;
        int totalExistentes = 0;
        List<String> erros = new ArrayList<>();
        List<Utilizador> utilizadoresImportados = new ArrayList<>();

        ImportDAL importDal = new ImportDAL();
        List<String[]> linhas = importDal.lerLinhasCSV(Constantes.caminhosFicheiros.CSV_FILE_IMPORT_CLIENTES, 4);

        for (String[] dados : linhas) {
            try {
                String nomeUtilizador = dados[0];
                String morada = dados[1];
                LocalDate dataNascimento = parseDate(dados[2]);
                String email = dados[3];

                if (utilizadorExiste(email)) {
                    totalExistentes++;
                    continue;
                }

                String password = gerarPasswordTemporaria();
                int id = inserirUtilizador(nomeUtilizador, email, dataNascimento, morada, password);
                enviarEmailNovaPassword(id);

                Utilizador utilizador = new Utilizador(
                        id,
                        nomeUtilizador,
                        email,
                        dataNascimento,
                        morada,
                        password,
                        LocalDate.now(), // dataRegisto
                        null,            // ultimoLogin
                        2,               // tipoUtilizador
                        2,               // estado
                        0.0              // saldo
                );

                utilizadoresImportados.add(utilizador);
                totalImportados++;
            } catch (Exception e) {
                erros.add("Erro ao importar linha: " + Arrays.toString(dados) + " - " + e.getMessage());
            }
        }

        System.out.println("Total de clientes importados: " + totalImportados);
        System.out.println("Total de clientes já existentes: " + totalExistentes);
        if (!erros.isEmpty()) {
            System.out.println("Erros encontrados durante a importação:");
            erros.forEach(System.out::println);
        }

        return utilizadoresImportados;
    }

    private int inserirUtilizador(String nome, String email, LocalDate dataNascimento, String morada, String password) throws SQLException {
        String sql = "INSERT INTO Utilizador (NOME, EMAIL, DATA_NASCIMENTO, MORADA, PASSWORD, TIPO_UTILIZADOR, ESTADO, SALDO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 0.0)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setDate(3, java.sql.Date.valueOf(dataNascimento));
            stmt.setString(4, morada);
            stmt.setString(5, password);

            // Supondo: 2 = cliente, 2 = estado ativo
            stmt.setInt(6, 2); // TIPO_UTILIZADOR
            stmt.setInt(7, 2); // ESTADO

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("A inserção do utilizador falhou, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println("Inseriu um id: " + generatedKeys.getInt(1));
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("A inserção do utilizador falhou, nenhum ID gerado.");
                }
            }

        }
    }


    private void enviarEmailNovaPassword(int id) throws MessagingException, IOException {
        EmailBLL emailBLL = new EmailBLL();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(id);
        emailBLL.enviarEmail(Constantes.templateIds.EMAIL_CLIENTES_CRIADO_IMPORT,utilizador.getEmail(),Tools.substituirTags(utilizador,null,null),utilizador.getId());
    }

    private String gerarPasswordTemporaria() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static LocalDate parseDate(String dataTexto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dataTexto, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Erro a converter data: " + dataTexto + " - " + e.getMessage());
            return null;
        }
    }

    private boolean utilizadorExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM Utilizador WHERE EMAIL = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }



}
