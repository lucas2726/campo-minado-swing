package br.com.lucas.cm.modelo;

import java.util.ArrayList;
import java.util.List;


public class Campo {
    
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>(); /*Lista de objetos que observ
	am e reagem aos eventos ocorridos no campo.*/


	public Campo(int linha, int coluna) {
		//Inicializa o campo com as coordenadas dadas.
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		//Adiciona um observador à lista.
		observadores.add(observador);
	}
	
	public void notificarObservadores(CampoEvento evento) {
		//Notifica todos os observadores sobre um evento específico.
		observadores.stream()
		   .forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		//Adiciona um campo à lista de vizinhos se estiver adjacente.
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if(deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
  }
	
	public void alternarMarcacao() {
		//Alterna a marcação do campo se não estiver aberto.
		if(!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
	}
	
	public boolean abrir() {
	//Abre o campo, revelando seu conteúdo, possivelmente explodindo uma mina.	
		if(!aberto && !marcado) {
			aberto = true;
			
			if(minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);
			
			if(vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean vizinhancaSegura() {
		//Verifica se todos os vizinhos são seguros, ou seja, sem minas
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	void minar() {
		//Marca o campo como minado.
		minado = true;
	}
	
	//verificam o estado atual do campo.
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
    void setAberto(boolean aberto) { 
    	this.aberto = aberto;
    	
    	if(aberto) {
    		notificarObservadores(CampoEvento.ABRIR);
    	}
    }
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		//Retorna a quantidade de minas nos campos vizinhos.
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		//Reinicia o campo, restaurando seus atributos padrão e notificando os observadores.
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
}