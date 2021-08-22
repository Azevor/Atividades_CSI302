//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Pr�tico 2 - Parte 02: Algoritmo de Elei��o													//
//																										//
// UFOP - CSI302 Sistemas Distribu�dos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package anel;

import java.util.LinkedList;

/**
 * Gerenciar a atividade do processo identificado pelo id.
 *
 */
public class Processo {
	private int mPid;
	private boolean ehCoordenador;
	
	public Processo(int pPid, boolean pEhCoordenador) {
		setPid(pPid);
		setEhCoordenador(pEhCoordenador);
	}

	public int getPid() {
		return mPid;
	}

	public void setPid(int pPid) {
		this.mPid = pPid;
	}

	public boolean ehCoordenador() {
		return ehCoordenador;
	}

	public void setEhCoordenador(boolean pEhCoordenador) {
		this.ehCoordenador = pEhCoordenador;
	}
	
	/**
	 * Realizar requisi��o ou iniciar elei��o, caso necess�rio.
	 * @return boolean
	 */
	public boolean enviaRequisicao() {
		boolean requisicaoFeita = false;
		// Procura coordenador na lista de processos ativos (est�tica).
		for(Processo p: Anel.processosAtivos) {
			if(p.ehCoordenador()) {
				requisicaoFeita = p.recebeRequisicao(this.mPid);
			}
		}
		// Caso n�o exista um coordenador, fazer elei��o.
		if(!requisicaoFeita) {
			this.novaEleicao();
		}
		System.out.println("Fim da requisicao");
		return requisicaoFeita;
	}
	
	/**
	 * M�todo respons�vel pelo tratamento da requisi��o.
	 * @param pIdOrigemRequisicao
	 * @return boolean
	 */
	private boolean recebeRequisicao(int pIdOrigemRequisicao) {
		/* Executa requisi��o ... */
		
		System.out.println("Requisi��o do processo ID: " + pIdOrigemRequisicao + " executada.");
		return true;
	}
	
	/**
	 * Seleciona novo coordenador atrav�s do processo de elei��o.
	 * O processo de maior ID ser� selecionado como coordenador.
	 */
	private void novaEleicao() {
		System.out.println("Elei��o iniciada");
		LinkedList<Integer> idListaParaEleicao = new LinkedList<>();
		// Consultar processos ativos e adiciona-los numa nova lista.
		for(Processo p : Anel.processosAtivos) {
			p.verificaProcesso(idListaParaEleicao);
		}
		// Procurando pelo maior ID
		int idNovoCoordenador = this.getPid();
		for(Integer id : idListaParaEleicao) {
			if(id > idNovoCoordenador) {
				idNovoCoordenador = id;
			}
		}
		// Atualiza novo coordenador
		boolean ehCoordenadorValido = false;
		ehCoordenadorValido = atualizarCoordenador(idNovoCoordenador);
		if(ehCoordenadorValido) {
			System.out.println("Elei��o conclu�da. Novo coordenador - processo ID: " + idNovoCoordenador + ".");
		} else {
			System.out.println("Elei��o Falhou! Novo coordenador n�o encontrado.");
			// Em caso de falha, uma nova elei��o ser� feita pela pr�xima requisi��o.
		}
	}
	
	/**
	 * Executar valida��o do processo para indica��o na lista de elei��o.
	 * @param ListaEleitoresValidos
	 */
	private void verificaProcesso(LinkedList<Integer> ListaEleitoresValidos) {
		/* Executa valida��o do processo ... */
		
		ListaEleitoresValidos.add(this.getPid());
	}
	
	/**
	 * Atribuir ou atualizar um processo como coordenador.
	 * @param pIdNovoCoordenador
	 * @return boolean
	 */
	private boolean atualizarCoordenador(int pIdNovoCoordenador) {
		for(Processo p : Anel.processosAtivos) {
			if(p.getPid() == pIdNovoCoordenador) {
				p.setEhCoordenador(true);
			} else {
				// Garantir que todos os processos restantes n�o sejam coordenadores.
				p.setEhCoordenador(false);
			}
		}
		return true;
	}
}
