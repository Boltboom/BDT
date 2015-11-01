package Display;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Reader {
	
	static InputStream fis;
	static BufferedReader br;
	static String line;
	static String lastPath = "";
	
	public static String[] readAllLines(String path) {
		lastPath = path;
		String[] lines = new String[0];
		String[] temp = new String[0];
		int index = 0;
		try {
			fis = new FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				index++;
				temp = lines;
				lines = new String[index];
				for(int i = 0; i < index-1; i++) {
					lines[i] = temp[i];
				}
				lines[index-1] = line;
			}
			return lines;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public static String readLine(int n, String path) {
		lastPath = path;
		String s = null;
		int r = 0;
		try {
			fis = new FileInputStream(path);	
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {
				if(r == n) {
					s = line;
				}
				r++;
			}
			return s;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}