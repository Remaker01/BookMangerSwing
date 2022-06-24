package admin.iframe;

import dao.Dao;
import pojo.User;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class UserModifyInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "用户编号", "用户名称", "用户密码","用户角色" };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable userTable;
    private DefaultTableModel defaultTableModel;
    private JTextField userIdTextField;
    private JTextField userNameTextField;
    private JTextField userPasswordTextField;
    private JComboBox userRoleComboBox;
    private JButton modifyBtn;
    private JButton deleteBtn;
    private List<User> userList;

    public UserModifyInternalFrame() {
        initSizeLocation();
        initView();
        initEvent();
        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"用户信息维护");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel1.add(scrollPane);
        userTable = new JTable();
        scrollPane.setViewportView(userTable);
        defaultTableModel = (DefaultTableModel) userTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initUserTable();

        JPanel panel2 = new JPanel();
        JLabel userIdLabel = new JLabel("编号：");
        userIdTextField = new JTextField(5);
        userIdTextField.setEditable(false);
        JLabel userNameLabel = new JLabel("名称：");
        userNameTextField = new JTextField(10);
        JLabel userPasswordLabel = new JLabel("密码：");
        userPasswordTextField = new JTextField(10);
        JLabel userRoleLabel = new JLabel("角色：");
        userRoleComboBox = new JComboBox(new String[]{"用户", "管理员"});
        modifyBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        panel2.add(userIdLabel); panel2.add(userIdTextField);
        panel2.add(userNameLabel); panel2.add(userNameTextField);
        panel2.add(userPasswordLabel); panel2.add(userPasswordTextField);
        panel2.add(userRoleLabel); panel2.add(userRoleComboBox);
        panel2.add(modifyBtn); panel2.add(deleteBtn);

        add(panel1, BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = userTable.getSelectedRow();
                userIdTextField.setText(String.valueOf(userTable.getValueAt(selectRow, 0)));
                userNameTextField.setText((String) userTable.getValueAt(selectRow, 1));
                userPasswordTextField.setText((String) userTable.getValueAt(selectRow, 2));
                userRoleComboBox.setSelectedItem(userTable.getValueAt(selectRow, 3));
            }
        });

        modifyBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = userTable.getSelectedRow();
                if(selectRow < 0) {
                    JOptionPane.showMessageDialog(null, "请先选择一个用户");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"确定修改该用户信息？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    int userId = Integer.parseInt(userIdTextField.getText());
                    String userName = userNameTextField.getText();
                    String userPassword = userPasswordTextField.getText();
                    boolean isAdmin = !("用户".equals(userRoleComboBox.getSelectedItem().toString()));
                    User user = new User();
                    user.setUserId(userId);
                    user.setUserName(userName);
                    user.setAdmin(isAdmin);

                    boolean flag = Dao.updateUser(user);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "用户信息更新成功");
                        initUserTable();
                        clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户信息更新失败");
                    }
                }
            }
        });

        deleteBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = userTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请先选择一个用户");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"确定删除该用户？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    int userId = Integer.parseInt(userIdTextField.getText());
                    boolean flag = Dao.deleteUserById(userId);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "用户删除成功");
                        initUserTable();
                        clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户删除失败");
                    }
                }

            }
        });

    }

    private void initUserTable() {
        defaultTableModel.setRowCount(0);
        userList = Dao.selectAllUser();

        for (User user : userList) {
            Vector<Object> vector = new Vector<>();
            vector.add(user.getUserId());
            vector.add(user.getUserName());
            vector.add(user.getUserPassword());
            vector.add(user.isAdmin() ? "管理员" : "用户");
            defaultTableModel.addRow(vector);
        }

    }

    private void clearAll() {
        userIdTextField.setText("");
        userNameTextField.setText("");
        userPasswordTextField.setText("");
    }

}
