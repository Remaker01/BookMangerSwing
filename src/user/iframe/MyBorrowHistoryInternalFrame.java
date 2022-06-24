package user.iframe;

import dao.Dao;
import pojo.BookInfo;
import pojo.Borrow;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyBorrowHistoryInternalFrame extends JInternalFrame {
    private static final String[] columnNames = { "借阅编号", "书名", "作者", "借阅日期", "还书日期" };
    private final int frameWidth = 800,frameHeight = 400;
    private JTable myBorrowsTable;
    private DefaultTableModel defaultTableModel;
    private List<Borrow> borrowList;
    private List<BookInfo> bookInfoList;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MyBorrowHistoryInternalFrame() {
        initSizeLocation(); initView(); setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"的借阅历史");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel(); JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(650, 350));
        panel1.add(scrollPane);
        myBorrowsTable = new JTable();
        scrollPane.setViewportView(myBorrowsTable);
        defaultTableModel = (DefaultTableModel) myBorrowsTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBorrowsTable();
        add(panel1, BorderLayout.CENTER);
    }

    private void initMyBorrowsTable() {
        defaultTableModel.setRowCount(0);
        borrowList = Dao.selectAllBorrowedByUserId(Global.user.getUserId());
        bookInfoList = new ArrayList<>();

        for (Borrow borrow : borrowList) {
            BookInfo bookInfo = Dao.selectBookInfoByBookId(borrow.getBookId());
            Vector<Object> vector = new Vector<>();
            vector.add(borrow.getBorrowId());
            vector.add(bookInfo.getBookName());
            vector.add(bookInfo.getBookAuthor());
            vector.add(sdf.format(borrow.getBorrowTime()));
            if (borrow.getReturnTime() != null) {
                vector.add(sdf.format(borrow.getReturnTime()));
            } else {
                vector.add("未还");
            }
            defaultTableModel.addRow(vector);
        }
    }
}
