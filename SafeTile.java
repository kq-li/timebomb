import java.awt.*;
import javax.swing.*;

public class SafeTile extends Tile {
  protected int _dist;
  
  public SafeTile(int x, int y, int size, int dist) {
    super(x, y, size);
    this._dist = dist;
  }
}
