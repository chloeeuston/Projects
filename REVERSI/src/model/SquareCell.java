package model;

import java.util.Objects;


/**
 * A class that represents a single cell in a game of square reversi.
 */
public class SquareCell implements Cell {
  //the state of a cell: whether it is occupied by player X, O, or empty
  private PlayerEnum state;
  //diagonal position (q axial coordinate) of the cell on a reversi game board
  private final int xPos;
  //row position (r axial coordinate) of the cell on a reversi game board
  private final int yPos;
  private boolean isSelected;


  /**
   * constructor.
   * NOTE: the game board coordinates in square reversi work so that 0,0 is the top left.
   * x coordinates move to the right, y coordinates move down.
   * @param xPos the x position of the board.
   * @param yPos the y position of the board.
   */
  public SquareCell(int xPos, int yPos) {
    this.state = PlayerEnum.Empty;
    this.xPos = xPos;
    this.yPos = yPos;
    this.isSelected = false;
  }


  @Override
  public int getDiagonalPos() {
    return xPos;
  }

  @Override
  public int getRowPos() {
    return yPos;
  }

  @Override
  public void changeCellState(PlayerEnum newState) {
    this.state = newState;
  }

  @Override
  public String toString() {
    return this.state.toString();
  }

  @Override
  public int getSVal() {
    return xPos - yPos;
  }

  @Override
  public boolean isEmpty() {
    return this.state == PlayerEnum.Empty;
  }

  @Override
  public boolean selected() {
    return isSelected;
  }

  @Override
  public void selectCell() {
    this.isSelected = !this.isSelected;
  }


  /**
   * overrides the equal method for comparing instances of hexagon cells.
   * @param other instance of an object.
   * @return true if the toString of this and the object are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof SquareCell) {
      return this.toString().equals(other.toString());
    }
    else {
      return false;
    }
  }

  /**
   * Create a unique number per object that exists.
   * Required method for any object.
   * @return a unique int for each object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(state, xPos, xPos, isSelected);
  }


}
