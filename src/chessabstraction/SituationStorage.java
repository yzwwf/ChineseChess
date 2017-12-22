package chessabstraction;

import java.util.Hashtable;

public class SituationStorage {

	private static final int TABLE_SIZE=1000000;
	
	Hashtable<Integer, Integer> storage=new Hashtable<Integer, Integer>();
	
	public void put(Situation situation,int score){
		this.maintain();
		if(!this.storage.containsKey(situation.hashCode())){
			this.storage.put(situation.hashCode(), score);
		}
		
	}
	
	public boolean has(Situation situation){
		return this.storage.containsKey(situation.hashCode());
	}
	
	public int get(Situation situation){
		if(this.storage.containsKey(situation.hashCode())){
			return this.storage.get(situation.hashCode());
		}else{
			return Integer.MIN_VALUE;
		}
		
	}
	
	public void maintain(){
		while(this.storage.size()>TABLE_SIZE){
			this.storage.remove(this.storage.keys().nextElement());
		}
	}
	
	public void clear(){
		this.storage.clear();
	}

}
