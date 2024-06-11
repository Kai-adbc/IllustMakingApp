package viewApp;  //statusManagerは進捗管理という意味
//このクラスは、イベントやキャンバスの表示・操作を管理するためのものであり、
//グラフィックス関連の要素を使用して描画や画像処理を行います。
//また、Swingコンポーネントを利用してGUIの表示や操作を行います。

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class StatusManager{
	Rectangle rect = null; //オブジェクトの変数を定義
	BufferedImage example=null;
	int w;//eventViewのサイズ
	int eventtt=0;
	int evenum=0,groupnum=0,viewType=0;//選択中のグループ番号
	String folderName,penData=null;
	File file1=null,file2=null,file4=null;
	//eventの情報を格納するための配列を作っている。
	ArrayList<Integer> event = new ArrayList<Integer>();//EventCanvasクラスにてイベント番号を格納
	ArrayList<ArrayList<Integer>> event2 = new ArrayList<ArrayList<Integer>>();//event2には、各eventの明度や彩度といった情報が入っている。
	ArrayList<Integer> event3 = new ArrayList<Integer>();//イベントグループ最後の要素を入れている。
	ArrayList<Integer> x = new ArrayList<Integer>();
	ArrayList<Integer> y = new ArrayList<Integer>();
	// 解説マークの位置と色を関連付けるためのマップ
	private Map<Point, Color> explanationColors = new HashMap<>();
	static int cw=600,ch=600;//canvass
	EventCanvas eventCanvas;
	EnlargedCanvas enlargedCanvas;
	MemoCanvas memoCanvas;
    ImagePanel imagePanel;
	JLabel hueLabel,satuLabel,brightLabel,alphaLabel,scrolllabel,widthLabel=new JLabel(),penlabel=new JLabel();
	JScrollPane s;
	JScrollBar bar;
	JFrame frame2;
	int back=255;
	int hoso,futo;
	int change,change2,width,alpha,type,spoitColor;
	float hue,satu,bright;
	ImageIcon penIcon= new ImageIcon(),EventIcon=new ImageIcon();
	private Graphics2D g2d;
	BufferedImage img=new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB),cImage;
	
	public boolean isCircleVisible = false; // デフォルトでは丸を表示しない
	ArrayList<Integer> memo = new ArrayList<Integer>();//EventCanvasクラスにてイベント番号を格納


	//コンストラクターは上に
	//コンストラクタはメンバ変数に初期値を代入する役割
	public StatusManager() {
		g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 50, 50);
		penIcon.setImage(img);
		penlabel.setIcon(penIcon);
	}

	public void setFolderName(String folderName){
		this.folderName = folderName;
	}

	public void setEventCanvas(EventCanvas eventCanvas){
		this.eventCanvas = eventCanvas;
	}

	public void setEnlargedCanvas(EnlargedCanvas canvas){
		this.enlargedCanvas = canvas;
	}
	
	public void setMemoCanvas(MemoCanvas memocanvas){
		this.memoCanvas = memocanvas;
	}
	
	public void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}
	
	private Color circleColor = Color.BLUE;  // デフォルトの色は青です

	public Color getCircleColor() {
	    return circleColor;
	}

	public void setCircleColor(Color circleColor) {
	    this.circleColor = circleColor;
	}

	//イベントグループ最後の要素を入れた配列　event3
	public void setEvent3(){
		int i = 0;
		for(; i<event.size(); i++){
			if(event2.get(i).get(1)==1){
				event3.add(i);
			}
		}
	}

	//これらのメソッドを使用することで、StatusManagerクラスのインスタンスからrectオブジェクトの値を取得したり、
	//他のオブジェクトからrectの値を設定したりすることができます
	public void setRect(Rectangle rect){
		this.rect = rect;
	}
	public int getX(){
		return rect.getX();
	}
	public int getY(){
		return rect.getY();
	}
	public int getW(){
		return rect.getW();
	}
	public int getH(){
		return rect.getH();
	}

	public void setLabel(JLabel hue, JLabel satu, JLabel bright,JLabel alpha, JLabel penlabel, JLabel width){
		this.hueLabel = hue;
		this.satuLabel = satu;
		this.brightLabel = bright;
		this.alphaLabel = alpha;
		this.penlabel = penlabel;
		this.widthLabel = width;
		
	}

	public void setpenIcon(ImageIcon icon){
		this.penIcon = icon;
	}

	//このメソッドを使用することで、StatusManagerクラスのインスタンスにスクロールパネル、フレーム、およびラベルの情報を設定することができます。
	public void setScrollPane(JScrollPane scrollPane, JFrame frame2,JLabel scrolllabel) {
		this.frame2 = frame2;
		this.s = scrollPane;
		this.scrolllabel = scrolllabel;
		bar = s.getHorizontalScrollBar();
	}

	public void setEventIcon() {
		ImageIcon icon = new ImageIcon(cImage);
		scrolllabel.setIcon(icon);
	}

	//penLabelは拡大図のアイコンを表示するのに使われる。
	public void setIcon2(){
		g2d.setColor(Color.getHSBColor(hue/100, satu/100,bright/100));
		g2d.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0F)
				);
		g2d.fillOval(5, 5, 40, 40);
		penIcon=new ImageIcon(img);
		penlabel.setIcon(penIcon);
	}


	//hutoとhosoを読み取っている。
	public void readFutoHoso(File file3){
		File file = null;
		file = file3;
		String line=null;
		try(BufferedReader in = new BufferedReader(new FileReader(file))){
			line = in.readLine();
			futo = Integer.parseInt(line);
			line = in.readLine();
			hoso = Integer.parseInt(line);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//選択されたイベントの情報を取得
	//拡大図での表示に用いられる。
	public void getData(){
		ArrayList<Integer> array = new ArrayList<Integer>();
		array = event2.get(evenum);
		change = array.get(0);
		change2 = array.get(1);
		hue = array.get(2);//get(2)はイベントグループ最後の要素を入れた配列であるかどうかを区別する。
		satu = array.get(3);
		bright = array.get(4);//明度
		alpha = array.get(5);//不透明度
		width = array.get(6);//太さ

		hueLabel.setText("色相:"+(int)(360*0.01*this.hue));
		satuLabel.setText(" 彩度:"+(int)this.satu);
		brightLabel.setText("明度:"+(int)this.bright);
		alphaLabel.setText(" 不透明度:"+(int)this.alpha*100/255);
		widthLabel.setText("太さ:"+(int)this.width);
		setIcon2();
	}

	public void setSize(int w,int h){
		rect.setSize(w,h);  //rectangleクラスのsetSizeを実行している
	}

	public void setBack(int back){
		this.back = back;  //thisを使ってメンバ変数を指定している。
	}

	//このメソッドは与えられた座標がイベントの領域内にあるかどうかを判定し、該当するイベントの情報を更新します。
	public void clickEvent(int pointx, int pointy) {
		int i = 0;
		for(; i<event.size(); i++){
			if(x.get(i) <= pointx  &&  pointx < x.get(i)+eventCanvas.C+2){
				if(y.get(i) <= pointy  &&  pointy < y.get(i)+eventCanvas.C+2){
					changeEvenum(i); //現在のイベント番号（現在地）を変更
					eventCanvas.drawRect();
					setEventIcon();
					break;
				}
			}
		}
	}

	//これらのメソッドは、イベントの位置が画面外にある場合にスクロールバーを調整するために使用されます。
	public void setaddBar() {
		bar.setValue(x.get(evenum)-300);
	}
	public void setremBar() {
		bar.setValue(x.get(evenum)-310);
	}

	//prevのイベントが何番目のグループで、何番目のイベントなのかを表示している。
	public void addEvenum(){
		viewType = 0;
		if(evenum < event2.size()-1){
			System.out.println(event2.size());
			evenum++;
			if(event2.get(evenum).get(0)==1) {
				groupnum++;
			}
			getData();
			eventCanvas.drawRect();
			enlargedCanvas.showView();
			memoCanvas.display();
		}
		System.out.println("グループ:"+groupnum);
		System.out.println("イベント:"+evenum);
		System.out.println("ストローク:"+event.get(evenum));
		setEventIcon();
		setaddBar();
	}
	
	


	public void addEvenum2(){
		viewType = 1;
		int i=evenum+1;
		int j = evenum;
		for(; i<event.size(); i++){
			if(event2.get(i).get(1)==1){
				evenum = i;
				if(event2.get(j).get(1)==1){//既にグループの末尾の時だけグループ移動
					groupnum++;
				}
				getData();
				eventCanvas.drawRect2();  //グラフの指定範囲を示す四角形を更新
				enlargedCanvas.showView2();  //拡大図の画像を更新
				memoCanvas.display();
				break;
			}
		}

		System.out.println("------");
		System.out.println("グループ:"+groupnum);
		System.out.println("イベント:"+evenum);
		System.out.println("ストローク:"+event.get(evenum));
		setEventIcon();
		setaddBar();
	}

	//addEvenumの逆？
	public void removeEvenum(){
		viewType = 0;
		if(0 < evenum){
			evenum--;
			if(event2.get(evenum).get(1)==1)
				groupnum--;
			getData();
			eventCanvas.drawRect();
			enlargedCanvas.showView();
			memoCanvas.display();
		}
		System.out.println("------");
		System.out.println("グループ:"+groupnum);
		System.out.println("イベント:"+evenum);
		System.out.println("ストローク:"+event.get(evenum));
		setEventIcon();
		setremBar();
	}

	public void removeEvenum2(){
		viewType = 1;
		int i=evenum-1;
		for(; 0<=i; i--){
			if(event2.get(i).get(1)==1){
				evenum = i;
				groupnum--;
				getData();
				eventCanvas.drawRect2();
				enlargedCanvas.showView2();
				memoCanvas.display();
				break;
			}
		}
		System.out.println("------");
		System.out.println("グループ:"+groupnum);
		System.out.println("イベント:"+evenum);
		System.out.println("ストローク:"+event.get(evenum));
		setEventIcon();
		setremBar();
	}

	public void changeEvenum(int i){
		viewType = 0;
		int j=0;
		int a=0;
		evenum = i;
		getData();
		for(; j<=evenum; j++){
			if(event2.get(j).get(0)==1){
				a++;
			}
		}
		groupnum = a-1;
		enlargedCanvas.showView();
		memoCanvas.display();
		System.out.println("------");
		System.out.println("グループ:"+groupnum);
		System.out.println("イベント:"+evenum);
		System.out.println("ストローク:"+event.get(evenum));
	}

	public void iniEvenum(){
		evenum = 0;
		groupnum = 0;
	}

	//お手本があれば四角つくる
	public int iniRect(int x, int y){
		if(example!=null){
			rect = null;
			rect = new Rectangle(x,y,0,0);//新しいRectangleオブジェクトを作成し、座標(x, y)、幅0、高さ0で初期化
			return 0;
		}else{
			return 1;
		}
	}

	public void iniEvent(){
		if(event2!=null){
			event2.clear();
			event.clear();
			event3.clear();
		}
	}

	public void delRect(){
		if(rect!=null){
			if(ifRect()==1 ){
				rect = null;
			}
		}
	}

	//長方形の指定範囲がはみ出していたり、比率が異常、小さすぎる場合1を返す。
	public int ifRect(){
		if((rect.getW()<5 || rect.getH()<5) || ((rect.getX()+rect.getW())>cw || ((rect.getY()+rect.getH())>ch)) ){
			return 1;
		}
		if((rect.getW()/rect.getH()) < 1/4 || 4 < rect.getW()/rect.getH()){//□の縦横比率が３以上でもダメ
			return 1;
		}
		return 0;
	}

	//イベントキャンバスの方のdrawRect
	public void drawEventRect(){
		eventCanvas.drawRect();
	}
	
	//MemoCanvasでevent番号を
	public ArrayList<Integer> getEvent() {
	    return event;
	}
	
	// 解説マークの色を取得または設定するメソッド
    public Color getExplanationColor(Point point) {
        return explanationColors.get(point);
    }
    
    public void setExplanationColor(Point point, Color color) {
        explanationColors.put(point, color);
    }
}