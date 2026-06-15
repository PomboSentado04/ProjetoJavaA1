package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pessoa implements Serializable {
    private int codigo;
    private String nome;
    private TipoPessoa tipoPessoa;
    private List<Endereco> enderecos;

    public Pessoa(int codigo, String nome, TipoPessoa tipoPessoa) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipoPessoa = tipoPessoa;
        this.enderecos = new ArrayList<>();
    }

    // Encapsulamento de comportamentos da lista
    public void adicionarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }

    // Getters e Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoPessoa getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }
    public List<Endereco> getEnderecos() { return enderecos; }
    public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nome + " (" + tipoPessoa + ") - Endereços cadastrados: " + enderecos.size();
    }
}