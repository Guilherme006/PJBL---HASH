package medidas;

public class ContadoresMetrica {
    private long quantidadeColisoesInsercao;

    private long quantidadePassosBusca;

    public void zerar() {
        quantidadeColisoesInsercao = 0L;
        quantidadePassosBusca = 0L;
    }

    public void incrementarColisaoInsercao() {
        quantidadeColisoesInsercao++;
    }

    public void adicionarPassosBusca(long passos) {
        quantidadePassosBusca += passos;
    }

    public long getQuantidadeColisoesInsercao() {
        return quantidadeColisoesInsercao;
    }

    public long getQuantidadePassosBusca() {
        return quantidadePassosBusca;
    }
}
