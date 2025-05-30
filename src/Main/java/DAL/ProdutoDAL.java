package DAL;

import Model.Produto;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.util.List;


public class ProdutoDAL {
    public List<Produto> carregarProdutos() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_PRODUTO, 4, dados -> {
            int id = Integer.parseInt(dados[0]);
            int estado = Integer.parseInt(dados[1]);
            String nome = dados[2];
            String descricao = dados[3];
            return new Produto(id, estado, nome, descricao);
        });
    }

    public void gravarProdutos(List<Produto> produtos) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;ESTADO;NOME;DESCRICAO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_PRODUTO, cabecalho, produtos, produto ->
                produto.getIdProduto() + Tools.separador() +
                        produto.getEstado() + Tools.separador() +
                        produto.getNome() + Tools.separador() +
                        produto.getDescricao()
        );
    }

}
