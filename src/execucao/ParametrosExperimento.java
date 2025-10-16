package execucao;

public final class ParametrosExperimento {
    private ParametrosExperimento(){}

    // Três tamanhos de tabela (ex.: primos próximos de 10^3, 10^4, 10^5)
    public static final int[] TAMANHOS_TABELA = { 1_009, 10_007, 100_003};

    // Três tamanhos de dados
    public static final int[] TAMANHOS_DADOS = { 100_000, 1_000_000, 10_000_000 };

    // Semente fixa para reprodutibilidade
    public static final long SEMENTE = 12345L;
}
