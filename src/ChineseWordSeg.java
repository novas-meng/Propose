import java.io.*;
import java.util.*;

import com.novas.data.FileUtils;
import com.novas.data.Jcard;
import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;
import net.sf.json.JSONObject;


/**
 * 分词使用示例
 * @author xpqiu
 *
 */
public class ChineseWordSeg {
    /**
     * 主程序
     * @throws
     */


    public static ArrayList<String> splitWords(String str, CWSTagger tagger,ArrayList<String> stopwords)
    {
        ArrayList<String> res=new ArrayList<>();
        ArrayList<String> strlist=tagger.tag2List(str);
        HashMap<String,Double> map=new HashMap<>();
        for(int i=0;i<strlist.size();i++)
        {
            String d=strlist.get(i).trim();
            if(!stopwords.contains(d)&&d.length()>1)
            {
               res.add(d);
            }
        }
        return res;
    }
    public static String readFile(String filename)throws Exception
    {
        byte[] bytes=new byte[1024];
        InputStream fis=new FileInputStream(filename);

        int length=fis.read(bytes);
        //	System.out.println(new String(bytes));
        byte[] res=new byte[0];
        while (length!=-1)
        {
            byte[] temp=new byte[res.length];
            System.arraycopy(res,0,temp,0,res.length);
            res=new byte[res.length+length];
            System.arraycopy(bytes,0,res,res.length-length,length);
            System.arraycopy(temp,0,res,0,temp.length);
            bytes=new byte[1024];
            length=fis.read(bytes);
        }
        return new String(res);
    }



    public static void main(String[] args) throws Exception {

        CWSTagger tag = new CWSTagger("./models/seg.m");
        //设置英文预处理
        tag.setEnFilter(true);
        ArrayList<String> al = new ArrayList<String>();
        al.add("英大财险大连分公司");
        al.add("英大泰和财产保险股份有限公司大连分公司");
        al.add("英大泰和财产保险股份有限公司");
        Dictionary dict = new Dictionary(false);
        dict.addSegDict(al);
        tag.setDictionary(dict);
        ArrayList<String> stopWordslist= FileUtils.readStopWords("StopWords.txt");


        //存储文章标题和文章的关键词对应
        HashMap<String,HashMap<String,Double>> ArticleMap=new HashMap<>();
        File dir=new File("data");
        File[] files=dir.listFiles();
        for(int j=0;j<files.length;j++)
        {
            String filename=files[j].getName();
            String strf=readFile("data/" + filename);
            ArrayList<String> strlist=tag.tag2List(strf);
            HashMap<String,Double> map=new HashMap<>();
            for(int i=0;i<strlist.size();i++)
            {
                String d=strlist.get(i).trim();
                if(!stopWordslist.contains(d)&&d.length()>1)
                {
                    if(map.containsKey(d))
                    {
                        map.put(d,map.get(d)+1);
                    }
                    else
                    {
                        map.put(d,1.0);
                    }
                }
            }
            ArticleMap.put(filename,map);
        }

        tfidf(ArticleMap);
        //将标题的权值进行添加
       for(Map.Entry<String,HashMap<String,Double>> entry:ArticleMap.entrySet())
             {
                 HashMap<String,Double> newmap=entry.getValue();
                 String articlename=entry.getKey();
                 ArrayList<String> strings=splitWords(articlename,tag,stopWordslist);
                 for(int i=0;i<strings.size();i++)
                 {
                     String var=strings.get(i);
                     if(newmap.containsKey(var))
                     {
                         newmap.put(var,newmap.get(var)+1.0/strings.size());
                     }
                     else
                     {
                         newmap.put(var,2.0/strings.size());
                     }
                 }
                 ArticleMap.put(articlename,newmap);
             }
        //进行关键词排序
        for(Map.Entry<String,HashMap<String,Double>> entry:ArticleMap.entrySet())
        {
           ArticleMap.put(entry.getKey(), sortmap(entry.getValue()));

        }
        for(Map.Entry<String,HashMap<String,Double>> entry:ArticleMap.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        String query="大连分公司关于2016年5月主要经营业绩指标完成情况的通报";
      //  String query="大连分公司关于申请集装箱业务费用增补的请示";
        ArrayList<String> strlist=tag.tag2List(query);
        ArrayList<String> queryList=new ArrayList<>();
        for(int i=0;i<strlist.size();i++)
        {
            String d=strlist.get(i).trim();
            if(!stopWordslist.contains(d)&&d.length()>1)
            {
                queryList.add(d);
            }
        }
        System.out.println("queryList");
        for (int i=0;i<queryList.size();i++)
        {
            System.out.println(queryList.get(i));
        }


      //  JSONArray array=new JSONArray(line);
      //  for(int i=0;i<array.length();i++)
       // {
            //JSONObject jsonObject=array.getJSONObject(i);

       // }




        // jsonObject.
      //  fileWriter.close();
        JSONObject object=new JSONObject();
        for (Map.Entry<String,HashMap<String,Double>> entry:ArticleMap.entrySet() )
        {
            object.put(entry.getKey(),entry.getValue());
        }
        FileWriter fw=new FileWriter("article_json");
        fw.write(object.toString());
        fw.close();

        ArrayList<String> articleList= Jcard.getMaxSimilarityArticle(ArticleMap, queryList, 20);
        for(int i=0;i<articleList.size();i++)
        {
            System.out.println(articleList.get(i));
        }


    }
    public static HashMap<String,Double> sortmap(HashMap<String,Double> map)
    {
        ArrayList<Map.Entry<String,Double>> list=new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
               // return o2.getValue().compareTo(o1.getValue());
                if(o2.getValue()-o1.getValue()>0)
                {
                    return  1;
                }
                else
                {
                    return -1;
                }
            }
        });
        map.clear();
        LinkedHashMap<String,Double> hashMap=new LinkedHashMap();
        for(int i=0;i<list.size();i++)
        {
            hashMap.put(list.get(i).getKey(),list.get(i).getValue());
        }

        //System.out.println(hashMap);
        return hashMap;
    }

    public static HashMap<String,HashMap<String,Double>> tfidf(HashMap<String,HashMap<String,Double>> articlemap)
    {
        double D=articlemap.size();
        HashMap<String,HashMap<String,Double>> res=new HashMap<>();
        for (Map.Entry<String,HashMap<String,Double>> entry:articlemap.entrySet() )
        {
            String articlename=entry.getKey();
            HashMap<String,Double> map=entry.getValue();
            double articlewordcount=0;
            for (Map.Entry<String,Double> entry1:map.entrySet())
            {
                articlewordcount=articlewordcount+entry1.getValue();
            }
            for (Map.Entry<String,Double> entry1:map.entrySet())
            {
                String key=entry1.getKey();
                double tf=entry1.getValue()/articlewordcount;
                //L表示单词在所有文件中出现的文件次数
                double L=0;
                for (Map.Entry<String,HashMap<String,Double>> entry2:articlemap.entrySet() )
                {
                    if(entry2.getValue().containsKey(key))
                    {
                        L++;
                    }
                }
                double idf=Math.log(D/(L+1));
                map.put(key,tf*idf);
            }
           // res.put(articlename,)
        }
        return null;
    }

}
