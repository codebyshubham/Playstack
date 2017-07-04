package com.spidren.api.backpress;



public abstract class Back implements BackCall{
	public BackTag tag;
	
	public Back(){
		
	}
	
	public Back(BackTag tag){
		this.tag = tag;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == this) {
            return true;
        }
		if (o == null || !(o instanceof Back)) {
			return false;
		}
		
		Back call = (Back) o;
		if(call.tag == tag)
			return true;
		
		return false;
	}
	
}
interface BackCall{
	public void perform();
}



