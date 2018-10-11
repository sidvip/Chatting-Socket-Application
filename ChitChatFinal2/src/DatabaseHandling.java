import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseHandling {

	public void WriteDatabase(String path, String user_info) {
		try { 	   
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true)); 
			user_info = user_info.substring(0, user_info.length() - 2);
			out.newLine();
			out.write(user_info.trim()); 
			out.close(); 
		} 
		catch (IOException e) { 
			System.out.println("exception occoured" + e); 
		} 
	}


	public String CheckAvailability(String path, String user_num){
		String status = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			String db_nums = "";
			//	         System.out.println("User Mob Number: " + user_num);
			while ((str = inp.readLine()) != null) {
				db_nums += str.split(",")[2];
				//	             System.out.println("DB_CON: " + db_nums);
			}

			if (db_nums.contains(user_num)) {
				status = "l";
			}
			else {
				status = "r";
			}
			inp.close();
		} catch (IOException e) {
		}
		return status;
	}

	public String UserAuthentication(String path, String username){
		String status = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			String db_users = "";
			//		         System.out.println("\nUser Mobile Number :" + username);
			while ((str = inp.readLine()) != null) {
				db_users += str.split(",")[2] + ",";
				//			         System.out.println("DB_USER Name:"  + db_users);
			}
			if (db_users.contains(username)) {
				status = "okay";
			}
			else {
				status = "fail";
			}

			inp.close();
		} catch (IOException e) {
		}
		return status;

	}


	public String PassAuthentication(String path, String password) {
		String status = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			String db_pass = "";
			//		         System.out.println("\nUser Password: "  + password);
			while ((str = inp.readLine()) != null) {
				db_pass += str.split(",")[4] + ",";
				//		             System.out.println("Database Password: " + db_pass);
			}
			if (db_pass.contains(password)) {
				status = "okay";
			}
			else {
				status = "fail";
			}

			inp.close();
		} catch (IOException e) {
		}
		return status;
	}

	public String ReadDatabaseUsers(String path) {
		String all_users = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				all_users += str.split(",")[0] + ",";
				//		         System.out.println("DB_USER Name:"  + all_users);
			}

			inp.close();  
		} catch (IOException e) {
		}
		all_users += "ls_users";
		return all_users;
	}

	public String ReadDatabaseConts(String path) {
		String all_conts = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				all_conts += str.split(",")[2] + ",";
				//		         System.out.println("DB_USER Name:"  + all_conts);
			}

			inp.close();  
		} catch (IOException e) {
		}
		all_conts += " ;-) Start Chatting  ;-) ";
		return all_conts;
	}

	public String FullAuthentication(String path, String user_info) {
		String status = "fail";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			String username = user_info.split(",")[0];
			String password = user_info.split(",")[1];

			System.out.println();
			//		         System.out.println("\nUser Password: "  + password);
			//		         System.out.println("\nUser Mob number : " + username + "\n");

			while ((str = inp.readLine()) != null) {
				String db_pass = str.split(",")[4];
				String db_user = str.split(",")[2];
				//		             System.out.println("Database Information : " + db_user + " : " +  db_pass);
				if (db_pass.equals(password) && db_user.equals(username)) {
					status = "okay";
					break;
				}
			}
			inp.close();
		} catch (IOException e) {
		}
		return status;
	}

	public String Readfriendslist(String path) {
		String all_friends = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				all_friends += str+":";
				//		         System.out.println(" User Friends:"  + all_friends);
			}

			inp.close();  
		} catch (IOException e) {
		}
		return all_friends;
	}

	public String Readfriendsnums(String path) {
		String all_nums = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				all_nums += str.split("--")[1] + ",";
				//		         System.out.println(" Friends Num:"  + all_nums);
			}

			inp.close();  
		} catch (IOException e) {
		}
		return all_nums;
	}

	public String Readfriendsname(String path) {
		String all_names = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				all_names += str.split("--")[0] + ",";
				//		         System.out.println(" Friends Num:"  + all_names);
			}

			inp.close();  
		} catch (IOException e) {
		}
		return all_names;
	}


	public String ListActiveUserNum(String a_nums) {
		String[] u_nums = a_nums.split(",");
		String all_nums = "";
		for (int j = 0; j < u_nums.length -1 ; j++) {
			all_nums += u_nums[j].split("--")[1] + ",";
		}
		return all_nums;

	}

	public int NumofLines(String path) {
		int num_lines = 0;
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				num_lines +=1;
			}

			inp.close();  
		} catch (IOException e) {
		}
		return num_lines;
	}

	public String currentlog(String path, int previous_index) {
		String recent_log = "";
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			int start_index = 0;
			while ((str = inp.readLine()) != null) {
				if(start_index>previous_index) {
					recent_log +=str + ",";
				}
				start_index += 1;
			}

			inp.close();  
		} catch (IOException e) {
		}
		return recent_log;
	}
	
	
	public String ReadFile_any(String path) {
		String file_content = "";

		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				file_content += str;
			}

			inp.close();  
		} catch (IOException e) {
		}
		return file_content;
	}
	
	public String ReadFile_any_user_log(String path) {
		String file_content = "";

		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				System.out.println(str + "oh no");
				file_content += str + "log";
			}
			inp.close();  
		} catch (IOException e) {
		}
		
		File check = new File(path);
		
		if(!check.exists()){
			file_content += "no logs for you";
		}
//		System.out.println("FIle COnetent  ==== " + file_content);
		return file_content;
	}
	
	public String ReadAllLogs(String path) {
		String file_content = "";

		File file = new File(path);
		if (!file.exists()) {
			file_content = "no logs for you";
		}
		
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
//				System.out.println(str);
				file_content += str + "log";
			}

			inp.close();  
		} catch (IOException e) {
		}
		return file_content;
	}
	
}


