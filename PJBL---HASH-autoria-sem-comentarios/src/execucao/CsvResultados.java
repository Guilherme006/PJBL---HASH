package execucao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class CsvResultados implements AutoCloseable {

    private final String caminhoArquivo;

    private BufferedWriter escritor;

    private boolean cabecalhoJaEscrito;

    public CsvResultados(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void abrir() throws IOException {
        this.escritor = Files.newBufferedWriter(
                Paths.get(caminhoArquivo),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
        escreverCabecalhoPadrao();
        this.cabecalhoJaEscrito = true;
    }

    private void escreverCabecalhoPadrao() throws IOException {
        String cabecalho = String.join(",",
                "M","N","estrategia","funcao",
                "tempo_insercao_ms","tempo_busca_ms",
                "colisoes","passos",
                "gap_min","gap_medio","gap_max",
                "top1","top2","top3"
        );
        escritor.write(cabecalho);
        escritor.newLine();
    }

    public void escreverLinha(
            int tamanhoTabela, int quantidadeDados,
            String nomeEstrategia, String nomeFuncao,
            double tempoInsercaoMs, double tempoBuscaMs,
            long quantidadeColisoes, long quantidadePassos,
            int gapMinimo, double gapMedio, int gapMaximo,
            int top1, int top2, int top3
    ) throws IOException {

        if (escritor == null) {
            throw new IllegalStateException("O arquivo CSV não está aberto. Chame abrir() antes de escrever linhas.");
        }

        StringBuilder linha = new StringBuilder(256);
        linha.append(tamanhoTabela).append(',');
        linha.append(quantidadeDados).append(',');
        linha.append(escaparTextoSeNecessario(nomeEstrategia)).append(',');
        linha.append(escaparTextoSeNecessario(nomeFuncao)).append(',');

        linha.append(formatarDecimal(tempoInsercaoMs)).append(',');
        linha.append(formatarDecimal(tempoBuscaMs)).append(',');

        linha.append(quantidadeColisoes).append(',');
        linha.append(quantidadePassos).append(',');

        linha.append(gapMinimo).append(',');
        linha.append(formatarDecimal(gapMedio)).append(',');
        linha.append(gapMaximo).append(',');

        linha.append(top1).append(',');
        linha.append(top2).append(',');
        linha.append(top3);

        escritor.write(linha.toString());
        escritor.newLine();
    }

    @Override
    public void close() throws IOException {
        if (escritor != null) {
            escritor.flush();
            escritor.close();
            escritor = null;
        }
    }

    private static String formatarDecimal(double valor) {
        return String.format(java.util.Locale.ROOT, "%.3f", valor);
    }

    private static String escaparTextoSeNecessario(String texto) {
        if (texto == null) return "";
        boolean precisaAspas =
                texto.indexOf(',') >= 0 ||
                        texto.indexOf('"') >= 0 ||
                        texto.indexOf('\n') >= 0 ||
                        texto.indexOf('\r') >= 0;
        if (!precisaAspas) return texto;
        String comAspasDuplicadas = texto.replace("\"", "\"\"");
        return "\"" + comAspasDuplicadas + "\"";
    }
}
