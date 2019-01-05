package com.raven.main;

import java.net.Socket;

import com.raven.server.Server;

public class FacetoFace extends Thread{
	Socket player1;
	Socket player2;
	public FacetoFace(Socket player1, String string) {
		this.player1 = player1;
		
	}
	@Override
	public void run() {
		while (true) {
			
		}
	}
}
