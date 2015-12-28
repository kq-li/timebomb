import javax.swing.*;
import java.awt.*;

public class Tile extends Rectangle {
  protected int _x, _y, _size;
  protected Color _color;

  public Tile(int x, int y, int size, Color color) {
    super(x, y, size, size);
    this._x = x;
    this._y = y;
    this._size = size;
    this._color = color;
  }
}
  
