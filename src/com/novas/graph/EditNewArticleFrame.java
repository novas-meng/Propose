package com.novas.graph;

import com.novas.data.DataModel;
import com.novas.data.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by novas on 2016/10/14.
 */
public class EditNewArticleFrame extends JFrame
{
    JTextArea abletextArea=new JTextArea();
    JTextArea labelarea=new JTextArea();
    public EditNewArticleFrame()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLayout(null);
        this.addTextAreaabled(abletextArea);
        this.setVisible(true);
    }

    public void addTextAreaabled(JTextArea textArea)
    {
        textArea.setSize(1000, 700);
        JLabel label=new JLabel("编辑文章");
        label.setBounds(50, 0, 400, 30);
        JLabel label1=new JLabel("标题");
        label1.setBounds(50,30,400,30);

        labelarea.setSize(900, 50);
        labelarea.setBounds(50, 60, 900, 50);

        JLabel label2=new JLabel("正文");
        label2.setBounds(50,110,210,50);

        textArea.setBounds(50, 160, 900, 450);
        JButton button=new JButton("保存");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
            }
        });
        button.setBounds(50,620,80,40);
        textArea.setLineWrap(true);
        this.add(label);
        this.add(label1);
        this.add(labelarea);
        this.add(label2);
        this.add(textArea);
        this.add(button);
    }
}
