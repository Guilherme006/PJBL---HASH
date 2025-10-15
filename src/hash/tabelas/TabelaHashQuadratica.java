package hash.tabelas;

import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

/**
 * Tabela hash com endereçamento aberto (sondagem quadrática).
 * - Índices sempre normalizados ao intervalo [0..tamanho-1].
 * - Cálculo em long para evitar overflow de i*i.
 */
public class TabelaHashQuadratica implements TabelaHash {

    /** Vetor principal de chaves. */
    private final int[] tabela;
    /** Marca se a posição já foi ocupada alguma vez. */
    private final boolean[] ocupado;
    /** Marca se a posição foi logicamente removida (mantido para futura extensão). */
    private final boolean[] removido;

    /** Capacidade da tabela. */
    private final int tamanho;
    /** Função hash utilizada. */
    private final FuncaoHash funcaoHash;

    /** Quantidade atual de elementos. */
    private int totalElementos;

    public TabelaHashQuadratica(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        this.ocupado = new boolean[tamanho];
        this.removido = new boolean[tamanho];
        this.totalElementos = 0;
    }

    /**
     * Insere a chave usando sondagem quadrática h + i^2.
     * Conta colisão para cada célula ocupada sondada.
     * Se não houver vaga após uma volta completa, retorna silenciosamente.
     */
    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        if (totalElementos >= tamanho) return; // tabela cheia (sem exceção)

        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            // (base + i*i) % tamanho calculado em long para não estourar
            final int indice = (int) (((long) base + (long) i * (long) i) % (long) tamanho);

            if (!ocupado[indice] || removido[indice]) {
                tabela[indice]   = chave;
                ocupado[indice]  = true;
                removido[indice] = false;
                totalElementos++;
                return;
            }

            if (tabela[indice] == chave) {
                // já existe; nada a fazer
                return;
            }

            // célula ocupada por outra chave -> conta colisão
            metrica.incrementarColisaoInsercao();
        }
        // sem vaga após uma volta -> não altera estado
    }

    /**
     * Busca a chave. Conta um passo por probe realizado.
     * Para quando encontra uma posição nunca ocupada.
     */
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
            if (!ocupado[indice]) { // slot nunca usado -> chave não existe
                metrica.adicionarPassosBusca(passos);
                return false;
            }
        }
        // deu a volta completa -> considera não encontrado
        metrica.adicionarPassosBusca(passos);
        return false;
    }

    /** Limpa a tabela (mantém a capacidade). */
    @Override
    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            ocupado[i] = false;
            removido[i] = false;
            tabela[i] = 0;
        }
        totalElementos = 0;
    }

    // =========================
    // Getters para as métricas
    // =========================

    /** @return capacidade da tabela (tamanho do vetor principal). */
    public int getTamanhoTabela() {
        return tamanho;
    }

    /** @return true se a posição i está vazia (nunca ocupada). */
    public boolean posicaoEstaVazia(int i) {
        return !ocupado[i];
    }
}
