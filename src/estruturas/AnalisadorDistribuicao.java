package estruturas;

import java.util.Arrays;

public final class AnalisadorDistribuicao {

    private AnalisadorDistribuicao() {}

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
