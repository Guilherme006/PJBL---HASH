package estruturas;

public class ListaEncadeada {
    private No cabeca;

    public ListaEncadeada(){
        this.cabeca = null;
    }

    // Insere um novo elemento no início da lista
    public void insereInicio(int x) {
        No n = new No(x); //Cria um novo nó com o valor de x
        n.setProx(cabeca); //Novo nó aponta para a cabeça
        cabeca = n; //Novo nó vira a cabeça
    }

    //Insere um novo elemento no final da lista
    public void insereFinal(int x) {
        No n = new No(x);
        //Se a lista estiver vazia, o novo nó vira a cabeça
        if (cabeca == null) {
            cabeca = n;
        } else {
            //Caso contrário percorre a lista até o último nó
            No atual = cabeca;
            while (atual.getProx() != null) {
                atual = atual.getProx();
            }
            //Ùltimo nó aponta para o novo nó
            atual.setProx(n);
        }
    }

    //Imprime todos os elementos da lista na tela
    public void imprime() {
        for (No atual = cabeca; atual != null; atual = atual.getProx()) {
            System.out.print(atual.getInfo() + " -> ");
        }
        System.out.println("null");
    }

    //Remove o nó que contém o valor x
    public boolean remove(int x) {
        //Retorna falso se a lista estiver vazia
        if (cabeca == null) return false;

        //Se o elemento a ser removido for o primeiro da lista, o próximo nó vira a cabeça
        if (cabeca.getInfo() == x) {
            cabeca = cabeca.getProx();
            return true;
        }

        //Percorre a lista até achar o elemento
        No atual = cabeca;
        while (atual.getProx() != null) {
            if (atual.getProx().getInfo() == x) {
                //Remove o nó fazendo com que nó anterior a x aponte para o próximo de x
                atual.setProx(atual.getProx().getProx());
                return true;
            }
            atual = atual.getProx();
        }
        //Retorna falso se o elemento não for encontrado
        return false;
    }

    //Busca um elemento na lista
    public boolean buscar(int x) {
        //Cria um nó para percorrer a lista
        No atual = cabeca;
        while (atual != null) {
            //Se o valor for encontrado, retorna verdadeiro
            if (atual.getInfo() == x) {
                return true;
            }
            atual = atual.getProx();
        }
        //Se o valor não for encontrado, retorna falso
        return false;
    }

    //Retorna o número de elementos da lista
    public int tamanho() {
        int tam = 0;
        No atual = cabeca;
        while (atual != null) {
            tam++;
            atual = atual.getProx();
        }
        return tam;
    }

    //Remove todos os elementos da lista, deixando ela vazia
    public void limpar() {
        cabeca = null;
    }
}
