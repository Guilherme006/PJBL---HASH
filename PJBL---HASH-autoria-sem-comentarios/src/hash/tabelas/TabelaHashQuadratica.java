package hash.tabelas;

import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

public class TabelaHashQuadratica implements TabelaHash {

    private final int[] tabela;
    private final boolean[] ocupado;
    private final boolean[] removido;

    private final int tamanho;
    private final FuncaoHash funcaoHash;

    private int totalElementos;

    public TabelaHashQuadratica(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        this.ocupado = new boolean[tamanho];
        this.removido = new boolean[tamanho];
        this.totalElementos = 0;
    }

    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        if (totalElementos >= tamanho) return;

        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            final int indice = (int) (((long) base + (long) i * (long) i) % (long) tamanho);

            if (!ocupado[indice] || removido[indice]) {
                tabela[indice]   = chave;
                ocupado[indice]  = true;
                removido[indice] = false;
                totalElementos++;
                return;
            }

            if (tabela[indice] == chave) {
                return;
            }

            metrica.incrementarColisaoInsercao();
        }
    }

    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        int passos = 0;
        for (int i = 0; i < tamanho; i++) {
            final int indice = (int) (((long) base + (long) i * (long) i) % (long) tamanho);
            passos++;

            if (ocupado[indice] && !removido[indice] && tabela[indice] == chave) {
                metrica.adicionarPassosBusca(passos);
                return true;
            }
            if (!ocupado[indice]) {
                metrica.adicionarPassosBusca(passos);
                return false;
            }
        }
        metrica.adicionarPassosBusca(passos);
        return false;
    }

    @Override
    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            ocupado[i] = false;
            removido[i] = false;
            tabela[i] = 0;
        }
        totalElementos = 0;
    }

    public int getTamanhoTabela() {
        return tamanho;
    }

    public boolean posicaoEstaVazia(int i) {
        return !ocupado[i];
    }
}
