package estruturas;

/**
 * Contrato mínimo para exportar dados necessários às análises:
 * - Encadeamento: cabeças e "próximo" do pool intrusivo.
 * - Aberto (linear/quadrático): vetor principal e sentinela de vazio.
 * Implementações podem retornar null para o que não se aplica.
 */
public interface ExportacaoAnaliseDistribuicao {
    // Encadeamento
    int[] obterVetorCabecas();     // buckets; vazio = -1 (ou null se não se aplica)
    int[] obterVetorProximoPool(); // "próximo" do pool (ou null se não se aplica)

    // Aberto (linear/quadrático)
    int[] obterVetorPrincipal();   // array de chaves (ou null se não se aplica)
    int  obterValorSentinelaVazio(); // ex.: 0x80000000 (se não se aplica, pode retornar 0)
}
