package user.iframe;

import dao.Dao;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*修改密码子窗口
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
        setTitle(Global.user.getUserName()+"修改密码");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout()); JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel(); GridLayout gridLayout = new GridLayout(4,2);
        panel2.setLayout(gridLayout); JLabel userNameLabel = new JLabel("用户名：");
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameTextField = new JTextField(); userNameTextField.setEditable(false);
        userNameTextField.setText(Global.user.getUserName());
        JLabel oldUserPasswordLabel = new JLabel("旧密码：");
        oldUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        oldUserPasswordTextField = new JPasswordField();
        JLabel newUserPasswordLabel = new JLabel("新密码：");
        newUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newUserPasswordTextField = new JPasswordField();
        JLabel confirmUserPasswordLabel = new JLabel("确认密码：");
        confirmUserPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmUserPasswordTextField = new JPasswordField();
        panel2.add(userNameLabel); panel2.add(userNameTextField);
        panel2.add(oldUserPasswordLabel); panel2.add(oldUserPasswordTextField);
        panel2.add(newUserPasswordLabel); panel2.add(newUserPasswordTextField);
        panel2.add(confirmUserPasswordLabel); panel2.add(confirmUserPasswordTextField);

        JPanel panel3 = new JPanel(); confirmBtn = new JButton("修改");
        panel3.add(confirmBtn);

        panel1.add(panel2, BorderLayout.CENTER); panel1.add(panel3, BorderLayout.SOUTH);
        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        confirmBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // TODO:  0301
                /*“修改密码”子窗口中的“修改”按钮点击之后
                判断“旧密码”、“新密码”和“确认密码”三个输入框的内容不能为空
                写3个if语句进行判断，如果为空则弹出提示信息，并返回
                参考登录界面的“登录”按钮点击事件
                */
                if(EmptyUtils.isEmpty(oldUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "旧密码不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(newUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "新密码不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(confirmUserPasswordTextField)) {
                    JOptionPane.showMessageDialog(null, "确认密码不能为空");
                    return;
                }

                String oldUserPassword = new String(oldUserPasswordTextField.getPassword());

                if(!oldUserPassword.equals(Global.user.getUserPassword())) {
                    JOptionPane.showMessageDialog(null, "旧密码不对");
                } else {
                    String newUserPassword = new String(newUserPasswordTextField.getPassword());
                    String confirmUserPassword = new String(confirmUserPasswordTextField.getPassword());
                    if(newUserPassword.equals(confirmUserPassword)) {
                        boolean flag = Dao.modifyUserPassword(Global.user.getUserId(), newUserPassword);
                        if(flag) {
                            JOptionPane.showMessageDialog(null, "密码修改成功");
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "密码修改失败");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "两次密码不同");
                    }
                }
            }
        });
    }
}
