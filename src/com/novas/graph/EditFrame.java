package com.novas.graph;

import com.novas.data.DataModel;
import com.novas.data.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by novas on 2016/10/6.
 */
public class EditFrame extends JFrame
{
    JTextArea disabletextArea=new JTextArea();
    JTextArea abletextArea=new JTextArea();
    JTextArea labelarea=new JTextArea();
    DataModel dataModel;
    public EditFrame(boolean isMyProfile,String search,DataModel dataModel)
    {
        this.dataModel=dataModel;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLayout(null);
        try {
            this.addProfile(isMyProfile,search);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.addTextAreaDisabled(disabletextArea);
        this.newArticleButton();
        this.setVisible(true);
    }
    public void showArticle(String articlename)
    {
        System.out.println("显示");
        String articlecontent=null;
        try
        {
            articlecontent= FileUtils.readFile("data/" + articlename);
            System.out.println("articlecont="+articlecontent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        disabletextArea.setText(articlecontent);
    }
    public void addTextAreaDisabled(JTextArea textArea)
    {
        textArea.setSize(600, 600);
        textArea.setAutoscrolls(true);
        textArea.setBackground(Color.gray);
        JLabel label=new JLabel("文章预览");
        label.setBounds(300,0,600,50);
        textArea.setBounds(300, 50, 600, 550);
        textArea.setLineWrap(true);
        this.add(label);
        this.add(textArea);
    }
    public void newArticleButton()
    {

        JButton button=new JButton("新建");
        button.setBounds(910,40,60,40);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditNewArticleFrame();
            }
        });
        this.add(button);
    }
    public void addProfile(boolean isprofile,String search)throws Exception
    {
        JPanel panel=new JPanel();
        VFlowLayout fl=new VFlowLayout();
        panel.setLayout(fl);
        /*
        想要阅读部分
         */
        final MyTableModel WantToReadModel = new MyTableModel("你可能想要阅读",dataModel.getWantToReadList());// myModel存放表格的数据
        JTable wanttoreadtable = new JTable(WantToReadModel);// 表格对象table的数据来源是myModel对象
        wanttoreadtable.setPreferredScrollableViewportSize(new Dimension(300, 150));// 表格的显示尺寸
        wanttoreadtable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置

                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String) (WantToReadModel.getValueAt(row, col));
                    System.out.println(cellVal);
                    showArticle(cellVal);
                }
            }
        });
        JScrollPane wanttoreadscrollPane = new JScrollPane(wanttoreadtable);
        panel.add(wanttoreadscrollPane, BorderLayout.CENTER);

   /*
        近一周创作的文档
         */
        final MyTableModel MyWriteModel = new MyTableModel("近一周创作的文档",dataModel.getWantToReadList());// myModel存放表格的数据
        JTable MyWriteTable = new JTable(MyWriteModel);// 表格对象table的数据来源是myModel对象
        MyWriteTable.setPreferredScrollableViewportSize(new Dimension(300, 150));// 表格的显示尺寸

        MyWriteTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置

                    int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                    String cellVal = (String) (MyWriteModel.getValueAt(row, col));
                    System.out.println(cellVal);
                    showArticle(cellVal);
                }
            }
        });
        // 产生一个带滚动条的面板
        JScrollPane mywritescrollPane = new JScrollPane(MyWriteTable);
        panel.add(mywritescrollPane, BorderLayout.CENTER);

 /*
        为你推荐
         */
        if(!isprofile)
        {
            final MyTableModel ProposeReadModel = new MyTableModel("搜索结果",dataModel.getProposeList(search));// myModel存放表格的数据

            JTable Proposetable = new JTable(ProposeReadModel);// 表格对象table的数据来源是myModel对象

            Proposetable.setPreferredScrollableViewportSize(new Dimension(300, 150));// 表格的显示尺寸
            Proposetable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint()); //获得行位置

                        int col = ((JTable) e.getSource()).columnAtPoint(e.getPoint()); //获得列位置
                        String cellVal = (String) (ProposeReadModel.getValueAt(row, col));
                        System.out.println(cellVal);
                        showArticle(cellVal);
                    }
                }
            });
            JScrollPane ProposescrollPane = new JScrollPane(Proposetable);
            panel.add(ProposescrollPane, BorderLayout.CENTER);
        }
        panel.setBounds(0,0,300,600);
        this.add(panel);
    }
}
