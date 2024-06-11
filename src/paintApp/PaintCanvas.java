package paintApp;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

public class PaintCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener {

	PaintApp paintApp = new PaintApp();

	File file4=paintApp.file4;
	int eventnum =paintApp.eventnum;
	int wc = paintApp.wc ,hc=paintApp.hc;
	JColorChooser colorChooser=PaintApp.colorChooser;
	String log;
	// 描画内容を保持するBufferedImage
	private BufferedImage cImage = null;
	private BufferedImage img = null;
	private BufferedImage img2 = null;
	File file=new File("making_data/data/event1.txt");//イベントファイル(ストローク)
	File file2=new File("making_data/data/event2.txt");//イベントファイル(座標)
	File file3=new File("making_data/data/event3.txt");//イベントファイル(太さ）

	// cImageに描画するためのインスタンス
	private Graphics2D g2d,gimg,gimg2,gimg3;
	private int x, y, xx, yy;
	int type,width=1,alpha=255,spoitColor=0;		// 描画モードＯＲ消しゴムモード
	int i=0;//偶数回だけ描画
	int s=0;//ストローク番号
	public Color c = Color.BLACK;
	ArrayList<Integer> array = new ArrayList<Integer>(),array2 = new ArrayList<Integer>();
	ArrayList<Object> array4 = new ArrayList<>();
	int futo=0,hoso=50;
	Cursor cSpoit=null,cDraw=null;//cDrawはデフォルトカーソル
	
	

	public PaintCanvas() {

		x = -1;
		y = -1;
		xx = -1;
		yy = -1;
		type = 0;
		array.add(1);
		array.add(-1);
		array.add(-1);
		array.add(-1);
		array.add(-1);
		array.add(-1);
		array.add(-1);


		// MouseListener・MouseMotionListenerを設定
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		// キャンバスの背景を白に設定
		setBackground(Color.WHITE);

		cImage = new BufferedImage(wc, hc, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) cImage.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, wc, hc);

		img = new BufferedImage(wc, hc, BufferedImage.TYPE_INT_ARGB);
		gimg = (Graphics2D) img.getGraphics();

		img2 = new BufferedImage(wc, hc, BufferedImage.TYPE_INT_RGB);
		gimg2 = (Graphics2D) img2.getGraphics();
		gimg3 = (Graphics2D) img2.getGraphics();
		gimg2.setColor(Color.WHITE);
		gimg2.fillRect(0, 0, wc, hc);

