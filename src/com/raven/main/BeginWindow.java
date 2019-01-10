package com.raven.main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.raven.client.ComputerGame;

import util.GameRoomUtil;

public class BeginWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	static public BufferedWriter out;
	static public BufferedReader in;

	public static boolean bofang;
	//自己的名字
	static String username = null;
	static public Socket socket = null;
	MyPlane myplane;
	public BeginWindow(){
		setSize(530,700);
		setTitle("五子棋网络版  By Raven");
		setResizable(false);
		setLayout(null);
		GameRoomUtil.CenterWindow(this);
		setVisible(true);
		
		
	
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				
			}
		});
		//this.repaint();
		myplane = new MyPlane(this);
		myplane.setSize(530,700);
		myplane.setLocation(0,0);
		add(myplane);
	}
	


	
	
	

}
class MyPlane extends JPanel implements MouseListener{
	BeginWindow beginWindow;
	int rectwidth = 180;
	int rectheight = 60;
	Font beginFont = new Font("黑体",Font.BOLD,60);
	Font gameFont = new Font("黑体",Font.BOLD,30);
	static ImageIcon bgImg = new ImageIcon("source/bkImg.jpg");
	static Point p =new Point();
	
	//判断鼠标是否按下
	static Boolean mousedown = false;
	int i= 0,y =100,j=20;
	int modelint = 0;
	Timer timer;
	static String serverIp = "127.0.0.1";
	
	public MyPlane() {
	
	}
	
