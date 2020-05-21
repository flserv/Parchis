package Parchis;

/**
 *
 * @author Paco
 */
public class Test2 {

    private MainBoard myBoard;
    private boolean[] playerIsHuman;
    private TokensSquares myTS;

    public Test2() {
        PlayersOptionsPanel optionsPanel = new PlayersOptionsPanel(this.playerIsHuman);

        this.myBoard = new MainBoard();
        checkE();
    }

    public static void main(String[] args) {
        Test2 myTest = new Test2();
    }

    public void checkE() {
        System.out.println(this.playerIsHuman);
    }
}
