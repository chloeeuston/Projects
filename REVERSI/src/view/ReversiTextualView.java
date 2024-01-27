package view;


import java.util.ArrayList;
import model.Cell;
import model.ReversiModel;

/**
 * a class to represent the textual view of a reversi game.
 */
public class ReversiTextualView implements ReversiView {
  private final ReversiModel model;

  public ReversiTextualView(ReversiModel model) {
    this.model = model;
  }


  /**
   * Represents the board of the model as a String.
   * @return a String representation of the current model.
   */
  @Override
  public String toString() {
    String printGameBoard = "";
    ArrayList<ArrayList<Cell>> gameBoard = model.getGameBoard();
    for (int i = 0; i < gameBoard.size(); i++) {
      String lineBase = "";

      int currentRowLength = gameBoard.get(i).size();
      int halfwayRowSize = gameBoard.get((gameBoard.size() - 1) / 2).size();
      int extraSpaces = halfwayRowSize - currentRowLength;

      for (int j = 0; j < extraSpaces; j++) {
        lineBase = lineBase + " ";
      }

      for (int j = 0; j < gameBoard.get(i).size(); j++) {
        lineBase = lineBase + gameBoard.get(i).get(j) + " "; // replace with toString()
      }

      // Add this new line to the board and enter to new line.
      // Add new line even for last line, since a new line will be needed in the system.
      printGameBoard = printGameBoard + lineBase + "\n";
    }
    return printGameBoard;
  }

  /**
   * Print the Board of the model at the given state.
   */
  public void printBoard() {
    System.out.print(this.toString());
  }

}
