import javax.swing.*;
import java.awt.*;

public class Tile extends Rectangle {
  protected int _x, _y, _size;
  protected boolean _revealed;
  protected Color _color, _origColor;

  public Tile(int x, int y, int size, Color color) {
    super(x, y, size, size);
    this._x = x;
    this._y = y;
    this._size = size;
    this._revealed = false;
    this._color = color;
    this._origColor = color;
  }

  public void hover() {
    this._color = Color.GRAY;
  }

  public void unhover() {
    this._color = this._origColor;
  }

  public void reveal() {
    this._color = Color.WHITE;
    this._revealed = true;
  }
}
  
