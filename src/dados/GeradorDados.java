package dados;

import java.util.Random;
import java.util.function.IntConsumer;

public class GeradorDados {
    private final long sementeAleatoria;
    private final int quantidadeTotalElementos;
    private final int limiteSuperiorCodigoExclusivo = 1_000_000_000; // 9 dígitos: 0..999_999_999

    private Random geradorAleatorio;
    private int quantidadeJaGerada;

    /**
     * @param sementeAleatoria semente usada para tornar a sequência reprodutível
     * @param quantidadeTotalElementos total de elementos que serão gerados
     */
    public GeradorDados(long sementeAleatoria, int quantidadeTotalElementos) {
        if (quantidadeTotalElementos < 0) {
            throw new IllegalArgumentException("A quantidade total de elementos não pode ser negativa.");
        }
        this.sementeAleatoria = sementeAleatoria;
        this.quantidadeTotalElementos = quantidadeTotalElementos;
        this.geradorAleatorio = new Random(sementeAleatoria);
        this.quantidadeJaGerada = 0;
    }

    /** Reseta o gerador para o início da sequência, mantendo a mesma semente. */
    public void reiniciarSequencia() {
        this.geradorAleatorio = new Random(sementeAleatoria);
        this.quantidadeJaGerada = 0;
    }

    /** @return true se ainda há elementos a serem gerados. */
    public boolean temProximoElemento() {
        return quantidadeJaGerada < quantidadeTotalElementos;
    }

    /**
     * @return próximo código inteiro no intervalo [0, 999_999_999]
     * @throws IllegalStateException se a quantidade solicitada exceder o total configurado
     */
    public int proximoCodigo() {
        if (!temProximoElemento()) {
            throw new IllegalStateException("Não há mais elementos para gerar.");
        }
        quantidadeJaGerada++;
        return geradorAleatorio.nextInt(limiteSuperiorCodigoExclusivo);
    }

    /** @return quantidade total configurada para esta execução. */
    public int getQuantidadeTotalElementos() {
        return quantidadeTotalElementos;
    }

    /** @return quantidade já gerada até o momento. */
    public int getQuantidadeJaGerada() {
        return quantidadeJaGerada;
    }

    /** Formata um código inteiro como texto com 9 dígitos (zeros à esquerda). */
    public static String formatarComoNoveDigitos(int codigo) {
        return String.format("%09d", codigo);
    }

    /**
     * Gera todos os elementos chamando o consumidor para cada código.
     */
    public void paraCadaCodigo(IntConsumer consumidorDeCodigo) {
        while (temProximoElemento()) {
            consumidorDeCodigo.accept(proximoCodigo());
        }
    }
}
