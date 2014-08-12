package es.s2g;

import javax.swing.ImageIcon;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;

/**
 * This factory provides a standard way of creating barcodes.
 *
 * @author <a href="mailto:paudavy@yahoo.es">pdsanchez</a>
 */
public final class S2GBarcodeFactory {

	/**
	* You can't construct one of these.
	*/
	private S2GBarcodeFactory() {
	}

	public static Barcode createPowerCode(Id id, int level) throws BarcodeException {
		return new S2GCode(getBits(id.getBits(), level));
	}

	public static Barcode createTurboCode(int level) throws BarcodeException {
		return new S2GCode(getBits("0000", level));
	}

	public enum Id {
        WOLVER_LIGHTNING("Wolver Lightning", "Lobo", "0101"),
        STORM_SLAZOR("Storm Slazor", "Tiburon", "0010"),
        ROCK_GIAMOTH("Rock Giamoth", "Elefante", "0100"),
        GALAXY_FALGOR("Galaxy Falgor", "Aguila", "0001"),
        DARK_FALGOR("Dark Falgor", "Aguila", "0001"),
        DRAGNITE_GENESIS("Dragnite Genesis", "null", "1000"), //Dragon
        JAPE("Jape", "null", "1001"), //Gorila
        JUNGER("Junger", "Tigre", "1100"), //Tigre
        LEOPATRA("Leopatra", "null", "0011"), //Pantera
        SCORVILAIN("Scorvilain", "null", "1010"), //Escorpion
        VAMBAT("Vambat", "null", "0110"), //Murcielago
        JELOK("Jelok", "null", "1011"), //Calamar
        HISSTORIA("Hisstoria", "null", "0111"); //Cobra

        private String name;
        private String symbol;
        private String bits;
        private ImageIcon img;

        private Id(String name, String symbol, String bits) {
        	this.name = name;
        	this.symbol = symbol;
        	this.bits = bits;
        	this.img = new ImageIcon(this.getClass().getResource("/resources/" + symbol + ".png"));
        }

		public String getName() {
			return name;
		}

		public String getSymbol() {
			return symbol;
		}

		public String getBits() {
			return bits;
		}

    public ImageIcon getImg() {
      return img;
    }

    public String toString() {
		  return name;
		}
	};

	private static String getBits(String id, int level) {
		String bits = Integer.toBinaryString(level - 1);
		bits = ("0000" + bits).substring(bits.length());

		bits = id + " " + bits;

		// CRC
		int tch = 0;
    	for (char ch : bits.toCharArray()) {
    		if (ch == '1') {
    			tch++;
    		}
    	}
    	bits += (tch%2 == 0) ? " 0" : " 1";


		StringBuilder sb = new StringBuilder(bits);

		return sb.reverse().toString();
	}
}
