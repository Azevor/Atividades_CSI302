//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Prático 2 - Parte 02: Algoritmo de Eleição													//
//																										//
// UFOP - CSI302 Sistemas Distribuídos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package anel;

public class Main {
	
	public static void main(String[] args) {
		Anel anel = new Anel();
		
		/*
		 * Ao chamar cada método do anel pela primeira vez,
		 * suas respectivas threads começam a realizar as
		 * operações especificadas periodicamente.
		 */
		anel.novoProcesso();
		anel.novaRequisicao();
		anel.inativarProcesso();
		anel.inativarCoordenador();
	}
}
