import model.PlayerEnum;
import org.junit.Assert;
import org.junit.Test;
import strategy.AvoidCorners;
import strategy.CaptureMostPieces;
import strategy.Strategy;


/**
 * Testing strategies on the SquareReversiModel using a mock for the model.
 * check that the strategies accurately call the correctly methods on each appropriate cell.
 */
public class TestSquareStrategyUsingMocks {

  @Test
  public void testCaptureMostPieces() {
    MockSquareReversi model = new MockSquareReversi(new StringBuilder());
    model.startGame(6);
    Strategy strategy = new CaptureMostPieces();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (1, 1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, 2)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (3, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (3, 3)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (1, 3)"));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (2, 4)"));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (3, 1)"));
    Assert.assertTrue(model.outToString().contains(
        "checking the number of pieces the current player would gain by "
            + "moving to the position (4, 2)"));
  }


  @Test
  public void testAvoidCorners() {
    MockSquareReversi model = new MockSquareReversi(new StringBuilder());
    model.startGame(6);
    Strategy strategy = new AvoidCorners();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (1, 1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, 3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (3, 3)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (0, 0) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (0, 3) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 2) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (2, 2) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (3, 3) is in a corner position."
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 3) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (2, 4) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (3, 1) is next to a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (4, 2) is next to a corner position."
    ));
  }


  @Test
  public void testGoForCorners() {
    MockSquareReversi model = new MockSquareReversi(new StringBuilder());
    model.startGame(6);
    Strategy strategy = new AvoidCorners();
    strategy.chooseMove(model, PlayerEnum.X);

    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 3)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (0, 0)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (1, 1)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (2, 4)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (3, 2)"
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if it is a valid move at the position (4, 4)"
    ));

    Assert.assertTrue(model.outToString().contains(
        "checking if the position (1, 3) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (2, 4) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (3, 1) is in a corner position."
    ));
    Assert.assertTrue(model.outToString().contains(
        "checking if the position (4, 2) is in a corner position."
    ));

  }

}
