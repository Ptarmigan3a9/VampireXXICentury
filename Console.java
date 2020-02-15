/**
 * Console class have the purpose to create a delay in words typing of the game code.
 *
 * @authors Lazaro Junior, Lucas Laet and Matheus Giovanny.
 * @version 1.0 of 2019.
 */
 

import java.util.concurrent.TimeUnit;

public class Console {

    public Console() {
    }

    /**
     *
     * @param textToByWritten
     * @throws Exception time delay by Time Unit lib
     */
    public void writeText(String textToByWritten) throws Exception {
        for (Character textChar : textToByWritten.toCharArray()) {
            System.out.print(textChar);
            TimeUnit.MILLISECONDS.sleep(100);
        }
        System.out.println();
    }
}
