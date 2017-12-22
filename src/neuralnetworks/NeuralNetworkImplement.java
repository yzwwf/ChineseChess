package neuralnetworks;

import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;


/**
 * ���������У�����ʹ�������sigmoid������Ϊ������Ԫ�ĺ���
 * 
 * ���򴫲��㷨�У�����sigmoid����Ϊsig(s)=1/(1+e^(-s))��������
 * ��������
 *                   d(sig)/d(s)=sig*(1-sig)
 *
 *
 *
 * ���ڣ���Ȼ���ܶ�ʵ���ˣ����Ǿ������飬Ч�������ã�Ӧ�òο����
 * ���鼮���϶���һЩ����û�п��ǵ���
 * @author wwf
 *
 */
public class NeuralNetworkImplement implements NeuralNetwork,Serializable {

    /*  
     * *******************������������Ĳ���**************************** 
     */  
    //�������ݵ�ά�ȣ�ÿ�����ݶ���0~1��ֵ��  
    private int inputDim;  
    //������ݵ�ά�ȣ�ÿ�����ݶ���0~1��ֵ��  
    private int outputDim;  
    //������Ĳ�������������->��1������->...->�������Ԫ->������ݣ�  
    //�����ÿһ���Ӧһ����Ԫ��ÿ����Ԫ����Ȩ֮�����sigmoid����һ  
    //��������̣�ÿһ����Ԫ��Ӧ�����ɸ�Ȩ�أ���1�������Ӧ��Ȩ�ظ�����  
    //inputDim*n1,����n1�ǵ�1�������е���Ԫ�ĸ�����  
    private int numLayers;  
    //ÿһ����ж��ٸ���Ԫ  
    //assert numNeuronsForEachLayer.length==numLayers  
    //assert numNeuronsForEachLayer[numNeuronsForEachLayer.length-1]=outputDim  
    private int [] numNeuronsForEachLayer;  
    /* 
     * **********************�������������״̬*************************** 
     */  
    //�蹲��m����Ԫ����i����Ԫ��n[i]������ÿһ����Ԫ��n[i-1]+1��Ȩ�أ����ж������  
    //�Ǹ�Ȩ������������offset��һ�㶼��1����  
    //weights.length=numLayers  
    //weights[i].length=numNeuronsForEachLayer[i]  
    //weights[i][j].length=numNeuronsForEachLayer[i-1]+1  
    private double [][][] weights;  
      
      
    /** 
     * ���ݲ������� 
     * @param inputDimension 
     * @param outputDimension 
     * @param numOfLayers 
     * @param numOfNeuronsForEachLayer 
     */  
    public NeuralNetworkImplement(int inputDimension, int outputDimension, int numOfLayers, int[] numOfNeuronsForEachLayer){  
        construct(inputDimension, outputDimension, numOfLayers, numOfNeuronsForEachLayer);  
    }  
      
    /** 
     * ���ݲ������� 
     * @param inputDimension 
     * @param numOfNeuronsForEachLayer 
     */  
    public NeuralNetworkImplement(int inputDimension, int[] numOfNeuronsForEachLayer){  
        assert numOfNeuronsForEachLayer.length>0;  
        construct(inputDimension, numOfNeuronsForEachLayer[numOfNeuronsForEachLayer.length-1], numOfNeuronsForEachLayer.length, numOfNeuronsForEachLayer);  
    }  
      
