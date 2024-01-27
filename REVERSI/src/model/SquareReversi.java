package model;

import controller.ModelFeatures;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A model of a game of Square Reversi.
 */
public class SquareReversi extends AbstractReversiModel {

  //the game board in the reversi game
  private List<List<SquareCell>> gameBoard;

  public SquareReversi() {
    super();
    this.gameBoard = new ArrayList<>();
  }




  /**
   * starts a game of reversi.
   * @param sideLength  desired length of a side of the square game board.
   */
  @Override
  public void startGame(int sideLength) {
    if (sideLength <= 2) {
      throw new IllegalArgumentException("board is too small to play");
    }
    if ((sideLength % 2) != 0) {
      throw new IllegalArgumentException("side length must be even");
    }
    //adjusts the start game field to true
    this.gameStarted = true;
    //creates a square board of the given side length (all empty cells)
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
      for (List<SquareCell> list : gameBoard) {
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
      SquareCell cellAtPosn = null;
      for (List<SquareCell> list : gameBoard) {
        for (SquareCell cell : list) {
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
      List<SquareCell> empties = new ArrayList<>();
      boolean pass = true;
      for (List<SquareCell> list : gameBoard) {
        for (SquareCell cell : list) {
          if (cell.isEmpty()) {
            empties.add(cell);
          }
        }
      }
      //checks if there are any valid moves to be made in the empty cells
      for (SquareCell emptyCell : empties) {
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
    List<Cell> row = this.getRow(diagonalPos);
    List<Cell> column = this.getColumn(rowPos);
    List<Cell> diagonal1 = this.getDiagonal1(diagonalPos, rowPos);
    List<Cell> diagonal2 = this.getDiagonal2(diagonalPos, rowPos);
    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int columnIndex = this.getListIndex(column, diagonalPos, rowPos);
    int diagonal1Index = this.getListIndex(diagonal1, diagonalPos, rowPos);
    int diagonal2Index = this.getListIndex(diagonal2, diagonalPos, rowPos);
    return (this.checkValidHelper(row, rowIndex)
        || this.checkValidHelper(column, columnIndex)
        || this.checkValidHelper(diagonal1, diagonal1Index)
        || this.checkValidHelper(diagonal2, diagonal2Index));
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
    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
        if (cell.toString().equals(player.toString())) {
          counter = counter + 1;
        }
      }
    }
    return counter;
  }


  /**
   * returns the side length of the reversi board, in number of square cells.
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
    //gets the four rows of a reversi game board
    List<Cell> row = this.getRow(diagonalPos);
    List<Cell> column = this.getColumn(rowPos);
    List<Cell> diagonal1 = this.getDiagonal1(diagonalPos, rowPos);
    List<Cell> diagonal2 = this.getDiagonal2(diagonalPos, rowPos);
    //the index of the cell in each of the rows
    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int columnIndex = this.getListIndex(column, diagonalPos, rowPos);
    int diagonal1Index = this.getListIndex(diagonal1, diagonalPos, rowPos);
    int diagonal2Index = this.getListIndex(diagonal2, diagonalPos, rowPos);
    //the number of pieces from each list that would be turned over
    int pieces1 = this.countAllHelper(row, rowIndex);
    int pieces2 = this.countAllHelper(column, columnIndex);
    int pieces3 = this.countAllHelper(diagonal1, diagonal1Index);
    int pieces4 = this.countAllHelper(diagonal2, diagonal2Index);
    return pieces1 + pieces2 + pieces3 + pieces4;
  }





  /**
   * determines if the given position is in the corner of the board.
   * note: it is in the corner if the coordinates are:
   * (0,0), (0, side length), (side length, 0), or (side length, side length).
   * @param diagonalPos the x position of the cell.
   * @param rowPos the y position of the cell.
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
    int edgeCoord = getBoardSideLength() - 1;
    boolean inCorner1 = (diagonalPos == 0) && (rowPos == 0);
    boolean inCorner2 = (diagonalPos == 0) && (rowPos == edgeCoord);
    boolean inCorner3 = (diagonalPos == edgeCoord) && (rowPos == 0);
    boolean inCorner4 = (diagonalPos == edgeCoord) && (rowPos == edgeCoord);
    return inCorner1 || inCorner2 || inCorner3 || inCorner4;
  }



  @Override
  public boolean isSquareReversi() {
    return true;
  }







  //HELPER METHODS FOR START GAME:
  /**
   * creates a square game board as a 2d array with the size of the given side length.
   * all cells are initialized as empty except the initial 4 at the center.
   * assigned diagonal and row coordinates to each cell.
   * @param sideLength desired length of a side of the square game board.
   */
  protected void createDefaultGameBoard(int sideLength) {
    List<List<SquareCell>> gameBoard = new ArrayList<>();

    for (int i = 0; i < sideLength; i++) {
      List<SquareCell> row = new ArrayList<>();
      for (int j = 0; j < sideLength; j++) {
        row.add(new SquareCell(j, i));
      }
      gameBoard.add(row);
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
    gameBoard.get((sideLength / 2) - 1).get((sideLength / 2) - 1).changeCellState(PlayerEnum.X);

    // top right O
    gameBoard.get((sideLength / 2) - 1).get(sideLength / 2).changeCellState(PlayerEnum.O);

    // bottom left O
    gameBoard.get(sideLength / 2).get((sideLength / 2) - 1).changeCellState(PlayerEnum.O);

    // bottom right X
    gameBoard.get(sideLength / 2).get(sideLength / 2).changeCellState(PlayerEnum.X);

  }




  /**
   * returns a list of cells from the game board in the same column.
   * @param diagonalPos the targeted X Value.
   * @return a list of cells from the game board with the given diagonal(X) position.
   */
  private List<Cell> getRow(int diagonalPos) {
    List<Cell> row = new ArrayList<Cell>();
    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
        if (cell.getDiagonalPos() == diagonalPos) {
          row.add(cell);
        }
      }
    }
    return row;
  }


  /**
   * returns a list of cells from the game board in the same row.
   * @param rowPos the targeted Y Value.
   * @return a list of cells from the game board with the given row(Y) position.
   */
  private List<Cell> getColumn(int rowPos) {
    List<Cell> column = new ArrayList<Cell>();
    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
        if (cell.getRowPos() == rowPos) {
          column.add(cell);
        }
      }
    }
    return column;
  }


  /**
   * returns a list of cells from the game board in the same diagonal row from
   * the top left of the board to the bottom right.
   * @param diagonalPos X coordinate of the cell.
   * @param rowPos Y coordinage of the cell.
   * @return a list of cells from the board in the same diagonal.
   */
  private List<Cell> getDiagonal1(int diagonalPos, int rowPos) {
    List<Cell> diagonal = new ArrayList<>();
    int diagonalVal = rowPos + diagonalPos;
    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
        if ((cell.getRowPos() + cell.getDiagonalPos()) == diagonalVal) {
          diagonal.add(cell);
        }
      }
    }
    return diagonal;
  }



  /**
   * returns a list of cells from the game board in the same diagonal row from
   * the top right of the board to the bottom left.
   * @param diagonalPos X coordinate of the cell.
   * @param rowPos Y coordinage of the cell.
   * @return a list of cells from the board in the same diagonal.
   */
  private List<Cell> getDiagonal2(int diagonalPos, int rowPos) {
    List<Cell> diagonal = new ArrayList<>();
    int diagonalVal = diagonalPos - rowPos;
    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
        if ((cell.getDiagonalPos() - cell.getRowPos()) == diagonalVal) {
          diagonal.add(cell);
        }
      }
    }
    return diagonal;
  }





  /**
   * turns over all opposing cells that are sandwiched in a valid move.
   * does NOT add a piece to the index, the cell at the index remains empty!!!!
   * @param diagonalPos the diagonal position of the placed piece.
   * @param rowPos the row position of the placed piece.
   */
  protected void turnOverAllValid(int diagonalPos, int rowPos) {
    List<Cell> row = this.getRow(diagonalPos);
    List<Cell> column = this.getColumn(rowPos);
    List<Cell> diagonal1 = this.getDiagonal1(diagonalPos, rowPos);
    List<Cell> diagonal2 = this.getDiagonal2(diagonalPos, rowPos);

    int rowIndex = this.getListIndex(row, diagonalPos, rowPos);
    int columnIndex = this.getListIndex(column, diagonalPos, rowPos);
    int diagonal1Index = this.getListIndex(diagonal1, diagonalPos, rowPos);
    int diagonal2Index = this.getListIndex(diagonal2, diagonalPos, rowPos);

    this.turnOverOpposites(row, rowIndex);
    this.turnOverOpposites(column, columnIndex);
    this.turnOverOpposites(diagonal1, diagonal1Index);
    this.turnOverOpposites(diagonal2, diagonal2Index);
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

    for (List<SquareCell> list : gameBoard) {
      for (SquareCell cell : list) {
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
    for (List<SquareCell> list: this.gameBoard) {
      for (SquareCell cell : list) {
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
    List<SquareCell> nonEmpties = new ArrayList<>();
    for (List<SquareCell> list: this.gameBoard) {
      for (SquareCell cell : list) {
        if (!cell.isEmpty()) {
          nonEmpties.add(cell);
        }
      }
    }
    //determine if all the non-empty cells are occupied by the same player.
    String state = nonEmpties.get(0).toString();
    for (SquareCell occupiedCell : nonEmpties) {
      if (!occupiedCell.toString().equals(state)) {
        sameColor = false;
      }
    }
    return sameColor;
  }





  //HELPER FOR POSN NEXT TO CORNER


  /**
   * determines if two positions on the square reversi game board are next to each other.
   * @param q1 the x coordinate of the first cell.
   * @param r1 the y coordinate of the first cell.
   * @param q2 the x coordinate of the second cell.
   * @param r2 the y coordinate of the second cell.
   * @return true if the first and second cell are next to each other on the game board.
   */
  protected boolean nextTo(int q1, int r1, int q2, int r2) {
    int d1 = Math.abs(q1 - q2);
    int d2 = Math.abs(r1 - r2);
    return d1 <= 1 && d2 <= 1;
  }



  //HELPER FOR POSN IN CORNER

  protected List<Cell> getCorners() {
    //gets all the cells that are corners on the game board
    List<Cell> corners = new ArrayList<>();
    ArrayList<ArrayList<Cell>> board = this.getGameBoard();
    for (ArrayList<Cell> list : board) {
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
