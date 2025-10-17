import pandas as pd
import matplotlib.pyplot as plt
import os

df_metricas = pd.read_csv("resultados_hash.csv")

os.makedirs("hashdef", exist_ok=True)

def grafico_barras_matplotlib(df, y_col, titulo, arquivo, logscale=False):
    estrategias = df['estrategia'].unique()
    funcoes = df['funcao'].unique()
    
    x = range(len(estrategias))
    width = 0.2
    
    plt.figure(figsize=(10,6))
    
    for i, func in enumerate(funcoes):
        valores = []
        for est in estrategias:
            subset = df[(df['estrategia']==est) & (df['funcao']==func)]
            if not subset.empty:
                valores.append(subset[y_col].values[0])
            else:
                valores.append(0)
        plt.bar([p + i*width for p in x], valores, width=width, label=func)
    
    plt.title(titulo)
    plt.xlabel("Estratégia")
    plt.ylabel(y_col.replace("_"," ").capitalize())
    plt.xticks([p + width for p in x], estrategias)
    if logscale:
        plt.yscale("log")
    plt.legend(title="Função Hash")
    plt.tight_layout()
    plt.savefig(f"hashdef/{arquivo}.png")
    plt.show()

def scatter_tamanho_vs_metricas(df, x_col, y_col, titulo, arquivo, logscale_y=True):
    plt.figure(figsize=(10,6))
    
    estrategias = df['estrategia'].unique()
    funcoes = df['funcao'].unique()
    
    for est in estrategias:
        for func in funcoes:
            subset = df[(df['estrategia']==est) & (df['funcao']==func)]
            plt.scatter(subset[x_col], subset[y_col], s=50, label=f"{est} / {func}")
    
    plt.xlabel(x_col)
    plt.ylabel(y_col.replace("_"," ").capitalize())
    plt.title(titulo)
    if logscale_y:
        plt.yscale("log")
    plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.tight_layout()
    plt.savefig(f"hashdef/{arquivo}.png")
    plt.show()

grafico_barras_matplotlib(df_metricas, 'tempo_insercao_ms', 
                          "Tempo de Inserção por Estratégia e Função Hash", 
                          "tempo_insercao", logscale=True)

grafico_barras_matplotlib(df_metricas, 'tempo_busca_ms', 
                          "Tempo de Busca por Estratégia e Função Hash", 
                          "tempo_busca", logscale=True)

grafico_barras_matplotlib(df_metricas, 'colisoes', 
                          "Número de Colisões por Estratégia e Função Hash", 
                          "colisoes", logscale=True)

# Tempo de inserção vs tamanho da tabela
scatter_tamanho_vs_metricas(df_metricas, 'M', 'tempo_insercao_ms',
                            "Tempo de Inserção vs Tamanho da Tabela (M)",
                            "tempo_insercao_vs_M")

# Tempo de busca vs tamanho da tabela
scatter_tamanho_vs_metricas(df_metricas, 'M', 'tempo_busca_ms',
                            "Tempo de Busca vs Tamanho da Tabela (M)",
                            "tempo_busca_vs_M")

# Colisões vs tamanho da tabela
scatter_tamanho_vs_metricas(df_metricas, 'M', 'colisoes',
                            "Número de Colisões vs Tamanho da Tabela (M)",
                            "colisoes_vs_M")

# Tempo de inserção vs tamanho dos dados
scatter_tamanho_vs_metricas(df_metricas, 'N', 'tempo_insercao_ms',
                            "Tempo de Inserção vs Tamanho dos Dados (N)",
                            "tempo_insercao_vs_N")

# Tempo de busca vs tamanho dos dados
scatter_tamanho_vs_metricas(df_metricas, 'N', 'tempo_busca_ms',
                            "Tempo de Busca vs Tamanho dos Dados (N)",
                            "tempo_busca_vs_N")

# Colisões vs tamanho dos dados
scatter_tamanho_vs_metricas(df_metricas, 'N', 'colisoes',
                            "Número de Colisões vs Tamanho dos Dados (N)",
                            "colisoes_vs_N")
