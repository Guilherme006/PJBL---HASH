package hash.tabelas;

import hash.funcoes.FuncaoHash;
import medidas.ContadoresMetrica;

/**
 * Tabela hash com endereçamento aberto e sondagem linear.
 * Em caso de colisão, avança uma posição por vez até encontrar vaga.
 */
public class TabelaHashLinear implements TabelaHash {

    /** Vetor principal onde as chaves são armazenadas. */
    private final int[] tabela;

    /** Capacidade da tabela (tamanho do vetor). */
    private final int tamanho;

    /** Função hash utilizada para mapear a chave à posição base. */
    private final FuncaoHash funcaoHash;

    /** Valor sentinela que representa posição vazia. */
    private static final int VAZIO = -1;

    public TabelaHashLinear(int tamanho, FuncaoHash funcaoHash) {
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        limpar(); // inicia todas as posições como vazias
    }

    /**
     * Insere uma chave. Se houver colisão, tenta as próximas posições.
     * Contabiliza uma colisão para cada célula ocupada sondada.
     */
    @Override
    public void inserir(int chave, ContadoresMetrica metrica) {
        // posição base já normalizada no intervalo [0..tamanho-1]
        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            final int posicao = (base + i) % tamanho; // sondagem linear

            if (tabela[posicao] == VAZIO) {
                tabela[posicao] = chave;
                return; // inseriu com sucesso
            }

            // célula ocupada → conta colisão e segue sondando
            metrica.incrementarColisaoInsercao();
        }
        // tabela efetivamente cheia após uma volta: retorna silenciosamente
    }

    /**
     * Busca uma chave. Conta um passo para cada célula visitada.
     * Para quando encontra uma posição vazia (chave não existe).
     */
    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica) {
        final int base = Math.floorMod(funcaoHash.calcular(chave, tamanho), tamanho);

        for (int i = 0; i < tamanho; i++) {
            final int posicao = (base + i) % tamanho;
            metrica.adicionarPassosBusca(1); // um passo por probe

            if (tabela[posicao] == VAZIO) {
                return false; // encontrou uma lacuna → chave não está na tabela
            }
            if (tabela[posicao] == chave) {
                return true;  // achou
            }
        }
        // deu a volta completa sem encontrar nem lacuna nem a chave
        return false;
    }

    /** Limpa a tabela marcando todas as posições como vazias. */
    @Override
    public void limpar() {
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = VAZIO;
        }
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
        return tabela[i] == VAZIO;
    }
}
