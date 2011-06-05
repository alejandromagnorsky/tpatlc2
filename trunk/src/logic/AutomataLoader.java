package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AutomataLoader {

	public static Automata<Integer> load(String filename) throws IOException {
		BufferedReader reader = null;
		Automata<Integer> automata = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String origin, destination, action, line;
			Integer number, beginIndex, endIndex;
			automata = new Automata<Integer>();
			while ((line = reader.readLine()) != null) {
				if (!line.matches("[a-zA-Z0-9]+,[0-2],[a-zA-Z0-9]+,[<>0-2]")) {
					System.err.println("/!\\ Invalid file format.");
					System.exit(1);
				}

				beginIndex = 0;
				endIndex = line.indexOf(',', beginIndex);
				origin = line.substring(beginIndex, endIndex);
				beginIndex = endIndex + 1;
				endIndex = line.indexOf(',', beginIndex);
				number = Integer.valueOf(line.substring(beginIndex, endIndex));
				beginIndex = endIndex + 1;
				endIndex = line.indexOf(',', beginIndex);
				destination = line.substring(beginIndex, endIndex);
				beginIndex = endIndex + 1;
				endIndex = line.length();
				action = line.substring(beginIndex, endIndex);

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
