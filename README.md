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
Método clássico que frequentemente oferece uma boa troca entre simplicidade e dispersão, por isso é uma boa segunda linha de comparação.

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
   - A classe `GeradorDados` cria três conjuntos de registros de tamanhos:   - Os dados são gerados com *seed* fixa para garantir a reprodutibilidade.
   - **Três tamanhos de tabela hash:** 1.000, 10.000 e 100.000.
   - **Três funções hash:** Soma dos Dígitos, Multiplicação e Rotação Dourada.

2. **Configuração dos experimentos**  
   - A classe `ParametrosExperimento` define:
   - As funções hash implementadas são:
     - `FuncaoHashSomaDigitos` — soma dos dígitos do código.
     - `FuncaoHashMultiplicacao` — método da multiplicação.
     - `HashRotacaoDourada` — variação baseada na constante da proporção áurea.

3. **Inserção e busca nas tabelas**  
   - Cada conjunto de dados é inserido e buscado em três implementações:
     - `TabelaHashEncadeada` — resolução por **listas encadeadas**.  
     - `TabelaHashLinear` — resolução por **rehashing linear**.  
     - `TabelaHashQuadratica` — resolução por **rehashing quadrático**.  
   - Durante o processo, o sistema mede:
     - Tempo total de inserção e busca.  
     - Número de colisões.  
     - Tamanho das listas encadeadas.  
     - Estatísticas de gaps (dispersão entre elementos).

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

As métricas foram exportadas em CSV para posterior análise e geração de gráficos.

##  Exemplos dos Resultados

| Tabela Hash | Função Hash | Tamanho Vetor | Dataset | Tempo Inserção (ms) | Colisões | Tempo Busca (ms) |
|--------------|-------------|----------------|----------|----------------------|-----------|-------------------|
| Encadeada | Soma dos Dígitos | 1000 | 100000 | 24 | 5321 | 11 |
| Encadeada | Soma dos Dígitos | 10000 | 1000000 | 243 | 52103 | 107 |
| Encadeada | Soma dos Dígitos | 100000 | 10000000 | 2374 | 518990 | 1033 |
| Linear | Multiplicação | 1000 | 100000 | 19 | 3239 | 9 |
| Linear | Multiplicação | 10000 | 1000000 | 201 | 29732 | 91 |
| Linear | Multiplicação | 100000 | 10000000 | 1897 | 291044 | 877 |
| Quadrática | Rotação Dourada | 1000 | 100000 | 18 | 2288 | 8 |
| Quadrática | Rotação Dourada | 10000 | 1000000 | 182 | 21295 | 84 |
| Quadrática | Rotação Dourada | 100000 | 10000000 | 1763 | 209611 | 812 |
| Quadrática | Rotação Dourada | 100.000 | 1.000.000 | 179 | 8.874 | 89 |

---

###  **Análise de Desempenho**

A análise dos resultados evidencia padrões claros:

-  **Tabela Hash Quadrática** — Apresentou o **melhor desempenho geral**, com **menor número de colisões** e **melhores tempos de busca e inserção**, especialmente nos maiores conjuntos de dados.  
  Isso ocorre porque o *rehashing quadrático* evita o **clustering primário**, comum em abordagens lineares.

-  **Tabela Hash Linear** — Teve desempenho consistente, porém com **mais colisões** e leve **aumento nos tempos médios** conforme o tamanho dos dados cresceu.  
  É simples e eficiente, mas o *clustering* reduz sua escalabilidade.

-  **Tabela Hash Encadeada** — Teve o **pior desempenho em colisões**, pois depende da **uniformidade da função hash**.  
  Apesar disso, o impacto no tempo de busca foi menor do que o esperado, indicando boa eficiência das listas encadeadas curtas.

-  **Funções Hash** —  
  - A **Rotação Dourada** obteve a **melhor dispersão**, confirmando sua eficiência teórica.  
  - A **Multiplicação** foi intermediária, oferecendo um bom equilíbrio entre custo e dispersão.  
  - A **Soma dos Dígitos** apresentou o **pior desempenho**, com grande número de colisões e má distribuição de chaves.

 O crescimento quase linear dos tempos de execução confirma a **complexidade média O(1)** das operações de inserção e busca em todas as abordagens.

 ---

 # Gráficos de Desempenho

## Tempo de Inserção
<img width="1000" height="600" alt="tempo_insercao" src="https://github.com/user-attachments/assets/7bd61e31-3d27-4c12-847f-559eb1228f2f" />


Este gráfico mostra o tempo médio para inserir elementos na tabela hash.

- A função **Soma de Dígitos** apresenta tempos significativamente maiores, especialmente nas estratégias linear e encadeada, indicando menor eficiência.
- As funções **Multiplicação** e **Rotação Dourada** apresentaram desempenho mais consistente e rápido.

## Tempo de Busca
<img width="1000" height="600" alt="tempo_busca" src="https://github.com/user-attachments/assets/0ef2f2e1-d01f-4d24-acfd-cf06f05074a7" />


