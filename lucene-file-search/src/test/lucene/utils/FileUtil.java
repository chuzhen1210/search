package test.lucene.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import test.lucene.consts.SearchConsts;

/**
 * �����ļ�
 * @author chuzhen
 *
 * 2017��6��4��
 */
public class FileUtil {
	
	public static String getIndexDir(String dataDir) {
		return getIndexDir(new File(dataDir));
	}
	
	public static String getIndexDir(File dataDir) {
		String result = null;
		if(dataDir.exists() && dataDir.isDirectory()) {
			String absolutePath = dataDir.getAbsolutePath();
			int index = absolutePath.indexOf(File.separator);
			result = SearchConsts.INDEX_DIR_ROOT + new String(absolutePath.substring(index));
		}
		
		return result;
	}
	
	/**
     * ����Ŀ¼�µ��ļ�
     * @param dirPath ��Ҫ��ȡ�ļ���Ŀ¼
     * @return �����ļ�list
     */
    public static List<File> getFileList(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (isTxtFile(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    
    public static void clearDir(String path) {
		File fileIndex = new File(path);
        if(deleteDir(fileIndex)){
            fileIndex.mkdir();
        }else{
            fileIndex.mkdir();
        }
	}
    
    /**
     * ɾ���ļ�Ŀ¼�µ������ļ�
     * @param file Ҫɾ�����ļ�Ŀ¼
     * @return ����ɹ�������true.
     */
    public static boolean deleteDir(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i=0; i<files.length; i++){
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }
    
    public static String getContent(File file) {
		String content = "";
		//��ȡ�ļ���׺
		String type = file.getName().substring(file.getName().lastIndexOf(".")+1);
		if("txt".equalsIgnoreCase(type)){
		    
		    content += FileUtil.txt2String(file);
		
		}else if("doc".equalsIgnoreCase(type)){
		
		    content += FileUtil.doc2String(file);
		
		}else if("xls".equalsIgnoreCase(type)){
		    
		    content += FileUtil.xls2String(file);
		    
		}
		return content;
	}
    
    /**
     * �ж��Ƿ�ΪĿ���ļ���Ŀǰ֧��txt xls doc��ʽ
     * @param fileName �ļ�����
     * @return ������ļ����������������������true�����򷵻�false
     */
    public static boolean isTxtFile(String fileName) {
        if (fileName.lastIndexOf(".txt") > 0) {
            return true;
        }else if (fileName.lastIndexOf(".xls") > 0) {
            return true;
        }else if (fileName.lastIndexOf(".doc") > 0) {
            return true;
        }
        return false;
    }
    
	/**
     * ��ȡtxt�ļ�������
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    public static String txt2String(File file){
        String result = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
            String s = null;
            while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
                result = result + "\n" +s;
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * ��ȡdoc�ļ�����
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    public static String doc2String(File file){
        String result = "";
        try{
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            Range rang = doc.getRange();
            result += rang.text();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * ��ȡxls�ļ�����
     * @param file ��Ҫ��ȡ���ļ�����
     * @return �����ļ�����
     */
    public static String xls2String(File file){
        String result = "";
        try{
            FileInputStream fis = new FileInputStream(file);   
            StringBuilder sb = new StringBuilder();   
            Workbook rwb = WorkbookFactory.create(fis); 
            int numberOfSheets = rwb.getNumberOfSheets();
            for(int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex ++) {
        		Sheet rs = rwb.getSheetAt(sheetIndex);
        		int firstRowNum = rs.getFirstRowNum();
        		int lastRowNum = rs.getLastRowNum();
        		for (int j = firstRowNum; j < lastRowNum; j++) {
        			Row row = rs.getRow(j);
        			short firstCellNum = row.getFirstCellNum();
        			short lastCellNum = row.getLastCellNum();
        			for(int k=firstCellNum;k<lastCellNum;k++)  {
        				Cell cell = row.getCell(k);
        				sb.append(getStringValue(cell));   
        			}
        		}   
            }
            fis.close();   
            result += sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    public static String getStringValue(Cell cell) {
    	String result = "";
    	int cellType = cell.getCellType();
    	switch (cellType) {
		case Cell.CELL_TYPE_BOOLEAN:
			result = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			result = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}
		return result;
    	
    }
}
