package DAL;

import Model.Negociacao;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;

public class NegociacaoDAL {

    public List<Negociacao> carregarNegociacoes() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_NEGOCIACAO, 8, dados -> {
            int idNegociacao = Integer.parseInt(dados[0]);
            int idCliente = Integer.parseInt(dados[1]);
            String nome = dados[2];
            String descricao = dados[3];
            double valor = Double.parseDouble(dados[4]);
            LocalDateTime dataInicio = Tools.parseDateTimeByDate(dados[5]);
            double valorContraproposta = Double.parseDouble(dados[6]);
            int estado = Integer.parseInt(dados[7]);

            return new Negociacao(idNegociacao, idCliente, nome, descricao, valor, dataInicio, valorContraproposta, estado);
        });
    }

    public void gravarNegociacoes(List<Negociacao> negociacoes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID_NEGOCIACAO;ID_CLIENTE;NOME;DESCRICAO;VALOR;DATA_INICIO;VALOR_CONTRAPROPOSTA;ESTADO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_NEGOCIACAO, cabecalho, negociacoes, negociacao ->
                negociacao.getIdNegociacao() + Tools.separador() +
                        negociacao.getIdCliente() + Tools.separador() +
                        negociacao.getNome() + Tools.separador() +
                        negociacao.getDescricao() + Tools.separador() +
                        negociacao.getValor() + Tools.separador() +
                        Tools.formatDateTime(negociacao.getDataInicio()) + Tools.separador() +
                        negociacao.getValorContraproposta() + Tools.separador() +
                        negociacao.getEstado()
        );
    }
}
