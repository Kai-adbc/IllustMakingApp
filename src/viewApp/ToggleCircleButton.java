package viewApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ToggleCircleButton extends JButton {
    StatusManager statusManager;
    ExampleView exampleView;

    public ToggleCircleButton(StatusManager statusManager, ExampleView exampleView) {
        super("解説マークを表示/非表示");
        addActionListener(new CircleToggleListener());
        this.statusManager = statusManager;
        this.exampleView = exampleView;
    }

    class CircleToggleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // フラグをトグル
            statusManager.isCircleVisible = !statusManager.isCircleVisible;
            exampleView.repaint(); // 描画を更新
        }
    }
}
