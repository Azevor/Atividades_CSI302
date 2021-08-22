//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Prático 2 - Parte 02: Algoritmo de Eleição													//
//																										//
// UFOP - CSI302 Sistemas Distribuídos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package anel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Controlar as especificações do processo de eleição.
 * Cada método implementa uma Thread que realiza as operações em paralelo.
 *
 */
public class Anel {
	private final int LOOP_NOVO_PROCESSO = 4000;
	private final int LOOP_NOVA_REQUISICAO = 3000;
	private final int LOOP_INATIVAR_PROCESSO = 8000;
	private final int LOOP_INATIVAR_COORDENADOR = 12000;
	
	public static ArrayList<Processo> processosAtivos;
	/*
	 * Objeto genrérico para controlar a sincronização de execução das threads,
	 * evitando execução de processo de eleição concorrentemente.
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
							// Caso não exista, o primeiro processo é criado como coordenador.
							processosAtivos.add(new Processo(1, true));
						} else {
							// Caso contrário, apenas adiciona um novo processo.
							processosAtivos.add(new Processo(
									processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
						}
						System.out.println("Processo ID: " + 
								(processosAtivos.get(processosAtivos.size() - 1).getPid()) + " criado.");
					}
					try {
						// Um novo processo será criado após o tempo especificado abaixo.
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
	 * A cada período especificado, uma nova requisição é realizada.
	 */
	public void novaRequisicao() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Após o tempo especificado abaixo, um processo  fará requisições periodicamente.
						Thread.sleep(LOOP_NOVA_REQUISICAO);
					} catch(Exception e) {
						System.err.println("Erro em Anel->fazRequisicoes(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
					synchronized(objControle) {
						// Caso exista processo ativo na lista de processos, uma requisição será feita.
						if(processosAtivos.size() > 0) {
							int idProcessoAleatorio = new Random().nextInt(processosAtivos.size());
							// Recuperando um processo aleatório para fazer requisição.
							Processo requisicaoDeProcesso = processosAtivos.get(idProcessoAleatorio);
							System.out.println("Processo ID: " + requisicaoDeProcesso.getPid() + " fazendo requisição.");
							requisicaoDeProcesso.enviaRequisicao();
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * Inativa um processo aleatório periodicamente.
	 */
	public void inativarProcesso() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Após o tempo especificado abaixo, um processo  será inativado periodicamente.
						Thread.sleep(LOOP_INATIVAR_PROCESSO);
					} catch(Exception e) {
						System.err.println("Erro em Anel->inativaProcessos(): " + e.getMessage() + "\n--");
						e.printStackTrace();
					}
					synchronized(objControle) {
						// Um processo aleatório será inativado apenas se a lista conter algum processo ativo.
						if(!processosAtivos.isEmpty()) {
							int idProcessoAleatorio = new Random().nextInt(processosAtivos.size());
							Processo processoParaRemover = processosAtivos.get(idProcessoAleatorio);
							// O processo, se válido, ficará inativo caso não seja coordenador.
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
	 * Um coordenador aleatório é inativado.
	 */
	public void inativarCoordenador() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						// Após o tempo especificado abaixo, um coordenador será inativado periodicamente.
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
						// Caso encontre o coordenador (not null) proceder a remoção da lista de processos ativos.
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
