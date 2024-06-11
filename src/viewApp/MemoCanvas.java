package viewApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MemoCanvas {
    StatusManager statusManager;
    JTextPane textPane = new JTextPane();
   

    public MemoCanvas(StatusManager statusManager) {
        this.statusManager = statusManager;
    }

    public void MemoWindow(JFrame frame4) {
        textPane.setEditable(false);
        textPane.setContentType("text/html");

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(200, 130)); // ここでサイズを設定
        // パネルを使用せず、直接スクロールペインをフレームに追加
        frame4.getContentPane().add(scrollPane, BorderLayout.SOUTH);
    }

    public void display() {
    	int n = statusManager.evenum;
    	int te= statusManager.event.get(n);
        ArrayList<Integer> event = statusManager.getEvent();
        File filePath = statusManager.file4; // テキストファイルのパスを取得
        setMemo(); // メモエリアをクリアする処理
       	int ex=-1;
       	int x=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue; // 空行の場合はスキップ
                }
                line = line.replace("[", "").replace("]", ""); // [, ]を取り除く

                ArrayList<String> array4 = new ArrayList<>(Arrays.asList(line.split(", ", 0)));
                if (!array4.isEmpty()) {
                    ex=x;
                    x=Integer.parseInt(array4.get(0));
                    for (int i = 0; i < event.size(); i++) {
                        int e = event.get(i);
                        if (e == x) {
                        	if (te-1 == x) {
                        		builder.append("左：");
                        		 builder.append("<br>");
                        		builder.append(array4.get(1));
                        		 // HTMLの改行タグを使用して改行を追加
                                builder.append("<br><br>");
                           
                        	}
                        	else if (te == x) {
                        		builder.append("真ん中：");
                        		builder.append("<br>");
                            	builder.append(array4.get(1));
                                // Unicodeの四角形文字を使用
                               // builder.append("<span style='color:red; font-size: 20px;'>&#x25A0;</span> ");
                                // HTMLの改行タグを使用して改行を追加
                                builder.append("<br><br>");
                            }
                            else if (te+1==x) {
                            	builder.append("右：");
                        		builder.append("<br>");
                            	builder.append(array4.get(1));
                            	 // HTMLの改行タグを使用して改行を追加
                                builder.append("<br><br>");
                            		
//                            		builder.append("<span style='color:red; font-size: 20px;'>&#x25A0;</span> ");
//                            		builder.append("<br>");
          
                            }
                            
                           
                        }

                    }
                }
            }
            textPane.setText(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setMemo() {
        textPane.setText("");
    }
}
