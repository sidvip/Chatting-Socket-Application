import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatClient {

	public static void main(String [] args) throws IOException{
		String hostName = "localhost";
		int portNumber = Integer.parseInt("1500");
		Utility my_util = new Utility();

		try(
				Socket ccSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(ccSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(ccSocket.getInputStream()));
				)
		{

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			Utility my_utils = new Utility();
			String fromServer; 
			String active_users_for_chat = "";

			// Database Directory Path //
			String db_dir = my_utils.getcwd() + "\\Chat_Database";

			// Database Entries File Path//
			String db_entries = db_dir + "\\all_users.txt";

			// Chat logs //
			String chat_logs = db_dir + "\\Chat_logs";

			int global_index_for_add_new_user = 0; // inside add new user //

			while ((fromServer = in.readLine()) != null) {
				//				System.out.println(" Data from server : "  + fromServer);
				//				System.out.println(fromServer.contains("log_history"));
				if (fromServer.contains("Welcome to Chit")) {

					// User Side Checking //
					System.out.println("\n\n\t\t" + fromServer.split(",")[0] + "\n");
					System.out.println("\t\t" + fromServer.split(",")[1] + "\n\t\t   "+fromServer.split(",")[2] + "\n");
					System.out.print("  User's Response : ");
					String fromUser = "";

					fromUser = stdIn.readLine();
					boolean flag = true;

					while (flag) {
						if (fromUser.equals("r") || fromUser.equals("l"))
						{
							out.println(fromUser);
							flag = false;       			
						}
						else 
						{
							System.out.print("  User's Response : ");
							fromUser = stdIn.readLine();
							continue;
						}
					}
				}

				// Login Information //
				else if(fromServer.contains("Login")) {
					String fromUser = "";

					System.out.println("\n\n" + fromServer.split(",")[0] +"\n");

					// Username check //

					System.out.print(fromServer.split(",")[1] + "  ");
					String name = stdIn.readLine();		
					my_util.checkNull(fromServer.split(",")[1] , name);
					my_util.check_cont(fromServer.split(",")[1] , name);


					// Password check //

					System.out.print(fromServer.split(",")[2] + "  ");
					String pass = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[2] , pass);

					fromUser = name + "," + pass + "," + "l";
					out.println(fromUser);


				}	

				// Registration Information //

				else if (fromServer.contains("Register")) {
					String fromUser = "";

					System.out.println("\n\n" + fromServer.split(",")[0] +"\n");

					// Name // 
					System.out.print(fromServer.split(",")[1] + "  ");
					String name = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[1] , name);


					// Date of Birth // 
					System.out.print(fromServer.split(",")[2] + "  ");
					String dob = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[2] , dob);
					my_util.check_dob(fromServer.split(",")[2] , dob);

					// Mobile Number//
					System.out.print(fromServer.split(",")[3] + "  ");
					String cont = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[3] , cont);
					my_util.check_cont(fromServer.split(",")[3] , cont);

					// Gender //
					System.out.print(fromServer.split(",")[4] + "  ");
					String gen = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[4] , gen);

					// Password //
					System.out.print(fromServer.split(",")[5] + "  ");
					String pass = stdIn.readLine();
					my_util.checkNull(fromServer.split(",")[5] , pass);

					fromUser = name + "," + dob + "," + cont + "," + gen + "," + pass + "," + "r";
					out.println(fromUser);
				}


				else if (fromServer.contains("Logged")){

					String[] loggedin_inf = fromServer.split(",");
					String loggedin_username = loggedin_inf[loggedin_inf.length - 1];

					System.out.println("\n\t <<<<<<<<< " + loggedin_inf[0] + " >>>>>>>>> " +"\n" );
					System.out.println("<<<<<<<<< " + loggedin_inf[1] + " >>>>>>>>>\n");
					System.out.println("\n\n\t |************* :-) " + " Welcome! " + loggedin_username + " :-) ************|\n\n");

					String command_field = " Type the command: ";
					System.out.print(command_field);
					String command = stdIn.readLine();
					String list_users = my_util.CommandCheck(command_field, command);
					out.println(list_users);
				}

				// List all user present in the database //

				else if( fromServer.contains("ls_users")) {
					//            		System.out.println(fromServer);

					String []all_infs = fromServer.split(",");
					int len_users = (all_infs.length-1)/2;
					//            		System.out.println(len_users);

					String warning = all_infs[all_infs.length -1];

					System.out.println("\n\n\tAll Users ");
					System.out.println("\t       |`- " + all_infs[0] + " < ==== > " + all_infs[len_users+1]);
					System.out.println("\t       |" );
					for(int i=1; i<len_users;i++) {
						System.out.println("\t       |`- " + all_infs[i] + " < ==== > " + all_infs[len_users+i+1]);
					}            		

					String insert_prompt = "\n\n Enter \"ls_friends\" or \"add_friend\" or \"active_users\" : ";
					System.out.print(insert_prompt);
					String friend_name = stdIn.readLine();
					friend_name = my_util.FriendsCommandCheck(insert_prompt, friend_name);
					out.println(friend_name);
				}

				// List Existing friends list ..

				else if(fromServer.contains("ls_friends")) {
					String[] friends_info = fromServer.split(",");
					String[] friends_list = friends_info[1].split(":");

					int friends_list_length = friends_list.length;

					System.out.println("\n  ===> Your Friends ");
					for(int i=0; i<friends_list_length;i++) {
						System.out.println("\t\t  `- " + friends_list[i]);
					}  
					//						System.out.println(friends_list_length);
					if (friends_list[0].equals("No friends")) {
						out.println("add_friend");
					}
					else {
						String chat_prompt = "\nEnter the mobile number of your friend to chat: ";
						System.out.print(chat_prompt);
						String chat_user = stdIn.readLine();
						chat_user = my_util.check_cont(chat_prompt, chat_user);
						chat_user += ",your_friend";
						if (global_index_for_add_new_user > 3) {
							out.println("add_friend");
						}
						global_index_for_add_new_user +=1;
						out.println(chat_user);
					}
				}
				//				}


				// Try user again //

				else if (fromServer.contains("Try")) {
					System.out.println("\n <<<<<<<<< " + fromServer + " >>>>>>>>> ");
					out.println("add_friend");
				}

				// Add Friend // 

				else if(fromServer.contains("chatter")) {
					String[] takeinf = fromServer.split(",");
					String username = takeinf[1];
					String mobile = takeinf[2];
					System.out.println("\n\n <<<<<<<<<<< " + takeinf[0] + " >>>>>>>>>>> ");

					System.out.print(username + " ");
					String user_name  = stdIn.readLine();
					System.out.print(mobile + " ");
					String mobile_num = stdIn.readLine();

					String addeduser = user_name + "," + mobile_num + "," + "add_newfriend"; 
					out.println(addeduser);
				}


				// Start Chatting by entering the valid number //

				else if(fromServer.contains("Type the mobile of the friends from the friend list to chat with")) {
					System.out.println( "\n\n<<<<< " + fromServer + " >>>>>\n");
					System.out.print(" ====> Enter the mobile number of your friend to chat: ");
					String friend_tochat = stdIn.readLine()+",your_friend";
					out.println(friend_tochat);			

				}

				// User Selection for chatting //

				else if(fromServer.contains("Start, List Friends")) {
					String[] notes = fromServer.split(",");
					System.out.println(" \n\n <<<<<<<<<<< " + notes[0] + " >>>>>>>>>>> \n");
					System.out.print(" ====> Type \"begin\" for conversation : " + notes[1]+"\n");
					System.out.print(" ====> Type \"ls_friends\" : " + notes[2] + "\n");
					System.out.print(" ====> Type \"add_friend\" to add new friend : " + notes[3] + "\n");

					String response = "\n ====> Enter your response: ";
					System.out.print(response);
					String ink = stdIn.readLine();
					ink = my_util.respcheck(response, ink);
					out.println(ink);
				}

				// Show Active Users //

				else if(fromServer.contains("@#!#!$!#*U!*(@U!(*@U(*#U!(*#U(*#U(")) {

					active_users_for_chat = fromServer;					
					String[] all_active_users = fromServer.split(",");
					int	len_active = all_active_users.length;
					//					System.out.println(len_active);

					System.out.println("\n\n\tAll Active Users ");
					for(int i=0; i<len_active-1;i++) {
						System.out.println("\t      `- " + all_active_users[i]);
					}            		
					System.out.println();
					out.println("ls_users");
				}

				// Start Chatting main //

				else if (fromServer.contains("@$chiatanlmdal#%^&(*(102912012373eqwkewqo")) {
					//					System.out.println(fromServer);
					if (fromServer.split(",")[2].equals("live_or_not@$!#!@#@!#!@$!@#@!#@!$(*!#&%(*#!")) {
						String friend_user = fromServer.split(",")[1];
						String received_message = fromServer.split(",")[2];
						String friend_cont = fromServer.split(",")[3];
						String you = fromServer.split(",")[4];
						String your_num = fromServer.split(",")[5];
						String message = "";

						System.out.println("\n\n\t\t <<<<<< =====  Conversion   ===== >>>>>> \n" );
						System.out.println(" \t ==== NOTE >>>>>>>>>> " + you + " can only write log once as " + friend_user + " is offline :-( \n");
						System.out.print(you + " : ");
						message = stdIn.readLine();

						String log_file = chat_logs + "\\" + your_num + "_" +  you + "\\" + your_num + "_" + friend_cont + ".log";
						String file_content = you + " : " + message;
						//						my_util.CreateTempfile(log_file, file_content);
						String encrypted_message = "@$chiatanlmdal#%^&(*(102912012373eqwkewqo" +","+ friend_user +"," + message +"," + friend_cont +","+ you + "," + your_num + ",offline!@#$";
						out.println(encrypted_message);
					}

					else if(fromServer.contains("online)(*&#@#*@&#*@#@#)")){
						//						System.out.println(fromServer);
						String[] live_user_string = fromServer.split(",");
						String friend_user = live_user_string[1];
						String message = live_user_string[6];
						String friend_cont = live_user_string[3];
						String you = live_user_string[4];
						String your_num = live_user_string[5];

						System.out.println(" \n < ========== " + message + " =========== > \n");
						System.out.print(you + " : ( bye => to quit ) ");
						String your_mess = stdIn.readLine();
						String encrypted_message = "@$chiatanlmdal#%^&(*(102912012373eqwkewqo" +","+ friend_user +"," + your_mess +"," + friend_cont +","+ you + "," + your_num;
						out.println(encrypted_message);
					}

					else {
						//						System.out.println();
						String friend_user = fromServer.split(",")[1];
						String received_message = fromServer.split(",")[2];
						String friend_cont = fromServer.split(",")[3];
						String you = fromServer.split(",")[4];
						String your_num = fromServer.split(",")[5];
						String message = "";

						//					System.out.println("\n\n\t\t <<<<<< =====  Conversion   ===== >>>>>> \n" );
						//					System.out.println(" \t<<<<<<<<<<<<<< :-8 Please be silent :-8 >>>>>>>>>>>>>> \n");
						//					System.out.println(" \t ====>>>>>>>>>> \"" + you + "\" <===> \"" + friend_user + "\" are chatting :-) \n");

						System.out.print(you + " : ( bye => to quit ) ");
						message = stdIn.readLine();

						//					System.out.print(friend_user + " : ");
						//					System.out.println(received_message);
						String encrypted_message = "@$chiatanlmdal#%^&(*(102912012373eqwkewqo" +","+ friend_user +"," + message +"," + friend_cont +","+ you + "," + your_num;
						out.println(encrypted_message);
					}
				}


				// log_history : in this block recent log and whole recent log is sent from user to server //
				// Threading can be done in this block only //

				else if(fromServer.contains("l@!#$%og_history")) {
//					System.out.println(fromServer);
					String [] every_user_log_recent = fromServer.split(",");
//					for (int p=0; p < every_user_log_recent.length; p++) {
//						System.out.println(every_user_log_recent[p]);
//					}

					String you = every_user_log_recent[2];
					String friend = every_user_log_recent[4];
					String[] recent_message_to_you = every_user_log_recent[0].split("log");
					String friend_status = every_user_log_recent[6];

					System.out.println("\n\t\t---------------------------------------------");
					System.out.println("\t\t\t  " + friend + " 's log to " + you+ "\n");

					for (int i = 0; i < recent_message_to_you.length-1; i++) {
						if(i==0) {
							System.out.println("\t\t---------------------------------------------\n");
						}
						System.out.println("\t\t\t\t" + recent_message_to_you[i]);
					}
					
					if(fromServer.contains("No message from")) {
						System.out.println("\t\t---------------------------------------------\n");
						System.out.println("\t\t" + fromServer.split(",")[0].split(";")[0]);
					}
					
					System.out.println("\n\t\t---------------------------------------------\n");
					
					System.out.println("\n\t\t\t  ;-) " + friend + friend_status + "\n");
					out.println("ls_users");

				}


			} 

		}catch(UnknownHostException e) {
			System.err.printf("Don't know about the host: %s", hostName);
			System.exit(1);}catch(IOException e){
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);}
	}
}