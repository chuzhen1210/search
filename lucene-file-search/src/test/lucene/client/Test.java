package test.lucene.client;

import test.lucene.consts.SearchConsts;
import test.lucene.core.Searcher;

import java.util.List;

/**
 * ������
 * @author chuzhen
 *
 * 2017��6��4��
 */
public class Test {

	public static void main(String[] args) {
//		MainUI mainUi = new MainUI();
		
		Searcher manager = Searcher.getInstance();
//		manager.createIndex(SearchConsts.DATA_DIR);
		manager.search("�����ֶ�");

		String s = new String();

	}

	public static void test(List<String> list) {
	}
}
