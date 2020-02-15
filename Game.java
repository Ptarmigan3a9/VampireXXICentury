/**
 * @authors Lazaro Junior, Lucas Laet and Matheus Giovanny.
 * @version 1.0 of 2019.
 */
 

import java.util.Random;
import java.util.*;

public class Game {

    private Parser parser;
    private Room currentRoom;
    private Missions missions;
    private Stack<Room> previousRooms;
    private Player player;
    private Console typer;
    private DigitalVersatileDisc disc;

    /**
     * Create the game and initialize its internal map.
     */
    public Game() {
        typer = new Console();
        parser = new Parser();
        missions = new Missions();
        previousRooms = new Stack<>();
        createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room casteloVilaEsperanca, butecoDoVladmilson, supermercadosHB, postoLipiranga, ifmg, cenarioAleatorio;

        // create the rooms
        casteloVilaEsperanca = new Room("Castelo Vila Esperanca", "Um castelo feito de tijolo e reboco.",
                "O ambiente é escuro...\nPois você não pagou a Semig. A pouca iluminação parece estar vindo de um computador.");
        butecoDoVladmilson = new Room("Buteco do Vladmilson", "Um buteco onde os demais seres sobrenaturais se reúnem.",
                "O local está vazio, no balcão está o Lambisame preparando um drinky e ao fundo o Etê Bilu bebendo uma dinamite pangaláctica.");
        supermercadosHB = new Room("Supermercados Hb", "Um supermercado que tem muito além do que uma picanha sangrando.",
                "É de seu acesso uma ala para contrato com servos e outros itens macabros\nO que se confunde com uma loja física da Deep Web.");
        postoLipiranga = new Room("Posto Lipiranga", "Um posto comum, onde o preço da gasolina é mais assustador que qualquer monstro.",
                "Você observa que no local há apenas um frentista\nMas segundo os boatos\nPor aqui, há tudo que lhe falta.");
        ifmg = new Room("IFMG Sabará", "Uma faculdade onde se encontram os alunos de BSI - Bebedores de Softwares da Informação.",
                "Na entrada do local\nExiste uma fera negra SUPER perigosa\nSeja cuidadoso e não entre comendo um salgado.");
        cenarioAleatorio = new Room("Trecho de estrada", "Um trecho da MGT 262 sem contexto e com muito mato.",
                "Pedaço da via em construção\nAssim como a utilidade deste cenário.");

        //Create a rooms references: direction and exits names
        casteloVilaEsperanca.setExit("sul", supermercadosHB);
        casteloVilaEsperanca.setExit("leste", butecoDoVladmilson);
        casteloVilaEsperanca.setExit("oeste", postoLipiranga);
        butecoDoVladmilson.setExit("oeste", casteloVilaEsperanca);
        supermercadosHB.setExit("norte", casteloVilaEsperanca);
        postoLipiranga.setExit("leste", casteloVilaEsperanca);
        postoLipiranga.setExit("oeste", cenarioAleatorio);
        ifmg.setExit("leste", cenarioAleatorio);
        cenarioAleatorio.setExit("oeste", ifmg);
        cenarioAleatorio.setExit("leste", postoLipiranga);

        //Create the rooms items of game.
        casteloVilaEsperanca.getRoomInventory().addNewItem("Jarvis",
                "(Computador) Um pequeno presente da Kobum. Sintel Core i9, 16gb de RAM, 220 Cv de potência.", 950.1f, false, true);
        supermercadosHB.getRoomInventory().addNewItem("Mordomo",
                "Um modormo clichê de filmes de vampiro.\n"
                + "Rapaz... é cada coisa que a gente vê por aqui.",
                200f, false, true);
        butecoDoVladmilson.getRoomInventory().addNewItem("Drinky Bloody Mary",
                "Bebida clássica do Buteco do Vladmilson. Pode ser que sóbrio você não consiga ver o que precisa.",
                1f, true, false);
        postoLipiranga.getRoomInventory().addNewItem("Frentista",
                "Se você possui dúvidas em como prosseguir em sua jornada, ele certamente poderá lhe ajudar.",
                90f, false, true);
        ifmg.getRoomInventory().addNewItem("Claudex",
                "Claudex Carlayle III. Mestre vampiro da matemágica computacional.",
                80f, false, true);
        cenarioAleatorio.getRoomInventory().addNewItem("Lixeira", 
                "Uma lixeira aleatória de um lugar aleatório com alguns DVD's nela descartados.", 100f, false, true);

        setCurrentRoom(casteloVilaEsperanca); // start game outside        
    }

