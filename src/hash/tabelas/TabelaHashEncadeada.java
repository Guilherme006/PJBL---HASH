package hash.tabelas;

import estruturas.ListaEncadeada;
import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

public class TabelaHashEncadeada implements TabelaHash {

    private final ListaEncadeada[] tabela;

    private final int tamanho;

    private final FuncaoHash funcaoHash;

    public TabelaHashEncadeada(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new ListaEncadeada[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new ListaEncadeada();
        }
    }

    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        final int indice = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);
        final ListaEncadeada lista = tabela[indice];

        if (lista.tamanho() > 0) {
            metrica.incrementarColisaoInsercao();
        }

        lista.insereFinal(chave);
    }

    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        final int indice = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);
        final ListaEncadeada lista = tabela[indice];

        int passos = lista.tamanho();

        boolean encontrado = lista.buscar(chave);
        metrica.adicionarPassosBusca(passos);
        return encontrado;
    }

    @Override
    public void limpar() {
        for (ListaEncadeada lista : tabela) {
            lista.limpar();
        }
    }


    public int getQuantidadeBuckets() {
        return tamanho;
    }

    public boolean bucketEstaVazio(int i) {
        return tabela[i].tamanho() == 0;
    }

    public int comprimentoDaLista(int i) {
        return tabela[i].tamanho();
    }
}
