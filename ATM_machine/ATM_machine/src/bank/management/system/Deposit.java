package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Deposit extends JFrame implements ActionListener {
   String card_number;
   TextField textField;

   JButton b1, b2;
    Deposit(String card_number){
        this.card_number = card_number;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550,830,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0,0,1550,830);
        add(l3);

        JLabel label1 = new JLabel("NHẬP SỐ TIỀN MUỐN NẠP: ");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(460,180,400,35);
        l3.add(label1);

        textField = new TextField();
        textField.setBackground(new Color(65,125,128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460,230,320,25);
        textField.setFont(new Font("Raleway", Font.BOLD,22));
        l3.add(textField);

        b1 = new JButton("NẠP TIỀN");
        b1.setBounds(700,362,150,35);
        b1.setBackground(new Color(65,125,128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(700,406,150,35);
        b2.setBackground(new Color(65,125,128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);




        setLayout(null);
        setSize(1550,1080);
        setLocation(0,0);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int amount = Integer.parseInt(textField.getText());
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            if (e.getSource()==b1){
                if (textField.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"HÃY NHẬP SỐ TIỀN MUỐN NẠP");
                }else {
                    Connn c = new Connn();
                    c.statement.executeUpdate("insert into ATM_transaction values('"+card_number+"', '"+date+"','Deposit', "+amount+")");
                    int balance = BalanceEnquriy.get_balance(card_number) + amount;
                    c.statement.executeUpdate("update ATM_account set balance ="+ balance +" where card_number = '"+ card_number +"'");
                    JOptionPane.showMessageDialog(null,amount + " VND. "+" NẠP TIỀN THÀNH CÔNG");
                    setVisible(false);
                    new main_Class(card_number);
                }
            }else if (e.getSource()==b2){
                setVisible(false);
                new main_Class(card_number);
            }
        }catch (Exception E){
            E.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new Deposit("");
    }
}
