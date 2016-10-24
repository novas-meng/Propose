package com.novas.data;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.nlp.cn.tag.CWSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by novas on 2016/10/7.
 */
public class DataModel {
    CWSTagger tag=null;
    ArrayList<String> stopWordslist=null;
    //存储文章标题和文章的关键词对应
    HashMap<String,HashMap<String,Double>> ArticleMap=new HashMap<>();
    private static DataModel dataModel;
    HashMap<Character,Double> similarityMap=new HashMap<>();
    ArrayList<String> myreadlist=null;
    HashMap<Character,ArrayList<String>> personMap=null;
    public static synchronized DataModel getDataModelInstance()
    {
        if(dataModel==null)
        {
            dataModel=new DataModel();
        }
        return dataModel;
    }
    public void initWantToRead()throws Exception
    {
        //我读的文章列表
        myreadlist=getMyReadList();
        HashMap<String,Double> myCharacterMap=getPersonCharacter(ArticleMap,myreadlist);
        // System.out.println("mycharacter"+myCharacterMap);
        //其他人对应的文章列表
        personMap=FileUtils.getPersonMap("person_article_json");
        // System.out.println("personMap="+personMap);
        //奇特人对应的相似度列表
        for (Map.Entry<Character,ArrayList<String>> entry:personMap.entrySet())
        {
            Character key=entry.getKey();
            ArrayList<String> value=entry.getValue();
            HashMap<String,Double> personCharacter=getPersonCharacter(ArticleMap,value);
            double similarity=Jcard.getJcardSimilariry(myCharacterMap,personCharacter);
            similarityMap.put(key,similarity);
        }
        similarityMap=   sortmap(similarityMap);
    }
    private DataModel()
    {
        try {
            ArticleMap=FileUtils.toHashMap("article_json");
            tag = new CWSTagger("./models/seg.m");
            stopWordslist= FileUtils.readStopWords("StopWords.txt");
            initWantToRead();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //设置英文预处理
        tag.setEnFilter(true);
        ArrayList<String> al = new ArrayList<String>();
        al.add("英大财险大连分公司");
        al.add("英大泰和财产保险股份有限公司大连分公司");
        al.add("英大泰和财产保险股份有限公司");
        Dictionary dict = new Dictionary(false);
        dict.addSegDict(al);
        tag.setDictionary(dict);
    }
    //对搜索语句进行分词
    public ArrayList<String> splitWords(String search)
    {
        ArrayList<String> strlist=tag.tag2List(search);
        ArrayList<String> queryList=new ArrayList<>();
        for(int i=0;i<strlist.size();i++)
        {
            String d=strlist.get(i).trim();
            if(!stopWordslist.contains(d)&&d.length()>1)
            {
                queryList.add(d);
            }
        }
        return queryList;
    }
    //获得搜索得到的语句
    public ArrayList<String>  getProposeList(String search)
    {
        ArrayList<String> queryList=splitWords(search);
        ArrayList<String> articleList= Jcard.getMaxSimilarityArticle(ArticleMap, queryList, 20);
        for(int i=0;i<articleList.size();i++)
        {
            System.out.println(articleList.get(i));
        }
        return articleList;
    }
    public ArrayList<String> getMyReadList()
    {
        ArrayList<String> list=new ArrayList<>();
        BufferedReader br=null;
        try {
            br=new BufferedReader(new FileReader("my_read_article"));
            String line=br.readLine();

            while (line!=null)
            {
                int loc=line.indexOf(',');
                line=line.substring(loc+1);
                list.add(line);
                line=br.readLine();
            }
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    //获取每个人的特征
    public  HashMap<String,Double> getPersonCharacter(HashMap<String,HashMap<String,Double>> ArticleMap,ArrayList<String> readarticlelist)
    {
        HashMap<String,Double> personCharacterMap=new HashMap<>();
        for (int i=0;i<readarticlelist.size();i++)
        {
             HashMap<String,Double> temp=ArticleMap.get(readarticlelist.get(i));
            if(temp!=null)
            {
                for (Map.Entry<String,Double> entry:temp.entrySet())
                {
                    String key=entry.getKey();
                    Double value=entry.getValue();
                    if(personCharacterMap.containsKey(key))
                    {
                        personCharacterMap.put(key,personCharacterMap.get(key)+value);
                    }
                    else
                    {
                        personCharacterMap.put(key,value);
                    }
                }
            }
        }
        return personCharacterMap;
    }
    public  HashMap<Character,Double> sortmap(HashMap<Character,Double> map)
    {
        ArrayList<Map.Entry<Character,Double>> list=new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Double>>() {
            @Override
            public int compare(Map.Entry<Character, Double> o1, Map.Entry<Character, Double> o2) {
                // return o2.getValue().compareTo(o1.getValue());
                if (o2.getValue() - o1.getValue() > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        map.clear();
        LinkedHashMap<Character,Double> hashMap=new LinkedHashMap();
        for(int i=0;i<list.size();i++)
        {
            hashMap.put(list.get(i).getKey(),list.get(i).getValue());
        }

        //System.out.println(hashMap);
        return hashMap;
    }
    //获取想要阅读的文章列表
    public  ArrayList<String> getWantToReadList()throws Exception
    {

        //获取相似度最高的两个人读取的所有文章
        ArrayList<String> twoPeopleList=new ArrayList<>();
        int count=0;
        for (Map.Entry<Character,Double> entry:similarityMap.entrySet())
        {
            count++;
            twoPeopleList.addAll(personMap.get(entry.getKey()));
            if(count==2)
            {
                break;
            }
        }
        //将这些文章中，去除我已经度过的
        for(int i=0;i<twoPeopleList.size();i++)
        {
            if(myreadlist.contains(twoPeopleList.get(i)))
            {
                twoPeopleList.remove(i);
            }
        }
        ArrayList<String> reslist=new ArrayList<>();
        Random random=new Random();
        for(int i=0;i<10;i++)
        {
            reslist.add(twoPeopleList.get(random.nextInt(twoPeopleList.size())));
        }
        return reslist;
    }
}
