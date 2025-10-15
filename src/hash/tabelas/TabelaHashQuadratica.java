package hash.tabelas;

import medidas.ContadoresMetrica;
import hash.funcoes.FuncaoHash;

public class TabelaHashQuadratica implements TabelaHash{
    private int[] tabela;
    private boolean[] ocupado;
    private boolean[] removido;
    private final int tamanho;
    private final FuncaoHash funcaoHash;
    private int totalElementos;

    public TabelaHashQuadratica(int tamanho, FuncaoHash funcaoHash){
        this.tamanho = tamanho;
        this.funcaoHash = funcaoHash;
        this.tabela = new int[tamanho];
        this.ocupado = new boolean[tamanho];
        this.removido = new boolean[tamanho];
        this.totalElementos = 0;
    }

    @Override
    public void inserir(int chave, ContadoresMetrica metrica){
        if(totalElementos >= tamanho){
            throw new RuntimeException("Tabela Hash esta cheia");
        }
        int hashInicial = funcaoHash.calcular(chave, tamanho);
        int indice = hashInicial;
        int i = 0;

        //sondagem quadratica h(k) + i*i
        while(i < tamanho){
            indice = (hashInicial + i*i) % tamanho;

            if(!ocupado[tamanho] || removido[tamanho]){
                tabela[indice] = chave;
                ocupado[indice] = true;
                removido[indice] = false;
                totalElementos++;
                return;
            }

            if(tabela[indice] == chave){
                return;
            }

            metrica.incrementarColisaoInsercao();
            i++;
        }
        throw new RuntimeException("Nao foi possivel fazer a insercao");
    }

    @Override
    public boolean buscar(int chave, ContadoresMetrica metrica){
        int hashInicial = funcaoHash.calcular(chave, tamanho);
        int indice = hashInicial;
        int i = 0;
        int passos = 0;

        while(i < tamanho){
            indice = (hashInicial + i*i) % tamanho;
            passos++;

            if(ocupado[indice] && !removido[indice] && tabela[indice] == chave){
                metrica.adicionarPassosBusca(passos);
                return true;
            }

            if(!ocupado[indice]){
                metrica.adicionarPassosBusca(passos);
                return false;
            }

            i++;
        }
        metrica.adicionarPassosBusca(passos);
        return false;
    }

    @Override
    public void limpar(){
        for(int i = 0; i < tamanho; i++){
            ocupado[i] = false;
            removido[i] = false;
            tabela[i] = 0;
        }
        totalElementos = 0;
    }

}
