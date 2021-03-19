package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class Klijent implements Runnable {

	static Socket soket = null;
	static BufferedReader serverInput = null;
	static PrintStream serverOutpun = null;
	static BufferedReader unosSaTastature = null;

	static boolean kraj = false;

	public static void main(String[] args) throws Exception, IOException {

		soket = new Socket("localhost", 9003);
		unosSaTastature = new BufferedReader(new InputStreamReader(System.in));
		serverOutpun = new PrintStream(soket.getOutputStream());
		serverInput = new BufferedReader(new InputStreamReader(soket.getInputStream()));

		new Thread(new Klijent()).start();

		JSONObject poruka;

		while (true) {
			poruka = new JSONObject(serverInput.readLine());
			// Odstampaj primljenu poruku kao string
			System.out.println(poruka);
			// Odstampaj listu imena igraca
			if (poruka.getString("tip").contentEquals("lista_igraca")) {
				for (Object imeIgraca : poruka.getJSONArray("igraci")) {
					System.out.println(imeIgraca.toString());
				}
			}
		}

	}

	public void run() {
		String poruka;

		while (true) {

			try {
				poruka = unosSaTastature.readLine();
				serverOutpun.println(poruka);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
