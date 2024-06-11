package paintApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PaintApp {
	static int w=2000, h=1000;
	static int wc=600,hc=500; //キャンバス
	static int eventnum =0;
	static String log;//logのための変数
	static String com;//一つのコメントを一時的に保存する変数
	static JColorChooser colorChooser;
	
	public static void main(String[] args) {
		run();
	}

	public static void run() {
		JFrame frame=new JFrame("お絵描きあぷり");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(w, h);
		frame.setLocationRelativeTo(null);
		
		JPanel pane=new JPanel();
		frame.getContentPane().add(pane,BorderLayout.CENTER);
		JPanel paneB=new JPanel();
		frame.getContentPane().add(paneB,BorderLayout.NORTH);
		JPanel paneC=new JPanel();
		frame.getContentPane().add(paneC,BorderLayout.EAST);
		
		
		// PaintCanvasのインスタンスを生成
		PaintCanvas canvas = new PaintCanvas();
		canvas.setPreferredSize(new Dimension(wc, hc));
		pane.add(canvas);
		
		// 全消去
		JMenuBar menubar=new JMenuBar();
		JMenu menu1=new JMenu("メニュー");
		menubar.add(menu1);
		JMenuItem menuitem1=new JMenuItem("保存");
		JMenuItem menuitem2=new JMenuItem("全消去");
		menuitem1.addActionListener(new SaveListener(canvas));
		//menuitem1.addActionListener(new LogSaveListener(canvas));
		menuitem2.addActionListener(new ClearListener(canvas));
		menu1.add(menuitem1);
	    menu1.add(menuitem2);
	    frame.setJMenuBar(menubar);
	    
	    //コメント機能の実装
	    JFrame frame2=new JFrame("お絵描きあぷり");
	    
	    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame2.setSize(200,300 );
	    frame2.setLocationRelativeTo(null);
	    JPanel comPane=new JPanel();
	    frame2.getContentPane().add(comPane,BorderLayout.CENTER);
	    JTextArea logArea = new JTextArea(3,30);
	    JTextArea commentArea = new JTextArea(3,30);
	    commentArea.setLineWrap(true);
	    JLabel commentLabel = new JLabel();
	    JLabel commentLabel2 = new JLabel();
	    commentLabel.setText("メモ");
	    commentLabel2.setText("      ");
	    JButton saveButton=new JButton("保存");
	    // アクションの定義
	    saveButton.addActionListener(new AbstractAction(){
	    	public void actionPerformed(ActionEvent arg0) {
	    		com=commentArea.getText();
	    	    canvas.comNumber(com);
	    	    canvas.comSave();
	    		logArea.append(com + "\n");
	    		
	    		  //logにlogAreaの内容を格納
	    	    log=logArea.getText();
	    	    System.out.println(log);
	    	}
	    });
	    paneB.add(commentLabel);
	    paneB.add(commentArea);
	    paneB.add(saveButton);
	    paneB.add(commentLabel2);
	    comPane.add(logArea);
	    
	  
	  		
	    //線の太さ調節
	    JLabel sizeLabel = new JLabel();
		BufferedImage img = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 50, 50);
		ImageIcon sizeIcon = new ImageIcon(img);
		JSlider size = new JSlider(1, 50, 1); // 最小値、最大値、初期値
		SpinnerNumberModel spinnermodel = new SpinnerNumberModel(1,1,50,1);
		JSpinner sizeSpinner = new JSpinner(spinnermodel);
		sizeLabel.setIcon(sizeIcon);
		sizeLabel.setText("太さ");
		sizeSpinner.setPreferredSize(new Dimension(45,25));
		sizeSpinner.addChangeListener(new SizeSpinnerListener(canvas,size,sizeLabel));
		size.addChangeListener(new SizeSliderListener(canvas,sizeSpinner));
		paneB.add(sizeLabel);
		paneB.add(sizeSpinner);
		paneB.add(size);
		
		
		// 線の透明度調節
		JLabel alphaLabel = new JLabel();
		JSlider alpha = new JSlider(1, 100, 100); // 最小値、最大値、初期値
		SpinnerNumberModel spinnermodel2 = new SpinnerNumberModel(100,1,100,1);// 初期値、最小値、最大値、単位
		JSpinner alphaSpinner = new JSpinner(spinnermodel2);
		alphaLabel.setText("   不透明度");
		sizeSpinner.setPreferredSize(new Dimension(45,25));
		alphaSpinner.addChangeListener(new AlphaSpinnerListener(canvas,alpha,sizeLabel));
		alpha.addChangeListener(new AlphaSliderListener(canvas,alphaSpinner,sizeLabel));
		paneB.add(alphaLabel);
		paneB.add(alphaSpinner);
		paneB.add(alpha);
		
		// 2019.10.08 K. Misue
		//2023.04.24 中川「色を選択するためのツールと思われる」
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(new ColorListener(canvas, colorChooser));
		paneC.add(colorChooser);
		
		frame.setVisible(true);
		frame2.setVisible(true);
	}
	
	// クリアボタン用
	static class ClearListener implements ActionListener {
		PaintCanvas canvas;
		public ClearListener(PaintCanvas canvas) {
			super();
			this.canvas = canvas;
	    }
	    @Override
		public void actionPerformed(ActionEvent e) {
			canvas.clear();
		}
	}

	// ファイル保存
	static class SaveListener implements ActionListener {
		PaintCanvas canvas;
		public SaveListener(PaintCanvas canvas) {
			super();
			this.canvas = canvas;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.save();
			//絵の保存を押した時logSaveを呼び出している。
			//iilogSave(log);
		}
	}

	// 太さスライダ用
	static class SizeSliderListener implements ChangeListener {
		PaintCanvas canvas;
		JSpinner spinner;
		public SizeSliderListener(PaintCanvas canvas,JSpinner spinner) {
			super();
			this.canvas = canvas;
			this.spinner = spinner;
		}
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			int fps = (int) source.getValue();
			spinner.setValue(fps);
			canvas.setStroke(fps);
		}
	}

	// 太さスピナー用
	static class SizeSpinnerListener implements ChangeListener {
		PaintCanvas canvas;
		JSlider slider;
		JLabel sizeLabel;
		public SizeSpinnerListener(PaintCanvas canvas,JSlider slider,JLabel sizeLabel) {
			super();
			this.canvas = canvas;
			this.slider = slider;
			this.sizeLabel = sizeLabel;
		}
		public void stateChanged(ChangeEvent e) {
			JSpinner source = (JSpinner)e.getSource();
			BufferedImage img = new BufferedImage(50,50,BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) img.getGraphics();

			int fps = (int) source.getValue();
			slider.setValue(fps);
			canvas.setStroke(fps);
			canvas.setPenIcon();

			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 50, 50);
			g2d.setColor(new Color(canvas.c.getRed(),canvas.c.getGreen(),canvas.c.getBlue(),canvas.alpha));
			g2d.fillOval((50-fps)/2, (50-fps)/2, fps, fps);
			ImageIcon icon = new ImageIcon(img);
			sizeLabel.setIcon(icon);
		}
	}
	// 透明度スライダ用
	static class AlphaSliderListener implements ChangeListener {
		PaintCanvas canvas;
		JSpinner spinner;
		JLabel label;
		public AlphaSliderListener(PaintCanvas canvas, JSpinner spinner, JLabel sizeLabel) {
			super();
			this.canvas = canvas;
			this.spinner = spinner;
			this.label = sizeLabel;
		}
		public void stateChanged(ChangeEvent e) {
			JSlider source2 = (JSlider) e.getSource();
			int fps2 = (int) source2.getValue();
			spinner.setValue(fps2);
			canvas.setAlpha(fps2);

			ImageIcon icon = new ImageIcon(canvas.setSampleIcon());
			label.setIcon(icon);
		}
	}

	// 透明度スピナー用
	static class AlphaSpinnerListener implements ChangeListener {
		PaintCanvas canvas;
		JSlider slider;
		JLabel label;
		public AlphaSpinnerListener(PaintCanvas canvas, JSlider slider, JLabel sizeLabel) {
			super();
			this.canvas = canvas;
			this.slider = slider;
			this.label = sizeLabel;
		}
		public void stateChanged(ChangeEvent e) {
			JSpinner source2 = (JSpinner) e.getSource();
			int fps2 = (int) source2.getValue();
			slider.setValue(fps2);
			canvas.setAlpha(fps2);
			ImageIcon icon = new ImageIcon(canvas.setSampleIcon());
			label.setIcon(icon);
		}
	}

	// 2019.10.08 K. Misue
	// カラーチューザ用
	static class ColorListener implements ChangeListener {

		final PaintCanvas canvas;
		final JColorChooser colorChooser;

		public ColorListener(PaintCanvas canvas, JColorChooser colorChooser) {
			super();
			this.canvas = canvas;
			this.colorChooser = colorChooser;
		}

		public void stateChanged(ChangeEvent e) {
			Color color = colorChooser.getColor();
			canvas.setColor(color);
		}
	}
	
	static File file4=new File("making_data/data/event4.txt");//イベントファイル（メモ）
	
	//commentを保存するためのヤツ
		public static void logSave(String log) {
			FileWriter filewriter;
			try {
				filewriter = new FileWriter(file4, true);
				PrintWriter pw = new PrintWriter(new BufferedWriter(filewriter));
				pw.println(log);
				pw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		}
}