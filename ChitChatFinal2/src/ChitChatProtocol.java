import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ChitChatProtocol {
	
    
	public String processInput(String theInput){
		String theOutput = null;
		if (theInput == "e1") {
			theOutput = " <<<<<<<<< ======= Welcome to Chit Chat on Mat ======= >>>>>>>>>,"
		+ " <<<<<<< ===== Registered User press \"l\" to login ===== >>>>>>>," + "<<<<<<< ===== New User press \"r\" to register ===== >>>>>>>";
		}
		else if((theInput.toLowerCase()).equals("l")){
			theOutput = " <<<<<<<< ====== Login to Your Account ======= >>>>>>>> , "
					+ "Mobile Number:, Password:";
		}
		else if((theInput.toLowerCase()).equals("r")) {
			theOutput = " <<<<<<<< ======= New User Register ====== >>>>>>>>, "
					+ "Name:, Date of Birth:, Mobile Number:, Gender:, Password: ";
		}
		
		
		return theOutput;
	}


}
