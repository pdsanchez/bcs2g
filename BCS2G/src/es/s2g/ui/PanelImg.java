package es.s2g.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import es.s2g.S2GBarcodeFactory.Id;

public class PanelImg extends JPanel {
  private static final long serialVersionUID = 1L;

  private int turbo;
  private int power;
  private Id id = Id.WOLVER_LIGHTNING;

  private Image cover;

  public PanelImg() {
    cover = new ImageIcon(this.getClass().getResource("/resources/s2gtemplate.png")).getImage();
  }

  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    Graphics2D g2d = (Graphics2D) graphics;
    g2d.drawImage(createCard(), 50, 50, this);
  }

  private BufferedImage createCard() {
    BufferedImage bufferedImage = new BufferedImage(cover.getWidth(null), cover.getHeight(null), BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = bufferedImage.createGraphics();

    // fill the background
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setPaint(Color.white);
    g2d.fillRect(0, 0, cover.getWidth(null), cover.getHeight(null));

    // Barcode Turbo
    BufferedImage imgBarTurbo = ImgFactory.createTurboBarcode(turbo);
    AffineTransform att = new AffineTransform();
    att.translate(imgBarTurbo.getWidth() / 2, imgBarTurbo.getHeight() / 2);
    att.translate(-8, 0);
    att.rotate(Math.PI);
    att.translate(-imgBarTurbo.getWidth()/2, -imgBarTurbo.getHeight()/2);
    g2d.drawImage(imgBarTurbo, att, null);

    // Barcode Power
    BufferedImage imgBarPower = ImgFactory.createPowerBarcode(id, power);
    g2d.drawImage(imgBarPower, 9, 105, this);

    // Plantilla
    g2d.drawImage(cover, 0, 0, this);

    // Turbo
    BufferedImage imgTurbo = ImgFactory.createChart(turbo, 6, new Color(254, 189, 159), Color.red);
    AffineTransform at = new AffineTransform();
    at.translate(imgTurbo.getWidth() / 2, imgTurbo.getHeight() / 2);
    at.translate(3, 31);
    at.rotate(Math.PI);
    at.translate(-imgTurbo.getWidth()/2, -imgTurbo.getHeight()/2);
    g2d.drawImage(imgTurbo, at, null);

    // Power
    BufferedImage imgPower = ImgFactory.createChart(power, 12, Color.CYAN, new Color(17, 1, 146));
    g2d.drawImage(imgPower, 163, 85, this);

    // Text
    BufferedImage imgTxt = ImgFactory.createID(id);
    g2d.drawImage(imgTxt, 1, 71, this);

    AffineTransform atxt = new AffineTransform();
    atxt.translate(imgTxt.getWidth() / 2, imgTxt.getHeight() / 2);
    atxt.translate(40, 55);
    atxt.rotate(Math.PI);
    atxt.translate(-imgTxt.getWidth()/2, -imgTxt.getHeight()/2);
    g2d.drawImage(imgTxt, atxt, null);

    g2d.dispose();

    return bufferedImage;
  }

  public BufferedImage createCard4print() {
    int width = cover.getWidth(null) * 2 + 9;
    int height = cover.getHeight(null) + 2;

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = bufferedImage.createGraphics();

    // fill the background
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setPaint(Color.white);
    g2d.fillRect(0, 0, width, height);

    g2d.setPaint(Color.black);
    g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[] {2f}, 0f) );
    g2d.drawRect(0, 0, width-1, height-1);
    g2d.drawLine(cover.getWidth(null)+ 4, 0, cover.getWidth(null) + 4, height);

    BufferedImage img = createCard();
    g2d.drawImage(img, 2, 1, this);
    g2d.drawImage(img, cover.getWidth(null) + 7, 1, this);

    g2d.dispose();

    return bufferedImage;
  }

  /**
   * @param turbo the turbo to set
   */
  public void setTurbo(int turbo) {
    this.turbo = turbo;
  }

  /**
   * @param power the power to set
   */
  public void setPower(int power) {
    this.power = power;
  }

  /**
   * @param id the id to set
   */
  public void setId(Id id) {
    this.id = id;
  }
}
