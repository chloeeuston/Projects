package player;

import controller.PlayerFeatures;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Player;
import model.ReversiModel;
import strategy.Coord;
import strategy.Strategy;


/**
 * A class that represents an AI player playing a game of reversi.
 */
public class AIPlayer implements ActivePlayer {

  private final Strategy strategy;
  private final ReversiModel model;
  private final Player player;
  private final List<PlayerFeatures> featuresListener;


  /**
   * constructor.
   * @param strategy the strategy used by the AI.
   * @param model the Reversi game model.
   * @param player the player the AI is playing as.
   */
  public AIPlayer(Strategy strategy, ReversiModel model, Player player) {
    this.strategy = strategy;
    this.model = model;
    this.player = player;
    this.featuresListener = new ArrayList<>();
  }


  /**
   * Makes a move if it is this players turn.
   */
  public void makeMove() {
    Coord coord = strategy.chooseMove(model, player);
    if (coord == null) {
      for (PlayerFeatures features : featuresListener) {
        features.makeAIPass();
      }
    }
    else {
      int qCoord = coord.getDiagonalPos();
      int rCoord = coord.getRowPos();

      for (PlayerFeatures features : featuresListener) {
        features.makeAIMove(qCoord, rCoord);
      }
    }
  }


  /**
   * Determines if the player is a human player.
   * @return false.
   */
  @Override
  public boolean isHumanPlayer() {
    return false;
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
    this.featuresListener.add(Objects.requireNonNull(features));
  }

}
