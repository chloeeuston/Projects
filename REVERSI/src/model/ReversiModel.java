package model;


import controller.ModelFeatures;

/**
 * A model of a Reversi Game that allows you to observe the game and make changes to the model
 * through player actions.
 */
public interface ReversiModel extends ReadOnlyReversiModel {

  /**
   * Starts a game of Reversi.
   * @param sideLength  desired length of a side of the hexagonal game board.
   * @throws IllegalArgumentException if the side length is too small to create a valid board.
   */
  void startGame(int sideLength);


  /**
   * Moves a player to the given position (adjusts the player field of the cell).
   * @param diagonalPos  diagonal position for the piece to be placed at.
   * @param rowPos  row position for the piece to be placed at.
   * @param player the player making the move.
   * @throws IllegalArgumentException if either positions do not exist on the board.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalStateException if the cell at the given position is not empty.
   * @throws IllegalStateException if the move is not allowable.
   * @throws IllegalArgumentException if it is not the player's turn.
   */
  void playerMove(int diagonalPos, int rowPos, Player player);


  /**
   * Execute what happens when a player passes as their turn.
   * Move to next player.
   * @param player the player that is passing.
   * @throws IllegalStateException if the game has not been started.
   * @throws IllegalArgumentException if it is not the player's turn.
   */
  void playerPass(Player player);


  /**
   * Adds a listener to the model to react to the signals the model sends.
   * @param features the listener (the controller).
   */
  void addFeaturesListener(ModelFeatures features);

}


