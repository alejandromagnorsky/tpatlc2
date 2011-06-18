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

public class Automata {

	protected class State {
		public String name;
		public Set<Arc> adj;

		public State(String name) {
			this.name = name;
			this.adj = new HashSet<Arc>();
		}
		
		public String getName(){
			return name;
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
		public Integer symbol;
		public String action;
		public State neighbor;

		public Arc(Integer number, String action, State neighbor) {
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

	// Para ubicar los nodos rápidamente
	protected HashMap<String, State> states;
	
	// Para recorrer la lista de nodos
	protected List<State> stateList; 

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

	public boolean addArc(String v, String w, Integer symbol, String action) {
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

	public State getFirstState() {
		return stateList.get(0);
	}

	public String getNeighbor(String name, Integer symbol) {
		State state = states.get(name);
		if (state == null || symbol == null)
			return null;
		for (Arc arc : state.adj)
			if (arc.symbol.equals(symbol))
				return arc.neighbor.name;
		return null;
	}

	public String getAction(String name, Integer symbol) {
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
	
	public String execute(String tape){
		State initial = getFirstState();
		return execute(tape, initial, 0);
	}
	
	private String execute(String tape, State q, int index){
		int symbolInTape;
		if (index < 0 || tape.length() <= index)
			symbolInTape = 0;
		else
			symbolInTape = Integer.valueOf(tape.charAt(index) + "");
		
		Arc destination = null;
		for (Arc e : q.adj)
			if (e.symbol.intValue() == symbolInTape){
				destination = e;
				break;
			}
		
		if(destination == null)
			return tape;
		
		if (destination.action.equals(">")){
			if (index+1 >= tape.length())
				tape = tape + "0";
			return execute(tape, destination.neighbor, index+1);
		} else if (destination.action.equals("<")){
			if (index-1 < 0)
				tape = "0" + tape;
			return execute(tape, destination.neighbor, 0);
		} else {
			if (index+1 <= tape.length())
				tape = tape.substring(0, index) + destination.action + tape.substring(index+1);
			else
				tape = tape.substring(0, index) + destination.action;
			return execute(tape, destination.neighbor, index);
		}
	}
}
