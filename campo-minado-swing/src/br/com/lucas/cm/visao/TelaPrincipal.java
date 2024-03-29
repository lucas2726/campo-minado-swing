package br.com.lucas.cm.visao;

import javax.swing.JFrame;

import br.com.lucas.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {
	
	public TelaPrincipal() {
		
		
		int porta = Integer.parseInt(System.getenv("PORT"));
		
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new TelaPrincipal();
		int porta = Integer.parseInt(System.getenv("PORT"));
	}
	
}