    /**
     * Main play routine.Loops until end of play.
     *
     * @throws java.lang.Exception wich is the time Unit delay.
     */
    public void play() throws Exception {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        typer.writeText("Obrigado por jogar. Um beijo no pescoço.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() throws Exception {
        System.out.println();
        typer.writeText("Bem vindo(a) a Sabará Hills!\n");
        typer.writeText("Você é um(a) vampiro(a) que vive no século XXI.\n");
        typer.writeText("Acorda em seu castelo localizado no bairro Vila Esperança.\n");
        typer.writeText("Caro(a) degustador(a) de sangue, informe seu saudosíssimo nome vampirístico:\n");
        String name = parser.getAnswer();
        createPlayer(name);
        missions.addMission("Procure um pedaço de Hardware com conexão a websites para checar o estado de sua desejada encomenda "
                + "do sangue Bombay.");
        typer.writeText("Meu(Minha) consagrado(a) " + name + "\n");
        typer.writeText("Digite 'acode' se precisar de ajuda.\n");
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) throws Exception {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            typer.writeText("Fazer oquê?");
            return false;
        }

        String commandWord = command.getCommandWord();

        switch (commandWord) {
            case "acode":
                printHelp();
                break;
            case "partiu":
                goRoom(command);
                break;
            case "sair":
                wantToQuit = quit(command);
                break;
            case "observar":
                look();
                break;
            case "voltar":
                backLastRoom();
                break;
            case "pegar":
                tryCatch(command);
                break;
            case "descartar":
                dropItem(command);
                break;
            case "inventario":
                String itens = player.getPlayerInventory().getItensName();
                typer.writeText(itens.length() > 0 ? itens : "Você coloca sua mochila no chão, abre-a e vê que não há nada."
                        + "\nPor que você está usando uma mochila vazia?");
                break;
            case "inventário":
                String itensB = player.getPlayerInventory().getItensName();
                typer.writeText(itensB.length() > 0 ? itensB : "Você coloca sua mochila no chão, abre-a e vê que não há nada."
                        + "\nPor que você está usando uma mochila vazia?");
                break;
            case "interagir":
                interactItem(command);
                break;
        }

        return wantToQuit;
    }

    private void look() throws Exception {
        typer.writeText(currentRoom.getLongDescription());
    }

