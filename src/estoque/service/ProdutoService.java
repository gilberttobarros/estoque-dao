package estoque.service;

import estoque.dao.ProdutoDAO;
import estoque.model.Produto;

import java.util.List;

public class ProdutoService {

    private final ProdutoDAO dao;

    public ProdutoService(ProdutoDAO dao) {
        this.dao = dao;
    }

    public void cadastrar(Produto produto) {
        validar(produto);
        if (dao.buscarPorId(produto.getId()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com o id " + produto.getId());
        }
        dao.salvar(produto);
    }

    public Produto buscar(int id) {
        return dao.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto com id " + id + " não encontrado"));
    }

    public List<Produto> listar() {
        return dao.listarTodos();
    }

    public void atualizar(Produto produto) {
        validar(produto);
        buscar(produto.getId());
        dao.atualizar(produto);
    }

    public void remover(int id) {
        buscar(id);
        dao.deletar(id);
    }

    private void validar(Produto produto) {
        if (produto.getId() <= 0) {
            throw new IllegalArgumentException("O id deve ser maior que zero");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }
        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa");
        }
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero");
        }
    }
}
