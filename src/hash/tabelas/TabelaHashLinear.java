package hash.tabelas;

import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

/*
Classe que implementa uma tabela hash linear, em caso de colisão, a tabela busca a próxima posição uma por uma
 */
public class TabelaHashLinear implements TabelaHash{
    //Vetor para armazenar as chaves
    private final int[] tabela;
    //Tamanho da tabela
    private final int tamanho;
    //Define qual função hash será usada
    private final FuncaoHash funcaoHash;
    //Constante que representa o estado vazio de uma posição
    private static final int vazio = -1;

    public TabelaHashLinear(int tamanho, FuncaoHash funcaoHash){
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        limpar(); //Inicia todas as posições como vazias (-1)
    }

    /*
    Método que insere uma chave na tabela
    Se houver colisão, tenta a próxima posição até achar uma posição livre
    Contabiliza colisões através da classe ContadoresMetrica
     */
    @Override
    public void inserir(int chave, ContadoresMetrica metrica){
        //Calcula a posição inicial usando a função hash
        int hashBase = funcaoHash.calcular(chave, tamanho);
        //Posição que tentaremos inserir a chave
        int pos =  hashBase;

        //Laço de repetição para iterar sobre a tabela
        for(int i = 0; i < tamanho; i++){
            //Rehashing
            pos = (hashBase + i) % tamanho;
            //Se encontrar uma posição livre, insere na posição e encerra
            if(tabela[pos] == vazio){
                tabela[pos] = chave;
                return; //Sai do método após inserir
            }
            //Caso contrário, significa que houve colisão, conta mais uma colisão
            metrica.incrementarColisaoInsercao();
        }

    }
}
