package neuralnetworks;

/**
 * 该接口规定了神经网络对外界提供的操作。总共有两个操作
 * 1.给定一对输入输出，进行学习
 * 2.给定一个输入，预测输出
 * @author wwf
 *
 */
public interface NeuralNetwork {
	
	/**
	 * 训练神经网络，给一个输入和一个输出，对该神经网络
	 * 进行一次有监督训练
	 */
	public void trainOnce(double[]inputs,double[]expectedOutputs);
	
	public void trainOnce(double[]inputs, double[]expectedOutputs, double delta);
	
	/**
	 * 给定一个输入，预测输出
	 * @param inputs
	 * @return
	 */
	public double[] predict(double[]inputs);
}
