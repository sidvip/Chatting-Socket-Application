import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Utility {


	public void CreateTempfile(String path, String file_cont){
		try { 	   
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true)); 
			out.write(file_cont); 
			out.newLine();
			out.close(); 
		} 
		catch (IOException e) { 
			System.out.println("exception occoured" + e); 
		} 

	}

	public void CreateTempfile_First(String path, String file_cont){
		try { 	   
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true)); 
			out.write(file_cont); 
			out.close(); 
		} 
		catch (IOException e) { 
			System.out.println("exception occoured" + e); 
		} 

	}

	public void CreateEmptyfile(String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
	}


	public void DeleteFile(String path) {
		File file = new File(path);
		file.delete();

	}

	public String checkNull(String field_name, String field) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while (field.equals("")) {
			System.out.println("Do not let it remain empty. As it is hungry :-) "); 
			System.out.print(field_name + "  ");
			field = stdIn.readLine();

		}
		return field;
	}	

	public String check_dob(String field_name, String field) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while ((field.split("-").length != 3) || field.matches("[0-9]+") || !(field.length() == 10)) {
			if (!field.matches("[0-9]+") || !(field.length() == 10)) {
				System.out.println(" Use valid dob and write it in dd-mm-yyyy");
				System.out.print(field_name + "  ");
				field = stdIn.readLine();

			}
			else {
				System.out.println(" Use - in the seperation ");
				System.out.print(field_name + "  ");
				field = stdIn.readLine();

			}
		}
		return field;
	}

	public String check_cont (String field_name, String field) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while(!(field.length() == 10) || !field.matches("[0-9]+")) {
			System.out.println(" Use valid number ");
			System.out.print(field_name + "  ");
			field = stdIn.readLine();
		}
		return field;
	}

	public String check_gender(String field_name, String field) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while(!field.toLowerCase().equals("m") || !field.toLowerCase().equals("f")) {
			System.out.println(" Use \"M\" for male and \"F\" for female");
			System.out.print(field_name + "  ");
			field = stdIn.readLine();
			if (field.equals("m")) {
				break;
			}
			else if (field.equals("f")) {
				break;
			}
		}
		return field;
	}

	public void CreateDirectory(String path) {
		File folder_create = new File(path);
		if (!folder_create.exists()) {
			folder_create.mkdir();
		}

	}

	public String CommandCheck(String command_field, String command) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while(!command.equals("ls_users")) {
			System.out.print(command_field);
			command = stdIn.readLine();
		}

		return command;
	}

	public int ContRespUser(String findIn, String find) {
		String[] arr = findIn.split(",");
		int index = 0;
		for (int i=0; i<arr.length; i++) {
			//			System.out.println(arr[i] + "  " +find + "  " +i);
			if (arr[i].equals(find)) {
				index = i;
			}
		}

		//		System.out.println("Index " + index);

		return index;
	}

	public String getcwd() {
		String path = System.getProperty("user.dir");
		return path;
	}

	public String FriendsCommandCheck(String command_field, String command) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while(!(command.equals("ls_friends") || command.equals("add_friend") || command.equals("active_users") || command.equals("ls_logs"))) {
			System.out.print(command_field);
			command = stdIn.readLine();
		}

		return command;
	}

	public String respcheck(String resp_field, String resp) throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		while(!(resp.equals("begin") || resp.equals("ls_friends") || resp.equals("add_friend"))) {
			System.out.print(resp_field);
			resp = stdIn.readLine();
		}
		return resp;
	}
	
	public void ReadandPrint(String path) {
		try {
			BufferedReader inp = new BufferedReader(new FileReader(path));
			String str;
			while ((str = inp.readLine()) != null) {
				System.out.println(str);
				}

			inp.close();  
		} catch (IOException e) {
		}
	}
}