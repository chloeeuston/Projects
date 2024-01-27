import model.PlayerEnum;
import org.junit.Assert;
import org.junit.Test;

import model.BasicReversi;
import view.ReversiTextualView;

/**
 * Test methods of the View class.
 */
public class TestView {

  // Tests that the starting view renders correctly.
  @Test
  public void testStandardStartingView() {
    BasicReversi model = new BasicReversi();
    model.startGame(6);

    String modelBoard =
        "     _ _ _ _ _ _ \n" +
        "    _ _ _ _ _ _ _ \n" +
        "   _ _ _ _ _ _ _ _ \n" +
        "  _ _ _ _ _ _ _ _ _ \n" +
        " _ _ _ _ X O _ _ _ _ \n" +
        "_ _ _ _ O _ X _ _ _ _ \n" +
        " _ _ _ _ X O _ _ _ _ \n" +
        "  _ _ _ _ _ _ _ _ _ \n" +
        "   _ _ _ _ _ _ _ _ \n" +
        "    _ _ _ _ _ _ _ \n" +
        "     _ _ _ _ _ _ \n";


    Assert.assertEquals(new ReversiTextualView(model).toString(), modelBoard);
  }

  @Test
  public void testSmallerStandardStartingView() {
    BasicReversi model = new BasicReversi();
    model.startGame(3);

    String modelBoard =
        "  _ _ _ \n" +
        " _ X O _ \n" +
        "_ O _ X _ \n" +
        " _ X O _ \n" +
        "  _ _ _ \n";

    Assert.assertEquals(new ReversiTextualView(model).toString(), modelBoard);
  }

  @Test
  public void testEvenSmallerStartingView() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);

    String modelBoard =
        "   _ _ _ _ \n" +
        "  _ _ _ _ _ \n" +
        " _ _ X O _ _ \n" +
        "_ _ O _ X _ _ \n" +
        " _ _ X O _ _ \n" +
        "  _ _ _ _ _ \n" +
        "   _ _ _ _ \n";

    Assert.assertEquals(new ReversiTextualView(model).toString(), modelBoard);
  }


  @Test
  public void testLargeStartingView() {
    BasicReversi model = new BasicReversi();
    model.startGame(10);

    String modelBoard =
        "         _ _ _ _ _ _ _ _ _ _ \n" +
        "        _ _ _ _ _ _ _ _ _ _ _ \n" +
        "       _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "      _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "     _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        " _ _ _ _ _ _ _ _ X O _ _ _ _ _ _ _ _ \n" +
        "_ _ _ _ _ _ _ _ O _ X _ _ _ _ _ _ _ _ \n" +
        " _ _ _ _ _ _ _ _ X O _ _ _ _ _ _ _ _ \n" +
        "  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "   _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "    _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "     _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "      _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "       _ _ _ _ _ _ _ _ _ _ _ _ \n" +
        "        _ _ _ _ _ _ _ _ _ _ _ \n" +
        "         _ _ _ _ _ _ _ _ _ _ \n";

    Assert.assertEquals(new ReversiTextualView(model).toString(), modelBoard);
  }

  @Test
  public void testMovesUpdateBoard() {
    BasicReversi model = new BasicReversi();
    model.startGame(3);
    ReversiTextualView modelView = new ReversiTextualView(model);

    String modelBoard =
        "  _ _ _ \n" +
        " _ X O _ \n" +
        "_ O _ X _ \n" +
        " _ X O _ \n" +
        "  _ _ _ \n";

    Assert.assertEquals(modelBoard, modelView.toString());

    model.playerMove(1, 1, PlayerEnum.X); // move 4th row, last space
    String modelBoardFirstMove =
        "  _ _ _ \n" +
        " _ X O _ \n" +
        "_ O _ X _ \n" +
        " _ X X X \n" +
        "  _ _ _ \n";
    Assert.assertEquals(modelBoardFirstMove, modelView.toString());

    model.playerMove(1, -2, PlayerEnum.O); // move top middle
    String modelBoardSecondMove =
        "  _ O _ \n" +
        " _ O O _ \n" +
        "_ O _ X _ \n" +
        " _ X X X \n" +
        "  _ _ _ \n";
    Assert.assertEquals(modelBoardSecondMove, modelView.toString());
  }
}