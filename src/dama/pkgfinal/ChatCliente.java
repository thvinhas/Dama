/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dama.pkgfinal;


  import java.awt.Color;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import java.awt.event.KeyEvent;
  import java.awt.event.KeyListener;
  import java.io.BufferedReader;
  import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.InputStreamReader;
  import java.io.OutputStream;
  import java.io.OutputStreamWriter;
  import java.io.Writer;
import java.net.ServerSocket;
  import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
  import javax.swing.*;

/**
 *
 * @author THIAGO
 */
public class ChatCliente extends JFrame implements ActionListener, KeyListener,Runnable {
private static final long serialVersionUID = 1L;
private JTextArea texto;
private JTextField txtMsg;
private JButton btnSend;
private JLabel lblHistorico;
private JLabel lblMsg;
private JPanel pnlContent;
private Writer ouw; 
private BufferedWriter bfw;
private JTextField txtIP;
private JTextField txtPorta;
private String txtNome; 
Socket s;
DataInputStream din;
DataOutputStream dout;
ServerSocket ss;

public ChatCliente(JTextField Ip, JTextField Porta) throws IOException{
    s = DamaCliente.s;
    ss =DamaCliente.ss;
    din = DamaCliente.din;
    dout = DamaCliente.dout;
    txtIP = Ip;
    txtPorta = Porta;
    txtNome = "Cliente";                     
     pnlContent = new JPanel();
     texto              = new JTextArea(10,20);
     texto.setEditable(false);
     texto.setBackground(new Color(240,240,240));
     txtMsg                       = new JTextField(20);
     lblHistorico     = new JLabel("Histórico");
     lblMsg        = new JLabel("Mensagem");
     btnSend                     = new JButton("Enviar");
     btnSend.setToolTipText("Enviar Mensagem");
     btnSend.addActionListener(this);
     btnSend.addKeyListener(this);
     txtMsg.addKeyListener(this);
     JScrollPane scroll = new JScrollPane(texto);
     texto.setLineWrap(true);  
     pnlContent.add(lblHistorico);
     pnlContent.add(scroll);
     pnlContent.add(lblMsg);
     pnlContent.add(txtMsg);
     pnlContent.add(btnSend);
     pnlContent.setBackground(Color.LIGHT_GRAY);                                 
     texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
     txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));                    
     setTitle(txtNome);
     setContentPane(pnlContent);
     setLocationRelativeTo(null);
     setResizable(false);
     setSize(250,300);
     setVisible(true);
}

 /***
  * Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
  * @throws IOException
  */

/***
  * Método usado para enviar mensagem para o server socket
  * @param msg do tipo String
  * @throws IOException retorna IO Exception caso dê algum erro.
  */
  public void enviarMensagem(String msg) throws IOException{
                          
    if(msg.equals("Sair")){
      dout.writeUTF("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    }else{
        dout.writeUTF(msg+"\r\n");
      texto.append( txtNome + " diz -> " +         txtMsg.getText()+"\r\n");
    }
     txtMsg.setText("");        
}
  
  /**
 * Método usado para receber mensagem do servidor
 * @throws IOException retorna IO Exception caso dê algum erro.
 */
public void escutar() throws IOException{
                          
  String msgin = "";
 
         int yy=0;
            String aa = null;                 
     while(!msgin.equals("exit")){
            if(yy == 0){
                aa = "";
                yy=1;
            }else{
                aa = "\n";
            }
                msgin = din.readUTF();
                if (!msgin.contains("[peca]")){
                texto.setText(texto.getText().trim()+aa+"Cliente: "+msgin);
                }else{
                    yy = 0;
                   DamaCliente.ReceberMensagem(msgin);
                }
            }
                                     
      
      
}     

 /***
   * Método usado quando o usuário clica em sair
   * @throws IOException retorna IO Exception caso dê algum erro.
   */
   
   
   @Override
public void actionPerformed(ActionEvent e) {
         
  try {
        enviarMensagem(txtMsg.getText());
     } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
     }                       
}

@Override
public void keyPressed(KeyEvent e) {
               
    if(e.getKeyCode() == KeyEvent.VK_ENTER){
       try {
          enviarMensagem(txtMsg.getText());
       } catch (IOException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }                                                          
   }                       
}
   
@Override
public void keyReleased(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}
   
@Override
public void keyTyped(KeyEvent arg0) {
  // TODO Auto-generated method stub               
}     

    @Override
    public void run() {
    try {
        escutar();
    } catch (IOException ex) {
        Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    
    
}
