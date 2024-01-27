package model;

import controller.ModelFeatures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * a class that holds information and methods in a reversi game.
 */
public class BasicReversi extends AbstractReversiModel {

  private List<List<HexagonCell>> gameBoard;
  //the player whose turn it is

  /**
   * constructs a game of reversi.
   */
  public BasicReversi() {
    super();
    this.gameBoard = new ArrayList<>();
  }



  /**
   * starts a game of reversi.
   * @param sideLength  desired length of a side of the hexagonal game board.
   */
  @Override
  public void startGame(int sideLength) {
    if (sideLength <= 2) {
      throw new IllegalArgumentException("board is too small to play");
    }
    //adjusts the start game field to true
    this.gameStarted = true;
    //creates a hexagonal board of the given side length (all empty cells)
    this.createDefaultGameBoard(sideLength);
    //adds the starting pieces to the game board
    this.assignInitialPieces(sideLength);
    for (ModelFeatures features : featuresListener) {
      features.notifyPlayerTurn();
    }
    for (ModelFeatures features : featuresListener) {
      features.makeMoveIfAI();
    }
  }



  /**
   * returns the current game board in a reversi game.
   * @return the current game board in this reversi game.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public ArrayList<ArrayList<Cell>> getGameBoard() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    else {
      ArrayList<ArrayList<Cell>> board = new ArrayList<>();
      for (List<HexagonCell> list : gameBoard) {
        ArrayList<Cell> row = new ArrayList<>(list);
        board.add(row);
      }
      return board;
    }
  }



  /**
   * returns the cell on the game board at the given position.
   * @param diagonalPos the diagonal position of the targeted cell.
   * @param rowPos the row position of the targeted cell.
   * @return the cell in the game board with the given coordinated.
   * @throws IllegalArgumentException if the game board does not contain a
   *     cell at this position.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public Cell getCellAtPosn(int diagonalPos, int rowPos) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    else {
      //cellAtPosn will never return as null because it will always be updated since
      //it is guarantees that this position exists in the game board (exception above).
      HexagonCell cellAtPosn = null;
      for (List<HexagonCell> list : gameBoard) {
        for (HexagonCell cell : list) {
          if (cell.getDiagonalPos() == diagonalPos && cell.getRowPos() == rowPos) {
            cellAtPosn = cell;
          }
        }
      }
      return cellAtPosn;
    }
  }




  /**
   * determines if a player must have to make a forced pass.
   * @return true if there are no valid moves in any of the empty cells for the current player.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public boolean forcedPass() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    //filters the game board to get only the empty cells
    else {
      List<HexagonCell> empties = new ArrayList<>();
      boolean pass = true;
      for (List<HexagonCell> list : gameBoard) {
        for (HexagonCell cell : list) {
          if (cell.isEmpty()) {
            empties.add(cell);
          }
        }
      }
      //checks if there are any valid moves to be made in the empty cells
      for (HexagonCell emptyCell : empties) {
        int cellDiagonalPos = emptyCell.getDiagonalPos();
        int cellRowPos = emptyCell.getRowPos();
        if (this.checkValid(cellDiagonalPos, cellRowPos)) {
          pass = false;
        }
      }
      return pass;
    }
  }




  /**
   * checks if a player move to a given cell position is valid.
   * @param diagonalPos the diagonal position that they are playing a piece on.
   * @param rowPos the row position that they are playing a piece on.
   * @return true if the move is valid (if they sandwich at least one of the other pieces).
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the coordinates are not in the game.
   */
  @Override
  public boolean checkValid(int diagonalPos, int rowPos) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    if (!this.getCellAtPosn(diagonalPos,rowPos).isEmpty()) {
      return false;
    }
    List<Cell> row = this.getRow(rowPos);
    List<Cell> qDiagonal = this.getQDiagonals(diagonalPos);
    List<Cell> sDiagonal = this.getSDiagonals(diagonalPos, rowPos);
    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int qIndex = this.getListIndex(qDiagonal, diagonalPos, rowPos);
    int sIndex = this.getListIndex(sDiagonal, diagonalPos, rowPos);
    return (this.checkValidHelper(row, rowIndex)
        || this.checkValidHelper(qDiagonal, qIndex)
        || this.checkValidHelper(sDiagonal, sIndex));
  }



  /**
   * gets a player's current score, which is the number of cells occupied by a given player.
   * @param player the player whose cell are being counted.
   * @return the current score of a given player.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public int getPlayerScore(PlayerEnum player) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    int counter = 0;
    for (List<HexagonCell> list : gameBoard) {
      for (HexagonCell cell : list) {
        if (cell.toString().equals(player.toString())) {
          counter = counter + 1;
        }
      }
    }
    return counter;
  }


  /**
   * returns the side length of the reversi board, in number of hexagons.
   * @return side length of a reversi board.
   * @throws IllegalStateException if the game has ot been started.
   */
  @Override
  public int getBoardSideLength() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    return gameBoard.get(0).size();
  }


  /**
   * counts all the pieces that WOULD be turned over if a player were to move at this cell.
   * @param diagonalPos the diagonal position of the cell.
   * @param rowPos the row position of the cell.
   * @return the number of pieces that a player would gain from this move.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the coordinates are not in the game.
   */
  @Override
  public int countPiecesGained(int diagonalPos, int rowPos) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    //gets the three rows of a reversi game board (row and both diagonals)
    List<Cell> row = this.getRow(rowPos);
    List<Cell> qDiagonal = this.getQDiagonals(diagonalPos);
    List<Cell> sDiagonal = this.getSDiagonals(diagonalPos, rowPos);
    //the index of the cell in each of the rows
    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int qIndex = this.getListIndex(qDiagonal, diagonalPos, rowPos);
    int sIndex = this.getListIndex(sDiagonal, diagonalPos, rowPos);
    //the number of pieces from each list that would be turned over
    int pieces1 = this.countAllHelper(row, rowIndex);
    int pieces2 = this.countAllHelper(qDiagonal, qIndex);
    int pieces3 = this.countAllHelper(sDiagonal, sIndex);
    return pieces1 + pieces2 + pieces3;
  }





  /**
   * determines if the given position is in the corner of the board.
   * note: it is in the corner if the q,r,s coordinates are equal to 0, (side length - 1),
   * and -(side length - 1).
   * @param diagonalPos the diagonal position of the cell.
   * @param rowPos the row position of the cell.
   * @return true if the position given is at the corner of the reversi board.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the coordinates are not in the game.
   */
  @Override
  public boolean posnInCorner(int diagonalPos, int rowPos) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    int sCoord = (- diagonalPos) - rowPos;
    //adds the q, r, and s coordinates to a list
    List<Integer> coords = new ArrayList<>(Arrays.asList(sCoord, diagonalPos, rowPos));
    int boardSideLength = this.getBoardSideLength();
    //checks that the list contains the coordinates it should
    return (coords.contains(0) && coords.contains(boardSideLength - 1)
        && coords.contains(- (boardSideLength - 1)));
  }



  @Override
  public boolean isSquareReversi() {
    return false;
  }







  //HELPER METHODS FOR START GAME:
  /**
   * creates a hexagonal game board as a 2d array with the size of the given side length.
   * all cells are initialized as empty except the initial 6 around the center.
   * assigned diagonal and row coordinates to each cell.
   * @param sideLength desired length of a side of the hexagonal game board.
   */
  protected void createDefaultGameBoard(int sideLength) {
    List<List<HexagonCell>> gameBoard = new ArrayList<>();

    //the length of the current row (going from top to bottom)
    int currentRowLength = sideLength;
    //the number of total rows, from top to bottom, in the hexagonal board
    int numRows = (2 * sideLength) - 1;
    //the index at which the length of the row is the largest, the middle row
    int halfwayRowIndex = sideLength - 1;

    for (int i = 0; i < numRows; i++) {
      List<HexagonCell> row = new ArrayList<>();

      for (int k = 0; k < currentRowLength; k++) {

        int diagonalPos;
        int rowPos;

        //before the halfway row in the hexagon, the diagonal pos is equal to the
        //index of the cell minus the index of the row it is in.
        if (i < halfwayRowIndex) {
          diagonalPos = k - i;
        }
        //after/at the halfway row in the hexagon, the diagonal pos is equal to the
        //negative of the halfway row index plus the index of the cell
        else {
          diagonalPos = -halfwayRowIndex + k;
        }

        //the row position is the negative of the halfway row index plus the index of
        //the row it is in.
        rowPos = -halfwayRowIndex + i;

        row.add(new HexagonCell(diagonalPos, rowPos)); // replace with new Cell()
      }

      gameBoard.add(row);

      if (i >= halfwayRowIndex) {
        currentRowLength--;
      } else {
        currentRowLength++;
      }
    }
    this.gameBoard = gameBoard;
  }


  /**
   * assigns the initial game pieces to each player, where the pieces are around
   * the center peice, alternating order.
   * @param sideLength the length of the board.
   */
  protected void assignInitialPieces(int sideLength) {
    // top left X
    gameBoard.get(sideLength - 2).get(sideLength - 2).changeCellState(PlayerEnum.X);
    // top right O
    gameBoard.get(sideLength - 2).get(sideLength - 1).changeCellState(PlayerEnum.O);
    // mid left O
    gameBoard.get(sideLength - 1).get(sideLength - 2).changeCellState(PlayerEnum.O);
    // mid right X
    gameBoard.get(sideLength - 1).get(sideLength).changeCellState(PlayerEnum.X);
    // bottom left X
    gameBoard.get(sideLength).get(sideLength - 2).changeCellState(PlayerEnum.X);
    // bottom right O
    gameBoard.get(sideLength).get(sideLength - 1).changeCellState(PlayerEnum.O);

  }




  /**
   * returns a list of cells from the game board in the same q diagonal (top left to bottom right).
   * @param diagonalPos the targeted diagonal line.
   * @return a list of cells from the game board with the given diagonal position.
   */
  private List<Cell> getQDiagonals(int diagonalPos) {
    List<Cell> diagonals = new ArrayList<Cell>();
    for (List<HexagonCell> list : gameBoard) {
      for (HexagonCell cell : list) {
        if (cell.getDiagonalPos() == diagonalPos) {
          diagonals.add(cell);
        }
      }
    }
    return diagonals;
  }

  /**
   * returns a list of cells from the game board in the same S diagonal (top right to bottom left).
   * @param diagonalPos the diagonal pos of a cell.
   * @param rowPos the row pos of a cell.
   * @return all the cells in the game board with the same S value as the inputted cell pos.
   */
  private List<Cell> getSDiagonals(int diagonalPos, int rowPos) {
    List<Cell> sDiagonals = new ArrayList<Cell>();
    int sValue = (- diagonalPos) - rowPos;
    for (List<HexagonCell> list : gameBoard) {
      for (HexagonCell cell : list) {
        if (cell.getSVal() == sValue) {
          sDiagonals.add(cell);
        }
      }
    }
    Collections.reverse(sDiagonals);
    return sDiagonals;
  }

  /**
   * returns all the cells in the game board in the same given row.
   * @param rowPos the targeted row position.
   * @return the cells in the game board with the given row position.
   */
  private List<Cell> getRow(int rowPos) {
    List<Cell> row = new ArrayList<Cell>();
    for (List<HexagonCell> list : gameBoard) {
      for (Cell cell : list) {
        if (cell.getRowPos() == rowPos) {
          row.add(cell);
        }
      }
    }
    return row;
  }





  /**
   * turns over all opposing cells that are sandwiched in a valid move.
   * does NOT add a piece to the index, the cell at the index remains empty!!!!
   * @param diagonalPos the diagonal position of the placed piece.
   * @param rowPos the row position of the placed piece.
   */
  protected void turnOverAllValid(int diagonalPos, int rowPos) {
    List<Cell> row = this.getRow(rowPos);
    List<Cell> qDiagonal = this.getQDiagonals(diagonalPos);
    List<Cell> sDiagonal = this.getSDiagonals(diagonalPos, rowPos);
    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int qIndex = this.getListIndex(qDiagonal, diagonalPos, rowPos);
    int sIndex = this.getListIndex(sDiagonal, diagonalPos, rowPos);
    this.turnOverOpposites(row, rowIndex);
    this.turnOverOpposites(qDiagonal, qIndex);
    this.turnOverOpposites(sDiagonal, sIndex);
  }



  /**
   * determines if the given position is a position on the game board.
   * @param diagonalPos the diagonal position.
   * @param rowPos the row position.
   * @return true if the position is a position on the game board.
   */
  protected boolean posnNotInGame(int diagonalPos, int rowPos) {
    List<Integer> diagonalPosns = new ArrayList<>();
    List<Integer> rowPosns = new ArrayList<>();

    for (List<HexagonCell> list : gameBoard) {
      for (HexagonCell cell : list) {
        diagonalPosns.add(cell.getDiagonalPos());
        rowPosns.add(cell.getRowPos());
      }
    }
    return (!diagonalPosns.contains(diagonalPos) || !rowPosns.contains(rowPos));
  }





  //HELPER METHODS FOR IS GAME OVER:


  /**
   * Determines if the board is full.
   * @return return true if there are no empty cells.
   */
  protected boolean isBoardFull() {
    boolean full = true;
    for (List<HexagonCell> list: this.gameBoard) {
      for (HexagonCell cell : list) {
        if (cell.isEmpty()) {
          full = false;
        }
      }
    }
    return full;
  }


  /**
   * determines if all the occupied cells are the same color.
   * @return true if all the occupied cells have the same cell state.
   */
  protected boolean monochromeCells() {
    boolean sameColor = true;
    //get all the non-empty cells in the game board.
    List<HexagonCell> nonEmpties = new ArrayList<>();
    for (List<HexagonCell> list: this.gameBoard) {
      for (HexagonCell cell : list) {
        if (!cell.isEmpty()) {
          nonEmpties.add(cell);
        }
      }
    }
    //determine if all the non-empty cells are occupied by the same player.
    String state = nonEmpties.get(0).toString();
    for (HexagonCell occupiedCell : nonEmpties) {
      if (!occupiedCell.toString().equals(state)) {
        sameColor = false;
      }
    }
    return sameColor;
  }





  //HELPER FOR POSN NEXT TO CORNER


  /**
   * determines if two axial positions on the reversi game board are next to each other.
   * note: two cells are next to each other if the sum of the absolute value of the
   *      difference between their coordinates is equal to 2.
   * @param q1 the q diagonal coordinate of the first cell.
   * @param r1 the r row coordinate of the first cell.
   * @param q2 the q diagonal coordinate of the second cell.
   * @param r2 the r row coordinate of the second cell.
   * @return true if the first and second cell are next to each other on the game board.
   */
  protected boolean nextTo(int q1, int r1, int q2, int r2) {
    int s1 = - q1 - r1;
    int s2 = - q2 - r2;
    int difference = Math.abs(q1 - q2) + Math.abs(r1 - r2) + Math.abs(s1 - s2);
    return difference == 2;
  }



  //HELPER FOR POSN IN CORNER

  protected List<Cell> getCorners() {
    //gets all the cells that are corners on the game board
    List<Cell> corners = new ArrayList<>();
    ArrayList<ArrayList<Cell>> board = this.getGameBoard();
    for (List<Cell> list : board) {
      for (Cell cell : list) {
        int q = cell.getDiagonalPos();
        int r = cell.getRowPos();
        if (this.posnInCorner(q, r)) {
          corners.add(cell);
        }
      }
    }
    return corners;
  }


  /**
   * Create a unique number per object that exists.
   * Required method for any object.
   * @return a unique int for each object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(gameStarted, gameBoard, playerTurn, turnsTaken);
  }

  /**
   * Does this equal other? check.
   * Required method for any object.
   * @param other represents the object being compared.
   * @return true if they are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof BasicReversi) {
      return this.equals(other);
    }
    return false;
  }

}
