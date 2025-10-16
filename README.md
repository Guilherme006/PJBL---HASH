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