    private void construct(int inputDimension, int outputDimension, int numOfLayers, int[] numOfNeuronsForEachLayer){  
        assert inputDimension>0;//������һά��������  
        assert outputDimension>0;//������һ�����  
        assert numOfLayers>0;//������һ����Ԫ  
        assert numOfNeuronsForEachLayer.length==numOfLayers;//��Ԫ�Ĳ���Ҫ��ÿһ����������Ӧ  
        assert numOfNeuronsForEachLayer[numOfLayers-1]==outputDimension;//���һ�㣨����㣩��Ԫ�ĸ��������ά�����  
          
        this.inputDim=inputDimension;  
        this.outputDim=outputDimension;  
        this.numLayers=numOfLayers;  
        this.numNeuronsForEachLayer=new int[numLayers];  
        System.arraycopy(numOfNeuronsForEachLayer, 0, this.numNeuronsForEachLayer, 0, numLayers);  
        this.randomInit();//�����ʼ������Ȩ��  
    } 
    
      
    /** 
     * �������������ʼ���������磺����ÿһ�����ӣ�����Ȩ�س�ʼ��Ϊ-1��1֮���һ������� 
     *  
     */  
    public void randomInit(){  
    		Random rand = new Random();
        this.weights=new double[numLayers][][];  
        //layer 0: each neuron has inputDim input weights  
        this.weights[0]=new double[this.numNeuronsForEachLayer[0]][inputDim+1];  
        for(int neuronIndex=0;neuronIndex<this.numNeuronsForEachLayer[0];neuronIndex++){  
            for(int prevIndex=0; prevIndex<inputDim; prevIndex++){  
                this.weights[0][neuronIndex][prevIndex]=rand.nextGaussian()*0.1;  
            }  
            this.weights[0][neuronIndex][inputDim]=Math.random()*2-1;  
        }  
          
        for(int layer=1; layer<this.numLayers; layer++){  
            this.weights[layer]=new double[this.numNeuronsForEachLayer[layer]][this.numNeuronsForEachLayer[layer-1]+1];  
            for(int neuronIndex=0; neuronIndex<this.numNeuronsForEachLayer[layer]; neuronIndex++){  
                for(int prevIndex=0; prevIndex<this.numNeuronsForEachLayer[layer-1]; prevIndex++){  
                    this.weights[layer][neuronIndex][prevIndex]=Math.random()*2-1;  
                }  
                this.weights[layer][neuronIndex][this.numNeuronsForEachLayer[layer-1]]=Math.random()*2-1;  
            }  
        }  
    }  
      
      
    /** 
     * �����������ݣ�����������Ŀǰ��Ȩֵ������������ 
     * @param input 
     * @return 
     */  
    public double[] predict(double[] input){  
        assert input!=null;  
        assert input.length==inputDim;  
        double [] intermediateResult=input;  
        for(int layer=0;layer<numLayers;layer++){//for each layer, we calculate the output  
            double [] layerOutput=new double[numNeuronsForEachLayer[layer]];  
            for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[layer];neuronIndex++){  
                //for each neuron of the layer  
                double[] neuronInputWeights=weights[layer][neuronIndex];  
                double sum=0;  
                assert intermediateResult.length==neuronInputWeights.length-1;  
                for(int i=0;i<intermediateResult.length;i++){  
                    sum+=intermediateResult[i]*neuronInputWeights[i];  
                }  
                sum+=neuronInputWeights[neuronInputWeights.length-1];  
                layerOutput[neuronIndex]=f(sum);  
            }//for each neuron of the layer  
            intermediateResult=layerOutput;  
        }  
        //now intermediateResult is the final result  
        return intermediateResult;  
    }  
      
      
    public void trainOnce(double[] input, double [] output, double delta){  
        backprop(input, output, delta);  
    }  
    
    public void trainOnce(double[] input, double[] output) {
    		trainOnce(input, output, 0.002);
    }
      
    /** 
     * �������� 
     * @param x 
     * @return 
     */  
    private double f(double x){  
        return 1/(1+Math.exp(-x));  
    }  
      
    /** 
     * ���������ĵ���df/dx ��x����ֵ 
     * @param x 
     * @return 
     */  
    private double df(double x){  
        double delta=0.0001;  
        double a=f(x+delta)-f(x);  
        return a/delta;  
    }  
      
      
      
    /** 
     * �����������ݣ�����ÿһ����Ԫ�������������Щ��������������������������� 
     * @param input 
     */  
    private double[][] calculate(double [] input){  
        assert input!=null;  
        assert input.length==inputDim;  
        double [][] result=new double[numLayers+1][];  
        result[0]=new double[inputDim];  
        System.arraycopy(input, 0, result[0], 0, input.length);  
        double [] intermediateResult=input;  
        for(int layer=0;layer<numLayers;layer++){//for each layer, we calculate the output  
            double [] layerOutput=new double[numNeuronsForEachLayer[layer]];  
            for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[layer];neuronIndex++){  
                //for each neuron of the layer  
                double[] neuronInputWeights=weights[layer][neuronIndex];  
                double sum=0;  
                for(int i=0;i<intermediateResult.length;i++){  
                    sum+=intermediateResult[i]*neuronInputWeights[i];  
                }  
                sum+=neuronInputWeights[neuronInputWeights.length-1];//��Ҫ���˼���offset: 1*offset_weight  
                layerOutput[neuronIndex]=f(sum);  
            }//for each neuron of the layer  
            result[layer+1]=new double[layerOutput.length];  
            System.arraycopy(layerOutput, 0, result[layer+1], 0, layerOutput.length);  
            intermediateResult=layerOutput;  
        }  
        return result;  
    }  
      
    /** 
     * ������������������backward propagation 
     * @param input 
     * @param expectedOutput 
     */  
    private void backprop(double[] input, double[] expectedOutput, double learningRate){  
        double [][] inputAndNeuronOutputVals=calculate(input);  
        //���ж�Ԥ���������ʵ���֮��������Ϊ0����ѧϰ  
        double error=0;  
        for(int i=0;i<outputDim; i++){  
            error+=Math.abs(inputAndNeuronOutputVals[inputAndNeuronOutputVals.length-1][i]-expectedOutput[i]);  
        }  
        if(error==0)return ;  
        //ÿһ��neuron��Ӧһ���м�ֵ������A-w->B,��������w��ֵ��ʱ�򣬾Ϳ���  
        //����ÿһ����Ԫ��deltaֵ  
        double[][]deltas=new double[numLayers][];  
        deltas[numLayers-1]=new double[numNeuronsForEachLayer[numLayers-1]];  
        //�ȼ������һ���deltaֵ���͵���derivative*(expectedOutput-output)=(1-output)*output*(expectedOutput-output)  
        for(int lastNeuronIndex=0;lastNeuronIndex<numNeuronsForEachLayer[numLayers-1];lastNeuronIndex++){  
            //output.length=numNeuronsForEachLayer[numLayers-1],�������һ�����Ԫ�����͵������ά��  
            double eOut=expectedOutput[lastNeuronIndex];  
            double rOut=inputAndNeuronOutputVals[this.numLayers][lastNeuronIndex];  
            double diff=eOut-rOut;  
            double derivative=rOut*(1-rOut);  
            double _delta=derivative*diff;  
            deltas[this.numLayers-1][lastNeuronIndex]=_delta;  
        }  
        //�ӵ����ڶ��㿪ʼѭ������ÿ����Ԫ��deltaֵ  
        for(int layer=numLayers-2;layer>=0;layer--){  
            double[] deltasForThisLayer=new double[this.numNeuronsForEachLayer[layer]];  
            int nxtLayer=layer+1;  
            for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[layer];neuronIndex++){  
                //���ڸò��ÿ����Ԫ,�������м�ֵ  
                double weightedSum=0;//��һ�����Ԫ��deltaֵ�ļ�Ȩ��  
                for(int nxtNeuronIndex=0;nxtNeuronIndex<numNeuronsForEachLayer[nxtLayer];nxtNeuronIndex++){  
                    //������һ����ÿ����Ԫ���ݵ�����Ԫ�ϵ��м�ֵ  
                    double connWeight=weights[nxtLayer][nxtNeuronIndex][neuronIndex];  
                    double nxtVal=deltas[nxtLayer][nxtNeuronIndex];  
                    weightedSum+=(connWeight*nxtVal);  
                }  
                //�������Ԫ��deltaֵ: weightedSum*derivative  
                double rOut=inputAndNeuronOutputVals[layer+1][neuronIndex];  
                double derivative=rOut*(1-rOut);  
                double _delta=weightedSum*derivative;  
                deltasForThisLayer[neuronIndex]=_delta;  
            }  
            deltas[layer]=deltasForThisLayer;  
        }  
          
          
        //����deltaֵ���Լ���ÿ�����ӵ�Ȩ�ص���������������A->B,Ȩ�ص���ֵΪalpha*output(A)*delta(B)  
        //����offsetȨ�أ�������Ϊalpha*inters(B)����Ϊ��output��ԶΪ1  
        for(int layer=numLayers-1;layer>0;layer--){  
            int prevLayer=layer-1;  
            for(int neuronB=0;neuronB<numNeuronsForEachLayer[layer];neuronB++){  
                double deltaB=deltas[layer][neuronB];  
                for(int neuronA=0;neuronA<numNeuronsForEachLayer[prevLayer];neuronA++){  
                    double outputA=inputAndNeuronOutputVals[prevLayer+1][neuronA];  
                    double adjustment=learningRate*outputA*deltaB;  
                    weights[layer][neuronB][neuronA]+=adjustment;  
                }  
                double offsetAdjustment=learningRate*deltaB;  
                weights[layer][neuronB][numNeuronsForEachLayer[prevLayer]]+=offsetAdjustment;  
            }  
        }//for each non-input layer  
        //�����������Ԫ��Ȩ��  
        for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[0];neuronIndex++){  
            double deltaB=deltas[0][neuronIndex];  
            for(int inputIndex=0;inputIndex<inputDim;inputIndex++){  
                double outputA=input[inputIndex];  
                double adjustment=learningRate*outputA*deltaB;  
                weights[0][neuronIndex][inputIndex]+=adjustment;  
            }  
            double offsetAdjustment=learningRate*deltaB;  
            weights[0][neuronIndex][inputDim]+=offsetAdjustment;  
        }//���ڽ����������������������Ѿ�����  
    }  
}