	public MyPlane(BeginWindow beginWindow) {
		
		this.beginWindow = beginWindow;
		//针对鼠标移动的接口  
				this.addMouseMotionListener(new MouseMotionAdapter() {
					
					public void mouseMoved(MouseEvent e) {
						p = e.getPoint();
						//System.out.println("sss");
						if(modelint!=2)
						repaint();
					}
					//按住鼠标移动才生效
					public void mouseDragged(MouseEvent e) {
						//System.out.println("鼠标按下不松拖动点的轨迹");
						System.out.println(e.getPoint());
					}
				});
				addMouseListener(this);
	}
	@Override
	public void paint(Graphics g) {
		
		BufferedImage bi = new BufferedImage(1024, 1024,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT); 
		if(modelint==0) {
			g2.drawImage(bgImg.getImage(), 0, 0,  530, 700,bgImg.getImageObserver());
			g2.setColor(Color.pink);
			g2.setFont(beginFont);
			g2.drawString("五子棋网络版", 40, 110);
			g2.setFont(gameFont);
			
			g2.drawString("网络对战", 150, 200);
			g2.drawString("人机对战", 150, 300);
			g2.drawString("游戏说明", 150, 400);
			g2.drawString("关于作者", 150, 500);
			
			GameRoomUtil.writeString(p,mousedown,"网络对战",g2,150, 200, rectwidth, rectheight);
			GameRoomUtil.writeString(p,mousedown,"人机对战",g2,150, 300, rectwidth, rectheight);
			GameRoomUtil.writeString(p,mousedown,"游戏说明",g2, 150, 400, rectwidth, rectheight);
			GameRoomUtil.writeString(p,mousedown,"关于作者", g2,150, 500, rectwidth, rectheight);
		}else if (modelint==1) {
			g2.drawImage(bgImg.getImage(), 0, 0,  530, 700, bgImg.getImageObserver());
			g2.setColor(Color.green);
			g2.setFont(gameFont);
			g2.drawString("这是一款网络对战的五子棋", 40, 100);
			g2.drawString("游戏，你可以和局域网中的", 40, 150);
			g2.drawString("好友一起玩游戏啦", 40, 200);
			g2.drawString("返回上个界面", 150, 500);
			GameRoomUtil.writeString(p, mousedown, "返回上个界面", g2, 150, 500, 250, 50);
		}else if (modelint==2) {
			super.paint(g);
			String msg = "你越觉得自己爱什么";
			String msg2= "那么你就越对什么爱的着迷~";
			String msg3 = "BY  Raven";
			if(timer!=null) {
				timer.cancel();
			}
			
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					i++;
					if(i>msg2.length()) {
						i=0;
					}
					if(i>10) {
						y+=50;
					}
					if(y>=400) {
						y=100;
					}
					
					repaint();
					
				}
			}, 250);
			g2.drawImage(bgImg.getImage(), 0, 0,  530, 700, bgImg.getImageObserver());
			g2.setColor(Color.RED);
			g2.setFont(gameFont);	
			if(i>msg.length()||i>msg3.length()) {
				
				g2.drawString(msg.substring(0,msg.length()), j, y);
				g2.drawString(msg3.substring(0,msg3.length()), j, y+100);
			}else {
				g2.drawString(msg.substring(0,i), j, y);
				g2.drawString(msg3.substring(0,i), j, y+100);
			}
			
			g2.drawString(msg2.substring(0,i), j, y+50);
			
			g2.drawString("返回上个界面", 150, 500);
			GameRoomUtil.writeString(p, mousedown, "返回上个界面", g2, 150, 500, 250, 50);
		}
		g2.dispose();
		g.drawImage(bi, 0, 0, this);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(p.getX()>=120&&p.getX()<=300&&p.getY()>=160&&p.getY()<=220&&modelint==0) {
			
			GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
			playGame();
			
		}else if(p.getX()>=120&&p.getX()<=300&&p.getY()>=260&&p.getY()<=320&&modelint==0) {
			GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
			//System.out.println("人机窗口！！");
			//隐藏该窗口 创建人机窗口
			if(beginWindow.username==null) {
				beginWindow.username = JOptionPane.showInputDialog("给您起一个个性的名称吧~");
				if(beginWindow.username==null) {
					return;
				}
			}
				
			
			if(beginWindow.username.equals("")) {
				beginWindow.username =null;
				JOptionPane.showMessageDialog(beginWindow, "名字不能为空哦。");
				return;
			}
			ComputerGame computerGame= new ComputerGame(beginWindow, beginWindow.username.trim());
			beginWindow.setVisible(false);
			
			repaint();
			
			
		}else if(p.getX()>=120&&p.getX()<=300&&p.getY()>=360&&p.getY()<=420&&modelint==0) {
			GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
			modelint=1;
			repaint();
		}else if(p.getX()>=120&&p.getX()<=300&&p.getY()>=460&&p.getY()<=520&&modelint==0) {
			GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
			modelint=2;
			repaint();
			//  y坐标注意要减字体大小的像素
			//点击 返回上一界面
		}else if (p.getX()>=150-30&&p.getX()<=400-30&&p.getY()>=500-40&&p.getY()<=550-40&&(modelint==1||modelint==2)) {
			modelint=0;
			
			GameRoomUtil.playChessMovemusic("source/mousedown.mp3");
			repaint();
		}
		//System.out.println("点击一次");
	}


	@Override
	public void mousePressed(MouseEvent e) {
		   //System.out.println("鼠标按下");  
		mousedown = true;
	
		this.repaint();
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
		mousedown = false;
		//System.out.println("鼠标松开"); 
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
		//System.out.println("鼠标进入窗体"); 
		
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("鼠标离开窗体");
		
		
	}
	
	private void playGame() {
		try {
			
			int i = JOptionPane.showConfirmDialog(this, "点击确定连接Raven的服务器，点击取消则连接本地服务器","连接方式",2);
			
			if(i==0) {
				URL ip=new URL("http://raven.iask.in");
				System.out.println(ip.getHost());
				BeginWindow.socket = new Socket(ip.getHost(), 12790);
			}else {
				serverIp  = JOptionPane.showInputDialog(this, "请输入服务器IP地址(默认为127.0.0.1)");
				if(serverIp==null)return;
				if(serverIp.equals("")) {
					serverIp ="127.0.0.1";
				}
				BeginWindow.socket = new Socket(serverIp,6666);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "服务器连接失败！请检查地址是否正确");
			return;
		}
		if(beginWindow.username==null) {
			String username = JOptionPane.showInputDialog("给您起一个个性的名称吧~");
			if(username==null||username.equals("")) {
				return;
			}
			
			if(username.length()>=12) {
				JOptionPane.showMessageDialog(this, "昵称不要太长哦");
				return;
			}
		
			BeginWindow.username =username.trim();
		}
		
		
			
			try {
				BeginWindow.out = new BufferedWriter(new OutputStreamWriter(BeginWindow.socket.getOutputStream(),"UTF-8"));
				BeginWindow.in = new BufferedReader(new InputStreamReader(BeginWindow.socket.getInputStream(),"UTF-8"));
				BeginWindow.out.write("MSGTYPE:username#"+beginWindow.username+"\r\n");
				BeginWindow.out.flush();
				//接受名字是否重复的消息
				String msg = BeginWindow.in.readLine();
				
				if(msg!=null) {
					if(msg.split("#")[1].equals("1")){
						JOptionPane.showMessageDialog(this, "在线玩家存在当前名字！，请重新取个名字吧~");
						return ;
					}
					beginWindow.setVisible(false);
					new Room(beginWindow);
				}else {
					JOptionPane.showMessageDialog(this, "Raven的服务器连接失败了，请开启本地Server吧~");
				}
				System.out.println("欢迎"+beginWindow.username+"加入游戏厅");
			} catch (HeadlessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				//e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				//e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				//e.printStackTrace();
			}
			
		
			
			
		
		
	}

}


