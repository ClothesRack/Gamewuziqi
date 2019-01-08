package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.raven.main.BeginWindow;
import com.raven.main.Room;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
public class GameRoomUtil {
	
	static Player p;
	static Thread bGThread;
	public static String GetGames(Room room){
		String gameRoomName;
		try {
			

			BeginWindow.out.write("MSGTYPE:GetOnlineGame\n");
			BeginWindow.out.flush();
			
			
			String b  = BeginWindow.in.readLine();
			
			gameRoomName =  new String(b) ;
			return gameRoomName;
			
		
		} catch (IOException e) {
			
		}catch (NullPointerException e) {
			room.priwid.setVisible(true);
			room.setVisible(false);
			
			JOptionPane.showMessageDialog(room, "Raven的服务器连接有问题，请启动本地Server服务");
		}
		
		return null;
		
	}
	public static void CenterWindow(JFrame jFrame){
		int ScreenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int ScreenHeight =Toolkit.getDefaultToolkit().getScreenSize().height;
		jFrame.setLocation((ScreenWidth-jFrame.getWidth())/2, (ScreenHeight-jFrame.getHeight())/2);
	}
	//i,j为左上角顶点,k,l为宽高
	//    -----------
	//	  |         |
	//    -----------
	public static void writeString(Point p ,Boolean mousedown,String string,Graphics g2, int i, int j, int k, int l) {
		
		
		if(p.getX()>=i-30&&p.getX()<=i-30+k&&p.getY()>=j-40&&p.getY()<=j-40+l) {
			if(mousedown) {
				g2.setColor(Color.RED);
			}else {
				g2.setColor(Color.cyan);
			}
			
		
				//画矩形
			g2.drawRect(i-30,j-40,k,l);

			g2.drawString(string,i,j);
			
		}
		g2.setColor(Color.black);
	}
	public static void palyothermusic(String filename){
		
		new Thread(()->{
			
		File file = new File(filename);
		if(file.exists()) {
			try {
				InputStream in = new FileInputStream(file);
				
				Player p = new Player(in);
				p.play();
				
			} catch (FileNotFoundException e1) {
			
				e1.printStackTrace();
			}catch (JavaLayerException e1) {
				
				e1.printStackTrace();
			}	
		}
		
		}).start();
	}
	public static  void  playChessMovemusic(String filename) {
		new Thread(()->{
			BeginWindow.bofang = true;
		File file = new File(filename);
		try {
			InputStream in = new FileInputStream(file);
			
			Player play = new Player(in);
			play.play();
			BeginWindow.bofang = false;
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}catch (JavaLayerException e1) {
			
			e1.printStackTrace();
		}	
		}).start();
	}
	public static  void  playBgmusic() {
	bGThread= 	new Thread() {
			@Override
			public void run() {
				while(true) {
				
					File file = new File("source/bgmusic.mp3");
					try {
						InputStream in = new FileInputStream(file);
						p = new Player(in);
						p.play();
						
						
						
					} catch (FileNotFoundException e1) {
						
						e1.printStackTrace();
					}catch (JavaLayerException e1) {
						
						e1.printStackTrace();
					}	
				
				}
			}
		};
		bGThread.start();
	}
	public static void stopmusic(){
		if(bGThread!=null)
		{
			
			bGThread.stop();
			bGThread = null;
		}
	
		
		
		
	}

}
