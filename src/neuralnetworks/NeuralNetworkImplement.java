package neuralnetworks;

import java.io.Serializable;
import java.util.Random;
import java.util.Scanner;


/**
 * 该神经网络中，我们使用最常见的sigmoid函数作为单个神经元的函数
 * 
 * 反向传播算法中，由于sigmoid函数为sig(s)=1/(1+e^(-s))，可以求
 * 出导数：
 *                   d(sig)/d(s)=sig*(1-sig)
 *
 *
 *
 * 现在，虽然功能都实现了，但是经过试验，效果并不好，应该参考相关
 * 的书籍，肯定有一些方面没有考虑到。
 * @author wwf
 *
 */
public class NeuralNetworkImplement implements NeuralNetwork,Serializable {

    /*  
     * *******************以下是神经网络的参数**************************** 
     */  
    //输入数据的维度（每个数据都是0~1的值）  
    private int inputDim;  
    //输出数据的维度（每个数据都是0~1的值）  
    private int outputDim;  
    //神经网络的层数（输入数据->第1隐含层->...->输出层神经元->输出数据）  
    //这里的每一层对应一列神经元（每个神经元即加权之后进行sigmoid这样一  
    //个处理过程）每一列神经元对应有若干个权重，第1隐含层对应的权重个数是  
    //inputDim*n1,这里n1是第1隐含层中的神经元的个数。  
    private int numLayers;  
    //每一层各有多少个神经元  
    //assert numNeuronsForEachLayer.length==numLayers  
    //assert numNeuronsForEachLayer[numNeuronsForEachLayer.length-1]=outputDim  
    private int [] numNeuronsForEachLayer;  
    /* 
     * **********************以下是神经网络的状态*************************** 
     */  
    //设共有m层神经元，第i层神经元有n[i]个，则每一个神经元有n[i-1]+1个权重，其中多出来的  
    //那个权重是用来乘以offset（一般都是1）的  
    //weights.length=numLayers  
    //weights[i].length=numNeuronsForEachLayer[i]  
    //weights[i][j].length=numNeuronsForEachLayer[i-1]+1  
    private double [][][] weights;  
      
      
    /** 
     * 根据参数构建 
     * @param inputDimension 
     * @param outputDimension 
     * @param numOfLayers 
     * @param numOfNeuronsForEachLayer 
     */  
    public NeuralNetworkImplement(int inputDimension, int outputDimension, int numOfLayers, int[] numOfNeuronsForEachLayer){  
        construct(inputDimension, outputDimension, numOfLayers, numOfNeuronsForEachLayer);  
    }  
      
    /** 
     * 根据参数构建 
     * @param inputDimension 
     * @param numOfNeuronsForEachLayer 
     */  
    public NeuralNetworkImplement(int inputDimension, int[] numOfNeuronsForEachLayer){  
        assert numOfNeuronsForEachLayer.length>0;  
        construct(inputDimension, numOfNeuronsForEachLayer[numOfNeuronsForEachLayer.length-1], numOfNeuronsForEachLayer.length, numOfNeuronsForEachLayer);  
    }  
      
    private void construct(int inputDimension, int outputDimension, int numOfLayers, int[] numOfNeuronsForEachLayer){  
        assert inputDimension>0;//至少有一维输入数据  
        assert outputDimension>0;//至少有一个输出  
        assert numOfLayers>0;//至少有一层神经元  
        assert numOfNeuronsForEachLayer.length==numOfLayers;//神经元的层数要和每一层的描述相对应  
        assert numOfNeuronsForEachLayer[numOfLayers-1]==outputDimension;//最后一层（输出层）神经元的个数与输出维数相等  
          
        this.inputDim=inputDimension;  
        this.outputDim=outputDimension;  
        this.numLayers=numOfLayers;  
        this.numNeuronsForEachLayer=new int[numLayers];  
        System.arraycopy(numOfNeuronsForEachLayer, 0, this.numNeuronsForEachLayer, 0, numLayers);  
        this.randomInit();//随机初始化连接权重  
    } 
    
      
    /** 
     * 采用随机方法初始化该神经网络：对于每一个连接，将其权重初始化为-1到1之间的一个随机数 
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
     * 给定输入数据，根据神经网络目前的权值计算出输出数据 
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
     * 激励函数 
     * @param x 
     * @return 
     */  
    private double f(double x){  
        return 1/(1+Math.exp(-x));  
    }  
      
