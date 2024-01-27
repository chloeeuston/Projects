package model;

import controller.ModelFeatures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * An abstract class of a Reversi game model that contains methods that are the same in
 * both BasicReversi (hexagonal) and SquareReversi.
 */
public abstract class AbstractReversiModel implements ReversiModel {

  //if the reversi game is started.
  protected boolean gameStarted;
  //the player whose turn it is
  protected PlayerEnum playerTurn;
  //tracks the 2 most recent moves made (true represents a player move, false
  //      represents a player pass).
  protected List<Boolean> turnsTaken;
  protected final List<ModelFeatures> featuresListener;

  /**
   * constructs a game of reversi.
   */
  public AbstractReversiModel() {
    this.gameStarted = false;
    this.playerTurn = PlayerEnum.X;
    this.turnsTaken = new ArrayList<>(Arrays.asList(true, true));
    this.featuresListener = new ArrayList<>();
  }

  /**
   * starts a game of reversi.
   * @param sideLength  desired length of a side of the hexagonal game board.
   */
  public abstract void startGame(int sideLength);


  /**
   * returns the current game board in a reversi game.
   * @return the current game board in this reversi game.
   * @throws IllegalStateException if the game has not been started.
   */
  public abstract ArrayList<ArrayList<Cell>> getGameBoard();


