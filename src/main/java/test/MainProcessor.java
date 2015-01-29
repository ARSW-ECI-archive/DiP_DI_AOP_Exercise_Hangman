package test;

import com.lasc.compb.DataProcessor;


public class MainProcessor {

	DataProcessor dp=null;

	public void setDataProcessor(DataProcessor dp) {
		this.dp = dp;
	}
	
	public String execute(int[] data){				
		if(dp.validateData(data)){
			return "Execution OK"; 
		}
		else{
			return "Execution FAILED";
		}
	}
	
}
