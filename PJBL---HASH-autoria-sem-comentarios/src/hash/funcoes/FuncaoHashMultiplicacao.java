package hash.funcoes;

public class FuncaoHashMultiplicacao implements FuncaoHash{

    private static final double A = 0.6180339887;

    @Override
    public int calcular(int chave, int tamanhoTabela){
        double produto = chave * A;

        int parteInteira = (int) produto;
        double parteFracao = produto - parteInteira;

        int hash = (int) (tamanhoTabela * parteFracao);

        return hash;
    }
}
