package hash.tabelas;

import estruturas.ListaEncadeada;
import medidas.ContadoresMetrica;
import hash.funcoes.FuncaoHash;

/**
 * Implementação de tabela hash com encadeamento separado.
 */
public class TabelaHashEncadeada implements TabelaHash {

    private final ListaEncadeada[] tabela;
    private final int tamanho;
    private final FuncaoHash funcaoHash;

    /**
     * Constrói a tabela hash encadeada.
     *
     * @param tamanho    número de buckets
     * @param funcaoHash estratégia de hash a usar
     */
    public TabelaHashEncadeada(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new ListaEncadeada[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new ListaEncadeada();
        }
    }

    /**
     * Insere uma chave, contabilizando colisões.
     */
    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        int indice = funcaoHash.calcular(chave, tamanho);
        ListaEncadeada lista = tabela[indice];

        // cada elemento já existente gera uma colisão para este novo
        int colisoes = lista.tamanho();
        for (int i = 0; i < colisoes; i++) {
            metrica.incrementarColisaoInsercao();
        }

        lista.insereFinal(chave);
    }

    /**
     * Busca uma chave, contabilizando passos percorridos.
     */
    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        int indice = funcaoHash.calcular(chave, tamanho);
        ListaEncadeada lista = tabela[indice];
        return lista.buscar(chave);
    }

    /**
     * Limpa todas as listas, deixando a tabela vazia.
     */
    @Override
    public void limpar() {
        for (ListaEncadeada lista : tabela) {
            lista.limpar();
        }
    }
}
