package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TranferMoney extends JFrame implements ActionListener {
    JButton b1, b2;
    JTextField tfReceiver, tfAmount;
    JPasswordField pfPin;
    JLabel lblReceiverName;
    String card_number;

    TranferMoney(String card_number) {
        this.card_number = card_number;

        // Hình nền
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 830);
        add(l3);

        // Tiêu đề
        JLabel label1 = new JLabel("CHUYỂN TIỀN");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(430, 180, 400, 35);
        l3.add(label1);

        // Tài khoản nhận
        JLabel label2 = new JLabel("Tài khoản nhận:");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(430, 220, 150, 35);
        l3.add(label2);

        tfReceiver = new JTextField();
        tfReceiver.setBackground(new Color(65, 125, 128));
        tfReceiver.setForeground(Color.WHITE);
        tfReceiver.setBounds(600, 220, 180, 25);
        tfReceiver.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(tfReceiver);

        // Tên người nhận (tự động hiện)
        JLabel lblName = new JLabel("Tên người nhận:");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("System", Font.BOLD, 16));
        lblName.setBounds(430, 255, 150, 35);
        l3.add(lblName);

        lblReceiverName = new JLabel("");
        lblReceiverName.setForeground(Color.YELLOW);
        lblReceiverName.setFont(new Font("Raleway", Font.BOLD, 18));
        lblReceiverName.setBounds(600, 255, 300, 35);
        l3.add(lblReceiverName);

        // Số tiền
        JLabel label3 = new JLabel("Số tiền chuyển:");
        label3.setForeground(Color.WHITE);
        label3.setFont(new Font("System", Font.BOLD, 16));
        label3.setBounds(430, 290, 150, 35);
        l3.add(label3);

        tfAmount = new JTextField();
        tfAmount.setBackground(new Color(65, 125, 128));
        tfAmount.setForeground(Color.WHITE);
        tfAmount.setBounds(600, 295, 180, 25);
        tfAmount.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(tfAmount);

        // Nhập lại mã PIN
        JLabel label4 = new JLabel("Nhập mã PIN:");
        label4.setForeground(Color.WHITE);
        label4.setFont(new Font("System", Font.BOLD, 16));
        label4.setBounds(430, 325, 150, 35);
        l3.add(label4);

        pfPin = new JPasswordField();
        pfPin.setBackground(new Color(65, 125, 128));
        pfPin.setForeground(Color.WHITE);
        pfPin.setBounds(600, 330, 180, 25);
        pfPin.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(pfPin);

        // Nút chuyển tiền
        b1 = new JButton("CHUYỂN TIỀN");
        b1.setBounds(700, 362, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        // Nút quay lại
        b2 = new JButton("QUAY LẠI");
        b2.setBounds(700, 406, 150, 35);
        b2.setBackground(new Color(65, 125, 128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        // Sự kiện: Tự động lấy tên người nhận khi rời khỏi ô tài khoản
        tfReceiver.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String receiverCard = tfReceiver.getText().trim();
                if (!receiverCard.isEmpty()) {
                    try {
                        Connn c = new Connn();
                        ResultSet rs = c.statement.executeQuery("SELECT name FROM ATM_account WHERE card_number = '" + receiverCard + "'");
                        if (rs.next()) {
                            String receiverName = rs.getString("name");
                            lblReceiverName.setText(receiverName);
                        } else {
                            lblReceiverName.setText("Không tìm thấy");
                        }
                    } catch (Exception ex) {
                        lblReceiverName.setText("Lỗi hệ thống");
                        ex.printStackTrace();
                    }
                }
            }
        });

        setSize(1550, 1080);
        setLayout(null);
        setLocation(0, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String receiverCard = tfReceiver.getText().trim();
            String amountStr = tfAmount.getText().trim();
            String pin = new String(pfPin.getPassword()).trim();

            if (e.getSource() == b1) {
                if (receiverCard.isEmpty() || amountStr.isEmpty() || pin.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin");
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Số tiền phải lớn hơn 0");
                    return;
                }
                Connn c = new Connn();
                ResultSet rsSender = c.statement.executeQuery("SELECT * FROM ATM_account WHERE card_number='" + card_number + "' AND pin='" + pin + "'");
                if (!rsSender.next()) {
                    JOptionPane.showMessageDialog(null, "Mã PIN không đúng");
                    return;
                }

                double senderBalance = rsSender.getDouble("balance");
                if (senderBalance < amount) {
                    JOptionPane.showMessageDialog(null, "Số dư không đủ");
                    return;
                }

                ResultSet rsReceiver = c.statement.executeQuery("SELECT * FROM ATM_account WHERE card_number='" + receiverCard + "'");
                if (!rsReceiver.next()) {
                    JOptionPane.showMessageDialog(null, "Tài khoản người nhận không tồn tại");
                    return;
                }

                double receiverBalance = rsReceiver.getDouble("balance");

                c.statement.executeUpdate("UPDATE ATM_account SET balance=" + (senderBalance - amount) + " WHERE card_number='" + card_number + "'");
                c.statement.executeUpdate("UPDATE ATM_account SET balance=" + (receiverBalance + amount) + " WHERE card_number='" + receiverCard + "'");

                JOptionPane.showMessageDialog(null, "Chuyển tiền thành công");
                setVisible(false);
                new main_Class(card_number);

            } else if (e.getSource() == b2) {
                setVisible(false);
                new main_Class(card_number);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Số tiền không hợp lệ");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống khi chuyển tiền");
        }
    }

    public static void main(String[] args) {
        new TranferMoney("");
    }
}
