package view.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import model.Cell;


/**
 * A class to represent an image of a square in a game of SquareReversi.
 */
public class Square extends Path2D.Double {
  //the square cell that the image Square represents
  private final Cell sqCell;
  //the pixel x-coordinate of a cell (where it is placed in the panel)
  private final int centerX;
  //the pixel y-coordinate of a cell (where it is placed in the panel)
  private final int centerY;
  //if the square cell is clicked/highlighted
  private boolean isClicked;


  /**
   * constructor.
   */
  public Square(Cell sqCell, int centerX, int centerY) {
    this.sqCell = sqCell;
    this.centerX = centerX;
    this.centerY = centerY;
    this.isClicked = this.sqCell.selected();
  }

  /**
   * outlines a square shape of a given side length.
   * @param g2d the graphics that the square is drawn on (passed through in paint component)
   * @param sqSideLength the side length of the square
   */
  public void drawSquare(Graphics2D g2d, double sqSideLength) {
    int[] xPoints = new int[4];
    int[] yPoints = new int[4];

    //the angle at each vertex in a square (pi / 2)
    double angle = Math.PI / 2;

    for (int i = 0; i < 4; i++) {
      //the next vertex in a square
      int xCoord = centerX + (int) (0.75 * sqSideLength * Math.cos((angle * i) + angle / 2));
      int yCoord = centerY - (int) (0.75 * sqSideLength * Math.sin((angle * i) + angle / 2));
      //sets the array values to be the x and y points at each vertex
      xPoints[i] = xCoord;
      yPoints[i] = yCoord;
    }
    Polygon square = new Polygon(xPoints, yPoints, 4);
    //set the outline thickness
    g2d.setStroke(new BasicStroke(3.0f));
    //draw the square outline
    g2d.setColor(Color.BLACK);
    g2d.draw(square);
    //set the stroke back to default
    g2d.setStroke(new BasicStroke(1.0f));
    //sets the fill color and fill the square
    this.setCellColor(g2d);
    g2d.fill(square);
    //adds a game piece if the cell is occupied by a player
    this.addGamePiece(g2d, sqSideLength);
  }

  /**
   * determines what the fill color of the square cell should be. Is set to gray if
   * the cell is not click, and is set to cyan if the cell is clicked.
   * @param g2d the graphics that the square is drawn on (passed into drawSquare).
   */
  private void setCellColor(Graphics2D g2d) {
    if (this.isClicked) {
      g2d.setColor(Color.CYAN);
    }
    else {
      g2d.setColor(Color.GRAY);
    }
  }

  /**
   * adds a game piece at a position in the g2d
   * white circle if player O, black circle if player X, none if the cell is empty.
   * @param g2d the graphics that the circle is drawn on (passed into drawSquare).
   * @param sqSideLength the side length of a single square cell on the game board.
   */
  private void addGamePiece(Graphics2D g2d, double sqSideLength) {
    if (!sqCell.isEmpty()) {
      int radius = (int) sqSideLength / 3;
      int diameter = radius * 2;
      int circleX = centerX - radius;
      int circleY = centerY - radius;
      Ellipse2D.Double circle = new Ellipse2D.Double(circleX, circleY, diameter, diameter);

      if (sqCell.toString().equals("X")) {
        g2d.setColor(Color.BLACK);
      }
      else {
        g2d.setColor(Color.WHITE);
      }
      g2d.draw(circle);
      g2d.fill(circle);
    }
  }

  /**
   * changes the cell state so that it is clicked.
   */
  public void selectCell() {
    this.sqCell.selectCell();
  }


  public int getCenterX() {
    return this.centerX;
  }

  public int getCenterY() {
    return this.centerY;
  }

  public Cell getSquareCell() {
    return this.sqCell;
  }

  public boolean isClicked() {
    return this.isClicked;
  }


}
