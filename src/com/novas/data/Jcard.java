package com.novas.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by novas on 2016/10/2.
 */
public class Jcard {
    //返回与查询关键词相似度最大的前几个文章，articlemap 为文章标题->文章关键词及频率 的hashmap,length为想要返回的文章个数
    public static ArrayList<String> getMaxSimilarityArticle(HashMap<String,HashMap<String,Double>> articlemap,ArrayList<String> queryword,int length)
    {
        ArrayList<String> reslist=new ArrayList<>();
        for (int i=0;i<length;i++)
        {
            reslist.add(null);
        }
        double[] resarray=new double[length];
        for(Map.Entry<String,HashMap<String,Double>> mapEntry:articlemap.entrySet())
        {
            String key=mapEntry.getKey();
            HashMap<String,Double> wordmap=mapEntry.getValue();
            double J=getJcardSimilariry(wordmap,queryword);
            System.out.println("j="+J+"  "+key);
            refresh(J, key, resarray, reslist);
        }
        return reslist;
    }
    public static void refresh(double J,String articlename,double[] resarray,ArrayList<String> reslist)
    {
        for(int i=0;i<resarray.length;i++)
        {
            if(J>resarray[i])
            {
                System.out.println("J"+J+"  "+articlename);
                double temp=resarray[i];
                resarray[i]=J;
                String s=reslist.get(i);
                reslist.set(i,articlename);
                for(int j=i+1;j<resarray.length;j++)
                {
                    double t=resarray[j];
                    resarray[j]=temp;
                    String r=reslist.get(j);
                    reslist.set(j,s);
                    temp=t;
                    s=r;
                }
                break;
            }
        }
    }

    //计算Jcard相似度
    public static double getJcardSimilariry(HashMap<String,Double> map,HashMap<String,Double>  word)
    {
        int union=0;
        double sum=0;
        for(Map.Entry<String,Double> entry:word.entrySet())
        {
            if(map.containsKey(entry.getKey()))
            {
                union++;
                sum=sum+map.get(entry.getKey())*entry.getValue();
            }
        }
        double j=sum*union/(word.size()+map.size()-union);
        return sum;
    }
    //计算Jcard相似度
    public static double getJcardSimilariry(HashMap<String,Double> map,ArrayList<String> word)
    {
        int union=0;
        double sum=0;
        for(int i=0;i<word.size();i++)
        {
            if(map.containsKey(word.get(i)))
            {
                union++;
                sum=sum+map.get(word.get(i));
            }
        }
        double j=sum*union/(word.size()+map.size()-union);
        return sum;
    }
}
