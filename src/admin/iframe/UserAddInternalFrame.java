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
        setTitle(Global.user.getUserName()+"用户信息添加");
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
        JLabel userNameLabel = new JLabel("用户名称：");
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameTextField = new JTextField();
        JLabel userPasswordLabel = new JLabel("用户密码：");
        userPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userPasswordField = new JPasswordField();
        JLabel userRoleLabel = new JLabel("用户角色：");
        userRoleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userRoleComboBox = new JComboBox(new String[]{"用户", "管理员"});
        panel2.add(userNameLabel); panel2.add(userNameTextField);
        panel2.add(userPasswordLabel); panel2.add(userPasswordField);
        panel2.add(userRoleLabel); panel2.add(userRoleComboBox);

        JPanel panel3 = new JPanel();
        addBtn = new JButton("添加");
        panel3.add(addBtn);

        panel1.add(panel2, BorderLayout.CENTER);
        panel1.add(panel3, BorderLayout.SOUTH);

        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        // TODO: 0707
        /*将“用户名称”，“用户密码”和“用户角色”的值取出
        实例化一个user对象，将user对象的userName, userPassword, isAdmin三个属性设置好
        将user对象传给Dao.addUser(user)方法，完成用户的添加操作
        根据添加用户的返回结构，弹出“用户信息添加成功”或“用户信息添加失败”的对话框
        参考BookInfoAddInternalFrame类中的addBtn.addActionListener方法
        */
        addBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {
                    JOptionPane.showMessageDialog(null, "用户名字不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(userPasswordField)) {
                    JOptionPane.showMessageDialog(null, "用户密码不能为空");
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
                    JOptionPane.showMessageDialog(null, "用户信息添加成功");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "用户信息添加失败");
                    return;
                }
            }
    });

    }

}
