import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurJeu extends Thread{
	int nbClient;
	private int nombreSecret;
	private boolean fin;
	private String gagnant;
@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(234);
			nombreSecret=(int)(Math.random()*1000);
			while(true){
				
				Socket s=ss.accept();
				++nbClient;
				new Conversation(s,nbClient).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		private Socket socket;
		private int numeroClient;
		
		public Conversation(Socket socket, int num) {
			super();
			this.socket = socket;
			this.numeroClient = num;
		}
		@Override
		public void run() {
			try {
				InputStream is=socket.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				
				OutputStream os=socket.getOutputStream();
				PrintWriter pw=new PrintWriter(os,true);
				
				String IP=socket.getRemoteSocketAddress().toString();
				
				System.out.println("Gamer #"+numeroClient+" is connecting"+ "IP="+IP);
				pw.println("Welcome, you're the gamer #"+numeroClient);
				pw.println("Guess the secret number between 0 et 1000");
				
				while(true) {
					String req;
					while((req=br.readLine())!=null) {
						System.out.println(IP+" a envoye "+req);	
						int nb=Integer.parseInt(req);
						if(fin==false) {
							if(nb<nombreSecret) {
								pw.println("	Your number is smaller");
							}
							else if(nb>nombreSecret){
								pw.println("	Your number is bigger");
							}
							else {
								gagnant=IP;
								fin=true;
								pw.println("	Congratulations, You are the winner...");
								System.out.println("***************************");
								System.out.println("Winner is Mr "+IP);
								System.out.println("***************************");
							}
						}
						else {
							pw.println("	The game ended, the winner is :"+gagnant);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	public static void main(String[] args) {
		System.out.println("waiting for gamers...");
		new ServeurJeu().start();
	}
}