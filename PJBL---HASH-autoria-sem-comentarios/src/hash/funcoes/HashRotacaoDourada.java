package hash.funcoes;

public class HashRotacaoDourada implements FuncaoHash {

    private static int misturarPorRotacaoDourada(int valor) {
        int x = valor * 0x9E3779B1;
        x = Integer.rotateLeft(x, 13) ^ (valor >>> 7);
        return x;
    }

    @Override
    public int calcular(int chave, int tamanhoTabela) {
        int misturado = misturarPorRotacaoDourada(chave) & 0x7fffffff;
        return misturado % tamanhoTabela;
    }
}
