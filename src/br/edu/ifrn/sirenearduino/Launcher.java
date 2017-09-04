package br.edu.ifrn.sirenearduino;


import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Launcher {

	public static void main(String[] args) {
		try {
            // select Look and Feel
//            UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            // start application
            EventQueue.invokeLater(new Runnable() {
                public void run() {   
                	
                	
                	
                	TelaInicial telaInicial = new TelaInicial();
    	             
    	            JFrame frame = new JFrame();
    	            frame.setTitle("CASE - Controlador Automático de Sirene Escolar");
    	            frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    	            frame.setMinimumSize(new Dimension(900, 400));
    	            frame.setResizable(false);
    	            frame.setContentPane(telaInicial);
    	            frame.setVisible( true ); // display frame	
                }
           });   

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
		
	}

}
