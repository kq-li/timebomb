import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements MouseListener {
  protected JFrame _frame;
  protected Tile[][] _tiles;
  protected int _maxX, _maxY, _numBombs, _tileSize, _tileSpacing, _frameWidth, _frameHeight;
  protected Font _font;
  protected static final String TITLE = "TimeBomb";
  protected static final String FONT_NAME = "Droid Sans";
  protected static final int FONT_STYLE = Font.PLAIN;

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
    this._font = new Font(Game.FONT_NAME, Game.FONT_STYLE, tileSize);
    this.initTiles();
    this.initFrame();
    this.addMouseListener(this);
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
          this._tiles[x][y] = new SafeTile(this.toCanvasCoord(x), this.toCanvasCoord(y),
                                           this._tileSize);
        }
      }
    }
  }

  public void initFrame() {
    this._frame = new JFrame(Game.TITLE);
    this._frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this._frame.setContentPane(this);
    this.setLayout(new GridBagLayout());
  }

  public int toCanvasCoord(int n) {
    return n * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2;
  }

  public int toTileCoord(int n) {
    return (n - this._tileSpacing / 2) / (this._tileSize + this._tileSpacing);
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
        Tile tile = this._tiles[x][y];
        if (tile._revealed) {
          g2.setFont(this._font);
          g2.setColor(Color.BLACK);
          g2.drawString("hi", tile._x, tile._y + this._tileSize);
        } else {
          g2.setColor(tile._color);
          g2.fill(tile);
        }
      }
    }
  }
    
  public void display() {
    this._frame.pack();
    this._frame.setVisible(true);
  }

  public void mousePressed(MouseEvent e) {
    int tileX = this.toTileCoord(e.getX());
    int tileY = this.toTileCoord(e.getY());
    Tile clicked = this._tiles[tileX][tileY];
    clicked.reveal();
    this.repaint();
  }

  public void mouseReleased(MouseEvent e) {}

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mouseClicked(MouseEvent e) {}
  
  public static void main(String[] args) {
    Game game = new Game();
    game.display();
  }
}
