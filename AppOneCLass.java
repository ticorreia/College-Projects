package Project;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class AppOneCLass {

	private JFrame frame;
	private Amostra am;
	private JTextField numbgr;
	private int n;
	private Bayes bayes;
	private BNLearn learn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppOneCLass window = new AppOneCLass();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppOneCLass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(102, 205, 170));
		frame.setBounds(100, 100, 450, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadCSV = new JButton("Load CSV");
		btnLoadCSV.setBackground(new Color(240, 255, 240));
		btnLoadCSV.setBounds(162, 115, 117, 29);
		frame.getContentPane().add(btnLoadCSV);
		
		JLabel lblSuccess = new JLabel("");
		lblSuccess.setForeground(Color.BLACK);
		lblSuccess.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuccess.setBounds(101, 233, 234, 69);
		frame.getContentPane().add(lblSuccess);
		
		
		numbgr = new JTextField();
		numbgr.setBounds(190, 162, 38, 19);
		frame.getContentPane().add(numbgr);
		numbgr.setColumns(10);
		numbgr.addActionListener(null);
		JLabel lblgraphs = new JLabel("Parâmetro aleatório:");
		lblgraphs.setBounds(55, 151, 206, 41);
		frame.getContentPane().add(lblgraphs);
		
		JButton ok = new JButton("OK");
		ok.setFont(new Font("Eras Bold ITC", Font.PLAIN, 10));
		ok.setForeground(new Color(255, 255, 255));
		ok.setBackground(new Color(0, 128, 128));
		ok.setBounds(238, 157, 58, 29);
		frame.getContentPane().add(ok);
		
		JButton btnBNL = new JButton("Bayesian Network Learning");
		btnBNL.setForeground(new Color(255, 255, 255));
		btnBNL.setBackground(new Color(0, 128, 128));
		btnBNL.setEnabled(false);
		btnBNL.setBounds(117, 202, 206, 21);
		frame.getContentPane().add(btnBNL);
		
		JButton btnBNSave = new JButton("Save Bayesian Netowrk");
		btnBNSave.setForeground(new Color(255, 255, 255));
		btnBNSave.setBackground(new Color(0, 128, 128));
		btnBNSave.setEnabled(false);
		btnBNSave.setBounds(117, 233, 206, 21);
		frame.getContentPane().add(btnBNSave);
		
		JLabel lblW1 = new JLabel("");
		lblW1.setForeground(new Color(204, 51, 0));
		lblW1.setBounds(10, 104, 142, 48);
		frame.getContentPane().add(lblW1);
		
		JLabel lblW2 = new JLabel("");
		lblW2.setForeground(new Color(204, 51, 0));
		lblW2.setBounds(296, 104, 130, 54);
		frame.getContentPane().add(lblW2);
		
		
		JTextArea txtrConstrutorDeRedes = new JTextArea();
		txtrConstrutorDeRedes.setForeground(new Color(255, 255, 255));
		txtrConstrutorDeRedes.setBackground(new Color(0, 128, 128));
		txtrConstrutorDeRedes.setFont(new Font("Microsoft JhengHei Light", Font.BOLD, 13));
		txtrConstrutorDeRedes.setEditable(false);
		txtrConstrutorDeRedes.setText("Construtor de redes de Bayes.\r\nPasso 1: Carregar um ficheiro CSV válido e introduzir um \r\nparâmetro de aleatoriedade.\r\nPasso 2: Clicar em \"Bayesian Network Learning\" e \r\nsalvar no computador.\r\n");
		txtrConstrutorDeRedes.setBounds(0, 0, 436, 105);
		frame.getContentPane().add(txtrConstrutorDeRedes);
		
	
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					n=Integer.parseInt(numbgr.getText());
					btnBNL.setEnabled(true);
					lblW2.setText("");
				}
				catch (Exception f) {
					lblW2.setText("Introduzir número!");
				}
			}
		});
		
		
		btnLoadCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(btnLoadCSV);
				File f = fc.getSelectedFile();
				String path = f.getAbsolutePath();
				String name = f.getName();
				am = new Amostra(path);
				lblSuccess.setText("Ficheiro CSV carregado: \n"+name);
				lblW1.setText("");
				System.out.println(am);
				}
				catch (Exception f) {
					lblW1.setText("Ficheiro inválido!");
				}
			}
		});
		
		btnBNL.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				try {
				learn = new BNLearn(n,am);
				bayes = new Bayes(am, learn.mgraph, 0.5);
				}
				catch (Exception f) {
					lblW1.setText("Uploadar ficheiro CSV");
					btnBNL.setEnabled(false);
				}
				btnBNSave.setEnabled(true);
			}
		});
		
		btnBNSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showSaveDialog(btnBNSave);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
		                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		                out.writeObject(bayes);
		                out.close();
		                System.out.println("Bayesian Network saved successfully.");
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		        }
			}
		});
		

	    
	}
}
