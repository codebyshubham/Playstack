package com.spidren.api.backpress;

import java.util.ArrayList;



public class BackPress {
	
	private ArrayList<Back> list;
	private boolean lock;
	
	
	public BackPress() {
		list =  new ArrayList<Back>();
		lock = false;
	}
	
	public void addBack(Back back){
		if(!list.contains(back))
			list.add(back);

		//MainActivity.cf.Log("i am back="+getBackCount());

	}
	
	public boolean performBack(){
		int count = getBackCount();
		if(count > 0){
			Back back = list.get(count - 1);
			back.perform();
			removeLast();
			return true;
		}
		return false;
	}
	
	public void removeBack(Back back){
		if(getBackCount() > 0){
			if(list.contains(back))
				list.remove(back);
		}
		//MainActivity.cf.Log("i am back="+getBackCount());

	}
	
	public void removeLast(){
		if(getBackCount() > 0){
			list.remove(getBackCount() - 1);	
		}
		//MainActivity.cf.Log("i am last back="+getBackCount());
	}
	
	public void removeBack(BackTag tag){
		removeBack(new Back(tag) {
			
			@Override
			public void perform() {
				
			}
		});

	}
	
	public int getBackCount(){
		return list.size();
	}
	
	public boolean isLock(){
		return lock;
	}
	
	public void lock(){
		lock = true;
	}
	
	public void unLock(){
		lock = false;
	}
	
	
}
