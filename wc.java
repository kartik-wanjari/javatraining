import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * Copyright (c) 2022.  - All Rights Reserved
 * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 * is strictly prohibited-
 * @Author -kartiks (Kartik Wanjari)
 */
public class wc {
	/**
	 * This method print line count, word count and character count of file.
	 * @param path -It is used to take the input file path
	 */
	public static void simpleWc(String path) throws IOException {

		File file = new File(path);
		String content = Files.readString(Paths.get(path));			//read and store file as string

		int l = 0;
		Pattern p = Pattern.compile("[a-zA-Z]+");				//alphabetic regular expression
		Matcher m = p.matcher(content);
		ArrayList<String> arr = new ArrayList<>() ;
		

		while (m.find()) {
			arr.add(m.group());
			
		}
		
		FileReader f = new FileReader(path);
		BufferedReader br = new BufferedReader(f);

		while (br.readLine() != null) {								//counting number of lines
			l++;
		}
		

		System.out.print((l == 1 ? 0:l) + "\t");
		System.out.print(arr.size() + "\t");
		System.out.print(content.length() + "\t");
		System.out.print(file.getName());

	}
	/**
	 * This method print line count of file.
	 * @param path -It is used to take the input file path
	 */
	public static void wc_l(String path) throws IOException {
		File file = new File(path);
		FileReader f = new FileReader(path);
		BufferedReader br = new BufferedReader(f);
		int l=0;
		while (br.readLine() != null) {
			l++;

		}
		System.out.print((l ==1?0:l)+"\t");
		System.out.print(file.getName());
	}

	/**
	 * This method print character count of file.
	 * @param path -It is used to take the input file path
	 */
	public static void wc_m(String path) throws IOException {
		File file = new File(path);
		String content = Files.readString(Paths.get(path));
		System.out.print(content.length() + "\t");
		System.out.print(file.getName());
	}

	/**
	 * This method print word count of file.
	 * @param path -It is used to take the input file path
	 */
	public static void wc_w(String path) throws IOException {
		File file = new File(path);
		String content = Files.readString(Paths.get(path));
		Pattern p = Pattern.compile("[a-zA-Z]+");     //pattern for only alphabetic words.
		Matcher m = p.matcher(content);
		ArrayList<String> arr = new ArrayList<>() ;
		

		while (m.find()) {
			arr.add(m.group());
			
		}
		System.out.print(arr.size() + "\t");
		System.out.print(file.getName());
	}

	/**
	 * This method print character count of line with max length of file.
	 * @param path -It is used to take the input file path
	 */
	public static void wc_L(String path) throws IOException {
		File file = new File(path);
		FileReader f = new FileReader(path);
		BufferedReader br = new BufferedReader(f);
		String line;
		String maxLine = "";
		while ((line=br.readLine()) != null) {
			
			if(line.length() > maxLine.length()) {
				maxLine = line;
				
			}


		}
		System.out.print(maxLine.length()+"\t");
		System.out.print(file.getName());
	}

	/**
	 * The Execution of programs starts from here i.e, main() method
	 * @param args - it is used to take the input from the user as command line argument
	 */
	public static void main(String[] args) throws IOException {
		switch(args[0]) {
		case "":
			simpleWc(args[1]);
			break;
		case "-l":
			wc_l(args[1]);
			break;
		case"-m":
			wc_m(args[1]);
			break;
		case "-w":
			wc_w(args[1]);
			break;
		case"-L":
			wc_L(args[1]);
			break;
		}

	}

}
