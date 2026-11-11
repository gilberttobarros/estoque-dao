# Estoque DAO — Gerenciamento de Produtos em Java

## Autores

**Gilberto Barros da Silva**
**&**
**Amanda Cristina Martins Campos**

## Informações do Projeto

| | |
|---|---|
| **Aluno** | Gilberto Barros |
| **Curso** | Bacharelado em Sistemas de Informação |
| **Instituição** | Centro Universitário Santa Terezinha (CEST) |
| **Período** | 7º período |
| **Linguagem** | Java |
| **Padrão de projeto** | DAO (Data Access Object) |
| **Tipo de aplicação** | Console |

## Descrição

Aplicação em Java que gerencia uma lista de produtos em estoque com operações **CRUD** (Criar, Ler, Atualizar e Deletar). O projeto usa o padrão **DAO (Data Access Object)** para separar a lógica de negócio do acesso aos dados.

## Objetivo

Separar as operações de acesso aos dados da lógica de negócio, de modo que a **fonte de dados possa ser trocada** (por exemplo, de memória para arquivo, ou para um banco de dados relacional) **sem nenhuma alteração na camada de negócio**.

## Padrão utilizado: DAO

O padrão DAO define uma **interface** com as operações de persistência. A camada de negócio depende apenas dessa interface, e não de uma implementação concreta. Cada fonte de dados é uma classe que implementa a interface.

```
Principal
   │
   ▼
ProdutoService
   │
   ▼
ProdutoDAO (interface)
   ▲                ▲
   │                │
ProdutoDAOMemoria   ProdutoDAOArquivo
```

O `ProdutoService` recebe o DAO pelo construtor (**injeção de dependência**). Assim, quem escolhe a fonte de dados é a classe `Principal`, e o Service não muda.

## Estrutura do projeto

```
estoque-dao/
├── README.md
└── src/
    └── estoque/
        ├── Principal.java
        ├── model/
        │   └── Produto.java
        ├── dao/
        │   ├── ProdutoDAO.java
        │   ├── ProdutoDAOMemoria.java
        │   └── ProdutoDAOArquivo.java
        └── service/
            └── ProdutoService.java
```

## Camadas

| Camada | Classe | Responsabilidade |
|---|---|---|
| Modelo | `Produto` | Representa os dados de um produto (id, nome, quantidade e preço) |
| Acesso a dados | `ProdutoDAO` | Interface com `salvar`, `buscarPorId`, `listarTodos`, `atualizar` e `deletar` |
| Acesso a dados | `ProdutoDAOMemoria` | Guarda os produtos em uma lista em memória (dados temporários) |
| Acesso a dados | `ProdutoDAOArquivo` | Guarda os produtos no arquivo `produtos.csv` (dados persistentes) |
| Negócio | `ProdutoService` | Aplica as regras de negócio e delega a persistência ao DAO |
| Apresentação | `Principal` | Executa o CRUD com cada fonte de dados |

## Regras de negócio

- O id deve ser maior que zero e não pode se repetir.
- O nome é obrigatório.
- A quantidade não pode ser negativa.
- O preço deve ser maior que zero.
- Não é possível buscar, atualizar ou remover um produto inexistente.

## Troca da fonte de dados

Na classe `Principal`, a única diferença entre as duas execuções é o DAO entregue ao Service:

```java
ProdutoService service = new ProdutoService(new ProdutoDAOMemoria());
```

```java
ProdutoService service = new ProdutoService(new ProdutoDAOArquivo("produtos.csv"));
```

Para usar um banco de dados relacional no futuro, basta criar uma classe `ProdutoDAOJdbc` que implemente `ProdutoDAO` e entregá-la ao Service. **Nenhuma linha do `ProdutoService` precisa ser alterada.**

## Como executar

**Requisito:** Java 11 ou superior (JDK).

### Pela IDE

Abra a pasta do projeto no IntelliJ IDEA, Eclipse ou VS Code e execute a classe `estoque.Principal`.

### Pelo terminal

Na pasta raiz do projeto:

```bash
javac -d out src/estoque/*.java src/estoque/model/*.java src/estoque/dao/*.java src/estoque/service/*.java
java -cp out estoque.Principal
```

## Exemplo de saída

```
=========== FONTE DE DADOS: MEMÓRIA ===========

[CREATE] Cadastrando produtos...
Produto{id=1, nome='Teclado', quantidade=10, preco=R$ 150,00}
Produto{id=2, nome='Mouse', quantidade=25, preco=R$ 80,00}
Produto{id=3, nome='Monitor', quantidade=5, preco=R$ 1200,00}

[READ] Buscando o produto de id 2:
Produto{id=2, nome='Mouse', quantidade=25, preco=R$ 80,00}

[UPDATE] Atualizando quantidade e preço do Mouse...
Produto{id=2, nome='Mouse', quantidade=30, preco=R$ 75,50}

[DELETE] Removendo o produto de id 1...
Produto{id=2, nome='Mouse', quantidade=30, preco=R$ 75,50}
Produto{id=3, nome='Monitor', quantidade=5, preco=R$ 1200,00}

[REGRAS] Tentando cadastrar produto com preço negativo...
Erro: O preço deve ser maior que zero
[REGRAS] Tentando buscar produto inexistente (id 99)...
Erro: Produto com id 99 não encontrado

=========== FONTE DE DADOS: ARQUIVO (produtos.csv) ===========
```

Na fonte de arquivo, as mesmas operações são executadas com o mesmo resultado. Ao final, o arquivo `produtos.csv` fica salvo com os dados:

```
2;Mouse;30;75.5
3;Monitor;5;1200.0
```

## Tecnologias

- Java (JDK 11+)
- Java NIO (`java.nio.file`) para leitura e escrita do arquivo CSV



