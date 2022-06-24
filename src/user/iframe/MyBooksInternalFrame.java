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

    private static final String[] columnNames = { "���ı��", "����", "����", "��������" };
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
        setTitle(Global.user.getUserName()+"�����");
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
        JLabel bookNameLabel = new JLabel("������");
        bookNameTextField = new JTextField(12);
        bookNameTextField.setEditable(false);
        JLabel bookAuthorLabel = new JLabel("���ߣ�");
        bookAuthorTextField = new JTextField(12);
        bookAuthorTextField.setEditable(false);
        JLabel borrowTimeLabel = new JLabel("�������ڣ�");
        borrowTimeTextField = new JTextField(12);
        borrowTimeTextField.setEditable(false);
        returnBtn = new JButton("����");
        panel2.add(bookNameLabel); panel2.add(bookNameTextField);
        panel2.add(bookAuthorLabel); panel2.add(bookAuthorTextField);
        panel2.add(borrowTimeLabel); panel2.add(borrowTimeTextField);
        panel2.add(returnBtn);

        add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.SOUTH);
    }

    private void initEvent() {
        //������ҵ���ܡ��е�һ����¼����ȡ�������ļ�¼��borrowId��
        //ͬʱ���������ļ�¼�������Ϣ�����Ӵ��ڵײ������������
        //��Ҫ�黹ͼ�飬��Ҫѡ�б���е�һ����¼
        myBorrowsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectRow = myBorrowsTable.getSelectedRow();
                borrowTimeTextField.setText(sdf.format(borrowList.get(selectRow).getBorrowTime()));
                bookNameTextField.setText(bookInfoList.get(selectRow).getBookName());
                bookAuthorTextField.setText(bookInfoList.get(selectRow).getBookAuthor());
            }
        });

        //���鰴ť�Ĺ���ʵ�֣���Ҫѡ���Ϸ�����е�һ�����ļ�¼
        returnBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow = myBorrowsTable.getSelectedRow();
                Borrow borrow = borrowList.get(selectRow);
                boolean flag = Dao.returnBook(borrow.getBorrowId());
                if(flag) {
                    JOptionPane.showMessageDialog(null, "����ɹ�");
                    initMyBooksTable();//����ɹ���Ҫ�����Ϸ�����Ѿ��黹�Ľ��ļ�¼Ҫ�ӱ����ɾ��
                }
            }
        });

    }

    /*
     * ���ҵ�����Ӵ��ڣ����ȴ����ݿ��и����û�id�������û����ĵ�����ͼ��
     * ����ʾ�ڱ����
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
