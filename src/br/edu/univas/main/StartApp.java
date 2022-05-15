package br.edu.univas.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StartApp {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Nome do arquivo a ser lido:");
		String fileName = in.nextLine();
		
		String envVarPath = System.getenv("CSV_FILE");// env var's name
		File completePath = new File(envVarPath);//access directory
		List<String> fileContent = new ArrayList();
		String names = "";
		int op;
		for(File f: completePath.listFiles()) {//encontra o arquivo a ser lido TODO: ENVELOPAR NO TRY
			//System.out.println(f); //arquivos existentes
			if(f.getName().equalsIgnoreCase(fileName+".csv")) {//search the file
				if(f.canRead()) {
					fileContent = readFile(f);
					break;
				}
			}
			
		}
		if(fileContent.size() == 0) {
			System.out.println("File not found.");
		}else {
			op = menu(fileContent);
			if(op == 0) {
				System.out.println("See ya!");
			}else {
				System.out.println("Realizar chamada da matéria: "+ fileContent.get(op - 1)+"\n	ATENÇÃO: Digite 9 para encerrar chamada.");
				names = getNames(fileContent.get(op - 1));
			}
			createFile(names, envVarPath, fileContent.get(op - 1));
		}
		in.close();
	}
	
	public static List<String> readFile(File readable) {
		Scanner readLines;
		List<String> fileContent = new ArrayList();
		try {
			readLines = new Scanner(readable);
			//readLines.useDelimiter(";");
			while(readLines.hasNextLine()) {
				String host = readLines.nextLine();
				String[] hostA = host.split("\n|;");
				for(String e: hostA) {
					fileContent.add(e);
				}	
			}
		} catch (FileNotFoundException e) {//na funcao anterior a esta, ja tem um tratamento para isso
			// TODO: handle exception
			System.out.println("File not found.");
		}
		return fileContent;
		
		
	}
	public static int menu(List<String> subjects) {
		System.out.println(":::CHAMADA:::");
		for(int i = 0; i < subjects.size(); i++) {
			System.out.println("	["+(i + 1)+"] "+subjects.get(i));
		}
		System.out.println("	[0] Sair");
		int op;
		do {
			System.out.print(">>");
			op = readInt();
			if(op == 0) {
				break;
			}
		}while(op < 0 || op > subjects.size());
		return op;
		
	}
	public static int readInt() {
		Scanner in = new Scanner(System.in);
		int op = in.nextInt();
		in.nextLine();
		return op;
	}
	public static String getNames(String subject) {
		Scanner in = new Scanner(System.in);
		String entry;
		String names = "";
		do {
			System.out.print(">>");
			entry = in.nextLine();
			if(entry.equals("9")) {
				break;
			}else {
				names += entry +"\n";
			}
		}while(true);
		//System.out.println("finalizada chamada.");
		return names;
	}
	public static void createFile(String names, String envVarPath, String subjectName) {
		String path = envVarPath + "\\"+fileName(subjectName);
		File newFile = new File(path);
		try {
			FileWriter writtenFile = new FileWriter(path);
			writtenFile.write(names);
			writtenFile.close();
			saveFile(newFile);
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("1");
			System.out.println(e);
		}
	}
	public static void saveFile(File newFile) {
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("2");
			System.out.println(e);
		}
	}
	public static String fileName(String subjectName) {
		subjectName = subjectName.toLowerCase().replace(' ', '_')+"_";
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedCurrentDate = currentDate.format(formatter).replace('-', '_');
		return subjectName+formattedCurrentDate+".txt";
	}
}
