package hash.tabelas;

import medidas.ContadoresMetrica;

//Interface base para criar as tabelas hash
public interface TabelaHash {
    //Insere uma chave na tabela, métrica contabiliza colisões e estatísticas de desempenho
    void inserir(int chave, ContadoresMetrica metrica);

    //Busca uma chave na tabela, métrica contabiliza passos realizados na busca, retorna true se a chave for encontrada
    boolean buscar(int chave, ContadoresMetrica metrica);

    //Limpa todo o conteúdo da tabela hash para garantir que ela comece vazia antes de realizar outros testes
    void limpar();
}
