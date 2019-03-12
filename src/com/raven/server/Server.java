package com.raven.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.raven.main.ConnectTomsg;
import com.raven.main.GameRoom;

public class Server implements Runnable{
	static ServerSocket serverSocket;
	public static Map<Socket,String> players = new LinkedHashMap<Socket,String>();
	
	static ExecutorService pool= Executors.newFixedThreadPool(50);
	public static GameRoom gameRoom = new GameRoom();
	public static void main(String[] args) {
		try {
			
		
			serverSocket = new ServerSocket(6666);
			System.out.println("服务已启动");
			//new Thread(new Server()).start();
			while(true) {
				Socket socket = serverSocket.accept();
				pool.execute(new ConnectTomsg(socket));
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
			/*games.forEach((k,v)->{
				new readClientmsg(k);
			});*/

			
		
		
	}
	
}
