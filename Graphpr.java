package Project;

import java.io.Serializable;
//import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Graphpr implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<LinkedList<Integer>> al;
	private int dim; 
	
	
	Graphpr(int d){
		super();
		this.dim = d;
		this.al = new LinkedList<>();
		for(int i=0; i<d; i++) {
			al.add(new LinkedList<>());
		}
	}

	public int getDim() {
		return dim;
	}
	
	public void addEdge(int o, int d) {
		LinkedList<Integer> L = al.get(o);
		if(!L.contains(d)) {
			L.add(d);
		}
	}
	
	public void removeEdge(int o, int d) {
		LinkedList<Integer> L = al.get(o);
		if(L.contains(d)) {
			int i=L.indexOf(d);
			L.remove(i);
		}
	}
	
	public void invertEdge(int o, int d) {
		LinkedList<Integer> L = al.get(o);
		if(L.contains(d)) {
			int i=L.indexOf(d);
			L.remove(i);
			LinkedList<Integer> F = al.get(d);
			F.add(o);
		}
	}
	
	public boolean edgeQ(int o, int d) {
		return al.get(o).contains(d);
	}
	

	@Override
	public String toString() {
		String res = "Graph [dim=" + dim + ", ";
		res += "edges = {";
		for(int o = 0; o < dim; o++) {
			for(int d : al.get(o)) {
				res += o + " -> " + d + ", ";
			}
		}
		res += "}]";
		return res;
	}
	
	public LinkedList<Integer> offspring(int o){
		return al.get(o);
	}
	
	public boolean connectedQ(int o, int d) {
		return this.BFS(o).contains(d);
	}
	
	public LinkedList<Integer> parents(int o){
		LinkedList<Integer> L = new LinkedList<>();
		for(int i=0; i<this.getDim(); i++){
			if (this.edgeQ(i, o)){
				L.add(i);
			}
		}
		return L;
	}
	
	public LinkedList<Integer> BFS(int o){
		/*
		 * to_visit é a fila (queue) com os nós a serem
		 * visitados. visited é a lista dos nós que em algum
		 * ponto já foram adicionados à queue.
		 * Quando um nó é visitado, ele é adicionado ao res
		 * e os seus filhos que não estão no visited são 
		 * adicionados à queue.
		 */
		LinkedList<Integer> res = new LinkedList<>();
		Queue<Integer> to_visit = new LinkedList<>();
		LinkedList<Integer> visited = new LinkedList<>();
		to_visit.add(o);
		visited.add(o);
		while(!to_visit.isEmpty()) {
			int node = to_visit.remove();
			res.add(node);
			for(int child : offspring(node)) {
				if(!visited.contains(child)) {
					to_visit.add(child);
					visited.add(child);
				}
			}
		}
		return res;
	}
	
	public boolean my_BFS(int my_node){
		LinkedList<Integer> res = new LinkedList<>();
		Queue<Integer> to_visit = new LinkedList<>();
		LinkedList<Integer> visited = new LinkedList<>();
		to_visit.add(my_node);
		boolean notcycle = true;
		visited.add(my_node);
		while(!to_visit.isEmpty() && notcycle) {
			int node = to_visit.remove();
			res.add(node);
			for(int child : this.offspring(node)) {
				if (my_node == child) notcycle=false;
				
				if(!visited.contains(child)) {
					to_visit.add(child);
					visited.add(child);
				}
			}
		}
		return !notcycle;
	}
	
	
	public boolean cycleQ() {
		boolean notcycle = true;
		int i=0;
		while(i<dim && notcycle) {
			if (my_BFS(i)) notcycle= false;
			i++;
		}
		return !notcycle;
	}
	
	public boolean cycleQ(int o, int u, int op) {//para confirmar se as operações no mdldlelta são possíveis
		boolean notcycle = true;
		if (op==0) notcycle=true;//impossivel criar ciclo por remoçao de uma aresta
		if (op==1) {
			this.invertEdge(o, u);
			if (my_BFS(o)||my_BFS(u)) notcycle=false;
			this.invertEdge(u, o);
		}
		if (op==2) {
			this.addEdge(o, u);
			if (my_BFS(o)||my_BFS(u)) notcycle=false;
			this.removeEdge(o, u);
		}
		return !notcycle;
	}

	
	public double IT(int i, Amostra am) {
		double it=0;//resultado
		double m = am.length();
		LinkedList<Integer> p = this.parents(i);
		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);//tirar o 10 da lista dos pais
		if (p.isEmpty()) return it;//se nao tiver pais, dá 0
		for (int o=0; o<am.domain(i); o++) {//dominio da variavel
			for (int u=0;u<am.domain(p.get(0)); u++) {//dominio do pai 1 da variavel
				for (int k=0; k<am.domain(this.getDim()-1); k++) {//dominio da classe
					double c;
					int [] varc = {this.getDim()-1};
					int [] valc = {k};
					c=am.count(varc, valc);
					double prt2=c/m; //fator que nao depende do numero do pais
					int[] vard3 = {i,this.getDim()-1};
					int[] vald3 = {o,k};
					double prt3=am.count(vard3, vald3)/m; //fator que nao depende do numero do pais
					double prt1=0;
					double prt4=0;
					if (p.size()==1) { //1 pai
					int[] vard1 = {i,p.get(0),this.getDim()-1};
					int[] vald1 = {o,u,k};
					prt1=am.count(vard1, vald1)/m;
					int[] vard4 = {p.get(0),this.getDim()-1};
					int[] vald4 = {u,k};
					prt4=am.count(vard4, vald4)/m;
					if (((prt1*prt2)/(prt3*prt4)==0&&prt1==0)||prt3*prt4==0) it+=0;
					else it+=prt1*((Math.log((prt1*prt2)/(prt3*prt4)))/Math.log(2));
					}
					if (p.size()==2) { //2 pais
						for (int j=0; j<am.domain(p.get(1)); j++) {
							int[] vard1 = {i,p.get(0),p.get(1),this.getDim()-1};
							int[] vald1 = {o,u,j,k};
							prt1=am.count(vard1, vald1)/m;
							int[] vard4 = {p.get(0),p.get(1),this.getDim()-1};
							int[] vald4 = {u,j,k};
							prt4=am.count(vard4, vald4)/m;
							if (((prt1*prt2)/(prt3*prt4)==0&&prt1==0)||prt3*prt4==0) it+=0;
							else it+=prt1*((Math.log((prt1*prt2)/(prt3*prt4)))/Math.log(2));
						}
						
					}
				}
			}
		}
		
		return it;
	}
	
	private double IT(int i, Amostra am, LinkedList<Integer> p) { //para utilizar com lista de pais especificas (MDLDelta)
		double it=0;//resultado
		double m = am.length();
		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);//tirar o 10 da lista dos pais
		if (p.isEmpty()) return it;//se nao tiver pais, dá 0
		for (int o=0; o<am.domain(i); o++) {//dominio da variavel
			for (int u=0;u<am.domain(p.get(0)); u++) {//dominio do pai 1 da variavel
				for (int k=0; k<am.domain(this.getDim()-1); k++) {//dominio da classe
					double c;
					int [] varc = {this.getDim()-1};
					int [] valc = {k};
					c=am.count(varc, valc);
					double prt2=c/m; //fator que nao depende do numero do pais
					int[] vard3 = {i,this.getDim()-1};
					int[] vald3 = {o,k};
					double prt3=am.count(vard3, vald3)/m; //fator que nao depende do numero do pais
					double prt1=0;
					double prt4=0;
					if (p.size()==1) { //1 pai
					int[] vard1 = {i,p.get(0),this.getDim()-1};
					int[] vald1 = {o,u,k};
					prt1=am.count(vard1, vald1)/m;
					int[] vard4 = {p.get(0),this.getDim()-1};
					int[] vald4 = {u,k};
					prt4=am.count(vard4, vald4)/m;
					if ((prt1*prt2)/(prt3*prt4)==0||prt1==0||(prt3*prt4)==0) it+=0;
					else it+=prt1*((Math.log((prt1*prt2)/(prt3*prt4)))/Math.log(2));
					}
					if (p.size()==2) { //2 pais
						for (int j=0; j<am.domain(p.get(1)); j++) {
							int[] vard1 = {i,p.get(0),p.get(1),this.getDim()-1};
							int[] vald1 = {o,u,j,k};
							prt1=am.count(vard1, vald1)/m;
							int[] vard4 = {p.get(0),p.get(1),this.getDim()-1};
							int[] vald4 = {u,j,k};
							prt4=am.count(vard4, vald4)/m;
							if ((prt1*prt2)/(prt3*prt4)==0||prt1==0||(prt3*prt4)==0) it+=0;
							else it+=prt1*((Math.log((prt1*prt2)/(prt3*prt4)))/Math.log(2));
						
						}
						
					}
				}
			}
		}
		
		return it;
	}
	
	public double LLGT(Amostra am) {
		double ll=0;
		for (int i=0; i<this.getDim()-1; i++) {
			ll+=this.IT(i, am);
		}
		ll*=am.length();
		return ll;
	}
	 
	public double [] theta(int i, Amostra am, double s) {
		LinkedList<Integer> p = this.parents(i);
		int t = p.size();
 		double [] th = new double [am.domain(p)*am.domain(i)];
		int cont=0;
		for (int o=0; o<am.domain(i); o++) { //dominio da variavel
			if (t==0) {
				LinkedList<Integer> val = new LinkedList<Integer>();
				th[cont]=this.thetaaux(am, i, p, val, o, s);
				cont++;
			}
			else {
				for (int p1=0; p1<am.domain(p.get(0)); p1++) {//tem sempre pelo menos um pai
					if (t==1) {
						LinkedList<Integer> val = new LinkedList<Integer>();
						val.add(p1);
						th[cont]=this.thetaaux(am, i, p, val, o, s);
						cont++;	
					}
					else if (t>=2) {
						for (int p2=0; p2<am.domain(p.get(1)); p2++){
							if (t==2) {
								LinkedList<Integer> val = new LinkedList<Integer>();
								val.add(p1);
								val.add(p2);
								th[cont]=this.thetaaux(am, i, p, val, o, s);
								cont++;	
							}
							else if (t==3) { //para 3 pais
								for (int p3=0; p3<am.domain(p.get(2)); p3++){
									LinkedList<Integer> val = new LinkedList<Integer>();
									val.add(p1);
									val.add(p2);
									val.add(p3);
									th[cont]=this.thetaaux(am, i, p, val, o, s);
									cont++;	
									
								}
							}
						}
					}
				}
			}	
		}
		return th;
	}
	
	
	public double thetaaux(Amostra am, int i, LinkedList<Integer> varp, LinkedList<Integer> valp, int o, double s) {
		double ta=0;
		double twi= am.count(varp, valp);
		varp.add(i);//adicionar variavel i e o seu valor aos vatores pais 
		valp.add(o);
		double tdiwi = am.count(varp, valp);
		varp.removeFirstOccurrence(i);//desfazer adições
		valp.removeFirstOccurrence(o);
		ta=(tdiwi+s)/(twi+s*am.domain(i));
		
		return ta;
	}
	
	public String ThetaShow(Amostra am, double s) {
		String res="Valores theta:\n";
		int cont=0;
		for (int i=0; i<this.getDim(); i++) {
			
			LinkedList<Integer> p = this.parents(i);
			int t = p.size();
			for (int o=0; o<am.domain(i); o++) { //dominio da variavel
				if (t==0) {
					LinkedList<Integer> val = new LinkedList<Integer>();
					res+="Cont="+cont+", i="+i+", di="+o+", parents=null, valparents=[], Theta="+this.thetaaux(am, i, p, val, o, s)+"\n";
					cont++;
				}
				else {
					for (int p1=0; p1<am.domain(p.get(0)); p1++) {//tem sempre pelo menos um pai
						if (t==1) {
							LinkedList<Integer> val = new LinkedList<Integer>();
							val.add(p1);
							res+="Cont="+cont+", i="+i+", di="+o+", parents="+p+", valparents="+val+", Theta="+this.thetaaux(am, i, p, val, o, s)+"\n";
							cont++;	
						}
						else if (t>=2) {
							for (int p2=0; p2<am.domain(p.get(1)); p2++){
								if (t==2) {
									LinkedList<Integer> val = new LinkedList<Integer>();
									val.add(p1);
									val.add(p2);
									res+="Cont="+cont+", i="+i+", di="+o+", parents="+p+", valparents="+val+", Theta="+this.thetaaux(am, i, p, val, o, s)+"\n";
									cont++;	
								}
								else if (t==3) { //para 3 pais
									for (int p3=0; p3<am.domain(p.get(2)); p3++){
										LinkedList<Integer> val = new LinkedList<Integer>();
										val.add(p1);
										val.add(p2);
										val.add(p3);
										res+="Cont="+cont+", i="+i+", di="+o+", parents="+p+", valparents="+val+", Theta="+this.thetaaux(am, i, p, val, o, s)+"\n";
										cont++;	
										
									}
								}
							}
						}
					}
				}
			}
		}
		return res;
	}
	
	private double mdlaux(Amostra am) {
		double r=0;
		for (int i=0; i<this.getDim()-1; i++) {
			LinkedList<Integer> p = this.parents(i);
			if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);
			r+=(am.domain(i)-1)*am.domain(p)*am.domain(this.getDim()-1);
			//System.out.println(r);
		}
		r+=am.domain(this.getDim()-1)-1;//stor PM disse que isto nao era preciso mas pus para dar igual ao que está no fenix
		return r;
	}
	
	public double MDL(Amostra am) {
		double mdl=0;
		double m=am.length();
		mdl=(Math.log(m)/(Math.log(2)*2))*this.mdlaux(am)-this.LLGT(am);
		return mdl;
	}
	
	public double MDLcont(Amostra am, int i, LinkedList<Integer> p) {
		double res=0;
		double m=am.length();
		double t=0;
		t=(am.domain(i)-1)*am.domain(p)*am.domain(this.getDim()-1);
		res=(Math.log(m)/(Math.log(2)*2))*t-this.IT(i, am, p)*m;
		return res;
	}
	
	public double MDLdelta(Amostra am, int i, int u, int op) throws Exception { 
		double res =0;
		if (op==0) { //remover
			if (i==this.getDim()-1) throw new Exception("Remoção Impossível: Classe C não pode ser removida");
			else if (!this.edgeQ(i, u)) throw new Exception("Remoção Impossível: Aresta inexistente");
			else {
				LinkedList<Integer> p = this.parents(u);
        		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);
        		double d=this.MDLcont(am, u, p);//Contributo do nó sem alteração
        		p.removeFirstOccurrence(i);
        		res=this.MDLcont(am, u, p)-d;//diferença entre contributos
			}
		}
    	if (op==1) {//inverter
    		if (i==this.getDim()-1) throw new Exception("Inversão Impossível: Classe C não pode ser filha");
    		else if (!this.edgeQ(i, u)) throw new Exception("Inversão Impossível: Aresta inexistente");
    		else if (this.cycleQ(i, u, op)) throw new Exception("Inversão Impossível: Operação impossível/Ciclos");
    		else {
    			LinkedList<Integer> p1 = this.parents(u);
        		if (p1.contains(this.getDim()-1)) p1.removeFirstOccurrence(this.getDim()-1);
        		LinkedList<Integer> p2 = this.parents(i);
        		if (p2.contains(this.getDim()-1)) p2.removeFirstOccurrence(this.getDim()-1);
        		if (p2.size()==2) throw new Exception("Iversão: Impossível:Pais a mais");
        		double d=this.MDLcont(am, u, p1)+this.MDLcont(am, i, p2);//Contributo dos nós sem alteração
        		p1.removeFirstOccurrence(i);
        		p2.add(u);
        		res=this.MDLcont(am, u, p1)+this.MDLcont(am, i, p2)-d;//diferença entre contributos
    		}
    	}
    	if (op==2) {//inserir
    		if (u==this.getDim()-1) throw new Exception("Inserção Impossível: Classe C não pode ser filha");
    		else if (this.edgeQ(i, u)) throw new Exception("Inserção Impossível: Aresta já existe");
    		else if (this.cycleQ(i, u, op)) throw new Exception("Inserção Impossível: Operação impossível/Ciclos");
    		else {
    			LinkedList<Integer> p = this.parents(u);
        		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);
        		if (p.size()==2) throw new Exception("Inserção Impossível: Pais a mais");
        		double d=this.MDLcont(am, u, p);//Contributo do nó sem alteração
        		p.add(i);
        		res=this.MDLcont(am, u, p)-d;//diferença entre contributos
    		}
    	}
		return res;
	}
	
	public double MDLdeltaQ(Amostra am, int i, int u, int op) { 
		double res =0;
		if (op==0) { //remover
			if (i==this.getDim()-1) return 0;
			else if (!this.edgeQ(i, u)) return 0;
			else {
				LinkedList<Integer> p = this.parents(u);
        		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);
        		double d=this.MDLcont(am, u, p);//Contributo do nó sem alteração
        		p.removeFirstOccurrence(i);
        		res=this.MDLcont(am, u, p)-d;//diferença entre contributos
			}
		}
    	if (op==1) {//inverter
    		if (i==this.getDim()-1) return 0;
    		else if (!this.edgeQ(i, u)) return 0;
    		else if (this.cycleQ(i, u, op)) return 0;
    		else {
    			LinkedList<Integer> p1 = this.parents(u);
        		if (p1.contains(this.getDim()-1)) p1.removeFirstOccurrence(this.getDim()-1);
        		LinkedList<Integer> p2 = this.parents(i);
        		if (p2.contains(this.getDim()-1)) p2.removeFirstOccurrence(this.getDim()-1);
        		if (p2.size()==2) return 0;
        		double d=this.MDLcont(am, u, p1)+this.MDLcont(am, i, p2);//Contributo dos nós sem alteração
        		p1.removeFirstOccurrence(i);
        		p2.add(u);
        		res=(this.MDLcont(am, u, p1)+this.MDLcont(am, i, p2))-d;//diferença entre contributos
    		}
    	}
    	if (op==2) {//inserir
    		if (u==this.getDim()-1) return 0;
    		else if (this.edgeQ(i, u)) return 0;
    		else if (this.cycleQ(i, u, op)) return 0;
    		else {
    			LinkedList<Integer> p = this.parents(u);
        		if (p.contains(this.getDim()-1)) p.removeFirstOccurrence(this.getDim()-1);
        		if (p.size()==2) return 0;
        		double d=this.MDLcont(am, u, p);//Contributo do nó sem alteração
        		p.add(i);
        		res=this.MDLcont(am, u, p)-d;//diferença entre contributos
    		}
    	}
		return res;
	}
}