  /**
   * the current player's turn in a reversi game.
   * @return the curent player turn in a reversi game.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public Player getPlayerTurn() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    else {
      return this.playerTurn;
    }
  }


  /**
   * the list of the past two turns taken in the game.
   * @return the turns taken.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public List<Boolean> getTurnsTaken() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    else {
      return new ArrayList<Boolean>(this.turnsTaken);
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
  public abstract Cell getCellAtPosn(int diagonalPos, int rowPos);


  /**
   * Determines if the game is over.
   * @return true if the game is over via a full board, a winner, or two passes in a row.
   * @throws IllegalStateException if the game has not been started.
   */
  @Override
  public boolean isGameOver() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    else {
      //returns true if the board is full, if all full cells are occupied by the same
      //      player, or if the players both passed back to back
      return this.isBoardFull() || this.monochromeCells() || this.twoPasses();
    }
  }


  /**
   * determines if a player must have to make a forced pass.
   * @return true if there are no valid moves in any of the empty cells for the current player.
   * @throws IllegalStateException if the game has not been started.
   */
  public abstract boolean forcedPass();


  /**
   * the winner of a game of reversi.
   * @return whichever player has the most tiles when the game is over.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalStateException if the game is not over.
   */
  @Override
  public Player getWinner() {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (!this.isGameOver()) {
      throw new IllegalStateException("game is not over");
    }
    else {
      int playerXCells = this.getPlayerScore(PlayerEnum.X);
      int playerOCells = this.getPlayerScore(PlayerEnum.O);
      if (playerXCells > playerOCells) {
        return PlayerEnum.X;
      }
      if (playerXCells < playerOCells) {
        return PlayerEnum.O;
      }
      else {
        //return the empty player if there is a tie
        return PlayerEnum.Empty;
      }
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
  public abstract boolean checkValid(int diagonalPos, int rowPos);



  /**
   * gets a player's current score, which is the number of cells occupied by a given player.
   * @param player the player whose cell are being counted.
   * @return the current score of a given player.
   * @throws IllegalStateException if the game has not been started.
   */
  public abstract int getPlayerScore(PlayerEnum player);


  /**
   * returns the side length of the reversi board, in number of hexagons.
   * @return side length of a reversi board.
   * @throws IllegalStateException if the game has ot been started.
   */
  public abstract int getBoardSideLength();


  /**
   * counts all the pieces that WOULD be turned over if a player were to move at this cell.
   * @param diagonalPos the diagonal position of the cell.
   * @param rowPos the row position of the cell.
   * @return the number of pieces that a player would gain from this move.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the coordinates are not in the game.
   */
  public abstract int countPiecesGained(int diagonalPos, int rowPos);


  /**
   * determines if the given position is next to a corner of the board.
   * @param diagonalPos the diagonal position of the cell.
   * @param rowPos the row position of the cell.
   * @return true if the cell is next to a corner.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the coordinates are not in the game.
   */
  @Override
  public boolean posnNextToCorner(int diagonalPos, int rowPos) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    List<Cell> corners = this.getCorners();
    //sets nextTo to true if this cell is next to any of the corner cells
    boolean nextTo = false;
    for (Cell cell : corners) {
      int q = cell.getDiagonalPos();
      int r = cell.getRowPos();
      if (this.nextTo(diagonalPos, rowPos, q, r)) {
        nextTo = true;
        break;
      }
    }
    return nextTo;
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
  public abstract boolean posnInCorner(int diagonalPos, int rowPos);



  /**
   * Moves a player to the given position (adjusts the player field of the cell).
   * @param diagonalPos  diagonal position for the piece to be placed at.
   * @param rowPos  row position for the piece to be placed at.
   * @param player the player that is making the move.
   * @throws IllegalArgumentException if either positions do not exist on the board.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if the cell at the given position is not empty.
   * @throws IllegalStateException if the move is not allowable.
   * @throws IllegalArgumentException if it is not the players turn.
   */
  @Override
  public void playerMove(int diagonalPos, int rowPos, Player player) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (!player.toString().equals(playerTurn.toString())) {
      throw new IllegalArgumentException("not this player's turn");
    }
    if (this.posnNotInGame(diagonalPos, rowPos)) {
      throw new IllegalArgumentException("invalid position");
    }
    if (!this.getCellAtPosn(diagonalPos, rowPos).isEmpty()) {
      throw new IllegalStateException("must place at an empty cell");
    }
    if (!this.checkValid(diagonalPos, rowPos)) {
      throw new IllegalStateException("invalid move");
    }
    else {
      //adds a piece of the current player at the targeted cell
      this.getCellAtPosn(diagonalPos, rowPos).changeCellState(playerTurn);
      //turns over all opposing cells in a valid move
      this.turnOverAllValid(diagonalPos, rowPos);
      //switches the player turn to the next player
      this.nextPlayer();
      //updates the turnsTaken field to reflect a player move
      this.madeMove(true);
      for (ModelFeatures features : featuresListener) {
        features.update();
      }
      for (ModelFeatures features : featuresListener) {
        features.notifyPlayerTurn();
      }
      for (ModelFeatures features : featuresListener) {
        features.makeMoveIfAI();
      }
      for (ModelFeatures features : featuresListener) {
        features.displayGameOver();
      }
    }
  }


  /**
   * Execute what happens when a player passes as their turn.
   * Move to next player.
   * @param player the player that is passing.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if it is not the player's turn.
   */
  @Override
  public void playerPass(Player player) {
    if (!this.gameStarted) {
      throw new IllegalStateException("game not started");
    }
    if (!player.toString().equals(playerTurn.toString())) {
      throw new IllegalArgumentException("not this player's turn");
    }
    else {
      //updates the turnsTaken field to reflect a player pass
      this.madeMove(false);
      //switches the player turn to the next player
      this.nextPlayer();
      for (ModelFeatures features : featuresListener) {
        features.update();
      }
      for (ModelFeatures features : featuresListener) {
        features.notifyPlayerTurn();
      }
      for (ModelFeatures features : featuresListener) {
        features.makeMoveIfAI();
      }
      for (ModelFeatures features : featuresListener) {
        features.makeMoveIfAI();
      }
      for (ModelFeatures features : featuresListener) {
        features.displayGameOver();
      }
    }
  }


  /**
   * Adds a listener to the model to react to the signals the model sends.
   * @param features the listener (the controller).
   */
  @Override
  public void addFeaturesListener(ModelFeatures features) {
    this.featuresListener.add(Objects.requireNonNull(features));
  }


  public abstract boolean isSquareReversi();





  //HELPER METHODS FOR START GAME:
  /**
   * creates a hexagonal game board as a 2d array with the size of the given side length.
   * all cells are initialized as empty except the initial 6 around the center.
   * assigned diagonal and row coordinates to each cell.
   * @param sideLength desired length of a side of the hexagonal game board.
   */
  protected abstract void createDefaultGameBoard(int sideLength);


  /**
   * assigns the initial game pieces to each player, where the pieces are around
   * the center peice, alternating order.
   * @param sideLength the length of the board.
   */
  protected abstract void assignInitialPieces(int sideLength);





  //HELPER METHODS FOR CHECKING A VALID MOVE/MAKING A MOVE:
  /**
   * returns the list index of a cell with the given position.
   * @param list the list in which we are checking the index in.
   * @param diagonalPos the diagonal position of the cell.
   * @param rowPos the row position of the cell.
   * @return the index of the cell in the lists on the board.
   */
  protected int getListIndex(List<Cell> list, int diagonalPos, int rowPos) {
    int index = 0;
    for (Cell cell : list) {
      if (cell.getDiagonalPos() == diagonalPos && cell.getRowPos() == rowPos) {
        break;
      }
      else {
        index = index + 1;
      }
    }
    return index;
  }

  /**
   * checks if a move is valid in ONE individual list.
   * @param list the list of cells being checked for a valid move.
   * @param index the index at which a piece will be potentially placed.
   * @return true if the piece would create a sandwich in the list of at least
   *     one of the opponents pieces.
   */
  protected boolean checkValidHelper(List<Cell> list, int index) {
    return this.scanForward(list, index) || this.scanBackward(list, index);
  }

  /**
   * scans forward in a list and checks if a piece would make a sanwich with at least
   * one of the other players pieces.
   * @param list the list of cells being checked for a valid move.
   * @param index the index at which a piece will potentially be placed.
   * @return true if the piece makes a sandwich with the other player's pieces.
   */
  protected boolean scanForward(List<Cell> list, int index) {
    //automatically returns false if the index is the last item and cannot scan foward.
    if (index == list.size() - 1) {
      return false;
    }
    else {
      boolean valid = false;
      int opposites = 0;
      for (int i = index + 1; i < list.size(); i++) {
        Cell cell = list.get(i);
        //breaks and returns false if the next piece is the same color.
        if (cell.toString().equals(playerTurn.toString()) && opposites == 0) {
          break;
        }
        //breaks and returns false if any of the next pieces are empty.
        if (cell.isEmpty()) {
          break;
        }
        //adds one to opposites and keeps scanning if the next pieces belongs to
        // the other player.
        if (!cell.toString().equals(playerTurn.toString())) {
          opposites = opposites + 1;
        }
        //breaks and returns true if the next piece is the same color and there are opposite
        //pieces between them.
        if (cell.toString().equals(playerTurn.toString()) && (opposites != 0)) {
          valid = true;
          break;
        }
      }
      return valid;
    }
  }

  /**
   * scans backward in a list and checks if a piece would make a sanwich with at least
   * one of the other players pieces.
   * @param list the list of cells being checked for a valid move.
   * @param index the index at which a piece will potentially be placed.
   * @return true if the piece makes a sandwich with the other player's pieces.
   */
  protected boolean scanBackward(List<Cell> list, int index) {
    //automatically returns false if the index is the last item and cannot scan backward.
    if (index == 0) {
      return false;
    }
    else {
      boolean valid = false;
      int opposites = 0;
      for (int i = index - 1; i >= 0; i--) {
        Cell cell = list.get(i);
        //breaks and returns false if the previous piece is the same color.
        if (cell.toString().equals(playerTurn.toString()) && opposites == 0) {
          break;
        }
        //breaks and returns false if any of the previous pieces are empty.
        if (cell.isEmpty()) {
          break;
        }
        //adds one to opposites and keeps scanning if the previous pieces belongs to the
        // other player.
        if (!cell.toString().equals(playerTurn.toString())) {
          opposites = opposites + 1;
        }
        //breaks and returns true if the previous piece is the same color and there are opposite
        //pieces between them.
        if (cell.toString().equals(playerTurn.toString()) && (opposites != 0)) {
          valid = true;
          break;
        }
      }
      return valid;
    }
  }



  /**
   * Move the PlayerEnum value to the next player.
   * If PlayerEnum is X -> O.
   * If PlayerEnum is O -> X.
   */
  protected void nextPlayer() {
    if (this.playerTurn.toString().equals("X")) {
      this.playerTurn = PlayerEnum.O;
    }
    else {
      // need to ensure that it doesnt switch X -> O -> X
      // if (this.playerTurn.toString().equals("0")) {
      this.playerTurn = PlayerEnum.X;
    }
  }

  /**
   * adjusts the turnsTaken field to reflect a move that has been made.
   * removed the first boolean move and adds a new boolean to reflect the move.
   * @param move the move that was made. true represents a player move, false
   *             represents a player pass.
   */
  protected void madeMove(boolean move) {
    //INVARIANT PRESERVED IN METHODS: in this method, something is removed from turnstaken, but
    //something is added at the same time. Therefore, the size does not change.
    this.turnsTaken.remove(0);
    this.turnsTaken.add(move);
  }

  /**
   * turns over all opposing cells that are sandwiched in a valid move.
   * does NOT add a piece to the index, the cell at the index remains empty!!!!
   * @param diagonalPos the diagonal position of the placed piece.
   * @param rowPos the row position of the placed piece.
   */
  protected abstract void turnOverAllValid(int diagonalPos, int rowPos);

  /**
   * turns over all opposite pieces between the current players pieces if the move is valid.
   * does NOT add a piece to the index, the cell at the index remains empty!!!!
   * @param list the list that is scanned through to turn over pieces.
   * @param index the starting index of the list, where the piece would be placed.
   */
  protected void turnOverOpposites(List<Cell> list, int index) {
    if (this.scanForward(list, index)) {
      for (int i = index + 1; i < list.size(); i++) {
        Cell cell = list.get(i);
        if (cell.toString().equals(playerTurn.toString())) {
          break;
        }
        cell.changeCellState(playerTurn);
      }
    }
    if (this.scanBackward(list, index)) {
      for (int i = index - 1; i >= 0; i--) {
        Cell cell = list.get(i);
        if (cell.toString().equals(playerTurn.toString())) {
          break;
        }
        cell.changeCellState(playerTurn);
      }
    }
  }

  /**
   * determines if the given position is a position on the game board.
   * @param diagonalPos the diagonal position.
   * @param rowPos the row position.
   * @return true if the position is a position on the game board.
   */
  protected abstract boolean posnNotInGame(int diagonalPos, int rowPos);





  //HELPER METHODS FOR IS GAME OVER:


  /**
   * Determines if the board is full.
   * @return return true if there are no empty cells.
   */
  protected abstract boolean isBoardFull();

  /**
   * determines if all the occupied cells are the same color.
   * @return true if all the occupied cells have the same cell state.
   */
  protected abstract boolean monochromeCells();

  /**
   * Have there been two passes in a row by players? check.
   * @return true if both players passed immediately after each other.
   */
  protected boolean twoPasses() {
    // if both booleans in the TurnsTaken list are false, return true.
    return (!this.turnsTaken.get(0) && !this.turnsTaken.get(1));
  }



  /**
   * the number of pieces a player WOULD turn over in a row if making a move at this
   * index in the row.
   * @param list the list or row of cells that a player is moving in.
   * @param index the index in the list.
   * @return the number of cells the player would gain from placing a piece at this index.
   */
  protected int countAllHelper(List<Cell> list, int index) {
    List<Cell> piecesWon = new ArrayList<>();
    if (this.scanForward(list, index)) {
      for (int i = index + 1; i < list.size(); i++) {
        Cell cell = list.get(i);
        if (cell.toString().equals(playerTurn.toString())) {
          break;
        }
        piecesWon.add(cell);
      }
    }
    if (this.scanBackward(list, index)) {
      for (int i = index - 1; i >= 0; i--) {
        Cell cell = list.get(i);
        if (cell.toString().equals(playerTurn.toString())) {
          break;
        }
        piecesWon.add(cell);
      }
    }
    return piecesWon.size();
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
  abstract boolean nextTo(int q1, int r1, int q2, int r2);



  //HELPER FOR POSN IN CORNER

  abstract List<Cell> getCorners();

}
