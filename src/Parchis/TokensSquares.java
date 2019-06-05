package Parchis;

/**
 *
 * @author Paco
 */
public class TokensSquares {

    private Location[][] tokenSq = new Location[4][4];

    public TokensSquares() {
        tokenSq = this.tokenSq;
        for (int player = 0; player < 4; player++) {
            for (int aToken = 0; aToken < 4; aToken++) {
//                tokenSq[player][aToken].square = 0;//1 + aToken + player * 4;
//                tokenSq[player][aToken].subSquare = 0;
                tokenSq[player][aToken] = new Location(0, 0);//aToken / 2 + player * 4 / 2 + 69, 2 * (aToken % 2));
            }
        }
    }

    public boolean moveToken(int player, int aToken, int steps) {
        Location finalSquare = new Location();
        finalSquare.square = tokenSq[player][aToken].square + steps;
        if (finalSquare.square < 77) {
            tokenSq[player][aToken] = finalSquare;
            return true;
        } else {
            return false;
        }
    }

    public int getSquare(int player, int aToken) { //Returns the square where the token is.
        return this.tokenSq[player][aToken].square;
    }

    public int getSubSquare(int player, int aToken) { //Returns the subsquare where the token is.
        return this.tokenSq[player][aToken].subSquare;
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
                if (this.tokenSq[aPlayer][aToken].square < 76) {
                    return false;
                }
            }
        }
        return true;
    }

}
