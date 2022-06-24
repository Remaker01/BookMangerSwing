package admin.iframe;

import dao.Dao;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BookTypeAddInternalFrame extends JInternalFrame {

    private final int frameWidth = 300,frameHeight = 200;
    private JTextField bookTypeNameTextField,bookTypeDescTextField;
    private JButton addBtn;

    public BookTypeAddInternalFrame() {
        initSizeLocation();
        initView();
        initEvent();
        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"图书类别添加");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel();
        GridLayout gridLayout = new GridLayout(2,2);
        panel2.setLayout(gridLayout);
        JLabel bookTypeNameLabel = new JLabel("图书类别名称：");
        bookTypeNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookTypeNameTextField = new JTextField();
        JLabel bookTypeDescLabel = new JLabel("图书类别描述：");
        bookTypeDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookTypeDescTextField = new JTextField();
        panel2.add(bookTypeNameLabel); panel2.add(bookTypeNameTextField);
        panel2.add(bookTypeDescLabel); panel2.add(bookTypeDescTextField);

        JPanel panel3 = new JPanel();
        addBtn = new JButton("添加");
        panel3.add(addBtn);

        panel1.add(panel2, BorderLayout.CENTER);
        panel1.add(panel3, BorderLayout.SOUTH);

        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        addBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(bookTypeNameTextField)) {
                    JOptionPane.showMessageDialog(null, "图书类别名称不能为空");
                    return;
                }
                if(EmptyUtils.isEmpty(bookTypeDescTextField)) {
                    JOptionPane.showMessageDialog(null, "图书类别描述不能为空");
                    return;
                }

                String bookTypeName = bookTypeNameTextField.getText();
                String bookTypeDesc = bookTypeDescTextField.getText();

                boolean flag = Dao.addBookType(bookTypeName, bookTypeDesc);

                if(flag) {
                    JOptionPane.showMessageDialog(null, "图书类别添加成功");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "图书类别添加失败");
                }
            }
        });
    }

}
