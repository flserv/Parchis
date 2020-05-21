package Parchis;

import static java.lang.Thread.sleep;
import java.util.Random;

public class Test1 {

    private void selfPlay(int i) throws InterruptedException {
        int tokenToMove, steps;
        for (int round = 1; round < i; round++) {
            System.out.println("Round " + round);
            for (int player = 0; player < 4; player++) {
                sleep(200);
                tokenToMove = randToken.nextInt(4);
                steps = myDice.roll();
                tokenSquares.moveToken(player, tokenToMove, steps);
                System.out.println("player " + player + " token " + tokenToMove + " steps " + steps);
                myBoard.updateTokensPositions(tokenSquares);
//                System.out.println("player " + player + " token " + tokenToMove + " steps " + steps);
//                myBoard.setTokenSquare(player, tokenToMove);
            }
        }
    }
    private Dice myDice;
    private MainBoard myBoard;
    private final Random randToken;
    private TokensSquares tokenSquares;

    public Test1() {
        this.myDice = new Dice(6);
        this.myBoard = new MainBoard();
        this.randToken = new Random();
        this.tokenSquares = new TokensSquares();
        myBoard.setVisible(true);
    }

    public static void main(String[] args) throws InterruptedException {
        Test1 myTest1 = new Test1();
//        MainBoard myBoard = new MainBoard();
//        myBoard.setVisible(true);
        myTest1.selfPlay(100);
    }
}
