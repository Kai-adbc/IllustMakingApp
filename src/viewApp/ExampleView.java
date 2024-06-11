package viewApp;  //完成図

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExampleView extends Canvas implements MouseListener, MouseMotionListener,KeyListener{
	//public class ExampleView extends JPanel implements MouseListener, MouseMotionListener,KeyListener{
	StatusManager statusManager;
	private List<Point> matchedCoordinates = new ArrayList<>();

	// 追加するフィールド
	private String balloonText = null; // 吹き出しの中に表示するテキスト
	private Point balloonLocation = null; // 吹き出しの位置
	float bairitsu2,hue2,satu2,bright2;//倍率、色相、彩度、明度
	int width2;

	public ExampleView(StatusManager statusManager) {
		this.statusManager = statusManager;

		// MouseListener・MouseMotionListenerを設定
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		// キャンバスの背景を白に設定
		setBackground(Color.WHITE);
		// 描画
		repaint();
	}

	public void paint(Graphics g){
		if(statusManager.example != null){
			g.drawImage(statusManager.example, 0, 0, null);
		}
		//		super.paint(g);
		if (statusManager.rect!=null) {
			statusManager.rect.draw(g);
		}
		paintStrokeRect(g); // この行を追加して、円を描画するためのメソッドを呼び出します。
		//paintBalloon(g); // 吹き出しを描画するための呼び出し
	}

	// 画像から特定の領域の平均色を計算するメソッド
	public Color calculateAverageColor(BufferedImage img, int x, int y, int width, int height) {
	    long sumr = 0, sumg = 0, sumb = 0;
	    int count = 0;

	    for (int i = x; i < x + width; i++) {
	        for (int j = y; j < y + height; j++) {
	            // 範囲チェック
	            if (i < 0 || i >= img.getWidth() || j < 0 || j >= img.getHeight()) {
	                continue; // 範囲外の座標は無視する
	            }

	            Color pixel = new Color(img.getRGB(i, j));
	            sumr += pixel.getRed();
	            sumg += pixel.getGreen();
	            sumb += pixel.getBlue();
	            count++;
	        }
	    }
	    
	    // 有効なピクセルがない場合の対応
	    if (count == 0) {
	        return new Color(0, 0, 0); // もしくは適切なデフォルト値またはエラー処理
	    }

	    // 平均を計算
	    int avgr = (int)sumr / count;
	    int avgg = (int)sumg / count;
	    int avgb = (int)sumb / count;

	    return new Color(avgr, avgg, avgb);
	}


	// コントラストの高い色を決定するメソッド
	public Color getContrastColor(Color color) {
		// YIQ色空間に基づいたコントラスト計算
		int d = (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000 >= 128 ? 0 : 255;
		return new Color(d, d, d);
	}


	// フラグがtrueのときのみ円を描画
	//メモがあるストロークのみマークを追加
	public void paintStrokeRect(Graphics g) {
		if (statusManager.isCircleVisible) {
			BufferedImage viewImage = statusManager.example;// ここで ExampleView の画像を取得するコードを書く必要があります
			try (BufferedReader out = new BufferedReader(new FileReader(statusManager.file4))) {
				String line2, num, memo;
				int num2;
				while ((line2 = out.readLine()) != null) {
					line2 = line2.replace("[", "");
					line2 = line2.replace("]", "");
					List<String> array2 = Arrays.asList(line2.split(",", 0));
					num = array2.get(0);
					num2 = Integer.parseInt(num);
					memo = array2.get(1);

					try (BufferedReader in = new BufferedReader(new FileReader(statusManager.file2))) {
						String line, test, test2, num3;
						int j = 1, x = 0, y = 0, z;

						while ((line = in.readLine()) != null) {
							line = line.replace("[", "");
							line = line.replace("]", "");
							List<String> array = Arrays.asList(line.split(", ", 0));
							test = array.get(j);
							x = Integer.parseInt(test);
							test2 = array.get(j + 1);
							y = Integer.parseInt(test2);
							num3 = array.get(j - 1);
							z = Integer.parseInt(num3);

							if (num2 == z) {
								int rectSize = 20;

								try(BufferedReader in3 = new BufferedReader(new FileReader(statusManager.file1))){
									String line3=null;
									int u;


									while ((line3 = in3.readLine()) != null) {
										line3 = line3.replace("[", "");
										line3 = line3.replace("]", "");//"[" と "]" を取り除く
										List<String> array3 = Arrays.asList(line3.split(", ", 0));//データの",　"ごとに値を格納
										u=Integer.parseInt(array3.get(0));
										hue2 = Integer.parseInt(array3.get(1));  //色相
										width2 = Integer.parseInt(array3.get(5)); //ペンの太さ

										if (num2==u) {
											if (width2<6) {
												rectSize=10;
											}else {
												rectSize=width2*2;
											}
											int rectX = x - rectSize / 2;
											int rectY = y - rectSize / 2;

											// 枠線の色を適応させる
											Point point = new Point(x, y);
											Color color = statusManager.getExplanationColor(point);
											if (color == null) {
												// 色がまだ計算されていない場合、計算して保存

												Color avgColor = calculateAverageColor(viewImage, rectX, rectY, rectSize, rectSize);
												color = getContrastColor(avgColor);
												statusManager.setExplanationColor(point, color);
											}

											g.setColor(color); // 枠線の色をコントラスト色に設定
											g.drawRect(rectX, rectY, rectSize, rectSize); // 枠線を描画
										}
									}
								} catch (FileNotFoundException e) {
									e.printStackTrace();
									System.exit(-1);
								} catch (IOException e) {
									e.printStackTrace();
									System.exit(-1);
								}
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						System.exit(-1);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}


	//	// 吹き出しを表示するためのメソッド
	//	private void showBalloon(String text, int x, int y) {
	//		this.balloonText = text;
	//		this.balloonLocation = new Point(x, y);
	//		repaint();
	//	}


	// 吹き出しを描画するためのメソッド

	//	private void paintBalloon(Graphics g) {
	//		if (statusManager.isCircleVisible) {
	//			if (balloonText != null && balloonLocation != null) {
	//				Graphics2D g2d = (Graphics2D) g;
	//				FontMetrics fm = g2d.getFontMetrics();
	//
	//				// テキストの幅を計算
	//				int textWidth = fm.stringWidth(balloonText);
	//
	//				// 吹き出しの最大幅を設定
	//				final int maxBalloonWidth = 150;
	//
	//				List<String> lines = new ArrayList<>();
	//				if (textWidth <= maxBalloonWidth) {
	//					lines.add(balloonText);
	//				} else {
	//					String[] words = balloonText.split("");
	//					StringBuilder line = new StringBuilder();
	//					int lineWidth = 0;
	//					for (String word : words) {
	//						int wordWidth = fm.stringWidth(word);
	//						if (lineWidth + wordWidth <= maxBalloonWidth) {
	//							line.append(word);
	//							lineWidth += wordWidth;
	//						} else {
	//							lines.add(line.toString());
	//							line = new StringBuilder(word);
	//							lineWidth = wordWidth;
	//						}
	//					}
	//					if (line.length() > 0) {
	//						lines.add(line.toString());
	//					}
	//				}
	//
	//				int lineHeight = fm.getHeight();
	//				int balloonHeight = lineHeight * lines.size() + 10;  // 上下に5pxずつの余白を追加
	//
	//				// 吹き出しを描画
	//				g.setColor(Color.WHITE);
	//				g.fillRoundRect(balloonLocation.x, balloonLocation.y - balloonHeight, maxBalloonWidth, balloonHeight, 10, 10);
	//				g.setColor(Color.BLACK);
	//				g.drawRoundRect(balloonLocation.x, balloonLocation.y - balloonHeight, maxBalloonWidth, balloonHeight, 10, 10);
	//
	//				// テキストを描画
	//				int y = balloonLocation.y - balloonHeight + fm.getAscent() + 5;  // 5pxの余白を追加してテキストのベースラインを調整
	//				for (String line : lines) {
	//					g.drawString(line, balloonLocation.x + 5, y);
	//					y += lineHeight;
	//				}
	//			}
	//		}
	//	}


	public void rePaint(){
		repaint();
	}

	//クラスのメンバ変数として前回の描画サイズを記憶するための変数を追加
	private int lastWidth = -1;
	private int lastHeight = -1;
	//マウスがドラッグされるたびに長方形のサイズが変更され、描画が更新される。
	public void mouseDragged(MouseEvent e) {
		if(statusManager.rect!=null){
			  // 色を変更する例
			int newWidth = e.getX()-statusManager.getX();
			int newHeight = e.getY()-statusManager.getY();
			if (newWidth != lastWidth || newHeight != lastHeight) {
				lastWidth = newWidth;
				lastHeight = newHeight;
				statusManager.setSize(newWidth, newHeight);
				repaint();
			}
		}
	}

	//長方形が選択されているかどうか判定する。
	public boolean isRect(){
		if(statusManager.rect == null){
			return false;
		}else{
			return true;
		}
	}



	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//	int mouseX = e.getX();
		//	int mouseY = e.getY();
		//
		//	try (BufferedReader out = new BufferedReader(new FileReader(statusManager.file4))) {
		//		String line2, num, memo;
		//		int num2;
		//		while ((line2 = out.readLine()) != null) {
		//			line2 = line2.replace("[", "");
		//			line2 = line2.replace("]", "");
		//			List<String> array2 = Arrays.asList(line2.split(",", 0));
		//			num = array2.get(0);
		//			num2 = Integer.parseInt(num);
		//			memo = array2.get(1);
		//
		//			try (BufferedReader in = new BufferedReader(new FileReader(statusManager.file2))) {
		//				String line, test, test2, num3;
		//				int j = 1, x = 0, y = 0, z;
		//
		//				while ((line = in.readLine()) != null) {
		//					line = line.replace("[", "");
		//					line = line.replace("]", "");
		//					List<String> array = Arrays.asList(line.split(", ", 0));
		//					test = array.get(j);
		//					x = Integer.parseInt(test);
		//					test2 = array.get(j + 1);
		//					y = Integer.parseInt(test2);
		//					num3 = array.get(j - 1);
		//					z = Integer.parseInt(num3);
		//
		//					if (num2 == z) {
		//						if (Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2) <= Math.pow(14, 2)) {
		//							showBalloon(memo, mouseX, mouseY);
		//							break;
		//						}
		//					}
		//				}
		//			}
		//		}
		//	} catch (Exception ex) {
		//		ex.printStackTrace();
		//	}
	}


	//マウスが押された時に、長方形を初期化している。
	@Override
	public void mousePressed(MouseEvent e) {
		if(statusManager.iniRect(e.getX(),e.getY())==0){
			statusManager.iniEvent();
			//repaint();//四角が描けたら再描画
		}
	}


	//マウスのボタンが離されたときに、矩形の削除、イベントの初期化、ストロークの探索、データの取得などの処理を行います。
	//また、statusManager.eventとstatusManager.event3の内容を出力しています。
	@Override
	public void mouseReleased(MouseEvent e) {
		if(statusManager.rect!=null){
			statusManager.delRect();
			//repaint();
		}
		if(statusManager.rect!= null){
			statusManager.iniEvenum();
			statusManager.eventCanvas.seekStroke();
			statusManager.enlargedCanvas.setXYWH();
			statusManager.memoCanvas.display();//ここを変え
			statusManager.getData();
			statusManager.setEventIcon();
		    //statusManager.imagePanel.setXYWH();
			System.out.println(statusManager.event);
			System.out.println(statusManager.event3);
		}
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	//キーボードのキーが押されたときに、対応する処理が実行されます。それぞれのキーに対して、イベントの追加や削除が行われます。
	@Override
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}