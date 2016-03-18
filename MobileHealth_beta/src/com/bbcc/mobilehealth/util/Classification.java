package com.bbcc.mobilehealth.util;
import java.util.Arrays;


public class Classification {

	 double[] array;
	 int length;
	 int stepCount=0;
	 String state;
	 double means=0;
	 double std=0;
	 double vs=0;
	 double sk=0;
	 double qd=0;
	 double csvm=0;
	 double[] csvms=new double[20];
	 double kurtosis=0;//峰态
	public Classification(){};
	public Classification(double[] array,int length){
		this.array=array;
		this.length=length;
	};
	public double getMeans()//平均值
	{    double sum=0;
		for (int i=0;i<length; i++)
		{
			sum+=array[i];
		}
		return means=sum/length;	
		
	}
	public double getStd()//标准差
	{
		
		double stdsum=0;
		for(int i=0;i<length;i++)
		{
			stdsum+=Math.pow(array[i]-means,2);
		}
		
		return std=Math.sqrt(stdsum/length);
	}
	public double getVs()//
	{
		
		return vs=std/means;
	}
	
	public double getSk()//偏度	
	{
		double sksum=0;
		for(int i=0;i<length;i++)
		{
			sksum+=Math.pow(array[i]-means, 3);
		}
		return sk=(sksum*length)/((length-1)*(length-2)*Math.pow(std,2));	
	}
	public double getQd()//四分位差
	{
		double[] sortArray=Arrays.copyOf(array, length);
		//冒泡排序
		for (int i = 0; i < length -1; i++){    //最多做n-1趟排序
			              for(int j = 0 ;j < length - i - 1; j++){    
			                  if(sortArray[j] < sortArray[j + 1]){   
			                      double temp = sortArray[j];
			                      sortArray[j] = sortArray[j + 1];
			                      sortArray[j + 1] = temp;
			                  }
			              }  
		}
		return qd=sortArray[3*((length+1)/4)]-sortArray[(length+1)/4];
	}
	public double[] getCsvm()
	{
		int prestate=0;
		double extrem=0;
		
		int index=0;
		
		for(int i=1;i<length;i++)
		{
			if(array[i]-array[i-1]>=0)
			{
				if(prestate==0)
				{
				prestate=1;
//				extrem=array[i-1];
				}
				else if(prestate==-1)
				{
					
					if(extrem==0)
					{
						extrem=array[i-1];
					}
					if (Math.abs(array[i-1]-extrem)>=0.2*9.8)
					{
						csvms[index]=Math.abs(array[i-1]-extrem);
						extrem=array[i-1];
						index++;
					}
					prestate=1;
				}	
			}
			else
			{
				
				if(prestate==0)
				{
				prestate=-1;	
				}
				else if(prestate==1)
				{
					
					if(extrem==0)
					{
						extrem=array[i-1];
					}
					if (Math.abs(array[i-1]-extrem)>=0.2*9.8)
					{
						csvms[index]=Math.abs(array[i-1]-extrem);
						extrem=array[i-1];
						index++;
						
					}
					prestate=-1;
				}
			}
		}
		double csvmsum=0;
		for(int j=0;j<index+1;j++)
		{
			if(csvms[j]>0)
			{
				stepCount++;
				csvmsum+=csvms[j];
			}
				
				
		}
		csvm=csvmsum/(index+1);
		return csvms;
	}
	public int getStepCount()
	{
		return this.stepCount;
		
	}
	public double getKurtosis()//峰态
	{
		double k4Sum=0;
		double k2Sum=0;
		for(int i=0;i<length;i++)
		{
			k4Sum+=Math.pow(array[i]-means, 4);
			k2Sum+=Math.pow(array[i]-means, 2);
		}
		return kurtosis=(length*(length+1)*k4Sum-3*(length-1)*Math.pow(k2Sum, 2))/((length-1)*(length-2)*(length-3)*Math.pow(std, 4));
		
	}
	public String getState()
	{
		if(qd<=-1.191)
		{
			if(std>4.906)
			{
				state="run";//state=2.0
			}
			else if(std>3.045&&std<=4.906)
			{
				if(sk<=1.853)
				{
					if(means<=10.076)
					{
						if(qd<=-5.064)
						{
							state="walk";//state=0.0
						}
						else
						{
							if(sk<=-0.363)
							{
								state="walk";//state=0.0
							}
							else
							{
								state="down";
							}
						}
					}
					
					else
					{
						state="walk";//state=0.0
					}
				}
				else
				{
					if(qd<=-6.581)
					{
						state="walk";//state=0.0
					}
					else
					{
						state="down";//state=4.0
					}
				}
			}
			else if(std>2.522&&std<=3.045)
			{
				if(means<=10.012)
				{
					state="down";
					
				}
				else
				{
					if(sk<=1.159)
					{
						state="walk";
					}
					else
					{
						if(qd<=-4.124)
						{
							state="down";
						}
						else
						{
							state="up";
						}
					}
				}
			}
			else if(std<=2.522)
			{
				if(qd<=-1.580)
				{
					state="up";
				}
				else
				{
					state="down";
				}
			}
		}
		else
		{
			state="static";//state=1.0
		}
		return state;
		
	}
	
}
