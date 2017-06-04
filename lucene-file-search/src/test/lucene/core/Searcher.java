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
 * ������
 * @author chuzhen
 *
 * 2017��6��4��
 */
public class Searcher{
    private static Searcher searcher;
    private static final Analyzer analyzer = new IKAnalyzer();
    
    /**
     * ��������������
     * @return ������������������
     */
    public static Searcher getInstance(){
        if(searcher == null){
            searcher = new Searcher();
        }
        return searcher;
    }

    /**
     * ������ǰ�ļ�Ŀ¼������
     * @param path ��ǰ�ļ�Ŀ¼
     * @return �Ƿ�ɹ�
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
        System.out.println("��������-----��ʱ��" + (end - start) + "ms\n");
        return true;
    }

    /**
     * �������������ط����������ļ�
     * @param text ���ҵ��ַ���
     * @return �����������ļ�List
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
            	System.out.println("���������");
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
        System.out.println("�鿴����-----��ʱ��" + (end - start) + "ms\n");
    }
    
}