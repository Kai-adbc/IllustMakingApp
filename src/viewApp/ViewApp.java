package viewApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ViewApp{
	static int w = 650,h = 585, wc=600, hc=500,w2=810;
	static StatusManager statusManager;
	static ExampleView exampleView;
	static EventCanvas eventCanvas;
	static EnlargedCanvas enlargedCanvas;
	static ImagePanel imagePanel;
	static MemoCanvas memoCanvas;

	public static void main(String[] args) {
		run();
	}

	public static void run(){
		statusManager = new StatusManager();	

		//お手本ウィンドウ
		JFrame frame = new JFrame("完成図");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0,0, w, h);

		//SpringLayout layout = new SpringLayout();
		JPanel eventpane = new JPanel();
		//eventpane.setLayout(layout);
		frame.getContentPane().add(eventpane, BorderLayout.CENTER);

		//完成図パネ
		JPanel pane = new JPanel();
		frame.getContentPane().add(pane, BorderLayout.CENTER);

		//ボタンパネ
		JPanel paneB = new JPanel();
		frame.getContentPane().add(paneB, BorderLayout.NORTH);

		//イベントパネ
		JPanel paneD = new JPanel();

		//拡大パネ
		JPanel paneE = new JPanel();

		//画像表示パネ
		JPanel paneG = new JPanel();

		//完成図ビュー
		exampleView= new ExampleView(statusManager);
		exampleView.setPreferredSize(new Dimension(wc, hc));
		ExampleButton_sonota examplebutton_sonota = new ExampleButton_sonota(statusManager,exampleView);
		paneB.add(examplebutton_sonota);
		ToggleCircleButton toggleCircleButton = new ToggleCircleButton(statusManager, exampleView);
		paneB.add(toggleCircleButton);
		pane.add(exampleView);

		//イベントキャンバス
		eventCanvas = new EventCanvas(statusManager);
		statusManager.setEventCanvas(eventCanvas);


		//イベントウィンドウ
		JFrame frame2 = new JFrame("グラフ");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setBounds(0,628,w+w2, 250);
		frame2.setFocusable(true);
		frame2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keycode = e.getKeyCode();
				if(keycode == KeyEvent.VK_UP) {
					statusManager.addEvenum();
				}
				else if(keycode == KeyEvent.VK_DOWN) {
					statusManager.removeEvenum();
				}
				else if(keycode == KeyEvent.VK_RIGHT) {
					statusManager.addEvenum2();
				}
				else if(keycode == KeyEvent.VK_LEFT) {

					statusManager.removeEvenum2();
				}
			}
		});



		//イベントパネル
		JLabel backLabel1=new JLabel();
		JLabel backLabel2=new JLabel();
		JLabel backLabel3=new JLabel();
		JLabel backLabel4=new JLabel();
		JLabel backLabel5=new JLabel();
		backLabel1.setIcon(fillIcon(0));
		backLabel2.setIcon(fillIcon(64));
		backLabel3.setIcon(fillIcon(127));
		backLabel4.setIcon(fillIcon(191));
		backLabel5.setIcon(fillIcon(255));

		backLabel1.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.setBack(0);
				eventCanvas.setBack();
				statusManager.setEventIcon();
			}});
		backLabel2.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.setBack(64);
				eventCanvas.setBack();
				statusManager.setEventIcon();
			}});
		backLabel3.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.setBack(127);
				eventCanvas.setBack();
				statusManager.setEventIcon();
			}});
		backLabel4.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.setBack(191);
				eventCanvas.setBack();
				statusManager.setEventIcon();
			}});
		backLabel5.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.setBack(255);
				eventCanvas.setBack();
				statusManager.setEventIcon();
			}});

		paneD.add(backLabel1);
		paneD.add(backLabel2);
		paneD.add(backLabel3);
		paneD.add(backLabel4);
		paneD.add(backLabel5);
		paneD.setEnabled(true);
		frame2.getContentPane().add(paneD, BorderLayout.NORTH);

		JScrollPane s;
		JLabel scrolllabel=new JLabel();
		scrolllabel.setFocusable(true);
		scrolllabel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				statusManager.clickEvent(e.getPoint().x, e.getPoint().y);
			}
		});

		//キーボードでグラフの操作をした場合、statusManagerを更新している。
		s = new JScrollPane(scrolllabel);
		s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		s.setFocusable(true);
		s.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				int keycode = e.getKeyCode();
				if(keycode == KeyEvent.VK_UP) {
					statusManager.addEvenum();
				}
				else if(keycode == KeyEvent.VK_DOWN) {
					statusManager.removeEvenum();
				}
				else if(keycode == KeyEvent.VK_RIGHT) {
					statusManager.addEvenum2();
				}
				else if(keycode == KeyEvent.VK_LEFT) {
					statusManager.removeEvenum2();
				}
			}
		});

		frame2.getContentPane().add(s, BorderLayout.CENTER);
		statusManager.setScrollPane(s,frame2,scrolllabel);//statusManagerのオブジェクト内で引数でせtScrollPaneを呼び出し

		//拡大図ウィンドウ
		JFrame frame3 = new JFrame("拡大図");
		frame3.getContentPane().add(paneE,BorderLayout.CENTER);
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame3.setBounds(655,0,w2,h);
		enlargedCanvas = new EnlargedCanvas(statusManager);
		enlargedCanvas.setPreferredSize(new Dimension(780,400));
		paneE.add(enlargedCanvas);
		statusManager.setEnlargedCanvas(enlargedCanvas);

		JPanel paneF = new JPanel();
		ImageIcon penIcon = new ImageIcon();
		JLabel penlabel = new JLabel(penIcon);
		statusManager.setpenIcon(penIcon);
		paneF.add(penlabel);
		JLabel hue = new JLabel("色相:");
		JLabel satu = new JLabel(" 彩度:");
		JLabel bright = new JLabel(" 値:");
		JLabel alpha = new JLabel(" 不透明度:");
		JLabel width = new JLabel(" 太さ:");
		statusManager.setLabel(hue,satu,bright,alpha,penlabel,width);
		paneF.add(hue);
		paneF.add(satu); 
		paneF.add(bright);
		paneF.add(alpha);
		paneF.add(width);
		frame3.getContentPane().add(paneF,BorderLayout.NORTH);

		memoCanvas = new MemoCanvas(statusManager);
		statusManager.setMemoCanvas(memoCanvas);
		memoCanvas.MemoWindow(frame3);

		//		// 拡大図ウィンドウの後に新しいウィンドウを追加
		//		JFrame frame4 = new JFrame("画像表示ウィンドウ");
		//		JPanel paneG = new JPanel();
		//		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		frame4.getContentPane().add(paneG,BorderLayout.CENTER);
		//		frame4.setBounds(655, 585, w2, 300); // 位置とサイズを設定
		//		imagePanel = new ImagePanel(statusManager);
		//		enlargedCanvas.setPreferredSize(new Dimension(780,400));
		//		paneE.add(enlargedCanvas);
		//		statusManager.setEnlargedCanvas(enlargedCanvas);
		//
		//		// ImagePanel クラスのインスタンスを作成
		//		ImagePanel imagePanel = new ImagePanel(statusManager);


		//		// スクロールパネルを作成し、ImagePanel を追加
		//		JScrollPane scrollPane = new JScrollPane(imagePanel);
		//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		//
		//		// スクロールパネルをフレームに追加
		//		frame4.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// 新しいウィンドウを表示

		//		//拡大図ウィンドウ
		//		JFrame frame4 = new JFrame("メモ表示");
		//		frame4.getContentPane().add(paneG,BorderLayout.CENTER);
		//		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		frame4.setBounds(655,445,w2,440);
		//		imagePanel = new ImagePanel(statusManager);
		//		imagePanel.setPreferredSize(new Dimension(780,400));
		//		paneG.add(imagePanel);
		//		statusManager.setImagePanel(imagePanel);
		//		frame4.setVisible(true);



		frame.setVisible(true);
		frame2.setVisible(true);
		frame3.setVisible(true);
		//frame4.setVisible(true);

		//コメント表示エリア

		//		JFrame frame4= new JFrame("メモ");
		//		JPanel paneG=new JPanel();
		//		frame4.getContentPane().add(paneG,BorderLayout.CENTER);
		//		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		memoCanvas = new MemoCanvas(statusManager);
		//		frame4.setBounds(690,650,w2,340);
		//		statusManager.setMemoCanvas(memoCanvas);

		// memoCanvas.MemoWindow(frame3);
	}

	//グラフでの背景の明るさ
	//このクラスの主な目的は、背景の明るさの変更に応じて関連するオブジェクトの状態を更新すること。
	static class BackListener implements ChangeListener {
		EventCanvas eventCanvas;
		StatusManager statusManager;

		public BackListener(EventCanvas eventCanvas, StatusManager statusManager) {
			super();
			this.eventCanvas = eventCanvas;
			this.statusManager = statusManager;
		}
		public void stateChanged(ChangeEvent e) {
			JSlider source2 = (JSlider) e.getSource();
			int fps2 = (int) source2.getValue();
			statusManager.setBack(fps2);
			eventCanvas.setBack();
			statusManager.setEventIcon();
		}
	}
	public static ImageIcon fillIcon(int a) {
		int back;
		BufferedImage img= new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB);
		ImageIcon icon = new ImageIcon();
		Graphics2D g = img.createGraphics();

		back = a;
		g.setColor(new Color(back,back,back));
		g.fillRect(0,0,20,40);
		icon.setImage(img);
		return icon;
	}
}