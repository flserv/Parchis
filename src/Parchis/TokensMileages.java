/*
 * The MIT License
 *
 * Copyright 2019 Paco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Parchis;

/**
 *
 * @author Paco
 */
public class TokensMileages {

    // mileage[player][token]
    //Maximum mileage is 76. Minimum mileage is 0.
    private int[][] mileage = new int[4][4];

    public TokensMileages() {
        this.mileage = new int[4][4];
    }

    public void advanceToken(int player, int token, int steps) {
        int finalMileage = this.mileage[player][token] + steps;
        if (finalMileage <= 76 && finalMileage >= 0) {
            this.mileage[player][token] = finalMileage;
        } else {
            throw new IllegalArgumentException("steps out of range");
        }
    }

    public void tokenGoHome(int player, int token) {
        this.mileage[player][token] = 0;
    }

    public int getMileage(int player, int token) {
        return this.mileage[player][token];
    }

    public int calculateSquare(int player, int token) {
        int aMileage = getMileage(player, token);
        return mileageToSquare(player, aMileage);
    }

    public int mileageToSquare(int player, int mileage) {
        if (mileage < 69) {
            return (mileage - 1 + (17 * player)) % 68 + 1;
        } else {
            return (mileage + player * 8);
        }
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
