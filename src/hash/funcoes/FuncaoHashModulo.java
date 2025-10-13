package hash.funcoes;

public class FuncaoHashModulo implements FuncaoHash{
    @Override
    public int calcular(int chave, int tamanhoTabela){
        int hash = chave % tamanhoTabela;
        return hash;
    }
}