package com.raven.main;



import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import util.GameRoomUtil;


public class ChessBoard extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int ChessBoardWidth = 1800;
	int ChessBoardHeight  =  1000;
	
	String RoomType;
	Room parFrame;
	GamePlane gamepanel;
	JSplitPane chatPlane;
	JTextArea jt  = new JTextArea();
	JScrollPane jscroll ;
	public ChessBoard() {
		
	}
	public ChessBoard(Room parFrame,String RoomType,String username) {
				setLayout(null);
				
				//设置标题
				this.setTitle("五子棋");
				//设置窗体大小
				this.setSize(ChessBoardWidth, ChessBoardHeight);
				//设置窗体出现位置
				GameRoomUtil.CenterWindow(this);
				this.setVisible(true);
				//窗口关闭事件
				addWindowListener(new WindowEvent(this));
							
				this.parFrame = parFrame;
				this.RoomType = RoomType;
				//播放背景音乐
				//GameRoomUtil.playBgmusic();
				
				
				setResizable(false);
				
				
				gamepanel = new GamePlane(this,username);
			
				gamepanel.setLocation(0, 0);
				gamepanel.setSize(ChessBoardWidth-500, ChessBoardHeight);
				
				
			
				
				
				
				chatPlane = new JSplitPane();
				chatPlane.setLayout(null);
				chatPlane.setSize(450, ChessBoardHeight);
				chatPlane.setLocation(1300, 0);
				
				jscroll = new JScrollPane(jt);
				jscroll.setViewportView(jt);
				jscroll.setLocation(0,0);
				jscroll.setSize(450,800);
				jt.setLineWrap(true);        //激活自动换行功能 
				jt.setWrapStyleWord(true); 
				jt.setEditable(false);
				jt.setLocation(0,0);
				jt.setSize(450,800);
				JTextField sendtext = new JTextField();
				sendtext.setLocation(10,850);
				sendtext.setSize(350,40);
				
				sendtext.setBackground(new Color((int)0xE6E6FA));
				jt.setFont(new Font("楷体", Font.PLAIN, 25));
				sendtext.setFont(new Font("楷体", Font.PLAIN, 20));
				JLabel bq = new JLabel("发送消息:");
				bq.setForeground(Color.pink);
				bq.setSize(200,30);
				bq.setLocation(10,810);
				chatPlane.add(bq);
				JButton send = new JButton("Send");
				send.setLocation(370,840);
				send.setSize(60,60);
			
				chatPlane.add(jscroll);
				//chatPlane.add(jt);
				chatPlane.add(send);
				chatPlane.add(sendtext);
			
				jt.append("系统："+gamepanel.dateFormat.format(new Date())+"\n"+"   欢迎加入游戏厅，希望来到这里能给你带来快乐，与室友一起组队开黑吧~~\n");	
				send.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							BeginWindow.out.write("MSGTYPE:GAMECHAT#"+sendtext.getText()+"\r\n");
							BeginWindow.out.flush();
							if(sendtext.getText().contains("快点")) {
								GameRoomUtil.palyothermusic("source/flowerdie.mp3");
							}
							if(sendtext.getText().contains("停止")) {
								GameRoomUtil.stopmusic();
							}
							if(sendtext.getText().contains("开始")) {
								GameRoomUtil.playBgmusic();
							}
							jt.append(gamepanel.gameplayer1+"："+gamepanel.dateFormat.format(new Date())+"\n"+"   "+sendtext.getText()+"\n");
							sendtext.setText("");
							//最下方
							jt.setCaretPosition(jt.getDocument().getLength());
							//获取焦点
							sendtext.grabFocus();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						
					}
				});
				sendtext.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						
						 if(e.getKeyChar() == KeyEvent.VK_ENTER )  
			                {  
								send.doClick();
			                    
			                }  
					}
				});
				
				add(gamepanel);
				add(chatPlane);
			

				
					
				
	}

		




	
}
class WindowEvent implements WindowListener{
	ChessBoard chessBoard;
	public WindowEvent(){
		
	}
	public WindowEvent(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
	}

	@Override
	public void windowClosed(java.awt.event.WindowEvent e) {
		
		
	}

	@Override
	public void windowOpened(java.awt.event.WindowEvent e) {
		//System.out.println("打开");
		
		
	}

