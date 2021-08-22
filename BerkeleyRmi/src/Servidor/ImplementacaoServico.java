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

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Implementa��o do objeto remoto que recebe a comunica��o dos clientes.
 * @author Gildo Tiago
 *
 */
public class ImplementacaoServico implements Servico {
	/*
	 * HashMap que adiciona como chave a interface cliente (proxy) e
	 * armazena sua hora como respectivo valor.
	 */
	private HashMap<ServicoListener, Integer> listeners = new HashMap<>();
	
	/* Defini��o de alguns valores para teste de sincroniza��o dos rel�gios. */
	private int horas = 3;
	private int minutos = 15;
	private int valorRelogioLocal = (horas * 60 + minutos);
	private DecimalFormat df = new DecimalFormat("00"); // Exibir dois d�gitos da hora.
	private int mediaDiferencas = 0; // Armazenar a m�dia das diferen�as de hor�rio servidor-clientes.

	/* Verificar se armazenou a hora dos respectivos clientes. */
	private boolean setouRelogio1;
	private boolean setouRelogio2;
	private boolean setouRelogio3;
	private boolean setouRelogio4;

	/* M�todo remoto que recebe o proxy cliente conectado e sua respectiva hora. */
	@Override
	public void addListener(ServicoListener listener, int valorRelogio) throws RemoteException {
		listeners.put(listener, valorRelogio);
		// Exibindo a hora local do servidor.
		System.out.println("Hora atual do servidor: " + df.format(valorRelogioLocal/60) + ":" 
						+ df.format(valorRelogioLocal%60));
	}

	/*
	 * Configura��o feita para receber 4 clientes, neste trabalho pr�tico.
	 */
	
	/* M�todo remoto que o cliente utiliza para setar (confirmar informa��o de hora) */
	@Override
	public void setRelogio1() throws RemoteException { // Cliente A
		setouRelogio1 = true;
		verifica();
	}

	@Override
	public void setRelogio2() throws RemoteException { // Cliente B
		setouRelogio2 = true;
		verifica();
	}
	
	@Override
	public void setRelogio3() throws RemoteException { // Cliente C
		setouRelogio3 = true;
		verifica();
	}
	
	@Override
	public void setRelogio4() throws RemoteException { // Cliente D
		setouRelogio4 = true;
		verifica();
	}
	
	/**
	 * Calcular diferen�a de horario entre o servidor e os clientes.
	 * @param HashMap<Cliente, Horario> : clientes
	 * @return HashMap<Cliente, Diferen�a_Horario> : diferencas
	 */
	private HashMap<ServicoListener, Integer> sincronizar(HashMap<ServicoListener, Integer> clientes) {
		HashMap<ServicoListener, Integer> diferencas = new HashMap<>();
		int totalDiferencas = 0;
		/*
		 * De acordo com o algoritmo de Berkeley, para cada horario do cliente,
		 * ser� calculado a diferen�a entre o servidor e o cliente.
		 */
		for (ServicoListener keyListener : clientes.keySet()) {
			diferencas.put(keyListener, clientes.get(keyListener)-valorRelogioLocal);
			totalDiferencas += clientes.get(keyListener)-valorRelogioLocal;
		}
		// C�lculo da m�dia das diferen�as de hor�rio do cliente.
		mediaDiferencas = totalDiferencas/(1+diferencas.size()); // Soma-se 1 para contar com o servidor na m�dia.
		return diferencas;
	}
	
	/**
	 * M�todo local que verifica se todos os clientes conectados informaram suas respectivas horas.
	 */
	private void verifica() {
		// Caso o servidor esteja ciente de todos os hor�rios, realiza-se a sincroniza��o dos hor�rios.
		if (setouRelogio1 && setouRelogio2 && setouRelogio3 && setouRelogio4) {
			HashMap<ServicoListener, Integer> diferencas = sincronizar(listeners);
			System.out.println("M�dia calculada = " + mediaDiferencas + " minutos");
			valorRelogioLocal += mediaDiferencas;
			// Rel�gio do servidor recebe a m�dia das diferen�as entre os rel�gios e exibe seu novo hor�rio.
			System.out.println("Ajuste do servidor: " + df.format(valorRelogioLocal/60) + ":" 
							+ df.format(valorRelogioLocal%60));
			
			// Todo cliente conectado receber� a diferen�a, pra mais ou pra menos, de ajuste do rel�gio.
			for (ServicoListener keyListener : listeners.keySet()) {
				int diferenca = mediaDiferencas-diferencas.get(keyListener);
				try {
					keyListener.atualizarRelogio(diferenca);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			setouRelogio1 = false;
			setouRelogio2 = false;
			setouRelogio3 = false;
		}
	}
}