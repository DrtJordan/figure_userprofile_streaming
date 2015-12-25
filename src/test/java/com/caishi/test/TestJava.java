package com.caishi.test;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by root on 15-11-23.
 */
public class TestJava {
	public static void main(String[] args){
		List<Document> r = new ArrayList<Document>();

		Document tmp= new Document();
		tmp.append("catId",1).append("weight", 0.2);
		r.add(tmp);

		Document tmp2= new Document();
		tmp2.append("catId",3).append("weight",0.4);
		r.add(tmp2);

		Document tmp1= new Document();
		tmp1.append("catId",2).append("weight",0.3);
		r.add(tmp1);
		Collections.sort(r, new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				int flag = 0;
				double result = o1.getDouble("weight")-o2.getDouble("weight");
				if(result > 0 )
					flag = 1;
				else if(result < 0)
					flag = -1;
				return -flag;
			}
		});
		for(Document d : r){
			System.out.println(d.getInteger("catId")+":"+d.getDouble("weight"));
		}
	}
}
