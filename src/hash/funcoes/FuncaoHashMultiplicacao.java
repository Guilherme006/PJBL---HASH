package hash.funcoes;

/*
Implementa a função hash que usa multiplicação
Fórmula: h(k) = m * ((k * A) % 1)
- k = chave
- A = constante entre 0 e 1
- m = tamanho da tabela
- (k * A) % 1 = parte fracionária do produto
*/
public class FuncaoHashMultiplicacao implements FuncaoHash{

    //Constante A = (√5 - 1) / 2 ≈ 0.6180339887
    //Número é o conjugado da razão áurea, buscando uma menor possibilidade de colisões
    private static final double A = 0.6180339887;

    @Override
    public int calcular(int chave, int tamanhoTabela){
        //Passo 1: multiplicar a chave pela constante A -> k * A
        //Isso gera um número double
        double produto = chave * A;

        //Passo 2: obter a parte fracionária do produto
        //Obtemos a parte inteira do produto e subtraímos do próprio produto para obter a parte fracionária
        int parteInteira = (int) produto;
        double parteFracao = produto - parteInteira;

        //Passo 3: multiplicar a parte fracionária pelo tamanho da tabela para obter o índice que irá conter a chave
        int hash = (int) (tamanhoTabela * parteFracao);

        //Retorna o índice final
        return hash;
    }
}
