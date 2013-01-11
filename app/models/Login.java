package models;
	
	public class Login {

		public String strPassword;
		public String strUsername;
		
		public Login(String pass, String user){
			strPassword=pass;
			strUsername=user;
		}
		
		public boolean authenticateUser(){
			
			boolean flag=false;
			if(strPassword.length()>0 && strUsername.length()>0){
				
				if(strPassword.equals("ayond2012!") && strUsername.equals("ayond")){
					flag=true;
				}
			}
			
			return flag;
		}
	}


