package com.raven.main;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameRoom() {
	
	}
	Map<String,String> chessBoards = new TreeMap<String,String>();
	
	public  Map<String, String> getChessBoards() {
		return chessBoards;
	}
	public void setChessBoards(Map<String, String> chessBoards) {
		this.chessBoards = chessBoards;
	}

	
	
}
