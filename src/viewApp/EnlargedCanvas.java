package viewApp; //拡大図についてのクラス

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnlargedCanvas extends Canvas implements MouseMotionListener,KeyListener,MouseListener{
	StatusManager statusManager;
	static float bairitsu = 1;
	static int X = 250, Y =30, W = 280, W2 = 224, H = 280, H2 = 224;//トリミング後画像を表示する正方形のサイズ
	int wc = 600,hc = 500;
	static int A=15;//拡大図同士の幅
	int newX,newY,newW, newH;//実際に描画する画像のサイズ
	Graphics2D g2d;
	int x,y,w,h;
	BufferedImage now=new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
	BufferedImage prev=new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
	BufferedImage next = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
	BufferedImage nowImage,prevImage,nextImage;

	public EnlargedCanvas(StatusManager statusManager){
		this.statusManager= statusManager;
		// MouseListener・MouseMotionListenerを設定
		addMouseListener(this);
		addKeyListener(this);
		repaint();
	}

	//トリミング範囲設定、repaint時の倍率設定
	public void setXYWH(){
		x = statusManager.getX();
		y = statusManager.getY();
		w = statusManager.getW();
		h = statusManager.getH();

		if(h < w){
			bairitsu = (float)W/(float)w;
			newW = W;
			newH = (int)(h*bairitsu);
			newX = X;
			newY = Y+((H-newH)/2);
		}else if (w < h){
			bairitsu = (float)H/(float)h;
			newW = (int)(w*bairitsu);
			newH = H;
			newX = X+((W-newW)/2);
			newY = Y;
		}else{
			bairitsu = 1;
			newW = W;
			newH = H;
			newX = X;
			newY = Y;
		}
		showView();//ビューも実行
	}

		//拡大図の画像それぞれを描画している。
		public void paint(Graphics g){
			int newW2 = (int)((float)newW*0.80);
			int newH2 = (int)((float)newH*0.80);
			g.setColor(Color.WHITE);
			g.fillRect(X-W2-A, Y+15, W2, H2);
			g.fillRect(X, Y, W, H);
			g.fillRect(X+W+A, Y+15, W2, H2);
			g.drawImage(prevImage, newX-W2-A, newY+15, newW2, newH2, this);
			g.drawImage(nowImage, newX, newY, newW,newH, this);
			g.drawImage(nextImage, newX+W+A, newY+15, newW2, newH2, this);

			//拡大図での画像の枠線を描画している。
			//prev（過去）は淡い赤（ピンク）をもう少し濃くした色、now（現在）は赤、next（未来）は柔らかい赤

			((Graphics2D) g).setStroke(new BasicStroke(2));//線のふとさ
			g.setColor(Color.RED); // 現在（now）の枠線の色
			g.drawRect(X,Y,W,H);

			((Graphics2D) g).setStroke(new BasicStroke(1.5F));//線のふとさ
			g.setColor(new Color(240,140,170)); // 過去（prev）の枠線の色: 淡いピンクをもう少し濃くした色
			g.drawRect(X-W2-A, Y+15, W2, H2);

			g.setColor(new Color(205,85,85)); // 未来（next）の枠線の色: 柔らかい赤
			g.drawRect(X+W+A, Y+15, W2, H2);

		}

		//imageごとにファイルネイムを定めて、filenameを返す。
		public String setFilename(int i){
			String filename = statusManager.folderName+"/image/"+i+".jpg";
			return filename;
		}

		//拡大図に表示するための画像を読み込んでいる。
		public void showView(){
			int i = statusManager.evenum;

			if(0 < i){
				System.out.println("if:"+i);
				try {prev =ImageIO.read(new File(setFilename(statusManager.event.get(i-1))));}
				catch (IOException e) {e.printStackTrace();}
			}else{
				System.out.println("else:"+i);
				Graphics2D g2d = (Graphics2D) prev.createGraphics();
				g2d.fillRect(0, 0, 600, 400);
			}

			try {now =ImageIO.read(new File(setFilename(statusManager.event.get(i))));}
			catch (IOException e) {e.printStackTrace();}

			if(i<statusManager.event.size()-1){
				try {next =ImageIO.read(new File(setFilename(statusManager.event.get(i+1))));}
				catch (IOException e) {e.printStackTrace();}
			}else{
				Graphics2D g2d = (Graphics2D) next.createGraphics();
				g2d.fillRect(0, 0, wc, hc);
			}
			nowImage = trimImage(now);
			prevImage = trimImage(prev);
			nextImage = trimImage(next);
			repaint();
		}

		//showViewとあまり変わらない。なんの役割があるの？
		public void showView2(){
			int i = statusManager.groupnum;

			if(0 < i){
				try {prev =ImageIO.read(new File(setFilename(statusManager.event.get(statusManager.event3.get(i-1)))));}
				catch (IOException e) {e.printStackTrace();}
			}else{
				Graphics2D g2d = (Graphics2D) prev.createGraphics();
				g2d.fillRect(0, 0, wc, hc);
			}

			try {now =ImageIO.read(new File(setFilename(statusManager.event.get(statusManager.event3.get(i)))));
			System.out.println("file:"+statusManager.event.get(statusManager.event3.get(i)));
			}
			catch (IOException e) {e.printStackTrace();}

			if(i<statusManager.event3.size()-1){
				try {next =ImageIO.read(new File(setFilename(statusManager.event.get(statusManager.event3.get(i+1)))));}
				catch (IOException e) {e.printStackTrace();}
			}else{
				Graphics2D g2d = (Graphics2D) next.createGraphics();
				g2d.fillRect(0, 0, wc, hc);
			}
			nowImage = trimImage(now);
			prevImage = trimImage(prev);
			nextImage = trimImage(next);
			repaint();
		}

		//座標 (x, y) から (x + w, y + h) の範囲でトリミングしてtrimmImageを返す
		public BufferedImage trimImage(BufferedImage image){
			BufferedImage trimmedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			trimmedImage.getGraphics().drawImage(image, 0, 0, w, h, x, y, x + w, y + h, null);
			return trimmedImage;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		@Override
		//矢印キーが押された時に、拡大図の画像が切り替わるのを実装している。
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_UP) {
				statusManager.addEvenum();
			}
			else if(keycode == KeyEvent.VK_DOWN) {
				statusManager.removeEvenum();
			}
			if(keycode == KeyEvent.VK_RIGHT) {
				statusManager.addEvenum2();
			}
			else if(keycode == KeyEvent.VK_LEFT) {
				statusManager.removeEvenum2();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		public void mousePressed(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		public void mouseReleased(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		public void mouseEntered(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		public void mouseExited(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
		}
	}