package estoque;

import estoque.dao.ProdutoDAO;
import estoque.dao.ProdutoDAOArquivo;
import estoque.dao.ProdutoDAOMemoria;
import estoque.model.Produto;
import estoque.service.ProdutoService;

public class Principal {

    public static void main(String[] args) {

        System.out.println("=========== FONTE DE DADOS: MEMÓRIA ===========");
        ProdutoDAO daoMemoria = new ProdutoDAOMemoria();
        executarCrud(new ProdutoService(daoMemoria));

        System.out.println();
        System.out.println("=========== FONTE DE DADOS: ARQUIVO (produtos.csv) ===========");
        ProdutoDAO daoArquivo = new ProdutoDAOArquivo("produtos.csv");
        limparArquivo(daoArquivo);
        executarCrud(new ProdutoService(daoArquivo));
    }

    private static void executarCrud(ProdutoService service) {

        System.out.println("\n[CREATE] Cadastrando produtos...");
        service.cadastrar(new Produto(1, "Teclado", 10, 150.00));
        service.cadastrar(new Produto(2, "Mouse", 25, 80.00));
        service.cadastrar(new Produto(3, "Monitor", 5, 1200.00));
        service.listar().forEach(System.out::println);

        System.out.println("\n[READ] Buscando o produto de id 2:");
        System.out.println(service.buscar(2));

        System.out.println("\n[UPDATE] Atualizando quantidade e preço do Mouse...");
        Produto mouse = service.buscar(2);
        mouse.setQuantidade(30);
        mouse.setPreco(75.50);
        service.atualizar(mouse);
        System.out.println(service.buscar(2));

        System.out.println("\n[DELETE] Removendo o produto de id 1...");
        service.remover(1);
        service.listar().forEach(System.out::println);

        System.out.println("\n[REGRAS] Tentando cadastrar produto com preço negativo...");
        try {
            service.cadastrar(new Produto(4, "Cabo HDMI", 10, -5.00));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("[REGRAS] Tentando buscar produto inexistente (id 99)...");
        try {
            service.buscar(99);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void limparArquivo(ProdutoDAO dao) {
        dao.listarTodos().forEach(p -> dao.deletar(p.getId()));
    }
}
