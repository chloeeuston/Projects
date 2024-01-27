import model.Cell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.BasicReversi;
import model.PlayerEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * a class for tests on a reversi model.
 */
public class TestModel {


  //START GAME TESTS:

  //tests that the correct number of cells are added to the game board
  @Test
  public void testStartGame1() {
    BasicReversi model = new BasicReversi();
    model.startGame(6);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Assert.assertEquals(board.size(), 11);

    int numCells = 0;
    for (ArrayList<Cell> cells : board) {
      numCells += cells.size();
    }
    Assert.assertEquals(numCells, 91);
  }

  //tests that row posn is assigned correctly
  @Test
  public void testStartGame2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();

    for (Cell cell : board.get(0)) {
      Assert.assertEquals(-3, cell.getRowPos());
    }
  }

  //tests that row posn is assigned correctly
  @Test
  public void testStartGame3() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();

    for (Cell cell : board.get(6)) {
      Assert.assertEquals(3, cell.getRowPos());
    }
  }

  //tests that diagonal posn is assigned correctly
  @Test
  public void testStartGame4() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();

    //checking the diagonal of the first cell in the first row
    Cell c1 = board.get(0).get(0);
    Assert.assertEquals(0, c1.getDiagonalPos());
    //checking the diagonal of the fourth cell in the first row
    Cell c2 = board.get(0).get(3);
    Assert.assertEquals(3, c2.getDiagonalPos());
    //checking the diagonal of the first cell in the second row
    Cell c3 = board.get(1).get(0);
    Assert.assertEquals(-1, c3.getDiagonalPos());
    //checking the diagonal of the first cell in the halway index row (4th row)
    Cell c4 = board.get(3).get(0);
    Assert.assertEquals(-3, c4.getDiagonalPos());
    //checking the diagonal of the first cell in the last row
    Cell c5 = board.get(6).get(0);
    Assert.assertEquals(-3, c5.getDiagonalPos());
    //checking the diagonal of the last cell in the last row
    Cell c6 = board.get(6).get(3);
    Assert.assertEquals(0, c6.getDiagonalPos());
  }

  //tests that an exception is thrown when the board is not big enough
  @Test (expected = IllegalArgumentException.class)
  public void testStartGame5() {
    BasicReversi model = new BasicReversi();
    model.startGame(2);
  }





  //GET CELL AT TESTS:

  //tests that the getCellAt method returns the correct cell
  @Test
  public void testGetCellAt1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Cell expected = board.get(2).get(4);
    Assert.assertEquals(expected, model.getCellAtPosn(2, -1));
  }

  //tests that an exception is thrown if the positions are not in the game board.
  @Test (expected = IllegalArgumentException.class)
  public void testGetCellAt2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.getCellAtPosn(6, 0);
  }

  //tests that an exception is thrown if the game is not started.
  @Test (expected = IllegalStateException.class)
  public void testGetCellAt3() {
    BasicReversi model = new BasicReversi();
    model.getCellAtPosn(6, 0);
  }

  //tests that the getCellAt method returns the correct cell
  @Test
  public void testGetCellAt4() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Cell expected = board.get(6).get(3);
    Assert.assertEquals(expected, model.getCellAtPosn(0, 3));
  }





  //TESTS FOR PLAYER MOVE:

  //tests a valid move:
  // - that the player takes over the empty cell it is places at
  // - that the sandwiched opposite pieces are turned over
  // - that no pieces in the row outside the sandwich are turned over
  @Test
  public void testPlayerMove1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(-2, 1, PlayerEnum.X);

    Assert.assertEquals("X",
        model.getCellAtPosn(-2, 1).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(-1, 0).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(0, -1).toString());
    Assert.assertEquals("_",
        model.getCellAtPosn(1, -2).toString());
    Assert.assertEquals("_",
        model.getCellAtPosn(-3, 2).toString());
  }

  //tests player move:
  // - that the player switches after a move is made
  @Test
  public void testPlayerMove2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    //same move as before
    model.playerMove(-2, 1, PlayerEnum.X);
    //checks that the player switches after move
    Assert.assertEquals("O", model.getPlayerTurn().toString());
    //player O makes a move.
    model.playerMove(-1, -1, PlayerEnum.O);

    Assert.assertEquals("O",
        model.getCellAtPosn(-1, -1).toString());
    Assert.assertEquals("O",
        model.getCellAtPosn(0, -1).toString());
    Assert.assertEquals("O",
        model.getCellAtPosn(1, -1).toString());
  }

  //tests player move:
  // - that an exception is thrown when the game is not started
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove3() {
    BasicReversi model = new BasicReversi();
    model.playerMove(-2, 1, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when the coordinates are not valid
  @Test (expected = IllegalArgumentException.class)
  public void testPlayerMove4() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(0, -4, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when a player tries to place on a non-empty cell.
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove5() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(1, 0, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when the move is not valid.
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove6() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(0, -2, PlayerEnum.X);
  }

  //tests player move:
  // - that a player move takes over sandwiched pieces when there are sandwiches in
  //      more than one line on the board (two sandwiches are made)
  @Test
  public void testPlayerMove7() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    //same 2 moves as before (already tested)
    model.playerMove(-2, 1, PlayerEnum.X);
    model.playerMove(-1, -1, PlayerEnum.O);

    model.playerMove(1, -2, PlayerEnum.X);

    Assert.assertEquals("X",
        model.getCellAtPosn(1, -2).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(0, -1).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(1, -1).toString());
  }

  //tests player move
  // - that the sandwiched pieces in the q diagonal are flipped
  @Test
  public void testPlayerMove8() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(1, -2, PlayerEnum.X);

    Assert.assertEquals("X",
        model.getCellAtPosn(1, -2).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(1, -1).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(1, 0).toString());
  }

  //tests player move:
  // - that .
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove9() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(0, -2, PlayerEnum.X);
  }





  //TESTS FOR PLAYER PASS

  //tests than an exception is thrown if the game has not been started
  @Test (expected = IllegalStateException.class)
  public void testPlayerPass1() {
    BasicReversi model = new BasicReversi();
    model.playerPass(PlayerEnum.X);
  }

  //tests that the player turn changes after one has passed
  @Test
  public void testPlayerPass2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals("X", model.getPlayerTurn().toString());
    model.playerPass(PlayerEnum.X);
    Assert.assertEquals("O", model.getPlayerTurn().toString());
  }

  //tests that the recent turn taken switches to false when a pass is made.
  @Test
  public void testPlayerPass3() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerPass(PlayerEnum.X);
    List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(true, false));
    Assert.assertEquals(list, model.getTurnsTaken());
  }

  //tests that the game board is not changed when a player passes.
  @Test
  public void testPlayerPass4() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> gameBoard1 =
        new ArrayList<ArrayList<Cell>>(model.getGameBoard());
    model.playerPass(PlayerEnum.X);
    Assert.assertEquals(gameBoard1, model.getGameBoard());
  }





  //TESTS FOR IS GAME OVER

  //tests that an exception is thrown when the game is not started
  @Test (expected = IllegalStateException.class)
  public void testGameOver1() {
    BasicReversi model = new BasicReversi();
    model.isGameOver();
  }

  //tests that the game is not over when the game has been started
  @Test
  public void testGameOver2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertFalse(model.isGameOver());
  }

  //tests that the game is over when both players pass
  @Test
  public void testGameOver3() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerPass(PlayerEnum.X);
    Assert.assertFalse(model.isGameOver());
    model.playerPass(PlayerEnum.O);
    Assert.assertTrue(model.isGameOver());
  }

  //tests that the game is over when all pieces on the board are the same color
  @Test
  public void testGameOver4() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(-2, 1, PlayerEnum.X);
    Assert.assertFalse(model.isGameOver());
    model.playerPass(PlayerEnum.O);
    model.playerMove(1, 1, PlayerEnum.X);
    Assert.assertFalse(model.isGameOver());
    model.playerPass(PlayerEnum.O);
    model.playerMove(1, -2, PlayerEnum.X);
    Assert.assertTrue(model.isGameOver());
  }

  //tests that the game over if the board is full
  @Test
  public void testGameOver5() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    for (ArrayList<Cell> list : board) {
      for (Cell cell : list) {
        cell.changeCellState(PlayerEnum.O);
      }
    }
    //change so not all are the same color
    board.get(0).get(0).changeCellState(PlayerEnum.X);
    Assert.assertTrue(model.isGameOver());
  }





  //TESTS FOR FORCED PASS:

  //tests that an exception is thrown if the game has not been started
  @Test (expected = IllegalStateException.class)
  public void testForcedPass1() {
    BasicReversi model = new BasicReversi();
    model.forcedPass();
  }

  //tests that a forced pass returns false when there are valid moves
  @Test
  public void testForcedPass2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertFalse(model.forcedPass());
  }

  //tests that a forced pass returns false when there are no valid moves
  @Test
  public void testForcedPass3() {
    BasicReversi model = new BasicReversi();
    model.startGame(3);
    model.playerMove(-2, 1, PlayerEnum.X);
    model.playerPass(PlayerEnum.O);
    model.playerMove(2, -1, PlayerEnum.X);
    Assert.assertTrue(model.forcedPass());

  }




  //TESTS FOR GET WINNER:

  //tests that an exception is thrown if the game is not started
  @Test (expected = IllegalStateException.class)
  public void testGetWinner1() {
    BasicReversi model = new BasicReversi();
    model.getWinner();
  }

  //tests that an exception is thrown if the game is not over
  @Test (expected = IllegalStateException.class)
  public void testGetWinner2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.getWinner();
  }

  //tests that the correct winner is returned when two players pass
  @Test
  public void testGetWinner3() {
    BasicReversi model = new BasicReversi();
    model.startGame(3);
    model.playerMove(-2, 1, PlayerEnum.X);
    model.playerPass(PlayerEnum.O);
    model.playerPass(PlayerEnum.X);
    Assert.assertEquals(PlayerEnum.X, model.getWinner());
  }

  //tests that the empty player is returned in a tie
  @Test
  public void testGetWinner4() {
    BasicReversi model = new BasicReversi();
    model.startGame(3);
    model.playerPass(PlayerEnum.X);
    model.playerPass(PlayerEnum.O);
    Assert.assertEquals(PlayerEnum.Empty, model.getWinner());
  }




  //TESTS FOR CHECK VALID

  //tests that a valid move is valid
  @Test
  public void testCheckValid() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);

    Assert.assertTrue(model.checkValid(1, -2));
  }




  //TESTS FOR GET BOARD SIDE LENGTH

  //tests that get board sideLength returns the correct size
  @Test
  public void testGetBoardSideLength1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals(4, model.getBoardSideLength());
  }

  //tests that get board sideLength returns the correct size
  @Test
  public void testGetBoardSideLength2() {
    BasicReversi model = new BasicReversi();
    model.startGame(5);
    Assert.assertEquals(5, model.getBoardSideLength());
  }



  //TESTS FOR GET PLAYER SCORE

  //tests that get player score returns the number of occupied cells a player has
  @Test
  public void testGetPlayerScore1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertEquals(3, model.getPlayerScore(PlayerEnum.X));
    Assert.assertEquals(3, model.getPlayerScore(PlayerEnum.O));
  }



  //TESTS FOR POSN IN CORNER

  @Test
  public void posnInCorner1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertTrue(model.posnInCorner(0, -3));
    Assert.assertTrue(model.posnInCorner(3, -3));
    Assert.assertTrue(model.posnInCorner(3, 0));
    Assert.assertTrue(model.posnInCorner(-3, 0));
    Assert.assertTrue(model.posnInCorner(-3, 3));
    Assert.assertTrue(model.posnInCorner(0, 3));
    Assert.assertFalse(model.posnInCorner(0, 0));
    Assert.assertFalse(model.posnInCorner(1, 0));
    Assert.assertFalse(model.posnInCorner(2, -1));
  }



  //TESTS FOR POSN NEXT TO CORNER

  @Test
  public void posnNextToCorner1() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    Assert.assertTrue(model.posnNextToCorner(1, -3));
    Assert.assertTrue(model.posnNextToCorner(0, -2));
    Assert.assertTrue(model.posnNextToCorner(-1, -2));
    Assert.assertTrue(model.posnNextToCorner(2, -3));
    Assert.assertTrue(model.posnNextToCorner(2, -2));
    Assert.assertTrue(model.posnNextToCorner(3, -2));
    Assert.assertFalse(model.posnNextToCorner(0, 0));
    Assert.assertFalse(model.posnNextToCorner(1, -2));
    Assert.assertFalse(model.posnNextToCorner(-1, 1));
    Assert.assertFalse(model.posnNextToCorner(-1, 2));
  }




  //TESTS FOR MODEL PRIVATE METHODS:
  /*
  //tests a valid move in a line
  @Test
  public void testCheckValidHelper1() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.X, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.O, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    Assert.assertTrue(model.checkValidHelper(list, 6));
  }

  //tests an invalid move in a line
  @Test
  public void testCheckValidHelper2() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.X, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.O, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    Assert.assertFalse(model.checkValidHelper(list, 2));
  }


  //tests an ivalid move in a line
  @Test
  public void testCheckValidHelper3() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.O, 0, 0);
    HexagonCell c4 = new HexagonCell( 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.X, 2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    Assert.assertFalse(model.checkValidHelper(list, 2));
  }

  //tests checkValid
  @Test
  public void testCheckValid() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);

    Assert.assertTrue(model.checkValid(1, -2));
  }

  @Test
  public void testGetIndex() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    ArrayList<HexagonCell> list = model.getQDiagonals(1);

    Assert.assertEquals(1, model.getListIndex(list, 1, -2));

  }

  @Test
  public void testScanForward1() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(0, 0);
    HexagonCell c4 = new HexagonCell(1, 0);
    HexagonCell c5 = new HexagonCell(2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));

    Assert.assertFalse(model.scanForward(list, 6));
  }

  @Test
  public void testScanForward2() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(0, 0);
    HexagonCell c4 = new HexagonCell(1, 0);
    HexagonCell c5 = new HexagonCell(2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));

    Assert.assertFalse(model.scanForward(list, 4));
  }

  @Test
  public void testScanForward3() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.X, 0, 0);
    HexagonCell c4 = new HexagonCell(1, 0);
    HexagonCell c5 = new HexagonCell(2, 0);
    HexagonCell c6 = new HexagonCell(3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));

    Assert.assertFalse(model.scanForward(list, 2));
  }

  @Test
  public void testScanForward4() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.O, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.O, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(PlayerEnum.O, 3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));

    Assert.assertFalse(model.scanForward(list, 2));
  }

  @Test
  public void testScanForward5() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-2, 0);
    HexagonCell c2 = new HexagonCell(-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.O, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.X, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(PlayerEnum.O, 3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));

    Assert.assertTrue(model.scanForward(list, 2));
  }

  @Test
  public void testQDiagonals() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    HexagonCell c0 = new HexagonCell(-3, 0);
    HexagonCell c1 = new HexagonCell(-3, 1);
    HexagonCell c2 = new HexagonCell(-3, 2);
    HexagonCell c3 = new HexagonCell(-3, 3);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3
    ));

    Assert.assertEquals(list, model.getQDiagonals(-3));
  }

  @Test
  public void testQDiagonals2() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    model.playerMove(-2, 1);
    model.playerMove(-1, -1);

    HexagonCell c0 = new HexagonCell(1, -3);
    HexagonCell c1 = new HexagonCell(1, -2);
    HexagonCell c2 = new HexagonCell(PlayerEnum.O, 1, -1);
    HexagonCell c3 = new HexagonCell(PlayerEnum.X, 1, 0);
    HexagonCell c4 = new HexagonCell(1, 1);
    HexagonCell c5 = new HexagonCell(1, 2);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5
    ));

    Assert.assertEquals(list, model.getQDiagonals(1));
  }

  @Test
  public void testSDiagonals() {
    BasicReversi model = new BasicReversi();
    model.startGame(4);
    HexagonCell c0 = new HexagonCell(1, -3);
    HexagonCell c1 = new HexagonCell(0, -2);
    HexagonCell c2 = new HexagonCell(-1, -1);
    HexagonCell c3 = new HexagonCell(-2, 0);
    HexagonCell c4 = new HexagonCell(-3, 1);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4
    ));

    Assert.assertEquals(list, model.getSDiagonals(-1, -1));
  }

  //tests that the helper method turns over sandwiched pieces
  @Test
  public void testTurnOverOpposites1() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(PlayerEnum.O, -3, 0);
    HexagonCell c1 = new HexagonCell(PlayerEnum.O,-2, 0);
    HexagonCell c2 = new HexagonCell(PlayerEnum.Empty,-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.O, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.O, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.X, 2, 0);
    HexagonCell c6 = new HexagonCell(PlayerEnum.O,3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    model.turnOverOpposites(list, 2);
    Assert.assertEquals("_", list.get(2).toString());
    Assert.assertEquals("X", list.get(3).toString());
    Assert.assertEquals("X", list.get(4).toString());
    Assert.assertEquals("X", list.get(5).toString());
    Assert.assertEquals("O", list.get(6).toString());
  }

  //tests that the helper method turns over sandwiched pieces
  @Test
  public void testTurnOverOpposites2() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(PlayerEnum.X, -3, 0);
    HexagonCell c1 = new HexagonCell(PlayerEnum.O,-2, 0);
    HexagonCell c2 = new HexagonCell(PlayerEnum.Empty,-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.O, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.X, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(PlayerEnum.O,3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    model.turnOverOpposites(list, 2);
    Assert.assertEquals("X", list.get(0).toString());
    Assert.assertEquals("X", list.get(1).toString());
    Assert.assertEquals("_", list.get(2).toString());
    Assert.assertEquals("X", list.get(3).toString());
    Assert.assertEquals("X", list.get(4).toString());
    Assert.assertEquals("O", list.get(5).toString());

  }

  //tests that the helper method turns over sandwiched pieces
  @Test
  public void testTurnOverOpposites3() {
    BasicReversi model = new BasicReversi();
    HexagonCell c0 = new HexagonCell(PlayerEnum.Empty, -3, 0);
    HexagonCell c1 = new HexagonCell(PlayerEnum.O,-2, 0);
    HexagonCell c2 = new HexagonCell(PlayerEnum.Empty,-1, 0);
    HexagonCell c3 = new HexagonCell(PlayerEnum.Empty, 0, 0);
    HexagonCell c4 = new HexagonCell(PlayerEnum.X, 1, 0);
    HexagonCell c5 = new HexagonCell(PlayerEnum.O, 2, 0);
    HexagonCell c6 = new HexagonCell(PlayerEnum.O,3, 0);
    ArrayList<HexagonCell> list = new ArrayList<>(Arrays.asList(
        c0, c1, c2, c3, c4, c5, c6
    ));
    model.turnOverOpposites(list, 2);
    Assert.assertEquals("_", list.get(0).toString());
    Assert.assertEquals("O", list.get(1).toString());
    Assert.assertEquals("_", list.get(2).toString());
    Assert.assertEquals("_", list.get(3).toString());
    Assert.assertEquals("X", list.get(4).toString());
    Assert.assertEquals("O", list.get(5).toString());

  }
*/

}
