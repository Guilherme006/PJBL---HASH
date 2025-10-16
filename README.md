#  Tabelas Hash em Java

**Disciplina:** Resolução de Problemas Estruturados em Computação  
**Período:** 4º Período  
**Instituição:** Pontifícia Universidade Católica do Paraná (PUCPR)  
**Autores:**  
- Danillo Gonçalves Camargo da Silva
- Guilherme Felippe Lazari
- Rodrigo Schiavinatto Plassmann
- Thomas Manussadjian Steinhausser

##  Descrição e Objetivo

Este projeto tem como objetivo **implementar e analisar o desempenho de diferentes tabelas hash em Java**.
O foco é comparar **estratégias de tratamento de colisões** (como *rehashing* e *encadeamento*), avaliando o **tempo de inserção, tempo de busca, número de colisões e distribuição dos elementos**.

Foram implementadas **três variações de tabelas hash** e **três funções hash distintas**, aplicadas sobre diferentes conjuntos de dados gerados aleatoriamente com *seeds* fixas, garantindo a reprodutibilidade dos experimentos.

---

## Funções Hash: Descrição, funcionamento e análise

No projeto utilizamos três funções hash implementadas em `src/hash/funcoes/`. Abaixo está uma explicação técnica de cada uma, com vantagens, desvantagens e justificativa da escolha.

### `FuncaoHashSomaDigitos` (Soma dos dígitos)
**Descrição / Algoritmo:**  
Calcula a soma dos dígitos decimais do código do `Registro` e aplica `soma % M` onde `M` é o tamanho do vetor da tabela. Ex.: para `123456789`, soma = `45`, índice = `45 % M`.

**Complexidade:** O(1) por elemento (variação pequena e constante, soma de 9 dígitos).

**Vantagens:**  
- Implementação simples e determinística.  
- Rápida execução (opera sobre 9 dígitos fixos).

**Desvantagens:**  
- Fraca distribuição para códigos com padrões semelhantes (ex.: se muitos códigos têm dígitos repetidos ou prefixos comuns, gera clusters).  
- Alta chance de colisões quando M tem fatores em comum com distribuições dos dígitos.

**Justificativa de uso:**  
Útil como baseline: mostra comportamento de uma função muito simples para comparação com funções mais sofisticadas.

---

### `FuncaoHashMultiplicacao` (Método da multiplicação)
**Descrição / Algoritmo:**  
Converte o código em inteiro `k`, multiplica por uma constante `A` (0 < A < 1, tipicamente baseada em números irracionais) e usa a parte fracionária: `h(k) = floor( M * frac(k * A) )`. Em implementações inteiras, usa-se `((k * c) >>> shift) & (M-1)` quando M é potência de 2.

**Complexidade:** O(1)

**Vantagens:**  
- Boa dispersão em muitos casos práticos.  
- Menos sensível a padrões lineares nos dados comparado à soma de dígitos.  
- Implementação rápida e de baixo custo.

**Desvantagens:**  
- Requer escolha cuidadosa da constante `A` para obter dispersão ideal.  
- Quando implementado de forma incorreta (ou com M não adequado) pode gerar artefatos.

**Justificativa de uso:**  
Método clássico que frequentemente oferece uma boa troca entre simplicidade e dispersão, por isso é uma ótima segunda linha de comparação.

---

### `HashRotacaoDourada` (Rotação / Proporção Áurea)
**Descrição / Algoritmo:**  
Inspira-se em técnicas que usam constantes irracionais (por exemplo, proporção áurea φ) e operações bitwise/rotacionais para misturar os bits do número chave. Uma implementação típica: multiplica `k` por uma constante relacionada à proporção áurea e realiza rotações/xors para espalhar melhor os bits antes de aplicar `% M`.

**Complexidade:** O(1)

**Vantagens:**  
- Muito boa dispersão de bits, tende a reduzir grandes agrupamentos (clustering).  
- Robusta contra padrões comuns nos códigos; geralmente gera menos colisões do que funções muito simples.

**Desvantagens:**  
- Um pouco mais custosa computacionalmente (operações de multiplicação e rotações).  
- Dependente da implementação: má escolha das operações ou constantes pode reduzir qualidade.

**Justificativa de uso:**  
Escolhida por ser uma função com tendência comprovada a distribuir bem chaves numéricas, servindo como alternativa mais “sofisticada” às anteriores.

##  Funcionamento do Sistema

O sistema foi projetado de forma modular e automatizada para permitir **execuções de experimentos controlados**, desde a geração dos dados até a análise e exportação dos resultados.  
O fluxo principal é executado pelo arquivo `Main.java`, que coordena todas as etapas:

