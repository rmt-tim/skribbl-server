package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Igrac extends Thread{
	
	String ime;
	Socket soket = null;
	BufferedReader clientInput = null;
	PrintStream clientOutput = null;
	
	public Igrac(Socket soket) {
		this.soket = soket;
	}

	public static void posaljiSvima(String korIme) {
		for (Igrac i : Server.igraci) {
			i.clientOutput.println(korIme);
		}
	}
	
	public void run() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			clientOutput = new PrintStream(soket.getOutputStream());
			clientOutput.println("Unesite Vase korisnicko ime: ");
			ime = clientInput.readLine();
			for (Igrac i : Server.igraci) {
				posaljiSvima(i.ime);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
