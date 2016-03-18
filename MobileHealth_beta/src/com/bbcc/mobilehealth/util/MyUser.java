package com.bbcc.mobilehealth.util;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	    /**
	 * 
	 */
	
		private String name;
	    private String contact;
	 	private Boolean sex;
        private Integer age;
        private String height; 
    	private String weight;
        private String stepDistance;
        public MyUser()
        {
        	super.setEmailVerified(true);
        	this.name="";
        	this.contact="";
        	this.sex=true;
        	this.age=0;
        	this.height="0.0";
        	this.weight="0.0";
        	this.stepDistance="0.0";
        }
        
	    public String getStepDistance() {
			return stepDistance;
		}
		public void setStepDistance(String stepDistance) {
			this.stepDistance = stepDistance;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer string) {
			this.age = string;
		}
		public String getWeight() {
			return weight;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}
	    public Boolean getSex(){
	        return this.sex;
	    }
	    public void setSex(Boolean sex) {
	        this.sex = sex;
	    }

	    
	
}
