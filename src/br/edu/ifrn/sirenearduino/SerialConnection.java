package br.edu.ifrn.sirenearduino;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.swing.JOptionPane;

public class SerialConnection {
	
	//Variaveis de Conexao
    private OutputStream output = null;
    SerialPort serialPort;
    private final String porta = "/dev/ttyACM1";
    private static final int timeOut = 2000; //2 segundos
    private static final int dataRate = 9600;
    
    public void start(){
        CommPortIdentifier portaId = null;
        Enumeration portaEnum = CommPortIdentifier.getPortIdentifiers();
        System.out.
        
        
        println(portaEnum.hasMoreElements());
        while(portaEnum.hasMoreElements()){
            CommPortIdentifier atualPortaId =(CommPortIdentifier) portaEnum.nextElement();
            if(porta.equals(atualPortaId.getName())){
                portaId=atualPortaId;
                break;
            }
        }
        if(portaId == null){
//            mostrarErro("Não se pode conectar a porta");
//            System.exit(ERROR);
        }
        
        try{
            serialPort = (SerialPort) portaId.open(this.getClass().getName(), timeOut);
            //parametros da porta serial
            
            serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            output = serialPort.getOutputStream();
        }catch(Exception e){
//            mostrarErro(e.getMessage());
//            System.exit(ERROR);
        }
    }
    
    private void send(String dados){
        try{
//        output.write(dados.getBytes()); 
        	output.write(Integer.parseInt(dados));
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
    }
    
    public void close() {
        try {
            output.close();
        }catch (IOException e) {
        	System.out.println(e.getMessage());
        }
      }

}
