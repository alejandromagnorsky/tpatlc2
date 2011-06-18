package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AutomataLoader {

	public static Automata load(String filename) throws IOException {
		BufferedReader reader = null;
		Automata automata = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String origin, destination, action, line;
			int number;
			automata = new Automata();
			while ((line = reader.readLine()) != null) {
				if (!line.matches("[a-zA-Z0-9]+,[0-2],[a-zA-Z0-9]+,[<>0-2]")) {
					System.err.println("/!\\ Invalid file format.");
					System.exit(1);
				}

				String[] args = line.split(",");
				origin = args[0];
				number = Integer.valueOf(args[1]);
				destination = args[2];
				action = args[3];

				automata.addState(origin);
				automata.addState(destination);
				if (!automata.addArc(origin, destination, number, action)) {
					System.err.println("/!\\ Invalid automata.");
					System.exit(1);
				}
			}
		} catch (IOException e) {
			System.err.println("/!\\ File not found.");
			System.exit(1);
		} finally {
			if (reader != null)
				reader.close();
		}
		return automata;
	}
}
