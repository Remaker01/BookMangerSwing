package admin.iframe;

import dao.Dao;
import pojo.BookInfo;
import pojo.Borrow;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BorrowReturnInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "借阅编号", "借阅用户", "书名", "作者", "借阅日期" };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable myBorrowsTable;
    private DefaultTableModel defaultTableModel;
    private JTextField userNameTextField;
    private JTextField bookNameTextField;
    private JTextField borrowTimeTextField;
    private JButton returnBtn;
    private List<Borrow> borrowList;
    private List<BookInfo> bookInfoList;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BorrowReturnInternalFrame() {
        initSizeLocation();

        initView();

        initEvent();

        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"还书管理");
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(700, 250));
        panel1.add(scrollPane);
        myBorrowsTable = new JTable();
        scrollPane.setViewportView(myBorrowsTable);
        defaultTableModel = (DefaultTableModel) myBorrowsTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBooksTable();

        JPanel panel2 = new JPanel();
        JLabel userNameLabel = new JLabel("用户：");
        userNameTextField = new JTextField(10);
        userNameTextField.setEditable(false);
        JLabel bookNameLabel = new JLabel("书名：");
        bookNameTextField = new JTextField(10);
        bookNameTextField.setEditable(false);
        JLabel borrowTimeLabel = new JLabel("借阅日期：");
        borrowTimeTextField = new JTextField(12);
        borrowTimeTextField.setEditable(false);
        returnBtn = new JButton("还书");
        panel2.add(userNameLabel);
        panel2.add(userNameTextField);
        panel2.add(bookNameLabel);
        panel2.add(bookNameTextField);
        panel2.add(borrowTimeLabel);
        panel2.add(borrowTimeTextField);
        panel2.add(returnBtn);

        add(panel1, BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        myBorrowsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = myBorrowsTable.getSelectedRow();
                userNameTextField.setText((String) myBorrowsTable.getValueAt(selectRow, 1));
                bookNameTextField.setText((String) myBorrowsTable.getValueAt(selectRow, 2));
                borrowTimeTextField.setText((String) myBorrowsTable.getValueAt(selectRow, 4));
            }
        });

        returnBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectRow;
                selectRow = myBorrowsTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请选择一本书");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"确定要还这本书？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    Borrow borrow = borrowList.get(selectRow);
                    boolean flag = Dao.returnBook(borrow.getBorrowId());
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "还书成功");
                        initMyBooksTable();
                    }
                }
            }
        });

    }

    private void initMyBooksTable() {
        defaultTableModel.setRowCount(0);
        borrowList = Dao.selectAllBorrowing();
        bookInfoList = new ArrayList<>();

        for(int i=0; i<borrowList.size(); i++) {
            BookInfo bookInfo = Dao.selectBookInfoByBookId(borrowList.get(i).getBookId());
            bookInfoList.add(bookInfo);
            Vector<Object> vector = new Vector<>();
            vector.add(borrowList.get(i).getBorrowId());
            vector.add(Dao.selectUserNameById(borrowList.get(i).getUserId()));
            vector.add(bookInfo.getBookName());
            vector.add(bookInfo.getBookAuthor());
            vector.add(sdf.format(borrowList.get(i).getBorrowTime()));
            defaultTableModel.addRow(vector);
        }
    }
}
