//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Pr�tico 2 - Parte 02: Algoritmo de Elei��o													//
//																										//
// UFOP - CSI302 Sistemas Distribu�dos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package anel;

public class Main {
	
	public static void main(String[] args) {
		Anel anel = new Anel();
		
		/*
		 * Ao chamar cada m�todo do anel pela primeira vez,
		 * suas respectivas threads come�am a realizar as
		 * opera��es especificadas periodicamente.
		 */
		anel.novoProcesso();
		anel.novaRequisicao();
		anel.inativarProcesso();
		anel.inativarCoordenador();
	}
}
