package user.iframe;

import dao.Dao;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*�޸������Ӵ���
 * */
public class ModifyPasswordInternalFrame extends JInternalFrame {

    private final int frameWidth = 300,frameHeight = 200;
    private JTextField userNameTextField;
    private JPasswordField oldUserPasswordTextField,newUserPasswordTextField,confirmUserPasswordTextField;
    private JButton confirmBtn;

    public ModifyPasswordInternalFrame() {
        initSizeLocation();  initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"�޸�����");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout()); JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel(); GridLayout gridLayout = new GridLayout(4,2);
        panel2.setLayout(gridLayout); JLabel userNameLabel = new JLabel("�û�����");
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameTextField = new JTextField(); userNameTextField.setEditable(false);
        userNameTextField.setText(Global.user.getUserName());
        JLabel oldUserPasswordLabel = new JLabel("�����룺");
        oldUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        oldUserPasswordTextField = new JPasswordField();
        JLabel newUserPasswordLabel = new JLabel("�����룺");
        newUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newUserPasswordTextField = new JPasswordField();
        JLabel confirmUserPasswordLabel = new JLabel("ȷ�����룺");
        confirmUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmUserPasswordTextField = new JPasswordField();
        panel2.add(userNameLabel); panel2.add(userNameTextField);
        panel2.add(oldUserPasswordLabel); panel2.add(oldUserPasswordTextField);
        panel2.add(newUserPasswordLabel); panel2.add(newUserPasswordTextField);
        panel2.add(confirmUserPasswordLabel); panel2.add(confirmUserPasswordTextField);

        JPanel panel3 = new JPanel(); confirmBtn = new JButton("�޸�");
        panel3.add(confirmBtn);

        panel1.add(panel2, BorderLayout.CENTER); panel1.add(panel3, BorderLayout.SOUTH);
        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        confirmBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // TODO:  0301
                /*���޸����롱�Ӵ����еġ��޸ġ���ť���֮��
                �жϡ������롱���������롱�͡�ȷ�����롱�������������ݲ���Ϊ��
                д3��if�������жϣ����Ϊ���򵯳���ʾ��Ϣ��������
                �ο���¼����ġ���¼����ť����¼�
                */
                if(EmptyUtils.isEmpty(oldUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "�����벻��Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(newUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "�����벻��Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(confirmUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "ȷ�����벻��Ϊ��");
                    return;
                }

                String oldUserPassword = new String(oldUserPasswordTextField.getPassword());

                if(!oldUserPassword.equals(Global.user.getUserPassword())) {
                    JOptionPane.showMessageDialog(null, "�����벻��");
                } else {
                    String newUserPassword = new String(newUserPasswordTextField.getPassword());
                    String confirmUserPassword = new String(confirmUserPasswordTextField.getPassword());
                    if(newUserPassword.equals(confirmUserPassword)) {
                        boolean flag = Dao.modifyUserPassword(Global.user.getUserId(), newUserPassword);
                        if(flag) {
                            JOptionPane.showMessageDialog(null, "�����޸ĳɹ�");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "�����޸�ʧ��");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "�������벻ͬ");
                    }
                }
            }
        });
    }
}
