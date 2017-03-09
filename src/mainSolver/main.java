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

/**Class main 
 * Author: VuongRon
 *
 * The executable class of Sudoku-solver, with a main method. The Main class encompasses all methods of the Sudoku-solver application
 * 
 */
public class main {
	
	/**
	*@brief Static method promptEnterKey, prompts user to press the enter button
	*@details Method takes no parameters, it records user input, after prompting user to press ENTER button to continue application
	*
	*/
  public static void promptEnterKey() {
	System.out.println("Press \"ENTER\" to continue...");
	//Instantiate scanner object to read user input
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();
  }
  /**
	*@brief Static method infoBox, creates a graphical object frame, for users to interact with
	*@param String infoMessage represents the application's message response to user
	*@param String titleBar represents the title of the gui frame
	*@details This method is called to construct a JOptionPane object in order, to communicate application's output (messages and solutions) to user
	*
	*/
  public static void infoBox(String infoMessage, String titleBar) {
   //Displays message dialog
	  JOptionPane.showMessageDialog(null, infoMessage, titleBar,
        JOptionPane.WARNING_MESSAGE);
  }
  /**
	*@brief main method to be called by the interpreter to compile and execute Java project
	*@param String array, part of main syntax and default input from console
	*@details main method calls methods that record user input (incomplete sudoku puzzle) and facilitates the computation of a valid solution for said puzzle
	*
	*
	*/
  public static void main(String args[]) {
	 //Instantiating an empty two dimensional array that will represent the solved array
	  //Instantiating solved of type boolean, that is used as a flag variable to indicate whether the application has found (true) the solution to input puzzle
    int[][] solvedPuzzle = new int[9][9];
    boolean solved = false;
    //Calling method createPuzzle to input an incomplete sudoku puzzle and storing it in two dimensional  integer array puzzle
    int[][] puzzle = createPuzzle();
    // for (int x = 0; x < 9; x++) {
    // System.out.println(Arrays.toString(puzzle[x]));
    // }
    while (!(solved)) { //solved=false, loop body is executed
     //method call createSolution computes a soltion for array puzzle and returns solution to solvedPuzzle, which is then passed as paramater to method call checkRules, which checks the validty of solution
    solvedPuzzle = createSolution(puzzle);
      solved = checkRules(solvedPuzzle);
    }
    //Leaves loop body once solved=true, prints out final solution using infoBox
    String finalSolution = "";
    for (int x = 0; x < 9; x++) {
      finalSolution += Arrays.toString(solvedPuzzle[x]) + "\n";
    }
    infoBox(finalSolution, "Solution");
    System.exit(0); //Program terminates


  }
  /**
	*@brief Static method createPuzzle facilitates storage of an incomplete sudoku puzzle as input, by parsing through a text file
	*@details createPuzzle method uses FileDialog library and allow the user to input a text file saved in the system, to submit a sudoku puzzle, for the application to solve. The class includes a parser, to convert puzzle values into a representation that can be used by the application.
	*The text files need to store information in a certain format. The 9*9 grids are represented by nine rows and non column of integer numbers. 0's represent empty spaces, while numbers 1-9 represents values necessary to compute solution  
	*@return Integer array that represents the initial sudoku puzzle to be solved
	*
	*/
  public static int[][] createPuzzle() {

  
	int[][] puzzle = new int[9][9];
	
	//FileDialog object openfile is used to allow users to navigate their system and select a text file for input
    String line = null;
    Frame frame = new Frame();
    FileDialog openfile =
        new FileDialog(frame, "Select a file", FileDialog.LOAD);
    openfile.setVisible(true);
    //Selected files contents are stored in String object file.
    String file = openfile.getDirectory() + File.separator + openfile.getFile();

    try {
    	//Code block that parses through file and isolates values for each integer element in the 9x9 grid
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      int row = 0;
      while ((line = bufferedReader.readLine()) != null) { //Reads each line in file and leaves loop when it encounters end of file
    	  //Splits at whitespaces and stores each segment into an array
        String[] items = line.split(" ");
        int[] values = new int[items.length];
        //For loop goes through each string element of array items and converts it into integer values, before sotring it into an integer array values.
        for (int i = 0; i < items.length; i++) {
          values[i] = Integer.parseInt(items[i]);
        }
        for (int i = 0; i < values.length; i++) {
          //inputs values into puzzle array for that specific row
        	puzzle[row][i] = values[i];
        }
        //The above body of the while loop is continuouslty executed, row by row until all rows of puzzle 2D array are filled with integer values
        row++; 
      }

      // Always close files.
      bufferedReader.close();
    } catch (FileNotFoundException ex) { //Error handling, for files that can't be opened and for exception encountered while parsing throught the contents of the file
      System.out.println("Unable to open file '" + file + "'");
    } catch (IOException ex) {
      System.out.println("Error reading file '" + file + "'");
      // Or we could just do this:
      // ex.printStackTrace();
    }
    //returns integer 2D array to main (source method call)
    return puzzle;

  }
  /**
	*@brief Static method arrayCheck, checks arrays passed as parameters for equivalence
	*@param One dimensional integer arrays X and Y
	*@details Method sorts array X and Y, and returns a boolean value based on whether the arrays are equal or not
	*@return boolean value which represents if two sorted arrays are equal (true) or not (false)
	*
	*/
  public static boolean arrayCheck(int[] X, int[] Y) {
	 //Use sort method from Arrays library, to sort the array X
    Arrays.sort(X);
    for (int i = 0; i < 9; i++) { //Goes through all elements of X and Y
      if (X[i] != Y[i]) {
        return false;  //Not equal
      }
    }
    return true; //Equal
  }
  /**
	*@brief Static method checkRules, checks the validity of the solution of a sudoku puzzle, as determined by the application
	*@param Two dimensional integer array puzzle, which represents the completed sudoku puzzle
	*@details This method analyzes each row and each column to ensure that the solution computed by the application is valid. A valid row/column should have all the numbers between 1 and 9. In a square grid no number should be repeating and no number should be clashing. If the solution has the above , the solution is not valid and the puzzle is unsolvable
	*@return boolean value, where true represents a valid sudoku solution and false represents an invalid sudoku solution
	*
	*/
  public static boolean checkRules(int[][] puzzle) { 
	  //One dimensional integer array finalValues represents the values a row/column should have
    int[] finalValues = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    for (int i = 0; i < 9; i++) { //Loop for row analysis
    	
      int[] temp = new int[9];
      //Copying contents of row from puzzle to array temp
      System.arraycopy(puzzle[i], 0, temp, 0, puzzle[i].length);

      int[] temp2 = new int[9];
      //Calls method arrayCheck to check the sorted contents of temp against final values,
      if (!(arrayCheck(temp, finalValues))) {
        // System.out.println("Row failure at row " + i + ": "
        // + Arrays.toString(temp));
        return false; //Row analysis fails, not equivalent to finalValues
      } else {
        // System.out.println("ROW PASS at row " + i + ": "
        // + Arrays.toString(temp));
      }//Row pass
      for (int j = 0; j < 9; j++) { //Nested loop for column analysis
        temp2[j] = puzzle[j][i]; //Copying elements of column j of puzzle for some row i (predetermined by loop above), into temp2
      }
      Arrays.sort(temp2);  //Sorting array (preemptive, done again in arrayCheck), calls arrayCheck to check contents of temp2 against finalValues 

      if (!(arrayCheck(temp2, finalValues))) {
        // System.out.println("Column failure at column " + i + ": "
        // + Arrays.toString(temp2));
        return false; //Column analysis fails, not equivalent to finalValues
      } else {
        // System.out.println("COLUMN PASS at column " + i + ": "
        // + Arrays.toString(temp2));
      }//Column  pass

    }
    //The above validates the rows and columns, the next block of code, validates the individual grids, to ensure that they all have numbers between and including 1 and 9
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
    	  //Values of each box is stored in temp for each iteration of the nested loops
    	  int[] temp = new int[9];
        //First row of square gird or box
        temp[0] = puzzle[3 * i][3 * j];
        temp[1] = puzzle[3 * i][(3 * j) + 1];
        temp[2] = puzzle[3 * i][(3 * j) + 2];
      //Second row of square gird or box
        temp[3] = puzzle[(3 * i) + 1][3 * j];
        temp[4] = puzzle[(3 * i) + 1][(3 * j) + 1];
        temp[5] = puzzle[(3 * i) + 1][(3 * j) + 2];
      //Third row of square gird or box
        temp[6] = puzzle[(3 * i) + 2][3 * j];
        temp[7] = puzzle[(3 * i) + 2][(3 * j) + 1];
        temp[8] = puzzle[(3 * i) + 2][(3 * j) + 2];

        Arrays.sort(temp);
        //calls arrayCheck to check contents of temp against finalValues
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
  /**
	*@brief Static method createSolution computes the solution for the input sudoku puzzle
	*@details 
	*@return Integer array with some/all solutions to a sudoku puzzle
	*
	*
	*/
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
  /**
	*@brief Static method possibleValues determines what possible values can be used to fill a square in the sudoku puzzle
	*@details
	*@return integer array with all possible values
	*
	*/
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
