import javax.swing.*;
import java.awt.*;

public class GBCBuilder {
  private GridBagConstraints _gbc;

  public GBCBuilder() {
    _gbc = new GridBagConstraints();
  }

  public GBCBuilder gridx(int gridx) {
    _gbc.gridx = gridx;
    return this;
  }
  
  public GBCBuilder gridy(int gridy) {
    _gbc.gridy = gridy;
    return this;
  }

  public GBCBuilder gridwidth(int gridwidth) {
    _gbc.gridwidth = gridwidth;
    return this;
  }

  public GBCBuilder gridheight(int gridheight) {
    _gbc.gridheight = gridheight;
    return this;
  }

  public GBCBuilder fill(int fill) {
    _gbc.fill = fill;
    return this;
  }

  public GBCBuilder ipadx(int ipadx) {
    _gbc.ipadx = ipadx;
    return this;
  }

  public GBCBuilder ipady(int ipady) {
    _gbc.ipady = ipady;
    return this;
  }

  public GBCBuilder insets(Insets insets) {
    _gbc.insets = insets;
    return this;
  }

  public GBCBuilder anchor(int anchor) {
    _gbc.anchor = anchor;
    return this;
  }

  public GridBagConstraints buildGBC() {
    return _gbc;
  }
}
