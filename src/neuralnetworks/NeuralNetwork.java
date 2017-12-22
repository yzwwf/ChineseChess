package neuralnetworks;

/**
 * �ýӿڹ涨�������������ṩ�Ĳ������ܹ�����������
 * 1.����һ���������������ѧϰ
 * 2.����һ�����룬Ԥ�����
 * @author wwf
 *
 */
public interface NeuralNetwork {
	
	/**
	 * ѵ�������磬��һ�������һ��������Ը�������
	 * ����һ���мලѵ��
	 */
	public void trainOnce(double[]inputs,double[]expectedOutputs);
	
	public void trainOnce(double[]inputs, double[]expectedOutputs, double delta);
	
	/**
	 * ����һ�����룬Ԥ�����
	 * @param inputs
	 * @return
	 */
	public double[] predict(double[]inputs);
}
