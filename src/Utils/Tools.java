package Utils;

import Model.ResultadoOperacao;
import Model.Utilizador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tools {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Utilizador> utilizadores = new ArrayList<>();

    public static String separador() {
        return ";";
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDateTime(LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(DATA_HORA) : "";
    }

    public static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String formatDate(LocalDate date) {
        return (date != null) ? date.format(FORMATTER) : "";
    }

    public static List<String[]> lerCSV(String caminho) {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha.split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linhas;
    }

    public static void escreverCSV(String caminho, List<String[]> linhas) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminho))) {
            for (String[] linha : linhas) {
                bw.write(String.join(";", linha));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum estadoUtilizador {
        DEFAULT(0), PENDENTE(1), ATIVO(2), INATIVO(3);

        private final int codigo;

        estadoUtilizador(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public static estadoUtilizador getDefault() {
            return DEFAULT; // Valor Default do enum estadoUtilizador
        }
    }

    public enum estadoProduto {
        ATIVO(1), RESERVADO(2), INATIVO(3);

        private final int codigo;
        private int estado;

        estadoProduto(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public static estadoProduto fromCodigo(int codigo) {
            for (estadoProduto estado : estadoProduto.values()) {
                if (estado.getCodigo() == codigo) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Código inválido: " + codigo);
        }
    }

    public enum estadoLeilao {
        ATIVO(1), PENDENTE(2), CANCELADO(3), FECHADO(4), INATIVO(5);

        private final int idEstado;

        estadoLeilao(int idEstado) {
            this.idEstado = idEstado;
        }

        public int getIdEstado() {
            return idEstado;
        }

        public static estadoLeilao fromCodigo(int idEstado) {
            for (estadoLeilao estado : estadoLeilao.values()) {
                if (estado.getIdEstado() == idEstado) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado inválido: " + idEstado);
        }
    }

    public enum tipoLeilao {
        ELETRONICO(1),
        CARTA_FECHADA(2),
        VENDA_DIRETA(3);

        private final int idTipoLeilao;

        tipoLeilao(int idTipoLeilao) { this.idTipoLeilao = idTipoLeilao; }
        public int getIdTipoLeilao() { return idTipoLeilao; }

        public static tipoLeilao fromCodigo(int idTipoLeilao) {
            for (tipoLeilao tipo : tipoLeilao.values()) {
                if (tipo.getIdTipoLeilao() == idTipoLeilao) return tipo;
            }
            throw new IllegalArgumentException("Tipo de Leilão inválido: " + idTipoLeilao);
        }
    }

    public static boolean verificarSaida(String input) {
        if (input.trim().equals("-1") || input.trim().equals("-1.0")) {
            System.out.println("Operação cancelada. Voltando ao menu anterior...");
            return true;
        }
        return false;
    }

    public static String alertaCancelar(){
        return "(-1 para cancelar): ";
    }

    public static ResultadoOperacao verificarDatasAnteriores (LocalDate dataInicial, LocalDate dataFinal) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (dataFinal.isBefore(dataInicial)) {
            resultado.msgErro = "A data final não pode ser anterior à data inicial...\n";
        } else {
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }
}
