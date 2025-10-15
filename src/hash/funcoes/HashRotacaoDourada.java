package hash.funcoes;

public class HashRotacaoDourada implements FuncaoHash {

    /**
     * Aplica uma mistura leve com rotação e deslocamento.
     * @param valor valor inteiro original (chave)
     * @return valor misturado (ainda não reduzido ao intervalo da tabela)
     */
    private static int misturarPorRotacaoDourada(int valor) {
        // Multiplicação pela constante próxima da razão áurea em 32 bits
        int x = valor * 0x9E3779B1;
        // Rotação à esquerda de 13 bits e combinação com deslocamento à direita
        x = Integer.rotateLeft(x, 13) ^ (valor >>> 7);
        return x;
    }

    /**
     * Converte a chave em um índice válido para a tabela hash.
     * @param chave chave inteira
     * @param tamanhoTabela tamanho total da tabela (M)
     * @return índice no intervalo [0, tamanhoTabela-1]
     */
    @Override
    public int calcular(int chave, int tamanhoTabela) {
        int misturado = misturarPorRotacaoDourada(chave) & 0x7fffffff; // garante não-negativo
        return misturado % tamanhoTabela;
    }
}
