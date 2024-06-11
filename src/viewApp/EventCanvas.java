package viewApp;//グラフについてのクラス

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCanvas{
	StatusManager statusManager;
	int w=6000,h=164; //650
	int change,width,alpha,spoitColor;
	int A=25,B=15,C=12,D=7;//A=イベントグループ間の距離　B=イベントグループ内行間　C=最大直径　D = 最小直径
	int Y = h*6/7; //yの初期値
	int x=0,y=Y;
	int i=0;
	float bairitsu,hue,satu,bright;//倍率、色相、彩度、明度
	private BufferedImage cImage = null,trimmedImage = null,subImage=null;
	private Graphics2D g2d;
	int cw=w;
	Rectangle rect=null,prevrect=null,nextrect=null;

	public EventCanvas(StatusManager statusManager){
		this.statusManager = statusManager;
		cImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D) cImage.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, w, h);
	}

	//x,y,iの初期化
	public void setValue(){
		x = 0;
		iniY();
		i = 0;
	}
	public void iniY(){
		y = Y;
	}

	//グラフを描画するときに、各ストロークのIconの倍率を設定している？
	//futoとhosoが同じだった場合、グラフが上手く表示されないので、修正した。
	public void setBairitsu(){
		if(statusManager.futo-statusManager.hoso>0) {
			bairitsu =((float)(C-D)/(float)(statusManager.futo-statusManager.hoso));
		}
		else {
			bairitsu=((float)(C-D)/(float)2);
		}
	}

	//背景の色を変えた時の背景色の描画を指示している。
	public void paint(){
		trimmedImage=null;
		if(cw<1460){
			trimmedImage = cImage.getSubimage(0, 0, 1460,h);
		}
		else{
			trimmedImage = cImage.getSubimage(0, 0, cw,h);
		}
	}

	//バッファイメージにグラフ描画
	public void drawGraph(int j){
		if(change == 0){
			if(i%10 == 0){
				iniY();
				x = x+B;
			}else{
				y = y - C - 2 ;//下の円から C+2 px上げる
			}
		}else if(change==1){
			iniY();
			x = x+A;
			i = 0;
		}
		g2d.setColor(Color.getHSBColor(hue/100, satu/100,bright/100)); //alphaなくした
		int chockei = (int)(D+(width-statusManager.hoso)*bairitsu);//最太W,最細ｗを記録
		g2d.fillOval( x+(C-chockei)/2, y+(C-chockei)/2, chockei, chockei );
		cw = x+(C-chockei)/2+A;
		statusManager.x.add(j, x);
		statusManager.y.add(j, y);
		i++;
	}

	//背景の色を変えた時のグラフ描画をになっている。
	public void drawGraph() {
		int j;
		int hue,satu,bright,alpha,width;
		for(j=0; j<statusManager.event.size(); j++) {
			hue=statusManager.event2.get(j).get(2);
			satu=statusManager.event2.get(j).get(3);
			bright=statusManager.event2.get(j).get(4);
			width=statusManager.event2.get(j).get(6);
			if(statusManager.event2.get(j).get(0) == 0){
				if(i%10 == 0){
					iniY();
					x = x+B;
				}else{
					y = y - C - 2 ;//下の円から C+2 px上げる
				}
			}else{
				iniY();
				x = x+A;
				i = 0;
			}
			g2d.setColor(Color.getHSBColor((float)hue/100, (float)satu/100,(float)bright/100));//alphaなくした
			int chockei = (int)(D+(width-statusManager.hoso)*bairitsu);//最太W,最細ｗを記録
			g2d.fillOval( x+(C-chockei)/2, y+(C-chockei)/2, chockei, chockei );
			cw = x+(C-chockei)/2+A;
			i++;
		}
		hue=statusManager.event2.get(0).get(2);
		satu=statusManager.event2.get(0).get(3);
		bright=statusManager.event2.get(0).get(4);
		width=statusManager.event2.get(0).get(6);
		BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.getHSBColor((float)hue/100, (float)satu/100,(float)bright/100));
		g.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0F)
				);
		g.fillOval(0,0,100,100);
	}

	public void delRect() {
		rect = null;
		prevrect = null;
		nextrect = null;
	}

	//□
	public void drawRect(){
		int i = statusManager.evenum;
		BufferedImage Image = new BufferedImage(trimmedImage.getWidth(),trimmedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = Image.createGraphics();
		g2d.drawImage(trimmedImage,0,0,null);

		//角四角形描画
		//グラフの中で、今どこを表示しているのかを示す四角形
		delRect();
		rect = new Rectangle(statusManager.x.get(i),statusManager.y.get(i),C,C);
		subImage = rect.draw(Image,0);
		if(0 < i){
			prevrect = new Rectangle(statusManager.x.get(i-1),statusManager.y.get(i-1),C,C);
			subImage = prevrect.draw(subImage,1);
		}
		if((i<statusManager.event.size()-1)) {
			nextrect = new Rectangle(statusManager.x.get(i+1),statusManager.y.get(i+1),C,C);
			subImage = nextrect.draw(subImage,2);
		}
		setIcon();
	}

	public void drawRect2() {
		int i = statusManager.groupnum;
		int j = statusManager.evenum;
		int k;
		BufferedImage Image = new BufferedImage(trimmedImage.getWidth(),trimmedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = Image.createGraphics();
		g2d.drawImage(trimmedImage,0,0,null);

		//角四角形描画
		delRect();
		rect = new Rectangle(statusManager.x.get(j),statusManager.y.get(j),C,C);
		subImage = rect.draw(Image,0);

		if(0 < i){
			k=j-1;
			for(; 0<=k; k--){
				if(statusManager.event2.get(k).get(1)==1){
					prevrect = new Rectangle(statusManager.x.get(k),statusManager.y.get(k),C,C);
					subImage = prevrect.draw(subImage,1);
					break;
				}
			}
		}
		if(i<statusManager.event3.size()-1){
			k = j+1;
			for(; k<statusManager.event.size(); k++) {
				if(statusManager.event2.get(k).get(1)==1){
					nextrect = new Rectangle(statusManager.x.get(k),statusManager.y.get(k),C,C);
					subImage = nextrect.draw(subImage,2);
					break;
				}
			}
		}
		setIcon();
	}

	public void drawCircle() {
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

	            for (int eventNum=0; eventNum< statusManager.event.size();eventNum++) {
	            	int te=statusManager.event.get(eventNum);
	                if (te == num2) {
	                    System.out.println("Found matching number: " + num2);
	                    if (memo != null && !memo.isEmpty()) {
	                        int rectX = statusManager.x.get(eventNum);
	                        int rectY = statusManager.y.get(eventNum);
	                        g2d.setColor(Color.BLUE);
	                        float lineWidth = 1.5f; // 線の太さを5ピクセルに設定
	                        g2d.setStroke(new BasicStroke(lineWidth)); // 線の太さを設定
	                        g2d.drawRect(rectX, rectY, 14, 14); // 太い線で四角形を描画
	                        System.out.println("中川海出す " + num2);
	                        System.out.println("eventNumの値"+eventNum);
	                    }
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




	public void setIcon(){
		statusManager.cImage = subImage;
	}

	//該当ストローク情報取得
	public void getStroke(){
		try(BufferedReader in = new BufferedReader(new FileReader(statusManager.file1))){
			String line=null;
			int j = 0,bef,aft=-1;
			for(; j<statusManager.event.size() ; j++){//イベント数だけ繰り返す
				bef = aft;
				aft = statusManager.event.get(j);

				//該当ストロークまでファイル１を読む.readlineは１行づつ読み込むので、該当ストロークまで読み込むことで、移動している。
				for(int k=0; k<(aft - bef); k++){
					line = in.readLine();
				}

				line = line.replace("[", "");
				line = line.replace("]", "");//"[" と "]" を取り除く
				List<String> array = Arrays.asList(line.split(", ", 0));//データの",　"ごとに値を格納
				ArrayList<Integer> array2 = new ArrayList<Integer>();

				//前のストロークと色・透明度が同じならchange=0
				//checkcolorは色が変わったどうかを判定している。
				change  = checkColor(Integer.parseInt(array.get(1)),Integer.parseInt(array.get(2)),Integer.parseInt(array.get(3)));
				hue = Integer.parseInt(array.get(1));  //色相
				satu = Integer.parseInt(array.get(2)); //彩度
				bright = Integer.parseInt(array.get(3));  //明度
				alpha = Integer.parseInt(array.get(4));  //透明度
				width = Integer.parseInt(array.get(5)); //ペンの太さ
				spoitColor = Integer.parseInt(array.get(6)); //スポイト

				array2.clear();
				array2.add(change);//
				for(int l=1;l<7;l++){
					array2.add(Integer.parseInt(array.get(l)));
				}
				//この時点でarray2にはarrayの情報が入っている。
				statusManager.event2.add(j,array2);//event2にevent2配列のj番目のいちにarray2を格納
				if(change==1 && 0<j){
					statusManager.event2.get(j-1).add(1, 1);//ケツかどうか j-1にケツと入力
				}else if(0<j){
					statusManager.event2.get(j-1).add(1, 0);//ケツかどうか　j-1にケツではないと入力
				}
				//changeの値が1であり、かつjが0より大きい場合、statusManager.event2のj-1番目のリストに1を追加しています。
				//それ以外の場合は、statusManager.event2のj-1番目のリストに0を追加しています。
				drawGraph(j);
			}
			statusManager.event2.get(statusManager.event2.size()-1).add(1,1);//一番最後はケツ
			statusManager.setEvent3();
			setValue();
			paint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//該当ストロークを探す
	public void seekStroke() {
		clear();
		setBairitsu();

		try (BufferedReader in = new BufferedReader(new FileReader(statusManager.file2))){
			String line,test;
			int X = statusManager.getX(), Y = statusManager.getY(),H = statusManager.getH(),W = statusManager.getW();
			int i = 0,j=0,flag=0,x=0,y=0;//i...行数（ストローク番号）j...座標（偶数はx） flag...1はストローク発見

			while((line = in.readLine()) != null){
				flag = 0;
				line = line.replace("[", "");
				line = line.replace("]", "");//[, ]を取り除く
				List<String> array = Arrays.asList(line.split(", ", 0));//データの",　"ごとに値を格納
				for(j = 1; j < array.size() && flag == 0; j=j+2){//xをさがすので+2,一つ目の番号を飛ばすようにj=1
					test = array.get(j);
					x = Integer.parseInt(test);
					if(X<=x && x<=(X+W)){
						test = array.get(j+1);
						y = Integer.parseInt(test); //xが範囲内だったらyもチェック
						if(Y<=y && y<=(Y+H)){
							statusManager.event.add(i);//eventにイベント番号を格納.イベント番号は０から始まる。
							flag = 1;//フラグ１になったらforおわりで次のlineへ
						}
					}
				}
				i++;
			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
			System.exit(-1); // 0 以外は異常終了
		} catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		getStroke();
		drawCircle();
		drawRect();
		
	}

	public int checkColor(int h, int s, int b){
		if(hue==h && satu== s && bright == b){
			return 0;
		}
		else{
			return 1;
		}
	}

	//背景をスライダで設定されている明るさで塗る
	public void clear() {
		int back = statusManager.back;
		g2d.setColor(new Color(back,back,back));
		g2d.fillRect(0, 0, w, h);
		statusManager.cImage = null;
	}

	//背景を塗り、グラフ描画
	public void setBack(){
		clear();
		if(statusManager.rect != null){
			drawGraph();
			setValue();
			paint();
		}

		if(statusManager.viewType ==0) {
			drawCircle();
			drawRect();
		}else {
			drawCircle();
			drawRect2();
		}
	}
}
