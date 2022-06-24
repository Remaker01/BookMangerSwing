package user.iframe;

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

public class MyBooksInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "借阅编号", "书名", "作者", "借阅日期" };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable myBorrowsTable;
    private DefaultTableModel defaultTableModel;
    private JTextField bookAuthorTextField,bookNameTextField,borrowTimeTextField;
    private JButton returnBtn;
    private List<Borrow> borrowList;
    private List<BookInfo> bookInfoList;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MyBooksInternalFrame() {
        initSizeLocation();  initView(); initEvent(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"的书架");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel(); JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel1.add(scrollPane); myBorrowsTable = new JTable();
        scrollPane.setViewportView(myBorrowsTable);
        defaultTableModel = (DefaultTableModel) myBorrowsTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBooksTable();

        JPanel panel2 = new JPanel();
        JLabel bookNameLabel = new JLabel("书名：");
        bookNameTextField = new JTextField(12);
        bookNameTextField.setEditable(false);
        JLabel bookAuthorLabel = new JLabel("作者：");
        bookAuthorTextField = new JTextField(12);
        bookAuthorTextField.setEditable(false);
        JLabel borrowTimeLabel = new JLabel("借阅日期：");
        borrowTimeTextField = new JTextField(12);
        borrowTimeTextField.setEditable(false);
        returnBtn = new JButton("还书");
        panel2.add(bookNameLabel); panel2.add(bookNameTextField);
        panel2.add(bookAuthorLabel); panel2.add(bookAuthorTextField);
        panel2.add(borrowTimeLabel); panel2.add(borrowTimeTextField);
        panel2.add(returnBtn);

        add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        //点击“我的书架”中的一条记录，获取该条借阅记录的borrowId，
        //同时将该条借阅记录的相关信息填入子窗口底部的三个输入框
        //想要归还图书，先要选中表格中的一条记录
        myBorrowsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = myBorrowsTable.getSelectedRow();
                borrowTimeTextField.setText(sdf.format(borrowList.get(selectRow).getBorrowTime()));
                bookNameTextField.setText(bookInfoList.get(selectRow).getBookName());
                bookAuthorTextField.setText(bookInfoList.get(selectRow).getBookAuthor());
            }
        });

        //还书按钮的功能实现，先要选择上方表格中的一条借阅记录
        returnBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow = myBorrowsTable.getSelectedRow();
                Borrow borrow = borrowList.get(selectRow);
                boolean flag = Dao.returnBook(borrow.getBorrowId());
                if(flag) {
                    JOptionPane.showMessageDialog(null, "还书成功");
                    initMyBooksTable();//还书成功后要更新上方表格，已经归还的借阅记录要从表格中删除
                }
            }
        });

    }

    /*
     * 打开我的书架子窗口，将先从数据库中根据用户id检索该用户借阅的所有图书
     * 并显示在表格中
     * */
    private void initMyBooksTable() {
        defaultTableModel.setRowCount(0);
        borrowList = Dao.selectBorrowingByUserId(Global.user.getUserId());
        bookInfoList = new ArrayList<>();

        Object[] data = new Object[4];
        for(int i=0; i<borrowList.size(); i++) {
            BookInfo bookInfo = Dao.selectBookInfoByBookId(borrowList.get(i).getBookId());
            bookInfoList.add(bookInfo);
            data[0] = borrowList.get(i).getBorrowId(); data[1] = bookInfo.getBookName();
            data[2] = bookInfo.getBookAuthor(); data[3] = sdf.format(borrowList.get(i).getBorrowTime());
            defaultTableModel.addRow(data);
        }
    }
}
