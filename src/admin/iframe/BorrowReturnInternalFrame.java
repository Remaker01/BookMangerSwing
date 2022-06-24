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

    private static final String[] columnNames = { "���ı��", "�����û�", "����", "����", "��������" };
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
        setTitle(Global.user.getUserName()+"�������");
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
        JLabel userNameLabel = new JLabel("�û���");
        userNameTextField = new JTextField(10);
        userNameTextField.setEditable(false);
        JLabel bookNameLabel = new JLabel("������");
        bookNameTextField = new JTextField(10);
        bookNameTextField.setEditable(false);
        JLabel borrowTimeLabel = new JLabel("�������ڣ�");
        borrowTimeTextField = new JTextField(12);
        borrowTimeTextField.setEditable(false);
        returnBtn = new JButton("����");
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
                    JOptionPane.showMessageDialog(null, "��ѡ��һ����");
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"ȷ��Ҫ���Ȿ�飿",
                        null,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if(choice==0) {
                    Borrow borrow = borrowList.get(selectRow);
                    boolean flag = Dao.returnBook(borrow.getBorrowId());
                    if(flag) {
                        JOptionPane.showMessageDialog(null, "����ɹ�");
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
