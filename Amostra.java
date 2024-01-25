package Project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;



public class Amostra implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<int []> lista; // Lista de amostras
	public int [] domain = null;    // Array de domínios, deve ser actualizado no método add.

	public Amostra() { //cria a lista de arrays
		this.lista = new ArrayList<int []>();
	}

	static int [] convert (String line) {
		String cvsSplitBy = ",";
		String[] strings     = line.split(cvsSplitBy);
		int[] stringToIntVec = new int[strings.length];
		for (int i = 0; i < strings.length; i++)
			stringToIntVec[i] = Integer.parseInt(strings[i]);
		return stringToIntVec;
		}
	
	public Amostra(String csvFile) {
		this.lista = new ArrayList<int []>();
		this.domain = null;
		BufferedReader br = null;
		String line = "";
		

		try {br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {	
				add(convert(line));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void add (int[] v){
		if (this.domain==null)
			this.domain = v.clone();
		for (int i = 0; i < v.length; i++) {
			if(this.domain[i]<=v[i]+1) {
				this.domain[i]=v[i]+1;
			}		
		}
		lista.add(v);
	}
	
	public int length (){
		return lista.size();
	}
	
	public int [] element (int i){
		return lista.get(i);
	}
	
	
	public int count(int[] var, int[] val){
		int r=0;
		int n=var.length;
		for (int i = 0; i < this.length(); i++) {
			boolean q=true;
			int u=0;
			while (u<n && q) {
				if (this.element(i)[var[u]]!=val[u]) {
				q=false;	
				}
				u++; 
			}
			if (q) r+=1;	
		}
		return r;
	}
	
	public int count(LinkedList<Integer> var, LinkedList<Integer> val) {
		int r=0;
		int n=var.size();
		for (int i = 0; i < this.length(); i++) {
			boolean q=true;
			int u=0;
			while (u<n && q) {
				if (this.element(i)[var.get(u)]!=val.get(u)) {
				q=false;	
				}
				u++; 
			}
			if (q) r+=1;	
		}
		return r;
	}
	
	public double [] freqam(int i) {
		double res [] = new double [this.domain[i]];
		double m=this.length();
		for (int o=0; o<this.domain[i];o++) {
			int[] var = {i};
			int[] val = {o};
			res[o]=this.count(var, val)/m;
		}
		return res;
	}

	public int domain (int var[]){
		int r=1;
		for (int i=0; i<var.length; i++) {
			r*=this.domain[var[i]];
		}
		return r;
	}
	
	public int domain(LinkedList<Integer> var) {
		int r=1;
		if (var.size()==0) return 1;
		for (int i=0; i<var.size(); i++) {
			r*=this.domain(var.get(i));
		}
		return r;
	}
	
	
	public int domain(int i) {
		return this.domain[i];
	}
	
	@Override
	public String toString() {
		String s="\n[\n";
		if (lista.size()>0) s+=Arrays.toString(lista.get(0));
		for (int i=1; i<lista.size();i++)
			s+="\n"+Arrays.toString(lista.get(i));
		s+="\n]";
		
		return "Amostra " + s;
	}
	
	public void write(String path) {
		try {
			FileOutputStream fs = new FileOutputStream(path);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Amostra read(String path) {
		try {
			FileInputStream fs = new FileInputStream(path);
			ObjectInputStream os = new ObjectInputStream(fs);
			Amostra res = (Amostra) os.readObject();
			os.close();
			return res;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}


