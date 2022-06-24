package user.iframe;

import dao.Dao;
import pojo.BookInfo;
import utils.Global;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class FindAndBorrowInternalFrame extends JInternalFrame {
    private static final String[] columnNames = {"编号", "书名", "作者", "价格", "类别", "描述", "状态"};
    private final int frameWidth = 800,frameHeight = 400;
    private JComboBox findOptionComboBox;
    private JTextField findContentTextField;
    private JButton findBtn,borrowBtn;
    private JTable findBooksTable;
    private DefaultTableModel defaultTableModel;
    private List<BookInfo> bookInfoList;

    public FindAndBorrowInternalFrame() {
        initSizeLocation();initView(); initEvent();setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"查询图书");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JLabel findOptionLabel = new JLabel("查询选项：");
        findOptionComboBox = new JComboBox(new String[]{"作者", "书名", "所有"});
        JLabel findContentLabel = new JLabel("查询内容：");
        findContentTextField = new JTextField(15);
        findBtn = new JButton("查询");
        borrowBtn = new JButton("借阅");
        panel1.add(findOptionLabel); panel1.add(findOptionComboBox);
        panel1.add(findContentLabel); panel1.add(findContentTextField);
        panel1.add(findBtn); panel1.add(borrowBtn);

        JPanel panel2 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(700, 300));
        panel2.add(scrollPane);
        findBooksTable = new JTable();
        scrollPane.setViewportView(findBooksTable);
        defaultTableModel = (DefaultTableModel) findBooksTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);

        add(panel1, BorderLayout.NORTH); add(panel2, BorderLayout.CENTER);
    }

    private void initEvent() {
        //“查询”按钮事件
        findBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = findOptionComboBox.getSelectedIndex();
                String findContent = findContentTextField.getText();
                if(choice==0) {//如果查询选项是“作者”，则查询内容不能为空
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"查询内容不能为空");
                        return;
                    }
                    findBooksByAuthor(findContent);
                } else if(choice==1) {//如果查询选项是“书名”，则查询内容不能为空
                    if("".equals(findContent)||findContent==null) {
                        JOptionPane.showMessageDialog(null ,"查询内容不能为空");
                        return;
                    }
                    findBooksByName(findContent);
                } else {//如果查询选项是“所有”，则查询内容可以为空，将数据库中所有图书信息都返回
                    findAllBooks();
                }
            }
        });

        //“借阅”按钮事件
        borrowBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = findBooksTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请选择一本书");
                } else {
                    String flag = (String) findBooksTable.getValueAt(selectRow, 6);
                    if("不可借".equals(flag)) {
                        JOptionPane.showMessageDialog(null, "本书已借出");
                    } else {
                        int bookId = (int) findBooksTable.getValueAt(selectRow, 0);
                        boolean flag1 = Dao.borrowBookByBookId1(Global.user.getUserId(), bookId);
                        boolean flag2 = Dao.borrowBookByBookId2(bookId);
                        if(flag1&&flag2) {
                            JOptionPane.showMessageDialog(null, "借书成功");
                            int choice = findOptionComboBox.getSelectedIndex();
                            String findContent = findContentTextField.getText();
                            switch (choice) {
                                case 0: findBooksByAuthor(findContent); break;
                                case 1: findBooksByName(findContent);   break;
                                case 2: findAllBooks();                 break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "借书失败");
                        }
                    }
                }
            }
        });

    }

    //根据“作者”查询图书
    private void findBooksByAuthor(String bookAuthor) {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findBooksByAuthor(bookAuthor);

        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "没有找到相关书籍");
            return;
        }

        Object[] data = new Object[7];
        for (BookInfo bookInfo : bookInfoList) {
            data[0] = bookInfo.getBookId();
            data[1] = bookInfo.getBookName();
            data[2] = bookInfo.getBookAuthor();
            data[3] = bookInfo.getBookPrice();
            data[4] = Dao.selectBookTypeName(bookInfo.getBookTypeId());
            data[5] = bookInfo.getBookDesc();
            data[6] = bookInfo.isBorrowed() ? "不可借" : "可借";
            defaultTableModel.addRow(data);
        }
    }

    // TODO: 0302
    /*根据“书名”查询图书，并将数据显示在表格中
    参考findBooksByAuthor方法
    */
    private void findBooksByName(String bookName) {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findBooksByName(bookName);

        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "没有找到相关书籍");
            return;
        }

        for (BookInfo info : bookInfoList) {
            Vector<Object> data = new Vector();
            data.add(info.getBookId());
            data.add(info.getBookName());
            data.add(info.getBookAuthor());
            data.add(info.getBookPrice());
            data.add(Dao.selectBookTypeName(info.getBookTypeId()));
            data.add(info.getBookDesc());
            data.add(info.isBorrowed() ? "不可借" : "可借");
            defaultTableModel.addRow(data);
        }
    }

    // TODO: 0303
    /*查询所有图书信息，并显示在表格中
    参考findBooksByAuthor方法
    */
    private void findAllBooks() {
        defaultTableModel.setRowCount(0); bookInfoList = Dao.findAllBooks();
        if(bookInfoList.size()==0) {
            JOptionPane.showMessageDialog(null, "没有找到相关书籍");
            return;
        }
        for (BookInfo info : bookInfoList) {
            Vector<Object> data = new Vector();
            data.add(info.getBookId());
            data.add(info.getBookName());
            data.add(info.getBookAuthor());
            data.add(info.getBookPrice());
            data.add(Dao.selectBookTypeName(info.getBookTypeId()));
            data.add(info.getBookDesc());
            data.add(info.isBorrowed() ? "不可借" : "可借");
            defaultTableModel.addRow(data);
        }
    }
}
