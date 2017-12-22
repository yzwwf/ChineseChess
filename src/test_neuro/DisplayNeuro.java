package test_neuro;

import java.awt.Graphics;
import java.util.Scanner;

import javax.swing.JFrame;

public class DisplayNeuro extends javax.swing.JPanel{

	public static final int SIDE_LENGTH=200;
	
	TestNeuro neuro;//=new TestNeuro();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DisplayNeuro dn=new DisplayNeuro();
		JFrame jFrame=new JFrame();
		jFrame.setBounds(100, 100, 300, 300);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(dn);
		jFrame.setVisible(true);
		
		TestNeuro neuro=new TestNeuro(1,20);
		dn.neuro=neuro;
		Scanner in=new Scanner(System.in);
		dn.repaint();
		for(int i=0;i<100000000;i++){
			double[] input=new double[1];
			input[0]=Math.random()/2+0.25;
			double expectedOutput=(Math.sin(3.14*(input[0]-0.25)*2*4)+1)/8*3+0.125;
			//System.out.println("input : "+input[0]+"\t\texpectedOutput : "+expectedOutput);
			//System.out.println("predict before training : "+neuro.predict(input));
			neuro.trainOnce(input, expectedOutput);
			//System.out.println("predict after training : "+neuro.predict(input));
			
			if(i%100000==0){
			
				System.out.println("input please ");
				//in.next();
				//neuro.initialize();
				dn.repaint();
			}
			
		}
	}
	
	
	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		
		for(double x=0.25;x<0.75;x+=0.005){
			double[] input=new double[1];
			input[0]=x;
			double y=neuro.predict(input);
			arg0.fillRect((int)(x*SIDE_LENGTH), (int)((1-y)*SIDE_LENGTH), 1, 1);
		}
		
		
	}
	

}
