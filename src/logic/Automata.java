package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Automata<T> {

	protected class State {
		public String name;
		public Set<Arc> adj;

		public State(String name) {
			this.name = name;
			this.adj = new HashSet<Arc>();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final State other = (State) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}

	protected class Arc {
		public T symbol;
		public String action;
		public State neighbor;

		public Arc(T number, String action, State neighbor) {
			this.symbol = number;
			this.action = action;
			this.neighbor = neighbor;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((symbol == null) ? 0 : symbol.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Arc other = (Arc) obj;
			if (symbol == null) {
				if (other.symbol != null)
					return false;
			} else if (!symbol.equals(other.symbol))
				return false;
			return true;
		}
	}

	protected HashMap<String, State> states; // Para ubicar los nodos
	// rápidamente
	protected List<State> stateList; // Para recorrer la lista de nodos

	public Automata() {
		this.states = new HashMap<String, State>();
		this.stateList = new ArrayList<State>();
	}

	public boolean isEmpty() {
		return states.size() == 0;
	}

	public boolean addState(String name) {
		if (!states.containsKey(name)) {
			State node = new State(name);
			states.put(name, node);
			stateList.add(node);
			return true;
		}
		return false;
	}

	public boolean addArc(String v, String w, T symbol, String action) {
		State origin = states.get(v);
		State dest = states.get(w);
		if (origin != null && dest != null && symbol != null) {
			Arc newArc = new Arc(symbol, action, dest);
			for (Arc arc : origin.adj)
				if (arc.equals(newArc))
					return false;

			origin.adj.add(newArc);
			return true;
		}
		return false;
	}

	public String getFirstState() {
		return stateList.get(0).name;
	}

	public String getNeighbor(String name, T symbol) {
		State state = states.get(name);
		if (state == null || symbol == null)
			return null;
		for (Arc arc : state.adj)
			if (arc.symbol.equals(symbol))
				return arc.neighbor.name;
		return null;
	}

	public String getAction(String name, T symbol) {
		State state = states.get(name);
		if (state == null || symbol == null)
			return null;
		for (Arc arc : state.adj)
			if (arc.symbol.equals(symbol))
				return arc.action;
		return null;
	}

	public void export(String fileName) throws IOException {
		BufferedWriter output = null;
		try {
			File file = new File(fileName + ".dot");
			output = new BufferedWriter(new FileWriter(file));
			output.write("digraph { ");
			output.newLine();
			output.write("graph [splines = true] "
					+ "node [height=0.4 shape=circle]");
			output.newLine();

			for (State state : stateList)
				for (Arc arc : state.adj) {
					output.write(state.name + " -> " + arc.neighbor.name
							+ "[label=\" " + arc.symbol.toString() + "/" + arc.action+"\"];");
					output.newLine();
				}

			output.write("}");
		} catch (IOException e) {
			return;
		} finally {
			if (output != null)
				output.close();
		}
	}
}
