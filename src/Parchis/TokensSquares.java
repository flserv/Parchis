package Parchis;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Paco
 */
public class TokensSquares {

    private Location[][] tokenSq = new Location[4][4];
    private TokensMileages mileages = new TokensMileages();

    public TokensSquares() {
        tokenSq = this.tokenSq;
        for (int player = 0; player < 4; player++) {
            for (int aToken = 0; aToken < 4; aToken++) {
                tokenSq[player][aToken] = new Location(0, 1);//aToken / 2 + player * 4 / 2 + 69, 2 * (aToken % 2));
            }
        }
    }

    public boolean moveToken(int player, int aToken, int steps) {
        //Arrange token in origin square.
        leaveSquare(player, aToken);
        mileages.advanceToken(player, aToken, steps);
        Location finalSquare = new Location(0, 1);

        finalSquare.setSquare(mileages.calculateSquare(player, aToken));

        tokenSq[player][aToken] = finalSquare;
        //Arrange tokens in destination square. Newer token in subsquare 2, older in subsquare 0.
        putInSquare(player, aToken);
        //if (finalSquare.getSquare() < 77) {
        System.out.println("Player " + player + " moves token " + aToken + " " + steps + " steps to square " + finalSquare.getSquare());
        return true;
        //} else {
        //    return false;
        //}
    }

    public int getSquare(int player, int aToken) { //Returns the square where the token is.
        return this.tokenSq[player][aToken].getSquare();
    }

    public int getSubSquare(int player, int aToken) { //Returns the subsquare where the token is.
        return this.tokenSq[player][aToken].getSubSquare();
    }

    public int sharesSquare(int player, int aToken) {
        int square = getSquare(player, aToken);
        for (int ePlayer = 0; ePlayer < 4; ePlayer++) {
            for (int eToken = 0; eToken < 4; eToken++) {
                if (square == getSquare(ePlayer, eToken)) {
                    if (!((player == ePlayer) && (aToken == eToken))) {
                        return ePlayer;
                    }
                }
            }
        }
        return -1;
    }

    public boolean gameIsOver() {
        for (int aPlayer = 0; aPlayer < 4; aPlayer++) {
            for (int aToken = 0; aToken < 4; aToken++) {
                if (this.getMileage(aPlayer, aToken) < 76) {
                    return false;
                }
            }
        }
        return true;
    }

    private void leaveSquare(int player, int token) {
        if (this.tokenSq[player][token].getSubSquare() != 1) {
            for (int aPlayer = 0; aPlayer < 4; aPlayer++) {
                for (int aToken = 0; aToken < 4; aToken++) {
                    if (this.getSquare(player, token) == this.getSquare(aPlayer, aToken)) {
                        if ((token == aToken) && (player == aPlayer)) {
                            //break;
                        } else {
                            this.tokenSq[aPlayer][aToken].setSubSquare(1);
                            //return;
                        }

                    }
                }
            }
        }
    }

    private void putInSquare(int player, int token) {
        //this.tokenSq[player][token].setSubSquare(1);
        for (int aPlayer = 0; aPlayer < 4; aPlayer++) {
            for (int aToken = 0; aToken < 4; aToken++) {
                if (this.getSquare(player, token) == this.getSquare(aPlayer, aToken)) {
                    if ((token == aToken) && (player == aPlayer)) {
                        break;
                    } else {
                        //If the square is already occupied, the token who arrives goes to subsquare 2, and the other goes to subsquare 0.
                        this.tokenSq[aPlayer][aToken].setSubSquare(0);
                        this.tokenSq[player][token].setSubSquare(2);
                        return;
                    }

                }
            }
        }
    }

    public int mileageToSquare(int player, int mileage) {
        return this.mileages.mileageToSquare(player, mileage);
    }

    public int getMileage(int player, int token) {
        return this.mileages.getMileage(player, token);
    }

    public boolean isFull(int square) {
        int tokensInSquare = 0;
        for (int aPlayer = 0; aPlayer < 4; aPlayer++) {
            for (int aToken = 0; aToken < 4; aToken++) {
                if (getSquare(aPlayer, aToken) == square) {
                    tokensInSquare += 1;
                }
            }
        }
        if (Arrays.asList(76, 84, 92, 100).contains(square)) {
            return (tokensInSquare == 4) ? true : false;
        } else {
            return (tokensInSquare > 1) ? true : false;
        }
    }

    //Returns an ArrayList of the tokens that are in a square.
    public ArrayList<Token> tokensInSquare(int square) {
        ArrayList<Token> listOfTokens = new ArrayList<Token>();
        for (int player = 0; player < 4; player++) {
            for (int aToken = 0; aToken < 4; aToken++) {
                if (getSquare(player, aToken) == square) {
                    listOfTokens.add(new Token(player, aToken));
                }
            }
        }
        return listOfTokens;
    }

    public int getDestinationSquare(int player, int token, int steps) {
        return mileageToSquare(player, steps + getMileage(player, token));
    }

    void tokenGoHome(int player, int tokenToMove) {
        this.mileages.tokenGoHome(player, tokenToMove);
        this.tokenSq[player][tokenToMove].setSquare(0);
        this.tokenSq[player][tokenToMove].setSubSquare(1);
    }

    public boolean allItsTokensFinished(int player) {
        for (int aToken = 0; aToken < 4; aToken++) {
            if (this.getMileage(player, aToken) < 76) {
                return false;
            }
        }
        return true;
    }
}
