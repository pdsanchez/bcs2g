package es.s2g.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.output.OutputException;
import es.s2g.S2GBarcodeFactory;
import es.s2g.S2GBarcodeFactory.Id;

public final class ImgFactory {
  private static int pxh = 75;
  private static int pxw = 5;

  private static int COVERWIDTH = 240;
  private static int COVERHEIGHT = 55;

  private ImgFactory() { }

//  static {
//    int dpi = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();dpi=86;
//    pxw = (int) Math.round(mm2px(1.5, dpi));
//    pxh = (int) Math.round(mm2px(22.0, dpi));
//  }

  public static BufferedImage createTurboBarcode(int level) {
    return createPowerBarcode(null, level);
  }

  public static BufferedImage createPowerBarcode(Id id, int level) {
    BufferedImage bufferedImage = new BufferedImage(COVERWIDTH, COVERHEIGHT, BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D graphics = bufferedImage.createGraphics();

    // fill the background
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setPaint(Color.white);
    graphics.fillRect(0, 0, COVERWIDTH, COVERHEIGHT);

    try {
      Barcode bcode = null;
      if (id == null) {
        bcode = S2GBarcodeFactory.createTurboCode(level);
      }
      else {
        bcode = S2GBarcodeFactory.createPowerCode(id, level);
      }
      bcode.setBarHeight(pxh);
      bcode.setBarWidth(pxw);
      bcode.setDrawingText(false);

      bcode.draw(graphics, 0, 0);
    }
    catch (BarcodeException e) {
      e.printStackTrace();
    } catch (OutputException e) {
      e.printStackTrace();
    }

    graphics.dispose();

    return bufferedImage;
  }

  public static BufferedImage createID(Id id) {
    BufferedImage bufferedImage = new BufferedImage(200, 30, BufferedImage.TRANSLUCENT);
    Graphics2D graphics = bufferedImage.createGraphics();

    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    graphics.drawImage(id.getImg().getImage(), 0, 0, null);

    graphics.setColor(Color.black);
    Font titleFont = new Font("Verdana", Font.BOLD, 11);
    graphics.setFont(titleFont);
    graphics.drawString(id.getName(), 40, 20);

    graphics.dispose();

    return bufferedImage;
  }

  public static BufferedImage createChart(int val, int max, Color color1, Color color2) {
    int title = 15;
    int clientWidth = 60 + title;
    int clientHeight = 40;
    int barWidth = (clientWidth - title) / max;

    BufferedImage bufferedImage = new BufferedImage(clientWidth, clientHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = bufferedImage.createGraphics();

    // fill the background
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setPaint(Color.white);
    graphics.fillRect(0, 0, clientWidth, clientHeight);

    double minValue = 0;
    double maxValue = max;

    int top = 1;
    int bottom = 1;
    double scale = (clientHeight - top - bottom) / (maxValue - minValue);

    for (int j = 1; j <= max; j++) {
      int valueP = (j-1) * barWidth + 1 + title;
      int valueQ = top;
      int height = (int) Math.ceil(j * scale);
      valueQ += (int) ((maxValue - j) * scale);

      if (j <= val) {
        int steps = max;
        float ratio = (float) (j-1) / (float) steps;
        int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
        int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
        int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
        Color stepColor = new Color(red, green, blue);
        graphics.setColor(stepColor);
        graphics.fillRect(valueP, valueQ, barWidth - 2, height);
      }
      graphics.setColor(Color.black);
      graphics.drawRect(valueP, valueQ, barWidth - 2, height);
    }

    Font titleFont = new Font("Verdana", Font.BOLD, 11);
//    FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
//    Rectangle2D rect = titleFontMetrics.getStringBounds(String.valueOf(val), graphics);
//    int titleWidth = titleFontMetrics.stringWidth(String.valueOf(val));
//    int titleAscent = titleFontMetrics.getAscent();
//    if (titleAscent > titleWidth) {
//      titleWidth = titleAscent;
//    }
//
//    graphics.setColor(color2);
//    graphics.fillOval(5, 5, titleWidth + 6, titleWidth + 6);
//
//    graphics.setFont(titleFont);
//    graphics.setColor(Color.white);
//    graphics.drawString(String.valueOf(val), (int) (6 + (titleWidth + 5) / 2 - rect.getWidth() / 2),
//            (int) (3 + (titleWidth + 5) / 2 + rect.getHeight() / 2));

    graphics.setFont(titleFont);
    graphics.drawString(String.valueOf(val), 2, clientHeight - 6);

    graphics.dispose();

    return bufferedImage;
  }

//  /**
//   * Converts millimeters (mm) to pixels (px)
//   * @param mm the value in mm
//   * @param resolution the resolution in dpi (dots per inch)
//   * @return the value in pixels
//   */
//  private static double mm2px(double mm, int resolution) {
//    // Converts millimeters (mm) to inches (in) -> mm / IN2MM;
//    // conversion factor from millimeters to inches -> IN2MM = 25.4f;
//      return (mm / 25.4f) * resolution;
//  }

}
