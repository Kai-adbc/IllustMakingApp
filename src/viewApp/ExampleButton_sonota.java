package viewApp; //イラスト選択のボタン

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class ExampleButton_sonota extends JButton{
	StatusManager statusManager;
	ExampleView exampleView;

	public ExampleButton_sonota(StatusManager statusManager, ExampleView exampleView) {
		super("イラストを選択");
		addActionListener(new ExampleListener());
		this.statusManager = statusManager;
		this.exampleView = exampleView;
	}

	//完成図を読み込む
	public void readExample() {
		Image image=null;
		JFileChooser filechooser = new JFileChooser("making_data/data");
		filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int selected = filechooser.showOpenDialog(this);

		if (selected == JFileChooser.APPROVE_OPTION){
			File folder = filechooser.getSelectedFile();
			statusManager.setFolderName(folder.getAbsolutePath());
			String filename = folder.getAbsolutePath()+"/sample.jpg";  //sample.jpgのファイルをファイルを読み込んでimage
			System.out.println(filename);
			try {
				image = ImageIO.read(new File(filename));
				//赤枠も消す
				statusManager.rect = null;
				statusManager.example = (BufferedImage) image;
			} catch (IOException e) {
				e.printStackTrace();
			}
			//event1.txt、event2.txt、event3.txtのファイルパスを作成し、statusManagerの対応するファイルフィールドに設定します。
			filename = folder.getAbsolutePath()+"/event1.txt";
			statusManager.file1 = new File(filename);
			filename = folder.getAbsolutePath()+"/event2.txt";
			statusManager.file2 = new File(filename);
			filename = folder.getAbsolutePath()+"/event3.txt";
			File file3 = new File(filename);
			statusManager.readFutoHoso(file3);
			filename =folder.getAbsolutePath()+"/event4.txt";//event4.txtの読み込み
			statusManager.file4=new File(filename);
			System.out.println(statusManager.file4.getAbsolutePath());
			exampleView.rePaint();
		}
	}
	
	//「イラスト選択」ボタンがクリックされた時に関連するデータを読み込む処理を担っている
	class ExampleListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			readExample();
		}
	}
}


