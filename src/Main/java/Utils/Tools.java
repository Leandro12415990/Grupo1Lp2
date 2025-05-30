package Utils;

import Model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Tools {
    public static Scanner scanner = new Scanner(System.in);
    public static List<Utilizador> utilizadores = new ArrayList<>();
    public static ClienteSessao clienteSessao = new ClienteSessao();

    public static String separador() {
        return ";";
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatDateTime(LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(DATA_HORA) : "";
    }

    public static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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

    public enum tipoUtilizador {
        GESTOR(1), CLIENTE(2);

        private final int codigo;

        tipoUtilizador(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public static tipoUtilizador fromCodigo(int codigo) {
            for (tipoUtilizador tipo : tipoUtilizador.values()) {
                if (tipo.getCodigo() == codigo) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Código inválido: " + codigo);
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

        public static estadoUtilizador fromCodigo(int codigo) {
            for (estadoUtilizador estado : estadoUtilizador.values()) {
                if (estado.getCodigo() == codigo) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Código inválido: " + codigo);
        }

        public static estadoUtilizador getDefault() {
            return DEFAULT; // Valor Default do enum estadoUtilizador
        }
    }

    public enum estadoProduto {
        DISPONIVEL(1), RESERVADO(2), INATIVO(3);

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

    public enum estadoCategoria {
        INATIVO(0), ATIVO(1);

        private final int codigo;
        private int estado;

        estadoCategoria(int codigo) {
            this.codigo = codigo;
        }

        public int getCodigo() {
            return codigo;
        }

        public static estadoCategoria fromCodigo(int codigo) {
            for (estadoCategoria estado : estadoCategoria.values()) {
                if (estado.getCodigo() == codigo) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Código inválido: " + codigo);
        }
    }

    public enum estadoLeilao {
        DEFAULT(0), ATIVO(1), PENDENTE(2), CANCELADO(3), FECHADO(4), INATIVO(5);

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

        public static estadoLeilao getDefault() {
            return DEFAULT;
        }
    }

    public enum tipoLeilao {
        ELETRONICO(1),
        CARTA_FECHADA(2),
        VENDA_DIRETA(3);

        private final int idTipoLeilao;

        tipoLeilao(int idTipoLeilao) {
            this.idTipoLeilao = idTipoLeilao;
        }

        public int getIdTipoLeilao() {
            return idTipoLeilao;
        }

        public static tipoLeilao fromCodigo(int idTipoLeilao) {
            for (tipoLeilao tipo : tipoLeilao.values()) {
                if (tipo.getIdTipoLeilao() == idTipoLeilao) return tipo;
            }
            throw new IllegalArgumentException("Tipo de Leilão inválido: " + idTipoLeilao);
        }
    }

    public enum estadoDeposito {
        PENDENTE(1), ACEITE(2), NEGADO(3);

        private final int idEstado;

        estadoDeposito(int idEstado) {
            this.idEstado = idEstado;
        }

        public int getIdEstado() {
            return idEstado;
        }

        public static estadoDeposito fromCodigo(int idEstado) {
            for (estadoDeposito estado : estadoDeposito.values()) {
                if (estado.getIdEstado() == idEstado) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado inválido: " + idEstado);
        }
    }

    public enum tipoTransacao {
        DEPOSITO(1),
        LANCE_DEBITO(2),
        LANCE_DEPOSITO(3),
        LANCE_REEMBOLSO(4);

        private final int idTipoTransacao;

        tipoTransacao(int idTipoTransacao) {
            this.idTipoTransacao = idTipoTransacao;
        }

        public int getIdTipoTransacao() {
            return idTipoTransacao;
        }

        public static tipoTransacao fromCodigo(int idTipoTransacao) {
            for (tipoTransacao tipo : tipoTransacao.values()) {
                if (tipo.getIdTipoTransacao() == idTipoTransacao) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo inválido: " + idTipoTransacao);
        }
    }

    public static boolean verificarSaida(String input) {
        if (input.trim().equals("-1") || input.trim().equals("-1.0")) {
            System.out.println("Operação cancelada. Voltando ao menu anterior...");
            return true;
        }
        return false;
    }

    public static String alertaCancelar() {
        return "(-1 para cancelar): ";
    }

    public static ResultadoOperacao verificarDatasAnteriores(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (dataFinal.isBefore(dataInicial))
            resultado.msgErro = "A data final não pode ser anterior à data inicial...\n";
        else {
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static LocalDateTime parseDateTimeByDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        dateStr = dateStr.trim();

        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        } catch (DateTimeParseException e1) {
            try {
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (DateTimeParseException e2) {
                try {
                    return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                } catch (DateTimeParseException e3) {
                    return null;
                }
            }
        }
    }


    public static LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDateTime.parse(dateStr, DATA_HORA);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    public static String formatarMinutosParaHorasEMinutosESegundos(double minutosTotal) {
        long totalSegundos = (long) (minutosTotal * 60);

        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        long segundos = totalSegundos % 60;

        return horas + " horas e " + minutos + " minutos e " + segundos + " segundos";
    }


    public static int pedirOpcaoMenu(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("⚠ Entrada inválida. Por favor insira um número inteiro.");
                scanner.nextLine();
            }
        }
    }

    public enum tipoEmail {
        EMAIL_REGISTO(1),
        EMAIL_APROVADO(2),
        EMAIL_VENCEDOR_LEILAO(3),
        EMAIL_CLIENTE_OFFLINE(4),
        EMAIL_SEM_CREDITOS(5);

        private final int idTipoEmail;

        tipoEmail(int idTipoEmail) {
            this.idTipoEmail = idTipoEmail;
        }

        public int getIdTipoEmail() {
            return idTipoEmail;
        }

        public static tipoEmail fromCodigo(int idTipoEmail) {
            for (tipoEmail tipo : tipoEmail.values()) {
                if (tipo.getIdTipoEmail() == idTipoEmail) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo inválido: " + idTipoEmail);
        }
    }

    public static Map<String, String> substituirTags(Utilizador u, Produto produto, Leilao leilao) {
        Map<String, String> variaveis = new HashMap<>();
        variaveis.put("NOME", u.getNomeUtilizador());
        variaveis.put("EQUIPA", Constantes.configEmail.equipa);
        variaveis.put("EMAIL", u.getEmail());
        variaveis.put("DATA", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        variaveis.put("SALDO", u.getSaldo().toString());
        if (produto != null) {
            variaveis.put("NOME_PRODUTO", produto.getNome());
        }
        if (leilao != null) {
            variaveis.put("TIPO_LEILAO", leilao.getTipoLeilao() == 1 ? "ELETRONICO" : leilao.getTipoLeilao() == 2 ? "CARTA FECHADA" : "VENDA DIRETA");
            variaveis.put("NOME_lEILAO", leilao.getDescricao());
        }

        return variaveis;
    }

    private static int ultimoIdLance = 0;
    private static final Object lockIdLance = new Object();

    public static void inicializarUltimoIdLance(List<Lance> lances) {
        synchronized (lockIdLance) {
            for (Lance lance : lances) {
                if (lance.getIdLance() > ultimoIdLance) {
                    ultimoIdLance = lance.getIdLance();
                }
            }
        }
    }

    public static int gerarNovoIdLance() {
        synchronized (lockIdLance) {
            return ++ultimoIdLance;
        }
    }
}
