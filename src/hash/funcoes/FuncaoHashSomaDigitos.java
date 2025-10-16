package hash.funcoes;

public class FuncaoHashSomaDigitos implements FuncaoHash{
    @Override
    public int calcular(int chave, int tamanhoTabela){
        int soma = 0;
        int k = chave;

        while(k != 0){
            soma += k % 10;
            k = k /10;
        }

        int hash = soma % tamanhoTabela;

        return hash;
    }
}
