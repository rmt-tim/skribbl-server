package paket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

	public static LinkedList<Igrac> igraci = new LinkedList<Igrac>();
	
	
	public static void main(String[] args) {
		int port = 9003;

		ServerSocket serverSoket = null;
		Socket soket = null;
		

		
		try {
			// soket na portu 9000
			serverSoket = new ServerSocket(port);
			while (true) {
				System.out.println("Povezujemo se sa sistemom...");
				soket = serverSoket.accept();
				System.out.println("Uspesno povezivanje");
				Igrac igrac = new Igrac(soket);
				igraci.add(igrac);
				System.out.println("dodat");
				igrac.start();	
				}
			
		} catch (IOException e) {
			System.out.println("Greska");
		}

	}
	
}
