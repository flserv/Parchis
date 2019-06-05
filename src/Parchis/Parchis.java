package Parchis;

/**
 *
 * @author Paco
 */
public class Parchis {

//variables declaration    
    private final int[] playerIsHuman;
    private Dice dice;
    private TokensSquares myTS;
//    End of variables declaration

    public Parchis() {
        initComponents();
    }

    public static void main(String[] args) {
        Parchis myGame = new Parchis();
        myGame.play();
    }

    private TokensSquares turn(int player) {
        boolean rollItAgain = false;
        int steps = dice.roll();
        int stepsToMove;
        int tokenToMove = evaluateMoves(player);
    }

    private int evaluateMoves(int player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initComponents() {
        PlayersOptionsPanel myOptionsPanel = new PlayersOptionsPanel();
        MainBoard myBoard = new MainBoard();
    }

    private void play() {
        boolean gameIsOver = false;
        Dice playerChooser = new Dice(4);
        //first player will be "player+1"
        int player = playerChooser.roll();
        do {
            player = (player + 1) % 4;
            turn(player);

        } while (!myTS.gameIsOver());
        this.endOfTheGame();
    }

    private void endOfTheGame() {
        System.out.println("YOU WIN!!!");
    }

}
