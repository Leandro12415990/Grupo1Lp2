package DAL;

import Model.Classificacao;
import Utils.Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClassificacaoDAL {
    private static final Logger logger = Logger.getLogger(ClassificacaoDAL.class.getName());
    private final String ficheiro = "data/Classificacao.csv";
    private final String cabecalho = "idLeilao,idUtilizador,classificacao,comentario";

    public List<Classificacao> carregarClassificacoes() {
        List<Classificacao> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);
                if (dados.length < 4) continue;

                int idLeilao = Integer.parseInt(dados[0]);
                int idUtilizador = Integer.parseInt(dados[1]);
                int classificacao = Integer.parseInt(dados[2]);
                String comentario = dados[3];

                lista.add(new Classificacao(idLeilao, idUtilizador, classificacao, comentario));
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler classificações: " + ficheiro, e);
        }

        return lista;
    }

    public void gravarClassificacoes(List<Classificacao> lista) {
        File pasta = new File("data");
        if (!pasta.exists()) pasta.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro))) {
            bw.write(cabecalho);
            bw.newLine();

            for (Classificacao c : lista) {
                String linha = c.getIdLeilao() + Tools.separador()
                        + c.getIdCLiente() + Tools.separador()
                        + c.getClassificacao() + Tools.separador()
                        + c.getComentario().replace(Tools.separador(), " ");
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao gravar classificações", e);
        }
    }

    public void adicionarClassificacao(Classificacao nova) {
        List<Classificacao> lista = carregarClassificacoes();
        lista.add(nova);
        gravarClassificacoes(lista);
    }
}
