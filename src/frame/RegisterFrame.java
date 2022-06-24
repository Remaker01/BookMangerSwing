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

    public RegisterFrame() {//初始化登录窗口的尺寸 位置 视图组件 实现登录界面的按钮事件 登录窗口可见化
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    void initSizeLocation() {
        setTitle("注册");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeight); setLocationRelativeTo(null);
    }

    private void initView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JPanel panel1 = new JPanel();
        titleLabel = new JLabel(); titleLabel.setText("用户注册");
        titleLabel.setFont(new Font("宋体", Font.PLAIN, 30));
        titleLabel.setIcon(CreateIcon.getIconFromFile("user.png"));
        panel1.add(titleLabel);

        JPanel panel2 = new JPanel(); JPanel panel21 = new JPanel();
        GridLayout gridLayout21 = new GridLayout(3, 2);
        gridLayout21.setVgap(10);
        panel21.setLayout(gridLayout21);
        panel21.setPreferredSize(new Dimension(300, 80));
        userNameLabel = new JLabel(); userNameLabel.setText("用  户  名：");
        userNameLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userNameTextField = new JTextField();
        userNameTextField.setFont(new Font("宋体", Font.PLAIN, 20));
        userPasswordLabel = new JLabel();
        userPasswordLabel.setText("密      码：");
        userPasswordLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userPasswordTextField = new JPasswordField();
        userPasswordTextField.setFont(new Font("宋体", Font.PLAIN, 20));
        confirmUserPasswordLabel = new JLabel("确 认 密 码：");
        confirmUserPasswordLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        confirmUserPasswordTextField = new JPasswordField();
        confirmUserPasswordTextField.setFont(new Font("宋体", Font.PLAIN, 20));
        panel21.add(userNameLabel); panel21.add(userNameTextField);
        panel21.add(userPasswordLabel); panel21.add(userPasswordTextField);
        panel21.add(confirmUserPasswordLabel); panel21.add(confirmUserPasswordTextField);
        panel2.add(panel21);

        JPanel panel3 = new JPanel(); JPanel panel31 = new JPanel();
        GridLayout gridLayout31 = new GridLayout(1, 2);
        gridLayout31.setHgap(30);
        panel31.setLayout(gridLayout31);
        resetBtn = new JButton("重置");
        resetBtn.setFont(new Font("宋体", Font.PLAIN, 20));
        registerBtn = new JButton("注册");
        registerBtn.setFont(new Font("宋体", Font.PLAIN, 20));
        panel31.add(resetBtn); panel31.add(registerBtn); panel3.add(panel31);

        mainPanel.add(panel1); mainPanel.add(panel2); mainPanel.add(panel3);
        getContentPane().add(mainPanel);
    }

    private void initEvent() { //注册窗口关闭之后，打开登录窗口
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                new LoginFrame();
            }
        });

        // TODO: 0202
        //重置按钮，将用户名、密码和确认密码清空，用户名输入框获取焦点
        /*完成重置按钮的功能
        将“用户名”、“密码”和“确认密码”三个输入框的内容清空 “用户名”输入框获得焦点
        */
        resetBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                userNameTextField.setText(""); userPasswordTextField.setText("");
                confirmUserPasswordTextField.setText(""); userNameTextField.requestFocus();
            }
        });

        //注册按钮 三个输入框不能为空，两次密码必须相同，用户名不能已存在
        //注册成功之后关闭注册窗口，打开登录窗口
        registerBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {
                    JOptionPane.showMessageDialog(null, "用户名不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(userPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "密码不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(confirmUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "确认密码不能为空");
                    return;
                }

                String userName = userNameTextField.getText();
                String userPassword = new String(userPasswordTextField.getPassword());
                String confirmUserPassword = new String(confirmUserPasswordTextField.getPassword());

                if(!userPassword.equals(confirmUserPassword)) {
                    JOptionPane.showMessageDialog(null, "两次密码不同");
                    return;
                }

                User user = new User(); user.setUserName(userName);
                user.setUserPassword(userPassword); user = Dao.registerUser(user);

                if(user == null) {
                    JOptionPane.showMessageDialog(null, "用户名已存在，重新注册");
                } else {
                    JOptionPane.showMessageDialog(null, "注册成功");
                    dispose();
                }
            }
        });
    }
}