1. **Geração dos dados**  
   - A classe `GeradorDados` cria três conjuntos de registros (100 mil, 1 milhão e 10 milhões de elementos), cada um com códigos únicos de 9 dígitos.
   - Os dados são gerados com *seed* fixa para garantir a reprodutibilidade.

2. **Configuração dos experimentos**  
   - A classe `ParametrosExperimento` define os tamanhos das tabelas hash (ex: 10³, 10⁴ e 10⁵) e as funções hash utilizadas.
   - As funções hash implementadas são:
     - `FuncaoHashSomaDigitos` — soma dos dígitos do código.
     - `FuncaoHashMultiplicacao` — método da multiplicação.
     - `HashRotacaoDourada` — variação baseada na constante da proporção áurea.

3. **Inserção e busca nas tabelas**  
   - Cada conjunto de dados é inserido em três implementações de tabela hash:
     - `TabelaHashEncadeada` — usa listas encadeadas (`ListaEncadeada`) para resolver colisões.
     - `TabelaHashLinear` — usa *rehashing* linear.
     - `TabelaHashQuadratica` — usa *rehashing* quadrático.
   - Durante as inserções e buscas, o sistema coleta:
     - Tempo total de execução.
     - Número total de colisões.
     - Gaps (espaços) entre elementos armazenados.
     - Tamanho das listas encadeadas.

4. **Medições e estatísticas**  
   - A classe `MedidorTempo` mede o tempo de inserção e busca (em milissegundos ou microssegundos, conforme a escala).
   - `ContadoresMetrica` contabiliza o número de colisões e interações.
   - `EstatisticasGap` calcula o menor, maior e média dos gaps.

5. **Exportação e análise dos resultados**  
   - Os resultados são gravados em arquivos CSV (`CsvResultados`, `CsvAmostraRegistros`).
   - O módulo `ExportacaoAnaliseDistribuicao` e `AnalisadorDistribuicao` auxiliam na análise da distribuição e criação de relatórios visuais.
   - É possível visualizar as três maiores listas encadeadas e comparar o comportamento das diferentes funções hash.

---

##  Estrutura do Projeto

```
src/
│
├── Main.java
│
├── dados/
│ ├── GeradorDados.java
│ └── Registro.java
│
├── estruturas/
│ ├── ListaEncadeada.java
│ ├── No.java
│ ├── AnalisadorDistribuicao.java
│ ├── EstatisticasGap.java
│ └── ExportacaoAnaliseDistribuicao.java
│
├── execucao/
│ ├── CsvAmostraRegistros.java
│ ├── CsvResultados.java
│ ├── ParametrosExperimento.java
│ └── ImpressoraResultados.java
│
├── hash/
│ ├── funcoes/
│ │ ├── FuncaoHash.java
│ │ ├── FuncaoHashSomaDigitos.java
│ │ ├── FuncaoHashMultiplicacao.java
│ │ └── HashRotacaoDourada.java
│ │
│ └── tabelas/
│ ├── TabelaHash.java
│ ├── TabelaHashEncadeada.java
│ ├── TabelaHashLinear.java
│ └── TabelaHashQuadratica.java
│
└── medidas/
├── ContadoresMetrica.java
└── MedidorTempo.java
```

##  Métricas Avaliadas

Durante os testes, o sistema mede automaticamente:

| Métrica | Descrição |
|----------|------------|
|  **Tempo de Inserção** | Tempo total para inserir todos os elementos. |
|  **Tempo de Busca** | Tempo para buscar todos os elementos. |
|  **Colisões** | Número total de colisões durante inserção. |
|  **Gaps** | Menor, maior e média dos espaços entre elementos. |
|  **Maiores Listas** | Tamanho das três maiores listas encadeadas. |

Os resultados são exportados em CSV para posterior visualização em planilhas ou ferramentas gráficas.

##  Exemplos simulados de Resultados

| Tabela Hash | Função Hash | Tamanho Vetor | Dataset | Tempo Inserção (ms) | Colisões | Tempo Busca (ms) |
|--------------|-------------|----------------|----------|----------------------|-----------|-------------------|
| Encadeada | Soma dos Dígitos | 100.000 | 1.000.000 | 235 | 14.532 | 118 |
| Linear | Multiplicação | 100.000 | 1.000.000 | 184 | 10.912 | 92 |
| Quadrática | Rotação Dourada | 100.000 | 1.000.000 | 179 | 8.874 | 89 |

> Dados ilustrativos.
