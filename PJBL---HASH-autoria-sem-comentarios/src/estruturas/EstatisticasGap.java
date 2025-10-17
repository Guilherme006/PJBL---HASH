package estruturas;

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
