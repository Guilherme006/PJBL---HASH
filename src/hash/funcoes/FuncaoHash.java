package hash.funcoes;

//Interface base para criar as funções hash
public interface FuncaoHash {
    //Calcula o índice da tabela correspondente a uma chave
    //recebe uma chave que será transformada em índice
    //recebe o tamanho da tabela
    int calcular(int chave, int tamanhoTabela);
}
