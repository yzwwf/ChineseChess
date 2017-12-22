package test_neuro;

import java.io.Serializable;
import java.util.Scanner;

/*
 * 现在看来，实验结果不大理想，参考了一个网上的程序，应该是没有为
 * 每一个单独的神经元节点再设置一个常量的原因，因此需要再为每一个
 * 神经元节点再设置一个常量，并且添加关于常量的权重的调整机制，然
 * 后再看看添加这种机制之后的效果。——2012.01.03
 */
public class TestNeuro implements Serializable{

	private int INPUT_DIM;
	private int HIDDEN_DIM;
	private double LEARNING_RATE=0.05;
	double [][] input_hidden_weights;
	double [] hidden_output_weights;
	double[] hidden_thresholds;
	double output_threshold;
	
	double ratio=0.0001;
	
	public static void main(String[]args){
		Scanner in=new Scanner(System.in);
		TestNeuro neuro=new TestNeuro(1,5);
		neuro.initialize();
		for(int i=0;i<10000;i++){
			double[] input=new double[1];
			input[0]=Math.random();
			double expectedOutput=input[0]*input[0];
			System.out.println("input : "+input[0]+"\t\texpectedOutput : "+expectedOutput);
			//System.out.println("predict before training : "+neuro.predict(input));
			neuro.trainOnce(input, expectedOutput);
			//System.out.println("predict after training : "+neuro.predict(input));
			//in.next();
		}
		while(true){
			//neuro.printLinks();
			double[] input=new double[1];
			input[0]=in.nextDouble();
			double expectedOutput=in.nextDouble();
			System.out.println("predict before training : "+neuro.predict(input));
			neuro.trainOnce(input, expectedOutput);
			System.out.println("predict after training : "+neuro.predict(input));
			
		}
	}
	
	public TestNeuro(int input_dimension,int hidden_dimension){
		this.INPUT_DIM=input_dimension;
		this.HIDDEN_DIM=hidden_dimension;
		INPUT_DIM=input_dimension;
		HIDDEN_DIM=hidden_dimension;
		input_hidden_weights=new double[INPUT_DIM][HIDDEN_DIM];
		hidden_output_weights=new double[HIDDEN_DIM];
		hidden_thresholds=new double[HIDDEN_DIM];
		this.initialize();
	}
	
	
	/**
	 * 打印出本神经元网络各层之间的连接权重，以及各个神经元上的阈值的信息。
	 */
	void print(){
		System.out.println("隐含层阈值：");
		for(int i=0;i<HIDDEN_DIM;i++){
			System.out.print(hidden_thresholds[i]+" ");
		}System.out.println();
		System.out.println("输出层阈值：");
		System.out.println(output_threshold);
		
		System.out.println("连接权重：*********************");
		System.out.println("输入层与隐含层的连接");
		for(int i=0;i<INPUT_DIM;i++){
			for(int j=0;j<HIDDEN_DIM;j++){
				System.out.print(input_hidden_weights[i][j]+" ");
			}System.out.println();
		}
		System.out.println("隐含层到输出层的连接");
		for(int i=0;i<HIDDEN_DIM;i++){
			System.out.print(hidden_output_weights[i]+" ");
		}System.out.println();
		System.out.println("*********************************");
	}
	
	/**
	 * 初始化，对所有的权值产生一个(0,1)之间的随机double型值
	 */
	void initialize(){
		
		System.out.println("initialize****************");
		
		//输入层到隐含层的连接权重
		for(int i=0;i<INPUT_DIM;i++){
			for(int j=0;j<HIDDEN_DIM;j++){
				input_hidden_weights[i][j]=Math.random()*ratio;
			}
		}
		//隐含层到输出层的连接权重
		for(int i=0;i<HIDDEN_DIM;i++){
			hidden_output_weights[i]=Math.random()*this.ratio;
		}
		//隐含层的阈值
		for(int i=0;i<HIDDEN_DIM;i++){
			hidden_thresholds[i]=Math.random()*this.ratio;
		}
		//输出层的阈值
		output_threshold=Math.random()*this.ratio;
	}
	
	/**
	 * 激励函数
	 * @param x
	 * @return
	 */
	double function(double x){
		return 1/(1+Math.pow(Math.E, -x));
	}
	
	/**
	 * 给定一个输入，进行预测
	 * @param input
	 * @return
	 */
	public double predict(double[]input){
		double[] hiddenValues=new double[HIDDEN_DIM];
		for(int i=0;i<hiddenValues.length;i++){
			double sum=0;
			for(int j=0;j<input.length;j++){
				sum+=input[j]*input_hidden_weights[j][i];
			}
			sum+=hidden_thresholds[i];//再加上本神经元的阈值
			hiddenValues[i]=function(sum);
		}
		
		
		double sum=0;
		for(int i=0;i<HIDDEN_DIM;i++){
			sum+=hiddenValues[i]*hidden_output_weights[i];
		}
		sum+=output_threshold;//输出层神经元的阈值
		return function(sum);
	}
	
	/**
	 * 进行一次训练
	 * @param input
	 * @param expectedOutput
	 */
	public void trainOnce(double[] input, double expectedOutput){
		double[] hiddenValues=new double[HIDDEN_DIM];
		double[] hiddenParams=new double[HIDDEN_DIM];
		
		for(int i=0;i<hiddenValues.length;i++){
			double sum=0;
			for(int j=0;j<input.length;j++){
				sum+=input[j]*input_hidden_weights[j][i];
			}
			sum+=hidden_thresholds[i];//
			hiddenValues[i]=function(sum);
			hiddenParams[i]=sum;
		}
		
		double sum=0;
		for(int i=0;i<HIDDEN_DIM;i++){
			sum+=hiddenValues[i]*hidden_output_weights[i];
		}
		sum+=output_threshold;//
		double outputValue=function(sum);
		double outputParam=sum;
		//System.out.println("实际输出");
		
		/*
		 * 调整权值和阈值
		 */
		
		for(int i=0;i<input.length;i++){
			double factor=(expectedOutput-outputValue)*outputValue*(1-outputValue)*LEARNING_RATE*input[i];
			for(int j=0;j<HIDDEN_DIM;j++){
				double delta=factor*hidden_output_weights[j]*hiddenValues[j]*(1-hiddenValues[j]);
				//System.out.println("输入层到隐含层连接的权重调整：delta = "+delta+"\t\t weight = "+input_hidden_weights[i][j]);
				input_hidden_weights[i][j]+=delta;
			}
		}
		double factor=(expectedOutput-outputValue)*outputValue*(1-outputValue)*LEARNING_RATE;
		for(int i=0;i<hidden_thresholds.length;i++){
			double delta=factor*hidden_output_weights[i]*hiddenValues[i]*(1-hiddenValues[i]);
			hidden_thresholds[i]+=delta;
		}
		
		//System.out.println("hidden_output_weights : "+hidden_output_weights.length);
		for(int i=0;i<hidden_output_weights.length;i++){
			//w+=(exp-act)*df/dw
			//df/dw=x(1-x)*hiddenj
			double delta=factor*hiddenValues[i];
			//System.out.println("隐含层到输出层连接的权值调整：delta = "+delta+"\t\t weight = "+hidden_output_weights[i]);
			hidden_output_weights[i]+=delta;
			
		}
		double delta=(expectedOutput-outputValue)*outputValue*(1-outputValue)*LEARNING_RATE;
		output_threshold+=delta;
		if(Math.abs(outputValue-expectedOutput)>0.1){
			//System.out.println(input[0]+"\t\t"+outputValue+"\t\t"+expectedOutput);
		}
		

		
	}
	
}
