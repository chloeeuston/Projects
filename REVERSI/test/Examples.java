import model.BasicReversi;
import model.PlayerEnum;
import model.ReversiModel;
import view.ReversiTextualView;

/**
 * Examples represents viable games for the basic Reversi game.
 */
public class Examples {

  /**
   * Represents Examples of correctly played games.
   */
  public Examples() {

    //represents a game where both players reach a stalemate and player O wins by having
    //more tiles on the game board.
    ReversiModel model1 = new BasicReversi();
    ReversiTextualView modelView1 = new ReversiTextualView(model1);
    model1.startGame(3);
    model1.playerMove(1, 1, PlayerEnum.X);
    model1.playerMove(-1, -1, PlayerEnum.O);
    model1.playerMove(1, -2, PlayerEnum.X);
    model1.playerMove(-1, 2, PlayerEnum.O);
    model1.playerMove(-2, 1, PlayerEnum.X);
    model1.playerMove(2, -1, PlayerEnum.O);
    model1.playerPass(PlayerEnum.X);
    model1.playerPass(PlayerEnum.O);


    //represents a game where the players tie and the game ends by a double pass.
    ReversiModel model2 = new BasicReversi();
    ReversiTextualView modelView2 = new ReversiTextualView(model2);
    model2.startGame(3);
    model2.playerMove(-1, 2, PlayerEnum.X);
    model2.playerMove(1, 1, PlayerEnum.O);
    model2.playerPass(PlayerEnum.X);
    model2.playerPass(PlayerEnum.O);


    //represents a game where player O wins by occupying all the full cells on the board.
    BasicReversi model3 = new BasicReversi();
    model3.startGame(4);
    ReversiTextualView modelView3 = new ReversiTextualView(model3);
    model3.playerMove(1, 1, PlayerEnum.X);
    model3.playerMove(-1, 2, PlayerEnum.O);
    model3.playerMove(1, -2, PlayerEnum.X);
    model3.playerPass(PlayerEnum.O);
    model3.playerMove(-2, 1, PlayerEnum.X);
    model3.playerPass(PlayerEnum.O);
    model3.playerMove(-2, 3, PlayerEnum.X);


    //represents a game where X wins
    BasicReversi model4 = new BasicReversi();
    model4.startGame(4);
    ReversiTextualView modelView4 = new ReversiTextualView(model4);
    model4.playerMove(1, 1, PlayerEnum.X);
    model4.playerMove(-1, 2, PlayerEnum.O);
    model4.playerMove(1, -2, PlayerEnum.X);
    model4.playerMove(2, -1, PlayerEnum.O);
    model4.playerMove(3, -1, PlayerEnum.X);
    model4.playerMove(3, -2, PlayerEnum.O);
    model4.playerMove(-2, 1, PlayerEnum.X);
    model4.playerMove(-1, -1, PlayerEnum.O);
    model4.playerMove(-2, -1, PlayerEnum.X);
    model4.playerMove(2, -3, PlayerEnum.O);
    model4.playerMove(1, -3, PlayerEnum.X);
    model4.playerMove(-3, 2, PlayerEnum.O);
    model4.playerMove(-2, 3, PlayerEnum.X);
    model4.playerMove(-1, 3, PlayerEnum.O);
    model4.playerMove(-3, 1, PlayerEnum.X);
    model4.playerMove(-3, 0, PlayerEnum.O);
    model4.playerPass(PlayerEnum.X);
    model4.playerPass(PlayerEnum.O);
  }
}
