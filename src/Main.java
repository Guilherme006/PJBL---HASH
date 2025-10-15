import dados.GeradorDados;
import execucao.CsvResultados;
import execucao.ImpressoraResultados;
import execucao.ParametrosExperimento;
import medidas.ContadoresMetrica;
import medidas.MedidorTempo;

import hash.funcoes.FuncaoHash;
import hash.funcoes.FuncaoHashMultiplicacao;
import hash.funcoes.FuncaoHashSomaDigitos;
import hash.funcoes.HashRotacaoDourada;

import hash.tabelas.TabelaHash;
import hash.tabelas.TabelaHashEncadeada;
import hash.tabelas.TabelaHashLinear;
import hash.tabelas.TabelaHashQuadratica;

/**
 * Orquestra os experimentos: (M × N × estratégia × função hash)
 * Imprime tabela em Markdown e grava CSV (quando habilitado).
 */
public class Main {

    private static final boolean GRAVAR_CSV = true;
    private static final String CAMINHO_CSV = "resultados_hash.csv";

    public static void main(String[] args) throws Exception {
        ImpressoraResultados.imprimirCabecalho();

        CsvResultados csv = null;
        if (GRAVAR_CSV) {
            csv = new CsvResultados(CAMINHO_CSV);
            csv.abrir();
        }

        int[] tamanhosTabela = ParametrosExperimento.TAMANHOS_TABELA;
        int[] tamanhosDados  = ParametrosExperimento.TAMANHOS_DADOS;

        FuncaoHash[] funcoes = {
                new FuncaoHashMultiplicacao(),
                new FuncaoHashSomaDigitos(),
                new HashRotacaoDourada()
        };
        String[] nomesFuncoes = {
                "FuncaoHashMultiplicacao",
                "FuncaoHashSomaDigitos",
                "HashRotacaoDourada"
        };

        for (int m : tamanhosTabela) {
            for (int n : tamanhosDados) {
                for (int i = 0; i < funcoes.length; i++) {
                    String nomeFuncao = nomesFuncoes[i];
                    FuncaoHash f = funcoes[i];

                    executar("encadeada", new TabelaHashEncadeada(m, f), m, n, nomeFuncao, csv);
                    executar("linear",    new TabelaHashLinear(m, f),     m, n, nomeFuncao, csv);
                    executar("quadratica",new TabelaHashQuadratica(m, f), m, n, nomeFuncao, csv);
                }
            }
        }

        if (csv != null) {
            csv.close();
        }
    }

    private static void executar(String nomeEstrategia, TabelaHash tabela,
                                 int tamanhoTabela, int quantidadeDados,
                                 String nomeFuncao, CsvResultados csv) throws Exception {

        ContadoresMetrica cont = new ContadoresMetrica();
        cont.zerar();

        MedidorTempo tempoInsercao = new MedidorTempo();
        MedidorTempo tempoBusca    = new MedidorTempo();

        GeradorDados gerador = new GeradorDados(ParametrosExperimento.SEMENTE, quantidadeDados);

        // Inserção
        tempoInsercao.iniciar();
        while (gerador.temProximoElemento()) {
            int chave = gerador.proximoCodigo();
            tabela.inserir(chave, cont);
        }
        tempoInsercao.parar();

        // Busca (mesma sequência)
        gerador.reiniciarSequencia();
        tempoBusca.iniciar();
        while (gerador.temProximoElemento()) {
            int chave = gerador.proximoCodigo();
            tabela.buscar(chave, cont);
        }
        tempoBusca.parar();

        // --------- Cálculo das métricas de distribuição ---------
        int gapMin = 0, gapMax = 0, top1 = 0, top2 = 0, top3 = 0;
        double gapMedio = 0.0;

        switch (nomeEstrategia) {
            case "encadeada": {
                TabelaHashEncadeada te = (TabelaHashEncadeada) tabela;
                Estatisticas g = calcularGapsEncadeada(te);
                gapMin = g.min; gapMedio = g.media; gapMax = g.max;

                int[] top = obterTop3Encadeada(te);
                top1 = top[0]; top2 = top[1]; top3 = top[2];
                break;
            }
            case "linear": {
                TabelaHashLinear tl = (TabelaHashLinear) tabela;
                Estatisticas g = calcularGapsAberta(tl.getTamanhoTabela(), tl::posicaoEstaVazia);
                gapMin = g.min; gapMedio = g.media; gapMax = g.max;
                break;
            }
            case "quadratica": {
                TabelaHashQuadratica tq = (TabelaHashQuadratica) tabela;
                Estatisticas g = calcularGapsAberta(tq.getTamanhoTabela(), tq::posicaoEstaVazia);
                gapMin = g.min; gapMedio = g.media; gapMax = g.max;
                break;
            }
        }
        // --------------------------------------------------------

        // Saída no console
        ImpressoraResultados.imprimirLinha(
                tamanhoTabela, quantidadeDados, nomeEstrategia, nomeFuncao,
                tempoInsercao.getDuracaoEmMilissegundos(),
                tempoBusca.getDuracaoEmMilissegundos(),
                cont.getQuantidadeColisoesInsercao(),
                cont.getQuantidadePassosBusca(),
                gapMin, gapMedio, gapMax, top1, top2, top3
        );

        // CSV opcional
        if (csv != null) {
            csv.escreverLinha(
                    tamanhoTabela, quantidadeDados, nomeEstrategia, nomeFuncao,
                    tempoInsercao.getDuracaoEmMilissegundos(),
                    tempoBusca.getDuracaoEmMilissegundos(),
                    cont.getQuantidadeColisoesInsercao(),
                    cont.getQuantidadePassosBusca(),
                    gapMin, gapMedio, gapMax, top1, top2, top3
            );
        }
    }

