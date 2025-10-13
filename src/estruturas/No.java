package estruturas;

public class No {
    private int info;
    private No prox;

    public No(int info){
        this.info = info;
        this.prox = null;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }
}
