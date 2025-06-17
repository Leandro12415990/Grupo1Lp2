package Utils;

import DAL.ConfigLoader;

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

    //Secção: Caminhos Ficheiros
    public static class caminhosFicheiros {
        public static final String CSV_FILE_LEILAO = ConfigLoader.getPath("CSV_FILE_LEILAO");
        public static final String CSV_FILE_UTILIZADOR = ConfigLoader.getPath("CSV_FILE_UTILIZADOR");
        public static final String CSV_FILE_LANCE = ConfigLoader.getPath("CSV_FILE_LANCE");
        public static final String CSV_FILE_TRANSACAO = ConfigLoader.getPath("CSV_FILE_TRANSACAO");
        public static final String CSV_FILE_PRODUTO = ConfigLoader.getPath("CSV_FILE_PRODUTO");
        public static final String CSV_FILE_TEMPLATE = ConfigLoader.getPath("CSV_FILE_TEMPLATE");
        public static final String CSV_FILE_EMAIL = ConfigLoader.getPath("CSV_FILE_EMAIL");
        public static final String CSV_FILE_FICHEIRO_EMAIL = ConfigLoader.getPath("CSV_FILE_FICHEIRO_EMAIL");
        public static final String CSV_FILE_AGENTE = ConfigLoader.getPath("CSV_FILE_AGENTE");
        public static final String CSV_FILE_CATEGORIA = ConfigLoader.getPath("CSV_FILE_CATEGORIA");
        public static final String CSV_FILE_PRODUTO_CATEGORIA = ConfigLoader.getPath("CSV_FILE_PRODUTO_CATEGORIA");
        public static final String CSV_FILE_NEGOCIACAO = ConfigLoader.getPath("CSV_FILE_NEGOCIACAO");
        public static final String CSV_FILE_IMPORT_CLIENTES = ConfigLoader.getPath("CSV_FILE_IMPORT_CLIENTES");
        public static final String CSV_FILE_IMPORT_LEILOES = ConfigLoader.getPath("CSV_FILE_IMPORT_LEILOES");
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
        public static final int FINALIZADO = 3;
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
