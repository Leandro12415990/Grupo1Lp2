package Controller;

import BLL.*;
import DAL.TemplateDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LeilaoController {

    public ResultadoOperacao criarLeiloes(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, Double valorMax, Double multiploLance, int idEstado) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição não pode ser nula.";
        } else if (dataInicio == null) {
            resultado.msgErro = "A data de inicio não pode ser nula.";
        } else if (valorMin < 0) {
            resultado.msgErro = "O valor minimo não pode ser negativo.";
        } else if (valorMax != 0.0 && valorMax > valorMin) {
            resultado.msgErro = "O valor do lance tem de ser menor do que o valor minimo.";
        } else if (idTipoLeilao == Constantes.tiposLeilao.ELETRONICO && multiploLance < 0) {
            resultado.msgErro = "O múltiplo de lance deve ser positivo.";
        } else if (dataFim != null && dataFim.isBefore(dataInicio)) {
            resultado.msgErro = "A data de fim deve ser superior à data de inicio.";
        } else {
            Leilao leilao = new Leilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
            leilaoBLL.adicionarLeilao(leilao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public List<Leilao> listarLeiloes(Tools.estadoLeilao estado) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        leilaoBLL.carregarLeiloes();
        return leilaoBLL.listarLeiloes(estado);
    }

    public Leilao procurarLeilaoPorId(int Id) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        if (Id > 0) {
            return leilaoBLL.procurarLeilaoPorId(Id);
        }
        return null;
    }

    public boolean eliminarLeilao(int Id) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        if (Id > 0) {
            Leilao leilao = procurarLeilaoPorId(Id);
            if (leilao != null) {
                leilaoBLL.eliminarLeilao(leilao);
                return true;
            }
        }
        return false;
    }

    public boolean editarLeilao(int id, int idProduto, String descricao, int idTipoLeilao, LocalDateTime dataInicio, LocalDateTime dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        return leilaoBLL.editarLeilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, idEstado);
    }

    public ResultadoOperacao verificarDisponibilidadeProduto(int idProduto) {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();
        Produto produto = produtoBLL.procurarProduto(idProduto);
        if (produto == null) {
            resultado.msgErro = "O Produto que introduziu não existe no sistema.";
        } else {
            boolean isAvailable = produtoBLL.verificarDisponibilidadeProduto(idProduto);
            if (isAvailable) {
                resultado.Objeto = resultado;
                resultado.Sucesso = true;
            } else {
                resultado.msgErro = "O Produto selecionado não se encontra disponível.";
            }
        }
        return resultado;
    }

    public ResultadoOperacao verificarValorMax(double valorMin, double valorMax) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (valorMax < valorMin) {
            resultado.msgErro = "O valor máximo não pode ser inferior ao valor minimo.\n";
        } else {
            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public int determinarEstadoLeilaoByDatas(LocalDateTime dataInicio, LocalDateTime dataFim, int idEstado) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        return leilaoBLL.determinarEstadoLeilaoByDatas(dataInicio, dataFim, idEstado);
    }

    public boolean fecharLeilao(int idLeilao, LocalDateTime dataFim) throws IOException, MessagingException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LanceBLL lanceBLL = new LanceBLL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        EmailBLL emailBLL = new EmailBLL();
        ProdutoBLL produtoBLL = new ProdutoBLL();
        TemplateDAL templateDAL = new TemplateDAL();

        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        if (leilao == null) return false;

        leilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
        int idLanceVencedor = lanceBLL.selecionarLanceVencedor(idLeilao);
        Produto produto = produtoBLL.procurarProduto(leilao.getIdProduto());
        Template template = templateDAL.carregarTemplatePorId(Constantes.templateIds.EMAIL_VENCEDOR_LEILAO);
        Utilizador u = lanceBLL.obterVencedor(idLanceVencedor);
        if (template != null) {
            emailBLL.enviarEmail(template, u.getEmail(), Tools.substituirTags(u,produto,null), u.getId());
        }
        transacaoBLL.devolverSaldo(idLeilao, idLanceVencedor);

        return true;
    }

    public List<Leilao> listarLeiloesTerminadosComLancesDoCliente(int idCliente) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        return leilaoBLL.listarLeiloesTerminadosComLancesDoCliente(idCliente);
    }


}

