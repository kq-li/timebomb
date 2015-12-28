import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {
  protected JFrame _frame;
  protected Tile[][] _tiles;
  protected int _maxX, _maxY, _numBombs, _tileSize, _tileSpacing, _frameWidth, _frameHeight;
  protected final String title = "TimeBomb";

  public Game() {
    this(40, 40, 100, 18, 2, 800, 800);
  }

  public Game(int maxX, int maxY, int numBombs, int tileSize, int tileSpacing,
              int frameWidth, int frameHeight) {
    this._maxX = maxX;
    this._maxY = maxY;
    this._numBombs = numBombs;
    this._tileSize = tileSize;
    this._tileSpacing = tileSpacing;
    this._frameWidth = frameWidth;
    this._frameHeight = frameHeight;
    this.initTiles();
    this.initFrame();
  }

  public void initTiles() {
    this._tiles = new Tile[this._maxX][this._maxY];
    
    for (int i = 0; i < this._numBombs; i++) {
      int x, y;
      do {
        x = (int) (Math.random() * this._maxX);
        y = (int) (Math.random() * this._maxY);
      } while (this._tiles[x][y] != null);
      this._tiles[x][y] = new BombTile(x * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                       y * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                       this._tileSize, i);
    }

    for (int x = 0; x < this._maxX; x++) {
      for (int y = 0; y < this._maxY; y++) {
        if (this._tiles[x][y] == null) {
          this._tiles[x][y] = new SafeTile(x * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                           y * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                           this._tileSize);
        }
      }
    }
  }

  public void initFrame() {
    this._frame = new JFrame(title);
    this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this._frame.setContentPane(this);
    this.setLayout(new GridBagLayout());
  }

  public void addComponent(Component c, GridBagConstraints gbc) {
    this.add(c, gbc);
  }

  public Dimension getPreferredSize() {
    return new Dimension(this._frameWidth, this._frameHeight);
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    for (int x = 0; x < this._maxX; x++) {
      for (int y = 0; y < this._maxY; y++) {
        g2.setColor(this._tiles[x][y]._color);
        g2.fill(this._tiles[x][y]);
      }
    }
  }
    
  public void display() {
    this._frame.pack();
    this._frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    game.display();
  }
}
