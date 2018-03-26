package example.server;

/**
 * ³ÌĞòÈë¿Ú
 *
 */
public class Server {

	public static void main(String[] args) {

		ClientServer clientServer = new ClientServer(80);
		clientServer.start();

	}
}
