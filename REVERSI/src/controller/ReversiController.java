package controller;

import model.Player;
import model.ReversiModel;
import player.ActivePlayer;
import view.gui.FrameView;


/**
 * A class that controls a game of Reversi by taking signals from the model, view, and player.
 */
public class ReversiController implements ViewFeatures, ModelFeatures, PlayerFeatures {

  private final ReversiModel model;
  private final FrameView view;
  private final ActivePlayer player;


  /**
   * Constructor.
   * @param model the Reversi game model.
   * @param view the view of the Reversi game.
   * @param player the player that is controlling the game (using this view).
   */
  public ReversiController(ReversiModel model, FrameView view, ActivePlayer player) {
    this.model = model;
    this.view = view;
    this.player = player;
    view.setVisible();
    this.view.addFeatureListener(this);
    this.model.addFeaturesListener(this);
    this.player.addFeaturesListener(this);
  }


  /**
   * signals a player move when a hexagon is clicked and the player presses "m", "M", the space bar,
   * or enter.
   * @param diagonalPos the diagonal position of the selected hexagon.
   * @param rowPos the row position of the selected hexagon.
   */
  @Override
  public void playerMove(int diagonalPos, int rowPos) {
    try {
      model.playerMove(diagonalPos, rowPos, player.getPlayer());
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showMessageDialog(e.getMessage());
      view.deselectAll();
      view.refresh();
    }
    view.deselectAll();
    view.refresh();
  }


  /**
   * signals a player pass when a player presses "p" or "P".
   */
  @Override
  public void playerPass() {
    try {
      model.playerPass(player.getPlayer());
    } catch (IllegalArgumentException | IllegalStateException e) {
      view.showMessageDialog(e.getMessage());
      view.deselectAll();
      view.refresh();
    }
    view.deselectAll();
    view.refresh();
  }


  /**
   * Displays a message that the game is over and who the winner is when the game is over.
   */
  @Override
  public void displayGameOver() {
    if (model.isGameOver()) {
      view.showMessageDialog("Game Over. " + getWinnerMessage(model.getWinner()));
    }
  }


  /**
   * Updates the every view that references the model by repainting the view panel.
   */
  @Override
  public void update() {
    view.deselectAll();
    view.refresh();
  }


  /**
   * Sends a message to a player's screen when it is their turn.
   * Also checks if they have any valid moves. If not, notifies the player that they must pass.
   */
  @Override
  public void notifyPlayerTurn() {
    if (model.getPlayerTurn().toString().equals(player.getPlayer().toString())
        && !model.isGameOver() && player.isHumanPlayer()) {
      if (model.forcedPass()) {
        view.showMessageDialog("No more moves! Pass turn.");
      } else {
        view.showMessageDialog("it's your turn!");
      }
    }
  }


  /**
   * Signals to the controller that if the next player is an AI player, they must make a move.
   */
  @Override
  public void makeMoveIfAI() {
    view.deselectAll();
    view.refresh();
    if (!player.isHumanPlayer()) {
      if (model.getPlayerTurn().toString().equals(player.getPlayer().toString())) {
        player.makeMove();
        view.deselectAll();
        view.refresh();
      }
    }
  }


  /**
   * Creates a message saying what player wins, or a tie is there was a tie.
   * @param winner the player that won.
   * @return a message stating the player that won or a tie.
   */
  private String getWinnerMessage(Player winner) {
    if (winner.toString().equals("X")) {
      return "Black Wins!";
    }
    if (winner.toString().equals("O")) {
      return "White Wins!";
    }
    else {
      return "Player Tie.";
    }
  }


  /**
   * Signals that an AI player chooses to make a move to the given position.
   * @param diagonalPos the diagonal position of the cell the AI player is moving to.
   * @param rowPos the row position of the cell the AI player is moving to.
   */
  @Override
  public void makeAIMove(int diagonalPos, int rowPos) {
    model.playerMove(diagonalPos, rowPos, player.getPlayer());
  }


  /**
   * Signals that an AI player chooses to pass on their turn.
   */
  @Override
  public void makeAIPass() {
    model.playerPass(player.getPlayer());
  }

}
