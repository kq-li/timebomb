import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.*;

public class Game extends JPanel implements MouseListener, MouseMotionListener {
  protected int _maxX, _maxY, _numBombs, _tileSize, _tileSpacing, _frameWidth, _frameHeight;
  protected BombTile[] _bombs;
  protected JFrame _frame;
  protected Tile _currentTile;
  protected Tile[][] _tiles;
  protected Font _font;
  protected static final String TITLE = "TimeBomb";
  protected static final String FONT_NAME = "Ubuntu Medium";
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
    this.addMouseMotionListener(this);
  }

  public void initTiles() {
    this._currentTile = null;
    this._tiles = new Tile[this._maxX][this._maxY];
    this._bombs = new BombTile[this._numBombs];
    
    for (int i = 0; i < this._numBombs; i++) {
      int x, y;
      do {
        x = (int) (Math.random() * this._maxX);
        y = (int) (Math.random() * this._maxY);
      } while (this._tiles[x][y] != null);
      BombTile bomb = new BombTile(x * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                   y * (this._tileSize + this._tileSpacing) + this._tileSpacing / 2,
                                   this._tileSize, i);
      this._bombs[i] = bomb;
      this._tiles[x][y] = bomb;
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

  public int getClosestBombDist(int x, int y) {
    int minDist = -1;

    for (int i = 0; i < this._numBombs; i++) {
      BombTile bomb = this._bombs[i];
      int dist = getManhattanDistance(x, y, toTileCoord(bomb._x), toTileCoord(bomb._y));

      if (minDist < 0) {
        minDist = dist;
      } else {
        minDist = Math.min(dist, minDist);
      }
    }

    return minDist;
  }

  public BombTile[] getBombsAtDist(int x, int y, int dist) {
    ArrayList<BombTile> bombs = new ArrayList<BombTile>();
    
    for (int i = 0; i < this._numBombs; i++) {
      BombTile bomb = this._bombs[i];
      if (dist == getManhattanDistance(x, y, toTileCoord(bomb._x), toTileCoord(bomb._y)))
        bombs.add(bomb);
    }
    
    return bombs.toArray(new BombTile[bombs.size()]);
  }

  public int getManhattanDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
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
          String out = "" + this.getClosestBombDist(this.toTileCoord(tile._x),
                                                    this.toTileCoord(tile._y));
          FontMetrics fm = g2.getFontMetrics();
          Rectangle2D r = fm.getStringBounds(out, g2);
          g2.drawString(out, 
                        tile._x + (this._tileSize - (int) r.getWidth()) / 2, 
                        tile._y + (this._tileSize - (int) r.getHeight()) / 2 + fm.getAscent());
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
    if (SwingUtilities.isLeftMouseButton(e)) {
      int tileX = this.toTileCoord(e.getX());
      int tileY = this.toTileCoord(e.getY());
      this._currentTile = this._tiles[tileX][tileY];
      this._currentTile.hover();
      this.repaint();
    }
  }

  public void mouseReleased(MouseEvent e) {
    if (this._currentTile != null && SwingUtilities.isLeftMouseButton(e)) {
      int tileX = this.toTileCoord(e.getX());
      int tileY = this.toTileCoord(e.getY());
      this._currentTile.reveal();
      this._currentTile = null;
      this.repaint();
    }
  }

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mouseClicked(MouseEvent e) {
    if (this._currentTile != null && SwingUtilities.isRightMouseButton(e)) {
      this._currentTile.unhover();
      this._currentTile = null;
      this.repaint();
    }
  }

  public void mouseMoved(MouseEvent e) {}

  public void mouseDragged(MouseEvent e) {
    if (this._currentTile != null && SwingUtilities.isLeftMouseButton(e)) {
      int tileX = this.toTileCoord(e.getX());
      int tileY = this.toTileCoord(e.getY());
      Tile hovered = this._tiles[tileX][tileY];
      if (this._currentTile != hovered) {
        this._currentTile.unhover();
        this._currentTile = hovered;
        this._currentTile.hover();
        this.repaint();
      }
    }
  }
  
  public static void main(String[] args) {
    Game game = new Game();
    game.display();
  }
}
