package Parchis;

import static java.lang.Math.max;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paco
 */
public class Parchis {

//variables declaration    
    private boolean[] playerIsHuman;
    private Dice dice;
    private TokensSquares myTS;
    private MainBoard myBoard;
//    End of variables declaration

    public Parchis() {
        initComponents();
    }

    public Parchis(boolean[] PlayerIsHuman) {
        initComponents();
        this.playerIsHuman = new boolean[4];
        this.playerIsHuman = PlayerIsHuman;
    }

    public static void main(String[] args) {
        Parchis myGame = new Parchis();
        myGame.play();

//        myGame.test3();
    }

    private void turn(int player) throws InterruptedException {
        int nSixes = 0;
        int steps;
        int stepsToMove;
        int destinationSquare;
        int tokenToMove = -1;
        ArrayList<Token> killedTokens = new ArrayList<Token>();
        ArrayList<Integer> movesToDo = new ArrayList<Integer>();
        do {
            steps = this.dice.roll();
            System.out.println("Dice=" + steps);
            stepsToMove = steps;
            if (steps == 6) {
                nSixes++;
                System.out.println("nSixes=" + nSixes);
                //At third six on the dice, moved token goes home.
                if (nSixes > 2) {
                    if (tokenToMove >= 0) {
                        System.out.println("3rd six. Token " + tokenToMove + " of player " + player + " goes home");
                        this.tokenGoHome(new Token(player, tokenToMove));
                        this.myBoard.updateTokensPositions(myTS);
                    }
                    return;
                }
                //If all the tokens are out and steps=6 then stepsToMove=7
                if (allItsTokensAreOut(player)) {
                    System.out.println("As all tokens are out will count 7");
                    stepsToMove = 7;
                }
            }
            movesToDo.add(stepsToMove);
            do {
                System.out.println("1st move to do is " + movesToDo.get(0) + " steps");
                tokenToMove = evaluateMoves(player, movesToDo.get(0));
                System.out.println("player " + player + " movesToDo size: " + movesToDo.size());
                if ((tokenToMove >= 0) && (tokenToMove < 4)) {
                    destinationSquare = this.myTS.getDestinationSquare(player, tokenToMove, movesToDo.get(0));
                    System.out.println("Best move is token " + tokenToMove + " to square " + destinationSquare);
                    if (Arrays.asList(76, 84, 92, 100).contains(destinationSquare)) {
                        movesToDo.add(10);
                        System.out.println("Will move 10 steps because arrives to square 76, 84, 92 or 100");
                    } else {
                        if (!isASecure(destinationSquare)) {
                            killedTokens.addAll(this.myTS.tokensInSquare(destinationSquare));
                            while (!killedTokens.isEmpty()) {
                                if (killedTokens.get(0).getPlayer() != player) {
                                    movesToDo.add(20);
                                    System.out.println("Will move 20 steps because killing token " + killedTokens.get(0).getToken() + " of player " + killedTokens.get(0).getPlayer());
//tokens that have been killed go home.
                                    tokenGoHome(killedTokens.get(0));
                                }
                                killedTokens.remove(0);
                            }
                        }
                    }
                    this.myTS.moveToken(player, tokenToMove, movesToDo.get(0));
                    this.myBoard.updateTokensPositions(myTS);
                } else {
                    System.out.println("Unable to move.");
                }
                movesToDo.remove(0);
            } while (!movesToDo.isEmpty() && (!this.myTS.allItsTokensFinished(player)));
        } while (steps == 6);
    }

