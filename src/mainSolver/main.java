package mainSolver;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class main {

  public static void promptEnterKey() {
    System.out.println("Press \"ENTER\" to continue...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }

  public static void infoBox(String infoMessage, String titleBar) {
    JOptionPane.showMessageDialog(null, infoMessage, titleBar,
        JOptionPane.WARNING_MESSAGE);
  }

  public static void main(String args[]) {
    int[][] solvedPuzzle = new int[9][9];
    boolean solved = false;
    int[][] puzzle = createPuzzle();
    // for (int x = 0; x < 9; x++) {
    // System.out.println(Arrays.toString(puzzle[x]));
    // }
    while (!(solved)) {
      solvedPuzzle = createSolution(puzzle);
      solved = checkRules(solvedPuzzle);
    }
    String finalSolution = "";
    for (int x = 0; x < 9; x++) {
      finalSolution += Arrays.toString(solvedPuzzle[x]) + "\n";
    }
    infoBox(finalSolution, "Solution");
    System.exit(0);


  }

  public static int[][] createPuzzle() {

    int[][] puzzle = new int[9][9];

    String line = null;
    Frame frame = new Frame();
    FileDialog openfile =
        new FileDialog(frame, "Select a file", FileDialog.LOAD);
    openfile.setVisible(true);
    String file = openfile.getDirectory() + File.separator + openfile.getFile();

    try {
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      int row = 0;
      while ((line = bufferedReader.readLine()) != null) {

        String[] items = line.split(" ");
        int[] values = new int[items.length];
        for (int i = 0; i < items.length; i++) {
          values[i] = Integer.parseInt(items[i]);
        }
        for (int i = 0; i < values.length; i++) {
          puzzle[row][i] = values[i];
        }
        row++;
      }

      // Always close files.
      bufferedReader.close();
    } catch (FileNotFoundException ex) {
      System.out.println("Unable to open file '" + file + "'");
    } catch (IOException ex) {
      System.out.println("Error reading file '" + file + "'");
      // Or we could just do this:
      // ex.printStackTrace();
    }
    return puzzle;

  }

  public static boolean arrayCheck(int[] X, int[] Y) {
    Arrays.sort(X);
    for (int i = 0; i < 9; i++) {
      if (X[i] != Y[i]) {
        return false;
      }
    }
    return true;
  }

  public static boolean checkRules(int[][] puzzle) {
    int[] finalValues = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    for (int i = 0; i < 9; i++) {

      int[] temp = new int[9];

      System.arraycopy(puzzle[i], 0, temp, 0, puzzle[i].length);

      int[] temp2 = new int[9];

      if (!(arrayCheck(temp, finalValues))) {
        // System.out.println("Row failure at row " + i + ": "
        // + Arrays.toString(temp));
        return false;
      } else {
        // System.out.println("ROW PASS at row " + i + ": "
        // + Arrays.toString(temp));
      }
      for (int j = 0; j < 9; j++) {
        temp2[j] = puzzle[j][i];
      }
      Arrays.sort(temp2);

      if (!(arrayCheck(temp2, finalValues))) {
        // System.out.println("Column failure at column " + i + ": "
        // + Arrays.toString(temp2));
        return false;
      } else {
        // System.out.println("COLUMN PASS at column " + i + ": "
        // + Arrays.toString(temp2));
      }

    }

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        int[] temp = new int[9];

        temp[0] = puzzle[3 * i][3 * j];
        temp[1] = puzzle[3 * i][(3 * j) + 1];
        temp[2] = puzzle[3 * i][(3 * j) + 2];

        temp[3] = puzzle[(3 * i) + 1][3 * j];
        temp[4] = puzzle[(3 * i) + 1][(3 * j) + 1];
        temp[5] = puzzle[(3 * i) + 1][(3 * j) + 2];

        temp[6] = puzzle[(3 * i) + 2][3 * j];
        temp[7] = puzzle[(3 * i) + 2][(3 * j) + 1];
        temp[8] = puzzle[(3 * i) + 2][(3 * j) + 2];

        Arrays.sort(temp);
        if (!(arrayCheck(temp, finalValues))) {

          // System.out.println("Box failure: " + Arrays.toString(temp));
          return false;
        } else {
          // System.out.println("BOX PASS: " + Arrays.toString(temp));
        }
      }


    }
    return true;

  }

  public static int[][] createSolution(int[][] puzzle) {
    int[][] attempt = new int[9][];
    for (int i = 0; i < 9; i++)
      attempt[i] = puzzle[i].clone();
    int indexSize = 1;
    boolean assignment;
    while (indexSize <= 9) {
      assignment = false;
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          int[] temp = possibleValues(attempt, i, j);
          if (attempt[i][j] == 0 && temp.length == indexSize) {
            int random = new Random().nextInt(temp.length);
            attempt[i][j] = temp[random];
            assignment = true;
            // System.out.println("Puzzle at (" + i + "," + j + ") set to: "
            // + attempt[i][j] + " out of options: " + Arrays.toString(temp));
            if (temp.length > 1) {
              return createSolution(attempt);
            }
          }
        }
      }
      if (assignment == false) {
        indexSize++;
      }

    }
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (attempt[i][j] == 0) {
          int random = new Random().nextInt(9) + 1;
          attempt[i][j] = random;
          // System.out.println("Puzzle at (" + i + "," + j + ") set to: "
          // + attempt[i][j] + " randomly.");
          return createSolution(attempt);
        }
      }
    }
    return attempt;
  }

  public static int[] possibleValues(int[][] puzzle, int X, int Y) {
    int[] dud = new int[0];
    if (puzzle[X][Y] != 0) {
      return dud;
    }
    int[] numsX = puzzle[X];
    // System.out.println("These are the values for the row observing row X = "
    // + X + ": " + Arrays.toString(numsX));
    // promptEnterKey();
    int[] numsY = new int[9];
    for (int i = 0; i < 9; i++) {
      if (puzzle[i][Y] != 0) {
        numsY[i] = puzzle[i][Y];
      }
    }
    // System.out.println("These are the values for the column observing column Y = "
    // + Y + ": " + Arrays.toString(numsY));
    // promptEnterKey();
    int[] numsBox = new int[9];
    int boxLocationX = X / 3;
    int boxLocationY = Y / 3;
    for (int i = 0; i < 3; i++) {
      if (puzzle[(boxLocationX * 3) + i][(boxLocationY * 3)] != 0) {
        numsBox[i * 3] = puzzle[(boxLocationX * 3) + i][(boxLocationY * 3)];
      }
      if (puzzle[(boxLocationX * 3) + i][(boxLocationY * 3) + 1] != 0) {
        numsBox[(i * 3) + 1] =
            puzzle[(boxLocationX * 3) + i][(boxLocationY * 3) + 1];
      }
      if (puzzle[(boxLocationX * 3) + i][(boxLocationY * 3) + 2] != 0) {
        numsBox[(i * 3) + 2] =
            puzzle[(boxLocationX * 3) + i][(boxLocationY * 3) + 2];
      }
    }
    // System.out.println("These are the values for the box observing the box for coordinates ("
    // + X + "," + Y + "): " + Arrays.toString(numsBox));
    // promptEnterKey();
    int[] possibleValues = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    for (int i = 0; i < 9; i++) {
      if (numsX[i] != 0) {
        possibleValues[numsX[i] - 1] = 0;
      }
      if (numsY[i] != 0) {
        possibleValues[numsY[i] - 1] = 0;
      }
      if (numsBox[i] != 0) {
        possibleValues[numsBox[i] - 1] = 0;
      }
    }
    // System.out.println("These are the possible values: " +
    // Arrays.toString(possibleValues));
    // promptEnterKey();
    int count = 0;
    for (int i = 0; i < 9; i++) {
      if (possibleValues[i] != 0) {
        count++;
      }
    }
    int[] finalValues = new int[count];
    count = 0;
    for (int i = 0; i < 9; i++) {
      if (possibleValues[i] != 0) {
        finalValues[count] = possibleValues[i];
        count++;
      }
    }
    // System.out.println("These are the final possible values without zeroes: "
    // + Arrays.toString(finalValues));
    // promptEnterKey();
    return finalValues;
  }

}
