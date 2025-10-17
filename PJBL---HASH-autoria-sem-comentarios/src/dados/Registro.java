package dados;
public final class Registro {

    private static final int LIMITE_SUPERIOR_EXCLUSIVO = 1_000_000_000;

    private final int codigo;

    private final boolean codigoFoiNormalizado;

    public Registro(int codigoEntrada) {
        int normalizado = Math.floorMod(codigoEntrada, LIMITE_SUPERIOR_EXCLUSIVO);
        this.codigo = normalizado;
        this.codigoFoiNormalizado = (codigoEntrada != normalizado);
    }

    public int getCodigo() {
        return codigo;
    }

    public boolean isCodigoFoiNormalizado() {
        return codigoFoiNormalizado;
    }

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
