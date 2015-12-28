import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
  protected JFrame _frame;
  protected Tile[][] _tiles;
  protected int _maxX, _maxY, _numBombs, _tileWidth, _tileHeight, _frameWidth, _frameHeight;
  protected final String title = "TimeBomb";

  public Game() {
    this(10, 10, 5, 30, 30, 600, 400);
  }

  public Game(int maxX, int maxY, int numBombs, int tileWidth, int tileHeight,
              int frameWidth, int frameHeight) {
    this._maxX = maxX;
    this._maxY = maxY;
    this._numBombs = numBombs;
    this._tileWidth = tileWidth;
    this._tileHeight = tileHeight;
    this._frameWidth = frameWidth;
    this._frameHeight = frameHeight;
    this.init();
  }

  public void init() {
    this._tiles = new Tile[this._maxX][this._maxY];
    
    for (int i = 0; i < this._numBombs; i++) {
      int x, y;
      do {
        x = (int) (Math.random() * this._maxX);
        y = (int) (Math.random() * this._maxY);
      } while (this._tiles[x][y] != null);
      this._tiles[x][y] = new BombTile(x, y, this._tileWidth, this._tileHeight, i);
    }

    for (int x = 0; x < this._maxX; x++) {
      for (int y = 0; y < this._maxY; y++) {
        if (this._tiles[x][y] == null) {
          this._tiles[x][y] = new SafeTile(x, y, this._tileWidth, this._tileHeight);
        }
      }
    }

    this._frame = new JFrame(title);
    this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this._frame.setSize(this._frameWidth, this._frameHeight);
    this._frame.setContentPane(this);
    this.setLayout(new GridBagLayout());
  }

  public void addComponent(Component c, GridBagConstraints gbc) {
    this.add(c, gbc);
  }
  
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
  }
  
  public void display() {
    this._frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    game.display();
  }
}
