package Display;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	static String Replace(String wholestring,String old,String newone) {
		String single=" ";
		 String Before=""; // Contains string after the
		 String After="";
		 boolean spaceexist=false;

		int temp = 0;
		 for(int i=0; i<wholestring.length();i++)
		 {
			 temp=i;
			 int oldindex=0;                             // Where we are in the 
			 boolean checkfirstletter=wholestring.charAt(temp)==old.charAt(oldindex);
			 while(checkfirstletter & (oldindex!=(old.length())))
			 {
				 if((temp+1)!=wholestring.length())
				 {
					 spaceexist=(wholestring.charAt(temp+1)==single.charAt(0));;
				 }
				 else
				 {
					spaceexist=false;
				 }
	          
				 if(oldindex==(old.length()-1) & spaceexist)  //The letters all match
				 {	
							After=wholestring.substring(temp+1, wholestring.length());
							Before=wholestring.substring(0,i);
							wholestring=Before+newone+After;  //Making the new string.
							break;
				 }

				 if((temp+1)==wholestring.length() | (oldindex+1)==old.length() )
				 {
					 break;
				 }
				 else
				 {
					 temp++; 
					 oldindex++;
					 checkfirstletter=wholestring.charAt(temp)==old.charAt(oldindex);;
				 }

			 }
		 }
		return wholestring;
	}
	//Assumes replace_old and replace_new are same size
	public static String[] Replace_Complete(String[] completeText, String replace_old, String replace_new) {
		String[] newText = new String[completeText.length];
		for(int i = 0; i < completeText.length; i++) {
			if(completeText[i] != null && !completeText[i].equals("")) {
				newText[i] = Replace(completeText[i], replace_old, replace_new);
			}
		}
		return newText;
	}
		
	public static void write(String[] newText, String name) throws IOException {
		File newDoc = new File("files/" + name + ".txt");
		if(!newDoc.exists()) {
			newDoc.createNewFile();
		}
		FileWriter fwriter = new FileWriter(newDoc.getAbsoluteFile());
		BufferedWriter bwriter = new BufferedWriter(fwriter);
		for(String a : newText) {
			if(a == null) {
				bwriter.write("\n");
			} else {
				bwriter.write(a + "\n");
			}
		}
		bwriter.close();
	}
}
