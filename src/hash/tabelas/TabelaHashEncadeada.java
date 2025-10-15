package hash.tabelas;

import estruturas.ListaEncadeada;
import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

/**
 * Implementação de tabela hash com encadeamento separado.
 * - Colisão (inserção): conta 1 quando o bucket já possuía elemento.
 * - Passos (busca): usa o tamanho da lista do bucket (pior caso),
 *   pois a ListaEncadeada não expõe iteração sobre os nós.
 */
public class TabelaHashEncadeada implements TabelaHash {

    /** Vetor principal de buckets (cada posição é uma lista encadeada). */
    private final ListaEncadeada[] tabela;

    /** Quantidade de buckets. */
    private final int tamanho;

    /** Função hash utilizada para mapear a chave ao bucket. */
    private final FuncaoHash funcaoHash;

    /**
     * Constrói a tabela hash encadeada.
     *
     * @param tamanho número de buckets
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
     * Regra: se já havia pelo menos um elemento no bucket, conta 1 colisão.
     */
    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        // Normaliza o índice para [0..tamanho-1] (mesmo se a função hash devolver negativo)
        final int indice = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);
        final ListaEncadeada lista = tabela[indice];

        // Se o bucket já tinha algum elemento, houve colisão nesta inserção
        if (lista.tamanho() > 0) {
            metrica.incrementarColisaoInsercao();
        }

        // Inserção (sua lista já tem esse método)
        lista.insereFinal(chave);
    }

    /**
     * Busca uma chave, contabilizando passos.
     * Como a ListaEncadeada não expõe a iteração sobre os nós, registramos
     * os passos como o tamanho da lista do bucket (pior caso).
     */
    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        final int indice = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);
        final ListaEncadeada lista = tabela[indice];

        // Pior caso: percorre toda a lista do bucket
        int passos = lista.tamanho();

        boolean encontrado = lista.buscar(chave);
        metrica.adicionarPassosBusca(passos);
        return encontrado;
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

    // =========================
    // Getters para as métricas
    // =========================

    /** @return quantidade de buckets (tamanho do vetor principal). */
    public int getQuantidadeBuckets() {
        return tamanho;
    }

    /** @return true se o bucket i não possui elementos. */
    public boolean bucketEstaVazio(int i) {
        return tabela[i].tamanho() == 0;
    }

    /** @return comprimento (número de elementos) da lista no bucket i. */
    public int comprimentoDaLista(int i) {
        return tabela[i].tamanho();
    }
}