    /** 
     * 激励函数的导数df/dx 在x处的值 
     * @param x 
     * @return 
     */  
    private double df(double x){  
        double delta=0.0001;  
        double a=f(x+delta)-f(x);  
        return a/delta;  
    }  
      
      
      
    /** 
     * 根据输入数据，计算每一个神经元的输出，并将这些输出（包括输入向量）保存下来 
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
                sum+=neuronInputWeights[neuronInputWeights.length-1];//不要忘了加上offset: 1*offset_weight  
                layerOutput[neuronIndex]=f(sum);  
            }//for each neuron of the layer  
            result[layer+1]=new double[layerOutput.length];  
            System.arraycopy(layerOutput, 0, result[layer+1], 0, layerOutput.length);  
            intermediateResult=layerOutput;  
        }  
        return result;  
    }  
      
    /** 
     * 根据输入和输出，进行backward propagation 
     * @param input 
     * @param expectedOutput 
     */  
    private void backprop(double[] input, double[] expectedOutput, double learningRate){  
        double [][] inputAndNeuronOutputVals=calculate(input);  
        //先判断预期输出和真实输出之间的误差，如果为0则不用学习  
        double error=0;  
        for(int i=0;i<outputDim; i++){  
            error+=Math.abs(inputAndNeuronOutputVals[inputAndNeuronOutputVals.length-1][i]-expectedOutput[i]);  
        }  
        if(error==0)return ;  
        //每一个neuron对应一个中间值，对于A-w->B,将来调整w的值的时候，就可以  
        //计算每一个神经元的delta值  
        double[][]deltas=new double[numLayers][];  
        deltas[numLayers-1]=new double[numNeuronsForEachLayer[numLayers-1]];  
        //先计算最后一层的delta值，就等于derivative*(expectedOutput-output)=(1-output)*output*(expectedOutput-output)  
        for(int lastNeuronIndex=0;lastNeuronIndex<numNeuronsForEachLayer[numLayers-1];lastNeuronIndex++){  
            //output.length=numNeuronsForEachLayer[numLayers-1],即：最后一层的神经元个数就等于输出维度  
            double eOut=expectedOutput[lastNeuronIndex];  
            double rOut=inputAndNeuronOutputVals[this.numLayers][lastNeuronIndex];  
            double diff=eOut-rOut;  
            double derivative=rOut*(1-rOut);  
            double _delta=derivative*diff;  
            deltas[this.numLayers-1][lastNeuronIndex]=_delta;  
        }  
        //从倒数第二层开始循环计算每个神经元的delta值  
        for(int layer=numLayers-2;layer>=0;layer--){  
            double[] deltasForThisLayer=new double[this.numNeuronsForEachLayer[layer]];  
            int nxtLayer=layer+1;  
            for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[layer];neuronIndex++){  
                //对于该层的每个神经元,计算其中间值  
                double weightedSum=0;//下一层的神经元的delta值的加权和  
                for(int nxtNeuronIndex=0;nxtNeuronIndex<numNeuronsForEachLayer[nxtLayer];nxtNeuronIndex++){  
                    //计算下一层中每个神经元传递到此神经元上的中间值  
                    double connWeight=weights[nxtLayer][nxtNeuronIndex][neuronIndex];  
                    double nxtVal=deltas[nxtLayer][nxtNeuronIndex];  
                    weightedSum+=(connWeight*nxtVal);  
                }  
                //计算该神经元的delta值: weightedSum*derivative  
                double rOut=inputAndNeuronOutputVals[layer+1][neuronIndex];  
                double derivative=rOut*(1-rOut);  
                double _delta=weightedSum*derivative;  
                deltasForThisLayer[neuronIndex]=_delta;  
            }  
            deltas[layer]=deltasForThisLayer;  
        }  
          
          
        //根据delta值可以计算每个连接的权重调节数。对于连接A->B,权重调节值为alpha*output(A)*delta(B)  
        //对于offset权重，调节量为alpha*inters(B)，因为其output永远为1  
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
        //调节输入层神经元的权重  
        for(int neuronIndex=0;neuronIndex<numNeuronsForEachLayer[0];neuronIndex++){  
            double deltaB=deltas[0][neuronIndex];  
            for(int inputIndex=0;inputIndex<inputDim;inputIndex++){  
                double outputA=input[inputIndex];  
                double adjustment=learningRate*outputA*deltaB;  
                weights[0][neuronIndex][inputIndex]+=adjustment;  
            }  
            double offsetAdjustment=learningRate*deltaB;  
            weights[0][neuronIndex][inputDim]+=offsetAdjustment;  
        }//调节结束，本函数的任务至此已经结束  
    }  
}
