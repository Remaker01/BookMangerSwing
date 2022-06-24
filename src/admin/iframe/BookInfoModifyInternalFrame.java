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

    private static final String[] columnNames = { "ͼ����", "ͼ������", "ͼ������", "ͼ��۸�", "ͼ�����", "ͼ������", };
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
        setTitle(Global.user.getUserName()+"ͼ����Ϣά��");
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
        JLabel bookIdLabel = new JLabel("��ţ�");
        bookIdTextField = new JTextField(5);
        bookIdTextField.setEditable(false);
        JLabel bookNameLabel = new JLabel("���ƣ�");
        bookNameTextField = new JTextField(12);
        JLabel bookAuthorLabel = new JLabel("���ߣ�");
        bookAuthorTextField = new JTextField(12);
        JLabel bookPriceLabel = new JLabel("�۸�");
        bookPriceTextField = new JTextField(5);
        JLabel bookTypeLabel = new JLabel("���");
        List<BookType> list = Dao.selectAllBookType();
        String[] bookTypeNames = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            bookTypeNames[i] = list.get(i).getBookTypeName();
        }
        bookTypeComboBox = new JComboBox(bookTypeNames);
        JLabel bookDescLabel = new JLabel("������");
        bookDescTextField = new JTextField(20);
        modifyBtn = new JButton("�޸�");
        deleteBtn = new JButton("ɾ��");
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
        //ͼ����Ϣά���Ӵ����б��ĵ���¼�
        //�������е�ĳһ�У��򽫸���ͼ���¼����Ϣ��ʾ���Ӵ��ڵײ��ĸ���������У������޸ĺ�ɾ��
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

        //���޸ġ���ť�¼�
        modifyBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookInfoTable.getSelectedRow();
                if(selectRow < 0) {
                    JOptionPane.showMessageDialog(null, "����ѡ��һ��ͼ��");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"ȷ���޸ĸ�ͼ����Ϣ��",
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
                        JOptionPane.showMessageDialog(null, "ͼ�������³ɹ�");
                        initMyBooksTable();
                        clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "ͼ��������ʧ��");
                    }
                }
            }
        });

        // TODO: 0607
        /*��ɾ������ť�¼�
        �ο�����ķ��������޸ġ���ť�¼�modifyBtn.addActionListener
        ���û��ѡ��һ���飬�����ɾ������ť����ᵯ��"����ѡ��һ��ͼ����Ϣ"�Ի���
        ����ѡ��һ���飬Ȼ������ɾ������ť������"ȷ��ɾ����ͼ����Ϣ��"�Ի���ѡ���ǡ������ɾ��������ѡ�񡰷���ɾ��
        ����ѡ�е�ͼ���idɾ��ͼ�飬ɾ���ɹ���������ͼ����Ϣɾ���ɹ����Ի��򣬲����±���е�ͼ����Ϣ�����Ӵ��ڵײ������������ȫ�����
        ʧ���򵯳���ͼ����Ϣɾ��ʧ�ܡ��Ի���
        */
        deleteBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selectRow;
                selectRow = bookInfoTable.getSelectedRow();
                if(selectRow==-1) {
                    JOptionPane.showMessageDialog(null, "����ѡ��һ��ͼ����Ϣ");
                    return;
                }
                int choice = JOptionPane.showConfirmDialog(null,"ȷ��ɾ����ͼ����Ϣ��",
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
                        JOptionPane.showMessageDialog(null, "ͼ��ɾ���ɹ�");
                        // initMyBooksTable();
                        // clearAll();
                    } else {
                        JOptionPane.showMessageDialog(null, "ͼ��ɾ��ʧ��");
                    }
                }
            }
        });
    }

    //ͼ����Ϣά���Ӵ����б�����ݵĳ�ʼ��
    //��book_info���в�ѯ���е�ͼ����Ϣ����ʾ�ڱ����
    //ÿ�θ��»���ɾ��ͼ����Ϣ֮�󶼻����¼�������
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

    //����ɡ��޸ġ����ߡ�ɾ��������֮�󣬱����û��ѡ�е�ͼ���¼
    //��ʱ��Ҫ���Ӵ��ڵײ��ĸ����������������
    private void clearAll() {
        bookIdTextField.setText("");
        bookNameTextField.setText("");
        bookAuthorTextField.setText("");
        bookPriceTextField.setText("");
        bookDescTextField.setText("");
    }
}
