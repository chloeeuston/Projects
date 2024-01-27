
import controller.ReversiController;
import model.BasicReversi;
import model.Player;
import model.PlayerEnum;
import model.ReversiModel;
import model.SquareReversi;
import player.AIPlayer;
import player.ActivePlayer;
import player.HumanPlayer;
import strategy.AvoidCorners;
import strategy.CaptureMostPieces;
import strategy.GoForCorners;
import strategy.Strategy;
import strategy.TryTwo;
import view.gui.ReversiFrame;


/**
 * A class which holds the main method that allows one to play a game of Reversi.
 */
public final class Reversi {


  /**
   * starts and opens a game of Reversi.
   * Note: if you want to pick the board size, enter the side length as the first argument.
   *       if you want a human player, enter "human".
   *       if you want an AI player, enter "easy", "medium", or "hard".
   * @param args inputted arguments that control the game.
   */
  public static void main(String[] args) {
    if (args.length < 3) {
      throw new IllegalArgumentException("enter a valid command");
    }

    int sideLength;
    ActivePlayer p1;
    ActivePlayer p2;
    ReversiModel model;


    if (args.length == 4) {
      model = createModel(args[0]);
      sideLength = Integer.parseInt(args[1]);
      p1 = makePlayer(args[2], model, PlayerEnum.X);
      p2 = makePlayer(args[3], model, PlayerEnum.O);
    }
    else {
      model = createModel(args[0]);
      if (model.isSquareReversi()) {
        sideLength = 8;
      }
      else {
        sideLength = 4;
      }
      p1 = makePlayer(args[1], model, PlayerEnum.X);
      p2 = makePlayer(args[2], model, PlayerEnum.O);
    }

    ReversiFrame viewP1 = new ReversiFrame(model);
    ReversiFrame viewP2 = new ReversiFrame(model);
    ReversiController c1 = new ReversiController(model, viewP1, p1);
    ReversiController c2 = new ReversiController(model, viewP2, p2);
    model.startGame(sideLength);
  }

  private static ActivePlayer makePlayer(String command, ReversiModel model, Player player) {
    Strategy easy = new CaptureMostPieces();
    Strategy medium = new TryTwo(new AvoidCorners(), easy);
    Strategy hard = new TryTwo(new GoForCorners(), medium);

    if (command.equals("human")) {
      return new HumanPlayer(model, player);
    }
    if (command.equals("easy")) {
      return new AIPlayer(easy, model, player);
    }
    if (command.equals("medium")) {
      return new AIPlayer(medium, model, player);
    }
    if (command.equals("hard")) {
      return new AIPlayer(hard, model, player);
    }
    else {
      throw new IllegalArgumentException("enter a valid command.");
    }
  }

  private static ReversiModel createModel(String arg) {
    if (arg.equals("hexagon")) {
      return new BasicReversi();
    }
    if (arg.equals("square")) {
      return new SquareReversi();
    }
    else {
      throw new IllegalArgumentException("enter a valid game type.");
    }
  }

}
