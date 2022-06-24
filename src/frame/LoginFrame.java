package frame;

import dao.Dao;
import pojo.User;
import utils.CreateIcon;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginFrame extends JFrame {// 继承父类JFrame，创建一个窗体实例 登录窗体
    private final int frameWidth = 500,frameHeight = 400;
    private JLabel titleLabel,userNameLabel,userPasswordLabel,userRoleLabel;//标签类,显示 “成员变量内容” 的图标
    private JTextField userNameTextField;//单行文本输入框,只可输入一行文字
    private JPasswordField userPasswordTextField;//一种swing组件，密码框
    private JComboBox userRoleComboBox;//创建有默认数据模型、可将按钮或可编辑字段与下拉列表组合的组件
    private JButton loginBtn,registerBtn,resetBtn;//创建一个无标签文本、无图标的按钮

    public LoginFrame() {
        initSizeLocation(); initView();//初始化登录窗口的尺寸 位置 视图组件
        initEvent(); setVisible(true);//实现登录界面的按钮事件 登录窗口可见化
    }

    private void initSizeLocation() {//初始化登录窗口的尺寸和位置
        setTitle("登录");//设置窗体标题
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//设置一个窗口，点右上角×,只有该窗口会关闭
        setSize(frameWidth,frameHeight);//登录窗口的尺寸
        setLocationRelativeTo(null);//设置窗口相对于指定组件的位置 null:此窗口将置于屏幕的中央。
    }

    private void initView() {//初始化登录界面上的视图组件
        //登录界面主面板，总共4行1列，每一行分别存放panel1, panel2, panel3, panel4
        JPanel mainPanel = new JPanel();//JPanel:在创建面板容器时设置图层管理器
        mainPanel.setLayout(new GridLayout(4, 1));
        //panel1存放登录界面的大标题和logo
        JPanel panel1 = new JPanel();
        titleLabel = new JLabel(); titleLabel.setText("图书管理系统");
        titleLabel.setFont(new Font("宋体", Font.PLAIN, 30));
        titleLabel.setIcon(CreateIcon.getIconFromFile("book.png"));
        panel1.add(titleLabel);

        //panel2是2行2列的布局，存放
        JPanel panel2 = new JPanel(); JPanel panel21 = new JPanel();
        GridLayout gridLayout21 = new GridLayout(2, 2);
        //切割版面，加入的组件,按顺序由左至右、由上至下摆放，无法直接指定要摆放的区域。

        gridLayout21.setVgap(10);
        //设置组件之间垂直间距，容器内的俩个上下控件的相隔距离;setHgap:水平
        panel21.setLayout(gridLayout21); //以间距，设置当前页面布局
        panel21.setPreferredSize(new Dimension(300, 80));
        //设置首选大小，该组件实际上可能不是这个大小，取决于所在容器大小，或用户是否手动调整组件大小
//      同类：setSize 将调整组件大小到指定的大小,要在不使用布局管理器时使用,也就是setLayout(null)
//      setLocation,setBounds
        userNameLabel = new JLabel(); userNameLabel.setText("用  户  名：");
        userNameLabel.setFont(new Font("宋体", Font.PLAIN, 20)); //设计字体显示效果
        //Font:JAVA字体类，PLAIN是Font类中的静态常量( static final ) ,表示是:普通样式常量。
        // 其他样式:BOLD :粗体样式常量 ,ITALIC: 斜体样式常量.
        userNameTextField = new JTextField();
        userNameTextField.setFont(new Font("宋体", Font.PLAIN, 20));
        userPasswordLabel = new JLabel(); userPasswordLabel.setText("密      码：");
        userPasswordLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userPasswordTextField = new JPasswordField();
        userPasswordTextField.setFont(new Font("宋体", Font.PLAIN, 20));
        panel21.add(userNameLabel); panel21.add(userNameTextField);
        panel21.add(userPasswordLabel); panel21.add(userPasswordTextField);
        panel2.add(panel21);//JPanel.add():标签类的方法，为指定组件添加到指定位置

        //panel3存放
        JPanel panel3 = new JPanel(); JPanel panel31 = new JPanel(); userRoleLabel = new JLabel();
        userRoleLabel.setFont(new Font("宋体", Font.PLAIN, 20));
        userRoleLabel.setText("角色：");
        userRoleComboBox = new JComboBox(new String[]{"读者", "管理员"});
        userRoleComboBox.setFont(new Font("宋体", Font.PLAIN, 20));
        panel31.add(userRoleLabel);  panel31.add(userRoleComboBox); panel3.add(panel31);

        //panel4存放
        JPanel panel4 = new JPanel(); JPanel panel41 = new JPanel();
        GridLayout gridLayout41 = new GridLayout(1, 3);
        gridLayout41.setHgap(30); panel41.setLayout(gridLayout41);//行55 水平距离
        loginBtn = new JButton("登录");
        loginBtn.setFont(new Font("宋体", Font.PLAIN, 20));
        resetBtn = new JButton("重置");
        resetBtn.setFont(new Font("宋体", Font.PLAIN, 20));
        registerBtn = new JButton("注册");
        registerBtn.setFont(new Font("宋体", Font.PLAIN, 20));
        panel41.add(loginBtn); panel41.add(resetBtn); panel41.add(registerBtn);
        panel4.add(panel41);

        //将4个面板都放在主面板中
        mainPanel.add(panel1); mainPanel.add(panel2);
        mainPanel.add(panel3); mainPanel.add(panel4);
        getContentPane().add(mainPanel); //将主面板放置在登录界面中
    }

    private void initEvent() {//实现登录界面上面的按钮事件
        loginBtn.addActionListener(new AbstractAction() {//登录按钮功能的实现
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(userNameTextField)) {//判断用户名输入框是否为空
                    JOptionPane.showMessageDialog(null, "用户名不能为空");
                    return;
                }

                if(EmptyUtils.isEmpty(userPasswordTextField)) {//判断密码输入框是否为空
                    JOptionPane.showMessageDialog(null, "密码不能为空");
                    return;
                }

                String userName = userNameTextField.getText();//获取用户名
                String userPassword = new String(userPasswordTextField.getPassword());//获取密码
                int authority = userRoleComboBox.getSelectedIndex();//获取用户角色

                User user = new User(); //实例化一个user对象，将用户名、密码和角色初始化
                user.setUserName(userName);  user.setUserPassword(userPassword);
                user.setAdmin(authority != 0); //判断职位 0:普通用户 1:管理员

                //user对象传递给Dao.loginCheck方法，进行数据库查询，查询结果赋值给newUser对象
                User newUser = Dao.loginCheck(user);
                Global.user = newUser;//Global.user中保存当前登录的用户信息
                //Global:全局变量，用于提供应用程序对象的规则，即用于提供数据或者服务使用的规则

                if(newUser==null) { //如果查询结果是null则说明查询失败，没有找到相关用户
                    JOptionPane.showMessageDialog(null, "用户名或密码错误");
                } else {
                    if(authority==0&& newUser.isAdmin()) {
                        //如果newUser是管理员，但是下拉列表是“读者”，则用户类型不匹配
                        JOptionPane.showMessageDialog(null, "该账户不是读者账户");
                    } else if(authority==1&& !newUser.isAdmin()) {
                        //如果newUser不是管理员，但是下拉列表是“管理员”，则用户类型不匹配
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "该账户不是管理员账户","消息对话框", JOptionPane.WARNING_MESSAGE);
                    } else {
                        dispose();//关闭一个GUI页面
                        if(!newUser.isAdmin())//如果newUser不是管理员则打开“用户主界面”
                            new UserFrame();
                        else
                            new AdminFrame();//如果newUser是管理员则打开“管理员主界面”
                    }
                }
            }
        });

        resetBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                userNameTextField.setText(""); userPasswordTextField.setText("");
                userRoleComboBox.setSelectedIndex(0);//角色由数组值决定，0：返回第一个值
                userNameTextField.grabFocus();//焦点方法//userNameTextField.requestFocus();
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
