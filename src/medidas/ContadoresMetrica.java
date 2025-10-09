package medidas;

public class ContadoresMetrica {
    /** Total de colisões ocorridas nas inserções. */
    private long quantidadeColisoesInsercao;

    /** Total de passos realizados nas buscas. */
    private long quantidadePassosBusca;

    /**
     * Zera ambos os contadores.
     * Útil quando inicia uma nova combinação de (M, função hash, estratégia).
     */
    public void zerar() {
        quantidadeColisoesInsercao = 0L;
        quantidadePassosBusca = 0L;
    }

    /**
     * Incrementa em 1 a contagem de colisões de inserção.
     * Chame conforme a regra de colisão da estrutura utilizada.
     */
    public void incrementarColisaoInsercao() {
        quantidadeColisoesInsercao++;
    }

    /**
     * Soma um número de passos à contagem total das buscas.
     * @param passos quantidade de passos realizados numa única operação de busca.
     */
    public void adicionarPassosBusca(long passos) {
        quantidadePassosBusca += passos;
    }

    /**
     * @return total acumulado de colisões nas inserções.
     */
    public long getQuantidadeColisoesInsercao() {
        return quantidadeColisoesInsercao;
    }

    /**
     * @return total acumulado de passos realizados nas buscas.
     */
    public long getQuantidadePassosBusca() {
        return quantidadePassosBusca;
    }
}
