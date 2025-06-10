package Utils;

public class Constantes {
    // Secção: Configurações Email
    public static class configEmail {
        public static final String fromEmail = "valoraltaleiloes@gmail.com";
        public static final String fromName = "Valor em Alta - Leiloeira";
        public static final String keyPassword = "zwgz xnzn kznc iieo";
        public static final String host = "smtp.gmail.com";
        public static final int port = 587;
        public static final String equipa = "Equipa Valor em Alta Leilões";
    }

    public static class caminhosFicheiros {
        public static final String CSV_FILE_LEILAO = "data\\Leilao.csv";
        public static final String CSV_FILE_UTILIZADOR = "data\\Utilizador.csv";
        public static final String CSV_FILE_LANCE = "data\\Lance.csv";
        public static final String CSV_FILE_TRANSACAO = "data\\Transacao.csv";
        public static final String CSV_FILE_PRODUTO = "data\\Produto.csv";
        public static final String CSV_FILE_TEMPLATE = "data\\Template.csv";
        public static final String CSV_FILE_EMAIL = "data\\Email.csv";
        public static final String CSV_FILE_FICHEIRO_EMAIL = "data\\RelatorioDiario\\";
        public static final String CSV_FILE_AGENTE = "data\\Agente.csv";
        public static final String CSV_FILE_CATEGORIA = "data\\Categoria.csv";
        public static final String CSV_FILE_PRODUTO_CATEGORIA = "data\\ProdutoCategoria.csv";
        public static final String CSV_FILE_NEGOCIACAO = "data\\Negociacao.csv";
        public static final String CSV_FILE_IMPORT_CLIENTES = "data\\importar_clientes.csv";
    }

    // Secção: Estados Leilões
    public static class estadosLeilao {
        public static final int DEFAULT = 0;
        public static final int ATIVO = 1;
        public static final int PENDENTE = 2;
        public static final int CANCELADO = 3;
        public static final int FECHADO = 4;
        public static final int INATIVO = 5;
    }

    public static class estadosLance {
        public static final int DEFAULT = 0;
        public static final int PROPOSTA = 1;
        public static final int CONTRAPROPOSTA = 2;
    }

    // Secção: Tipos Leilões
    public static class tiposLeilao {
        public static final int ELETRONICO = 1;
        public static final int CARTA_FECHADA = 2;
        public static final int VENDA_DIRETA = 3;
        public static final int NEGOCIACAO = 4;
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

    // Secção: Tipos Email's
    public static class templateIds {
        public static final String EMAIL_REGISTO = "1";
        public static final String EMAIL_APROVADO = "2";
        public static final String EMAIL_VENCEDOR_LEILAO = "3";
        public static final String EMAIL_CLIENTE_OFFLINE = "4";
        public static final String EMAIL_SEM_CREDITOS = "5";
        public static final String EMAIL_RELATORIO_DIARIO = "6";
        public static final String EMAIL_CLIENTES_CRIADO_IMPORT = "7";
    }

    public static class estadosCategoria {
        public static final int INATIVO = 0;
        public static final int ATIVO = 1;
    }
}