    /**
     * Print out some help information. Here we print some stupid, cryptic
     * message and a list of the command words.
     */
    private void printHelp() throws Exception {
        typer.writeText("Você está em: " + currentRoom.getDescription() + "\n");
        typer.writeText("Dilema(s):\n");
        missions.getMissions();
        System.out.println();
        typer.writeText("Seus comandos são:");
        parser.showCommands();
        System.out.println();
        typer.writeText("\nAções no jogo:\n 'Comando + Nome objeto ou personagem' - (exemplo: pegar maracas)");
        typer.writeText(currentRoom.getExitString());
        System.out.println();
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     */
    private void goRoom(Command command) throws Exception {
        Random maybeRandom = new Random();
        String[] answers = new String[]{"Você pegou um transporte alternativo até ", "Você pegou um patinete pelo aplicativo e foi até ",
            "Você pegou um mototaxi até"};
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            typer.writeText("Ir onde?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            typer.writeText("Não há saída nessa direção.");
        } else {
            previousRooms.push(currentRoom);
            setCurrentRoom(nextRoom);
            typer.writeText(answers[maybeRandom.nextInt(3)] + currentRoom.getName());
            typer.writeText(currentRoom.getDescription());
            System.out.println();
        }
    }

    /**
     * Tenta pegar o item informado.
     *
     * @param command o comando contendo o nome do item
     */
    private void tryCatch(Command command) throws Exception {
        if (!(command.hasSecondWord()) || !currentRoom.longDescriptionHasSeenBefore()) {
            typer.writeText("Pegar o quê?");
            return;
        }

        String itemName = command.getSecondWord();

        //Try to search a item in inventory of the room.
        Item roomItem = currentRoom.getRoomInventory().getItemByName(itemName);

        if (roomItem == null) {
            typer.writeText("Este item não existe!\nTalvez você esteja sonhando...");
        } else if (roomItem.isCollectible()) {
            //verify if was be possible to catch the item, if is true, remove that from the room.
            if (player.addPlayerInventoryItem(roomItem)) {
                currentRoom.getRoomInventory().removeItemByName(roomItem.getItemName());
                if (roomItem.getItemName().equals("Encomenda")) {
                    endGame();
                }
            }
        } else {
            typer.writeText("Este item não é coletável. Talvez outra abordagem...");
        }
    }

    /**
     * Drop the item on the room who has this discarted.
     *
     * @param command o comando contendo o nome do item
     */
    private void dropItem(Command command) throws Exception {
        if (!(command.hasSecondWord())) {
            typer.writeText("Descartar o quê?");
            return;
        }

        String itemName = command.getSecondWord();

        //Tenta localizar o item no inventário da sala atual.
        Item playerInventoryItem = player.getPlayerInventory().getItemByName(itemName);

        if (playerInventoryItem == null) {
            typer.writeText("Ei!\nVocê não possui esse item.");
        } else {
            typer.writeText("O item [" + playerInventoryItem.getItemName() + "] foi descartado.");
            currentRoom.getRoomInventory().addNewItem(playerInventoryItem);
            player.getPlayerInventory().removeItemByName(playerInventoryItem.getItemName());
        }
    }

    /**
     * Joga fora o item de nome informado
     *
     * @param command o comando contendo o nome do item
     */
    private void interactItem(Command command) throws Exception {
        if (!(command.hasSecondWord()) || !currentRoom.longDescriptionHasSeenBefore()) {
            typer.writeText("Interagir com o quê?");
            return;
        }

        String itemName = command.getSecondWord();

        //Tenta localizar o item no inventário da sala atual.
        Item roomInventoryItem = currentRoom.getRoomInventory().getItemByName(itemName);

        if (roomInventoryItem == null) {
            typer.writeText("Ei! Este item não existe nesse espaço tempo... igual minha nota em cálculo.");
        } else {
            itemAction(roomInventoryItem);
        }
    }

    /**
     * Gerencia o evento de interação com o item
     */
    private void itemAction(Item item) throws Exception {
        if (item.isInteractable()) {
            String itemName = item.getItemName();
            switch (itemName.toLowerCase()) {
                case "jarvis":
                    if (missions.getHierarchy() == 1) {
                        typer.writeText("Você começa entrar no site do Korreias, mas está extremamente lento..."
                                + "\nVocê, dotado de uma bela paciência, bate no computador, e acidentalmente,"
                                + "um site contendo vídeos de gatinhos abre automaticamente."
                                + "\nWhoa, você passou horas vendo vídeos de gatinhos. Se esqueceu do que veio fazer aqui? \n"
                                + "Rapidamente muda de aba, onde o site do Korreias"
                                + " finalmente carregou.\nAo digitar o código de rastreio, a seguinte mensagem aparece de maneira lenta "
                                + "e gradual...\n "
                                + "  ________   _________ _____       __      _______          _____   ____  \n"
                                + " |  ____\\ \\ / /__   __|  __ \\     /\\ \\    / /_   _|   /\\   |  __ \\ / __ \\ \n"
                                + " | |__   \\ V /   | |  | |__) |   /  \\ \\  / /  | |    /  \\  | |  | | |  | |\n"
                                + " |  __|   > <    | |  |  _  /   / /\\ \\ \\/ /   | |   / /\\ \\ | |  | | |  | |\n"
                                + " | |____ / . \\   | |  | | \\ \\  / ____ \\  /   _| |_ / ____ \\| |__| | |__| |\n"
                                + " |______/_/ \\_\\  |_|  |_|  \\_\\/_/    \\_\\/   |_____/_/    \\_\\_____/ \\____/ ");
                        missions.addMission("Sua encomenda do sangue Bombay (falso 'O') fora extraviada..."
                                + "Descubra onde ela está.");
                        missions.finishMission("Procure um pedaço de Hardware com conexão a websites para checar o estado de sua"
                                + " desejada encomenda do sangue Bombay.");
                        typer.writeText("\n{Nova missão}");
                        missions.getMissions();
                        missions.setHierarchy(2);
                    } else if (missions.getEasterEgg() == true && !(player.getPlayerInventory().getItemByName("dvd").equals(null))) {
                        disc = new DigitalVersatileDisc();
                        typer.writeText("Você insere o dvd no terminal, vulgo Jarvis\nComeça a assistir os filmes da saga.");
                        typer.writeText("Entre vampiros que brilham na luz e homens sarados que viram lobos\nVocê vicia nesta obra prima.");
                        disc.playContent();
                        typer.writeText("E chega a cena do casamento...");
                        typer.writeText("Você se vê chorando!");
                        typer.writeText("Já se foram 5 horas e nada de você procurar sua encomenda!");
                    }
                    break;
                case "frentista":
                    if (missions.getHierarchy() == 2) {
                        typer.writeText("Você chega perto do conhecido Frentista... e realiza uma saudação. \n"
                                + "O Frentista nota que você parece estar em um turbilhão de emoções, e o questiona se algo está errado.\n"
                                + "Você conversa com ele sobre sua encomenda extraviada, e o mesmo diz: \n"
                                + "Frentista:\n - Uai sô...\nO sô Claudex passô aqui carregano um trem grande den do carro..."
                                + "\n - falô que tava atrasado para da a aula de teoria de vampirage... "
                                + "\nParecia meio nervoso como se estivesse escondendo exatamente "
                                + "o que você está procurando. Estranho, não? \n"
                                + " -Pra ser sincero nem sei como eu sei disso... parece até que esse universo é uma simulação, e que estamos sendo controlados. Hehe.");
                        typer.writeText("Sabendo onde sua encomenda se encontra, você percebe que não aguentaria o peso dela, afinal ela está em uma caixa de chumbo. "
                                + "\nEu realmente preciso dar um jeito nisso antes de ir no IF...");
                        missions.addMission("Claudex se encontra na turma de teoria de vampiragens.");
                        missions.finishMission("Sua encomenda do sangue Bombay (falso 'O') fora extraviada..."
                                + "Descubra onde ela está.");
                        typer.writeText("\n{Nova missão}\n");
                        missions.getMissions();
                        item.setInteractable(false);
                        missions.setHierarchy(3);
                    }
                    break;
                case "mordomo":
                    if (item.isInteractable()) {
                        typer.writeText("Você se aproxima do mordomo, o mesmo parece paralisado e com a boca aberta. \n"
                                + "Que estranho... é como se ele desejasse algo para beber. Acho que uma bebida pangaláctica seria demais... \n"
                                + "Você vasculha seu inventário e procura algo que possa tirar o mordomo daquele estado e... ");
                        if (player.getPlayerInventory().getItensName().length() == 0) {
                            typer.writeText("Uau! É incrível como você está carregando uma mochila vazia por ai."
                                    + "Me parece ter algum sentido... na verdade, acho que não.");
                        } else {
                            Item bloodyMary = player.getPlayerInventory().getItemByName("Drinky Bloody Mary");
                            if (bloodyMary != null) {
                                typer.writeText("Você se lembra da bebida que pegou no bar, e rapidamente retira ela de sua mochila."
                                        + "\nVocê retira a tampa da bebida e percebe que o cheiro é horrivel... "
                                        + "\nMesmo assim, vira todo o liquido na boca do mordomo. "
                                        + "Afinal, se não matar talvez funcione.");
                                typer.writeText("De inicio, nada parece ter acontecido, e você fica sem saber o que fazer. \n"
                                        + "Após alguns segundos o mordomo se mexe e começa a tossir. \n"
                                        + " - Mordomo: Cara, muito obrigado por me salvar. Eu fui paralisado por uma pergunta"
                                        + " feita pelo Claudex.\n"
                                        + " - Não sei como posso te agradecer. Existe algo que posso fazer por você?\n"
                                        + "Você, como se não fosse óbvio, questiona se ele pode ajuda-lo a carregar umas muamba.\n"
                                        + "Então o mordomo decide que irá ajudá-lo em sua empreitada."
                                        + "você aumenta o peso máximo que consegue carregar (200kg). \n");
                                player.setPlayerMaxWeight(200f);
                                player.getPlayerInventory().removeItemByName("Drinky Bloody Mary");
                                missions.finishMission("Procure uma forma de aumentar o peso máximo que você consegue carregar.");
                                item.setInteractable(false);
                            }
                        }
                    }
                    break;
                case "claudex":
                    if (missions.getHierarchy() == 3 && item.isInteractable()) {
                        typer.writeText("Claudex:\n - Sim.\n - A encomenda está comigo. "
                                + "Mas você precisa ser digno para recuperá-la. Vou lhe fazer 2 perguntas e você precisa acertá-las!");
                        boolean resultOfFinalQuiz = missions.getFinalQuiz();
                        if (resultOfFinalQuiz == true) {
                            item.setInteractable(false);
                            Item encomenda = new Item("Encomenda", "Sua encomenda do sangue Bombay.",
                                    121f, true, false);
                            if (!player.addPlayerInventoryItem(encomenda)) {
                                typer.writeText("O Claudex joga a encomenda no chão pois você parece fraco demais para carrega-lo.");
                                missions.addMission("Procure uma forma de aumentar o peso máximo que você consegue carregar.");
                                typer.writeText("\n{Nova missão}\n");
                                missions.getMissions();
                                currentRoom.getRoomInventory().addNewItem(encomenda);
                            } else {
                                //call the method who's finish the game.
                                endGame();
                            }
                            missions.finishMission("Claudex se encontra na turma de teoria de vampiragens.");
                        } else {
                            typer.writeText("Você fracassou miserávelmente.\nNão conseguindo lidar com este trágico destino"
                                    + "... você resolve virar um pássaro intergalático.");
                        }
                    }
                    break;
                case "lixeira":
                    typer.writeText("Você observa que entre muitos DVD's com conteúdos não autorizados, há um DVD intitulado:\n"
                            + "'5 em 1 Saga Créspulo, Lua Minguante, Calypsto, Entardecer parte 1 e Entardecer parte 2.'");
                    item.setInteractable(false);
                    Item dvd = new Item("DVD", "Um DVD com filmes de vampiros teens nele contido.", 0.1f, true, false);
                    currentRoom.getRoomInventory().addNewItem(dvd);
                    missions.setEasterEgg();

            }
        } else {
            typer.writeText(item.getItemDescription());
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the game.
     *
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) throws Exception {
        if (command.hasSecondWord()) {
            typer.writeText("Sair oque?");
            return false;
        } else {
            return true;  // return to quit.
        }
    }

    /**
     * Set the current player location(room) and update player map, as the
     * player reaches a new place.
     *
     * @param newLocationRoom the new location that the player discovered, it
     * will also be the current location in the game
     */
    private void setCurrentRoom(Room newLocationRoom) {
        currentRoom = newLocationRoom;
    }

    private void backLastRoom() throws Exception {
        if (previousRooms.empty()) {
            typer.writeText("Lamento, não há para onde voltar!");
        } else {
            Room targetRoom = previousRooms.peek();
            typer.writeText(targetRoom.getDescription());
            setCurrentRoom(targetRoom);
            previousRooms.pop();
        }
    }

    /**
     * This method end the game.
     *
     * @throws Exception time sleep by method writeText
     */
    private void endGame() throws Exception {
        typer.writeText("Parabeinxx!!\nVocê recuperou sua encomenda que havia sido extraviada!\n"
                + "Meu(minha) consagrado(a)...tu retornas a teu aconchego para assistir um Catflix e apreciar sua "
                + "série favorita...'Orange is the new Cat'.\n-=-=-=-=-=-=-=-=- FIM -=-=-=-=-=-=-=-=-");
        System.exit(0);
    }

    /**
     * This method get the name of new current player in the init of game. He's
     * called by constructor method.
     */
    private void createPlayer(String playerName) {
        player = new Player(playerName);
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        game.play();
    }
}
