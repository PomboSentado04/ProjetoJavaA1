package repository;

import model.Pessoa;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepository {
    private static final String FILE_PATH = "db_pessoas.dat";

    @SuppressWarnings("unchecked")
    public List<Pessoa> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Pessoa>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<Pessoa> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados das Pessoas: " + e.getMessage());
        }
    }

    public void salvar(Pessoa pessoa) {
        List<Pessoa> lista = listarTodos();
        lista.removeIf(p -> p.getCodigo() == pessoa.getCodigo());
        lista.add(pessoa);
        salvarTodos(lista);
        LogRepository.registrar("Salvar/Atualizar Pessoa ID: " + pessoa.getCodigo() + " - Nome: " + pessoa.getNome());
    }

    public Pessoa buscarPorId(int id) {
        return listarTodos().stream().filter(p -> p.getCodigo() == id).findFirst().orElse(null);
    }

    public boolean deletar(int id) {
        List<Pessoa> lista = listarTodos();
        boolean removido = lista.removeIf(p -> p.getCodigo() == id);
        if (removido) {
            salvarTodos(lista);
            LogRepository.registrar("Exclusão de Pessoa ID: " + id);
        }
        return removido;
    }
}