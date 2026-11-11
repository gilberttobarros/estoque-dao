package estoque.dao;

import estoque.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoDAO {

    void salvar(Produto produto);

    Optional<Produto> buscarPorId(int id);

    List<Produto> listarTodos();

    void atualizar(Produto produto);

    void deletar(int id);
}