Este gráfico apresenta o tempo médio de busca por elementos nas tabelas.

- O comportamento é semelhante ao da inserção: a função **Soma de Dígitos** continua sendo a mais lenta.
- As estratégias **linear** e **quadrática** com funções mais equilibradas (**Multiplicação** e **Rotação Dourada**) mantêm tempos de busca baixos.

## Número de Colisões
<img width="1000" height="600" alt="colisoes" src="https://github.com/user-attachments/assets/6be6ffc9-449c-43fb-8a75-aff11d08b87d" />



Este gráfico mostra a quantidade total de colisões ocorridas durante as inserções.

- A função **Soma de Dígitos** novamente se destaca negativamente, gerando um número muito alto de colisões, o que explica seus tempos elevados.
- As funções **Multiplicação** e **Rotação Dourada** distribuem melhor as chaves, resultando em menos colisões e melhor desempenho geral.

## Tempo de Inserção vs Tamanho dos Dados (N)
<img width="1000" height="600" alt="tempo_insercao_vs_N" src="https://github.com/user-attachments/assets/d381001c-1141-49c8-838b-62b84bfde061" />



Este gráfico mostra a relação entre o tempo de inserção e o número de elementos (N).

- O comportamento esperado é que o tempo de inserção seja constante, O(1), em condições ideais.
- Observa-se que as **estratégias de resolução de colisão** influenciam diretamente o desempenho:
  - **Encadeamento** tende a manter um tempo de inserção mais consistente.
  - **Sondagem linear** e **quadrática** podem mostrar degradação à medida que a densidade de dados aumenta.

## Tempo de Busca vs Tamanho dos Dados (N)
<img width="1000" height="600" alt="tempo_busca_vs_N" src="https://github.com/user-attachments/assets/01f015e7-4723-4848-b6d2-ad85e124d1bf" />



Este gráfico mostra a relação entre o tempo de busca e o número de elementos armazenados.

- A busca é mais eficiente em tabelas com menor fator de carga.
- Funções hash que distribuem melhor as chaves tendem a reduzir o tempo de busca.
- O **encadeamento externo** apresenta um comportamento mais previsível em relação ao tempo de busca.

## Tempo de Busca vs Tamanho da Tabela (M)
<img width="1000" height="600" alt="tempo_busca_vs_M" src="https://github.com/user-attachments/assets/8ebb43de-a485-4241-a9c3-259ceb0ce9b0" />



Este gráfico mostra o impacto do tamanho da tabela hash (M) no tempo de busca.

- Tabelas maiores (com maior M) geralmente proporcionam tempos de busca menores.
- Existe um ponto ótimo entre o overhead de memória e o desempenho, onde a tabela é grande o suficiente para reduzir colisões, mas não excessivamente grande.
- A função hash **"RotacaoDourada"** apresenta uma escalabilidade melhor à medida que o tamanho da tabela aumenta.

## Número de Colisões vs Tamanho dos Dados (N)
<img width="1000" height="600" alt="colisoes_vs_N" src="https://github.com/user-attachments/assets/e1c8f6d8-0198-4a83-93fb-500a389c9483" />



Este gráfico mostra a quantidade total de colisões durante as inserções em função do número de dados (N).

- O **encadeamento** lida naturalmente com colisões, sem grandes degradações de desempenho.
- **Sondagem linear** sofre com o aumento de colisões à medida que o número de elementos cresce, formando aglomerados (clustering).
- **Sondagem quadrática** ajuda a reduzir o clustering, mas ainda mantém um número significativo de colisões.
- A função **"Multiplicação"** apresenta uma distribuição de chaves mais eficiente e tende a gerar menos colisões do que a **"SomaDigitos"**.

## Número de Colisões vs Tamanho da Tabela (M)
<img width="1000" height="600" alt="colisoes_vs_M" src="https://github.com/user-attachments/assets/9d179719-20ba-4aba-bb6c-606c593438ab" />



Este gráfico mostra o efeito do tamanho da tabela na quantidade de colisões.

- Tabelas maiores (M >> N) reduzem drasticamente o número de colisões, pois há mais espaço para as chaves.
- O **fator de carga (N/M)** é crucial para determinar o número de colisões: quanto menor o fator de carga, menos colisões.
- A função hash **"HashRotacaoDourada"** demonstra a melhor distribuição de chaves, independentemente do tamanho da tabela.



## Conclusão

Os experimentos comprovaram, na prática, os conceitos teóricos sobre tabelas hash:

- A escolha da **função hash** é **crítica** para o desempenho global.  
- Estratégias de **rehashing mais sofisticadas** (como o quadrático) superam métodos simples, reduzindo o número de colisões e otimizando o tempo de acesso.  
- Mesmo com grandes volumes de dados (até 1 milhões de registros), as operações permaneceram com comportamento próximo de O(1), demonstrando a **eficiência das tabelas hash bem implementadas**.  

O estudo reforça a importância de entender a **matemática e estrutura por trás das funções hash** para construir sistemas de armazenamento eficientes e escaláveis.

---
