package com.raven.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.xml.stream.events.Namespace;

import util.GameRoomUtil;

public class Room extends JFrame {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ImageIcon man = new ImageIcon("source/man.jpg");
	public static ImageIcon women = new ImageIcon("source/women.jpg");
	public static ImageIcon chessWhite = new ImageIcon("source/chessWhite.png");
	public static ImageIcon chessBlack = new ImageIcon("source/chessBlack.png");
	
	static String MSG;
	public JPanel roomPlane;
	public BeginWindow priwid;

	
	public Room(BeginWindow priwid){
		setLayout(null);
		//大小不变
		setResizable(false);
		
		man.setImage(man.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		women.setImage(women.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		chessWhite.setImage(chessWhite.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		chessBlack.setImage(chessBlack.getImage().getScaledInstance(40, 40,
				Image.SCALE_DEFAULT));
		
		
		this.priwid = priwid;
		
		setSize(800, 800);
		GameRoomUtil.CenterWindow(this);
		
		MSG = GameRoomUtil.GetGames(this);
		
		roomPlane = new RoomPlane(this);
		roomPlane.setSize(800,800);
		roomPlane.setLocation(0, 0);
		
		add(roomPlane);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			
				try {
					if(BeginWindow.out!=null) {
						BeginWindow.out.close();
						BeginWindow.socket.close();
					}
					
					dispose();
					priwid.setVisible(true);
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		
		setVisible(true);
			
	}
	
	
	
	
};
class MouseThing extends MouseAdapter{
	RoomPlane roomPlane;
	
	
	public MouseThing(){
		
	}
	public MouseThing(RoomPlane roomPlane){
		this.roomPlane = roomPlane;
	}
	public void mouseClicked(MouseEvent e) {
		//System.out.println("点击一次");
		if(!roomPlane.hide) {
			//x -30  Y-40是矩形的左上角的点
			if(roomPlane.p.getX()>=100-30&&roomPlane.p.getX()<=100-30+roomPlane.rectwidth&&roomPlane.p.getY()>=roomPlane.lasty-40&&roomPlane.p.getY()<=roomPlane.lasty-40+roomPlane.rectheight) {
				
				try {
					BeginWindow.out.write("MSGTYPE:CreateGameRoom#"+BeginWindow.username+"\r\n");
					BeginWindow.out.flush();
					
					roomPlane.room.setVisible(false);
					new ChessBoard(roomPlane.room,"CreateRoom",BeginWindow.username);
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			
			
			}else if(roomPlane.p.getX()>=500-30&&roomPlane.p.getX()<=500-30+roomPlane.rectwidth&&roomPlane.p.getY()>=roomPlane.lasty-40&&roomPlane.p.getY()<=roomPlane.lasty-40+roomPlane.rectheight) {
				try {
					BeginWindow.out.write("MSGTYPE:GetOnlineGame\r\n");
					BeginWindow.out.flush();
					String qtext =  BeginWindow.in.readLine();
					Room.MSG =qtext;
					System.out.println("获取棋盘列表");
					System.out.println(qtext);
					
					System.out.println("刷新成功！");
				} catch (IOException e1) {
					System.out.println("刷新失败！");
				} 
				
			//上一页	
			}else if (roomPlane.p.getX()>=100-30&&roomPlane.p.getX()<=100-30+roomPlane.rectwidth&&roomPlane.p.getY()>=700-40&&roomPlane.p.getY()<=700-40+roomPlane.rectheight) {
				if(roomPlane.currpage==roomPlane.indexpage) {
					JOptionPane.showMessageDialog(roomPlane, "当前已经是第一页啦");
				}else {
					roomPlane.currpage -= 1;
				}
				
			//下一页		
			}else if (roomPlane.p.getX()>=500-30&&roomPlane.p.getX()<=500-30+roomPlane.rectwidth&&roomPlane.p.getY()>=700-40&&roomPlane.p.getY()<=700-40+roomPlane.rectheight) {
				if(roomPlane.currpage==roomPlane.lastpage-1) {
					JOptionPane.showMessageDialog(roomPlane, "当前已经是最后一页啦");
				}else {
					roomPlane.currpage += 1;
				}
				
				//加入对战
			}else {
				
				for(int i = 0;i<roomPlane.Y/80;i++) {
					if (roomPlane.p.getX()>=400-30&&roomPlane.p.getX()<=400-30+roomPlane.rectwidth&&roomPlane.p.getY()>=80*(i+1)+40-40&&roomPlane.p.getY()<=80*(i+1)+40-40+roomPlane.rectheight) {
						System.out.println("要加入游戏了");
						try {
							if(roomPlane.hasplayer.get(i).equals("0")) {
																		//namemap存放的是当前也的所有姓名
								BeginWindow.out.write(("MSGTYPE:AddGamePlayerName#"+roomPlane.nameMap.get(""+i))+"\r\n");
								BeginWindow.out.flush();
								String RoomStaus = BeginWindow.in.readLine();
								if(RoomStaus.split("#")[1].equals("yes")) {
									Room.MSG = BeginWindow.in.readLine();
									roomPlane.repaint();
									//if(roomPlane.hasplayer.get(i).equals("0")){
										//可以不用设置 因为repaint会重新赋值
										//roomPlane.hasplayer.set(i-1, "1");
										JOptionPane.showMessageDialog(roomPlane, "房间状态已改变，加入失败！");
								//	}
									
								}else {
									roomPlane.room.setVisible(false);
									new ChessBoard(roomPlane.room,"ADDRoom",BeginWindow.username);
								}
							}
							
							
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
					}
				
				}
			
			}
			roomPlane.repaint();
			roomPlane.hide = true;
		}
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		roomPlane.mousedown = true;
		//this.repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		roomPlane.hide = false;
		roomPlane.mousedown = false;
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
	
		
	}
}
class RoomPlane extends JPanel{
	
	private static final long serialVersionUID = 1L;
	Room room;
	int rectwidth = 180;
	int rectheight = 60;
	int indexpage = 0;
	int lastpage = 0;
	int currpage = 0;
	public boolean hide =false;
	// 坑爹 的boolean不初始化就是空指针！！！
	Boolean mousedown = false;
	int lasty = 150;
	Map<String,String> nameMap = new HashMap<String,String>();
	Point p = new Point();

	int Y;
	List<String> hasplayer = new ArrayList<String>();
	public RoomPlane() {
		
	}
	public RoomPlane(Room room) {
		this.room = room;
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				p = e.getPoint();
				
				repaint();	
			}
			
		});
		
	}
	
	public void paint(Graphics g) {
		//super.paint(g);
		//setBackground(Color.white);
		
		
		BufferedImage bi = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = bi.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT); 
		g2.drawImage(MyPlane.bgImg.getImage(), 0, 0,  800, 800, MyPlane.bgImg.getImageObserver());
		g2.setColor(Color.green);
		int index = 0;
		
		if(Room.MSG==null||Room.MSG.equals("")) {
			g2.setFont(new Font("黑体", Font.BOLD, 30));
			g2.setColor(Color.PINK);
			g2.drawString("亲爱的："+BeginWindow.username+",当前暂无房间~", 150, 100);
		}else {
			hasplayer.clear();
			g2.setFont(new Font("楷体", Font.PLAIN, 22));
			
			g2.drawString("亲爱的："+BeginWindow.username+",以下是当前服务器房间列表~：", 150, 40);
			g2.setColor(Color.black);
			g2.drawString("当前是第："+(currpage+1)+"页,共有"+lastpage+"页。", 220, 750);
			List<String> m = Arrays.asList(room.MSG.split("#"));
			//四个房间为一页
			int last = m.size()%5;
					
			if(last==0) {
				lastpage = m.size()/5;
			}else {
				lastpage = m.size()/5+1;
			}
			
			for(int i = 0;i<5;i++) {
				if(i+(currpage*5)>m.size()-1)break;
				String[] plays = m.get(i+(currpage*5)).split(",");
				
				String play1 = plays[0];
				String play2 = plays[1];

				index++;
				Y = 80*index;
				g2.setFont(new Font("楷体", Font.BOLD, 25));
				g2.drawImage(Room.man.getImage(), 100, Y, Room.man.getImageObserver());
				g2.setColor(Color.red);
				g2.drawString(play1, 100, Y+60);
				g2.drawImage(Room.chessWhite.getImage(), 250, Y+20, Room.chessWhite.getImageObserver());
				if(!play2.equals("null")) {
					g2.drawImage(Room.women.getImage(), 400, Y, Room.women.getImageObserver());
					g2.drawString(play2, 400, Y+60);
					hasplayer.add("1");
				}else {
					g2.drawString("加入该对局", 400, Y+40);
					GameRoomUtil.writeString(p, mousedown, "加入该对局", g2, 400, Y+40, 200, 60);
					hasplayer.add("0");
				}
				g2.setColor(Color.BLUE);
				g2.drawLine(0, Y+70, this.getWidth(), Y+70);
				nameMap.put(""+(index-1), play1);
			}
			lasty = 100*(index+1);
			g2.setColor(Color.red);
			g2.drawString("上一页", 100, 700);
			g2.drawString("下一页", 500, 700);
			GameRoomUtil.writeString(p, mousedown, "上一页", g2, 100, 700, rectwidth, rectheight);
			GameRoomUtil.writeString(p, mousedown, "下一页", g2, 500, 700, rectwidth, rectheight);
		}
		
		g2.setColor(Color.PINK);
		g2.drawString("创建房间", 100, lasty);
		g2.drawString("点击刷新", 500, lasty);
		GameRoomUtil.writeString(p, mousedown, "创建房间", g2, 100, lasty, rectwidth, rectheight);
		GameRoomUtil.writeString(p, mousedown, "点击刷新", g2, 500, lasty, 200, 60);
		g.drawImage(bi, 0,0,this);
		
		addMouseListener(new MouseThing(this));
		
		
	}
}