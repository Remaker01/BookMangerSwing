package frame;

import dao.Dao;
import pojo.User;
import utils.CreateIcon;
import utils.EmptyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterFrame extends JFrame {
    private final int frameWidth = 500,frameHeight = 400;
    private JLabel titleLabel,userNameLabel,userPasswordLabel,confirmUserPasswordLabel;
    private JTextField userNameTextField;
    private JPasswordField userPasswordTextField,confirmUserPasswordTextField;
    private JButton registerBtn,resetBtn;

    public RegisterFrame() {//��ʼ����¼���ڵĳߴ� λ�� ��ͼ��� ʵ�ֵ�¼����İ�ť�¼� ��¼���ڿɼ���
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    void initSizeLocation() {
        setTitle("ע��");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);
    }

    private void initView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JPanel panel1 = new JPanel();
        titleLabel = new JLabel(); titleLabel.setText("�û�ע��");
        titleLabel.setFont(new Font("����", Font.PLAIN, 30));
        titleLabel.setIcon(CreateIcon.getIconFromFile("user.png"));
        panel1.add(titleLabel);

        JPanel panel2 = new JPanel(); JPanel panel21 = new JPanel();
        GridLayout gridLayout21 = new GridLayout(3, 2);
        gridLayout21.setVgap(10);
        panel21.setLayout(gridLayout21);
        panel21.setPreferredSize(new Dimension(300, 80));
        userNameLabel = new JLabel(); userNameLabel.setText("��  ��  ����");
        userNameLabel.setFont(new Font("����", Font.PLAIN, 20));
        userNameTextField = new JTextField();
        userNameTextField.setFont(new Font("����", Font.PLAIN, 20));
        userPasswordLabel = new JLabel();
        userPasswordLabel.setText("��      �룺");
        userPasswordLabel.setFont(new Font("����", Font.PLAIN, 20));
        userPasswordTextField = new JPasswordField();
        userPasswordTextField.setFont(new Font("����", Font.PLAIN, 20));
        confirmUserPasswordLabel = new JLabel("ȷ �� �� �룺");
        confirmUserPasswordLabel.setFont(new Font("����", Font.PLAIN, 20));
        confirmUserPasswordTextField = new JPasswordField();
        confirmUserPasswordTextField.setFont(new Font("����", Font.PLAIN, 20));
        panel21.add(userNameLabel); panel21.add(userNameTextField);
        panel21.add(userPasswordLabel); panel21.add(userPasswordTextField);
        panel21.add(confirmUserPasswordLabel); panel21.add(confirmUserPasswordTextField);
        panel2.add(panel21);

        JPanel panel3 = new JPanel(); JPanel panel31 = new JPanel();
        GridLayout gridLayout31 = new GridLayout(1, 2);
        gridLayout31.setHgap(30);
        panel31.setLayout(gridLayout31);
        resetBtn = new JButton("����");
        resetBtn.setFont(new Font("����", Font.PLAIN, 20));
        registerBtn = new JButton("ע��");
        registerBtn.setFont(new Font("����", Font.PLAIN, 20));
        panel31.add(resetBtn); panel31.add(registerBtn); panel3.add(panel31);

        mainPanel.add(panel1); mainPanel.add(panel2); mainPanel.add(panel3);
        getContentPane().add(mainPanel);
    }

    private void initEvent() { //ע�ᴰ�ڹر�֮�󣬴򿪵�¼����
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                new LoginFrame();
            }
        });

        // TODO: 0202
        //���ð�ť�����û����������ȷ��������գ��û���������ȡ����
        /*������ð�ť�Ĺ���
        �����û������������롱�͡�ȷ�����롱����������������� ���û�����������ý���
        */
        resetBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                userNameTextField.setText(""); userPasswordTextField.setText("");
                confirmUserPasswordTextField.setText(""); userNameTextField.requestFocus();
            }
        });

        //ע�ᰴť �����������Ϊ�գ��������������ͬ���û��������Ѵ���
        //ע��ɹ�֮��ر�ע�ᴰ�ڣ��򿪵�¼����
        registerBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {
                    JOptionPane.showMessageDialog(null, "�û�������Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(userPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "���벻��Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(confirmUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "ȷ�����벻��Ϊ��");
                    return;
                }

                String userName = userNameTextField.getText();
                String userPassword = new String(userPasswordTextField.getPassword());
                String confirmUserPassword = new String(confirmUserPasswordTextField.getPassword());

                if(!userPassword.equals(confirmUserPassword)) {
                    JOptionPane.showMessageDialog(null, "�������벻ͬ");
                    return;
                }

                User user = new User(); user.setUserName(userName);
                user.setUserPassword(userPassword); user = Dao.registerUser(user);

                if(user == null) {
                    JOptionPane.showMessageDialog(null, "�û����Ѵ��ڣ�����ע��");
                } else {
                    JOptionPane.showMessageDialog(null, "ע��ɹ�");
                    dispose();
                }
            }
        });
    }
}
