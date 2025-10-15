package estruturas;

/**
 * Estrutura imutável para representar estatísticas de "gaps" (sequências de
 * posições vazias consecutivas) observadas no vetor principal da tabela.
 *
 * - gapMinimo: menor sequência de posições vazias observada (ou 0 se nenhuma)
 * - gapMedio: média aritmética das sequências de posições vazias
 * - gapMaximo: maior sequência de posições vazias observada
 * - quantidadeBlocosVazios: quantos blocos de posições vazias foram contados
 *
 * Obs.: Para tabelas encadeadas, o "vetor principal" é o vetor de buckets.
 *       Para endereçamento aberto, é o próprio vetor de armazenamento.
 */
public final class EstatisticasGap {
    private final int gapMinimo;
    private final double gapMedio;
    private final int gapMaximo;
    private final int quantidadeBlocosVazios;

    public EstatisticasGap(int gapMinimo, double gapMedio, int gapMaximo, int quantidadeBlocosVazios) {
        this.gapMinimo = gapMinimo;
        this.gapMedio = gapMedio;
        this.gapMaximo = gapMaximo;
        this.quantidadeBlocosVazios = quantidadeBlocosVazios;
    }

    public int getGapMinimo() {
        return gapMinimo;
    }

    public double getGapMedio() {
        return gapMedio;
    }

    public int getGapMaximo() {
        return gapMaximo;
    }

    public int getQuantidadeBlocosVazios() {
        return quantidadeBlocosVazios;
    }

    @Override
    public String toString() {
        return "EstatisticasGap{" +
                "gapMinimo=" + gapMinimo +
                ", gapMedio=" + gapMedio +
                ", gapMaximo=" + gapMaximo +
                ", quantidadeBlocosVazios=" + quantidadeBlocosVazios +
                '}';
    }
}
