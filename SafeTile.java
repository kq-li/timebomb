import java.awt.*;
import javax.swing.*;

public class SafeTile extends Rectangle implements Tile {
  protected int _x, _y, _width, _height, _id, _dist;
  
  public SafeTile(int x, int y, int width, int height) {
    super(x, y, width, height);
    this._x = x;
    this._y = y;
    this._width = width;
    this._height = height;
  }
}
