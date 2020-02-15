/**
 * This class is part of the "Vampiro do Século XXI" application.
 * "Vampiro do Século XXI" is a very simple, text based on humor and adventure game.
 *
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 *
 * @authors Lazaro Junior, Lucas Laet e Matheus Giovanny.
 * @version 1.0 of 2019.
 */
 

public class CommandWords {

    // a constant array that holds all valid command words
    private static final String[] VALIDCOMMANDS = {
        "partiu", "sair", "acode", "observar", "voltar", "pegar", "descartar", "inventario", "interagir"
    };

    /**
     * Constructor - initialize the command words.
     */
    public CommandWords() {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word.
     *
     * @return true if a given string is a valid command, false if it isn't.
     */
    public boolean isCommand(String aString) {
        for (int i = 0; i < VALIDCOMMANDS.length; i++) {
            if (VALIDCOMMANDS[i].equals(aString)) {
                return true;
            }
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    public void allCommands() {
        System.out.print("  [");
        for (String commands : VALIDCOMMANDS) {
            System.out.print(commands + ", ");
        }
        System.out.print("]");
    }
}
