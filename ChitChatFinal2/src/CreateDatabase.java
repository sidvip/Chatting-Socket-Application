import java.io.IOException;

public class CreateDatabase {

	public static void main(String[] args) throws IOException {
		Utility my_utils = new Utility();
		
		String db_dir = my_utils.getcwd() + "\\Chat_Database";
		String chat_logs = db_dir + "\\Chat_logs";

		my_utils.CreateDirectory(db_dir);
		my_utils.CreateDirectory(chat_logs);
		String db_entries = db_dir + "\\all_users.txt";
		
		my_utils.CreateTempfile_First(db_entries, "Name,DOB,Contact,Gender,Password");

	}

}
