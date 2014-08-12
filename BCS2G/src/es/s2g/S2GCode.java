package es.s2g;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BlankModule;
import net.sourceforge.barbecue.Module;
import net.sourceforge.barbecue.linear.LinearBarcode;


public class S2GCode extends LinearBarcode {
	private static final long serialVersionUID = 1L;

	public S2GCode(String code) throws BarcodeException {
		super(code);
	}

	@Override
	protected Module calculateChecksum() {
		return null;
	}

	@Override
	protected Module[] encodeData() {
		List<Module> modules = new ArrayList<Module>();
		for (int i = 0; i < data.length(); i++) {
			char c = data.charAt(i);

			// Barra gruesa
			if (c == '0') {
				if (i > 0) {
					modules.add(new BlankModule(0));
				}
				Module module = new Module(new int[] { 2, 1 });
				modules.add(module);
			}
			// Barra fina
			else if (c == '1') {
				modules.add(new BlankModule(1));
				//modules.add(new SeparatorModule(0));

				Module module = new Module(new int[] { 1, 1 });
				modules.add(module);
			}
		}

		return (Module[]) modules.toArray(new Module[0]);
	}

	@Override
	protected Module getPostAmble() {
		// Principio: 3 barras (1 gruesa, 2 finas)
		return new Module(new int[] { 2, 1, 1, 1, 1, 5 });
	}

	@Override
	protected Module getPreAmble() {
		// Final: bloque negro
		return new Module(new int[] { 7, 1 });
	}
}
