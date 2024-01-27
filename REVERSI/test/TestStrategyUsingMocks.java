import model.PlayerEnum;
import org.junit.Assert;
import org.junit.Test;
import strategy.AvoidCorners;
import strategy.CaptureMostPieces;
import strategy.Strategy;


/**
 * tests that each strategy goes through and checks every cell in a game board and filters
 * them correctly.
 */
public class TestStrategyUsingMocks {

  @Test
  public void testCaptureMostPieces() {
    MockReversi model = new MockReversi(new StringBuilder());
    model.startGame(4);
    Strategy strategy = new CaptureMostPieces();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-1, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-3, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -2)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (1, -2)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (-1, -1)"    ));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (2, -1)"    ));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (-2, 1)"    ));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (1, 1)"    ));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (-1, 2)"    ));
  }


  @Test
  public void testAvoidCorners() {
    MockReversi model = new MockReversi(new StringBuilder());
    model.startGame(4);
    Strategy strategy = new AvoidCorners();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-1, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-3, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -2)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-2, 3) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (0, 0) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-1, -1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (3, 0) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (0, 1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-2, 3) is in a corner position."
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, -2) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-1, -1) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (2, -1) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-2, 1) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 1) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-1, 2) is next to a corner position."
    ));
  }


  @Test
  public void testGoForCorners() {
    MockReversi model = new MockReversi(new StringBuilder());
    model.startGame(4);
    Strategy strategy = new AvoidCorners();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-1, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (-3, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, -1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, -2)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, -2) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-1, -1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (2, -1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-2, 1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (-1, 2) is in a corner position."
    ));

  }


}