    private int evaluateMoves(int player, int steps) {
        //Returns the token to move.
        //With a 5 you must take out a token.
        if (steps == 5) {
            if (!isFull(this.myTS.mileageToSquare(player, 5))) {
                for (int aToken = 0; aToken < 4; aToken++) {
                    if (this.myTS.getSquare(player, aToken) == 0) {
                        return aToken;
                    }
                }
            }
        }

        int[] movValue = new int[4];
        int destinationSquare;
        // Give a value to each token move.
        for (int aToken = 0; aToken < 4; aToken++) {
            destinationSquare = this.myTS.mileageToSquare(player, this.myTS.getMileage(player, aToken) + steps);
            // Check if token is in start square.
            if (this.myTS.getMileage(player, aToken) == 0) {
                movValue[aToken] = -1000;
                // Check for token already at finish.    
            } else if (this.myTS.getSquare(player, aToken) == 76) {
                movValue[aToken] = -1000;
                System.out.println("P " + player + " T " + aToken + " steps:" + steps + " token already at finish");
                // Check for finish is not so far.
            } else if (this.myTS.getMileage(player, aToken) + steps > 76) {
                movValue[aToken] = -1000;
                System.out.println("P " + player + " T " + aToken + " steps:" + steps + " finish is not so far " + (this.myTS.getMileage(player, aToken) + steps));
                // Check for barriers in the path.
            } else if (areBarriersInPath(player, aToken, steps)) {
                movValue[aToken] = -1000;
                System.out.println("P " + player + " T " + aToken + " steps:" + steps + " barriers in the path");
                // Check for destination is already full.
            } else if (isFull(destinationSquare) && this.myTS.tokensInSquare(destinationSquare).get(0).getPlayer() == player) {
                movValue[aToken] = -1000;
                System.out.println("P " + player + " T " + aToken + " steps:" + steps + " destination is already full");
            } else {
                // Critera 1: most advanced is most valuable. +mileage
                movValue[aToken] = this.myTS.getMileage(player, aToken);
                // Criteria 2: not in a secure square. +10
                if (!isASecure(this.myTS.getSquare(player, aToken))) {
                    movValue[aToken] += 10;
                }
                // Criteria 3: goes to a secure square. +10
                if (isASecure(destinationSquare)) {
                    movValue[aToken] += 10;
                }
                // Criteria 4: will kill some enemy token. +20*each kill
                if ((!this.myTS.tokensInSquare(destinationSquare).isEmpty()) && this.myTS.tokensInSquare(destinationSquare).get(0).getPlayer() != player) {
                    movValue[aToken] += 20 * this.myTS.tokensInSquare(destinationSquare).size();
                }
                // If one gets a six should open its barriers. +100
                if (steps <= 6 && (isABarrier(this.myTS.getSquare(player, aToken)))) {
                    movValue[aToken] += 100;
                }
            }
        }
        //Choose the most valued token move.
        int maxValued = 0;
        for (int aToken = 1; aToken < 4; aToken++) {
            if (movValue[maxValued] < movValue[aToken]) {
                maxValued = aToken;
            }
            //maxValued = (movValue[aToken] < movValue[aToken + 1] ? (aToken + 1) : aToken);
        }
        //Returns -1 if moves are not possible.
        if (movValue[maxValued] < 0) {
            return -1;
        }
        return maxValued;
    }

    private void initComponents() {
        this.myBoard = new MainBoard();
        myBoard.setLocationRelativeTo(null);
        myBoard.setVisible(true);
        dice = new Dice(6);
        myTS = new TokensSquares();
    }

    public void play() {

        Dice playerChooser = new Dice(4);
        //first player will be "player+1"
        int player = playerChooser.roll() - 1;
        System.out.println("The game begins with player " + player);
        this.myTS.moveToken(player, 0, 5);
        this.myBoard.updateTokensPositions(myTS);
        do {
            player = (player + 1) % 4;
            try {
                sleep(350);
                System.out.println("Turn for player -----------------> " + player);
                turn(player);
            } catch (InterruptedException ex) {
                Logger.getLogger(Parchis.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("InterruptedException Parchis ln 117");
            }
        } while (!myTS.gameIsOver());
        this.endOfTheGame();
    }

    private void endOfTheGame() {
        System.out.println("GAME OVER!!!");
    }

    private boolean isASecure(int square) {
        return Arrays.asList(5, 12, 17, 22, 29, 34, 39, 46, 51, 56, 63, 68).contains(square);
    }

    private boolean isFull(int square) {
        return this.myTS.isFull(square);
    }

    private boolean isABarrier(int square) {
        return isASecure(square) && isFull(square);
    }

    private boolean areBarriersInPath(int player, int token, int steps) {
        for (int step = 0; step < steps; step++) {
            if (isABarrier(this.myTS.mileageToSquare(player, this.myTS.getMileage(player, token) + step + 1))) {
                return true;
            }
        }
        return false;
    }

    private boolean allItsTokensAreOut(int player) {
        for (int aToken = 0; aToken < 4; aToken++) {
            if (this.myTS.getSquare(player, aToken) == 0) {
                return false;
            }
        }
        return true;
    }

    private void test3() {
//        int player = 1;
//        this.myTS.moveToken(player, 0, 66);
//        this.myTS.moveToken(player, 1, 66);
//        this.myBoard.updateTokensPositions(myTS);
        int initMileage = 66;
        this.myTS.moveToken(0, 0, initMileage);
        this.myTS.moveToken(0, 1, initMileage);
        this.myTS.moveToken(1, 0, initMileage);
        this.myTS.moveToken(1, 1, initMileage);
        this.myTS.moveToken(2, 0, initMileage);
        this.myTS.moveToken(2, 1, initMileage);
        this.myTS.moveToken(3, 0, initMileage);
        this.myTS.moveToken(3, 1, initMileage);

        do {
            for (int player = 0; player < 4; player++) {
                for (int token = 0; token < 2; token++) {
                    this.myTS.moveToken(player, token, 1);
                    this.myBoard.updateTokensPositions(myTS);
                    try {
                        sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Parchis.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } while (this.myTS.getMileage(3, 1) < 76);
    }

    private void tokenGoHome(Token token) {
        this.myTS.tokenGoHome(token.getPlayer(), token.getToken());
    }

}
