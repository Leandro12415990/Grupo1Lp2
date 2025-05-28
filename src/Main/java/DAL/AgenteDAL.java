package DAL;

import Model.Agente;
import Utils.Constantes;
import Utils.Tools;
import org.apache.poi.ss.formula.functions.T;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AgenteDAL {
    private static final String FICHEIRO = Constantes.caminhosFicheiros.CSV_FILE_AGENTE;

    public List<Agente> carregarAgentes() {
        List<Agente> agentes = new ArrayList<>();
        File ficheiro = new File(FICHEIRO);

        if (!ficheiro.exists()) return agentes;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) { primeiraLinha = false; continue; }

                String[] partes = linha.split(Tools.separador());
                if (partes.length != 4) continue;

                int id = Integer.parseInt(partes[0]);
                int clienteId = Integer.parseInt(partes[1]);
                int leilaoId = Integer.parseInt(partes[2]);
                LocalDateTime data = LocalDateTime.parse(partes[3], Tools.DATA_HORA);

                agentes.add(new Agente(id, clienteId, leilaoId, data));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return agentes;
    }

    public List<Agente> carregarAgentesPorLeilao(int leilaoId) {
        List<Agente> todos = carregarAgentes();
        List<Agente> filtrados = new ArrayList<>();

        for (Agente a : todos) {
            if (a.getLeilaoId() == leilaoId) filtrados.add(a);
        }

        filtrados.sort(Comparator.comparing(Agente::getDataConfiguracao));
        return filtrados;
    }

    public boolean guardarAgentes(List<Agente> agentes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHEIRO))) {
            bw.write("id;cliente_id;leilao_id;data_configuracao");
            bw.newLine();

            for (Agente a : agentes) {
                String linha = String.join(Tools.separador(),
                        String.valueOf(a.getId()),
                        String.valueOf(a.getClienteId()),
                        String.valueOf(a.getLeilaoId()),
                        Tools.formatDateTime(a.getDataConfiguracao()));
                bw.write(linha);
                bw.newLine();
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
