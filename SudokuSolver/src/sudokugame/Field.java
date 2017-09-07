/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokugame;

/**
 *
 * @author r41
 */
import java.io.*;
import java.util.*;

/**
 * Abstract Data Type for Sudoku playing field
 */
public class Field {

    public static final int SIZE = 9;

    private int model[][];

    public Field() {
        // make new array of size SIZExSIZE
        this.model = new int[SIZE][SIZE];
        // initialize with empty cells
        init(SIZE - 1, SIZE - 1);
    }

    private void init(int i, int j) {
        if (i < 0) {
            // all rows done!
        } else if (j < 0) {
            // this row done - go to next!
            init(i - 1, SIZE - 1);
        } else {
            this.clear(i, j);
            init(i, j - 1);
        }
    }

    public void fromFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            fromScanner(sc, 0, 0);
        } catch (FileNotFoundException e) {
            // :-(
        }
    }

    private void fromScanner(Scanner sc, int i, int j) {
        if (i >= SIZE) {
            // all rows done!
        } else if (j >= SIZE) {
            // this row done - go to next!
            fromScanner(sc, i + 1, 0);
        } else {
            try {
                int val = Integer.parseInt(sc.next());
                this.model[i][j] = val;
            } catch (NumberFormatException e) {
                // skip this cell
            }
            fromScanner(sc, i, j + 1);
        }
    }

    public String toString() {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0) {
                res.append("+-------+-------+-------+\n");
            }
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0) {
                    res.append("| ");
                }
                int val = this.model[i][j];
                res.append(val > 0 ? val + " " : "  ");
            }
            res.append("|\n");
        }
        res.append("+-------+-------+-------+");
        return res.toString();
    }

    /**
     * returns false if the value val cannot be placed at row i and column j.
     * returns true and sets the cell to val otherwise.
     */
    public boolean tryValue(int val, int i, int j) {
        if (!checkRow(val, i)) {
            return false;
        }
        if (!checkCol(val, j)) {
            return false;
        }
        if (!checkBox(val, i, j)) {
            return false;
        }
        this.model[i][j] = val;
        return true;
    }

    /**
     * checks if the cell at row i and column j is empty, i.e., whether it
     * contains 0
     */
    public boolean isEmpty(int i, int j) {
        return model[i][j] == 0;
        //if value is 0, it'll return true
    }

    /**
     * sets the cell at row i and column j to be empty, i.e., to be 0
     */
    public void clear(int i, int j) {
        model[i][j] = 0;
    }

    /**
     * checks if val is an acceptable value for the row i
     */
    private boolean checkRow(int val, int i) {
        for (int columnN = 0; columnN < SIZE; columnN++) {
            if (val == model[i][columnN]) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if val is an acceptable value for the column j
     */
    private boolean checkCol(int val, int j) {
        for (int rowN = 0; rowN < SIZE; rowN++) {
            if (val == model[j][rowN]) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks if val is an acceptable value for the box around the cell at row i
     * and column j
     */
    private boolean checkBox(int val, int i, int j) {
        final int s = (int) Math.sqrt(SIZE);
        //square root of the size of 
        final int x = i / s * s, y = j / s * s;
        //checks what box one is occupying in
        for (int columnN = 0; columnN < s; ++columnN) {
            for (int rowN = 0; rowN < s; ++rowN) {
                if (model[columnN + x][rowN + y] == val) {
                    return false;
                }
            }
        }
        return true;
    }
}
