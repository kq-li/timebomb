import javax.swing.*;
import java.awt.*;

public class Tile extends Rectangle {
  protected int _x, _y, _size;
  protected boolean _marked, _revealed;
  protected Color _color, _origColor;
  
  public Tile(int x, int y, int size) {
    super(x, y, size, size);
    this._x = x;
    this._y = y;
    this._size = size;
    this._revealed = false;
    this._color = Color.GRAY;
    this._origColor = Color.GRAY;
  }

  public void hover() {
    if (!this._marked && !this._revealed)
      this._color = new Color(200, 200, 200);
  }

  public void unhover() {
    if (!this._marked && !this._revealed)
      this._color = this._origColor;
  }

  public void toggleMark() {
    if (this._marked)
      this.unmark();
    else
      this.mark();
  }
  
  public void mark() {
    if (!this._revealed) {
      this._color = Color.RED;
      this._marked = true;
    }
  }

  public void unmark() {
    this._color = Color.GRAY;
    this._marked = false;
  }
    
  public void reveal() {
    if (!this._marked) {
      this._color = Color.WHITE;
      this._revealed = true;
    }
  }
}
  
