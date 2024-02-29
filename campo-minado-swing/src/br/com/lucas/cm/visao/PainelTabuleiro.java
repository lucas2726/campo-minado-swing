package br.com.lucas.cm.visao;

import java.awt.GridLayout;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.lucas.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas())); 
		//Para colocar as linhas e colunas na interface
		
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c))); 
		//Para colocar um botão para cada linha / coluna
		
		tabuleiro.registrarObservador(e -> {
			// mostrar para o usuário
			SwingUtilities.invokeLater(() -> {
				if(e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				tabuleiro.reiniciar();
			});
			
		});
	}
	
}
