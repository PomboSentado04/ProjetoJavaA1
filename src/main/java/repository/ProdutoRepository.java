package repository;

import model.Produto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private static final String FILE_PATH = "db_produtos.dat";

    @SuppressWarnings("unchecked")
    public List<Produto> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Produto>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<Produto> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados dos Produtos: " + e.getMessage());
        }
    }

    public void salvar(Produto produto) {
        List<Produto> lista = listarTodos();
        lista.removeIf(p -> p.getCodigo() == produto.getCodigo());
        lista.add(produto);
        salvarTodos(lista);
        LogRepository.registrar("Salvar/Atualizar Produto ID: " + produto.getCodigo() + " - " + produto.getDescricao());
    }

    public Produto buscarPorId(int id) {
        return listarTodos().stream().filter(p -> p.getCodigo() == id).findFirst().orElse(null);
    }

    public boolean deletar(int id) {
        List<Produto> lista = listarTodos();
        boolean removido = lista.removeIf(p -> p.getCodigo() == id);
        if (removido) {
            salvarTodos(lista);
            LogRepository.registrar("Exclusão de Produto ID: " + id);
        }
        return removido;
    }
}