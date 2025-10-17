import dados.GeradorDados;
import dados.Registro;

import execucao.CsvAmostraRegistros;
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
 * Orquestra os experimentos:
 * (M × N × estratégia × função hash)
 * - imprime tabela Markdown no console
 * - grava CSV de métricas (CsvResultados)
 * - imprime e grava uma AMOSTRA de registros por combinação (opcional)
 * Usa objetos Registro para cada elemento.
 */
public class Main {

    // ----- controles de saída -----
    private static final boolean GRAVAR_CSV_METRICAS     = true;
    private static final String  CAMINHO_CSV_METRICAS    = "resultados_hash.csv";

    private static final boolean EXIBIR_AMOSTRA_CONSOLE  = true;   // imprime a amostra no console
    private static final boolean GRAVAR_CSV_AMOSTRA      = true;   // grava amostra em CSV separado
    private static final String  CAMINHO_CSV_AMOSTRA     = "registros_amostra.csv";
    private static final int     TAMANHO_AMOSTRA         = 10;     // quantidade de registros por combinação

    public static void main(String[] args) throws Exception {
        ImpressoraResultados.imprimirCabecalho();

        CsvResultados csvMetricas = null;
        if (GRAVAR_CSV_METRICAS) {
            csvMetricas = new CsvResultados(CAMINHO_CSV_METRICAS);
            csvMetricas.abrir();
        }

        CsvAmostraRegistros csvAmostra = null;
        if (GRAVAR_CSV_AMOSTRA) {
            csvAmostra = new CsvAmostraRegistros(CAMINHO_CSV_AMOSTRA);
            csvAmostra.abrir();
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

                    boolean tabelaPequena = n > m;

                    executar("encadeada", new TabelaHashEncadeada(m, f), m, n, nomeFuncao, csvMetricas, csvAmostra);

                    if (!tabelaPequena) {
                        executar("linear", new TabelaHashLinear(m, f), m, n, nomeFuncao, csvMetricas, csvAmostra);
                        executar("quadratica", new TabelaHashQuadratica(m, f), m, n, nomeFuncao, csvMetricas, csvAmostra);
                    } else {
                        System.out.printf(
                                "Combinação inválida: n=%d > m=%d para função %s (linear/quadrática)%n",
                                n, m, nomeFuncao
                        );
                    }
                }
            }
        }

        if (csvMetricas != null) {
            csvMetricas.close();
        }
        if (csvAmostra != null) {
            csvAmostra.close();
        }
    }

    private static void executar(
            String nomeEstrategia,
            TabelaHash tabela,
            int tamanhoTabela,
            int quantidadeDados,
            String nomeFuncao,
            CsvResultados csvMetricas,
            CsvAmostraRegistros csvAmostra
    ) throws Exception {

        ContadoresMetrica cont = new ContadoresMetrica();
        cont.zerar();

        MedidorTempo tempoInsercao = new MedidorTempo();
        MedidorTempo tempoBusca    = new MedidorTempo();

        // Amostra de registros
        final int tamanhoAmostraEfetivo = Math.min(TAMANHO_AMOSTRA, quantidadeDados);
        final String[] amostra = new String[tamanhoAmostraEfetivo];
        int preenchidosAmostra = 0;

        // ---------- INSERÇÃO ----------
        GeradorDados gerador = new GeradorDados(ParametrosExperimento.SEMENTE, quantidadeDados);
        tempoInsercao.iniciar();
        while (gerador.temProximoElemento()) {
            Registro registro = new Registro(gerador.proximoCodigo());
            int chave = registro.getCodigo();

            // Coleta os primeiros N registros para amostra
            if (preenchidosAmostra < tamanhoAmostraEfetivo) {
                amostra[preenchidosAmostra++] = registro.getCodigoComoNoveDigitos();
            }

            tabela.inserir(chave, cont);
        }
        tempoInsercao.parar();

        // ---------- BUSCA ----------
        gerador.reiniciarSequencia();
        tempoBusca.iniciar();
        while (gerador.temProximoElemento()) {
            Registro registro = new Registro(gerador.proximoCodigo());
            tabela.buscar(registro.getCodigo(), cont);
        }
        tempoBusca.parar();

        // ---------- MÉTRICAS DE DISTRIBUIÇÃO ----------
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

        // ---------- SAÍDAS ----------
        // 1) Métricas em tabela Markdown
        ImpressoraResultados.imprimirLinha(
                tamanhoTabela, quantidadeDados, nomeEstrategia, nomeFuncao,
                tempoInsercao.getDuracaoEmMilissegundos(),
                tempoBusca.getDuracaoEmMilissegundos(),
                cont.getQuantidadeColisoesInsercao(),
                cont.getQuantidadePassosBusca(),
                gapMin, gapMedio, gapMax,
                top1, top2, top3
        );

        // 2) Amostra no console (opcional)
        if (EXIBIR_AMOSTRA_CONSOLE && tamanhoAmostraEfetivo > 0) {
            StringBuilder sb = new StringBuilder(64 + 12 * tamanhoAmostraEfetivo);
            sb.append("Amostra de registros (")
                    .append(tamanhoAmostraEfetivo).append(") [")
                    .append("M=").append(tamanhoTabela).append(", ")
                    .append("N=").append(quantidadeDados).append(", ")
                    .append("Estrategia=").append(nomeEstrategia).append(", ")
                    .append("Funcao=").append(nomeFuncao).append("]: ");

            for (int i = 0; i < tamanhoAmostraEfetivo; i++) {
                if (i > 0) sb.append(", ");
                sb.append(amostra[i]);
            }
            System.out.println(sb.toString());
        }

        // 3) CSV principal (métricas)
        if (csvMetricas != null) {
            csvMetricas.escreverLinha(
                    tamanhoTabela, quantidadeDados, nomeEstrategia, nomeFuncao,
                    tempoInsercao.getDuracaoEmMilissegundos(),
                    tempoBusca.getDuracaoEmMilissegundos(),
                    cont.getQuantidadeColisoesInsercao(),
                    cont.getQuantidadePassosBusca(),
                    gapMin, gapMedio, gapMax,
                    top1, top2, top3
            );
        }

        // 4) CSV da amostra de registros (um registro por linha)
        if (csvAmostra != null && tamanhoAmostraEfetivo > 0) {
            for (int i = 0; i < tamanhoAmostraEfetivo; i++) {
                csvAmostra.escreverLinha(
                        tamanhoTabela, quantidadeDados, nomeEstrategia, nomeFuncao,
                        i, amostra[i]
                );
            }
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
