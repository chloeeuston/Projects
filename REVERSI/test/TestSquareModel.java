import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Cell;
import model.PlayerEnum;
import model.ReversiModel;
import model.SquareReversi;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests for a ReversiSquareModel.
 */
public class TestSquareModel {

  //START GAME TESTS:

  //tests that the correct number of cells are added to the game board
  @Test
  public void testStartGame1() {
    ReversiModel model = new SquareReversi();
    model.startGame(6);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Assert.assertEquals(board.size(), 6);

    int numCells = 0;
    for (ArrayList<Cell> cells : board) {
      numCells += cells.size();
    }
    Assert.assertEquals(numCells, 36);
  }

  //tests that row posn is assigned correctly
  @Test
  public void testStartGame2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();

    for (Cell cell : board.get(0)) {
      Assert.assertEquals(0, cell.getRowPos());
    }
  }

  //tests that row posn is assigned correctly
  @Test
  public void testStartGame3() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();

    for (Cell cell : board.get(3)) {
      Assert.assertEquals(3, cell.getRowPos());
    }
  }

  //tests that diagonal posn is assigned correctly
  @Test
  public void testStartGame4() {
    ReversiModel model = new SquareReversi();
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
    Assert.assertEquals(0, c3.getDiagonalPos());
    //checking the diagonal of the first cell in the halway index row (4th row)
    Cell c4 = board.get(3).get(1);
    Assert.assertEquals(1, c4.getDiagonalPos());
    //checking the diagonal of the first cell in the last row
    Cell c5 = board.get(2).get(2);
    Assert.assertEquals(2, c5.getDiagonalPos());
    //checking the diagonal of the last cell in the last row
    Cell c6 = board.get(2).get(3);
    Assert.assertEquals(3, c6.getDiagonalPos());
  }

  //tests that an exception is thrown when the board is not big enough
  @Test (expected = IllegalArgumentException.class)
  public void testStartGame5() {
    ReversiModel model = new SquareReversi();
    model.startGame(3);
  }






  //GET CELL AT TESTS:

  //tests that the getCellAt method returns the correct cell
  @Test
  public void testGetCellAt1() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Cell expected = board.get(0).get(0);
    Assert.assertEquals(expected, model.getCellAtPosn(0, 0));
  }

  //tests that an exception is thrown if the positions are not in the game board.
  @Test (expected = IllegalArgumentException.class)
  public void testGetCellAt2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.getCellAtPosn(6, 0);
  }

  //tests that an exception is thrown if the game is not started.
  @Test (expected = IllegalStateException.class)
  public void testGetCellAt3() {
    ReversiModel model = new SquareReversi();
    model.getCellAtPosn(0, 0);
  }

  //tests that the getCellAt method returns the correct cell
  @Test
  public void testGetCellAt4() {
    ReversiModel model = new SquareReversi();
    model.startGame(8);
    ArrayList<ArrayList<Cell>> board = model.getGameBoard();
    Cell expected = board.get(0).get(0);
    Assert.assertEquals(expected, model.getCellAtPosn(0, 0));
  }






  //TESTS FOR PLAYER MOVE:

  //tests a valid move:
  // - that the player takes over the empty cell it is places at
  // - that the sandwiched opposite pieces are turned over
  // - that no pieces in the row outside the sandwich are turned over
  @Test
  public void testPlayerMove1() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(2, 0, PlayerEnum.X);

    Assert.assertEquals("X",
        model.getCellAtPosn(1, 1).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(2, 2).toString());
    Assert.assertEquals("X",
        model.getCellAtPosn(2, 0).toString());
    Assert.assertEquals("_",
        model.getCellAtPosn(0, 0).toString());
  }

  //tests player move:
  // - that the player switches after a move is made
  @Test
  public void testPlayerMove2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(2, 0, PlayerEnum.X);
    //checks that the player switches after move
    Assert.assertEquals("O", model.getPlayerTurn().toString());
    //player O makes a move.
    model.playerPass(PlayerEnum.O);
    Assert.assertEquals("X", model.getPlayerTurn().toString());

  }

  //tests player move:
  // - that an exception is thrown when the game is not started
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove3() {
    ReversiModel model = new SquareReversi();
    model.playerMove(2, 0, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when the coordinates are not valid
  @Test (expected = IllegalArgumentException.class)
  public void testPlayerMove4() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(2, 5, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when a player tries to place on a non-empty cell.
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove5() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(1, 1, PlayerEnum.X);
  }

  //tests player move:
  // - that an exception is thrown when the move is not valid.
  @Test (expected = IllegalStateException.class)
  public void testPlayerMove6() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(0, 0, PlayerEnum.X);
  }





  //TESTS FOR PLAYER PASS

  //tests than an exception is thrown if the game has not been started
  @Test (expected = IllegalStateException.class)
  public void testPlayerPass1() {
    ReversiModel model = new SquareReversi();
    model.playerPass(PlayerEnum.X);
  }

  //tests that the player turn changes after one has passed
  @Test
  public void testPlayerPass2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertEquals("X", model.getPlayerTurn().toString());
    model.playerPass(PlayerEnum.X);
    Assert.assertEquals("O", model.getPlayerTurn().toString());
  }

  //tests that the recent turn taken switches to false when a pass is made.
  @Test
  public void testPlayerPass3() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerPass(PlayerEnum.X);
    List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(true, false));
    Assert.assertEquals(list, model.getTurnsTaken());
  }

  //tests that the game board is not changed when a player passes.
  @Test
  public void testPlayerPass4() {
    ReversiModel model = new SquareReversi();
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
    ReversiModel model = new SquareReversi();
    model.isGameOver();
  }

  //tests that the game is not over when the game has been started
  @Test
  public void testGameOver2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertFalse(model.isGameOver());
  }

  //tests that the game is over when both players pass
  @Test
  public void testGameOver3() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerPass(PlayerEnum.X);
    Assert.assertFalse(model.isGameOver());
    model.playerPass(PlayerEnum.O);
    Assert.assertTrue(model.isGameOver());
  }


  //tests that the game over if the board is full
  @Test
  public void testGameOver4() {
    ReversiModel model = new SquareReversi();
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
    ReversiModel model = new SquareReversi();
    model.forcedPass();
  }

  //tests that a forced pass returns false when there are valid moves
  @Test
  public void testForcedPass2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertFalse(model.forcedPass());
  }





  //TESTS FOR GET WINNER:

  //tests that an exception is thrown if the game is not started
  @Test (expected = IllegalStateException.class)
  public void testGetWinner1() {
    ReversiModel model = new SquareReversi();
    model.getWinner();
  }

  //tests that an exception is thrown if the game is not over
  @Test (expected = IllegalStateException.class)
  public void testGetWinner2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.getWinner();
  }

  //tests that the correct winner is returned when two players pass
  @Test
  public void testGetWinner3() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerMove(2, 0, PlayerEnum.X);
    model.playerPass(PlayerEnum.O);
    model.playerPass(PlayerEnum.X);
    Assert.assertEquals(PlayerEnum.X, model.getWinner());
  }

  //tests that the empty player is returned in a tie
  @Test
  public void testGetWinner4() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    model.playerPass(PlayerEnum.X);
    model.playerPass(PlayerEnum.O);
    Assert.assertEquals(PlayerEnum.Empty, model.getWinner());
  }






  //TESTS FOR CHECK VALID

  //tests that a valid move is valid
  @Test
  public void testCheckValid() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);

    Assert.assertTrue(model.checkValid(2, 0));
  }


  //tests that a valid move is valid
  @Test
  public void testCheckValid2() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);

    Assert.assertFalse(model.checkValid(0, 0));
  }




  //TESTS FOR GET BOARD SIDE LENGTH

  //tests that get board sideLength returns the correct size
  @Test
  public void testGetBoardSideLength1() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertEquals(4, model.getBoardSideLength());
  }




  //TESTS FOR GET PLAYER SCORE

  //tests that get player score returns the number of occupied cells a player has
  @Test
  public void testGetPlayerScore1() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertEquals(2, model.getPlayerScore(PlayerEnum.X));
    Assert.assertEquals(2, model.getPlayerScore(PlayerEnum.O));
  }





  //TESTS FOR POSN IN CORNER

  @Test
  public void posnInCorner1() {
    ReversiModel model = new SquareReversi();
    model.startGame(4);
    Assert.assertTrue(model.posnInCorner(0, 0));
    Assert.assertTrue(model.posnInCorner(3, 0));
    Assert.assertTrue(model.posnInCorner(3, 3));
    Assert.assertTrue(model.posnInCorner(0, 3));
    Assert.assertFalse(model.posnInCorner(0, 1));
    Assert.assertFalse(model.posnInCorner(1, 0));
    Assert.assertFalse(model.posnInCorner(2, 1));
  }



  //TESTS FOR POSN NEXT TO CORNER

  @Test
  public void posnNextToCorner1() {
    ReversiModel model = new SquareReversi();
    model.startGame(6);
    Assert.assertTrue(model.posnNextToCorner(1, 0));
    Assert.assertTrue(model.posnNextToCorner(0, 1));
    Assert.assertTrue(model.posnNextToCorner(1, 1));
    Assert.assertTrue(model.posnNextToCorner(5, 4));
    Assert.assertTrue(model.posnNextToCorner(5, 1));
    Assert.assertTrue(model.posnNextToCorner(5, 5));
    Assert.assertFalse(model.posnNextToCorner(2, 2));
    Assert.assertFalse(model.posnNextToCorner(3, 2));
    Assert.assertFalse(model.posnNextToCorner(3, 1));
    Assert.assertFalse(model.posnNextToCorner(4, 2));
  }




}
