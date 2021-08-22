//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Pr�tico 2 - Parte 02: Algoritmo de Elei��o													//
//																										//
// UFOP - CSI302 Sistemas Distribu�dos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package anel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Controlar as especifica��es do processo de elei��o.
 * Cada m�todo implementa uma Thread que realiza as opera��es em paralelo.
 *
 */
public class Anel {
	private final int LOOP_NOVO_PROCESSO = 4000;
	private final int LOOP_NOVA_REQUISICAO = 3000;
	private final int LOOP_INATIVAR_PROCESSO = 8000;
	private final int LOOP_INATIVAR_COORDENADOR = 12000;
	
	public static ArrayList<Processo> processosAtivos;
	/*
	 * Objeto genr�rico para controlar a sincroniza��o de execu��o das threads,
	 * evitando execu��o de processo de elei��o concorrentemente.
	 */
	private final Object objControle = new Object();
	
	public Anel() {
		processosAtivos = new ArrayList<Processo>();
	}
	
	/**
	 * Cria um novo processo periodicamente.
	 */
	public void novoProcesso() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					synchronized(objControle) {
						// Verificar se existe processo criado anteriormente.
						if(processosAtivos.isEmpty()) {
							// Caso n�o exista, o primeiro processo � criado como coordenador.
							processosAtivos.add(new Processo(1, true));
						} else {
							// Caso contr�rio, apenas adiciona um novo processo.
							processosAtivos.add(new Processo(
									processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
						}
						System.out.println("Processo ID: " + 
								(processosAtivos.get(processosAtivos.size() - 1).getPid()) + " criado.");
					}
					try {
						// Um novo processo ser� criado ap�s o tempo especificado abaixo.
						Thread.sleep(LOOP_NOVO_PROCESSO);
					} catch(Exception e) {
						System.err.println("Erro em Anel->novoProcessos(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * A cada per�odo especificado, uma nova requisi��o � realizada.
	 */
	public void novaRequisicao() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Ap�s o tempo especificado abaixo, um processo  far� requisi��es periodicamente.
						Thread.sleep(LOOP_NOVA_REQUISICAO);
					} catch(Exception e) {
						System.err.println("Erro em Anel->fazRequisicoes(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
					synchronized(objControle) {
						// Caso exista processo ativo na lista de processos, uma requisi��o ser� feita.
						if(processosAtivos.size() > 0) {
							int idProcessoAleatorio = new Random().nextInt(processosAtivos.size());
							// Recuperando um processo aleat�rio para fazer requisi��o.
							Processo requisicaoDeProcesso = processosAtivos.get(idProcessoAleatorio);
							System.out.println("Processo ID: " + requisicaoDeProcesso.getPid() + " fazendo requisi��o.");
							requisicaoDeProcesso.enviaRequisicao();
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * Inativa um processo aleat�rio periodicamente.
	 */
	public void inativarProcesso() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Ap�s o tempo especificado abaixo, um processo  ser� inativado periodicamente.
						Thread.sleep(LOOP_INATIVAR_PROCESSO);
					} catch(Exception e) {
						System.err.println("Erro em Anel->inativaProcessos(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
					synchronized(objControle) {
						// Um processo aleat�rio ser� inativado apenas se a lista conter algum processo ativo.
						if(!processosAtivos.isEmpty()) {
							int idProcessoAleatorio = new Random().nextInt(processosAtivos.size());
							Processo processoParaRemover = processosAtivos.get(idProcessoAleatorio);
							// O processo, se v�lido, ficar� inativo caso n�o seja coordenador.
							if(processoParaRemover != null && !processoParaRemover.ehCoordenador()) {
								processosAtivos.remove(processoParaRemover);
								System.out.println("Processo ID: " + processoParaRemover.getPid() + " inativado.");
							}
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * Um coordenador aleat�rio � inativado.
	 */
	public void inativarCoordenador() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Ap�s o tempo especificado abaixo, um coordenador ser� inativado periodicamente.
						Thread.sleep(LOOP_INATIVAR_COORDENADOR);
					} catch(Exception e) {
						System.err.println("Erro em Anel->inativaCoordenador(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
					synchronized(objControle) {
						Processo coordenador = null;
						// Percorre lista de processos ativos procurando pelo coordenador.
						for(Processo p : processosAtivos) {
							if(p.ehCoordenador()) {
								coordenador = p;
							}
						}
						// Caso encontre o coordenador (not null) proceder a remo��o da lista de processos ativos.
						if(coordenador != null) {
							processosAtivos.remove(coordenador);
							System.out.println("Processo coordenador ID: " + coordenador.getPid() + " inativado.");
						}
					}
				}
			}
		}).start();
	}
}
