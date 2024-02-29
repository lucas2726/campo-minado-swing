package br.com.lucas.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Tabuleiro implements CampoObservador {
/*Implementa a interface CampoObservador, indicando que
	a classe pode observar eventos em instâncias da classe Campo.*/

	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		//Inicializa as dimensões do tabuleiro e o número de minas.
		   
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		//Chama métodos para gerar os campos, associar vizinhos e sortear minas.
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao) {
		//Executa a função fornecida para cada campo no tabuleiro.
		campos.forEach(funcao);
	}
	
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		//adiciona observadores à lista.
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		//Notifica todos os observadores sobre um resultado específico.
		observadores.stream()
		   .forEach(o -> o.accept(new ResultadoEvento(resultado)));
	}
	
	public void abrir(int linha, int coluna) {
		//Abre um campo específico no tabuleiro.
	 campos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst() 
		.ifPresent(c -> c.abrir());
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		//Alterna a marcação (bandeira) de um campo específico.
		campos.parallelStream()
		     .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst() 
		.ifPresent(c -> c.alternarMarcacao());
	}

	private void gerarCampos() {
		// Cria os objetos Campo para cada posição no tabuleiro.
		for(int linha = 0; linha < linhas; linha++) {
			for(int coluna = 0; coluna < colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
			}
		}
		
	}

	private void associarVizinhos() {
		//Associa cada campo aos seus vizinhos.
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
		
	}

	private void sortearMinas() {
		// Coloca as minas de forma aleatória no tabuleiro.
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			 int aleatorio = (int) (Math.random() * campos.size());
			 campos.get(aleatorio).minar();
			 minasArmadas = campos.stream().filter(minado).count();
		} while(minasArmadas < minas);
	
	}
	
	public boolean objetivoAlcancado() {
		//Verifica se o objetivo do jogo foi alcançado (todos os campos abertos, exceto as minas).
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		// Reinicia o jogo, resetando os campos e redistribuindo as minas.
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
		

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		/*Se ocorrer uma explosão, mostra todas as minas e notifica os observadores.
		Se o objetivo for alcançado, notifica os observadores sobre a vitória.*/

		if(evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
		
	}
	
	public void mostrarMinas() {
		//Revela todas as minas no tabuleiro após o jogo terminar
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	}
	
}
