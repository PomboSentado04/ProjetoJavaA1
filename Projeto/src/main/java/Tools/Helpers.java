package Tools;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Helpers {

    private static final Logger LOGGER = Logger.getLogger(Helpers.class.getName());

    public static void LimparTerminal() {
        try {
            ProcessBuilder builder;
            if (System.getProperty("os.name").contains("Windows")) {
                builder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                builder = new ProcessBuilder("clear");
            }

            try {
                Process process = builder.inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao limpar o console", e);
        }
    }
}
