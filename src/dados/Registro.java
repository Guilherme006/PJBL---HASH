package dados;

/**
 * Representa um elemento contendo apenas o "código do registro"
 * (nove dígitos, 0..999_999_999).
 *
 * Não lança exceções: se o valor de entrada estiver fora do intervalo,
 * ele é NORMALIZADO via Math.floorMod(codigo, 1_000_000_000).
 * Isso garante sempre um número de 9 dígitos sem interromper a execução.
 */
public final class Registro {

    /** Limite superior exclusivo para manter 9 dígitos (0..999_999_999). */
    private static final int LIMITE_SUPERIOR_EXCLUSIVO = 1_000_000_000;

    /** Valor inteiro do código (sempre normalizado para 0..999_999_999). */
    private final int codigo;

    /** Indica se o valor original precisou ser normalizado. */
    private final boolean codigoFoiNormalizado;

    /**
     * Cria um registro garantindo 9 dígitos por normalização modular.
     * @param codigoEntrada valor inteiro do código (qualquer int)
     * será convertido para [0, 999_999_999]
     */
    public Registro(int codigoEntrada) {
        int normalizado = Math.floorMod(codigoEntrada, LIMITE_SUPERIOR_EXCLUSIVO);
        this.codigo = normalizado;
        this.codigoFoiNormalizado = (codigoEntrada != normalizado);
    }

    /** @return o valor inteiro do código (0..999_999_999). */
    public int getCodigo() {
        return codigo;
    }

    /** @return true se o valor de entrada foi ajustado para caber em 9 dígitos. */
    public boolean isCodigoFoiNormalizado() {
        return codigoFoiNormalizado;
    }

    /** @return o código formatado com nove dígitos (zeros à esquerda). */
    public String getCodigoComoNoveDigitos() {
        return String.format("%09d", codigo);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Registro)) return false;
        Registro outro = (Registro) objeto;
        return this.codigo == outro.codigo;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigo);
    }

    @Override
    public String toString() {
        return getCodigoComoNoveDigitos();
    }
}
