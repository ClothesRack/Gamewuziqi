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

import com.raven.client.GameClient;

import util.GameRoomUtil;

public class Room extends JFrame {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ImageIcon man = new ImageIcon("source/man.jpg");
	static ImageIcon women = new ImageIcon("source/women.jpg");
	public static ImageIcon chessWhite = new ImageIcon("source/chessWhite.png");
	public static ImageIcon chessBlack = new ImageIcon("source/chessBlack.png");
	
	
	public JPanel roomPlane;
	public BeginWindow priwid;

	
	public Room(BeginWindow priwid){
		setLayout(null);
		//大小不变
		setResizable(false);
		setTitle("在线游戏列表");
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
		
		GameRoomUtil.SendToServerMsg(this,"MSGTYPE:GetOnlineGame\r\n");
		GameRoomUtil.ResultMsg();
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
	String lastMsg = null;
	
	public MouseThing(){
		
	}
	public MouseThing(RoomPlane roomPlane){
		this.roomPlane = roomPlane;
	}
	public void mouseClicked(MouseEvent e) {
		//为什么点击一次会出现好多个点击一次？？
				//或许跟新增了多个监听器有关系！！！
		//System.out.println("点击一次");
		//如果击中为假  防止点击一次发送多次 
				//已经验证是 所以注释掉了 删除多余的监听器
	//	if(!roomPlane.hide) {
			//x -30  Y-40是矩形的左上角的点
			if(roomPlane.p.getX()>=100-30&&roomPlane.p.getX()<=100-30+roomPlane.rectwidth&&roomPlane.p.getY()>=roomPlane.lasty-40&&roomPlane.p.getY()<=roomPlane.lasty-40+roomPlane.rectheight) {
				GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
				//给服务端发送创建房间消息
				GameRoomUtil.SendToServerMsg(roomPlane.room,"MSGTYPE:CreateGameRoom#"+BeginWindow.username+"\r\n");
				
				//然后设置当前窗口不可见
				roomPlane.room.setVisible(false);
				//棋盘窗口
				new ChessBoard(roomPlane.room,"CreateRoom",BeginWindow.username);
		
			
			}else if(roomPlane.p.getX()>=500-30&&roomPlane.p.getX()<=500-30+roomPlane.rectwidth&&roomPlane.p.getY()>=roomPlane.lasty-40&&roomPlane.p.getY()<=roomPlane.lasty-40+roomPlane.rectheight) {
				
				GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
				//获取在线游戏房间列表	
				GameRoomUtil.SendToServerMsg(roomPlane.room,"MSGTYPE:GetOnlineGame\r\n");
				GameRoomUtil.ResultMsg();
				JOptionPane.showMessageDialog(roomPlane, "刷新成功！");
				//System.out.println("获取棋盘列表:\r\n"+GameClient.MSG);
				//每次刷新必须清空这个hasplayer
				roomPlane.hasplayer.clear();
				//删除重复的监听器 只留下一个最终的
				//只有当房间更新的时候再添加新的监听器
				if(lastMsg!=null&&!lastMsg.equals(GameClient.MSG)) {
					if(roomPlane.mous!=null) {
						roomPlane.removeMouseListener(roomPlane.mous);
					}
					roomPlane.mous = new MouseThing(roomPlane);
					roomPlane.addMouseListener(roomPlane.mous);
				}
				
				lastMsg = GameClient.MSG;
			//上一页	
			}else if (roomPlane.p.getX()>=100-30&&roomPlane.p.getX()<=100-30+roomPlane.rectwidth&&roomPlane.p.getY()>=700-40&&roomPlane.p.getY()<=700-40+roomPlane.rectheight) {
				GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
				if(roomPlane.currpage==roomPlane.indexpage) {
					JOptionPane.showMessageDialog(roomPlane, "当前已经是第一页啦");
				}else {
					roomPlane.currpage -= 1;
				}
				
			//下一页		
			}else if (roomPlane.p.getX()>=500-30&&roomPlane.p.getX()<=500-30+roomPlane.rectwidth&&roomPlane.p.getY()>=700-40&&roomPlane.p.getY()<=700-40+roomPlane.rectheight) {
				GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
				if(roomPlane.currpage==roomPlane.lastpage-1) {
					JOptionPane.showMessageDialog(roomPlane, "当前已经是最后一页啦");
				}else {
					roomPlane.currpage += 1;
				}
				//加入对战
			}else {
				//Y/80 说明一共有这么多房间
				for(int i = 0;i<roomPlane.Y/80;i++) {
					if (roomPlane.p.getX()>=400-30&&roomPlane.p.getX()<=400-30+roomPlane.rectwidth&&roomPlane.p.getY()>=80*(i+1)+40-40&&roomPlane.p.getY()<=80*(i+1)+40-40+roomPlane.rectheight) {
						//System.out.println("要加入游戏了");
					
						if(i<roomPlane.hasplayer.size())
						if(roomPlane.hasplayer.get(i).equals("0")) {
							GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
						
																	//namemap存放的是当前也的所有姓名
							GameRoomUtil.SendToServerMsg(roomPlane.room,"MSGTYPE:AddGamePlayerName#"+roomPlane.nameMap.get(""+i)+"\r\n");
							GameRoomUtil.ResultMsg();
							if(GameClient.MSG.equals("yes")) {
								//注意 如果该房间已经加入人了，那么就要在读一次，因为服务器会自动给客户端发送最新棋盘
								GameRoomUtil.ResultMsg();
									/*
									 * 可以不用设置 因为repaint会重新赋值
									 * 我错了 这里需要设置一下 为什么呢 ？虽然repaint会重新赋值 但是前提是针对有房间的情况下
									 * 	就是说房间有人已经加入了房间  再次加入
									 *	加入就提示一次加入失败。然后再次点击就不会进来这个逻辑
									 *	然而 设置下重新赋值为1又是因为什么原因呢 是因为 如果房间为空了 那么paintjiu不会给hasplayer赋值
									 *	会导致没有房间也会提示房间状态已改变
									 */		
									roomPlane.hasplayer.set(i, "1");
									JOptionPane.showMessageDialog(roomPlane, "房间状态已改变，加入失败,请刷新后重试！");
						
								
							}else {
								
								roomPlane.room.setVisible(false);
								new ChessBoard(roomPlane.room,"ADDRoom",BeginWindow.username);
								
							}
						}
							
							
					
					}
				
				}
			
			}
			roomPlane.repaint();
			//击中为真
			//roomPlane.hide = true;
			
	//	}
		
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		roomPlane.mousedown = true;
		//this.repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// 松开则 击中为假
		//roomPlane.hide = false;
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
	MouseThing mous;
	public boolean hide =false;
	// 坑爹 的boolean不初始化就是空指针！！！
	Boolean mousedown = false;
	int lasty = 150;
	Map<String,String> nameMap = new HashMap<String,String>();
	/**
	 * p  是鼠标记录移动的最后一个位置的点
	 */
	Point p = new Point();

	int Y;
	List<String> hasplayer = new ArrayList<String>();
	public RoomPlane() {
		
	}
	public RoomPlane(Room room) {
		this.room = room;
		mous = new MouseThing(this);
		addMouseListener(mous);
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
		
		if(GameClient.MSG==null||GameClient.MSG.equals("")) {
			g2.setFont(new Font("黑体", Font.BOLD, 30));
			g2.setColor(Color.PINK);
			g2.drawString("亲爱的："+BeginWindow.username+",当前暂无房间~", 150, 100);
		}else {
			
			g2.setFont(new Font("楷体", Font.PLAIN, 22));
			
			g2.drawString("亲爱的："+BeginWindow.username+",以下是当前服务器房间列表~：", 150, 40);
			g2.setColor(Color.black);
			g2.drawString("当前是第："+(currpage+1)+"页,共有"+lastpage+"页。", 220, 750);
			List<String> m = Arrays.asList(GameClient.MSG.split("&"));
			//5个房间为一页
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
				//因为房间有可能只有房主 所以这里默认为null
				String play2 ="null";
				if(plays.length==2) {
					play2 = plays[1];
				}

				index++;
				Y = 80*index;
				g2.setFont(new Font("楷体", Font.BOLD, 25));
				g2.drawImage(Room.man.getImage(), 100, Y, Room.man.getImageObserver());
				g2.setColor(Color.red);
				g2.drawString(play1, 100, Y+60);
				g2.drawImage(Room.chessWhite.getImage(), 250, Y+20, 40,40,this);
				if(!play2.equals("null")) {
					g2.drawImage(Room.women.getImage(), 400, Y, 40,40,this);
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
		/**
		 * 注意了 这个鼠标监听事件一定要卸载paint里面  为什么 不直接写在对象的构造器里？因为这样子
		 * 每次刷新一次界面都会重新创建一个监听器  为什么要这么做？ 因为房间信息是动态变化的  必须这样  不然 刷新新增的房间
		 *  点击效果怎么实现 以及 房间销毁 取消点击事件   
		 *  	更好的不是放在paint方法里 而是放入 刷新房间点击事件里 如果房间信息改变 那么重新构造新的监听器 这样写 太不好了 
		 */
		
		
		
	}
}