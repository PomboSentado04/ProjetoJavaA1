package repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogRepository {
    private static final String FILE_PATH = "log_operacoes.txt";

    public static void registrar(String acao) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            bw.write("[" + timestamp + "] " + acao);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
}