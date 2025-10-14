package hash.funcoes;

//Implementa uma função hash baseada na soma dos dígitos da chave
public class FuncaoHashSomaDigitos implements FuncaoHash{
    //Calcula o hash de um inteiro
    @Override
    public int calcular(int chave, int tamanhoTabela){
        int soma = 0; //Variável para acumular a soma dos dígitos da chave
        int k = chave; //cópia da chave

        //Loop enquanto k ainda tiver dígitos
        while(k != 0){
            soma += k % 10; //Obtém o último dígito de k
            k = k /10; //Remove o último dígito de k
        }

        //Aplica módulo no resultado da soma dos dígitos sobre o tamanho da tabela
        int hash = soma % tamanhoTabela;

        //Retorna o índice calculado
        return hash;
    }
}
