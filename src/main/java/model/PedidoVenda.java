package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PedidoVenda implements Serializable {
    private int numeroPedido;
    private Pessoa cliente;
    private Endereco enderecoEntrega;
    private List<Produto> produtos;
    private double montanteTotal;

    public PedidoVenda(int numeroPedido, Pessoa cliente, Endereco enderecoEntrega) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
        this.produtos = new ArrayList<>();
        this.montanteTotal = 0.0;
    }

    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
        calcularMontante();
    }

    private void calcularMontante() {
        this.montanteTotal = this.produtos.stream().mapToDouble(Produto::getPrecoVenda).sum();
    }

    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }
    public Pessoa getCliente() { return cliente; }
    public void setCliente(Pessoa cliente) { this.cliente = cliente; }
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(Endereco enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public List<Produto> getProdutos() { return produtos; }
    public double getMontanteTotal() { return montanteTotal; }

    @Override
    public String toString() {
        return "Pedido Nº: " + numeroPedido + " | Cliente: " + cliente.getNome() + " | Total: R$" + String.format("%.2f", montanteTotal);
    }
}