package hash.tabelas;

import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

public class TabelaHashLinear implements TabelaHash {

    private final int[] tabela;

    private final int tamanho;

    private final FuncaoHash funcaoHash;

    private static final int VAZIO = -1;

    public TabelaHashLinear(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        limpar();
    }

    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            final int posicao = (base + i) % tamanho;

            if (tabela[posicao] == VAZIO) {
                tabela[posicao] = chave;
                return;
            }

            metrica.incrementarColisaoInsercao();
        }
    }

    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            final int posicao = (base + i) % tamanho;
            metrica.adicionarPassosBusca(1);

            if (tabela[posicao] == VAZIO) {
                return false;
            }
            if (tabela[posicao] == chave) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = VAZIO;
        }
    }

    public int getTamanhoTabela() {
        return tamanho;
    }

    public boolean posicaoEstaVazia(int i) {
        return tabela[i] == VAZIO;
    }
}
