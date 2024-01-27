package player;

import controller.PlayerFeatures;
import model.Player;
import model.ReversiModel;


/**
 * A class to represent a human player playing a game of Reversi.
 */
public class HumanPlayer implements ActivePlayer {

  ReversiModel model;
  private final Player player;

  /**
   * constructor.
   * @param model the model that the human is playing in.
   * @param player the player the human is playing as.
   */
  public HumanPlayer(ReversiModel model, Player player) {
    this.player = player;
  }


  /**
   * Makes a move if it is this players turn.
   */
  @Override
  public void makeMove() {
    //nothing happens because a human player must make the move themselves
  }


  /**
   * Determines if the player is a human player.
   * @return true.
   */
  @Override
  public boolean isHumanPlayer() {
    return true;
  }


  /**
   * Returns the player type that this player is playing as (black or white).
   * @return the player that the player is making moves for.
   */
  @Override
  public Player getPlayer() {
    return player;
  }


  /**
   * Adds a listener to a player to react to the signals the player sends.
   * @param features the listener (the controller).
   */
  @Override
  public void addFeaturesListener(PlayerFeatures features) {
    //does nothing because a human player does not require a listener
  }

}
