package viewApp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Rectangle{
	private int x, y, w, h;
	private Color color; // 色を保持するためのフィールド

	public Rectangle(int xpt, int ypt){
		super();
		setLocation( xpt, ypt );
	}

	public Rectangle(int xpt, int ypt, int width, int height) {
        super();
        setLocation(xpt, ypt);
        setW(width);
        setH(height);
        //this.color = color; // 色を設定
    }
	
	// ゲッターとセッターメソッド（省略可能）
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() { 
		return w; 
	}
	public void setW(int w) { 
		this.w = w; 
	}

	public int getH() { 
		return h; 
	}
	public void setH(int h) { 
		this.h= h;
	}

	public void draw(Graphics g ) {
		int x = getX();
		int y = getY();
		int w = getW();
		int h = getH();

		if(w<0) {
			x += w;
			w *= -1;
		}
		if(h<0) {
			y += h;
			h *= -1;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));//線のふとさ
		g2.setColor(Color.black);
		g2.drawRect(x, y, w, h);
	}

	public BufferedImage draw( BufferedImage Image, int Type) {//nowなら0赤、prev1青、next2緑
		int type = Type;
		BufferedImage img = Image;
		int x = getX();
		int y = getY();
		int w = getW();
		int h = getH();

		Graphics2D g2 = (Graphics2D) img.getGraphics();

		switch(type) {
		case 0: // 今（現在）
			g2.setStroke(new BasicStroke(2));//線のふとさ
			g2.setColor(Color.RED); // 鮮やかな赤
			break;
		case 1: // 前（過去）
			g2.setStroke(new BasicStroke(1.7F));//線のふとさ
			g2.setColor(new Color(255,105,180)); // 中間のピンク（ホットピンクに近い）
			break;
		case 2: // 次（未来）
			g2.setStroke(new BasicStroke(1.6F));//線のふとさ
			g2.setColor(new Color(186, 85, 85)); // ソフトなブリックレッド
			break;
		case 3: // これは以前のコードに含まれていたので、変更せずに保持
			g2.setStroke(new BasicStroke(1F));//線のふとさ
			g2.setColor(Color.BLACK); 
			break;
		}




		g2.drawRect(x, y, w, h);

		return img;
	}
	
	 
}