package com.tarena.bird;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class World extends JPanel {

	Column column1;
	Column column2;
	Bird bird;
	Ground ground;
	BufferedImage background;
	BufferedImage gameoverImg;
	BufferedImage startImg;

	boolean start;
	int score;
	boolean gameOver;

	public World() throws IOException {
		background = ImageIO.read(getClass().getResource("bg.png"));
		gameoverImg = ImageIO.read(getClass().getResource("gameover.png"));
		startImg = ImageIO.read(getClass().getResource("start.png"));
		start();
	}

	public void start() {
		try {
			start = false;
			gameOver = false;
			bird = new Bird();
			ground = new Ground(400);
			column1 = new Column(320 + 100);
			column2 = new Column(320 + 100 + 180);
			score = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void action() throws Exception {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (gameOver) {
					start();
					return;
				}
				start = true;
				bird.flappy();
			}
		});
		requestFocus();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (gameOver) {
						start();
						return;
					}
					start = true;
					bird.flappy();
				}
			}
		});
		// ��ѭ��, ʱ������ 1/60 ��
		while (true) {
			if (start && !gameOver) {
				bird.step();
				column1.step();
				column2.step();
				// ����Ƿ�ͨ��������
				if (bird.pass(column1, column2)) {
					score++;
				}
				if (bird.hit(column1, column2, ground)) {
					start = false;
					gameOver = true;
					String string = caclScore(score);
					JOptionPane.showMessageDialog(null, string);
				}
			}
			ground.step();
			repaint();
			Thread.sleep(1000 / 60);
		}

	}
	
	public String caclScore(int score) {
		String string;
		if(score<3) {
			string="�÷�Ϊ"+score+"��,���ź���D��!";
		}else if(score>=3&& score<5) {
			string="�÷�Ϊ"+score+"��,����Ŷ��C��!";
		}else if(score>=5&& score<10) {
			string="�÷�Ϊ"+score+"��,����Ŷ��B��!";
		}else {
			string="�÷�Ϊ"+score+"��,ţ�ƣ�A��!";
		}
		return string;
	}

	@Override
	public void paint(Graphics g) {
		// ����ݴ���
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);
		// ���Ʊ���
		g.drawImage(background, 0, 0, null);
		// ��������
		column1.paint(g);
		column2.paint(g);
		// ���Ƶ���
		ground.paint(g);
		// ���Ʒ���
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(score + "", 30, 50);
		// ����С��
		bird.paint(g);
		// ���ƽ���״̬
		if (gameOver) {
			// g.drawString("Game Over!", 70 , 190);
			g.drawImage(gameoverImg, 0, 0, null);
			
			return;
		}
		if (!start) {
			// g.drawString("Start >>>", bird.x+35, bird.y);
			g.drawImage(startImg, 0, 0, null);
		}
	}

	public static void main(String[] args) throws Exception {
		final JFrame frame = new JFrame("����С��");
		World world = new World();
		Button button = new Button("��Ϸ���");
		button.setBounds(250, 20, 60, 40);
		button.setBackground(new Color(0,135,147));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "������Ϸ����", "��Ϸ����", JOptionPane.INFORMATION_MESSAGE); 
			}
		});
		frame.add(button);
		/*
		 * JMenu jm = new JMenu("File"); // ����JMenu�˵����� JMenuItem t1 = new
		 * JMenuItem("��Ϸ����"); // �˵��� jm.add(t1); JMenuBar br = new JMenuBar();
		 * br.add(jm); br.setSize(50, 30); frame.add(br);
		 */
		frame.add(world);

		frame.setSize(325, 505);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		world.action();
	}
}
