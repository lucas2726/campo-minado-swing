package br.com.lucas.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.lucas.cm.modelo.Campo;
import br.com.lucas.cm.modelo.CampoEvento;
import br.com.lucas.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {
     
	private final Color BG_PADRAO = new Color(184, 184, 184); //Para definir as cores
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) { //Interface Botão
		this.campo = campo;
		setBackground(BG_PADRAO);//Para mudar a cor de fundo
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0)); //Para mudar as bordas
		
		addMouseListener(this); //Para chamar um evento
		campo.registrarObservador(this); //Para chamar o eventoOcorreu
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		//Para aplicar o estilo correto para cada evento 
		switch(evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
		    aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
		}
		
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0)); //Para mudar as bordas
		setText(" ");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstiloAbrir() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			return;
		}
		
		setBackground(BG_PADRAO);
		switch (campo.minasNaVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
        case 2:
        	setForeground(Color.BLUE);
			break;
        case 3:
        	setForeground(Color.YELLOW);
			break;	
        case 4:
        case 5:
        case 6:
        	setForeground(Color.RED);
        	break;	
		default:
			setForeground(Color.PINK);
		}
		
		String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + " " : " ";
		setText(valor);
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.alternarMarcacao();
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	
}
