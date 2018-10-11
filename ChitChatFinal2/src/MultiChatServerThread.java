import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class MultiChatServerThread extends Thread{
	private Socket socket = null;

	public MultiChatServerThread (Socket socket) {
		super("Normal_thread");
		this.socket = socket;
	}

	public void run() {

		try( 
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				)
		{      



			String inputLine;
			Utility my_utils = new Utility();
			DatabaseHandling my_db = new DatabaseHandling();

			ChitChatProtocol ccp = new ChitChatProtocol();
			out.println(ccp.processInput("e1")); // First time writing on socket //

			// Database Directory Path //
			String db_dir = my_utils.getcwd() + "\\Chat_Database";

			// Database Entries File Path//
			String db_entries = db_dir + "\\all_users.txt";

			// Chat logs //
			String chat_logs = db_dir + "\\Chat_logs";

			// All Friends //
			String your_friends = "";
			String your_friends_name = "";

			// Logged In user //
			String logged_user = "";
			String logged_user_phone = "";

			String loggedin_user_thread = "";

			String active_users_for_chat = "";

			String all_recent_logs_by_you = ""; //// all recent logs typed by us /////



			// Main Loop for the Client Server Communication //

			while ((inputLine = in.readLine()) != null) {

				// System.out.println("Input Line from User inside main while loop: " + inputLine);

				// Input from the client //
				if (inputLine.length() > 2) 
				{
					String[] all_data = inputLine.split(",");
					String label = all_data[all_data.length -1];


					// Registration Check //

					if (label.equals("r")) 
					{
						String user_data = db_entries;
						String existence = my_db.CheckAvailability(user_data,inputLine.split(",")[2]);

						String mobile_number = inputLine.split(",")[2]; // User name for registration //
						String user_name = inputLine.split(",")[0];  // Contact Number for Registration //

						if (existence.equals("l")) {
							out.print("Remember ;-) User already exists. Please Login now ! ;-)");
							out.println(ccp.processInput("l"));
						}
						else {
							String user_chat_data = chat_logs + "\\" + mobile_number + "_" + user_name; // User chat log directory inside the db_dir named as contact_username //
							String friend_file_path = user_chat_data + "\\friends_list.txt";
							my_utils.CreateDirectory(user_chat_data);
							my_db.WriteDatabase(user_data, inputLine);
							my_utils.CreateEmptyfile(friend_file_path);
							out.print("Thankfully :-) User is registered successfully. Please Login ! :-)");
							out.println(ccp.processInput("l"));
						}
					}

					// Login Check //

					else if(label.equals("l"))
					{

						String user_data = db_entries;

						String username = inputLine.split(",")[0];	// username is the mobile number //
						String password = inputLine.split(",")[1];  // password for logging in //

						String login_info = username + "," + password;

						String user_sta = my_db.UserAuthentication(user_data, username);
						String pass_sta = my_db.PassAuthentication(user_data, password);
						String full_sta = my_db.FullAuthentication(user_data, login_info);

						if ((user_sta.equals("okay") && pass_sta.equals("fail")) && full_sta.equals("fail")) {
							out.print(" Warning :-(  Password is invalid !  :-( ");
							out.println(ccp.processInput("l"));
						}

						else if ((user_sta.equals("okay") && pass_sta.equals("okay")) && full_sta.equals("okay")) {

							String list_of_users = my_db.ReadDatabaseUsers(user_data); // Read Database Users //
							String list_of_conts = my_db.ReadDatabaseConts(user_data); // Read Database Mobile Numbers //
							int user_index = my_utils.ContRespUser(list_of_conts, username);
							String logged_in_username = list_of_users.split(",")[user_index]; 
							logged_user = logged_in_username;
							logged_user_phone = username;

							Thread.currentThread().setName(logged_user + "--" + logged_user_phone); /// setting the name of the current thread ///
							loggedin_user_thread = Thread.currentThread().getName();

							out.println(" :) Logged in Successfully :), To watch the commands all users type \"ls_users\"" + "," + username +"," + password + "," + logged_in_username);
						}

						else if ((user_sta.equals("fail") && pass_sta.equals("okay")) && full_sta.equals("fail")) {
							out.print(" Warning :-( Username is invalid !  :-( ");
							out.println(ccp.processInput("l"));
						}
						else if ((user_sta.equals("okay") && pass_sta.equals("okay")) && full_sta.equals("fail")){
							out.print(" :-# You have wrong username or password :-# ");
							out.println(ccp.processInput("l"));
						}
						else {
							out.print(" :-# You are not registered user. Please register :-# ");
							out.println(ccp.processInput("r"));

						}

					}

					// Send whole list of users //

					else if(inputLine.equals("ls_users")) {
						String all_users_data = db_entries;
						String fulllist_of_users = my_db.ReadDatabaseUsers(all_users_data);
						String fulllist_of_conts = my_db.ReadDatabaseConts(all_users_data);
						String whole_infs = fulllist_of_users+","+fulllist_of_conts;
						out.println(whole_infs);
					}

					// Receives the mobile number from the user // 

					else if (inputLine.equals("ls_friends") || inputLine.equals("add_friend") || inputLine.contains("add_newfriend")) {

						String friends_mob_num = inputLine;
						String user_data = db_entries;
						String list_of_users = my_db.ReadDatabaseUsers(user_data);
						String list_of_conts = my_db.ReadDatabaseConts(user_data);
						String all_infs = list_of_users+","+list_of_conts;

						// Give pop up to add new friend //

						if (inputLine.equals("add_friend")) {
							out.println(" Add new chatter gye from the list , Username:, Mobile Number: ");
						}

						// give the list of friends present in user's friends_list.txt //

						else if(inputLine.equals("ls_friends")) {
							String friend_file_path = chat_logs + "\\" + logged_user_phone + "_" + logged_user + "\\friends_list.txt";
							your_friends = my_db.Readfriendslist(friend_file_path);
							if(your_friends.isEmpty()) {
								out.println("friend ls_friends," + "No friends" + ",Add a friend ");
							}
							else {
								out.println("friend ls_friends," + your_friends + ",Send the number of your friend to chat");
							}
						}

						// Add new friend in the users' friends_list.txt // 

						else if(inputLine.contains("add_newfriend")) 
						{
							String[] added_friend_info = inputLine.split(",");
							String added_friend_username = added_friend_info[0];
							String added_friend_mob = added_friend_info[1];
							String loggedin_user_friend_list = chat_logs + "\\" + logged_user_phone + "_" + logged_user +"\\"+ "friends_list.txt";
							String available_friends = my_db.Readfriendsnums(loggedin_user_friend_list);
							if(list_of_conts.contains(added_friend_mob) && list_of_users.contains(added_friend_username)) {
								if(available_friends.contains(added_friend_mob)) {
									out.println(" ;-) User Exists in the database ;-), Start, List Friends, Join with new user again ");
								}
								else {
									String added_friend_inf = added_friend_username + "--" + added_friend_mob;
									my_utils.CreateTempfile(loggedin_user_friend_list, added_friend_inf);
									out.println(" :-) User Added successfully :-) , Start, List Friends, Join with new user again ");
								}
							}
							else {	
								out.println("  Try, the another user as it is not in database ");
							}


						}

					}


					// send all active to clients //

					else if(inputLine.equals("active_users")) {

						ThreadGroup currentgroup = Thread.currentThread().getThreadGroup();
						//						System.out.println(currentgroup);
						int noThreads = currentgroup.activeCount();
						Thread[] lstThreads = new Thread[noThreads];
						currentgroup.enumerate(lstThreads);
						String all_active_users = "";
						for (int i = 0; i < noThreads; i++) {
							//System.out.println("Thread No:" + i + " = " + lstThreads[i].getName());
							String t_name = lstThreads[i].getName();
							if (!t_name.equals("main") && !t_name.equals("Normal_thread")) {
								// System.out.print(lstThreads[i].getName());
								all_active_users += lstThreads[i].getName() + ",";
							}
						}

						all_active_users += "@#!#!$!#*U!*(@U!(*@U(*#U!(*#U(*#U(";
						active_users_for_chat = all_active_users;
						out.println(all_active_users);

					}

					// take begin command from client to start chat //

					else if(inputLine.equals("begin")) {
						out.println("Type the mobile of the friends from the friend list to chat with");
					}


					// chat block start for client -client interactions ////

					else if(inputLine.contains("your_friend")) 
					{	
						ThreadGroup currentgroup = Thread.currentThread().getThreadGroup();
						int noThreads = currentgroup.activeCount();
						Thread[] lstThreads = new Thread[noThreads];
						currentgroup.enumerate(lstThreads);
						String all_active_users = "";
						for (int i = 0; i < noThreads; i++) {
							String t_name = lstThreads[i].getName();
							if (!t_name.equals("main") && !t_name.equals("Normal_thread")) {
								all_active_users += lstThreads[i].getName() + ",";
							}
						}
						all_active_users += "@#!#!$!#*U!*(@U!(*@U(*#U!(*#U(*#U(";
						active_users_for_chat = all_active_users;

						String friend_name = inputLine.split(",")[0]; ///// ====>  mobile number of requested chatting user
						String friend_file_path = chat_logs + "\\" + logged_user_phone + "_" + logged_user + "\\friends_list.txt";

						String your_friend_nums = my_db.Readfriendsnums(friend_file_path);
						String your_friend_names = my_db.Readfriendsname(friend_file_path);

						String chat_user_name = your_friend_names.split(",")[my_utils.ContRespUser(your_friend_nums, friend_name)];  ////// =====>  name of requested chat user //
						String your_friends_info = my_db.Readfriendslist(friend_file_path);

						/// check for active user or not ///
						String active_users_nums = my_db.ListActiveUserNum(active_users_for_chat);

						boolean live_or_not = active_users_nums.contains(friend_name); // active user friend //
						if (your_friends_info.contains(friend_name)) {
							if (!live_or_not) {
								String live_info =  "@$chiatanlmdal#%^&(*(102912012373eqwkewqo"+"," + chat_user_name + ",live_or_not@$!#!@#@!#!@$!@#@!#@!$(*!#&%(*#!," + friend_name 
										+ "," + logged_user + "," + logged_user_phone;
								out.println(live_info);
							}
							else {
								String live_info =  "@$chiatanlmdal#%^&(*(102912012373eqwkewqo"+"," + chat_user_name + ",online)(*&#@#*@&#*@#@#)," + friend_name 
										+ "," + logged_user + "," + logged_user_phone + "," + chat_user_name + " is live! send a message \" ";
								out.println(live_info);
							}
						}
						else {
							out.println("ls_friends," + your_friends_info);
						}
					}

					//// Chatting and logging block ////

					else if(inputLine.contains("@$chiatanlmdal#%^&(*(102912012373eqwkewqo")) {
						String[] data_arr = inputLine.split(",");
						String friend_file_path = chat_logs + "\\" + logged_user_phone + "_" + logged_user + "\\friends_list.txt";
						String your_friends_info = my_db.Readfriendslist(friend_file_path);

						String friend_user = inputLine.split(",")[1];
						String message = inputLine.split(",")[2];
						String friend_cont = inputLine.split(",")[3];
						String you = inputLine.split(",")[4];
						String your_num = inputLine.split(",")[5];
						String log_file = chat_logs + "\\" + friend_cont + "_" +  friend_user + "\\" + your_num + "_" + friend_cont + ".log";


						//// First time log to the conversation file ////

						if (data_arr.length >= 7) {
							String formatted_message = you + " : " + message;
							my_utils.CreateTempfile(log_file, formatted_message);
							out.println("ls_friends," + your_friends_info);
						}
						else {

							/// Final Bye Message from the user /// 

							if (((message.trim()).toLowerCase()).equals("bye")) {

								my_utils.CreateTempfile(log_file, you + " : " + "bye");

								all_recent_logs_by_you += "bye"; // all recent logs that you have typed in terminal //

								//								System.out.println(" Full Recent Chat Log " + all_recent_logs_by_you);

								String full_log_path = chat_logs + "\\" + your_num + "_" +  you + "\\" + your_num +  "_" + friend_cont + ".log";
								String line1 = "l@!#$%og_history," + full_log_path + "," + you + "," + friend_user + "," + all_recent_logs_by_you + "," + friend_cont;


								String line2 = "";
								//								String current_user_thread = friend_user + "--" + your_num;
								String your_friends_list = chat_logs + "\\" + logged_user_phone + "_" + logged_user + "\\friends_list.txt";
								String[] readed_friends_list = my_db.Readfriendslist(your_friends_list).split(":");
								//								System.out.print(your_friends_list);
								//								System.out.println(my_db.Readfriendslist(your_friends_list));
								for (int y=0; y<readed_friends_list.length; y++) {
									String friends_name_ = readed_friends_list[y].split("--")[0];
									String friends_cont_ = readed_friends_list[y].split("--")[1];
									String log_file_of_each_friend_path = chat_logs + "\\" +your_num + "_" + you + "\\" + friend_cont + "_" + your_num + ".log";

									//									System.out.println(log_file_of_each_friend_path);

									String read_content = my_db.ReadFile_any_user_log(log_file_of_each_friend_path); // log sepearted //
									//									System.out.println(read_content);
									if(y==0) {
//										System.out.println(read_content);
									if (read_content.equals("no logs for you")){
										read_content = "    :( No message from " + friends_name_ + " to " + you + " :( ";
//										System.out.println(read_content);
									}

									line2 += read_content + ";";
									}
								}

								ThreadGroup live_or_die = Thread.currentThread().getThreadGroup();
								int num_Threads = live_or_die.activeCount();
								Thread[] lstThreads = new Thread[num_Threads];
								live_or_die.enumerate(lstThreads);

								String acative_threads_list = "";
								//								System.out.println(live_or_die);
								for (int i = 0; i < num_Threads; i++) {
//									System.out.println("Thread No:" + i + " = " + lstThreads[i].getName());
									String th_name = lstThreads[i].getName();
									acative_threads_list += th_name;
								}

								String avilable_friend = friend_user + "--" + friend_cont;
								line2 += ",l@!#$%og_history," + you + ",l@!#$%og_history," + friend_user;

								if (acative_threads_list.contains(avilable_friend)) {
									line2 += ",l@!#$%og_history," + " is online :-) "; 
								}
								else if (!acative_threads_list.contains(avilable_friend)){
									line2 += ",l@!#$%og_history," + " is offline :-& "; 
								}
								out.println(line2);
							}


							/// If the conversation is not bye ////

							else {

								String received_message = you + " : " + message;

								all_recent_logs_by_you += message + ";";  		   /// addition of all the typed messages log ////

								my_utils.CreateTempfile(log_file, received_message);
								String encrypted_message = "@$chiatanlmdal#%^&(*(102912012373eqwkewqo" + "," + friend_user + "," + message + "," + friend_cont+ "," + you + ","  + your_num;
								//								System.out.println(encrypted_message);
								out.println(encrypted_message);
							}
						}
					}

			}

				//// Process the first input ////

				else if (inputLine.length() == 1) {
					String outputLine = ccp.processInput(inputLine);
					out.println(outputLine);
				} 

			}

			socket.close();
		}

		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
