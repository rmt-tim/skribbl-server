package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import org.json.JSONObject;

public class Igrac extends Thread{
	
	String ime;
	Socket soket = null;
	BufferedReader clientInput = null;
	PrintStream clientOutput = null;
	
	public Igrac(Socket soket) {
		this.soket = soket;
	}
	
	public void run() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			clientOutput = new PrintStream(soket.getOutputStream());
			ime = clientInput.readLine();
			
			LinkedList<String> imenaIgraca = new LinkedList<String>();
			for (Igrac i : Server.igraci) {
				imenaIgraca.push(i.ime);
			}
			
			JSONObject odgovor = new JSONObject();
			odgovor.put("tip", "lista_igraca");
			odgovor.put("igraci", imenaIgraca);
			System.out.println(odgovor);
			
			for (Igrac i : Server.igraci) {
				i.clientOutput.println(odgovor.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
