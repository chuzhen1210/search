package test.lucene.client;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 主界面
 * @author chuzhen
 *
 * 2017年6月4日
 */
public class MainUI {

	public MainUI() {
		super();
		
		JFrame frame = new JFrame("文件搜索");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 500);
		
        JPanel panel = new JPanel();    
        frame.add(panel);

		creteElement(panel);
		
        frame.setVisible(true);
	}

	private void creteElement(JPanel panel) {
		GridLayout layout = new GridLayout();
		layout.setColumns(3);
		panel.setLayout(layout);
		
		JLabel label = new JLabel("关键字：");
		label.setVisible(true);
		label.setSize(80, 30);
		panel.add(label);
		
		JTextField textField = new javax.swing.JTextField();
		textField.setVisible(true);
		textField.setSize(200, 40);
		panel.add(textField);
		
		JButton button = new JButton();
		button.setText("搜索");
		button.setVisible(true);
		panel.add(button);
	}

}
