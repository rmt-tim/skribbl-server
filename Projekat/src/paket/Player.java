package paket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Player implements Runnable {
	static String[] words = { "soliter", "paradajz", "kompjuter", "telefon", "amerika", "fudbal", "so" };
	static int minimumPlayerCount = 2;
	
	static ArrayList<Player> players = new ArrayList<Player>();
	static boolean gameInProgress = false;
	static String word;
	static int drawerIndex = 0;

	Socket socket;
	BufferedReader input;
	PrintStream output;
	String username = "<unnamed>";

	public Player(Socket socket) throws IOException {
		this.socket = socket;
		this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.output = new PrintStream(this.socket.getOutputStream());
		addToPlayers(this);
		System.out.println("A new player connected.");
	}

	@Override
	public void run() {
		try {
			for (;;) {
				String payload = input.readLine();
				System.out.println("Received payload: " + payload);
				JSONObject message = new JSONObject(payload);
				handleMessage(this, message);
			}
		} catch (IOException e) {
			System.out.println("Connection with user " + username + " closed.");
		} finally {
			onPlayerDisconnect(this);
			try {
				this.socket.close();
			} catch (IOException e) {
				System.out.println("Failed to close the user socket.");
			}
		}
	}

	private static synchronized void addToPlayers(Player player) {
		players.add(player);
	}
	
	private static synchronized void onPlayerDisconnect(Player player) {
		players.remove(player);
		
		ArrayList<String> usernames = new ArrayList<String>();
		for (Player p : players) {
			usernames.add(p.username);
		}
		
		if (players.size() < minimumPlayerCount) {
			gameInProgress = false;
			JSONObject message = new JSONObject();
			message.put("type", "gameAborted");
			message.put("usernames", usernames);
			broadcastMessage(message);
		} else {
			JSONObject message = new JSONObject();
			message.put("type", "usernameList");
			message.put("usernames", usernames);
			broadcastMessage(message);
		}
	}

	private static synchronized void handleMessage(Player player, JSONObject message) {
		String type = message.getString("type");
		switch (type) {
		case "username":
			player.handleUsername(message.getString("username"));
			break;
		case "startGame":
			player.handleStartGame();
			break;
		case "line":
			player.handleLine(message);
			break;
		case "endLine":
			player.handleEndLine(message);
			break;
		case "guess":
			player.handleGuess(message.getString("word"));
			break;
		default:
			System.out.println("handleMessage: Unknown message type (" + type + ")");
			break;
		}
	}

	private void handleUsername(String username) {
		if (!this.username.equals("<unnamed>")) {
			System.out.println("handleUsername: Player " + this.username + " already has a username.");
			return;
		}
		
		for (Player player : players) {
			if (username.equals(player.username)) {
				System.out.println("handleUsername: Username " + username + " is already taken.");
				return;
			}
		}
		
		this.username = username;
		System.out.println("handleUsername: Username set to " + this.username);

		ArrayList<String> usernames = new ArrayList<String>();
		for (Player player : players) {
			usernames.add(player.username);
		}
		JSONObject response = new JSONObject();
		response.put("type", "usernameList");
		response.put("usernames", usernames);
		broadcastMessage(response);
	}

	private void handleStartGame() {
		if (gameInProgress) {
			System.out.println("handleStartGame: Game is already in progress.");
			return;
		}

		if (players.size() < minimumPlayerCount) {
			System.out.println("handleStartGame: Not enough players to start the game.");
			return;
		}

		gameInProgress = true;
		word = randomWord();
		drawerIndex = (drawerIndex + 1) % players.size();
		
		JSONObject response = new JSONObject();
		response.put("type", "gameStarted");
		response.put("word", word);
		response.put("drawer", players.get(drawerIndex).username);
		broadcastMessage(response);
	}
	
	private void handleLine(JSONObject message) {
		if (!gameInProgress) {
			System.out.println("handleLine: Game is not in progress.");
			return;
		}
		
		if (this != players.get(drawerIndex)) {
			System.out.println("handleLine: This player is not allowed to draw.");
			return;
		}
		
		broadcastMessageToOthers(message);
	}
	
	private void handleEndLine(JSONObject message) {
		if (!gameInProgress) {
			System.out.println("handleEndLine: Game is not in progress.");
			return;
		}
		
		if (this != players.get(drawerIndex)) {
			System.out.println("handleEndLine: This player is not allowed to draw.");
			return;
		}
		
		broadcastMessageToOthers(message);
	}
	
	private void handleGuess(String wordGuess) {
		if (!gameInProgress) {
			System.out.println("handleGuess: Game is not in progress.");
			return;
		}
		
		if (this == players.get(drawerIndex)) {
			System.out.println("handleGuess: Drawer cannot guess words.");
			return;
		}
		
		if (!wordGuess.equals(word)) {
			JSONObject response = new JSONObject();
			response.put("type", "incorrectGuess");
			response.put("username", this.username);
			response.put("word", wordGuess);
			broadcastMessage(response);
			return;
		}
		
		JSONObject response = new JSONObject();
		response.put("type", "correctGuess");
		response.put("username", this.username);
		response.put("word", wordGuess);
		broadcastMessage(response);
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gameInProgress = false;
		handleStartGame();
	}
	
	private static String randomWord() {
		return words[(int)Math.round(Math.random() * words.length)];
	}

	private static void broadcastMessage(JSONObject message) {
		for (Player player : players) {
			player.output.println(message.toString());
		}
	}
	
	private void broadcastMessageToOthers(JSONObject message) {
		for (Player player : players) {
			if (player != this) {
				player.output.println(message.toString());
			}
		}
	}
}
