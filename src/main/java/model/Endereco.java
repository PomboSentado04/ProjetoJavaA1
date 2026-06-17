package model;

import java.io.Serializable;

public class Endereco implements Serializable {
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String tipoEndereco;

    public Endereco(String cep, String logradouro, String numero, String complemento, String tipoEndereco) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.tipoEndereco = tipoEndereco;
    }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getTipoEndereco() { return tipoEndereco; }
    public void setTipoEndereco(String tipoEndereco) { this.tipoEndereco = tipoEndereco; }

    @Override
    public String toString() {
        return tipoEndereco + " -> " + logradouro + ", Nº " + numero + " (" + complemento + ") - CEP: " + cep;
    }
}