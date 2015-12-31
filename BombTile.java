import java.awt.*;
import javax.swing.*;

public class BombTile extends Tile {
  protected int _id, _time;

  public BombTile(int x, int y, int size, int id) {
    super(x, y, size, Color.RED);
    this._id = id;
  }
}
