package br.edu.ifrn.sirenearduino;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

public class TelaInicial extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5853816806752983409L;
	
	// Componentes
	private JTextField txtDadoParaEviar;
	private JLabel status;
	private JButton btnEnviar;
	private JButton btnAcionar;
	private JMenuBar menu;
	private JToolBar toolBar;
	private JTable table;
	private Vector colHeaders;
	
	//Variaveis de Conexao
    private OutputStream output = null;
    SerialPort serialPort;
    private final String porta = "/dev/ttyACM0";
    private static final int timeOut = 2000; //2 segundos
    private static final int dataRate = 9600;
    
    {
    	menu = new JMenuBar();
    	toolBar = new JToolBar();
    	txtDadoParaEviar = new JTextField();
    	txtDadoParaEviar.setColumns(30);
    	status = new JLabel("Pronto");
    	btnEnviar = new JButton("Enviar");
    	btnAcionar = new JButton("Acionar Sirene");
    	colHeaders = new Vector(7);
    	colHeaders.add(new String("Domingo"));
    	colHeaders.add(new String("Segunda"));
    	colHeaders.add(new String("Terça"));
    	colHeaders.add(new String("Quanta"));
    	colHeaders.add(new String("Quinta"));
    	colHeaders.add(new String("Sexta"));
    	colHeaders.add(new String("Sábado"));
    	
    	DefaultTableModel tblModel = new DefaultTableModel(50, 7);
        tblModel.setColumnIdentifiers(colHeaders);
    	table = new JTable(tblModel);

    }
    
    public TelaInicial() {
		super(new BorderLayout());
		iniciarConexao();
		
		toolBar.setRollover(true);
		toolBar.setOrientation(JToolBar.VERTICAL);
		
		Action saveAction = new AbstractAction() {
			
			{
				putValue(Action.NAME, "Salvar");
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
						
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				String [][] times = new String[50][7];
//				iniciarConexao();
				for(int i = 0; i < 7; i++){
					for(int j = 0; j < 50; j++){
//				        Pattern pattern = Pattern.compile("[0-2][0-9]:[0-5][0-9]");
//				        Matcher matcher = pattern.matcher(table.getValueAt(j, i).toString());
//				        boolean matches = matcher.matches();
						if(table.getValueAt(j, i) != null){
//							System.out.println(table.getValueAt(j, i));
							String current = "A" + String.valueOf(i) + table.getValueAt(j, i);
							System.out.println(current);
							enviarDados(current+'x');
						}
					}
				}
				status.setText("Salvo");
//				fecharConexao();
				
			}
		};
		
		Action exitAction = new AbstractAction() {
			
			{
				putValue(Action.NAME, "Sair");
				putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
						
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		
		Action acionarAction = new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				enviarDados("RNOWx");
				status.setText("Sirene acionada!");
				fecharConexao();
				
			}
		};
		
		JMenu arquivo = new JMenu("Arquivo");
		JMenuItem salvar = new JMenuItem("Salvar");
		salvar.addActionListener(saveAction);
		JMenuItem sair = new JMenuItem("Sair");
		sair.addActionListener(exitAction);
		
		menu.add(arquivo);
		
		arquivo.add(salvar);
		arquivo.addSeparator();
		arquivo.add(sair);
		
		btnEnviar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				enviarDados(txtDadoParaEviar.getText());
			}
		});
		
		JPanel center = new JPanel(new BorderLayout());
		center.add(new JScrollPane(table), BorderLayout.CENTER);
		
		btnAcionar.addActionListener(acionarAction);
		btnAcionar.setMnemonic('A');
		btnAcionar.setSize(25, 25);
		toolBar.add(btnAcionar);
		
		this.add(menu, BorderLayout.NORTH);
		this.add(center, BorderLayout.CENTER);
		this.add(status, BorderLayout.SOUTH);
		this.add(toolBar, BorderLayout.WEST);
	}
    

    
    public void iniciarConexao(){
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
            mostrarErro("Não se pode conectar a porta");
            System.exit(ERROR);
        }
        
        try{
            serialPort = (SerialPort) portaId.open(this.getClass().getName(), timeOut);
            //parametros da porta serial
            
            serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            output = serialPort.getOutputStream();
        }catch(Exception e){
            mostrarErro(e.getMessage());
            System.exit(ERROR);
        }
    }
    
    private void fecharConexao() {
        try {
            output.close();
        }catch (IOException e) {
        	System.out.println(e.getMessage());
        }
      }
    
    private void enviarDados(String dados){
        try{
//        	if(dados.length() > 1){
//        		for(byte b : dados.getBytes()){
//        			output.write(b);
//        		}
//        	}else{
//        	byte[] dataToSend = dados.getBytes();
//        	output.write(dataToSend);
//        	}
        	output.write(dados.getBytes());
        }catch(Exception e){
            mostrarErro(e.getMessage());
//            System.exit(ERROR);
        }
    }
    
    public void mostrarErro(String mensagem){
        JOptionPane.showMessageDialog(this, mensagem,"ERRO",JOptionPane.ERROR_MESSAGE);
    }


}
