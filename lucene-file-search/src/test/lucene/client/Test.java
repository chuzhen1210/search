package test.lucene.client;

import test.lucene.consts.SearchConsts;
import test.lucene.core.Searcher;

/**
 * 测试类
 * @author chuzhen
 *
 * 2017年6月4日
 */
public class Test {

	public static void main(String[] args) {
//		MainUI mainUi = new MainUI();
		
		Searcher manager = Searcher.getInstance();
//		manager.createIndex(SearchConsts.DATA_DIR);
		manager.search("关连字段");
		
	}
}
