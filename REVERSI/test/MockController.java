import player.ActivePlayer;
import controller.ReversiController;
import model.ReversiModel;
import view.gui.ReversiFrame;


/**
 * A Mock of the reversi controller used to test the controller and how it responds to signals.
 * Consists of a string builder that recieves a message when various methods in the
 * model/view/player are called.
 */
public class MockController extends ReversiController {

  StringBuilder out;

  public MockController(ReversiModel model, ReversiFrame view,
      ActivePlayer player, StringBuilder out) {
    super(model, view, player);
    this.out = out;
  }

  public String outToString() {
    return this.out.toString();
  }

  @Override
  public void displayGameOver() {
    out.append("check if the game is over, display a game over message if it is.");
  }

  @Override
  public void update() {
    out.append("update the view to the current state.");
  }

  @Override
  public void notifyPlayerTurn() {
    out.append("checks if it is this player's turn, notify them if it is.");
  }

  @Override
  public void makeMoveIfAI() {
    out.append("check if this player is AI and if it is their turn, "
        + "signal for them to make a move if it is.");
  }

  @Override
  public void makeAIMove(int diagonalPos, int rowPos) {
    out.append("move this player to the given position.");
  }

  @Override
  public void makeAIPass() {
    out.append("pass this player's turn.");
  }

}
