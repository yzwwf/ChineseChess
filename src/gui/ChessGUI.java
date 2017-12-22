package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import chessabstraction.Move;
import chessabstraction.Rule;
import chessabstraction.Situation;
import player.IPlayer;
import player.OnlineBackwardLearningPlayer;
import player.OnlineLearningPlayer;
import player.RandomPlayer;
import player.SimpleAlphaBetaPlayer;
import player.SimpleLearningPlayer;

public class ChessGUI {
	public static void main(String[] args) {
		new ChessGUI().init();
	}

	private void init() {
		JFrame jFrame = new JFrame();
		jFrame.setSize(600, 600);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChessPanel chessPanel = new ChessPanel();
		chessPanel.setSize(jFrame.getWidth(), jFrame.getHeight());
		jFrame.add(chessPanel);
		chessPanel.paint();
	}
}

class ChessPanel extends JPanel {

	private static final int IMAGE_SIDE = 55;// һ������ͼƬ�Ŀ��Ϊ55

	private static final int ROWS = 10;// �������ܹ���10��

	private static final int COLUMNS = 9;// �������ܹ���9��

	private static final int MARGIN = 30;// ������panel�ı�Ե����30�����صľ���

	private String[] chessStr={"�쳵.gif","����.gif","����.gif","��ʿ.gif","�콫.gif","����.gif","����.gif","�ڳ�.gif","����.gif","����.gif","��ʿ.gif","�ڽ�.gif","����.gif","����.gif"};
	
	private Situation situation;
	
	/*
	 * �����һЩ�ֶ������������û�������״̬�ġ�������������У����Է�Ϊ����״̬��
	 * 1.�û��Ѿ�����ѡ��һ������
	 * 2.ѡ�е������Ѿ���ָ����Ŀ��λ�û����ǻ�û��ѡ�����ӡ�
	 * ��Ϊ�û�ֻ�����ֲ�����������һ��ѭ���У�Ҳֻ��������״̬��
	 */
	
	//�û��Ƿ��Ѿ�ѡ��һ������
	private boolean chess_selected=false;
	//�û�ѡ�е��������ڵ�λ��
	private int selected_row;
	private int selected_col;
	
	
	
	public ChessPanel() {
		super();
		this.addEvents();
		this.situation=new Situation();
		this.situation.initBoard();
		//this.setSize(600, 600);
		this.repaint();
		System.out.println("width is "+this.getWidth());
	}
	private void init(){
		if (Math.random()<0.5) {
			System.out.println("init a first");
			this.situation.init(true);
		}else {
			System.out.println("init b first");
			this.situation.init(false);
		}
		this.chess_selected=false;
		this.paint();
	}
	private void initAFirst() {
		this.situation.init(true);
		this.chess_selected=false;
		this.paint();
	}
	private void initBFirst() {
		this.situation.init(false);
		this.chess_selected=false;
		this.paint();
	}
	
