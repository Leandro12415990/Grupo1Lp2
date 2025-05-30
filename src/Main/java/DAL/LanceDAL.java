package DAL;

import Model.Lance;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;


public class LanceDAL {
    public List<Lance> carregarLances() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LANCE, 7, dados -> {
            int idLance = Integer.parseInt(dados[0]);
            int idLeilao = Integer.parseInt(dados[1]);
            int idCliente = Integer.parseInt(dados[2]);
            double valorLance = Double.parseDouble(dados[3]);
            int numLance = dados[4].isEmpty() ? 0 : Integer.parseInt(dados[4]);
            int pontosUtilizados = dados[5].isEmpty() ? 0 : Integer.parseInt(dados[5]);
            LocalDateTime dataLance = Tools.parseDateTimeByDate(dados[6]);
            return new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        });
    }

    public void gravarLances(List<Lance> lances) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LANCE, cabecalho, lances, lance ->
                String.join(Tools.separador(),
                        String.valueOf(lance.getIdLance()),
                        String.valueOf(lance.getIdLeilao()),
                        String.valueOf(lance.getIdCliente()),
                        String.valueOf(lance.getValorLance()),
                        String.valueOf(lance.getNumLance()),
                        String.valueOf(lance.getPontosUtilizados()),
                        Tools.formatDateTime(lance.getDataLance())
                )
        );

    }
}

/*package DAL;

import Model.Lance;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;

public class LanceDAL {

    public List<Lance> carregarLances() {
        ImportDAL importDal = new ImportDAL();
        System.out.println("🔄 A carregar lances do ficheiro: " + caminhosFicheiros.CSV_FILE_LANCE);
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LANCE, 7, dados -> {
            try {
                if (dados.length != 7) {
                    System.err.println("⚠️ Linha com número incorreto de campos: " + String.join(";", dados));
                    return null;
                }

                int idLance = Integer.parseInt(dados[0]);
                int idLeilao = Integer.parseInt(dados[1]);
                int idCliente = Integer.parseInt(dados[2]);
                double valorLance = Double.parseDouble(dados[3]);
                int numLance = dados[4].isEmpty() ? 0 : Integer.parseInt(dados[4]);
                int pontosUtilizados = dados[5].isEmpty() ? 0 : Integer.parseInt(dados[5]);
                LocalDateTime dataLance = Tools.parseDateTimeByDate(dados[6]);

                System.out.printf("✅ Lance carregado: ID=%d | Leilão=%d | Cliente=%d | Valor=%.2f€ | Multiplos=%d | Pontos=%d | Data=%s%n",
                        idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dados[6]);

                return new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);

            } catch (Exception e) {
                System.err.println("❌ Erro ao processar linha do CSV: " + String.join(";", dados));
                e.printStackTrace();
                return null;
            }
        });
    }

    public void gravarLances(List<Lance> lances) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA";

        System.out.println("💾 A gravar " + lances.size() + " lances no ficheiro: " + caminhosFicheiros.CSV_FILE_LANCE);

        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LANCE, cabecalho, lances, lance -> {
            String linha = String.join(Tools.separador(),
                    String.valueOf(lance.getIdLance()),
                    String.valueOf(lance.getIdLeilao()),
                    String.valueOf(lance.getIdCliente()),
                    String.valueOf(lance.getValorLance()),
                    String.valueOf(lance.getNumLance()),
                    String.valueOf(lance.getPontosUtilizados()),
                    Tools.formatDateTime(lance.getDataLance())
            );

            System.out.println("📝 Linha escrita no CSV: " + linha);
            return linha;
        });
    }
}*/
