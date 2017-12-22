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

	private static final int IMAGE_SIDE = 55;// 一个棋子图片的宽度为55

	private static final int ROWS = 10;// 棋盘上总共有10行

	private static final int COLUMNS = 9;// 棋盘上总共有9列

	private static final int MARGIN = 30;// 棋盘与panel的边缘还有30个像素的距离

	private String[] chessStr={"红车.gif","红马.gif","红象.gif","红士.gif","红将.gif","红炮.gif","红卒.gif","黑车.gif","黑马.gif","黑象.gif","黑士.gif","黑将.gif","黑炮.gif","黑卒.gif"};
	
	private Situation situation;
	
	/*
	 * 下面的一些字段是用来控制用户操作的状态的。整个下棋过程中，可以分为两个状态：
	 * 1.用户已经单击选中一个棋子
	 * 2.选中的棋子已经被指定了目标位置或者是还没有选中棋子。
	 * 因为用户只有两种操作，所以在一个循环中，也只能有两种状态。
	 */
	
	//用户是否已经选择一个棋子
	private boolean chess_selected=false;
	//用户选中的棋子所在的位置
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
		
		//标识选中的棋子,主要是换一下背景色
		g.setColor(Color.WHITE);
		if(this.chess_selected==true){
			int x=this.selected_col*IMAGE_SIDE+MARGIN-IMAGE_SIDE/2;
			int y=this.selected_row*IMAGE_SIDE+MARGIN-IMAGE_SIDE/2;
			g.fillRect(x, y, IMAGE_SIDE, IMAGE_SIDE);
		}
		
		g.setColor(Color.BLACK);
		// 首先要画出棋盘，即一个9*10的网格
		// 在界面显示时，横的是x轴，竖的是y轴，这一点与矩阵就刚好相反了，确定一个
		// 坐标时，首先指出的是属于那一列，然后指出属于哪一行
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
		// 士的斜线
		g.drawLine(MARGIN + 3 * IMAGE_SIDE, MARGIN, MARGIN + 5 * IMAGE_SIDE,
				MARGIN + 2 * IMAGE_SIDE);
		g.drawLine(MARGIN + 5 * IMAGE_SIDE, MARGIN, MARGIN + 3 * IMAGE_SIDE,
				MARGIN + 2 * IMAGE_SIDE);

		g.drawLine(MARGIN + 3 * IMAGE_SIDE, MARGIN + 9 * IMAGE_SIDE, MARGIN + 5
				* IMAGE_SIDE, MARGIN + 7 * IMAGE_SIDE);
		g.drawLine(MARGIN + 5 * IMAGE_SIDE, MARGIN + 9 * IMAGE_SIDE, MARGIN + 3
				* IMAGE_SIDE, MARGIN + 7 * IMAGE_SIDE);

		// 画出棋子
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
	 * 根据所在方格位置和棋子名字，画出该棋子
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
	 * 处理各种事件，主要是鼠标单击
	 */
	private void addEvents() {
		/*
		 * 已经做过一个测试：直到上一次事件响应完毕后，对下一次事件的响应才开始，也就是说，
		 * 这个事件响应是单线程的。这样就方便了处理系统中的状态转换
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
				//System.out.println("鼠标单击事件" + e.getX() + " " + e.getY());
				/*
				 *人VS机器代码 
				 *
				 for (int row = 0; row < 10; row++) {
					if (Math.abs(row * IMAGE_SIDE + MARGIN - e.getY()) <= 20) {
						for (int col = 0; col < 9; col++) {
							if (Math.abs(col * IMAGE_SIDE + MARGIN - e.getX()) <= 20) {
								int chess=situation.getChess(row, col);
								//System.out.println("点击到的格点：("+row+","+")"+"该格点的chess值是："+chess);
								if(chess>=8&&chess<=14){//编号为8到14的棋子才是玩家（B方）的棋子，而1到7的棋子是机器（A方）的棋子
									//System.out.println("是选择用户的棋子");
									chess_selected=true;
									selected_row=row;
									selected_col=col;
									repaint();
								}else{
									//System.out.println("所点击的方格中没有棋子或者是对方的棋子	");
									//如果已经选择了一个棋子，并且现在点击的是另外一个位置，则是要走一步棋
									
									if(chess_selected){
										//System.out.println("用户棋子已经选择,值是："+situation.getChess(selected_row, selected_col));
										//首先创建一个Move动作
										Move move=new Move();
										move.setChess_num(situation.getChess(selected_row, selected_col));
										move.setFrom_row(selected_row);
										move.setFrom_col(selected_col);
										move.setTo_row(row);
										move.setTo_col(col);
										//然后判断这个动作是否合法
										if(new Rule().canMove(situation, move)){
											//如果能够移动，那么就移动
											//System.out.println("用户此移动合法");
											situation.move(move);
											chess_selected=false;
											//repaint(10);
											paint();
											//System.out.println("用户走棋后重画一次");
											
											if(judgeWinner()) {
												initBFirst();
												return;
											}
											//System.out.println("重画一次");
											//System.out.println("机器开始计算");
											//IPlayer player = new SimpleAlphaBetaPlayer(5);
											
											//Move autoMove = onlineLearningPlayer.decideMove(situation);
											//Move autoMove = player6.decideMove(situation);
											Move autoMove = onlineLearningPlayer.decideMove(situation);
											//System.out.println("机器走棋: "+autoMove);
											situation.move(autoMove);
											//situation.move(new Player("A").A_goOneStep(situation));
											if(judgeWinner()) {
												initBFirst();
												return;
											}
											paint();
											//repaint(10);
											//System.out.println("机器走棋后重画一次");
										}else{
											//否则，不管
											System.out.println("用户此移动不合法");
										}
									}
								}
								
							}
						}
					}
				}
				
				
				/*
				 * 
				 * 机器VS机器的代码
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
			//JOptionPane.showMessageDialog(this, "A方赢了");
			System.out.println("A wins*************");
			//System.out.println("弹出了对话框");
			return true;
		}else if(winner==1){
			//JOptionPane.showMessageDialog(this, "B方赢了");
			System.out.println("B wins----------------");
			//System.out.println("弹出了对话框");
			return true;
		}else{
			return false;
		}
	}

}