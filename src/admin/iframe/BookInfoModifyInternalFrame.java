package admin.iframe;

import dao.Dao;
import pojo.BookInfo;
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

public class BookInfoModifyInternalFrame extends JInternalFrame {

    private static final String[] columnNames = { "图书编号", "图书名称", "图书作者", "图书价格", "图书类别", "图书描述", };
    private final int frameWidth = 800;
    private final int frameHeight = 400;
    private JTable bookInfoTable;
    private DefaultTableModel defaultTableModel;
    private JTextField bookIdTextField,bookNameTextField,bookAuthorTextField,bookPriceTextField;
    private JComboBox bookTypeComboBox;
    private JTextField bookDescTextField;
    private JButton modifyBtn,deleteBtn;
    private List<BookInfo> bookInfoList;

    public BookInfoModifyInternalFrame() {
        initSizeLocation();
        initView();
        initEvent();
        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"图书信息维护");
        setIconifiable(true); setClosable(true); setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel1.add(scrollPane);
        bookInfoTable = new JTable();
        scrollPane.setViewportView(bookInfoTable);
        defaultTableModel = (DefaultTableModel) bookInfoTable.getModel();
        defaultTableModel.setColumnIdentifiers(columnNames);
        initMyBooksTable();

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 1));
        JPanel panel2a = new JPanel(); JPanel panel2b = new JPanel();
        JLabel bookIdLabel = new JLabel("编号：");
        bookIdTextField = new JTextField(5);
        bookIdTextField.setEditable(false);
        JLabel bookNameLabel = new JLabel("名称：");
        bookNameTextField = new JTextField(12);
        JLabel bookAuthorLabel = new JLabel("作者：");
        bookAuthorTextField = new JTextField(12);
        JLabel bookPriceLabel = new JLabel("价格：");
        bookPriceTextField = new JTextField(5);
        JLabel bookTypeLabel = new JLabel("类别：");
        List<BookType> list = Dao.selectAllBookType();
        String[] bookTypeNames = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            bookTypeNames[i] = list.get(i).getBookTypeName();
        }
        bookTypeComboBox = new JComboBox(bookTypeNames);
        JLabel bookDescLabel = new JLabel("描述：");
        bookDescTextField = new JTextField(20);
        modifyBtn = new JButton("修改");
        deleteBtn = new JButton("删除");
        panel2a.add(bookIdLabel); panel2a.add(bookIdTextField);
        panel2a.add(bookNameLabel); panel2a.add(bookNameTextField);
        panel2a.add(bookAuthorLabel); panel2a.add(bookAuthorTextField);
        panel2a.add(bookPriceLabel); panel2a.add(bookPriceTextField);
        panel2b.add(bookTypeLabel); panel2b.add(bookTypeComboBox);
        panel2b.add(bookDescLabel); panel2b.add(bookDescTextField);
        panel2b.add(modifyBtn); panel2b.add(deleteBtn);
        panel2.add(panel2a); panel2.add(panel2b);

        add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        //图书信息维护子窗口中表格的点击事件
        //点击表格中的某一行，则将该条图书记录的信息显示在子窗口底部的各个输入框中，便于修改和删除
        bookInfoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = bookInfoTable.getSelectedRow();
                bookIdTextField.setText(String.valueOf(bookInfoTable.getValueAt(selectRow, 0)));
                bookNameTextField.setText(String.valueOf(bookInfoTable.getValueAt(selectRow, 1)));
                bookAuthorTextField.setText((String) bookInfoTable.getValueAt(selectRow, 2));
                bookPriceTextField.setText(String.valueOf(bookInfoTable.getValueAt(selectRow, 3)));
                bookTypeComboBox.setSelectedItem(bookInfoTable.getValueAt(selectRow, 4));
                bookDescTextField.setText((String) bookInfoTable.getValueAt(selectRow, 5));
            }
        });

        //“修改”按钮事件
        modifyBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookInfoTable.getSelectedRow();
                if(selectRow < 0) {
                    JOptionPane.showMessageDialog(null, "请先选择一本图书");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"确定修改该图书信息？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(choice==0) {
                    int bookId = Integer.parseInt(bookIdTextField.getText());
                    String bookName = bookNameTextField.getText();
                    String bookAuthor = bookAuthorTextField.getText();
                    double bookPrice = Double.parseDouble(bookPriceTextField.getText());
                    int bookTypeId = Dao.selectBookTypeIdByBookTypeName(bookTypeComboBox.getSelectedItem().toString());
                    String bookDesc = bookDescTextField.getText();
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setBookId(bookId); bookInfo.setBookName(bookName);
                    bookInfo.setBookAuthor(bookAuthor); bookInfo.setBookPrice(bookPrice);
                    bookInfo.setBookTypeId(bookTypeId); bookInfo.setBookDesc(bookDesc);

                    boolean flag = Dao.updateBookInfo(bookInfo);
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

        // TODO: 0607
        /*“删除”按钮事件
        参考上面的方法，“修改”按钮事件modifyBtn.addActionListener
        如果没有选择一本书，点击“删除”按钮，则会弹出"请先选择一个图书信息"对话框
        首先选择一本书，然后点击“删除”按钮，弹出"确定删除该图书信息？"对话框，选择“是”则进行删除操作，选择“否”则不删除
        根据选中的图书的id删除图书，删除成功，弹出“图书信息删除成功”对话框，并更新表格中的图书信息，将子窗口底部的输入框内容全部清空
        失败则弹出“图书信息删除失败”对话框
        */
        deleteBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookInfoTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "请先选择一本图书信息");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"确定删除该图书信息？",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(choice==0) {
                    int bookId = Integer.parseInt(bookIdTextField.getText());
                    String bookName = bookNameTextField.getText();
                    String bookAuthor = bookAuthorTextField.getText();
                    double bookPrice = Double.parseDouble(bookPriceTextField.getText());
                    int bookTypeId = Dao.selectBookTypeIdByBookTypeName(bookTypeComboBox.getSelectedItem().toString());
                    String bookDesc = bookDescTextField.getText();
                    BookInfo bookInfo = new BookInfo();
                    bookInfo.setBookId(bookId); bookInfo.setBookName(bookName);
                    bookInfo.setBookAuthor(bookAuthor); bookInfo.setBookPrice(bookPrice);
                    bookInfo.setBookTypeId(bookTypeId); bookInfo.setBookDesc(bookDesc);

                    boolean flag = Dao.updateBookInfo(bookInfo);
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "图书删除成功");
                        // initMyBooksTable();
                        // clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "图书删除失败");
                    }
                }
            }
        });
    }

    //图书信息维护子窗口中表格数据的初始化
    //从book_info表中查询所有的图书信息，显示在表格中
    //每次更新或者删除图书信息之后都会重新加载数据
    private void initMyBooksTable() {
        defaultTableModel.setRowCount(0);
        bookInfoList = Dao.findAllBooks();
        for (BookInfo bookInfo : bookInfoList) {
            Vector<Object> vector = new Vector<>();
            vector.add(bookInfo.getBookId());
            vector.add(bookInfo.getBookName());
            vector.add(bookInfo.getBookAuthor());
            vector.add(bookInfo.getBookPrice());
            vector.add(Dao.selectBookTypeName(bookInfo.getBookTypeId()));
            vector.add(bookInfo.getBookDesc());
            defaultTableModel.addRow(vector);
        }

    }

    //当完成“修改”或者“删除”操作之后，表格中没有选中的图书记录
    //此时需要将子窗口底部的各个输入框的内容清空
    private void clearAll() {
        bookIdTextField.setText("");
        bookNameTextField.setText("");
        bookAuthorTextField.setText("");
        bookPriceTextField.setText("");
        bookDescTextField.setText("");
    }
}
