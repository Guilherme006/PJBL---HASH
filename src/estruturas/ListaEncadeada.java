package estruturas;

public class ListaEncadeada {
    private No cabeca;

    public ListaEncadeada(){
        this.cabeca = null;
    }

    public void insereInicio(int x) {
        No n = new No(x);
        n.setProx(cabeca);
        cabeca = n;
    }

    public void insereFinal(int x) {
        No n = new No(x);
        if (cabeca == null) {
            cabeca = n;
        } else {
            No atual = cabeca;
            while (atual.getProx() != null) {
                atual = atual.getProx();
            }
            atual.setProx(n);
        }
    }

    public void imprime() {
        for (No atual = cabeca; atual != null; atual = atual.getProx()) {
            System.out.print(atual.getInfo() + " -> ");
        }
        System.out.println("null");
    }

    public boolean remove(int x) {
        if (cabeca == null) return false;

        if (cabeca.getInfo() == x) {
            cabeca = cabeca.getProx();
            return true;
        }

        No atual = cabeca;
        while (atual.getProx() != null) {
            if (atual.getProx().getInfo() == x) {
                atual.setProx(atual.getProx().getProx());
                return true;
            }
            atual = atual.getProx();
        }
        return false;
    }

    public boolean buscar(int x) {
        No atual = cabeca;
        while (atual != null) {
            if (atual.getInfo() == x) {
                return true;
            }
            atual = atual.getProx();
        }
        return false;
    }

    public int tamanho() {
        int tam = 0;
        No atual = cabeca;
        while (atual != null) {
            tam++;
            atual = atual.getProx();
        }
        return tam;
    }

    public void limpar() {
        cabeca = null;
    }
}
