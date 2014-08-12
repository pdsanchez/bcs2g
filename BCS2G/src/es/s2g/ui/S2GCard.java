package es.s2g.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

import es.s2g.S2GBarcodeFactory;
import es.s2g.S2GBarcodeFactory.Id;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;

public class S2GCard {
  public static final Color COLOR_BG = new Color(0x004B66);

  private JFrame frame;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          S2GCard window = new S2GCard();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public S2GCard() {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }

    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 600, 350);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);

    frame.getContentPane().setBackground(COLOR_BG);

    CurvesPanel curvesPanel = new CurvesPanel();
    BorderLayout borderLayout = (BorderLayout) curvesPanel.getLayout();
    borderLayout.setVgap(15);
    borderLayout.setHgap(25);
    frame.getContentPane().add(curvesPanel);

    final PanelImg panelImg = new PanelImg();
    panelImg.setOpaque(false);
    curvesPanel.add(panelImg, BorderLayout.CENTER);

    JPanel panelOpt = new JPanel();
    panelOpt.setOpaque(false);
    curvesPanel.add(panelOpt, BorderLayout.WEST);
    panelOpt.setLayout(new MigLayout("", "[grow]", "[15,bottom][][25,bottom][][25,bottom][][15][]"));

    JLabel lblId = new JLabel("Identificador");
    lblId.setFont(new Font("Tahoma", Font.BOLD, 11));
    lblId.setForeground(Color.WHITE);
    panelOpt.add(lblId, "cell 0 0");

    final JComboBox cbId = new JComboBox(S2GBarcodeFactory.Id.values());
    cbId.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        Id id = (Id)cb.getSelectedItem();
//        panelImg.setSymbol(id.getImg());
        panelImg.setId(id);
      }
    });
    cbId.setSelectedIndex(0);
    panelOpt.add(cbId, "cell 0 1,growx");

    JLabel lblTurbo = new JLabel("Turbo");
    lblTurbo.setFont(new Font("Tahoma", Font.BOLD, 11));
    lblTurbo.setForeground(Color.WHITE);
    panelOpt.add(lblTurbo, "cell 0 2,alignx left,aligny bottom");

    final JSlider slTurbo = new JSlider();
    slTurbo.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        //if (!slTurbo.getValueIsAdjusting()) {
          panelImg.setTurbo(slTurbo.getValue());
          panelImg.repaint();
        //}
      }
    });
    slTurbo.setPaintLabels(true);
    slTurbo.setPaintTicks(true);
    slTurbo.setFont(new Font("Tahoma", Font.PLAIN, 9));
    slTurbo.setForeground(Color.WHITE);
    slTurbo.setSnapToTicks(true);
    slTurbo.setMinimum(1);
    slTurbo.setMaximum(6);
    slTurbo.setMajorTickSpacing(1);
    slTurbo.setMinorTickSpacing(1);
    slTurbo.setValue(1);
    slTurbo.setOpaque(false);
    panelOpt.add(slTurbo, "cell 0 3");

    JLabel lblPower = new JLabel("Potencia");
    lblPower.setFont(new Font("Tahoma", Font.BOLD, 11));
    lblPower.setForeground(Color.WHITE);
    panelOpt.add(lblPower, "cell 0 4");

    final JSlider slPower = new JSlider();
    slPower.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        //if (!slPower.getValueIsAdjusting()) {
          panelImg.setPower(slPower.getValue());
          panelImg.repaint();
        //}
      }
    });
    slPower.setMinorTickSpacing(1);
    slPower.setMajorTickSpacing(1);
    slPower.setFont(new Font("Tahoma", Font.PLAIN, 9));
    slPower.setForeground(Color.WHITE);
    slPower.setSnapToTicks(true);
    slPower.setValue(1);
    slPower.setMinimum(1);
    slPower.setMaximum(12);
    slPower.setPaintLabels(true);
    slPower.setPaintTicks(true);
    slPower.setOpaque(false);
    panelOpt.add(slPower, "cell 0 5");

    JButton btnSave = new JButton("Guardar");
    btnSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("png", "png"));

        Id id = (Id)cbId.getSelectedItem();
        int pw = slPower.getValue();
        int tb = slTurbo.getValue();
        String fname = id.getName().replaceAll(" ", "_") + "_" + tb + "_" + pw + ".png";
        fileChooser.setSelectedFile(new File(fname));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          String name = file.getName();
          name = (name.endsWith(".png")) ? name : name+".png";

          file = new File(file.getParent(), name);

          // save
          try {
            ImageIO.write(panelImg.createCard4print(), "png", file);
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    });
    panelOpt.add(btnSave, "flowx,cell 0 7,alignx center");

    JButton btnImprimir = new JButton("Imprimir");
    btnImprimir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(MediaSizeName.ISO_A4);
        aset.add(OrientationRequested.LANDSCAPE);
        aset.add(Chromaticity.MONOCHROME);

        job.setPrintable(new InnerPrinter(panelImg.createCard4print()));
        boolean ok = job.printDialog(aset);
        if (ok) {
          try {
            job.print();
          } catch (PrinterException ex) {
            /* The job did not successfully complete */
          }
        }
      }
    });
    panelOpt.add(btnImprimir, "cell 0 7");
  }

  private class InnerPrinter implements Printable {
    private BufferedImage img;

    public InnerPrinter(BufferedImage img) {
      this.img = img;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
      // We have only one page, and 'page' is zero-based
      if (page > 0) {
        return NO_SUCH_PAGE;
      }

      //pf.setOrientation(PageFormat.LANDSCAPE);
      //pf.getPaper().s

      // User (0,0) is typically outside the imageable area, so we must translate
      // by the X and Y values in the PageFormat to avoid clipping.
      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pf.getImageableX(), pf.getImageableY());

      // Now we perform our rendering
      g2d.drawImage(img, 0, 0, null);

      // tell the caller that this page is part of the printed document
      return PAGE_EXISTS;
    }
  }
}
