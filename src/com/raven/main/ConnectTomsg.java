package com.raven.main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import com.raven.server.Server;

public class ConnectTomsg implements Runnable{
	Socket socket;
	String myname;
	String othername;
	BufferedWriter otherBW;
	Socket otherSocket;
	BufferedReader reader = null;
	BufferedWriter writer = null;
	public ConnectTomsg(){
	}
	public  ConnectTomsg(Socket socket) {
		this.socket = socket;
	}		
	@Override
	public void run() {
		
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(socket
					.getInputStream(),"UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		while(true) {
			try {
				
				String msg = reader.readLine();
				System.out.println(msg);
				//如果收到消息是null，说明对方关闭了io流 那么这个线程退出
				//如果消息类型头不是规定的 那么删掉这个socket连接 
				if(msg==null||!msg.split(":")[0].equals("MSGTYPE")) {
					Server.players.remove(socket);
					IOEX();
					break;
				}
				if(!Server.players.containsKey(socket)) {
						String msgsp[] = msg.split("#");
						//新加入用户放入在线列表
						if(msgsp[0].equals("MSGTYPE:username")) {
							if(Server.players.containsValue(msgsp[1])){//已经有该名字了 提醒玩家重新取名字
								writer.write("MSGTYPE:namerepeat#1\r\n");
								writer.flush();
								return;
							}else {
								writer.write("MSGTYPE:namerepeat#0\r\n");
								writer.flush();
								Server.players.put(socket, msgsp[1]);
								myname = msgsp[1];
								System.out.println(msgsp[1]+"登录了");
							}
							
						}
						
					
				}else {
						
						String[] msgsplit = msg.split("#");
						//创建一个房间
						if(msgsplit[0].equals("MSGTYPE:CreateGameRoom")) {
							Server.gameRoom.getChessBoards().put(msgsplit[1], null);

						}
						// 发送棋盘list
						else if(msgsplit[0].equals("MSGTYPE:GetOnlineGame")) {
							SendChessBoardlist();
						//加入者会给服务端发送他要加入房间创建者的姓名
						}else if(msgsplit[0].equals("MSGTYPE:AddGamePlayerName")) {
							Server.players.forEach((k,v)->{
								if(v.equals(msgsplit[1])) {
									//拿到创建房间玩家姓名
									othername = v;
									if(Server.gameRoom.getChessBoards().get(v)!=null||!Server.gameRoom.getChessBoards().containsKey(v)) {
										try {//加入的时候由于刷新导致房间状态改变 加入失败
											writer.write("MSGTYPE:RoomFullOrRoomDistroy#yes\r\n");
											writer.flush();
											SendChessBoardlist();
											return;
										} catch (IOException e) {
											
											e.printStackTrace();
										}
										
									}else {
										try {
											writer.write("MSGTYPE:RoomFullOrRoomDistroy#no\r\n");
											writer.flush();
										} catch (IOException e) {
											
											e.printStackTrace();
										}
										
									}
									//更新棋盘玩家与玩家
									Server.gameRoom.getChessBoards().put(othername, myname);
									
									//拿到创建房间玩家的BufferedWriter					
									try {
										otherSocket = k;
										otherBW = new BufferedWriter(new OutputStreamWriter(k.getOutputStream(),"UTF-8"));
										// 此条信息重点 发送了 加入者的名字和创建游戏者的名字。
										otherBW.write("MSGTYPE:ADDYourRoom#"+othername+","+myname+",来跟你一起玩游戏啦！\r\n");
										
										otherBW.flush();
										otherBW.write("MSGTYPE:youareblackoriswhite#\r\n");
										otherBW.flush();
									} catch (IOException e1) {
										
										e1.printStackTrace();
									}								
								}
							});
							
							//如果正在游戏中
						}else if (msgsplit[0].equals("MSGTYPE:ChessGameing")) {
							String gamemsg  = msg.split("#")[1];
							//悔棋
							if(gamemsg.split(",").length==1) {//消息分割为1  那么就通知另一方 和棋 认输  悔棋 赢了游戏
								if (gamemsg.equals("huiqi")) {
									
									otherBW.write("MSGTYPE:ChessGameing#huiqi\r\n");
									otherBW.flush();
								}else if (gamemsg.equals("heqi")) {
									
									otherBW.write("MSGTYPE:ChessGameing#heqi\r\n");
									otherBW.flush();
								}else if (gamemsg.equals("renshu")){	
									
									otherBW.write("MSGTYPE:ChessGameing#YouWIN\r\n");
									otherBW.flush();
								}else if (gamemsg.equals("GameWIN")) {
									otherBW.write("MSGTYPE:ChessGameing#HeWON\r\n");
									otherBW.flush();
								}//如果是消息分割是 2和3的话，那就说明那个是发送 和棋，悔棋 对方响应的状态  （认输不需要对方同意哦）和下的棋子的坐标
							}else if (gamemsg.split(",").length==2||gamemsg.split(",").length==3) {
								otherBW.write("MSGTYPE:ChessGameing#"+gamemsg+"\r\n");
								otherBW.flush();
							}
							//创建者返回自己的名字回来，更新加入者线程的othereBW 对象 让加入者方便与其通信。
						}else if(msgsplit[0].equals("MSGTYPE:NoticeGameState")) {
							Server.players.forEach((k,v)->{
								if(v.equals(msgsplit[1].split(",")[0])) {
									try {
										otherBW = new BufferedWriter(new OutputStreamWriter(k.getOutputStream(),"UTF-8"));
										othername = v;
										otherSocket = k;
										otherBW.write("MSGTYPE:ADDSucc#"+myname+",我很开心你能加进来\r\n");
										otherBW.flush();
									} catch (IOException e) {
										
										e.printStackTrace();
									}
								}
							});
							//拿到自己的颜色发送给加入者
						}else if(msgsplit[0].equals("MSGTYPE:MyColor")) {
							otherBW.write("SendHisColor#"+msgsplit[1]+"\r\n");
							otherBW.flush();
							//退出房间
						}else if(msgsplit[0].equals("MSGTYPE:LingoutGameNowPerson")) {
							writer.write("MSGTYPE:STOPREAD!\r\n");
							writer.flush();
							//如果当前局有人的话就通知他自己下线了！！，没有的话就不通知了，直接删除服务器对象自己创建的房间
							if(msgsplit[1].equals("1")) {
								//这个方法比DeleteLingoutPlayer方法多个发送自己下线了
								IOEX();
							}else {
								DeleteLingoutPlayer();
							}
							//游戏开始
						}else if (msgsplit[0].equals("MSGTYPE:GAMEBEGIN")) {
							otherBW.write("MSGTYPE:GAMEBEGIN\r\n");
							otherBW.flush();
							//游戏准备
						}else if (msgsplit[0].equals("MSGTYPE:GAMEREADY")) {
							otherBW.write(msg+"\r\n");
							otherBW.flush();
							//游戏聊天
						}else if (msgsplit[0].equals("MSGTYPE:GAMECHAT")) {
							if(otherBW!=null) {
								otherBW.write(msg+"\r\n");
								otherBW.flush();
							}
							//强行退出游戏
						}else if(msgsplit[0].equals("MSGTYPE:BreakGame")) {
							otherBW.write(msg+"\r\n");
							otherBW.flush();
						}
					
				}
			
				
			} catch (IOException e) {
				IOEX();
				try {
					otherBW.close();
					otherSocket.close();
					otherSocket = null;
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				e.printStackTrace();
				break;
			} 
		}
		System.err.println("服务端一条循环线程终止");
	}
	private void SendChessBoardlist() throws IOException {
		System.out.println("获取棋盘列表");
		String name ="";
		for(Map.Entry<String, String> entry:Server.gameRoom.getChessBoards().entrySet()) {
			
			String play1 = entry.getKey();
			String play2 = entry.getValue();
			name +=(play1+","+play2+"#");
		}
		System.out.println("name:"+name);
		writer.write(name+"\r\n");
		writer.flush();
	}
	public void DeleteLingoutPlayer(){
		for(Map.Entry<String, String> entry:Server.gameRoom.chessBoards.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			//说明自己是创建者
			if(k.equals(myname)) {
				
				Server.gameRoom.chessBoards.remove(k);
				if(v==null) {
					
					return;
				}
				Server.gameRoom.chessBoards.put(v, null);
			
				
			}
			//说明自己是加入者
			if(v!=null&&v.equals(myname)) {
				Server.gameRoom.chessBoards.put(k, null);
				
			}
		}
		
		System.out.println("新的棋盘列表");
		Server.gameRoom.chessBoards.forEach((k,v)->{
			System.out.println(k+","+v);
		});
	}
	private synchronized void IOEX() {
		if(socket.isConnected()) {
			System.out.println("当前用户下线了。"+myname);
			
			DeleteLingoutPlayer();
			
		
			//Server.players.remove(socket);
			try {
				if(otherSocket!=null&&otherSocket.isConnected()==true&&otherBW!=null) {
					otherBW.write("MSGTYPE:GamePlayerLingout\r\n");
					otherBW.flush();
					
				
					
				}
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		
			
		}
		
	}
	private void SendToOther(String myname, String string) {
		
		
	}



}
