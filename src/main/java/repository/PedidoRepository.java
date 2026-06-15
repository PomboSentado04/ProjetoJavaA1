package repository;

import model.PedidoVenda;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {
    private static final String FILE_PATH = "db_pedidos.dat";

    @SuppressWarnings("unchecked")
    public List<PedidoVenda> listarTodos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<PedidoVenda>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void salvarTodos(List<PedidoVenda> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados dos Pedidos: " + e.getMessage());
        }
    }

    public void salvar(PedidoVenda pedido) {
        List<PedidoVenda> lista = listarTodos();
        lista.removeIf(p -> p.getNumeroPedido() == pedido.getNumeroPedido());
        lista.add(pedido);
        salvarTodos(lista);
        LogRepository.registrar("Salvar/Atualizar Pedido Nº: " + pedido.getNumeroPedido() + " - Cliente ID: " + pedido.getCliente().getCodigo());
    }

    public PedidoVenda buscarPorId(int numero) {
        return listarTodos().stream().filter(p -> p.getNumeroPedido() == numero).findFirst().orElse(null);
    }

    public boolean deletar(int numero) {
        List<PedidoVenda> lista = listarTodos();
        boolean removido = lista.removeIf(p -> p.getNumeroPedido() == numero);
        if (removido) {
            salvarTodos(lista);
            LogRepository.registrar("Exclusão de Pedido Nº: " + numero);
        }
        return removido;
    }
}