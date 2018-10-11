import java.io.IOException;
import java.net.ServerSocket;

public class MultiChatServer {

	public static void main(String[] args) throws IOException{
		int portNumber = Integer.parseInt("1500");
		boolean listening = true;
		
		try(
				ServerSocket serverSocket = new ServerSocket(portNumber))
		{ 
			while (listening) {
				MultiChatServerThread thread = new MultiChatServerThread(serverSocket.accept());
//				System.out.println(thread.getName());
				thread.start();
				System.out.print(" \n\t << ======  A new user is connected ======= >> ");
			}
		}
		catch (IOException e) {
			  System.err.println("Could not listen on port " + portNumber);
	          System.exit(-1);
		}
		}
}


