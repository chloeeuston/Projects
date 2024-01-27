import model.BasicReversi;
import model.PlayerEnum;
import org.junit.Assert;
import org.junit.Test;
import strategy.AvoidCorners;
import strategy.CaptureMostPieces;
import strategy.Coord;
import strategy.GoForCorners;
import strategy.Strategy;


/**
 * tests for reversi game strategies.
 */
public class TestStrategy {

  //plays a series of moves game of reversi where both players are using the capture most pieces
  // strategy to pick the coordinate they move to.
  @Test
  public void testCaptureMostPieces() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Strategy strategy = new CaptureMostPieces();

    //player X uses the strategy to pick a coordinate
    Coord actual = strategy.chooseMove(model, PlayerEnum.X);
    Coord expected = new Coord(1, -2);
    Assert.assertEquals(expected, actual);
    //player X makes a move to the strategy coordinate
    model.playerMove(1, -2, PlayerEnum.X);

    //player O uses the strategy to pick a coordinate
    Coord actual1 = strategy.chooseMove(model, PlayerEnum.O);
    Coord expected1 = new Coord(2, -3);
    Assert.assertEquals(expected1, actual1);
    //player O makes a move to the strategy coordinate
    model.playerMove(2, -3, PlayerEnum.O);

    //player X uses the strategy to pick a coordinate
    Coord actual2 = strategy.chooseMove(model, PlayerEnum.X);
    Coord expected2 = new Coord(-1, -1);
    Assert.assertEquals(expected2, actual2);
    //player X makes a move to the strategy coordinate
    model.playerMove(-1, -1, PlayerEnum.X);

    //player O uses the strategy to pick a coordinate
    Coord actual3 = strategy.chooseMove(model, PlayerEnum.O);
    Coord expected3 = new Coord(-2, 1);
    Assert.assertEquals(expected3, actual3);
    //player O makes a move to the strategy coordinate
    model.playerMove(-2, 1, PlayerEnum.O);
  }

  //plays a series of moves game of reversi where both players are using the avoid corners
  // strategy to pick the coordinate they move to.
  @Test
  public void testAvoidCorners() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Strategy strategy = new AvoidCorners();

    //player X uses the strategy to pick a coordinate
    Coord actual = strategy.chooseMove(model, PlayerEnum.X);
    Coord expected = new Coord(1, -2);
    Assert.assertEquals(expected, actual);
    //player X makes a move to the strategy coordinate
    model.playerMove(1, -2, PlayerEnum.X);

    //player O uses the strategy to pick a coordinate
    Coord actual1 = strategy.chooseMove(model, PlayerEnum.O);
    Coord expected1 = new Coord(2, -1);
    Assert.assertEquals(expected1, actual1);
    //player O makes a move to the strategy coordinate
    model.playerMove(2, -1, PlayerEnum.O);

    //player X uses the strategy to pick a coordinate
    Coord actual2 = strategy.chooseMove(model, PlayerEnum.X);
    Coord expected2 = new Coord(1, 1);
    Assert.assertEquals(expected2, actual2);
    //player X makes a move to the strategy coordinate
    model.playerMove(1, 1, PlayerEnum.X);

    //player O uses the strategy to pick a coordinate
    Coord actual3 = strategy.chooseMove(model, PlayerEnum.O);
    int r = actual3.getRowPos();
    Coord expected3 = new Coord(-1, 2);
    Assert.assertEquals(expected3, actual3);
    //player O makes a move to the strategy coordinate
    model.playerMove(-1, 2, PlayerEnum.O);
  }


  //plays a series of moves game of reversi where both players are using the go for corners
  // strategy to pick the coordinate they move to.
  @Test
  public void testGoForCorners() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Strategy strategy = new GoForCorners();

    model.playerMove(1, -2, PlayerEnum.X);
    model.playerMove(-2, 1, PlayerEnum.O);
    model.playerMove(-3, 2, PlayerEnum.X);
    model.playerMove(-3, 1, PlayerEnum.O);

    //player X uses the strategy to pick a coordinate
    Coord actual = strategy.chooseMove(model, PlayerEnum.X);
    Coord expected = new Coord(-3, 0);
    Assert.assertEquals(expected, actual);
    //player X makes a move to the strategy coordinate
    model.playerMove(-3, 0, PlayerEnum.X);

    //player O uses the strategy to pick a coordinate
    Coord actual1 = strategy.chooseMove(model, PlayerEnum.O);
    Coord expected1 = null;
    Assert.assertEquals(expected1, actual1);
  }

}
