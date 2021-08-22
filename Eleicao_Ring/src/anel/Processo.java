//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Prático 2 - Parte 02: Algoritmo de Eleição													//
//																										//
// UFOP - CSI302 Sistemas Distribuídos																	//
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
	 * Realizar requisição ou iniciar eleição, caso necessário.
	 * @return boolean
	 */
	public boolean enviaRequisicao() {
		boolean requisicaoFeita = false;
		// Procura coordenador na lista de processos ativos (estática).
		for(Processo p: Anel.processosAtivos) {
			if(p.ehCoordenador()) {
				requisicaoFeita = p.recebeRequisicao(this.mPid);
			}
		}
		// Caso não exista um coordenador, fazer eleição.
		if(!requisicaoFeita) {
			this.novaEleicao();
		}
		System.out.println("Fim da requisicao");
		return requisicaoFeita;
	}
	
	/**
	 * Método responsável pelo tratamento da requisição.
	 * @param pIdOrigemRequisicao
	 * @return boolean
	 */
	private boolean recebeRequisicao(int pIdOrigemRequisicao) {
		/* Executa requisição ... */
		
		System.out.println("Requisição do processo ID: " + pIdOrigemRequisicao + " executada.");
		return true;
	}
	
	/**
	 * Seleciona novo coordenador através do processo de eleição.
	 * O processo de maior ID será selecionado como coordenador.
	 */
	private void novaEleicao() {
		System.out.println("Eleição iniciada");
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
			System.out.println("Eleição concluída. Novo coordenador - processo ID: " + idNovoCoordenador + ".");
		} else {
			System.out.println("Eleição Falhou! Novo coordenador não encontrado.");
			// Em caso de falha, uma nova eleição será feita pela próxima requisição.
		}
	}
	
	/**
	 * Executar validação do processo para indicação na lista de eleição.
	 * @param ListaEleitoresValidos
	 */
	private void verificaProcesso(LinkedList<Integer> ListaEleitoresValidos) {
		/* Executa validação do processo ... */
		
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
				// Garantir que todos os processos restantes não sejam coordenadores.
				p.setEhCoordenador(false);
			}
		}
		return true;
	}
}
