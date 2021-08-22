//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Trabalho Pr�tico 2 - Parte 01: Algoritmo de Berkeley													//
//																										//
// UFOP - CSI302 Sistemas Distribu�dos																	//
// Prof.: D.r Theo Silva Lins																			//
// Aluno: Gildo Tiago Azevedo - 17.1.8242																//
//																										//
// * C�digo adaptado:																					//
//	(FONTE: https://www.guj.com.br/t/java-rmi-cliente-a-e-b-enviar-info-servidor-dar-resposta/349650)	//
//////////////////////////////////////////////////////////////////////////////////////////////////////////

package Servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface remota para comunica��o dos clientes com o servidor remoto.
 * @author Gildo Tiago
 *
 */
public interface Servico extends Remote {
	void addListener(ServicoListener listener, int valorRelogio) throws RemoteException;
	void setRelogio1() throws RemoteException;
	void setRelogio2() throws RemoteException;
	void setRelogio3() throws RemoteException;
	void setRelogio4() throws RemoteException;
}