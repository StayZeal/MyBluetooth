package example.server;

public class Server {

	public static void main(String[] args) {

		ClientServer clientServer = new ClientServer(80);
		clientServer.start();

	}
}
