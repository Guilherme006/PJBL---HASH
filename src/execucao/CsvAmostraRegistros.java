package execucao;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * CSV simples para gravar uma amostra de registros por combinação.
 * Cabeçalho: M,N,estrategia,funcao,ordem,registro_9_digitos
 */
public final class CsvAmostraRegistros implements AutoCloseable {

    private final String caminhoArquivo;
    private BufferedWriter escritor;

    public CsvAmostraRegistros(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void abrir() throws IOException {
        this.escritor = Files.newBufferedWriter(
                Paths.get(caminhoArquivo),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
        escritor.write("M,N,estrategia,funcao,ordem,registro_9_digitos");
        escritor.newLine();
    }

    public void escreverLinha(
            int tamanhoTabela, int quantidadeDados,
            String nomeEstrategia, String nomeFuncao,
            int ordemNaAmostra, String registroFormatado
    ) throws IOException {
        StringBuilder sb = new StringBuilder(96);
        sb.append(tamanhoTabela).append(',')
                .append(quantidadeDados).append(',')
                .append(escapar(nomeEstrategia)).append(',')
                .append(escapar(nomeFuncao)).append(',')
                .append(ordemNaAmostra).append(',')
                .append(registroFormatado);
        escritor.write(sb.toString());
        escritor.newLine();
    }

    @Override
    public void close() throws IOException {
        if (escritor != null) {
            escritor.flush();
            escritor.close();
            escritor = null;
        }
    }

    private static String escapar(String t) {
        if (t == null) return "";
        boolean precisa = t.indexOf(',')>=0 || t.indexOf('"')>=0 || t.indexOf('\n')>=0 || t.indexOf('\r')>=0;
        if (!precisa) return t;
        return "\"" + t.replace("\"","\"\"") + "\"";
    }
}
