import java.awt.*;
import javax.swing.*;

public class SafeTile extends Tile {
  protected int _target, _dist;
  
  public SafeTile(int x, int y, int size) {
    super(x, y, size, Color.BLACK);
  }
}
