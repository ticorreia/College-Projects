package Project;



import java.util.concurrent.ThreadLocalRandom;


public class BNLearn {
	private Amostra am;
	private int dim;
	public double mmdl;
	public Graphpr mgraph;
	private Graphpr gr;
	
	public BNLearn (int n, Amostra amostra) throws Exception {
		long start2 = System.currentTimeMillis();
		this.am=amostra;
		this.dim=this.am.element(0).length;
		//this.LLG = new LinkedList<Graphpr>();
		//CRIAR GRAFOS ALEATÓRIOS
		Graphpr go = new Graphpr(dim);
		for (int i=0; i<go.getDim()-1; i++) go.addEdge(go.getDim()-1, i);
		System.out.println("2"+go.toString()+"; mdl1="+go.MDL(amostra));
		
		this.mdlfind(go);
		System.out.println("2"+go.toString()+"; mdl2="+go.MDL(amostra));
		this.mmdl=go.MDL(amostra);
		this.mgraph=go;

		for (int i=0; i<n-1; i++) {
			this.gr = new Graphpr(dim);
			for (int o = 0; o<gr.getDim()-1; o++) {
				gr.addEdge(gr.getDim()-1, o);
				int p=ThreadLocalRandom.current().nextInt(0,3);
				for (int j=0; j<p; j++) {
					while (gr.parents(o).size()!=p) {
						int m = ThreadLocalRandom.current().nextInt(0,gr.getDim()-1);
						if (!gr.parents(o).contains(m)&&m!=o&&!gr.cycleQ(m,o,2)) {
							gr.addEdge(m, o);
						}
					}
				}
			}
			boolean min = false;
			while (!min) {//otimiza o mdl até nao haver melhoramento
			this.mdlfind(gr);
				if (gr.MDL(amostra)<this.mmdl) {
					this.mmdl=gr.MDL(amostra);
					this.mgraph=gr;
				}
				else {
					min=true;
				}
			}

			System.out.println("Loading...");//para ver progresso
		}	
		  long end2 = System.currentTimeMillis();      
	      System.out.println("Elapsed Time in milli seconds: "+ (end2-start2));
	}
	
	public void mdlfind(Graphpr gr) throws Exception { //otimizar mdl
		for (int i=0; i<gr.getDim()-1; i++) {
			for (int o=0; o<gr.getDim()-1; o++) {
				double [] alt = new double [3];
				
				if (i!=o) {
					if (!gr.edgeQ(i, o)) {
						try {
						alt[2]=gr.MDLdelta(am, i, o, 2);
						alt[1]=Double.POSITIVE_INFINITY;
						alt[0]=Double.POSITIVE_INFINITY;
						
						} catch (Exception e) {
							alt[2]=Double.POSITIVE_INFINITY;
							alt[1]=Double.POSITIVE_INFINITY;
							alt[0]=Double.POSITIVE_INFINITY;
						}
					}
					else {
						alt[2]=Double.POSITIVE_INFINITY;
						try {
						alt[1]=gr.MDLdelta(am, i, o, 1);
						} catch (Exception e){
							alt[1]=Double.POSITIVE_INFINITY;
							try {
								alt[0]=gr.MDLdelta(am, i, o, 0);
								} catch (Exception f) {
									alt[0]=Double.POSITIVE_INFINITY;
									alt[2]=Double.POSITIVE_INFINITY;
								}
						}
						try {
						alt[0]=gr.MDLdelta(am, i, o, 0);
						} catch (Exception e) {
							alt[0]=Double.POSITIVE_INFINITY;
							try {
								alt[1]=gr.MDLdelta(am, i, o, 1);
								} catch (Exception f){
									alt[1]=Double.POSITIVE_INFINITY;
									alt[2]=Double.POSITIVE_INFINITY;
								}
						}
					}
				double[] alte = this.min(alt);
				if (alte[0]<0) {
					if (alte[1]==0) gr.removeEdge(i, o);
					else if (alte[1]==1) gr.invertEdge(i, o);
					else if (alte[1]==2) gr.addEdge(i,o);
					}
				}
			}
		}
	}

	private double[] min(double[] v) {
		double res[] = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
		for (int i=0; i<v.length; i++) {
			if (res[0]>v[i]) {
				res[0]=v[i];
				res[1]=i;
				}
		}
		return res;
	}
	
	@Override
	public String toString() {
		return "BNLearn [mmdl=" + mmdl + ", mgraph=" + mgraph + "]";
	}
}
