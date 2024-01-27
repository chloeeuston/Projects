import model.SquareReversi;



/**
 * A mock class of SquareReversi created to test strategies. Consists of a String Builder
 * that receives messages for every method called in different strategies.
 */
public class MockSquareReversi extends SquareReversi {

  StringBuilder out;

  public MockSquareReversi(StringBuilder out) {
    super();
    this.out = out;
  }


  @Override
  public boolean checkValid(int diagonalPos, int rowPos) {
    out.append("checking if it is a valid move at the position ("
        + diagonalPos + ", " + rowPos + ")" + "\n");

    return super.checkValid(diagonalPos, rowPos);
  }

  @Override
  public int countPiecesGained(int diagonalPos, int rowPos) {
    out.append("checking the number of pieces the current player "
        + "would gain by moving to the position (" + diagonalPos + ", " + rowPos + ")" + "\n");
    return super.countPiecesGained(diagonalPos, rowPos);
  }


  @Override
  public boolean posnInCorner(int diagonalPos, int rowPos) {
    out.append("checking if the position (" + diagonalPos + ", " + rowPos +
        ") is in a corner position." + "\n");
    return super.posnInCorner(diagonalPos, rowPos);
  }


  @Override
  public boolean posnNextToCorner(int diagonalPos, int rowPos) {
    out.append("checking if the position (" + diagonalPos + ", " + rowPos +
        ") is next to a corner position." + "\n");
    return super.posnNextToCorner(diagonalPos, rowPos);
  }


  public String outToString() {
    return this.out.toString();
  }

  public void printTranscript() {
    System.out.print(out);
  }

}
