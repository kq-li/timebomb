import java.awt.*;
import javax.swing.*;

public class BombTile extends Rectangle implements Tile {
  protected int _x, _y, _width, _height, _id, _time;

  public BombTile(int x, int y, int width, int height, int id) {
    _x = x;
    _y = y;
    _width = width;
    _height = height;
    _id = id;
  }
}
