package app;

import model.*;
import repository.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PessoaRepository pessoaRepo = new PessoaRepository();
    private static final ProdutoRepository produtoRepo = new ProdutoRepository();
    private static final PedidoRepository pedidoRepo = new PedidoRepository();

    public static void main(String[] args) {
        executarMenuPrincipal();
    }

    private static void exibirTrechoMenu(int blocoInicio, int blocoFim) {
        try (BufferedReader br = new BufferedReader(new FileReader("menus.txt"))) {
            String linha;
            int contadorLinha = 1;
            while ((linha = br.readLine()) != null) {
                if (contadorLinha >= blocoInicio && contadorLinha <= blocoFim) {
                    System.out.println(linha);
                }
                contadorLinha++;
            }
        } catch (IOException e) {
            System.out.println("Erro crítico: Arquivo 'menus.txt' não foi encontrado na raiz.");
        }
    }

    private static void executarMenuPrincipal() {
        int opcao;
        do {
            exibirTrechoMenu(1, 6);
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();
            switch (opcao) {
                case 1 -> executarSubmenuPessoas();
                case 2 -> executarSubmenuProdutos();
                case 3 -> executarSubmenuPedidos();
                case 0 -> System.out.println("Sistema finalizado.");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void executarSubmenuPessoas() {
        int opcao;
        do {
            exibirTrechoMenu(7, 13);
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();
            switch (opcao) {
                case 1 -> cadastrarPessoa();
                case 2 -> listarPessoas();
                case 3 -> excluirPessoa();
                case 4 -> gerenciarEnderecos();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarPessoa() {
        System.out.print("Digite o código da Pessoa: ");
        int codigo = lerInteiro();
        System.out.print("Digite o nome da Pessoa: ");
        String nome = scanner.nextLine();
        System.out.print("Tipo (1-Cliente, 2-Fornecedor, 3-Ambos): ");
        int tipoInt = lerInteiro();
        TipoPessoa tipo = switch (tipoInt) {
            case 2 -> TipoPessoa.FORNECEDOR;
            case 3 -> TipoPessoa.AMBOS;
            default -> TipoPessoa.CLIENTE;
        };

        Pessoa exist = pessoaRepo.buscarPorId(codigo);
        Pessoa p = (exist != null) ? exist : new Pessoa(codigo, nome, tipo);
        p.setNome(nome);
        p.setTipoPessoa(tipo);

        pessoaRepo.salvar(p);
        System.out.println("Pessoa salva com sucesso!");
    }

    private static void listarPessoas() {
        List<Pessoa> lista = pessoaRepo.listarTodos();
        if (lista.isEmpty()) System.out.println("Nenhuma pessoa cadastrada.");
        else lista.forEach(System.out::println);
    }

    private static void excluirPessoa() {
        System.out.print("Código da pessoa a excluir: ");
        int id = lerInteiro();
        if (pessoaRepo.deletar(id)) System.out.println("Pessoa removida.");
        else System.out.println("Pessoa não encontrada.");
    }

    private static void gerenciarEnderecos() {
        System.out.print("Código da pessoa para gerenciar endereços: ");
        int id = lerInteiro();
        Pessoa p = pessoaRepo.buscarPorId(id);
        if (p == null) {
            System.out.println("Pessoa não encontrada.");
            return;
        }

        System.out.println("Endereços atuais de " + p.getNome() + ":");
        for (int i = 0; i < p.getEnderecos().size(); i++) {
            System.out.println(i + " - " + p.getEnderecos().get(i));
        }

        System.out.print("Deseja (1) Adicionar novo ou (2) Remover algum? Outro valor para voltar: ");
        int acao = lerInteiro();
        if (acao == 1) {
            System.out.print("CEP: "); String cep = scanner.nextLine();
            System.out.print("Logradouro: "); String logr = scanner.nextLine();
            System.out.print("Número: "); String num = scanner.nextLine();
            System.out.print("Complemento: "); String comp = scanner.nextLine();
            System.out.print("Tipo de Endereço (Ex: Comercial, Entrega): "); String tipoEnd = scanner.nextLine();

            p.adicionarEndereco(new Endereco(cep, logr, num, comp, tipoEnd));
            pessoaRepo.salvar(p);
            System.out.println("Endereço adicionado!");
        } else if (acao == 2) {
            System.out.print("Digite o índice do endereço a remover: ");
            int idx = lerInteiro();
            if (idx >= 0 && idx < p.getEnderecos().size()) {
                p.getEnderecos().remove(idx);
                pessoaRepo.salvar(p);
                System.out.println("Endereço removido.");
            } else {
                System.out.println("Índice inválido.");
            }
        }
    }

    private static void executarSubmenuProdutos() {
        int opcao;
        do {
            exibirTrechoMenu(14, 19);
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();
            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> excluirProduto();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarProduto() {
        System.out.print("Código do Produto: ");
        int cod = lerInteiro();
        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        System.out.print("Custo (R$): ");
        double custo = lerDouble();
        System.out.print("Preço de Venda (R$): ");
        double preco = lerDouble();
        System.out.print("Código do Fornecedor: ");
        int codForn = lerInteiro();

        Pessoa fornecedor = pessoaRepo.buscarPorId(codForn);
        if (fornecedor == null || fornecedor.getTipoPessoa() == TipoPessoa.CLIENTE) {
            System.out.println("Aviso: Fornecedor não cadastrado ou não configurado como Fornecedor. Cadastro não permitido.");
            return;
        }

        Produto prod = new Produto(cod, desc, custo, preco, codForn);
        produtoRepo.salvar(prod);
        System.out.println("Produto salvo com sucesso!");
    }

    private static void listarProdutos() {
        List<Produto> lista = produtoRepo.listarTodos();
        if (lista.isEmpty()) System.out.println("Nenhum produto cadastrado.");
        else lista.forEach(System.out::println);
    }

    private static void excluirProduto() {
        System.out.print("Código do produto a remover: ");
        int id = lerInteiro();
        if (produtoRepo.deletar(id)) System.out.println("Produto removido.");
        else System.out.println("Produto não encontrado.");
    }

    private static void executarSubmenuPedidos() {
        int opcao;
        do {
            exibirTrechoMenu(20, 25);
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();
            switch (opcao) {
                case 1 -> criarPedido();
                case 2 -> listarPedidos();
                case 3 -> excluirPedido();
                case 0 -> {}
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void criarPedido() {
        System.out.print("Número do Pedido: ");
        int numPed = lerInteiro();
        System.out.print("Código do Cliente: ");
        int codCli = lerInteiro();
        Pessoa cliente = pessoaRepo.buscarPorId(codCli);

        if (cliente == null) {
            System.out.println("Cliente não encontrado! Cadastre o cliente primeiro.");
            return;
        }

        if (cliente.getEnderecos().isEmpty()) {
            System.out.println("Este cliente não possui endereços cadastrados. Adicione um endereço antes.");
            return;
        }

        System.out.println("Selecione o Endereço de Entrega:");
        for (int i = 0; i < cliente.getEnderecos().size(); i++) {
            System.out.println(i + " - " + cliente.getEnderecos().get(i));
        }
        int idxEnd = lerInteiro();
        if (idxEnd < 0 || idxEnd >= cliente.getEnderecos().size()) {
            System.out.println("Índice de endereço inválido.");
            return;
        }
        Endereco entrega = cliente.getEnderecos().get(idxEnd);

        PedidoVenda pedido = new PedidoVenda(numPed, cliente, entrega);

        char adicionarMais;
        do {
            System.out.print("Código do Produto a adicionar: ");
            int codProd = lerInteiro();
            Produto prod = produtoRepo.buscarPorId(codProd);
            if (prod != null) {
                pedido.adicionarProduto(prod);
                System.out.println("'" + prod.getDescricao() + "' adicionado ao pedido.");
            } else {
                System.out.println("Produto não encontrado.");
            }
            System.out.print("Deseja adicionar mais produtos? (S/N): ");
            adicionarMais = scanner.nextLine().trim().toUpperCase().charAt(0);
        } while (adicionarMais == 'S');

        pedidoRepo.salvar(pedido);
        System.out.println("Pedido salvo! Valor Total: R$ " + String.format("%.2f", pedido.getMontanteTotal()));
    }

    private static void listarPedidos() {
        List<PedidoVenda> lista = pedidoRepo.listarTodos();
        if (lista.isEmpty()) System.out.println("Nenhum pedido registrado.");
        else {
            for (PedidoVenda ped : lista) {
                System.out.println("\n=================================");
                System.out.println(ped);
                System.out.println("Entregar em: " + ped.getEnderecoEntrega());
                System.out.println("Produtos:");
                ped.getProdutos().forEach(p -> System.out.println("  - " + p.getDescricao() + " (R$" + p.getPrecoVenda() + ")"));
                System.out.println("=================================");
            }
        }
    }

    private static void excluirPedido() {
        System.out.print("Número do pedido a excluir: ");
        int num = lerInteiro();
        if (pedidoRepo.deletar(num)) System.out.println("Pedido cancelado/excluído.");
        else System.out.println("Pedido não encontrado.");
    }

    private static int lerInteiro() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                double valor = Double.parseDouble(scanner.nextLine().replace(",", "."));
                return valor;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um valor numérico decimal: ");
            }
        }
    }
}