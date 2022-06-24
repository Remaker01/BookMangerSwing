package admin.iframe;

import dao.Dao;
import pojo.User;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserAddInternalFrame extends JInternalFrame {

    private final int frameWidth = 300;
    private final int frameHeight = 200;
    private JTextField userNameTextField;
    private JPasswordField userPasswordField;
    private JComboBox userRoleComboBox;
    private JButton addBtn;

    public UserAddInternalFrame() {
        initSizeLocation();
        initView();
        initEvent();
        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"�û���Ϣ���");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel();
        GridLayout gridLayout = new GridLayout(3,2);
        panel2.setLayout(gridLayout);
        JLabel userNameLabel = new JLabel("�û����ƣ�");
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameTextField = new JTextField();
        JLabel userPasswordLabel = new JLabel("�û����룺");
        userPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userPasswordField = new JPasswordField();
        JLabel userRoleLabel = new JLabel("�û���ɫ��");
        userRoleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userRoleComboBox = new JComboBox(new String[]{"�û�", "����Ա"});
        panel2.add(userNameLabel); panel2.add(userNameTextField);
        panel2.add(userPasswordLabel); panel2.add(userPasswordField);
        panel2.add(userRoleLabel); panel2.add(userRoleComboBox);

        JPanel panel3 = new JPanel();
        addBtn = new JButton("���");
        panel3.add(addBtn);

        panel1.add(panel2, BorderLayout.CENTER);
        panel1.add(panel3, BorderLayout.SOUTH);

        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        // TODO: 0707
        /*�����û����ơ������û����롱�͡��û���ɫ����ֵȡ��
        ʵ����һ��user���󣬽�user�����userName, userPassword, isAdmin�����������ú�
        ��user���󴫸�Dao.addUser(user)����������û�����Ӳ���
        ��������û��ķ��ؽṹ���������û���Ϣ��ӳɹ������û���Ϣ���ʧ�ܡ��ĶԻ���
        �ο�BookInfoAddInternalFrame���е�addBtn.addActionListener����
        */
        addBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {
                    JOptionPane.showMessageDialog(null, "�û����ֲ���Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(userPasswordField)) {
                    JOptionPane.showMessageDialog(null, "�û����벻��Ϊ��");
                    return;
                }

                String userName = userNameTextField.getText();
                String userPassword = new String(userPasswordField.getPassword());

                User user = new User();
                user.setUserName(userName);
                user.setUserPassword(userPassword);
                user.setAdmin(false);

                boolean flag = Dao.addUser(user);

                if(flag) {
                    JOptionPane.showMessageDialog(null, "�û���Ϣ��ӳɹ�");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "�û���Ϣ���ʧ��");
                    return;
                }
            }
    });

    }

}
