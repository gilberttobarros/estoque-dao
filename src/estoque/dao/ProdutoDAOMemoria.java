package estoque.dao;

import estoque.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAOMemoria implements ProdutoDAO {

    private final List<Produto> produtos = new ArrayList<>();

    @Override
    public void salvar(Produto produto) {
        produtos.add(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(int id) {
        return produtos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    @Override
    public void atualizar(Produto produto) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produto.getId()) {
                produtos.set(i, produto);
                return;
            }
        }
    }

    @Override
    public void deletar(int id) {
        produtos.removeIf(p -> p.getId() == id);
    }
}
