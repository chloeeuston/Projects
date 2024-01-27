package view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import model.Cell;


/**
 * A decorated version of the panel view used when hint mode is enabled.
 * Alters the "paintComponent" method so that highlighted cells show the hint.
 */
public class ReversiPanelDecorator extends ReversiPanel {
  ReversiPanel panel;

  public ReversiPanelDecorator(ReversiPanel panel) {
    super(panel.getModel());
    this.panel = panel;
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Rectangle bounds = this.getBounds();
    double sideLength = this.getHexSideLength(bounds);
    Graphics2D g2d = (Graphics2D) graphics;

    //resets the board every time paint is called
    this.hexagons = new ArrayList<>();
    this.createHexagons(bounds);
    //draws the hexagons onto the panel
    for (Hexagon hex : hexagons) {
      hex.drawHex(g2d, sideLength);
      if (hex.isClicked()) {
        addHint(hex, g2d);
      }
    }
  }

  private void addHint(Hexagon hex, Graphics2D graphics) {
    Cell cell = hex.getHexCell();
    int diagonal = cell.getDiagonalPos();
    int row = cell.getRowPos();
    int x = hex.getCenterX();
    int y = hex.getCenterY();
    int hintNumber = model.countPiecesGained(diagonal, row);

    Font originalFont = graphics.getFont();
    Color originalColor = graphics.getColor();

    graphics.setFont(new Font("Arial", Font.BOLD, 16)); // Adjust the font as needed
    graphics.setColor(Color.BLACK);

    graphics.drawString(Integer.toString(hintNumber), x, y);

    graphics.setFont(originalFont);
    graphics.setColor(originalColor);
  }


}