	@Override
	public void windowClosing(java.awt.event.WindowEvent e) {
		System.out.println("退出");
		try {
			if(chessBoard.gamepanel.kaishi) {
				
				int i = JOptionPane.showConfirmDialog(chessBoard, "当前游戏已经开始，退出游戏你将输掉比赛，确认退出游戏？","确认退出？",2);
				if(i==0) {
					BeginWindow.out.write("MSGTYPE:BreakGame\r\n");
					BeginWindow.out.flush();
				}else {
					return;
				}
			}
			
			if(chessBoard.RoomType.equals("CreateRoom")||chessBoard.RoomType.equals("ADDRoom")) {
				String msg = "";
				if(chessBoard.gamepanel.gameplayer2.equals("")) {
					msg  = "MSGTYPE:LingoutGameNowPerson#0\r\n";
				}else {
					msg  = "MSGTYPE:LingoutGameNowPerson#1\r\n";
				}
				BeginWindow.out.write(msg);
				BeginWindow.out.flush();
				
				Room.man.setImage(Room.man.getImage().getScaledInstance(40, 40,
						Image.SCALE_DEFAULT));
				Room.women.setImage(Room.women.getImage().getScaledInstance(40, 40,
						Image.SCALE_DEFAULT));
				
				chessBoard.dispose();
				Room.MSG = GameRoomUtil.GetGames(chessBoard.parFrame);
				chessBoard.parFrame.setVisible(true);
			}
		} catch (IOException e1) {
		
		
		}
			
		
		
		
		
	}

	@Override
	public void windowIconified(java.awt.event.WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(java.awt.event.WindowEvent e) {
		
		
	}

	@Override
	public void windowActivated(java.awt.event.WindowEvent e) {

		
	}

	@Override
	public void windowDeactivated(java.awt.event.WindowEvent e) {
		
		
	}
}
class getServerMsg extends Thread{
	GamePlane gmPlane;
	
