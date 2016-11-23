/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dama.pkgfinal;

import static dama.pkgfinal.DamaServidor.din;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author THIAGO
 */
public class DamaCliente extends JFrame implements ActionListener {

    static JTextField txtIP;
    static JTextField txtPorta;
    static JTextField txtNome;
    JButton pecas[] = new JButton[64];
    String corUltmaPeca = "Preto";
    static Container c;
    JButton PecaClicada,pecaMovida,casaClicada;
    ImageIcon DamaPreta, DamaBranca, PecaPreta, PecaBranca;
    static JButton casaAzul1, casaAzul2, button;
    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    static ServerSocket ss;
    static int PecasPerdidas = 12;
    static int PecasGanhadas = 0;
    static String vezJogar = "servidor";


    public DamaCliente() throws IOException {
        super("Jogo de Dama");

       JLabel lblMessage = new JLabel("Verificar!");
        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("12345");
        txtNome = new JTextField("Cliente");
        Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };  
        JOptionPane.showMessageDialog(null, texts);

        c = getContentPane();
        c.setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 64; i++) {
            if (i == 0 || i == 16 || i == 32 || i == 48) {
                corUltmaPeca = "Preto";
            } else if (i == 8 || i == 24 || i == 40 || i == 56) {
                corUltmaPeca = "branco";
            }
            if (corUltmaPeca == "Preto") {
                pecas[i] = new JButton();
                pecas[i].setBackground(Color.decode("#fecea0"));
                pecas[i].setEnabled(!pecas[i].isEnabled());
                corUltmaPeca = "branco";
            } else {
                pecas[i] = new JButton();
                pecas[i].setBackground(Color.decode("#d18b46"));

                corUltmaPeca = "Preto";

            }
            pecas[i].addActionListener(this);
            c.add(pecas[i]);
        }
        for (int i = 0; i < 64; i++) {
             switch (i) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                case 14:
                case 17:
                case 19:
                case 21:
                case 23:
                    pecas[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/dama/pkgfinal/PecaBranca.png")));
                    break;
                    
                case 40:
                case 42:
                case 44:
                case 46:
                case 49:
                case 51:
                case 53:
                case 55:
                case 56:
                case 58:
                case 60:
                case 62:
                    pecas[i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/dama/pkgfinal/PecaPreta.png")));
            }

        }
        setSize(600, 600);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        s = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
        ChatCliente c1 = new ChatCliente(txtIP, txtPorta);
c1.run();
    }

    public static void main(String[] args) throws IOException {
        DamaCliente jogo = new DamaCliente();
        // DamaTread d1 = new DamaTread(c1);
        // d1.run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         button = (JButton) e.getSource();
         String[] msg;
        Icon image = button.getIcon();
        System.out.println(image);
if(vezJogar != "servidor"){
        if (button.getBackground() == Color.BLUE) {
          MudarPeca(button);
          vezJogar = "servidor";
        } else if (image.toString().contains("PecaPreta")) {
            SelecionarAreaJogada(button,71,74);
        }
}
    }
    
    public void MudarPeca(JButton button1){
          casaClicada = button;
        int casaX = casaClicada.getLocation().x;
        int casaY = casaClicada.getLocation().y;
        int pecaX = PecaClicada.getLocation().x;
        int pecaY = PecaClicada.getLocation().y;
        PecaClicada.setBounds(casaX, casaY, button.getWidth(), button.getHeight());
        casaClicada.setBounds(pecaX, pecaY, button.getWidth(), button.getHeight());
        PecaClicada.setBackground(Color.decode("#d18b46"));
        casaClicada.setBackground(Color.decode("#d18b46"));
        System.out.println("casaX = " + casaX + "casaY" + casaY);
        System.out.println("pecaX = " + pecaX + "pecaY" + pecaY);
        if ((( pecaY - casaY) == 142) && ((pecaX-casaX) == 148)) {
            JButton bt = (JButton) c.getComponentAt(pecaX - 74, pecaY - 71);
            System.out.println(bt.getIcon());
            bt.setIcon(new javax.swing.ImageIcon(DamaCliente.class.getResource("/dama/pkgfinal/PecaTransparente.png")));
            PecasGanhadas++;
        }
        if (PecasGanhadas == 12) {
            JOptionPane.showInputDialog("Voce Ganhou");
        }
        casaAzul1.setBackground(Color.decode("#d18b46"));
        casaAzul2.setBackground(Color.decode("#d18b46"));
      MandarMensagem(pecaX, pecaY, casaX, casaY);
    }
    
    public void SelecionarAreaJogada(JButton button, int YM, int XM){
            PecaClicada = button;
            int x = button.getLocation().x;
            int Y = button.getLocation().y;
            if ((x + XM >= 75 && x + XM <= 519) && (Y - YM >= 1 && Y - YM <= 498)) {
                 Icon image = button.getIcon();
                 casaAzul1 = (JButton) c.getComponentAt(x + XM, Y - YM);
                 if((casaAzul1.getIcon() != null)&&(casaAzul1.getIcon().toString().contains("PecaBranca"))){
                   casaAzul1 = (JButton) c.getComponentAt(x + XM*2, Y - YM*2);   
                 }
                casaAzul1.setBackground(Color.BLUE);                
            }
            if ((x - 74 >= 1 && x - 74 <= 519) && (Y - 71 >= 1 && Y - 71 <= 498)) {
                casaAzul2 = (JButton) c.getComponentAt(x - XM, Y - YM);
                   if((casaAzul2.getIcon() != null) && (casaAzul2.getIcon().toString().contains("PecaBranca"))){
                     casaAzul2 = (JButton) c.getComponentAt(x - XM*2, Y - YM*2);  
                 }             
                casaAzul2.setBackground(Color.BLUE);
            }
    }
    
    public void MandarMensagem(int UltimoX, int UltimoY, int NovoX, int NovoY) {
        String msg = "[peca]" + UltimoX + ',' + UltimoY + ',' + NovoX + ',' + NovoY;
        try {
            dout.writeUTF(msg);            
        } catch (IOException ex) {
            Logger.getLogger(DamaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public static void ReceberMensagem(String msgin){
          String Msg = msgin;
      String[] msg;
         Msg = Msg.replace("[peca]", "");
           msg = Msg.split(",");
       
         JButton b1 = (JButton) c.getComponentAt(Integer.parseInt(msg[0]), Integer.parseInt(msg[1]));
         JButton b2 = (JButton) c.getComponentAt(Integer.parseInt(msg[2]), Integer.parseInt(msg[3]));
         System.out.println(b2.getIcon());
         b1.setBounds(Integer.parseInt(msg[2]), Integer.parseInt(msg[3]), 74,  71);
         b2.setBounds(Integer.parseInt(msg[0]), Integer.parseInt(msg[1]), 74,  71);
           if(((Integer.parseInt(msg[2])- Integer.parseInt(msg[0])) == 148)&&((Integer.parseInt(msg[3])-Integer.parseInt(msg[1])) == 142)){
             JButton b3 = (JButton) c.getComponentAt(Integer.parseInt(msg[0])+74, Integer.parseInt(msg[1])+71);           
             b3.setIcon(new javax.swing.ImageIcon(DamaCliente.class.getResource("/dama/pkgfinal/PecaTransparente.png")));
             PecasPerdidas--;
             if(PecasPerdidas==0){
                 JOptionPane.showInputDialog("Voce Perdeu");
             }
           }
           vezJogar = "cliente";
      }
}
