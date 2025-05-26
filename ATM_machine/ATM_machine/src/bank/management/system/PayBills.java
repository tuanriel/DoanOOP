package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayBills extends JFrame implements ActionListener {
    JTextField tfBillId, tfAmount;
    JComboBox<String> cbBillType;
    JPasswordField pfPin;
    JButton b1, b2;
    JLabel lblStatus;
    String card_number;

    PayBills(String card_number) {
        this.card_number = card_number;

        // Background
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 830);
        add(l3);

        JLabel label1 = new JLabel("THANH TOÁN HÓA ĐƠN");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(430, 180, 400, 35);
        l3.add(label1);

        JLabel label2 = new JLabel("Mã hóa đơn:");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(430, 220, 150, 35);
        l3.add(label2);

        tfBillId = new JTextField();
        tfBillId.setBackground(new Color(65, 125, 128));
        tfBillId.setForeground(Color.WHITE);
        tfBillId.setFont(new Font("Raleway", Font.BOLD, 22));
        tfBillId.setBounds(600, 220, 180, 25);
        l3.add(tfBillId);

        JLabel label3 = new JLabel("Loại hóa đơn:");
        label3.setForeground(Color.WHITE);
        label3.setFont(new Font("System", Font.BOLD, 16));
        label3.setBounds(430, 255, 150, 35);
        l3.add(label3);

        String[] billTypes = {"Điện", "Nước", "Internet", "Điện thoại", "Khác"};
        cbBillType = new JComboBox<>(billTypes);
        cbBillType.setBounds(600, 255, 180, 25);
        cbBillType.setBackground(new Color(65, 125, 128));
        cbBillType.setForeground(Color.WHITE);
        cbBillType.setFont(new Font("Raleway", Font.BOLD, 18));
        l3.add(cbBillType);

        JLabel label4 = new JLabel("Số tiền:");
        label4.setForeground(Color.WHITE);
        label4.setFont(new Font("System", Font.BOLD, 16));
        label4.setBounds(430, 290, 150, 35);
        l3.add(label4);

        tfAmount = new JTextField();
        tfAmount.setBackground(new Color(65, 125, 128));
        tfAmount.setForeground(Color.WHITE);
        tfAmount.setFont(new Font("Raleway", Font.BOLD, 22));
        tfAmount.setBounds(600, 295, 180, 25);
        tfAmount.setEditable(false); // chỉ cho hệ thống tự điền
        l3.add(tfAmount);

        JLabel label5 = new JLabel("Mã PIN:");
        label5.setForeground(Color.WHITE);
        label5.setFont(new Font("System", Font.BOLD, 16));
        label5.setBounds(430, 325, 150, 35);
        l3.add(label5);

        pfPin = new JPasswordField();
        pfPin.setBackground(new Color(65, 125, 128));
        pfPin.setForeground(Color.WHITE);
        pfPin.setFont(new Font("Raleway", Font.BOLD, 22));
        pfPin.setBounds(600, 330, 180, 25);
        l3.add(pfPin);

        lblStatus = new JLabel("");
        lblStatus.setForeground(Color.YELLOW);
        lblStatus.setFont(new Font("System", Font.BOLD, 16));
        lblStatus.setBounds(430, 360, 400, 30);
        l3.add(lblStatus);

        b1 = new JButton("THANH TOÁN");
        b1.setBounds(700, 400, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("QUAY LẠI");
        b2.setBounds(700, 445, 150, 35);
        b2.setBackground(new Color(65, 125, 128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        // Sự kiện khi rời khỏi ô Mã hóa đơn
        tfBillId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                checkAndFillBillInfo();
            }
        });

        cbBillType.addActionListener(e -> checkAndFillBillInfo());

        setSize(1550, 1080);
        setLayout(null);
        setLocation(0, 0);
        setVisible(true);
    }

    private void checkAndFillBillInfo() {
        String billId = tfBillId.getText().trim();
        String billType = cbBillType.getSelectedItem().toString();
        try {
            if (!billId.isEmpty()) {
                Connn c = new Connn();
                String q = "SELECT amount, is_paid FROM bill_transactions " +
                        "WHERE bill_id = '" + billId + "' AND bill_type = N'" + billType + "'";
                ResultSet rs = c.statement.executeQuery(q);
                if (rs.next()) {
                    int amount = rs.getInt("amount");
                    int isPaid = rs.getInt("is_paid");
                    tfAmount.setText(String.valueOf(amount));

                    if (isPaid == 1) {
                        lblStatus.setText("⚠️ Hóa đơn này đã được thanh toán.");
                        b1.setEnabled(false);
                    } else {
                        lblStatus.setText("✅ Hóa đơn chưa thanh toán.");
                        b1.setEnabled(true);
                    }
                } else {
                    tfAmount.setText("");
                    lblStatus.setText("⚠️ Không tìm thấy hóa đơn.");
                    b1.setEnabled(false);
                }
            } else {
                tfAmount.setText("");
                lblStatus.setText("");
                b1.setEnabled(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tfAmount.setText("");
            lblStatus.setText("⚠️ Lỗi truy vấn hóa đơn.");
            b1.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String billId = tfBillId.getText().trim();
            String billType = cbBillType.getSelectedItem().toString();
            String amountStr = tfAmount.getText().trim();
            String pin = new String(pfPin.getPassword()).trim();

            if (e.getSource() == b1) {
                if (billId.isEmpty() || amountStr.isEmpty() || pin.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin");
                    return;
                }

                int amount;
                try {
                    amount = Integer.parseInt(amountStr);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(null, "Số tiền phải lớn hơn 0");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Số tiền không hợp lệ");
                    return;
                }

                Connn c = new Connn();
                ResultSet rs = c.statement.executeQuery("SELECT * FROM ATM_account WHERE card_number = '" + card_number + "' AND pin = '" + pin + "'");
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Mã PIN không đúng");
                    return;
                }

                int balance = rs.getInt("balance");
                if (balance < amount) {
                    JOptionPane.showMessageDialog(null, "Số dư không đủ để thanh toán");
                    return;
                }

                // Trừ tiền
                int newBalance = balance - amount;
                c.statement.executeUpdate("UPDATE ATM_account SET balance = " + newBalance + " WHERE card_number = '" + card_number + "'");

                // Cập nhật hóa đơn
                String update = "UPDATE bill_transactions SET card_number = '" + card_number + "', is_paid = 1, paid_at = GETDATE() " +
                        "WHERE bill_id = '" + billId + "' AND bill_type = N'" + billType + "'";
                c.statement.executeUpdate(update);

                JOptionPane.showMessageDialog(null, "Thanh toán thành công!");
                setVisible(false);
                new main_Class(card_number);
            }

            if (e.getSource() == b2) {
                setVisible(false);
                new main_Class(card_number);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống khi thanh toán");
        }
    }

    public static void main(String[] args) {
        new PayBills("9704000012345678");
    }
}
