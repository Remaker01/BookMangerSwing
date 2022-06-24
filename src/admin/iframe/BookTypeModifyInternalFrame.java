package admin.iframe;

import dao.Dao;
import pojo.BookType;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class BookTypeModifyInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "图书类别编号", "图书类别名称", "图书类别描述" };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable bookTypeTable;
    private DefaultTableModel defaultTableModel;
    private JTextField bookTypeIdTextField,bookTypeNameTextField,bookTypeDescTextField;
    private JButton modifyBtn,deleteBtn;
    private List<BookType> bookTypeList;

    public BookTypeModifyInternalFrame() {
        initSizeLocation(); initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"图书类别维护");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel1.add(scrollPane);
        bookTypeTable = new JTable();
        scrollPane.setViewportView(bookTypeTable);
        defaultTableModel = (DefaultTableModel) bookTypeTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBooksTable();

        JPanel panel2 = new JPanel();
        JLabel bookTypeIdLabel = new JLabel("编号：");
        bookTypeIdTextField = new JTextField(12);
        bookTypeIdTextField.setEditable(false);
        JLabel bookTypeNameLabel = new JLabel("名称：");
        bookTypeNameTextField = new JTextField(12);
        JLabel bookTypeDescLabel = new JLabel("描述：");
        bookTypeDescTextField = new JTextField(12);
        modifyBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        panel2.add(bookTypeIdLabel); panel2.add(bookTypeIdTextField);
        panel2.add(bookTypeNameLabel); panel2.add(bookTypeNameTextField);
        panel2.add(bookTypeDescLabel); panel2.add(bookTypeDescTextField);
        panel2.add(modifyBtn); panel2.add(deleteBtn);

        add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        bookTypeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = bookTypeTable.getSelectedRow();
                bookTypeIdTextField.setText(String.valueOf(bookTypeTable.getValueAt(selectRow, 0)));
                bookTypeNameTextField.setText((String) bookTypeTable.getValueAt(selectRow, 1));
                bookTypeDescTextField.setText((String) bookTypeTable.getValueAt(selectRow, 2));
            }
        });

        modifyBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookTypeTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请先选择一个图书类别");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"确定修改该图书类别？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    int bookTypeId = Integer.parseInt(bookTypeIdTextField.getText());
                    String bookTypeName = bookTypeNameTextField.getText();
                    String bookTypeDesc = bookTypeDescTextField.getText();
                    BookType bookType = new BookType();
                    bookType.setBookTypeId(bookTypeId);
                    bookType.setBookTypeName(bookTypeName);
                    bookType.setBookTypeDesc(bookTypeDesc);
                    boolean flag = Dao.updateBookType(bookType);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "图书类别更新成功");
                        initMyBooksTable();
                        clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "图书类别更新失败");
                    }
                }
            }
        });

        // TODO: 0507
        /*删除选中的图书类别
        参考上面的方法modifyBtn.addActionListener
        先选中表格中的某一条图书类别，点击“删除”按钮删除该图书类别，删除成功后刷新表格，并将子窗口底部的三个文本框内容清空
        */
        deleteBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookTypeTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请先选择一个图书类别");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"确定删除该图书类别？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(choice==0) {
                    int bookTypeId = Integer.parseInt(bookTypeIdTextField.getText());
                    boolean flag = Dao.deleteBookTypeById(bookTypeId);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "图书类别删除成功");
                        initMyBooksTable(); clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "图书类别删除失败");
                    }
                }
            }
        });
    }

    private void initMyBooksTable() {
        defaultTableModel.setRowCount(0);
        bookTypeList = Dao.selectAllBookType();

        for(int i=0; i<bookTypeList.size(); i++) {
            Vector<Object> vector = new Vector<>();
            vector.add(bookTypeList.get(i).getBookTypeId());
            vector.add(bookTypeList.get(i).getBookTypeName());
            vector.add(bookTypeList.get(i).getBookTypeDesc());
            defaultTableModel.addRow(vector);
        }

    }

    private void clearAll() {
        bookTypeIdTextField.setText("");
        bookTypeNameTextField.setText("");
        bookTypeDescTextField.setText("");
    }

}
