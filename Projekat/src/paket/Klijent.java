package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

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

		String poruka;

		while (true) {
			poruka = serverInput.readLine();
			System.out.println(poruka);
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
