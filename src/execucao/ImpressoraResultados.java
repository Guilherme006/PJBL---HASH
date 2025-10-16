package execucao;

public final class ImpressoraResultados {
    private ImpressoraResultados(){}

    public static void imprimirCabecalho() {
        System.out.println(
                "| M | N | Estrategia | Funcao | TempoInsercao(ms) | TempoBusca(ms) | Colisoes | Passos | GapMin | GapMedio | GapMax | Top1 | Top2 | Top3 |"
        );
        System.out.println(
                "|---:|---:|:----------|:------|------------------:|---------------:|--------:|------:|------:|--------:|------:|----:|----:|----:|"
        );
    }

    public static void imprimirLinha(
            int m, int n, String estrategia, String funcao,
            double tempoInsercaoMs, double tempoBuscaMs,
            long colisoes, long passos,
            int gapMin, double gapMedio, int gapMax,
            int top1, int top2, int top3
    ) {
        System.out.printf(
                "| %d | %d | %s | %s | %.3f | %.3f | %d | %d | %d | %.2f | %d | %d | %d | %d |%n",
                m, n, estrategia, funcao,
                tempoInsercaoMs, tempoBuscaMs,
                colisoes, passos,
                gapMin, gapMedio, gapMax,
                top1, top2, top3
        );
    }
}
