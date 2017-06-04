package test.lucene.core;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import test.lucene.consts.SearchConsts;
import test.lucene.utils.FileUtil;
import test.lucene.utils.IOUtil;

/**
 * 搜索器
 * @author chuzhen
 *
 * 2017年6月4日
 */
public class Searcher{
    private static Searcher searcher;
    private static final Analyzer analyzer = new IKAnalyzer();
    
    /**
     * 创建索引管理器
     * @return 返回索引管理器对象
     */
    public static Searcher getInstance(){
        if(searcher == null){
            searcher = new Searcher();
        }
        return searcher;
    }

    /**
     * 创建当前文件目录的索引
     * @param path 当前文件目录
     * @return 是否成功
     */
    public boolean createIndex(String path){
    	File indexFile = new File(SearchConsts.INDEX_DIR_ROOT);
    	if (!indexFile.exists()) {
    		indexFile.mkdirs();
    	}
//    	FileUtil.clearDir(SearchConsts.INDEX_DIR_ROOT);
    	
        long start = System.currentTimeMillis();
        List<File> fileList = FileUtil.getFileList(path);
        IndexWriter indexWriter = null;
        try{
        	IndexWriterConfig config = new IndexWriterConfig(analyzer);
        	Path dataPath = FileSystems.getDefault().getPath(SearchConsts.INDEX_DIR_ROOT);
        	Directory directory = FSDirectory.open(dataPath);
        	indexWriter = new IndexWriter(directory, config);
        	indexWriter.deleteAll();
        	
	        for (File file : fileList) {
            	String content = FileUtil.getContent(file);
                Document document = new Document();
                document.add(new TextField("filename", file.getName(), Store.YES));
                document.add(new TextField("content", content, Store.YES));
                document.add(new TextField("path", file.getPath(), Store.YES));
                document.add(new LongPoint("lastModified", file.lastModified()));
                indexWriter.updateDocument(new Term("path", file.getPath()), document.getFields());
//                indexWriter.deleteDocuments(new Term("path", file.getPath()));
//                indexWriter.addDocument(document);
                indexWriter.commit();
	        }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
			IOUtil.close(indexWriter);
        }
        
        long end = System.currentTimeMillis();
        System.out.println("创建索引-----耗时：" + (end - start) + "ms\n");
        return true;
    }

    /**
     * 查找索引，返回符合条件的文件
     * @param text 查找的字符串
     * @return 符合条件的文件List
     */
    public void search(String text){
        long start = System.currentTimeMillis();
        try{
        	Path indexPath = FileSystems.getDefault().getPath(SearchConsts.INDEX_DIR_ROOT);
            FSDirectory directory = FSDirectory.open(indexPath);
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
    
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(text);
            
            ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;
        
            if(hits.length > 0) {
            	System.out.println("搜索结果：");
            }
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println("-->");
//                System.out.println(hitDoc.get("filename"));
//                System.out.println(hitDoc.get("content"));
                System.out.println(hitDoc.get("path"));
            }
            IOUtil.close(ireader);
            IOUtil.close(directory);
        }catch(Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("查看索引-----耗时：" + (end - start) + "ms\n");
    }
    
}