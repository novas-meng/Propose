package com.novas.graph;

import com.novas.data.DataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Created by novas on 2016/10/5.
 */
public class MainFrame extends JFrame
{
    helper h;
    DataModel dataModel;
    public MainFrame(helper h,DataModel dataModel)
    {
        this.dataModel=dataModel;
        this.h=h;
     //   this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VFlowLayout fl=new VFlowLayout();
        this.setLayout(fl);
        this.addLabel("DocHelper");
        try {
            this.addWantToReadTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.addEdit();
        this.pack();
        this.setVisible(true);

    }
    public  void addLabel(String labelname)
    {
        JLabel label=new JLabel(labelname);
        this.add(label);
        JPanel panel=new JPanel();
        panel.setSize(200, 20);
        this.add(panel);
    }
    public void addEdit()
    {
        final JTextArea jTextArea=new JTextArea();
        JPanel panel=new JPanel();
        JButton button1=new JButton("我的文档");
        JButton button2=new JButton("搜索");
        panel.add(button1);
        panel.add(button2);
        this.add(panel);
        this.add(jTextArea);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.myprofile();
            }
        });
       button2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               h.search(jTextArea.getText());
           }
       });
    }
    public void addWantToReadTable()throws Exception
    {
        JLabel label=new JLabel("你可能想要阅读");
        this.add(label);
        String columnname="想要阅读";
        ArrayList<String> list=dataModel.getWantToReadList();
        MyTableModel myModel = new MyTableModel(columnname,list);// myModel存放表格的数据
        JTable table = new JTable(myModel);// 表格对象table的数据来源是myModel对象
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));// 表格的显示尺寸

        // 产生一个带滚动条的面板
        JScrollPane scrollPane = new JScrollPane(table);


        // 将带滚动条的面板添加入窗口中
        this.add(scrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {// 注册窗口监听器
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
