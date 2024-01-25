package Project;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;

public class AppTwoClass {

	private JFrame frame;
	private Bayes bayes;
	private JTextField vector;
	private int[] v;
	private double[] pr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppTwoClass window = new AppTwoClass();
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
	public AppTwoClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 235, 205));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnLoadBN = new JButton("Load Bayesian Network");
		btnLoadBN.setForeground(new Color(255, 255, 255));
		btnLoadBN.setBackground(new Color(222, 184, 135));
		btnLoadBN.setFont(new Font("Verdana", Font.PLAIN, 10));
		btnLoadBN.setBounds(10, 90, 159, 30);
		frame.getContentPane().add(btnLoadBN);
		
		JLabel lblSuccess = new JLabel("");
		lblSuccess.setForeground(new Color(0, 128, 0));
		lblSuccess.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuccess.setBounds(179, 87, 292, 43);
		frame.getContentPane().add(lblSuccess);
		
		vector = new JTextField();
		vector.setBounds(10, 159, 247, 19);
		frame.getContentPane().add(vector);
		vector.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Parâmetros da amostra:");
		lblNewLabel.setBounds(10, 139, 135, 13);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnOK = new JButton("CLASSIFY");
		btnOK.setBackground(new Color(222, 184, 135));
		btnOK.setEnabled(false);
		btnOK.setFont(new Font("TI-Nspire Sans", Font.BOLD, 10));
		btnOK.setBounds(267, 140, 159, 60);
		frame.getContentPane().add(btnOK);
		
		JTextArea prob = new JTextArea();
		prob.setFont(new Font("Monospaced", Font.PLAIN, 17));
		prob.setEditable(false);
		prob.setBounds(0, 204, 436, 49);
		frame.getContentPane().add(prob);
		
		JLabel lblNewLabel_1 = new JLabel("Classificação:");
		lblNewLabel_1.setBounds(10, 188, 135, 13);
		frame.getContentPane().add(lblNewLabel_1);
		
		JTextArea txtrClassificadorDeAmostras = new JTextArea();
		txtrClassificadorDeAmostras.setForeground(new Color(255, 255, 255));
		txtrClassificadorDeAmostras.setBackground(new Color(210, 180, 140));
		txtrClassificadorDeAmostras.setFont(new Font("Malgun Gothic Semilight", Font.BOLD, 13));
		txtrClassificadorDeAmostras.setText("Classificador de amostras.\r\nCarregar uma Rede de Bayes válida, intrduzir os \r\nparâmetros da amostra e clicar em \"CLASSIFY\".");
		txtrClassificadorDeAmostras.setEditable(false);
		txtrClassificadorDeAmostras.setBounds(0, 0, 436, 77);
		frame.getContentPane().add(txtrClassificadorDeAmostras);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setForeground(new Color(255, 255, 255));
		btnClear.setBackground(new Color(222, 184, 135));
		btnClear.setBounds(172, 179, 85, 21);
		frame.getContentPane().add(btnClear);
		
		btnLoadBN.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showOpenDialog(btnLoadBN);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            // Get the selected file
		            File file = fc.getSelectedFile();
		            String name = file.getName();
		            try {
		                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		                bayes = (Bayes) in.readObject();
		                in.close();
		                btnOK.setEnabled(true);
			            lblSuccess.setForeground(Color.GREEN);
			            lblSuccess.setText("Ficheiro lido corretamente: "+name);
		                System.out.println("Bayesian Network loaded successfully.");
		            } catch (IOException | ClassNotFoundException ex) {
		            	lblSuccess.setForeground(Color.red);
			        	lblSuccess.setText("Ficheiro inválido!");
		            } 
        		}
		    }
		});
		
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputText = vector.getText();
		        try {
		        	String[] numbersAsStrings;
		            if (inputText.contains(", ")) {
		                numbersAsStrings = inputText.split(", ");
		            } else {
		                numbersAsStrings = inputText.split(",");
		            }
			        v = new int[numbersAsStrings.length];
		            for (int i = 0; i < numbersAsStrings.length; i++) {
		                v[i] = Integer.parseInt(numbersAsStrings[i]);
		            }
		            pr = bayes.prob(v);
		            System.out.println(bayes.graph.toString());
			        System.out.println(Arrays.toString(pr));
			        prob.setForeground(Color.black);
			        prob.setText(bayes.probTranslater(pr));
		        } 
		        	catch (NumberFormatException ex) {
		        		prob.setForeground(Color.red);
		        		prob.setText("Parâmetros inválidos.\r\nErro: Sintaxe");
		        } catch (ArrayIndexOutOfBoundsException f) {
		        	prob.setForeground(Color.red);
		        	prob.setText("Parâmetros inválidos.\r\n Erro: Domínios");
		        } catch (Exception e1) {
		        	prob.setForeground(Color.red);
		        	prob.setText("Parâmetros inválidos.\r\nErro: Tamanho da amostra");
				}
		        
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vector.setText(null);
			}
		});
	}
}