    // =================== Helpers de métricas ===================

    // Encadeada: gaps nos buckets (sequências de buckets vazios)
    private static Estatisticas calcularGapsEncadeada(TabelaHashEncadeada t) {
        int m = t.getQuantidadeBuckets();
        int gapAtual = 0, gapMin = Integer.MAX_VALUE, gapMax = 0, blocos = 0;
        long soma = 0L;

        for (int i = 0; i < m; i++) {
            if (t.bucketEstaVazio(i)) {
                gapAtual++;
            } else if (gapAtual > 0) {
                blocos++; soma += gapAtual;
                if (gapAtual < gapMin) gapMin = gapAtual;
                if (gapAtual > gapMax) gapMax = gapAtual;
                gapAtual = 0;
            }
        }
        if (gapAtual > 0) {
            blocos++; soma += gapAtual;
            if (gapAtual < gapMin) gapMin = gapAtual;
            if (gapAtual > gapMax) gapMax = gapAtual;
        }
        if (blocos == 0) return new Estatisticas(0, 0.0, 0);
        return new Estatisticas(gapMin, (double) soma / blocos, gapMax);
    }

    // Encadeada: top-3 tamanhos de lista nos buckets
    private static int[] obterTop3Encadeada(TabelaHashEncadeada t) {
        int m = t.getQuantidadeBuckets();
        int a = 0, b = 0, c = 0;
        for (int i = 0; i < m; i++) {
            int len = t.comprimentoDaLista(i);
            if (len >= a) { c = b; b = a; a = len; }
            else if (len >= b) { c = b; b = len; }
            else if (len > c) { c = len; }
        }
        return new int[]{a, b, c};
    }

    // Aberta (linear/quadrática): gaps no vetor principal (runs de posições vazias)
    private static Estatisticas calcularGapsAberta(int tamanho, java.util.function.IntPredicate posicaoVazia) {
        int gapAtual = 0, gapMin = Integer.MAX_VALUE, gapMax = 0, blocos = 0;
        long soma = 0L;

        for (int i = 0; i < tamanho; i++) {
            if (posicaoVazia.test(i)) {
                gapAtual++;
            } else if (gapAtual > 0) {
                blocos++; soma += gapAtual;
                if (gapAtual < gapMin) gapMin = gapAtual;
                if (gapAtual > gapMax) gapMax = gapAtual;
                gapAtual = 0;
            }
        }
        if (gapAtual > 0) {
            blocos++; soma += gapAtual;
            if (gapAtual < gapMin) gapMin = gapAtual;
            if (gapAtual > gapMax) gapMax = gapAtual;
        }
        if (blocos == 0) return new Estatisticas(0, 0.0, 0);
        return new Estatisticas(gapMin, (double) soma / blocos, gapMax);
    }

    // Estrutura simples para devolver as 3 métricas de gaps
    private static final class Estatisticas {
        final int min; final double media; final int max;
        Estatisticas(int min, double media, int max) { this.min = min; this.media = media; this.max = max; }
    }
}
