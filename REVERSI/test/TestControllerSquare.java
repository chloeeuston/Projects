import model.PlayerEnum;
import model.ReversiModel;
import model.SquareReversi;
import org.junit.Assert;
import org.junit.Test;
import player.AIPlayer;
import player.ActivePlayer;
import player.HumanPlayer;
import strategy.CaptureMostPieces;
import strategy.GoForCorners;
import view.gui.ReversiFrame;



/**
 * Tests for a reversi controller in a square reversi model.
 */
public class TestControllerSquare {

  /**
   * Tests that after a move is made, the model signals to the controller to check if
   * the game is over, and displays a game over message if the game is over.
   */
  @Test
  public void testGameOverSignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new HumanPlayer(model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    model.playerMove(0, 2, player.getPlayer());
    Assert.assertTrue(controller.outToString().contains(
        "check if the game is over, display a game over message if it is."));
  }

  /**
   * Tests that after a move is made, the model signals to the controller to update the view.
   */
  @Test
  public void testUpdateSignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new HumanPlayer(model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    model.playerPass(player.getPlayer());
    Assert.assertTrue(controller.outToString().contains(
        "update the view to the current state."));
  }

  /**
   * Tests that after a move is made, the model signals to the controller to check if it is the
   * player's turn. If it is the player's turn, it signals to the view to display a message (only
   * for human players).
   */
  @Test
  public void testNotifyPlayerTurnSignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new HumanPlayer(model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    model.playerPass(player.getPlayer());
    Assert.assertTrue(controller.outToString().contains(
        "checks if it is this player's turn, notify them if it is."));
  }

  /**
   * Tests that after a move is made, the model signals to the controller to check if the player is
   * an AI player and if it is their turn. If it is, the controller sends a signal to the player to
   * make a move.
   */
  @Test
  public void testMakeMoveIfAISignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new HumanPlayer(model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    model.playerPass(player.getPlayer());
    Assert.assertTrue(controller.outToString().contains(
        "check if this player is AI and if it is their turn, "
            + "signal for them to make a move if it is."));
  }

  /**
   * Tests that when an AI player is signaled to make a move, it sends a signal to the controller
   * to make the move it chooses. (Here, the AI strategy chooses to place a piece).
   */
  @Test
  public void testMakeAIMoveSignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new AIPlayer(new CaptureMostPieces(), model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    player.makeMove();
    Assert.assertTrue(controller.outToString().contains(
        "move this player to the given position."));
  }

  /**
   * Tests that when an AI player is signaled to make a move, it sends a signal to the controller
   * to make the move it chooses. (Here, the AI strategy chooses to pass).
   */
  @Test
  public void testMakeAIPassSignal() {
    ReversiModel model = new SquareReversi();
    ReversiFrame view = new ReversiFrame(model);
    ActivePlayer player = new AIPlayer(new GoForCorners(), model, PlayerEnum.X);
    MockController controller = new MockController(model, view, player, new StringBuilder());

    model.startGame(4);
    player.makeMove();
    Assert.assertTrue(controller.outToString().contains(
        "pass this player's turn."));
  }

}
