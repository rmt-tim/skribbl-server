package paket;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static final int port = 9004;

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			while (true) {
				new Thread(new Player(server.accept())).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.close();
			}
		}
	}
}