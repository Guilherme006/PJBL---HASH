package execucao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Escreve resultados em CSV.
 * - Cria/zera o arquivo na abertura, em UTF-8.
 * - Usa BufferedWriter obtido via Files.newBufferedWriter.
 * - Escapa corretamente campos de texto que contenham vírgula/aspas/quebras de linha.
 *
 * Uso típico (try-with-resources):
 *   try (CsvResultados csv = new CsvResultados("resultados_hash.csv")) {
 *       csv.abrir();
 *       csv.escreverLinha(...);
 *   }
 */
public final class CsvResultados implements AutoCloseable {

    /** Caminho completo do arquivo CSV a ser criado. */
    private final String caminhoArquivo;

    /** Escritor em buffer (UTF-8). */
    private BufferedWriter escritor;

    /** Indica se o cabeçalho já foi emitido. */
    private boolean cabecalhoJaEscrito;

    /** Constrói o utilitário apontando para um caminho de arquivo. */
    public CsvResultados(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    /**
     * Abre o arquivo e escreve o cabeçalho (sempre cria/zera o conteúdo).
     * Usa UTF-8 e buffering do NIO.
     */
    public void abrir() throws IOException {
        this.escritor = Files.newBufferedWriter(
                Paths.get(caminhoArquivo),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,            // cria se não existir
                StandardOpenOption.TRUNCATE_EXISTING, // zera o conteúdo
                StandardOpenOption.WRITE              // abre para escrita
        );
        escreverCabecalhoPadrao();
        this.cabecalhoJaEscrito = true;
    }

    /** Escreve o cabeçalho padrão do CSV. */
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

    /**
     * Escreve uma linha com os resultados medidos para uma combinação de
     * (tamanho tabela, quantidade de dados, estratégia, função hash).
     *
     * @throws IllegalStateException se abrir() não foi chamado antes.
     */
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

        // Constrói a linha utilizando StringBuilder (evita concatenação custosa)
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

    /** Fecha o arquivo e garante flush do buffer. */
    @Override
    public void close() throws IOException {
        if (escritor != null) {
            escritor.flush();
            escritor.close();
            escritor = null;
        }
    }

    // ----------------- utilidades internas -----------------

    /** Garante ponto decimal com '.', independente do locale do SO. */
    private static String formatarDecimal(double valor) {
        return String.format(java.util.Locale.ROOT, "%.3f", valor);
        // Ajuste a precisão se desejar (ex.: "%.2f" para médias).
    }

    /**
     * CSV exige aspas duplas se o campo contiver vírgula, aspas ou quebra de linha.
     */
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
