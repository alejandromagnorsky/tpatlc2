package logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TuringMachine {

	public static void main(String args[]) {
		boolean generateDot = false;
		String filename = null;
		for (String arg : args) {
			if (arg.equals("-g"))
				generateDot = true;
			else if (filename == null)
				filename = new String(arg);
			else {
				System.err.println("/!\\ Invalid argument sequence.");
				System.exit(1);
			}
		}
		if (filename == null) {
			System.err.println("/!\\ Invalid argument sequence.");
			System.exit(1);
		}

		Automata automata = null;
		try {
			automata = AutomataLoader.load(filename);
		} catch (IOException e) {
			System.err.println("/!\\ Error reading file.");
			System.exit(1);
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
		String tape = null;
		try {
			tape = reader.readLine();
			if( tape == null)
				tape = "";
		} catch(IOException e) {
			System.err.println("/!\\ Error reading from input.");
			System.exit(1);
		}
		
		System.out.println(automata.execute(tape));
		
		if (generateDot)
			try {
				automata.export(filename);
			} catch (IOException e) {
				System.err.println("/!\\ Error writing file.");
				System.exit(1);
			}

	}
}