		Image iconImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Image iconImage2 = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		//アイコンイメージの読み込み
		try {
			iconImage = ImageIO.read(new File("resource/spoit2.png"));
			cSpoit = Toolkit.getDefaultToolkit().createCustomCursor(iconImage, new java.awt.Point(), "");//スポイトカーソルに画像設定
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			iconImage2 = ImageIO.read(new File("resource/pen.png"));
			cDraw = Toolkit.getDefaultToolkit().createCustomCursor(iconImage2, new java.awt.Point(), "");//カーソルに画像設定
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		setCursor(cDraw);//カーソルのデフォルトを設定

		//txtファイル生成
		try {
			createFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File("making_data/data/image");
		if (!file.exists()) {
			Path path1 = Paths.get("making_data/data/image");
			try {
				Files.createDirectory(path1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		repaint();
	}

	//commentをストローク番号とともに格納するためのクラス
	public void comNumber(String com) {
		array4.add(s);
		array4.add(com);
		System.out.println(array4);
	}

	public void comSave() {
		FileWriter filewriter;
		try {
			filewriter = new FileWriter(file4, true);
			PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
			pw.println(array4);
			pw.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		array4.clear();
	}

	public void createFile() throws IOException{
		//createNewFileメソッドを使用してファイルを作成する
		if (file.createNewFile()){
			System.out.println("ファイル作成成功１");
		}else{
			System.out.println("ファイル作成失敗１");
		}
		if (file2.createNewFile()){
			System.out.println("ファイル作成成功２");
		}else{
			System.out.println("ファイル作成失敗２");
		}
	}

	// キャンバスをクリア
	public void clear() {
		array.clear();
		array2.clear();
		array4.clear();
		fileClear(file);
		fileClear(file2);
		fileClear(file3);
		//file4を初期化
		fileClear(file4);
		
		clearDirectory("making_data/data/image");//image画像を消去
		
		s=0;
		futo = 0;
		hoso = 50;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, wc, hc);
		repaint();
		//絵が消えるだけで作られたファイルは残ります　手付かず
	}
	
	//image画像を消去する
	public void clearDirectory(String directoryPath) {
	    File dir = new File(directoryPath);
	    File[] files = dir.listFiles();
	    if (files != null) {
	        for (File file : files) {
	            if (!file.isDirectory()) {
	                file.delete();
	            }
	        }
	    }
	}

	//描いた絵の内容を保存している
	public void save() {
		JFileChooser filechooser = new JFileChooser("making_data/data");
		int selected = filechooser.showSaveDialog(this);
		if (selected == JFileChooser.APPROVE_OPTION) {
			File file = filechooser.getSelectedFile();
			try {
				ImageIO.write(cImage, "jpg", file);
			} catch (IOException ox) {
				// TODO: handle exception
				ox.printStackTrace();
			}
		}
		FileWriter filewriter;
		try {
			filewriter = new FileWriter(file3, true);
			PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
			pw.println(futo);
			pw.println(hoso);
			pw.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		array2.clear();
	}



	public void saveRenban(){
		String filename = "making_data/data/image/"+eventnum+".jpg";
		try {
			ImageIO.write(cImage, "jpg", new File(filename));
		} catch (IOException ox) {
			// TODO: handle exception
			ox.printStackTrace();
		}
	}

	//イベントファイル初期化
	public void fileClear(File file){
		FileWriter filewriter;
		try {
			filewriter = new FileWriter(file);
			PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
			pw.print("");
			pw.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}

	// 線の太さ変更
	public void setStroke(int n) {
		width = n;
	}


	// 線の透明度変更
	public void setAlpha(int n) {
		alpha = n*255/100;
		System.out.println(n);
		System.out.println(alpha);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)n/100);
		gimg2.setComposite(ac);
	}


	// 線の色変更
	public void setColor(Color color) {
		c =color;
		if(type == 3){
			spoitColor = 1;
		}else{
			spoitColor = 0;
		}
	}

	//mouseの太さ変更
	public void setPenIcon() {
		Image img = new BufferedImage(width+3,width+3,BufferedImage.TYPE_INT_ARGB);
		Cursor c;
		Graphics2D pen = (Graphics2D) img.getGraphics();
		pen.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		pen.setPaintMode();
		pen.setColor(Color.black);
		pen.drawOval(0, 0, width, width);
		c = Toolkit.getDefaultToolkit().createCustomCursor(img, new java.awt.Point(), "");//スポイトカーソルに画像設定
		cDraw = c;
		setCursor(cDraw);
	}

	public BufferedImage setSampleIcon() {
		BufferedImage img = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 50, 50);
		g2d.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
		g2d.fillOval((50-width)/2, (50-width)/2, width, width);

		return img;
	}

	public void setArray(){
		float hsb[] = new float[3];

		array.clear();//changeをチェックしたらarray全消し、セットへ
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(),hsb);
		array.add(s);
		array.add((int) (100*hsb[0]));	//色相
		array.add((int) (100*hsb[1]));	//彩度
		array.add((int) (100*hsb[2]));	//明度
		array.add((Integer) alpha);
		array.add((Integer) width);
		array.add(spoitColor);

		array2.add(0,s++);
	}

	public void setArray2(int x,int y){
		array2.add((Integer) x);
		array2.add((Integer) y);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img2, 0, 0, null);
	}

	public void update(Graphics g){
		paint(g);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// 押されているボタンを検知
		if(type != 3){
			type = 1;
			// 過去の座標を開始座標に設定
			xx = x;
			yy = y;
			// 新しい座標を終了座標に設定
			Point point = e.getPoint();
			x = point.x;
			y = point.y;

			//img2をストローク描画前へ
			gimg3.drawImage(cImage, 0, 0, null);
			//描画
			if (x+width/2 >= 0 && y+width/2 >= 0 && xx+width/2 >= 0 && yy+width/2 >= 0) {
				BasicStroke stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				gimg.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue()));
				gimg.setStroke(stroke);
				gimg.drawLine(xx+width/2, yy+width/2, x+width/2,y+width/2);

				//imgに書いたストロークをimg2に描画
				gimg2.drawImage(img,0,0,null);
			}

			//array2
			if(i==0){
				setArray2(x,y);
				i=1;
			}else{
				i=0;
			}
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// ドラッグが終了したら座標を初期化
		if(type != 3){
			x = -1;
			y = -1;
			xx = -1;
			yy = -1;
			type = 0;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(type != 3){
			Point point = e.getPoint();
			x = point.x;
			y = point.y;
			setArray2(x,y);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		i=0;
		if(type == 3){
			Point point = e.getPoint();
			x = point.x;
			y = point.y;
			Color color = new Color( cImage.getRGB(x, y) );
			setColor(color);
			//colorChooser.setColor(color);
			type = 1;
			setCursor(cDraw);//ボタンを離すと自動的にペンに切り替わる。

		}else{
			g2d.drawImage(img2,0,0,null);//pen部分を透明にしてcImageに
			gimg3.drawImage(cImage, 0, 0, null);
			if(futo<width){
				futo = width;
			}
			if(width<hoso){
				hoso = width;
			}
			setArray();
			FileWriter filewriter;
			try {
				filewriter = new FileWriter(file, true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
				pw.println(array);
				pw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}

			try {
				filewriter = new FileWriter(file2, true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
				pw.println(array2);
				pw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			array2.clear();
			System.out.println("release");
			repaint();

			img = new BufferedImage(wc, hc, BufferedImage.TYPE_INT_ARGB);
			gimg = (Graphics2D) img.getGraphics();
			saveRenban();
			eventnum++;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		if(keycode == KeyEvent.VK_I) {
			type = 3;
			setCursor(cSpoit);
		}
		else if(keycode == KeyEvent.VK_A) {
			type = 1;
			setCursor(cDraw);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}