package Controller;

import BLL.CarteiraBLL;
import Model.Carteira;
import Model.ResultadoOperacao;
import Utils.Constantes;

import java.time.LocalDateTime;

public class CarteiraController {
    public static ResultadoOperacao criarDeposito(int idCliente, Double saldoAtual, Double creditos) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (creditos <= 0) {
            resultado.msgErro = "O crédito deve ser positivo.";
        } else {
            Carteira carteira = new Carteira(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.estadosDeposito.PENDENTE);
            CarteiraBLL.criarDeposito(carteira);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static Double buscarValorTotalAtual(int idCliente) {
        if (idCliente > 0) {
            return CarteiraBLL.buscarValorTotalAtual(idCliente);
        }
        return 0.0;
    }

    public static Double valorPendente(int idCliente) {
        if (idCliente > 0) {
            return CarteiraBLL.valorPendente(idCliente);
        }
        return 0.0;
    }

}
