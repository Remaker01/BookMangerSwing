package frame;

import dao.Dao;
import pojo.User;
import utils.CreateIcon;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginFrame extends JFrame {// �̳и���JFrame������һ������ʵ�� ��¼����
    private final int frameWidth = 500,frameHeight = 400;
    private JLabel titleLabel,userNameLabel,userPasswordLabel,userRoleLabel;//��ǩ��,��ʾ ����Ա�������ݡ� ��ͼ��
    private JTextField userNameTextField;//�����ı������,ֻ������һ������
    private JPasswordField userPasswordTextField;//һ��swing����������
    private JComboBox userRoleComboBox;//������Ĭ������ģ�͡��ɽ���ť��ɱ༭�ֶ��������б���ϵ����
    private JButton loginBtn,registerBtn,resetBtn;//����һ���ޱ�ǩ�ı�����ͼ��İ�ť

    public LoginFrame() {
        initSizeLocation(); initView();//��ʼ����¼���ڵĳߴ� λ�� ��ͼ���
        initEvent(); setVisible(true);//ʵ�ֵ�¼����İ�ť�¼� ��¼���ڿɼ���
    }

    private void initSizeLocation() {//��ʼ����¼���ڵĳߴ��λ��
        setTitle("��¼");//���ô������
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//����һ�����ڣ������Ͻǡ�,ֻ�иô��ڻ�ر�
        setSize(frameWidth,frameHeight);//��¼���ڵĳߴ�
        setLocationRelativeTo(null);//���ô��������ָ�������λ�� null:�˴��ڽ�������Ļ�����롣
    }

    private void initView() {//��ʼ����¼�����ϵ���ͼ���
        //��¼��������壬�ܹ�4��1�У�ÿһ�зֱ���panel1, panel2, panel3, panel4
        JPanel mainPanel = new JPanel();//JPanel:�ڴ����������ʱ����ͼ�������
        mainPanel.setLayout(new GridLayout(4, 1));
        //panel1��ŵ�¼����Ĵ�����logo
        JPanel panel1 = new JPanel();
        titleLabel = new JLabel(); titleLabel.setText("ͼ�����ϵͳ");
        titleLabel.setFont(new Font("����", Font.PLAIN, 30));
        titleLabel.setIcon(CreateIcon.getIconFromFile("book.png"));
        panel1.add(titleLabel);

        //panel2��2��2�еĲ��֣����
        JPanel panel2 = new JPanel(); JPanel panel21 = new JPanel();
        GridLayout gridLayout21 = new GridLayout(2, 2);
        //�и���棬��������,��˳���������ҡ��������°ڷţ��޷�ֱ��ָ��Ҫ�ڷŵ�����

        gridLayout21.setVgap(10);
        //�������֮�䴹ֱ��࣬�����ڵ��������¿ؼ����������;setHgap:ˮƽ
        panel21.setLayout(gridLayout21); //�Լ�࣬���õ�ǰҳ�沼��
        panel21.setPreferredSize(new Dimension(300, 80));
        //������ѡ��С�������ʵ���Ͽ��ܲ��������С��ȡ��������������С�����û��Ƿ��ֶ����������С
//      ͬ�ࣺsetSize �����������С��ָ���Ĵ�С,Ҫ�ڲ�ʹ�ò��ֹ�����ʱʹ��,Ҳ����setLayout(null)
//      setLocation,setBounds
        userNameLabel = new JLabel(); userNameLabel.setText("��  ��  ����");
        userNameLabel.setFont(new Font("����", Font.PLAIN, 20)); //���������ʾЧ��
        //Font:JAVA�����࣬PLAIN��Font���еľ�̬����( static final ) ,��ʾ��:��ͨ��ʽ������
        // ������ʽ:BOLD :������ʽ���� ,ITALIC: б����ʽ����.
        userNameTextField = new JTextField();
        userNameTextField.setFont(new Font("����", Font.PLAIN, 20));
        userPasswordLabel = new JLabel(); userPasswordLabel.setText("��      �룺");
        userPasswordLabel.setFont(new Font("����", Font.PLAIN, 20));
        userPasswordTextField = new JPasswordField();
        userPasswordTextField.setFont(new Font("����", Font.PLAIN, 20));
        panel21.add(userNameLabel); panel21.add(userNameTextField);
        panel21.add(userPasswordLabel); panel21.add(userPasswordTextField);
        panel2.add(panel21);//JPanel.add():��ǩ��ķ�����Ϊָ�������ӵ�ָ��λ��

        //panel3���
        JPanel panel3 = new JPanel(); JPanel panel31 = new JPanel(); userRoleLabel = new JLabel();
        userRoleLabel.setFont(new Font("����", Font.PLAIN, 20));
        userRoleLabel.setText("��ɫ��");
        userRoleComboBox = new JComboBox(new String[]{"����", "����Ա"});
        userRoleComboBox.setFont(new Font("����", Font.PLAIN, 20));
        panel31.add(userRoleLabel);  panel31.add(userRoleComboBox); panel3.add(panel31);

        //panel4���
        JPanel panel4 = new JPanel(); JPanel panel41 = new JPanel();
        GridLayout gridLayout41 = new GridLayout(1, 3);
        gridLayout41.setHgap(30); panel41.setLayout(gridLayout41);//��55 ˮƽ����
        loginBtn = new JButton("��¼");
        loginBtn.setFont(new Font("����", Font.PLAIN, 20));
        resetBtn = new JButton("����");
        resetBtn.setFont(new Font("����", Font.PLAIN, 20));
        registerBtn = new JButton("ע��");
        registerBtn.setFont(new Font("����", Font.PLAIN, 20));
        panel41.add(loginBtn); panel41.add(resetBtn); panel41.add(registerBtn);
        panel4.add(panel41);

        //��4����嶼�����������
        mainPanel.add(panel1); mainPanel.add(panel2);
        mainPanel.add(panel3); mainPanel.add(panel4);
        getContentPane().add(mainPanel); //�����������ڵ�¼������
    }

    private void initEvent() {//ʵ�ֵ�¼��������İ�ť�¼�
        loginBtn.addActionListener(new AbstractAction() {//��¼��ť���ܵ�ʵ��
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {//�ж��û���������Ƿ�Ϊ��
                    JOptionPane.showMessageDialog(null, "�û�������Ϊ��");
                    return;
                }

                if(EmptyUtils.isEmpty(userPasswordTextField)) {//�ж�����������Ƿ�Ϊ��
                    JOptionPane.showMessageDialog(null, "���벻��Ϊ��");
                    return;
                }

                String userName = userNameTextField.getText();//��ȡ�û���
                String userPassword = new String(userPasswordTextField.getPassword());//��ȡ����
                int authority = userRoleComboBox.getSelectedIndex();//��ȡ�û���ɫ

                User user = new User(); //ʵ����һ��user���󣬽��û���������ͽ�ɫ��ʼ��
                user.setUserName(userName);  user.setUserPassword(userPassword);
                user.setAdmin(authority != 0); //�ж�ְλ 0:��ͨ�û� 1:����Ա

                //user���󴫵ݸ�Dao.loginCheck�������������ݿ��ѯ����ѯ�����ֵ��newUser����
                User newUser = Dao.loginCheck(user);
                Global.user = newUser;//Global.user�б��浱ǰ��¼���û���Ϣ
                //Global:ȫ�ֱ����������ṩӦ�ó������Ĺ��򣬼������ṩ���ݻ��߷���ʹ�õĹ���

                if(newUser==null) { //�����ѯ�����null��˵����ѯʧ�ܣ�û���ҵ�����û�
                    JOptionPane.showMessageDialog(null, "�û������������");
                } else {
                    if(authority==0&& newUser.isAdmin()) {
                        //���newUser�ǹ���Ա�����������б��ǡ����ߡ������û����Ͳ�ƥ��
                        JOptionPane.showMessageDialog(null, "���˻����Ƕ����˻�");
                    } else if(authority==1&& !newUser.isAdmin()) {
                        //���newUser���ǹ���Ա�����������б��ǡ�����Ա�������û����Ͳ�ƥ��
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "���˻����ǹ���Ա�˻�","��Ϣ�Ի���", JOptionPane.WARNING_MESSAGE);
                    } else {
                        dispose();//�ر�һ��GUIҳ��
                        if(!newUser.isAdmin())//���newUser���ǹ���Ա��򿪡��û������桱
                            new UserFrame();
                        else
                            new AdminFrame();//���newUser�ǹ���Ա��򿪡�����Ա�����桱
                    }
                }
            }
        });

        resetBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                userNameTextField.setText(""); userPasswordTextField.setText("");
                userRoleComboBox.setSelectedIndex(0);//��ɫ������ֵ������0�����ص�һ��ֵ
                userNameTextField.grabFocus();//���㷽��//userNameTextField.requestFocus();
            }
        });

        userPasswordTextField.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                loginBtn.doClick();
            }
        });

        registerBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterFrame();
            }
        });
    }
}