	public getServerMsg() {
		
	}
	public getServerMsg(GamePlane chessBord){
		this.gmPlane = chessBord;
	}
	@Override
	public void run() {
		while (true) {
			try {
					
				
				String qtext = BeginWindow.in.readLine();
				System.out.println(qtext);
			
				if(qtext==null) {
					return;
				}
				String[] msgsplit = qtext.split("#");
				// 关闭此窗口一定要break此循环 ，要不然这个sb线程会读取了父窗口的刷新消息卡顿了。。找个半个小时。
				if(msgsplit[0].equals("MSGTYPE:STOPREAD!")) {
					
					break;
					// 
					//通知创建房间的玩家xx加入游戏
				}else if(msgsplit[0].equals("MSGTYPE:ADDYourRoom")) {
					BeginWindow.out.write("MSGTYPE:NoticeGameState#"+msgsplit[1].split(",")[1]+"\r\n");
					BeginWindow.out.flush();
					
					gmPlane.gameplayer2 = msgsplit[1].split(",")[1];
					gmPlane.chessBoard.jt.append("系统："+gmPlane.dateFormat.format(new Date())+"\r\n"+"   "+gmPlane.gameplayer2+"加入了您的房间\n");
					//通知加入玩家 加入了谁的房间 获取 他的名字
				}else if (msgsplit[0].equals("MSGTYPE:ADDSucc")) {
					gmPlane.gameplayer2 = msgsplit[1].split(",")[0];
					gmPlane.chessBoard.jt.append("系统："+gmPlane.dateFormat.format(new Date())+"\r\n"+"   你加入了"+gmPlane.gameplayer2+"的房间\n");
				}else if(msgsplit[0].equals("MSGTYPE:ChessGameing")) {
					String cxy[] = msgsplit[1].split(",");
					if(cxy.length==3) {//长度未3的话，说明是下棋的信息
						gmPlane.rx = Integer.parseInt(cxy[1]);
						gmPlane.ry = Integer.parseInt(cxy[2]);
						int x = gmPlane.rx*45+430;
						int y = gmPlane.ry *45+110;
						gmPlane.chessPoint.add(cxy[0]+","+x+","+y);
						//设置我的状态为可用
						gmPlane.isme = true;
						if(cxy[0].equals("white")) {
							gmPlane.allChess[Integer.parseInt(cxy[1])][Integer.parseInt(cxy[2])] =1;
						}
						else if(cxy[0].equals("black")){
							
							gmPlane.allChess[Integer.parseInt(cxy[1])][Integer.parseInt(cxy[2])] =2;
						}//长度是1的话说明 和棋和认输 和悔棋 
					}else if (cxy.length==1) {//如果是认输或者和棋
						if(cxy[0].equals("HeWON")||cxy[0].equals("YouWIN")||cxy[0].equals("heqi")) {
							if(cxy[0].equals("HeWON")) {
								JOptionPane.showMessageDialog(gmPlane, "你输了！！！！");
								gmPlane.chessBoard.jt.append("系统："+gmPlane.dateFormat.format(new Date())+"\r\n   你输了！！！！\n");
								gmPlane.HisplayGamewinnum++;
							}else if(cxy[0].equals("YouWIN")){
								JOptionPane.showMessageDialog(gmPlane, "对方认输了，你很棒哦");
								gmPlane.MyplayGamewinnum++;
							}else if (cxy[0].equals("heqi")) {
								int i =JOptionPane.showConfirmDialog(gmPlane, "对方请求和棋，你同意吗？","对方请求和棋",2);
								if(i==0) {
									BeginWindow.out.write("MSGTYPE:ChessGameing#heqi,0\r\n");
									BeginWindow.out.flush();
									gmPlane.HisplayGamewinnum++;
									gmPlane.MyplayGamewinnum++;
								}else {
									BeginWindow.out.write("MSGTYPE:ChessGameing#heqi,1\r\n");
									BeginWindow.out.flush();
									return;
								}
								
							} 
							//游戏完成所做的事
							gmPlane.GameWinAfter(gmPlane);
						}else if (cxy[0].equals("huiqi")) {//如果是悔棋
							
							int i =JOptionPane.showConfirmDialog(gmPlane, "对方想要悔棋，你同意吗？","对方请求悔棋",2);
							if(i==0) {
								BeginWindow.out.write("MSGTYPE:ChessGameing#huiqi,0\r\n");
								BeginWindow.out.flush();
								//同意之后禁用自己的状态
								gmPlane.isme =false;
								gmPlane.allChess[gmPlane.rx][gmPlane.ry] =0;
								gmPlane.chessPoint.remove(gmPlane.chessPoint.size()-1);
							
							}else {
								BeginWindow.out.write("MSGTYPE:ChessGameing#huiqi,1\r\n");
								BeginWindow.out.flush();
								
							}
							
						}
					}else if (cxy.length==2) {
						if(cxy[0].equals("huiqi")) {
							if(cxy[1].equals("0")) {
								System.out.println("同意悔棋");
								gmPlane.allChess[gmPlane.rx][gmPlane.ry] =0;
								gmPlane.chessPoint.remove(gmPlane.chessPoint.size()-1);
								gmPlane.isme = true;
							}else {
								JOptionPane.showMessageDialog(gmPlane, "对方不同意悔棋");
							}
						}else if (cxy[0].equals("heqi")) {
							if(cxy[1].equals("0")) {
								System.out.println("同意和棋");
								gmPlane.HisplayGamewinnum++;
								gmPlane.MyplayGamewinnum++;
								//游戏完成所做的事
								gmPlane.GameWinAfter(gmPlane);	
	
							}else {
								JOptionPane.showMessageDialog(gmPlane, "对方不同意和棋");
							}
						}
							
						
					}

					
				}else if(msgsplit[0].equals("MSGTYPE:youareblackoriswhite")) {
					BeginWindow.out.write("MSGTYPE:MyColor#"+gmPlane.MyChessColor+"\r\n");
					BeginWindow.out.flush();
				}else if(msgsplit[0].equals("SendHisColor")) {
					System.out.println("房主的棋子颜色："+msgsplit[1]);
						//设置我的颜色与防止的相反
						if(msgsplit[1].equals("white")) {
							gmPlane.MyChessColor = "black";
							gmPlane.MyChessColorINT = 2;
							
							
							System.out.println("设置我的颜色为black");
						}else {
							gmPlane.MyChessColor = "white";
							gmPlane.MyChessColorINT = 1;
							
							System.out.println("设置我的颜色为white");
						}
				}else if(msgsplit[0].equals("MSGTYPE:GamePlayerLingout")){
					if(gmPlane.chessBoard.RoomType.equals("CreateRoom")) {
						//JOptionPane.showMessageDialog(gmPlane, "加入者下线");
						
						
					}else {
						//JOptionPane.showMessageDialog(gmPlane, gmPlane.gameplayer2+"离开了房间");
						
						gmPlane.chessBoard.RoomType="CreateRoom";
					}
					gmPlane.chessBoard.jt.append("系统："+gmPlane.dateFormat.format(new Date())+"\n   "+gmPlane.gameplayer2+"离开了房间\n");
					gmPlane.gameplayer2 ="";
					
				}else if (msgsplit[0].equals("MSGTYPE:GAMEBEGIN")) {
					System.out.println("游戏开始");
					gmPlane.kaishi = true;
					GameRoomUtil.palyothermusic("source/begin.mp3");
					
				}else if (msgsplit[0].equals("MSGTYPE:GAMEREADY")) {
					if(msgsplit[1].equals("0")) {
						gmPlane.zhunbei = false;
					}else {
						gmPlane.zhunbei = true;
					}
				}else if(msgsplit[0].equals("MSGTYPE:GAMECHAT")){
					if(msgsplit[1].contains("快点")) {
						GameRoomUtil.palyothermusic("source/flowerdie.mp3");
					}
					gmPlane.chessBoard.jt.append(gmPlane.gameplayer2+"："+gmPlane.dateFormat.format(new Date())+"\n   "+msgsplit[1]+"\n");
					//设置总在最下方
					gmPlane.chessBoard.jt.setCaretPosition(gmPlane.chessBoard.jt.getDocument().getLength());
					
				}else if(msgsplit[0].equals("MSGTYPE:BreakGame")) {
					JOptionPane.showMessageDialog(gmPlane, "对方退出了房间，你赢的了这场比赛！");
					gmPlane.MyplayGamewinnum++;
					gmPlane.GameWinAfter(gmPlane);
				}
				gmPlane.repaint();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		System.out.println("sb线程退出");
	}
	
}

class xiaqiThread extends Thread{
	GamePlane gmplane;
	MouseEvent e;
	public xiaqiThread(GamePlane chessBoard, MouseEvent e) {
		this.gmplane = chessBoard;
		this.e = e;
	}

	@Override
	public void run() {
		
		if(gmplane.isme) {
			
			Point p = e.getPoint();
			if(gmplane.MouseAtChess) {
				if(!gmplane.kaishi) {
					JOptionPane.showMessageDialog(gmplane, "游戏还未开始您不能落子");
					return;
				}
				if(gmplane.chessPoint.size()==0&&gmplane.MyChessColorINT==1) {
					JOptionPane.showMessageDialog(gmplane, "黑方先手~~");
					return;
					
				}
				double x = (p.getX()-430)/45;
				double y = (p.getY()-110)/45;
				gmplane.rx = (int)x;
				gmplane.ry =(int)y;
				String xstr = Double.toString(x);
				String ystr = Double.toString(y);
				char xd = xstr.charAt(xstr.indexOf(".")+1);
				char yd = ystr.charAt(ystr.indexOf(".")+1);
				if(xd-'0'>5) {
					gmplane.rx += 1;
				}
				if(yd-'0'>5) {
					gmplane.ry +=1;
				}
				//System.out.println(chessBoard.rx+"..."+chessBoard.ry);
				if(gmplane.allChess[gmplane.rx][gmplane.ry] !=0) {
					System.out.println("一定落子了");
					return;
				}
				// 白1黑2
				if(gmplane.MyChessColor.equals("white")) {
					gmplane.allChess[gmplane.rx][gmplane.ry] =1;
				}else {
					gmplane.allChess[gmplane.rx][gmplane.ry] =2;
				}
				
				gmplane.chessPoint.add(gmplane.MyChessColor+","+(gmplane.rx*45+430)+","+(gmplane.ry*45+110));
				gmplane.isme = false;
				
				
				try {
					System.out.println(gmplane.rx+"..."+gmplane.ry);
					BeginWindow.out.write(("MSGTYPE:ChessGameing#"+gmplane.MyChessColor+","+gmplane.rx+","+gmplane.ry+"\r\n"));
					BeginWindow.out.flush();
					//判断是否赢了比赛
					boolean iswin = gmplane.checkwin();
					if(iswin) {
						System.out.println("你赢了啊！！！");
						BeginWindow.out.write(("MSGTYPE:ChessGameing#GameWIN"+"\r\n"));
						BeginWindow.out.flush();
						JOptionPane.showMessageDialog(gmplane, "你赢得了比赛！！");
						gmplane.chessBoard.jt.append("系统："+gmplane.dateFormat.format(new Date())+"\n   你赢得了比赛！！\n");
						gmplane.MyplayGamewinnum++;
						gmplane.GameWinAfter(gmplane);
					}
					gmplane.repaint();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			}
			
			
				
		}
	}
}
class GamePlane extends JSplitPane implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ImageIcon stopImage  = new ImageIcon("source/stop.png");
	static ImageIcon bgImage  = new ImageIcon("source/bgImage.jpg");
	static ImageIcon chatImage = new ImageIcon("source/chat.png");
	static ImageIcon moveImage = new ImageIcon("source/move.png");
	static ImageIcon beginBG = new ImageIcon("source/bfmusic.png");
	static ImageIcon StopBG = new ImageIcon("source/stopmusic.png");
	BufferedImage qizi = null;
	BufferedImage bqizi = null;
	BufferedImage hqizi = null;
	String blackMessage = "无限制";
	String whiteMessage = "无限制";
	String gameplayer1 ="";
	
	int GameWidth = 1300;
	int GameHeight = 1000;
	ChessBoard chessBoard;
	Boolean MouseAtChess =false;
	Boolean mousedown = false;
	boolean musicing = true;
	//当前棋子的行列
	int rx ;
	int ry ;
	//棋子的坐标
	List<String> chessPoint = new ArrayList<String>();
	//下过棋子的坐标
	Point mouseMove = new Point();
	//下过棋子的状态
	int [][]allChess = new int[15][15];
	Font gmFont = new Font("楷体",Font.BOLD,28);
	Color gameColor = new Color((int)0xB03060);
	//判断是自己下棋
	boolean isme = true;
	DecimalFormat df = new DecimalFormat("0.00");
	String gameplayer2 ="";
	
	
	
	String MyChessColor ="black";
	/*String MyChessColor ="black";*/
	//白1黑2
	int MyChessColorINT =2;
	BufferedImage Colorstaus;
	//胜率
	double MYWINLV = 0;
	double HisWINLV = 0;
	
	SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm:ss");
	int MyplayGamewinnum = 0;
	int HisplayGamewinnum = 0;
	// 总场数
	int PlayGamenum = 0;
	boolean zhunbei = false;
	boolean kaishi = false;

	public GamePlane(ChessBoard chessBoard,String gameplayer1) {
		setLayout(null);
		
		this.gameplayer1 = gameplayer1;
		this.chessBoard = chessBoard;
		//透明
		//setOpaque(false); 
		//不要忘记添加 这个事件
		addMouseListener(this);	
		new getServerMsg(this).start();
		try {
			
			qizi = ImageIO.read(new File("source/qizi.png"));
			hqizi = qizi.getSubimage(10, 10, 85, 85);
			bqizi = qizi.getSubimage(5, 95, 85,85);
			
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}	

		
		Room.man.setImage(Room.man.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
		Room.women.setImage(Room.women.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
		stopImage.setImage(stopImage.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		moveImage.setImage(moveImage.getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT));	
		addMouseMotionListener(new MouseMotionListener() {
		
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMove = e.getPoint();
				if(e.getX()>=430-10&&e.getX()<=1060+10&&e.getY()>=110&&e.getY()<=740+10) {

					
					if(!BeginWindow.bofang) {
						GameRoomUtil.playChessMovemusic("source/move.mp3");
					}
					MouseAtChess = true;
					
				}else {
					MouseAtChess = false;
				}
				
				repaint();
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
				
			}
	
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		BufferedImage bf = new BufferedImage(GameWidth, GameHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bf.createGraphics();
		
		//bf = g2.getDeviceConfiguration().createCompatibleImage(GameWidth, GameHeight, Transparency.TRANSLUCENT);
		//g2.dispose();
		//g2 = bf.createGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT); 
		g2.drawImage(GamePlane.bgImage.getImage(), 0, 0,GameWidth, GameHeight, GamePlane.bgImage.getImageObserver());
		g2.setColor(Color.BLACK);
		for(int i=0;i<15;i++) {
			g2.drawLine(430, 110+45*i, 1060, 110+45*i);
			g2.drawLine(430+45*i, 110, 430+45*i, 110+45*14);
		}
		
		
		
		
		g2.setFont(gmFont);
		g2.setColor(Color.black);

		g2.drawString("认输", 510, 820);
		GameRoomUtil.writeString(mouseMove, mousedown, "认输", g2, 510, 820, 180, 60);
		g2.drawString("悔棋", 710, 820);
		GameRoomUtil.writeString(mouseMove, mousedown, "悔棋", g2, 710, 820, 180, 60);
		g2.drawString("和棋", 910, 820);
		GameRoomUtil.writeString(mouseMove, mousedown, "和棋", g2, 910, 820, 180, 60);
		g2.drawImage(Room.man.getImage(), 150,70,80,80,Room.man.getImageObserver());
		g2.drawString(gameplayer1, 130,180);
		g2.drawString("胜率"+String.format("%.2f", MYWINLV*100)+"%", 130,220);
		if(gameplayer2!="") {
			g2.drawImage(Room.women.getImage(), 150,550,80,80,Room.women.getImageObserver());
			g2.drawString(gameplayer2, 130,660);
			g2.drawString("胜率"+String.format("%.2f", HisWINLV*100)+"%", 130,700);
		}
		if(chessBoard.RoomType.equals("CreateRoom")) {
			if(kaishi) {
				g2.drawString("正在游戏中", 650, 80);
				GameRoomUtil.writeString(mouseMove, mousedown, "正在游戏中", g2, 650, 80, 180, 60);
			}else {
				g2.drawString("开始游戏", 650, 80);
				GameRoomUtil.writeString(mouseMove, mousedown, "开始游戏", g2, 650, 80, 180, 60);
			}
			g2.setColor(Color.red);
			if(!gameplayer2.equals("")) {
				if(zhunbei) {
					g2.drawString("准备", 140,740);
				}else {
					g2.drawString("未准备", 140,740);
				}
			}
			
		}else {
			if(kaishi) {//如果游戏开始了 那么就 显示正在游戏
				g2.drawString("正在游戏中", 650, 80);
				GameRoomUtil.writeString(mouseMove, mousedown, "正在游戏中", g2, 650, 80, 180, 60);
			}else {//否则的话就是准备与取消
				if(zhunbei) {
					g2.drawString("取消准备", 650, 80);
					GameRoomUtil.writeString(mouseMove, mousedown, "取消准备", g2, 650, 80, 180, 60);
				}else {
					g2.drawString("点击准备", 650, 80);
					GameRoomUtil.writeString(mouseMove, mousedown, "点击准备", g2, 650, 80, 180, 60);
				}
			}
			
			
		}
	
		//画头像下的棋子颜色
				if(MyChessColor.equals("white")) {
					g2.drawImage(Room.chessWhite.getImage(), 150,260,60,60,this);
					if(!gameplayer2.equals(""))
					g2.drawImage(Room.chessBlack.getImage(), 150,750,60,60,this);
				}else {
					g2.drawImage(Room.chessBlack.getImage(), 150,260,60,60,this);
					if(!gameplayer2.equals(""))
					g2.drawImage(Room.chessWhite.getImage(), 150,750,60,60,this);
				}
			
			// 画棋子
				chessPoint.forEach((e)->{
					String msg[] = e.split(",");
					if(msg[0].equals("black")) {
						g2.drawImage(hqizi, (int)Integer.parseInt(msg[1])-30,(int)Integer.parseInt(msg[2])-30,60,60,this);
					}else {
						g2.drawImage(bqizi, (int)Integer.parseInt(msg[1])-34,(int)Integer.parseInt(msg[2])-20,60,60,this);
					}
					
				});
				// 最后一个棋子标红点
				if(chessPoint.size()>=1) {
					int x = (int)Integer.parseInt(chessPoint.get(chessPoint.size()-1).split(",")[1]);
					int y = (int)Integer.parseInt(chessPoint.get(chessPoint.size()-1).split(",")[2]);
					g2.setColor(Color.red);
					g2.fillOval(x, y, 8, 8);
				}
		//画移动棋子 因为移动棋子过快 开个线程画
		new PaintMoveChess(bf,g,g2,this).start();
		
		g.drawImage(bf,0,0,this);
	}
	//鼠标按下
	@Override
	public void mousePressed(MouseEvent e) {
		
		mousedown  = true;
		//gemePlane.repaint();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		new xiaqiThread(this,e).start();
		if (e.getX()>=510&&e.getX()<=630&&e.getY()>=820-40&&e.getY()<=880-40) {
			if(!kaishi) {
				JOptionPane.showMessageDialog(this, "游戏还没有开始~你就认输了？？","false",2);
				return;
			}
			int i =JOptionPane.showConfirmDialog(this, "你确定要认输吗","认输",2);
			if(i==0) {
				try {
					BeginWindow.out.write("MSGTYPE:ChessGameing#renshu\r\n");
					HisplayGamewinnum++;
					BeginWindow.out.flush();
					GameWinAfter(this);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
		
		}else if (e.getX()>=710&&e.getX()<=830&&e.getY()>=820-40&&e.getY()<=880-40) {
			if(!kaishi) {
				JOptionPane.showMessageDialog(this, "游戏还没有开始~","false",2);
				return;
			}
			if(chessPoint.size()==0) {
				JOptionPane.showMessageDialog(this, "棋盘还没有棋子，不要乱点啊，再点我生气了");
				return;
			}
			if(allChess[rx][ry]!=MyChessColorINT) {
				JOptionPane.showMessageDialog(this, "对方已经落子你不能悔棋");
				return;
			}

			int i =JOptionPane.showConfirmDialog(this, "你确定要悔棋吗","悔棋",2);
			if(i==0) {
				try {
					BeginWindow.out.write("MSGTYPE:ChessGameing#huiqi\r\n");
					BeginWindow.out.flush();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
		
		}else if (e.getX()>=910&&e.getX()<=1030&&e.getY()>=820-40&&e.getY()<=880-40) {
			if(!kaishi) {
				JOptionPane.showMessageDialog(this, "游戏还没有开始~","false",2);
				return;
			}
			int i =JOptionPane.showConfirmDialog(this, "你确定要和棋吗","和棋",2);
			if(i==0) {
				try {
					BeginWindow.out.write("MSGTYPE:ChessGameing#heqi\r\n");
					BeginWindow.out.flush();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
			
		}else if (e.getX()>=650&&e.getX()<=830&&e.getY()>=80-40&&e.getY()<=140-40) {
			if(!kaishi) {
				if(chessBoard.RoomType.equals("CreateRoom")) {
					//判断时候准备了
					
						if(zhunbei) {
							
							try {
								BeginWindow.out.write("MSGTYPE:GAMEBEGIN\r\n");
								BeginWindow.out.flush();
								kaishi = true;
								System.out.println("开始游戏了");
								GameRoomUtil.palyothermusic("source/begin.mp3");
							} catch (IOException e1) {
								
								e1.printStackTrace();
							}
							
						}else {
							if(gameplayer2!=null) {
								JOptionPane.showMessageDialog(chessBoard, gameplayer2+"还没有准备，无法开始游戏~");
								System.out.println("他还没有还没有准备");
								kaishi = false;
							}else {
								JOptionPane.showMessageDialog(chessBoard, "还没有玩家加入您的房间！无法开始游戏~");
								System.out.println("还没有玩家加入您的房间！无法开始游戏！");
							}
						
						}
						
					
					//加入玩家点击已准备发送消息
				}else {
					
					if(zhunbei) {
						zhunbei= false;
						try {
							BeginWindow.out.write("MSGTYPE:GAMEREADY#0\r\n");
							BeginWindow.out.flush();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						System.out.println("未准备！");
					}else {
						zhunbei = true;
						System.out.println("已准备！");
						try {
							BeginWindow.out.write("MSGTYPE:GAMEREADY#1\r\n");
							BeginWindow.out.flush();
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
					}
					//给服务器发送消息  zhunbei的状态;
					
				}
			}
		}else if (e.getX()>=1200&&e.getX()<=1250&&e.getY()>=30&&e.getY()<=80) {
			if(musicing) {
				musicing = false;
				GameRoomUtil.stopmusic();
			}else {
				musicing = true;
				GameRoomUtil.playBgmusic();
			}
			
		}
		
		repaint();
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		
		mousedown  = false;
		this.repaint();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("离开");
		
	}
	public  void GameWinAfter(GamePlane gamepanel) {
		gamepanel.isme = true;
		gamepanel.PlayGamenum++;
		zhunbei = false;
		kaishi = false;
		isme = true;
		System.out.println("当前总局数："+PlayGamenum+"对方的局数："+HisplayGamewinnum);
		System.out.println("当前总局数："+PlayGamenum+"你赢的局数："+MyplayGamewinnum);
		MYWINLV = (double)MyplayGamewinnum/PlayGamenum;
		HisWINLV = (double)HisplayGamewinnum/PlayGamenum;
		
		chessPoint.clear();
		for (int i = 0; i < allChess.length; i++) {
			for (int j = 0; j < allChess[i].length; j++) {
				allChess[i][j]=0;
			}
		}
		if(gamepanel.MyChessColor.equals("white")) {
			gamepanel.MyChessColorINT = 2;
			gamepanel.MyChessColor = "black";
			
		}else {
			gamepanel.MyChessColorINT = 1;
			gamepanel.MyChessColor = "white";
			
		}
	}
	
	public boolean checkwin() {
		int num = 0,l=1;
		boolean ks = true;
		//   \这种情况
		for(int k =0;k<8;k++) {
		
				//上半部分
				if(ks) {
					//确保不越界 
					if(rx-l>=0&&ry-l>=0) {
						if(allChess[rx-l][ry-l]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}
						}else {
							if(ks) {
								ks =false;
								l = 0;
							}
							
						}
						
					}else {
						if(ks) {
							ks =false;
							l = 0;
						}
					}
				}else {
					if(rx+l<=14&&ry+l<=14) {
						if(allChess[rx+l][ry+l]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}
							
						}else {
							break;
						}
					}
				}
				l++;
		}
		num = 0;l=1;ks = true;
		//   |这种情况
		for(int k =0;k<8;k++) {
			//上半部分
			if(ks) {
				//确保不越界 
				if(ry-l>=0) {
					if(allChess[rx][ry-l]==MyChessColorINT) {
						num++;
						if (num==4) {
							return true;
						}
					}else {
						if(ks) {
							ks =false;
							l = 0;
						}		
					}	
				}else {
					if(ks) {
						ks =false;
						l = 0;
					}
				}	
			}else {
				if(ry+l<=14) {
					if(allChess[rx][ry+l]==MyChessColorINT) {
						num++;
						if (num==4) {
							return true;
						}				
					}else {
						break;
					}
				}
			}
			l++;
		}
			num = 0;l=1;ks = true;
			//   /这种情况
			for(int k =0;k<8;k++) {
				//上半部分
				if(ks) {
					//确保不越界 
					if(ry-1>=0&&rx+l<=14) {
						if(allChess[rx+l][ry-l]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}
						}else {
							if(ks) {
								ks =false;
								l = 0;
							}
							
						}
						
					}else {
						if(ks) {
							ks =false;
							l = 0;
						}
					}	
				}else {
					if(rx-l>=0&&ry+l<=14) {
						if(allChess[rx-l][ry+l]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}
							
						}else {
							break;
						}
				
					}
					
		
				}
				l++;
		}
		
			num = 0;l=1;ks = true;
			//   ------ 这种情况
			for(int k =0;k<8;k++) {
				//上半部分
				if(ks) {
					//确保不越界 
					if(rx-l>=0) {
						if(allChess[rx-l][ry]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}
						}else {
							if(ks) {
								ks =false;
								l = 0;
							}					
						}				
					}else {
						if(ks) {
							ks =false;
							l = 0;
						}
					}	
				}else {
					if(ry+l<=14) {
						if(allChess[rx+l][ry]==MyChessColorINT) {
							num++;
							if (num==4) {
								return true;
							}				
						}else {
							break;
						}
					}
				}
			l++;
				
		}
			
				
		
		
		return false;
	}
	
}