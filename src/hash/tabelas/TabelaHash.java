package hash.tabelas;

import medidas.ContadoresMetrica;

public interface TabelaHash {
    void inserir(int chave, ContadoresMetrica metrica);

    boolean buscar(int chave, ContadoresMetrica metrica);

    void limpar();
}