	public void paint(){
		BufferedImage bufferedImage=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.OPAQUE);
		Graphics g =bufferedImage.getGraphics();
		this.paint(g);
		bufferedImage.flush();
		this.getGraphics().drawImage(bufferedImage, 0, 0, this);
	}

	
	public void paint(Graphics g) {		
		// this.setBackground(new Color(204, 204, 0));
		g.setColor(new Color(204, 204, 0));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//��ʶѡ�е�����,��Ҫ�ǻ�һ�±���ɫ
		g.setColor(Color.WHITE);
		if(this.chess_selected==true){
			int x=this.selected_col*IMAGE_SIDE+MARGIN-IMAGE_SIDE/2;
			int y=this.selected_row*IMAGE_SIDE+MARGIN-IMAGE_SIDE/2;
			g.fillRect(x, y, IMAGE_SIDE, IMAGE_SIDE);
		}
		
		g.setColor(Color.BLACK);
		// ����Ҫ�������̣���һ��9*10������
		// �ڽ�����ʾʱ�������x�ᣬ������y�ᣬ��һ�������͸պ��෴�ˣ�ȷ��һ��
		// ����ʱ������ָ������������һ�У�Ȼ��ָ��������һ��
		for (int i = 0; i < 10; i++) {
			g.drawLine(MARGIN, MARGIN + i * IMAGE_SIDE,
					MARGIN + 8 * IMAGE_SIDE, MARGIN + i * IMAGE_SIDE);
		}
		for (int i = 0; i < 9; i++) {
			g.drawLine(MARGIN + i * IMAGE_SIDE, MARGIN,
					MARGIN + i * IMAGE_SIDE, MARGIN + 4 * IMAGE_SIDE);
			g.drawLine(MARGIN + i * IMAGE_SIDE, MARGIN + 5 * IMAGE_SIDE, MARGIN
					+ i * IMAGE_SIDE, MARGIN + 9 * IMAGE_SIDE);
		}
		// ʿ��б��
		g.drawLine(MARGIN + 3 * IMAGE_SIDE, MARGIN, MARGIN + 5 * IMAGE_SIDE,
				MARGIN + 2 * IMAGE_SIDE);
		g.drawLine(MARGIN + 5 * IMAGE_SIDE, MARGIN, MARGIN + 3 * IMAGE_SIDE,
				MARGIN + 2 * IMAGE_SIDE);

		g.drawLine(MARGIN + 3 * IMAGE_SIDE, MARGIN + 9 * IMAGE_SIDE, MARGIN + 5
				* IMAGE_SIDE, MARGIN + 7 * IMAGE_SIDE);
		g.drawLine(MARGIN + 5 * IMAGE_SIDE, MARGIN + 9 * IMAGE_SIDE, MARGIN + 3
				* IMAGE_SIDE, MARGIN + 7 * IMAGE_SIDE);

		// ��������
		this.drawBoard(situation, g);
	}
	
	private void drawBoard(Situation situation, Graphics g){
		for(int i=0;i<10;i++){
			for(int j=0;j<9;j++){
				if(situation.getChess(i, j)!=0){
					this.drawChess(i, j, chessStr[situation.getChess(i, j)-1], g);
				}
			}
		}
	}

	/*
	 * �������ڷ���λ�ú��������֣�����������
	 */
	private void drawChess(int row, int col, String chessStr, Graphics g) {
		int drawX = MARGIN + col * IMAGE_SIDE - IMAGE_SIDE / 2;
		int drawY = MARGIN + row * IMAGE_SIDE - IMAGE_SIDE / 2;
		Image image = null;
		try {
			image = ImageIO.read(new File(chessStr));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(image, drawX, drawY, this);
	}

	/*
	 * ��������¼�����Ҫ����굥��
	 */
	private void addEvents() {
		/*
		 * �Ѿ�����һ�����ԣ�ֱ����һ���¼���Ӧ��Ϻ󣬶���һ���¼�����Ӧ�ſ�ʼ��Ҳ����˵��
		 * ����¼���Ӧ�ǵ��̵߳ġ������ͷ����˴���ϵͳ�е�״̬ת��
		 * 
		 */
		this.addMouseListener(new MouseListener() {
			IPlayer player1 = new SimpleAlphaBetaPlayer(1);
			IPlayer player2 = new SimpleAlphaBetaPlayer(2);
			IPlayer player3 = new SimpleAlphaBetaPlayer(3);
			IPlayer player4 = new SimpleAlphaBetaPlayer(4);
			IPlayer player5 = new SimpleAlphaBetaPlayer(5);
			IPlayer player6 = new SimpleAlphaBetaPlayer(6);
			IPlayer onlineLearningPlayer = new OnlineLearningPlayer("online_learning_player_1");
			IPlayer randomPlayer = new RandomPlayer();
			IPlayer onlineBackwardLearningPlayer = new OnlineBackwardLearningPlayer("online_backward_learning_player_1");
			IPlayer simpleLearningPlayer = new SimpleLearningPlayer();
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("��굥���¼�" + e.getX() + " " + e.getY());
				/*
				 *��VS�������� 
				 *
				 for (int row = 0; row < 10; row++) {
					if (Math.abs(row * IMAGE_SIDE + MARGIN - e.getY()) <= 20) {
						for (int col = 0; col < 9; col++) {
							if (Math.abs(col * IMAGE_SIDE + MARGIN - e.getX()) <= 20) {
								int chess=situation.getChess(row, col);
								//System.out.println("������ĸ�㣺("+row+","+")"+"�ø���chessֵ�ǣ�"+chess);
								if(chess>=8&&chess<=14){//���Ϊ8��14�����Ӳ�����ң�B���������ӣ���1��7�������ǻ�����A����������
									//System.out.println("��ѡ���û�������");
									chess_selected=true;
									selected_row=row;
									selected_col=col;
									repaint();
								}else{
									//System.out.println("������ķ�����û�����ӻ����ǶԷ�������	");
									//����Ѿ�ѡ����һ�����ӣ��������ڵ����������һ��λ�ã�����Ҫ��һ����
									
									if(chess_selected){
										//System.out.println("�û������Ѿ�ѡ��,ֵ�ǣ�"+situation.getChess(selected_row, selected_col));
										//���ȴ���һ��Move����
										Move move=new Move();
										move.setChess_num(situation.getChess(selected_row, selected_col));
										move.setFrom_row(selected_row);
										move.setFrom_col(selected_col);
										move.setTo_row(row);
										move.setTo_col(col);
										//Ȼ���ж���������Ƿ�Ϸ�
										if(new Rule().canMove(situation, move)){
											//����ܹ��ƶ�����ô���ƶ�
											//System.out.println("�û����ƶ��Ϸ�");
											situation.move(move);
											chess_selected=false;
											//repaint(10);
											paint();
											//System.out.println("�û�������ػ�һ��");
											
											if(judgeWinner()) {
												initBFirst();
												return;
											}
											//System.out.println("�ػ�һ��");
											//System.out.println("������ʼ����");
											//IPlayer player = new SimpleAlphaBetaPlayer(5);
											
											//Move autoMove = onlineLearningPlayer.decideMove(situation);
											//Move autoMove = player6.decideMove(situation);
											Move autoMove = onlineLearningPlayer.decideMove(situation);
											//System.out.println("��������: "+autoMove);
											situation.move(autoMove);
											//situation.move(new Player("A").A_goOneStep(situation));
											if(judgeWinner()) {
												initBFirst();
												return;
											}
											paint();
											//repaint(10);
											//System.out.println("����������ػ�һ��");
										}else{
											//���򣬲���
											System.out.println("�û����ƶ����Ϸ�");
										}
									}
								}
								
							}
						}
					}
				}
				
				
				/*
				 * 
				 * ����VS�����Ĵ���
				 */
				 while(true){

					double rand =Math.random();
					if (rand < 0.9) {
						situation.move(simpleLearningPlayer.decideMove(situation));
					} else if(rand < 0.4) {
						situation.move(player1.decideMove(situation));
					} else if(rand < 0.5) {
						situation.move(player2.decideMove(situation));
					} else if (rand < 0.6) {
						situation.move(player3.decideMove(situation));
					} else if (rand < 0.7) {
						situation.move(player4.decideMove(situation));
					} else if (rand < 0.9){
						situation.move(player4.decideMove(situation));
					} else {
						situation.move(randomPlayer.decideMove(situation));
					}
					//situation.move(player4.decideMove(situation));
//					paint();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
					if(judgeWinner()) {
						init();
						continue;
					}
					//situation.move(onlineLearningPlayer.decideMove(situation));
					//paint();
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
					//if(judgeWinner()) {
				//		init();
				//		continue;
				//	}
				}/**/

			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

		});
	}
	
	private boolean judgeWinner(){
		int winner=this.situation.winner();
		if(winner==0){
			//JOptionPane.showMessageDialog(this, "A��Ӯ��");
			System.out.println("A wins*************");
			//System.out.println("�����˶Ի���");
			return true;
		}else if(winner==1){
			//JOptionPane.showMessageDialog(this, "B��Ӯ��");
			System.out.println("B wins----------------");
			//System.out.println("�����˶Ի���");
			return true;
		}else{
			return false;
		}
	}

}