package medidas;

public class MedidorTempo {
    private long instanteInicialNanos;

    private long duracaoNanos;

    public void iniciar() {
        duracaoNanos = 0L;              
        instanteInicialNanos = System.nanoTime();
    }

    public void parar() {
        duracaoNanos = System.nanoTime() - instanteInicialNanos;
    }

    public long getDuracaoEmNanos() {
        return duracaoNanos;
    }

    public double getDuracaoEmMicrossegundos() {
        return duracaoNanos / 1_000.0;
    }

    public double getDuracaoEmMilissegundos() {
        return duracaoNanos / 1_000_000.0;
    }
}
