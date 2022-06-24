package admin.iframe;

import dao.Dao;
import pojo.BookInfo;
import pojo.BookType;
import utils.EmptyUtils;
import utils.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BookInfoAddInternalFrame extends JInternalFrame {
    private final int frameWidth = 300;
    private final int frameHeight = 250;
    private JTextField bookNameTextField;
    private JTextField bookAuthorTextField;
    private JTextField bookPriceTextField;
    private JComboBox bookTypeComboBox;
    private JTextField bookDescTextField;
    private JButton addBtn;

    public BookInfoAddInternalFrame() {
        initSizeLocation();
        initView();
        initEvent();
        setVisible(true);
    }

    private void initSizeLocation() {
        setTitle(Global.user.getUserName()+"ͼ����Ϣ���");
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
        setBounds(100, 50, frameWidth, frameHeight);
    }

    private void initView() {
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        JPanel panel2 = new JPanel();
        GridLayout gridLayout = new GridLayout(5,2);
        panel2.setLayout(gridLayout);
        JLabel bookNameLabel = new JLabel("ͼ�����ƣ�");
        bookNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookNameTextField = new JTextField();
        JLabel bookAuthorLabel = new JLabel("ͼ�����ߣ�");
        bookAuthorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookAuthorTextField = new JTextField();
        JLabel bookPriceLabel = new JLabel("ͼ��۸�");
        bookPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookPriceTextField = new JTextField();
        JLabel bookTypeLabel = new JLabel("ͼ�����");
        bookTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        List<BookType> list = Dao.selectAllBookType();
        String[] bookTypeNames = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            bookTypeNames[i] = list.get(i).getBookTypeName();
        }
        bookTypeComboBox = new JComboBox(bookTypeNames);
        JLabel bookDescLabel = new JLabel("ͼ��������");
        bookDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookDescTextField = new JTextField();
        panel2.add(bookNameLabel);
        panel2.add(bookNameTextField);
        panel2.add(bookAuthorLabel);
        panel2.add(bookAuthorTextField);
        panel2.add(bookPriceLabel);
        panel2.add(bookPriceTextField);
        panel2.add(bookTypeLabel);
        panel2.add(bookTypeComboBox);
        panel2.add(bookDescLabel);
        panel2.add(bookDescTextField);

        JPanel panel3 = new JPanel();
        addBtn = new JButton("���");
        panel3.add(addBtn);

        panel1.add(panel2, BorderLayout.CENTER);
        panel1.add(panel3, BorderLayout.SOUTH);

        add(panel1, BorderLayout.CENTER);
    }

    private void initEvent() {
        addBtn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(EmptyUtils.isEmpty(bookNameTextField)) {
                    JOptionPane.showMessageDialog(null, "ͼ�����Ʋ���Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(bookAuthorTextField)) {
                    JOptionPane.showMessageDialog(null, "ͼ�����߲���Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(bookPriceTextField)) {
                    JOptionPane.showMessageDialog(null, "ͼ��۸���Ϊ��");
                    return;
                }
                if(EmptyUtils.isEmpty(bookDescTextField)) {
                    JOptionPane.showMessageDialog(null, "ͼ����������Ϊ��");
                    return;
                }

                String bookName = bookNameTextField.getText();
                String bookAuthor = bookAuthorTextField.getText();
                double bookPrice;
                try {
                    bookPrice = Double.parseDouble(bookPriceTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "�۸��ʽ����");
                    return;
                }
                int bookTypeId = Dao.selectBookTypeIdByBookTypeName(
                        bookTypeComboBox.getSelectedItem().toString());
                String bookDesc = bookDescTextField.getText();

                BookInfo bookInfo = new BookInfo();
                bookInfo.setBookName(bookName);
                bookInfo.setBookAuthor(bookAuthor);
                bookInfo.setBookPrice(bookPrice);
                bookInfo.setBookTypeId(bookTypeId);
                bookInfo.setBookDesc(bookDesc);
                bookInfo.setBorrowed(false);

                boolean flag = Dao.addBookInfo(bookInfo);

                if(flag) {
                    JOptionPane.showMessageDialog(null, "ͼ����Ϣ��ӳɹ�");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "ͼ����Ϣ���ʧ��");
                }
            }
        });
    }
}
