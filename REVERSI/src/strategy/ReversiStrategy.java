package strategy;

import model.Cell;
import java.util.ArrayList;
import model.Player;
import model.ReadOnlyReversiModel;


/**
 * An abstract class representing the basic Strategy with methods used to differentiate
 * between coordinate options.
 */
abstract class ReversiStrategy implements Strategy {

  /**
   * picks the coordinate that a player should move to.
   * @param model the model of a reversi game.
   * @param player the player placing a piece.
   * @return the coordinate of the cell the player should move to.
   * @throws IllegalArgumentException if it is not the player's turn.
   */
  public abstract Coord chooseMove(ReadOnlyReversiModel model, Player player);


  /**
   * returns all the cells in the game board that would currently be a valid move.
   * @param model the reversi game model.
   * @return the cells in the game board that a player can move to.
   */
  ArrayList<Cell> getValidCells(ReadOnlyReversiModel model) {
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    ArrayList<Cell> valids = new ArrayList<>();
    for (ArrayList<Cell> list : board) {
      for (Cell cell : list) {
        int diagonalPos = cell.getDiagonalPos();
        int rowPos = cell.getRowPos();
        if (model.checkValid(diagonalPos, rowPos)) {
          valids.add(cell);
        }
      }
    }
    return valids;
  }


  Coord getBestCell(ReadOnlyReversiModel model, ArrayList<Cell> validCells) {
    int bestMove = 0;
    Cell bestCell = null;
    for (Cell cell : validCells) {
      //finds the number of cells a player would gain by placing a piece at this cell
      int cellsGained = model.countPiecesGained(cell.getDiagonalPos(), cell.getRowPos());
      //replaces the cell with this cell if it gains more pieces than the current best cell.
      if (cellsGained > bestMove) {
        bestMove = cellsGained;
        bestCell = cell;
      }
    }
    return new Coord(bestCell.getDiagonalPos(), bestCell.getRowPos());
  }

  ArrayList<Cell> getNoCorners(ReadOnlyReversiModel model, ArrayList<Cell> valids) {
    ArrayList<Cell> noCorners = new ArrayList<>();
    for (Cell cell : valids) {
      if (!model.posnNextToCorner(cell.getDiagonalPos(), cell.getRowPos())) {
        noCorners.add(cell);
      }
    }
    return noCorners;
  }


  ArrayList<Cell> getCorners(ReadOnlyReversiModel model, ArrayList<Cell> valids) {
    ArrayList<Cell> corners = new ArrayList<>();
    for (Cell cell : valids) {
      if (model.posnInCorner(cell.getDiagonalPos(), cell.getRowPos())) {
        corners.add(cell);
      }
    }
    return corners;
  }

}
