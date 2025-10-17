package dados;

import java.util.Random;
import java.util.function.IntConsumer;

public class GeradorDados {
    private final long sementeAleatoria;
    private final int quantidadeTotalElementos;
    private final int limiteSuperiorCodigoExclusivo = 1_000_000_000;

    private Random geradorAleatorio;
    private int quantidadeJaGerada;

    public GeradorDados(long sementeAleatoria, int quantidadeTotalElementos) {
        if (quantidadeTotalElementos < 0) {
            throw new IllegalArgumentException("A quantidade total de elementos não pode ser negativa.");
        }
        this.sementeAleatoria = sementeAleatoria;
        this.quantidadeTotalElementos = quantidadeTotalElementos;
        this.geradorAleatorio = new Random(sementeAleatoria);
        this.quantidadeJaGerada = 0;
    }

    public void reiniciarSequencia() {
        this.geradorAleatorio = new Random(sementeAleatoria);
        this.quantidadeJaGerada = 0;
    }

    public boolean temProximoElemento() {
        return quantidadeJaGerada < quantidadeTotalElementos;
    }

    public int proximoCodigo() {
        if (!temProximoElemento()) {
            throw new IllegalStateException("Não há mais elementos para gerar.");
        }
        quantidadeJaGerada++;
        return geradorAleatorio.nextInt(limiteSuperiorCodigoExclusivo);
    }

    public int getQuantidadeTotalElementos() {
        return quantidadeTotalElementos;
    }

    public int getQuantidadeJaGerada() {
        return quantidadeJaGerada;
    }

    public static String formatarComoNoveDigitos(int codigo) {
        return String.format("%09d", codigo);
    }

    public void paraCadaCodigo(IntConsumer consumidorDeCodigo) {
        while (temProximoElemento()) {
            consumidorDeCodigo.accept(proximoCodigo());
        }
    }
}
