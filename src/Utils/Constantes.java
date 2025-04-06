package Utils;

public class Constantes {
    // Secção: Estados Leilões
    public static class estadosLeilao {
        public static final int ATIVO = 1;
        public static final int PENDENTE = 2;
        public static final int CANCELADO = 3;
        public static final int FECHADO = 4;
        public static final int INATIVO = 5;
    }

    // Secção: Tipos Leilões
    public static class tiposLeilao {
        public static final int ELETRONICO = 1;
        public static final int CARTA_FECHADA = 2;
        public static final int VENDA_DIRETA = 3;
    }

    // Secção: Estados Produtos
    public static class estadosProduto {
        public static final int ATIVO = 1;
        public static final int RESERVADO = 2;
        public static final int INATIVO = 3;

    }

    // Secção: Estados Transacoes
    public static class estadosTransacao {
        public static final int PENDENTE = 1;
        public static final int ACEITE = 2;
        public static final int NEGADO = 3;
    }

    // Secção: Tipos Transacoes
    public static class tiposTransacao {
        public static final int DEPOSITO = 1;
        public static final int LANCE_DEBITO = 2;
        public static final int LANCE_DEPOSITO = 3;
        public static final int LANCE_REEMBOLSO = 4;
    }
}
