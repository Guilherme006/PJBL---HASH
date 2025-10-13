package medidas;

public class MedidorTempo {
    /** Marca o instante inicial em nanossegundos. */
    private long instanteInicialNanos;

    /** Duração medida em nanossegundos após chamar parar(). */
    private long duracaoNanos;

    /**
     * Inicia a medição de tempo.
     * Deve ser chamado imediatamente antes do trecho de código que se deseja medir.
     */
    public void iniciar() {
        duracaoNanos = 0L;                 // zera qualquer medição anterior
        instanteInicialNanos = System.nanoTime(); // usa relógio de alta resolução
    }

    /**
     * Encerra a medição de tempo.
     * Calcula e armazena a duração desde a última chamada de iniciar().
     */
    public void parar() {
        duracaoNanos = System.nanoTime() - instanteInicialNanos;
    }

    /**
     * @return duração total medida em nanossegundos.
     */
    public long getDuracaoEmNanos() {
        return duracaoNanos;
    }

    /**
     * @return duração total medida em microssegundos (µs).
     *         Conversão simples a partir de nanossegundos.
     */
    public double getDuracaoEmMicrossegundos() {
        return duracaoNanos / 1_000.0;
    }

    /**
     * @return duração total medida em milissegundos (ms).
     *         Útil para relatórios onde se pede ms.
     */
    public double getDuracaoEmMilissegundos() {
        return duracaoNanos / 1_000_000.0;
    }
}
