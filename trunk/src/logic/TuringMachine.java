package logic;

import java.io.IOException;

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

		Automata<Integer> automata = null;
		try {
			automata = AutomataLoader.load(filename);
		} catch (IOException e) {
			System.err.println("/!\\ Error reading file.");
			System.exit(1);
		}

		String q0 = automata.getFirstState();
		System.out.println(q0);
		System.out.println(automata.getAction(q0, 1));
		String q1 = automata.getNeighbor(q0, 0);
		System.out.println(automata.getAction(q1, 1));
		System.out.println(automata.getNeighbor(q1, 1));

		if (generateDot)
			try {
				automata.export(filename);
			} catch (IOException e) {
				System.err.println("/!\\ Error writing file.");
				System.exit(1);
			}

	}
}
