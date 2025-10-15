package estruturas;

import java.util.Arrays;

/**
 * Funções utilitárias para calcular:
 *  - Estatísticas de gaps (mínimo, médio e máximo) no vetor principal;
 *  - Top-3 maiores listas (apenas para encadeamento separado).
 */
public final class AnalisadorDistribuicao {

    private AnalisadorDistribuicao() {}

    // ============================
    // Cálculo de gaps (encadeamento)
    // ============================

    /**
     * Calcula estatísticas de gaps para uma tabela com encadeamento separado.
     * Considera "vazio" quando vetorCabecas[posicao] == -1.
     *
     * @param vetorCabecas vetor de cabeças de lista (tamanho M)
     * @return estatísticas de gaps
     */
    public static EstatisticasGap calcularGapsEmBucketsEncadeados(int[] vetorCabecas) {
        if (vetorCabecas == null || vetorCabecas.length == 0) {
            return new EstatisticasGap(0, 0.0, 0, 0);
        }
        int tamanho = vetorCabecas.length;

        int gapAtual = 0;
        int gapMinimo = Integer.MAX_VALUE;
        int gapMaximo = 0;
        long somaGaps = 0L;
        int quantidadeBlocosVazios = 0;

        for (int i = 0; i < tamanho; i++) {
            boolean vazio = (vetorCabecas[i] == -1);
            if (vazio) {
                gapAtual++;
            } else {
                if (gapAtual > 0) {
                    // Fechou um bloco de vazios
                    quantidadeBlocosVazios++;
                    somaGaps += gapAtual;
                    if (gapAtual < gapMinimo) gapMinimo = gapAtual;
                    if (gapAtual > gapMaximo) gapMaximo = gapAtual;
                    gapAtual = 0;
                }
            }
        }

        // Caso o vetor termine com bloco de vazios
        if (gapAtual > 0) {
            quantidadeBlocosVazios++;
            somaGaps += gapAtual;
            if (gapAtual < gapMinimo) gapMinimo = gapAtual;
            if (gapAtual > gapMaximo) gapMaximo = gapAtual;
        }

        if (quantidadeBlocosVazios == 0) {
            return new EstatisticasGap(0, 0.0, 0, 0);
        }

        double media = (double) somaGaps / (double) quantidadeBlocosVazios;
        return new EstatisticasGap(gapMinimo, media, gapMaximo, quantidadeBlocosVazios);
    }

    // ============================
    // Cálculo de gaps (endereçamento aberto)
    // ============================

    /**
     * Calcula estatísticas de gaps para uma tabela com endereçamento aberto.
     * Considera "vazio" quando vetorTabela[posicao] == valorSentinelaVazio.
     *
     * @param vetorTabela vetor principal da tabela aberta
     * @param valorSentinelaVazio valor que representa célula vazia (ex.: 0x80000000)
     * @return estatísticas de gaps
     */
    public static EstatisticasGap calcularGapsEmTabelaAberta(int[] vetorTabela, int valorSentinelaVazio) {
        if (vetorTabela == null || vetorTabela.length == 0) {
            return new EstatisticasGap(0, 0.0, 0, 0);
        }
        int tamanho = vetorTabela.length;

        int gapAtual = 0;
        int gapMinimo = Integer.MAX_VALUE;
        int gapMaximo = 0;
        long somaGaps = 0L;
        int quantidadeBlocosVazios = 0;

        for (int i = 0; i < tamanho; i++) {
            boolean vazio = (vetorTabela[i] == valorSentinelaVazio);
            if (vazio) {
                gapAtual++;
            } else {
                if (gapAtual > 0) {
                    quantidadeBlocosVazios++;
                    somaGaps += gapAtual;
                    if (gapAtual < gapMinimo) gapMinimo = gapAtual;
                    if (gapAtual > gapMaximo) gapMaximo = gapAtual;
                    gapAtual = 0;
                }
            }
        }

        if (gapAtual > 0) {
            quantidadeBlocosVazios++;
            somaGaps += gapAtual;
            if (gapAtual < gapMinimo) gapMinimo = gapAtual;
            if (gapAtual > gapMaximo) gapMaximo = gapAtual;
        }

        if (quantidadeBlocosVazios == 0) {
            return new EstatisticasGap(0, 0.0, 0, 0);
        }

        double media = (double) somaGaps / (double) quantidadeBlocosVazios;
        return new EstatisticasGap(gapMinimo, media, gapMaximo, quantidadeBlocosVazios);
    }

    // ============================
    // Top-3 maiores listas (encadeamento)
    // ============================

    /**
     * Encontra os três maiores comprimentos de lista em uma tabela com encadeamento,
     * considerando um pool intrusivo:
     *   - vetorCabecas[i] = índice do primeiro nó da lista (ou -1 se vazia)
     *   - vetorProximo[j] = índice do próximo nó do pool (ou -1 para fim)
     *
     * @param vetorCabecas vetor de cabeças por bucket
     * @param vetorProximo vetor "próximo" do pool intrusivo
     * @return arranjo de tamanho 3 com os maiores comprimentos em ordem decrescente
     */
    public static int[] obterTop3TamanhosListas(int[] vetorCabecas, int[] vetorProximo) {
        if (vetorCabecas == null || vetorProximo == null || vetorCabecas.length == 0) {
            return new int[]{0, 0, 0};
        }

        int maior = 0, segundo = 0, terceiro = 0;

        for (int cabeca : vetorCabecas) {
            int comprimento = 0;
            for (int ponteiro = cabeca; ponteiro != -1; ponteiro = vetorProximo[ponteiro]) {
                comprimento++;
            }

            // Atualiza top-3 manualmente para evitar alocações
            if (comprimento >= maior) {
                terceiro = segundo;
                segundo = maior;
                maior = comprimento;
            } else if (comprimento >= segundo) {
                terceiro = segundo;
                segundo = comprimento;
            } else if (comprimento > terceiro) {
                terceiro = comprimento;
            }
        }

        return new int[]{maior, segundo, terceiro};
    }
}
