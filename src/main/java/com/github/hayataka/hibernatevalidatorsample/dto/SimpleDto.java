package com.github.hayataka.hibernatevalidatorsample.dto;
import javax.validation.constraints.AssertTrue;

public class SimpleDto {
	    @AssertTrue
	    private boolean aaa;
	    public boolean isAaa() {
	        return aaa;
	    }
	    public void setAaa(boolean aaa) {
	        this.aaa = aaa;
	    }
}
