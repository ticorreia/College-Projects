package Project;

import java.io.Serializable;

import java.util.Arrays;
import java.util.LinkedList;

public class Bayes implements Serializable {
	private static final long serialVersionUID = 1L;
	private double [] [] [] [] [] BN;
	public Graphpr graph;
    private int [] amdomain;
	public Bayes(Amostra am, Graphpr gr, double S){
		super();
        this.graph = gr;
        this.amdomain=am.domain;
        int d = this.graph.getDim()-1;
		this.BN = new double[this.graph.getDim()][][][][];
		for (int i=0; i<this.graph.getDim(); i++) {
			int s=this.graph.parents(i).size();
			if (s==0) {
				this.BN[i]=new double [this.amdomain[d]][1][1][1];
				for (int o=0; o<this.amdomain[d]; o++) {
					BN[i][o][0][0][0]=am.freqam(i)[o];
				}
			}
			else if (s==1) {
				this.BN[i]=new double [this.amdomain[d]][this.amdomain[i]][1][1];
				for (int o=0; o<this.amdomain[d]; o++) {
					for (int di=0; di<this.amdomain[i];di++) {
					LinkedList<Integer> val = new LinkedList<Integer>();
					val.add(o);
					this.BN[i][o][di][0][0]=this.graph.thetaaux(am, i, this.graph.parents(i), val, di, S);
					}
				}
			}
			else if (s==2) {
				this.BN[i]=new double [this.amdomain[d]][this.amdomain[i]][this.amdomain[this.graph.parents(i).get(0)]][1];
				for (int o=0; o<this.amdomain[d]; o++) {
					for (int di=0; di<this.amdomain[i];di++) {
						for (int wi1=0; wi1<this.amdomain[this.graph.parents(i).get(0)]; wi1++) {	
						LinkedList<Integer> val = new LinkedList<Integer>();
						val.add(wi1);
						val.add(o);
						BN[i][o][di][wi1][0]=this.graph.thetaaux(am, i, this.graph.parents(i), val, di, S);
						}
					}
				}
			}
			else if (s==3) {
				
				this.BN[i]=new double [this.amdomain[d]][this.amdomain[i]][this.amdomain[this.graph.parents(i).get(0)]][this.amdomain[this.graph.parents(i).get(1)]];
				for (int o=0; o<this.amdomain[d]; o++) {
					for (int di=0; di<this.amdomain[i];di++) {
						for (int wi1=0; wi1<this.amdomain[this.graph.parents(i).get(0)]; wi1++) {	
							for (int wi2=0; wi2<this.amdomain[this.graph.parents(i).get(1)]; wi2++) {
							LinkedList<Integer> val = new LinkedList<Integer>();
							val.add(wi1);
							val.add(wi2);
							val.add(o);
							BN[i][o][di][wi1][wi2]=this.graph.thetaaux(am, i, this.graph.parents(i), val, di, S);
							}
						}
					}
				}
			}	
		}
    }
	
	public double [] prob(int[] v) throws Exception{
		if (v.length==this.BN.length-1) {
		double [] pc = new double [this.amdomain[this.graph.getDim()-1]];
		for (int o=0; o<this.amdomain[this.graph.getDim()-1]; o++) {
				pc[o]=this.BN[this.graph.getDim()-1][o][0][0][0];
		}
		for (int i=0; i<this.graph.getDim()-1; i++) {
			int s = this.graph.parents(i).size();
			for (int o=0; o<this.amdomain[this.graph.getDim()-1]; o++) {
				if (s==1) {
					pc[o]*=this.BN[i][o][v[i]][0][0];
				}
				else if (s==2) {
					pc[o]*=this.BN[i][o][v[i]][v[this.graph.parents(i).get(0)]][0];
				}
				else if (s==3) {
					pc[o]*=this.BN[i][o][v[i]][v[this.graph.parents(i).get(0)]][v[this.graph.parents(i).get(1)]];
				}
			}
		}
		double [] prc = new double [pc.length];
		for (int i=0; i<pc.length; i++) {
			prc[i]=pc[i]/(this.sumvect(pc));
		}
		return prc;
		}
		else throw new Exception ("Tamanho inválido");
	}
	
	private double sumvect(double [] v) {
		double sum=0;
		for (int i=0; i<v.length; i++) {
			sum+=v[i];
		}
		return sum;
	}
	
	public String showprob(double[] pr, int [] v) throws Exception {
		String res = "am:"+Arrays.toString(v)+" prob: "+Arrays.toString(this.prob(v));
		return res;
	}

	
	public String probTranslater(double[] v) {
		String res ="";
		double m = Double.NEGATIVE_INFINITY;
		int p=Integer.MIN_VALUE;
		for (int o=0; o<v.length; o++) {
			if (v[o]>m) {
				m = v[o];
				p = o;
			}
		}
		res = (p+"");
		return res;
	}
}
