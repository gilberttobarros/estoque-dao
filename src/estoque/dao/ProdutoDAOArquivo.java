package estoque.dao;

import estoque.model.Produto;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOArquivo implements ProdutoDAO {

    private static final String SEPARADOR = ";";
    private final Path arquivo;

    public ProdutoDAOArquivo(String caminhoArquivo) {
        this.arquivo = Path.of(caminhoArquivo);
        try {
            if (Files.notExists(arquivo)) {
                Files.createFile(arquivo);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível criar o arquivo " + caminhoArquivo, e);
        }
    }

    @Override
    public void salvar(Produto produto) {
        List<Produto> produtos = lerArquivo();
        produtos.add(produto);
        gravarArquivo(produtos);
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        return lerArquivo().stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<Produto> listarTodos() {
        return lerArquivo();
    }

    @Override
    public void atualizar(Produto produto) {
        List<Produto> produtos = lerArquivo();
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produto.getId()) {
                produtos.set(i, produto);
                break;
            }
        }
        gravarArquivo(produtos);
    }

    @Override
    public void deletar(int id) {
        List<Produto> produtos = lerArquivo();
        produtos.removeIf(p -> p.getId() == id);
        gravarArquivo(produtos);
    }

    private List<Produto> lerArquivo() {
        try {
            List<Produto> produtos = new ArrayList<>();
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                if (linha.isBlank()) continue;
                String[] campos = linha.split(SEPARADOR);
                produtos.add(new Produto(
                        Integer.parseInt(campos[0]),
                        campos[1],
                        Integer.parseInt(campos[2]),
                        Double.parseDouble(campos[3])
                ));
            }
            return produtos;
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao ler o arquivo " + arquivo, e);
        }
    }

    private void gravarArquivo(List<Produto> produtos) {
        List<String> linhas = new ArrayList<>();
        for (Produto p : produtos) {
            linhas.add(p.getId() + SEPARADOR + p.getNome() + SEPARADOR
                    + p.getQuantidade() + SEPARADOR + p.getPreco());
        }
        try {
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao gravar o arquivo " + arquivo, e);
        }
    }
}
