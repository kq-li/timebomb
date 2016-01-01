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
          this._tiles[x][y] = new SafeTile(this.toCanvasCoord(x),
                                       this.toCanvasCoord(y),
                                       this._tileSize,
                                       this.getClosestBombDist(x, y));
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

  public Tile[] getTilesInRadius(int x, int y, int r) {
    Tile[] neighbors = getNeighbors(x, y);
    if (r == 0) {
      return new Tile[0];
    } else {
      ArrayList<Tile> tiles = new ArrayList<Tile>(Arrays.asList(neighbors));

      for (Tile neighbor : neighbors)
        tiles.addAll(Arrays.asList(getTilesInRadius(this.toTileCoord(neighbor._x),
                                                    this.toTileCoord(neighbor._y),
                                                    r - 1)));

      return tiles.toArray(new Tile[tiles.size()]);
    }
  }

  public Tile[] getNeighbors(int x, int y) {
    ArrayList<Tile> tiles = new ArrayList<Tile>();

    if (x > 0)
      tiles.add(this._tiles[x - 1][y]);
    if (x < this._maxX - 1)
      tiles.add(this._tiles[x + 1][y]);
    if (y > 0)
      tiles.add(this._tiles[x][y - 1]);
    if (y < this._maxY - 1)
      tiles.add(this._tiles[x][y + 1]);

    return tiles.toArray(new Tile[tiles.size()]);
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
          if (tile instanceof SafeTile) {
            SafeTile safe = (SafeTile) tile;
            String dist = "" + safe._dist;
            g2.setFont(this._font);
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(dist, g2);
            g2.drawString(dist,
                          safe._x + (this._tileSize - (int) r.getWidth()) / 2, 
                          safe._y + (this._tileSize - (int) r.getHeight()) / 2 + fm.getAscent());
          } else {
            BombTile bomb = (BombTile) tile;
            String id = "" + bomb._id;
            g2.setFont(this._font);
            g2.setColor(Color.RED);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(id, g2);
            g2.drawString(id,
                          bomb._x + (this._tileSize - (int) r.getWidth()) / 2,
                          bomb._y + (this._tileSize - (int) r.getHeight()) / 2 + fm.getAscent());
          }
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
    if (this._currentTile != null) {
      this._currentTile.unhover();
      this._currentTile = null;
    } else {
      int tileX = this.toTileCoord(e.getX());
      int tileY = this.toTileCoord(e.getY());
      this._currentTile = this._tiles[tileX][tileY];
      this._currentTile.hover();
    }
    this.repaint();
  }

  public void mouseReleased(MouseEvent e) {
    if (this._currentTile != null) {
      if (SwingUtilities.isLeftMouseButton(e)) {
        if (this._currentTile instanceof BombTile) {
          for (BombTile bomb : this._bombs)
            bomb.reveal();
        } else if (this._currentTile._revealed) {
          Tile[] notBombs = this.getTilesInRadius(this.toTileCoord(this._currentTile._x),
                                                   this.toTileCoord(this._currentTile._y),
                                                   ((SafeTile) this._currentTile)._dist - 1);
          for (Tile notBomb : notBombs)
            notBomb.reveal();
        } else {
          this._currentTile.reveal();
        }
      } else if (SwingUtilities.isRightMouseButton(e)) {
        this._currentTile.toggleMark();
      }
      this._currentTile = null;
      this.repaint();
    }
  }

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mouseClicked(MouseEvent e) {}

  public void mouseMoved(MouseEvent e) {}

  public void mouseDragged(MouseEvent e) {
    if (this._currentTile != null) {
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
